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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.StorageReference;
import com.google.firebase.storage.StorageReference;

public class ToPackFragment extends Fragment {
    private EditText addTextFilePath, editTextOldFilePath, editTextNewFilePath, editTextRemoveFilePath;
    private Button buttonAdd;
    private Button buttonEdit;
    private Button buttonDelete;

    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    //private StorageReference storageRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_documents, container, false);

        //set up text fields and buttons
        addTextFilePath = view.findViewById(R.id.add_document);
        editTextOldFilePath = view.findViewById(R.id.old_path);
        editTextNewFilePath = view.findViewById(R.id.new_path);
        editTextRemoveFilePath = view.findViewById(R.id.delete_path);
        buttonAdd = view.findViewById(R.id.add_button);
        buttonEdit = view.findViewById(R.id.replace_button);
        buttonDelete = view.findViewById(R.id.delete_button);

        db = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/");

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteItem();
            }
        });
        return view;
    }
    /*private void deleteItem() {
        String removePath = editTextRemoveFilePath.getText().toString().trim();


        if (removePath.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

// Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("images/desert.jpg");

// Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        /*
        String id = itemsRef.push().getKey();
        SafeLocation item = new SafeLocation(id, name, address, notes);

        itemsRef.child("location").child(id).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });

         */
    //}

}
