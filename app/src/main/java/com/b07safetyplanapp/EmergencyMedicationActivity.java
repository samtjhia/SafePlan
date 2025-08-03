
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

import com.b07safetyplanapp.models.emergencyinfo.Medication;

import java.util.ArrayList;
import java.util.List;

public class EmergencyMedicationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmergencyMedicationsAdapter adapter;
    private List<Medication> medicationList;
    private FloatingActionButton fabAdd;

    private FirebaseUser currentUser;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        setupFirebase();
        setupUI();
        loadMedications();
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
                .child("users").child(userId).child("medications");
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.recyclerViewMedications);
        fabAdd = findViewById(R.id.fabAddContact);

        medicationList = new ArrayList<>();
        adapter = new EmergencyMedicationsAdapter(medicationList, this::editContact, this::deleteContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddContactDialog());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
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

    private boolean isDuplicateName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return false;
            }



            if (medicationList == null || medicationList.isEmpty()) {
                return false;
            }

            for (Medication medication : medicationList) {
                if (medication == null || medication.getName() == null) {
                    continue;
                }



                String existingName = medication.getName();

                if (existingName.equals(name)) {
                    Toast.makeText(this, "Medication name already exists!", Toast.LENGTH_SHORT).show();
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
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_medication, null);

        EditText nameInput = dialogView.findViewById(R.id.etMedicationName);
        EditText relationshipInput = dialogView.findViewById(R.id.etDosage);


        builder.setView(dialogView);
        builder.setTitle("Add Medication");
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String dosage = relationshipInput.getText().toString().trim();


            if (name.isEmpty() || dosage.isEmpty() ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }



            if (isDuplicateName(name)){
                return;
            }


            saveMedication(name, dosage);
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void saveMedication(String name, String dosage) {
        String contactId = database.push().getKey();

        Medication medication = new Medication(
                name,
                dosage
        );

        database.child(contactId).setValue(medication)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Medication saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadMedications() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medicationList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Medication medication = snapshot.getValue(Medication.class);
                    if (medication != null) {
                        medicationList.add(medication);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EmergencyMedicationActivity.this, "Failed to load medications", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editContact(Medication medication) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_medication, null);

        EditText nameInput = dialogView.findViewById(R.id.etMedicationName);
        EditText dosageInput = dialogView.findViewById(R.id.etDosage);

        nameInput.setText(medication.getName());
        dosageInput.setText(medication.getDosage());


        builder.setView(dialogView)
                .setTitle("Edit Medication")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = nameInput.getText().toString().trim();
                    String newDosage = dosageInput.getText().toString().trim();

                    if (newName.isEmpty()) {
                        Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (isDuplicateName(newName)) {
                        return;
                    }


                    medication.setName(newName);
                    medication.setDosage(newDosage);


                    database.child(medication.getName()).setValue(medication)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Medication updated!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteContact(Medication medication) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Medication")
                .setMessage("Delete " + medication.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.child(medication.getName()).removeValue()
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
