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

/**
 * Activity for managing emergency safe locations.
 * <p>
 * This screen allows users to add, edit, and delete safe locations associated with their profile.
 * Data is persisted in Firebase Realtime Database under the authenticated user's node.
 */
public class EmergencySafeLocationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmergencySafeLocationsAdapter adapter;
    private List<SafeLocation> safeLocationList;
    private FloatingActionButton fabAdd;
    private FirebaseUser currentUser;
    private DatabaseReference database;

    /**
     * Initializes the activity and sets up Firebase and UI components.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_safe_location);
        setupFirebase();
        setupUI();
        loadSafeLocations();
    }


    @Override
    public void finish() {
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

    /**
     * Normalizes address strings for comparison.
     *
     * @param address the address string
     * @return the normalized (lowercase, trimmed) address
     */
    private String normalizeAddress(String address) {
        return address.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    /**
     * Checks if the provided address already exists in the list, excluding a specific ID if needed.
     *
     * @param address              the address to check
     * @param excludeSafeLocationId the ID to exclude from comparison (for updates)
     * @return true if duplicate exists, false otherwise
     */
    private boolean isDuplicateAddress(String address, String excludeSafeLocationId) {
        try {
            if (address == null || address.trim().isEmpty() || safeLocationList == null || safeLocationList.isEmpty()) {
                return false;
            }

            for (SafeLocation location : safeLocationList) {
                if (location == null || location.getAddress() == null) continue;
                if (excludeSafeLocationId != null && excludeSafeLocationId.equals(location.getId())) continue;

                if (normalizeAddress(location.getAddress()).equals(normalizeAddress(address))) {
                    Toast.makeText(this, "This address is already being used!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return false;
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

            if (isDuplicateAddress(address, null)) return;

            saveSafeLocation(name, address, notes);
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /**
     * Saves a new safe location to Firebase.
     *
     * @param name    the name of the location
     * @param address the address of the location
     * @param notes   any notes for the location
     */
    private void saveSafeLocation(String name, String address, String notes) {
        String id = database.push().getKey();
        SafeLocation location = new SafeLocation(name, address, notes, id);

        database.child(id).setValue(location)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Safe Location saved!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show());
    }


    private void loadSafeLocations() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                safeLocationList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    SafeLocation location = child.getValue(SafeLocation.class);
                    if (location != null) safeLocationList.add(location);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EmergencySafeLocationsActivity.this, "Failed to load safe locations", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void editSafeLocation(SafeLocation location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_safe_location, null);

        EditText nameInput = dialogView.findViewById(R.id.etSafeLocationName);
        EditText addressInput = dialogView.findViewById(R.id.SafeLocationAddress);
        EditText notesInput = dialogView.findViewById(R.id.etSafeLocationNotes);

        nameInput.setText(location.getName());
        addressInput.setText(location.getAddress());
        notesInput.setText(location.getNotes());

        builder.setView(dialogView)
                .setTitle("Edit Emergency Safe Location")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = nameInput.getText().toString().trim();
                    String newAddress = addressInput.getText().toString().trim();
                    String newNotes = notesInput.getText().toString().trim();

                    if (newName.isEmpty() || newAddress.isEmpty()) {
                        Toast.makeText(this, "Name and address are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isDuplicateAddress(newAddress, location.getId())) return;

                    location.setName(newName);
                    location.setAddress(newAddress);
                    location.setNotes(newNotes);

                    database.child(location.getId()).setValue(location)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Safe Location updated!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void deleteSafeLocation(SafeLocation location) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Safe Location")
                .setMessage("Delete " + location.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.child(location.getId()).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Safe Location deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show());
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
