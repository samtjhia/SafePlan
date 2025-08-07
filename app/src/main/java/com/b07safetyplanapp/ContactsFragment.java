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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Fragment that allows users to add, edit, or delete emergency contacts
 * stored in the Firebase Realtime Database.
 */

public class ContactsFragment extends Fragment {
    private EditText editTextName, editTextRelationship, editTextPhone;

    private Button buttonAdd;
    private Button buttonEdit;
    private Button buttonDelete;

    //private FirebaseDatabase db;




    private DatabaseReference db;



    /**
     * Inflates the contact management view and sets up the database reference
     * and button click listeners for add, edit, and delete actions.
     *
     * @param inflater the LayoutInflater object used to inflate views
     * @param container the parent ViewGroup for the fragment's UI
     * @param savedInstanceState the previously saved state, if any
     * @return the inflated root view
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_contact, container, false);
        editTextName = view.findViewById(R.id.editTextName);
        editTextRelationship = view.findViewById(R.id.editTextRelationship);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        //db = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/");
        String userId = "userId123"; // hardcoded for testing

        db = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("emergency_contacts");



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

    /**
     * Adds a new contact to the Firebase Realtime Database after checking for duplicates.
     *
     * @throws IllegalStateException if any of the input fields are empty
     */
    private void addItem() {
        String name = editTextName.getText().toString().trim();
        String relationship = editTextRelationship.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (name.isEmpty() || relationship.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        db.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contact item = snapshot.getValue(Contact.class);
                    if (item != null && item.getPhone().equalsIgnoreCase(phone) ) {

                        Toast.makeText(getContext(), "Number "+phone+" is currently being used under contact: "+item.getName()+".", Toast.LENGTH_SHORT).show();

                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    Contact item = new Contact(name, relationship, phone);

                    db.child(name).setValue(item).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Contact added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to add contact", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Edits an existing contact in the Firebase Realtime Database based on the provided name.
     *
     * @throws IllegalStateException if any of the input fields are empty
     */
    private void editItem(){
        String name = editTextName.getText().toString().trim();
        String relationship = editTextRelationship.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (name.isEmpty() || relationship.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contact item = snapshot.getValue(Contact.class);
                    if (item != null && item.getName().equalsIgnoreCase(name) ) {
                        Contact contact = new Contact(name, relationship, phone);

                        db.child(name).setValue(contact).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Contact Edited", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to edit contact", Toast.LENGTH_SHORT).show();
                            }
                        });
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    Toast.makeText(getContext(), "Contact not found. Please enter the name of an existing contact.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Deletes a contact from the Firebase Realtime Database if all fields match.
     *
     * @throws IllegalStateException if any of the input fields are empty
     */
    private void deleteItem() {
        String name = editTextName.getText().toString().trim();
        String relationship = editTextRelationship.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (name.isEmpty() || relationship.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contact item = snapshot.getValue(Contact.class);
                    if (item != null && item.getName().equalsIgnoreCase(name) && item.getRelationship().equalsIgnoreCase(relationship)&& item.getPhone().equalsIgnoreCase(phone)) {
                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Contact deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to delete contact", Toast.LENGTH_SHORT).show();
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
