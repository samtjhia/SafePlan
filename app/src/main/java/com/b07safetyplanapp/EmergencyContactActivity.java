package com.b07safetyplanapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import java.util.UUID;

import com.b07safetyplanapp.R;

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

    private void setupFirebase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

//        if (currentUser == null) {
//            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }

        // below should be what we are doing once we have user authentication
//         String userId = currentUser.getUid();
        String userId = "userId123"; // hardcoded for testing

        database = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("emergencyContacts");
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.recyclerViewContacts);
        fabAdd = findViewById(R.id.fabAddContact);

        contactsList = new ArrayList<>();
        adapter = new EmergencyContactAdapter(contactsList, this::callContact, this::editContact, this::deleteContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddContactDialog());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
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

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.isEmpty()) {
                Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            saveContact(name, relationship, phone);
        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void saveContact(String name, String relationship, String phone) {
        String contactId = UUID.randomUUID().toString();

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

    private void callContact(EmergencyContact contact) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contact.getPhone()));

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot make call", Toast.LENGTH_SHORT).show();
        }
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

                    if (!newName.isEmpty() && !newPhone.isEmpty()) {
                        contact.setName(newName);
                        contact.setRelationship(newRelationship);
                        contact.setPhone(newPhone);

                        database.child(contact.getId()).setValue(contact)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Contact updated!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Name and phone are required", Toast.LENGTH_SHORT).show();
                    }
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
//        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//            Toast.makeText(this, "Session expired, please log in again", Toast.LENGTH_SHORT).show();
//            finish();
//        }
    }
}