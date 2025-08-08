package com.b07safetyplanapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.documentfile.provider.DocumentFile;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.b07safetyplanapp.models.emergencyinfo.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentActivity extends AppCompatActivity {

    private static final String TAG = "DocumentActivity";

    // UI Components
    private RecyclerView recyclerView;
    private DocumentAdapter adapter;
    private List<Document> documentsList;
    private FloatingActionButton fabAdd;

    // Firebase
    private FirebaseUser currentUser;
    private DatabaseReference database;

    // File handling
    private File documentsDir;
    private ActivityResultLauncher<Intent> filePicker;
    private Uri selectedFileUri;

    // Dialog components
    private AlertDialog addDocumentDialog;
    private EditText titleInput;
    private EditText descriptionInput;
    private TextView selectedFileText;
    private Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        // Check if user is logged in
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupFirebase();
        setupFileStorage();
        setupUI();
        setupFilePicker();
        loadDocuments();
    }

    @Override
    public void finish() { // back animation
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setupFirebase() {
        String userId = currentUser.getUid();
        database = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("documents");
    }

    private void setupFileStorage() {
        documentsDir = new File(getFilesDir(), "documents");
        if (!documentsDir.exists()) {
            documentsDir.mkdirs();
        }
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.recyclerViewDocuments);
        fabAdd = findViewById(R.id.fabAddDocument);

        documentsList = new ArrayList<>();
        adapter = new DocumentAdapter(documentsList,
                this::openDocument,
                this::editDocument,
                this::deleteDocument);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddDocumentDialog());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void setupFilePicker() {
        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedFileUri = result.getData().getData();
                        if (selectedFileUri != null) {
                            String fileName = getFileName(selectedFileUri);
                            selectedFileText.setText("Selected: " + fileName);
                            selectedFileText.setVisibility(View.VISIBLE);
                            uploadButton.setEnabled(true);
                        }
                    }
                }
        );
    }

    private void showAddDocumentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_document, null);

        // Get dialog components
        titleInput = dialogView.findViewById(R.id.etDocumentTitle);
        descriptionInput = dialogView.findViewById(R.id.etDocumentDescription);
        Button selectFileButton = dialogView.findViewById(R.id.btnSelectFile);
        selectedFileText = dialogView.findViewById(R.id.tvSelectedFileName);
        uploadButton = dialogView.findViewById(R.id.btnUploadDocument);

        // Reset dialog
        selectedFileUri = null;
        uploadButton.setEnabled(false);
        selectedFileText.setVisibility(View.GONE);

        // Set up buttons
        selectFileButton.setOnClickListener(v -> selectFile());
        uploadButton.setOnClickListener(v -> uploadDocument());

        builder.setView(dialogView)
                .setTitle("Add Document")
                .setNegativeButton("Cancel", null);

        addDocumentDialog = builder.create();
        addDocumentDialog.show();
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePicker.launch(Intent.createChooser(intent, "Select Document"));
    }

    private void uploadDocument() {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        // Check all fields filled
        if (title.isEmpty()) {
            titleInput.setError("Please enter a title");
            return;
        }

        if (selectedFileUri == null) {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button and show progress
        uploadButton.setEnabled(false);
        uploadButton.setText("Uploading...");
        Toast.makeText(this, "Saving document...", Toast.LENGTH_SHORT).show();

        // Save file
        saveDocument(title, description);
    }

    private void saveDocument(String title, String description) {
        new Thread(() -> {
            try {
                // Create unique file name
                String fileId = UUID.randomUUID().toString();
                String originalFileName = getFileName(selectedFileUri);
                String extension = getFileExtension(originalFileName);
                String localFileName = fileId + "." + extension;

                // Copy file to app storage
                File localFile = new File(documentsDir, localFileName);
                copyFile(selectedFileUri, localFile);

                Document document = new Document( // Save to Firebase
                        fileId,
                        title,
                        description,
                        localFile.getAbsolutePath(),
                        System.currentTimeMillis()
                );

                runOnUiThread(() -> saveToFirebase(document, originalFileName));

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error when saving file", Toast.LENGTH_SHORT).show();
                    resetUploadButton();
                });
            }
        }).start();
    }

    /**
     * Copies the content of the selected file to a local file on the device.
     *
     * @param sourceUri the source URI of the selected file
     * @param destinationFile the file to which the content will be saved
     * @throws Exception if an I/O error occurs during copy
     */
    private void copyFile(Uri sourceUri, File destinationFile) throws Exception {
        try (InputStream input = getContentResolver().openInputStream(sourceUri);
             FileOutputStream output = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        }
    }

    /**
     * Saves the document metadata to Firebase.
     *
     * @param document the document metadata
     * @param originalFileName the original name of the uploaded file
     */
    private void saveToFirebase(Document document, String originalFileName) {
        database.child(document.getId()).setValue(document)
                .addOnSuccessListener(aVoid -> {
                    database.child(document.getId()).child("originalFileName").setValue(originalFileName);

                    addDocumentDialog.dismiss();
                    Toast.makeText(this, "Document saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save document", Toast.LENGTH_SHORT).show();
                    resetUploadButton();

                    new File(document.getDownloadUrl()).delete();
                });
    }

    private void resetUploadButton() {
        uploadButton.setEnabled(true);
        uploadButton.setText("Upload Document");
    }

    private void loadDocuments() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                documentsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Document document = snapshot.getValue(Document.class);
                    if (document != null) {
                        File localFile = new File(document.getDownloadUrl()); // check file exists
                        if (localFile.exists()) {
                            documentsList.add(document);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DocumentActivity.this, "Cannot load documents", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Opens a document using the appropriate external app.
     *
     * @param document the document to open
     * @throws Exception if the file cannot be opened
     */
    private void openDocument(Document document) {
        try {
            File file = new File(document.getDownloadUrl());

            if (!file.exists()) {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri fileUri = androidx.core.content.FileProvider.getUriForFile(
                    this, getPackageName() + ".fileprovider", file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, getFileType(file.getName()));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Open with"));

        } catch (Exception e) {
            Toast.makeText(this, "Cannot open document", Toast.LENGTH_SHORT).show();
        }
    }

    private void editDocument(Document document) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_document, null);

        EditText titleInput = dialogView.findViewById(R.id.etDocumentTitle);
        EditText descriptionInput = dialogView.findViewById(R.id.etDocumentDescription);

        titleInput.setText(document.getTitle());
        descriptionInput.setText(document.getDescription());

        // Hide file selection components
        // because it doesn't make sense for user to upload a new file;
        // just create a new file
        dialogView.findViewById(R.id.btnSelectFile).setVisibility(View.GONE);
        dialogView.findViewById(R.id.btnUploadDocument).setVisibility(View.GONE);
        dialogView.findViewById(R.id.tvSelectedFileName).setVisibility(View.GONE);

        builder.setView(dialogView)
                .setTitle("Edit Document")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newTitle = titleInput.getText().toString().trim();
                    String newDescription = descriptionInput.getText().toString().trim();

                    if (newTitle.isEmpty()) {
                        Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Update document
                    document.setTitle(newTitle);
                    document.setDescription(newDescription);

                    database.child(document.getId()).setValue(document)
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(this, "Document updated!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteDocument(Document document) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Document")
                .setMessage("Are you sure you want to delete \"" + document.getTitle() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete local file
                    File localFile = new File(document.getDownloadUrl());
                    if (localFile.exists()) {
                        localFile.delete();
                    }

                    // Delete from Firebase
                    database.child(document.getId()).removeValue()
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(this, "Document deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private String getFileName(Uri uri) {
        DocumentFile documentFile = DocumentFile.fromSingleUri(this, uri);
        if (documentFile != null && documentFile.getName() != null) {
            return documentFile.getName();
        }
        return "unknown_file";
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "txt";
    }

    private String getFileType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();

        if (extension.equals("pdf")) {
            return "application/pdf";
        } else if (extension.equals("jpg") || extension.equals("jpeg")) {
            return "image/jpeg";
        } else if (extension.equals("png")) {
            return "image/png";
        } else if (extension.equals("txt")) {
            return "text/plain";
        } else if (extension.equals("doc")) {
            return "application/msword";
        } else if (extension.equals("docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (extension.equals("mp3")) {
            return "audio/mpeg";
        } else if (extension.equals("mp4")) {
            return "video/mp4";
        } else {
            return "*/*";
        }
    }
}