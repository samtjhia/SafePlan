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

import com.b07safetyplanapp.models.emergencyinfo.SafeLocation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmergencySafeLocationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmergencySafeLocationsAdapter adapter;
    private List<SafeLocation> safeLocationList;
    private FloatingActionButton fabAdd;

    private FirebaseUser currentUser;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_safe_location);
        setupFirebase();
        setupUI();
        loadSafeLocations();
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
                .child("users").child(userId).child("safe_locations");
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.recyclerViewSafeLocations);
        fabAdd = findViewById(R.id.fabAddSafeLocation);

        safeLocationList = new ArrayList<>();
        adapter = new EmergencySafeLocationsAdapter(safeLocationList, this::editSafeLocation, this::deleteSafeLocation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddSafeLocationDialog());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }



    private String normalizeAddress(String address) {
        return address.trim().replaceAll("\\s+", " ").toLowerCase();
    }


    private boolean isDuplicateAddress(String address, String excludeSafeLocationId) {
        try {
            if (address == null || address.trim().isEmpty()) {
                return false;
            }



            if (safeLocationList == null || safeLocationList.isEmpty()) {
                return false;
            }

            for (SafeLocation safe_location : safeLocationList) {
                if (safe_location == null || safe_location.getAddress() == null) {
                    continue;
                }

                String safe_locationId = safe_location.getId();
                if (excludeSafeLocationId != null && safe_locationId != null && safe_locationId.equals(excludeSafeLocationId)) {
                    continue;
                }

                String existingAddress = normalizeAddress(safe_location.getAddress());
                String currentAddress = normalizeAddress(address);
                if (existingAddress.equals(currentAddress)) {
                    Toast.makeText(this, "This address is already being used!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return false; // Allow saving if there's an error
        }
    }

    private void showAddSafeLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_safe_location, null);

        EditText nameInput = dialogView.findViewById(R.id.etSafeLocationName);
        EditText addressInput = dialogView.findViewById(R.id.SafeLocationAddress);
        EditText notesInput = dialogView.findViewById(R.id.etSafeLocationNotes);

        builder.setView(dialogView);
        builder.setTitle("Add Emergency Safe Location");
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String address = addressInput.getText().toString().trim();
            String notes = notesInput.getText().toString().trim();

            if (name.isEmpty() || address.isEmpty() || notes.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isDuplicateAddress(address, null)) {
                return;
            }


            saveSafeLocation(name, address, notes);
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void saveSafeLocation(String name, String address, String notes) {
        String safeLocationId = database.push().getKey();

        SafeLocation safe_location = new SafeLocation(
                name,
                address,
                notes,
                safeLocationId
        );

        database.child(safeLocationId).setValue(safe_location)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Safe Location saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadSafeLocations() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                safeLocationList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SafeLocation safe_location = snapshot.getValue(SafeLocation.class);
                    if (safe_location != null) {
                        safeLocationList.add(safe_location);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EmergencySafeLocationsActivity.this, "Failed to load safe locations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editSafeLocation(SafeLocation safe_location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_safe_location, null);

        EditText nameInput = dialogView.findViewById(R.id.etSafeLocationName);
        EditText addressInput = dialogView.findViewById(R.id.SafeLocationAddress);
        EditText noteInput = dialogView.findViewById(R.id.etSafeLocationNotes);

        nameInput.setText(safe_location.getName());
        addressInput.setText(safe_location.getAddress());
        noteInput.setText(safe_location.getNotes());

        builder.setView(dialogView)
                .setTitle("Edit Emergency Safe Location")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = nameInput.getText().toString().trim();
                    String newAddress = addressInput.getText().toString().trim();
                    String newNote = noteInput.getText().toString().trim();

                    if (newName.isEmpty() || newAddress.isEmpty()) {
                        Toast.makeText(this, "Name and address are required", Toast.LENGTH_SHORT).show();
                        return;
                    }



                    if (isDuplicateAddress(newAddress, safe_location.getId())) {
                        return;
                    }


                    safe_location.setName(newName);
                    safe_location.setAddress(newAddress);
                    safe_location.setNotes(newNote);

                    database.child(safe_location.getId()).setValue(safe_location)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Safe Location updated!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteSafeLocation(SafeLocation safe_location) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Safe Location")
                .setMessage("Delete " + safe_location.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.child(safe_location.getId()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Safe Location deleted", Toast.LENGTH_SHORT).show();
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