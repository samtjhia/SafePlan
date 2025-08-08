package com.b07safetyplanapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PrivacyControlActivity extends AppCompatActivity {


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_control);

        //Delete button creation
        Button deleteButton = findViewById(R.id.btnDeleteAccount);
        deleteButton.setOnClickListener(v -> promptForReauth());

        //Listener for back button from LinearLayout
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        //findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    //Pop-up window fo re-authorization
    private void promptForReauth() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "No user signed in.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Re-authentication Required");

        final android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);

        final android.widget.EditText emailInput = new android.widget.EditText(this);
        emailInput.setHint("Email");
        emailInput.setText(user.getEmail()); // Autofill
        layout.addView(emailInput);

        final android.widget.EditText passwordInput = new android.widget.EditText(this);
        passwordInput.setHint("Password");
        passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        builder.setView(layout);

        builder.setPositiveButton("Continue", (dialog, which) -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            user.reauthenticate(credential)
                    .addOnSuccessListener(aVoid -> showConfirmationDialog())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Re-authentication failed.", Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    //Confirmation Dialog
    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account & Data")
                .setMessage("This will permanently delete your account and all associated data. Continue?")
                .setPositiveButton("Delete", (dialog, which) -> deleteUserData())
                .setNegativeButton("Cancel", null)
                .show();
    }

    //Deleting user's DATA from DB
    private void deleteUserData() {
        //Getting user's instance (uid)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //No user signed-in
        if (user == null) {
            Toast.makeText(this, "No user is signed in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();
        DatabaseReference userRef = FirebaseDatabase
                .getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/")
                .getReference("users")
                .child(uid);
        //remobing user and all user's children
        userRef.removeValue()
                .addOnSuccessListener(aVoid -> deleteAuthUser())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to delete user data from database.", Toast.LENGTH_SHORT).show());
    }


    //Flow for delete fro user
    /**
     * Deletes the user's Firebase Authentication account.
     * Finishes the app session afterward using `finishAffinity()`.
     */
    private void deleteAuthUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(this, "Account and data deleted.", Toast.LENGTH_LONG).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to delete user account.", Toast.LENGTH_SHORT).show());

            finishAffinity(); // Close the app or return to Welcome/Login
        }
    }
}
