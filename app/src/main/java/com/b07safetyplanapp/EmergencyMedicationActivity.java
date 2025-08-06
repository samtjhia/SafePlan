
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
        fabAdd = findViewById(R.id.fabAddMedication);

        medicationList = new ArrayList<>();
        adapter = new EmergencyMedicationsAdapter(medicationList, this::editMedication, this::deleteMedication);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddMedicationDialog());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private String normalizeName(String address) {
        return address.trim().replaceAll("\\s+", " ").toLowerCase();
    }
    private boolean isDuplicateName(String name, String excludeMedicationId) {
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


                String medicationId = medication.getMedicationId();

                if (excludeMedicationId != null && medicationId != null && medicationId.equals(excludeMedicationId)) {
                    continue;
                }
                String existingName = normalizeName(medication.getName());
                String currentName = normalizeName(name);
                if (existingName.equals(currentName)) {
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

    private void showAddMedicationDialog() {
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



            if (isDuplicateName(name,null)){
                return;
            }


            saveMedication(name, dosage);
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void saveMedication(String name, String dosage) {
        String medicationId = database.push().getKey();

        Medication medication = new Medication(
                name,
                dosage,
                medicationId
        );

        database.child(medicationId).setValue(medication)
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

    private void editMedication(Medication medication) {
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


                    //if (isDuplicateName(newName, medication.getMedicationId())) {
                      //  return;

                    //}


                    medication.setName(newName);
                    medication.setDosage(newDosage);


                    database.child(medication.getMedicationId()).setValue(medication)
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

    private void deleteMedication(Medication medication) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Medication")
                .setMessage("Delete " + medication.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.child(medication.getMedicationId()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Medication deleted", Toast.LENGTH_SHORT).show();
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
