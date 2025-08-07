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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import com.b07safetyplanapp.models.emergencyinfo.SafeLocation;

import java.util.UUID;

/**
 * Fragment for managing safe locations within the Safety Plan App.
 * Allows the user to add, edit, and delete entries to the Firebase Realtime Database.
 */
public class SafeLocationsFragment extends Fragment {
    private EditText editTextName, editTextAddress, editTextNotes;

    private Button buttonAdd;
    private Button buttonEdit;
    private Button buttonDelete;

    private FirebaseUser currentUser;

    //private FirebaseDatabase db;
    private DatabaseReference db;

    /**
     * Initializes the SafeLocationsFragment view with UI components and sets up click listeners for add, edit, and delete actions.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_safe_location, container, false);
        //set up text fields and buttons
        editTextName = view.findViewById(R.id.editTextName);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextNotes = view.findViewById(R.id.editTextNotes);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        //db = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/");
//        String userId = "userId123"; // hardcoded for testing
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        db = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("safe_locations");

        //storage = FirebaseStorage.getInstance().getReference()
          //      .child("documents").child(userId);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

        return view;

    }


    private void addItem() {
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String notes = editTextNotes.getText().toString().trim();
        String id = UUID.randomUUID().toString();

        if (name.isEmpty() || address.isEmpty() || notes.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        //String id = itemsRef.push().getKey();
        SafeLocation item = new SafeLocation(name, address, notes, id);

        db.child(name).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Safe Location added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add safe location", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void editItem(){
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String notes = editTextNotes.getText().toString().trim();
        String id = UUID.randomUUID().toString();

        if (name.isEmpty() || address.isEmpty() || notes.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SafeLocation item = snapshot.getValue(SafeLocation.class);
                    if (item != null && item.getName().equalsIgnoreCase(name) ) {
                        SafeLocation location = new SafeLocation(name, address, notes, id);

                        db.child(name).setValue(location).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Safe location Edited", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to edit safe location", Toast.LENGTH_SHORT).show();
                            }
                        });
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    Toast.makeText(getContext(), "Safe location not found. Please enter the name of an existing safe location.", Toast.LENGTH_SHORT).show();
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
        String address = editTextAddress.getText().toString().trim();
        String notes = editTextNotes.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || notes.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        db.addListenerForSingleValueEvent(new ValueEventListener() {
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            boolean itemFound = false;
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                SafeLocation item = snapshot.getValue(SafeLocation.class);
                if (item != null && item.getName().equalsIgnoreCase(name) && item.getAddress().equalsIgnoreCase(address)) {
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
