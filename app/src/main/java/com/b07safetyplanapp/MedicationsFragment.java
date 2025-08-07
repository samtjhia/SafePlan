package com.b07safetyplanapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.b07safetyplanapp.models.emergencyinfo.Medication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

/**
 * Fragment for managing user medications.
 * <p>
 * Users can add, edit, or delete medications that are stored in Firebase Realtime Database
 * under their unique user ID.
 */
public class MedicationsFragment extends Fragment {

    private EditText editTextName, editTextDosage;
    private Button buttonAdd, buttonEdit, buttonDelete;

    private FirebaseUser currentUser;
    private DatabaseReference db;

    /**
     * Called to create the fragment's UI view.
     *
     * @param inflater           the LayoutInflater used to inflate views
     * @param container          the parent view this fragment's UI should be attached to
     * @param savedInstanceState saved state for restoration, if applicable
     * @return the view hierarchy associated with this fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_medication, container, false);

        editTextName = view.findViewById(R.id.editTextName);
        editTextDosage = view.findViewById(R.id.editTextDosage);

        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            db = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(userId).child("medications");
        }

        buttonAdd.setOnClickListener(v -> addItem());
        buttonEdit.setOnClickListener(v -> editItem());
        buttonDelete.setOnClickListener(v -> deleteItem());

        return view;
    }


    private void addItem() {
        String name = editTextName.getText().toString().trim();
        String dosage = editTextDosage.getText().toString().trim();
        String id = UUID.randomUUID().toString();

        if (name.isEmpty() || dosage.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Medication item = new Medication(name, dosage, id);
        db.child(name).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void editItem() {
        String name = editTextName.getText().toString().trim();
        String dosage = editTextDosage.getText().toString().trim();
        String id = UUID.randomUUID().toString();

        if (name.isEmpty() || dosage.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Medication item = snapshot.getValue(Medication.class);
                    if (item != null && item.getName().equalsIgnoreCase(name)) {
                        Medication medication = new Medication(name, dosage, id);
                        db.child(name).setValue(medication).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Medication Edited", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to edit medication", Toast.LENGTH_SHORT).show();
                            }
                        });
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    Toast.makeText(getContext(), "Medication not found. Please enter the name of an existing medication.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem() {
        String name = editTextName.getText().toString().trim();
        String dosage = editTextDosage.getText().toString().trim();

        if (name.isEmpty() || dosage.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Medication item = snapshot.getValue(Medication.class);
                    if (item != null &&
                            item.getName().equalsIgnoreCase(name) &&
                            item.getDosage().equalsIgnoreCase(dosage)) {

                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                            }
                        });
                        itemFound = true;
                        break;
                    }
                }

                if (!itemFound) {
                    Toast.makeText(getContext(), "Item not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
