package com.b07safetyplanapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.b07safetyplanapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Fragment that allows users to delete an item from Firebase by specifying the title
 * and selecting a category.
 */

public class DeleteItemFragment extends Fragment {
    private EditText editTextTitle;
    private Spinner spinnerCategory;
    private Button buttonDelete;

    private FirebaseDatabase db;
    private DatabaseReference itemsRef;



    /**
     * Inflates the delete item layout and initializes input fields, spinner,
     * and delete button listener.
     *
     * @param inflater the LayoutInflater object used to inflate views
     * @param container the parent ViewGroup that holds the fragment UI
     * @param savedInstanceState the saved state bundle, if any
     * @return the root view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_item, container, false);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        db = FirebaseDatabase.getInstance("https://b07-demo-summer-2024-default-rtdb.firebaseio.com/");

        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemByTitle();
            }
        });

        return view;
    }


    /**
     * Deletes an item from the Firebase database by matching the entered title
     * under the selected category.
     *
     * @throws IllegalStateException if the title field is empty
     */
    private void deleteItemByTitle() {
        String title = editTextTitle.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().toLowerCase();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter item title", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsRef = db.getReference("categories/" + category);
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null && item.getTitle().equalsIgnoreCase(title)) {
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
