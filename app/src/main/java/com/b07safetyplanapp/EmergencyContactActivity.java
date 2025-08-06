package com.b07safetyplanapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import com.b07safetyplanapp.models.emergencyinfo.EmergencyContact;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmergencyContactAdapter adapter;
    private List<EmergencyContact> contactsList;
    private FloatingActionButton fabAdd;

    private FirebaseUser currentUser;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        setupFirebase();
        setupUI();
        loadContacts();
    }

    @Override
    public void finish() { // back animation
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                .child("users").child(userId).child("emergency_contacts");
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.recyclerViewContacts);
        fabAdd = findViewById(R.id.fabAddContact);

        contactsList = new ArrayList<>();
        adapter = new EmergencyContactAdapter(contactsList, this::editContact, this::deleteContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddContactDialog());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private String normalizePhoneNumber(String phone) {
        if (phone == null) {
            return "";
        }

        // Remove spaces and dashes
        String cleaned = phone.replaceAll("[\\s-]", "");

        return cleaned;
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        String normalizedPhone = normalizePhoneNumber(phone);

        if (normalizedPhone.length() != 10) {
            Toast.makeText(this, "Phone number must be 10 digits long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!normalizedPhone.matches("\\d{10}")) {
            Toast.makeText(this, "Phone number can only contain digits, spaces, and dashes", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isDuplicatePhone(String phone, String excludeContactId) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                return false;
            }

            String normalizedPhone = normalizePhoneNumber(phone);

            if (contactsList == null || contactsList.isEmpty()) {
                return false;
            }

            for (EmergencyContact contact : contactsList) {
                if (contact == null || contact.getPhone() == null) {
                    continue;
                }

                String contactId = contact.getId();
                if (excludeContactId != null && contactId != null && contactId.equals(excludeContactId)) {
                    continue;
                }

                String existingPhone = normalizePhoneNumber(contact.getPhone());

                if (existingPhone.equals(normalizedPhone)) {
                    Toast.makeText(this, "Phone number already exists!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return false; // Allow saving if there's an error
        }
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_emergency_contact, null);

        EditText nameInput = dialogView.findViewById(R.id.etContactName);
        EditText relationshipInput = dialogView.findViewById(R.id.etContactRelationship);
        EditText phoneInput = dialogView.findViewById(R.id.etContactPhone);

        builder.setView(dialogView);
        builder.setTitle("Add Emergency Contact");
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String relationship = relationshipInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();

            if (name.isEmpty() || relationship.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPhoneNumber(phone)) {
                return;
            }

            if (isDuplicatePhone(phone, null)) {
                return;
            }

            String normalizedPhone = normalizePhoneNumber(phone);
            saveContact(name, relationship, normalizedPhone);
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void saveContact(String name, String relationship, String phone) {
        String contactId = database.push().getKey();

        EmergencyContact contact = new EmergencyContact(
                contactId,
                name,
                relationship,
                phone
        );

        database.child(contactId).setValue(contact)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Contact saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadContacts() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EmergencyContact contact = snapshot.getValue(EmergencyContact.class);
                    if (contact != null) {
                        contactsList.add(contact);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EmergencyContactActivity.this, "Failed to load contacts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editContact(EmergencyContact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_emergency_contact, null);

        EditText nameInput = dialogView.findViewById(R.id.etContactName);
        EditText relationshipInput = dialogView.findViewById(R.id.etContactRelationship);
        EditText phoneInput = dialogView.findViewById(R.id.etContactPhone);

        nameInput.setText(contact.getName());
        relationshipInput.setText(contact.getRelationship());
        phoneInput.setText(contact.getPhone());

        builder.setView(dialogView)
                .setTitle("Edit Emergency Contact")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = nameInput.getText().toString().trim();
                    String newRelationship = relationshipInput.getText().toString().trim();
                    String newPhone = phoneInput.getText().toString().trim();

                    if (newName.isEmpty() || newPhone.isEmpty()) {
                        Toast.makeText(this, "Name and phone are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!isValidPhoneNumber(newPhone)) {
                        return;
                    }

                    if (isDuplicatePhone(newPhone, contact.getId())) {
                        return;
                    }

                    String normalizedPhone = normalizePhoneNumber(newPhone);
                    contact.setName(newName);
                    contact.setRelationship(newRelationship);
                    contact.setPhone(normalizedPhone);

                    database.child(contact.getId()).setValue(contact)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Contact updated!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteContact(EmergencyContact contact) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Delete " + contact.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.child(contact.getId()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
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