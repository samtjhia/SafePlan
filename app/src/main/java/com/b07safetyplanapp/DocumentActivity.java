package com.b07safetyplanapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.b07safetyplanapp.models.emergencyinfo.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DocumentAdapter adapter;
    private List<Document> documentsList;
    private FloatingActionButton fabAdd;
    private FirebaseUser currentUser;
    private DatabaseReference database;
    private StorageReference storage;

    private ActivityResultLauncher<Intent> filePicker;

    private String currentTitle;
    private String currentDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        setupFirebase();
        setupUI();
        setupFilePicker();
        loadDocuments();
    }

    private void setupFirebase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();
//        String userId = "userId123"; // hardcoded for testing

        database = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("documents");

        storage = FirebaseStorage.getInstance().getReference()
                .child("documents").child(userId);
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.recyclerViewDocuments);
        fabAdd = findViewById(R.id.fabAddDocument);

        documentsList = new ArrayList<>();
        adapter = new DocumentAdapter(documentsList, this::viewDocument, this::editDocument, this::deleteDocument);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddDocumentDialog());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupFilePicker() {
        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        if (fileUri != null) {
                            uploadFile(fileUri);
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(this, "File selection canceled", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void showAddDocumentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_document, null);

        EditText titleInput = dialogView.findViewById(R.id.etDocumentTitle);
        EditText descriptionInput = dialogView.findViewById(R.id.etDocumentDescription);
        Button selectFileButton = dialogView.findViewById(R.id.btnSelectFile);

        builder.setView(dialogView);
        builder.setTitle("Add Document");
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();

        selectFileButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();

            if (title.isEmpty()) {
                titleInput.setError("Please enter a title");
                return;
            }

            currentTitle = title;
            currentDescription = description;

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            String[] mimeTypes = {"image/*", "application/pdf", "text/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

            Intent chooser = Intent.createChooser(intent, "Select Document");
            filePicker.launch(chooser);

            dialog.dismiss();
        });

        dialog.show();
    }

    private void uploadFile(Uri fileUri) {
        if (currentUser == null) {
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();

        String fileId = UUID.randomUUID().toString();
        StorageReference fileRef = storage.child(fileId);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                        saveDocumentInfo(fileId, downloadUrl.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveDocumentInfo(String fileId, String downloadUrl) {
        Document document = new Document(
                fileId,
                currentTitle,
                currentDescription,
                downloadUrl,
                System.currentTimeMillis()
        );

        database.child(fileId).setValue(document)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Document saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadDocuments() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                documentsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Document document = snapshot.getValue(Document.class);
                    if (document != null) {
                        documentsList.add(document);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DocumentActivity.this, "Failed to load documents", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewDocument(Document document) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(document.getDownloadUrl()));

        try {
            startActivity(intent);
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

        builder.setView(dialogView)
                .setTitle("Edit Document")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newTitle = titleInput.getText().toString().trim();
                    String newDescription = descriptionInput.getText().toString().trim();

                    if (newTitle.isEmpty()) {
                        Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

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
                .setMessage("Delete " + document.getTitle() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    storage.child(document.getId()).delete();
                    database.child(document.getId()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Document deleted", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}