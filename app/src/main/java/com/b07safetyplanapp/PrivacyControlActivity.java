package com.b07safetyplanapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PrivacyControlActivity extends AppCompatActivity {

    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_control); // Make sure this layout file exists

        deleteButton = findViewById(R.id.btnDeleteAccount);
        deleteButton.setOnClickListener(v ->
                deleteSpecificQuestionnaireSession("session_1753633425698"));
    }

    private void deleteSpecificQuestionnaireSession(String sessionId) {
        DatabaseReference sessionRef = FirebaseDatabase.getInstance()
                .getReference("questionnaire_sessions")
                .child(sessionId);

        sessionRef.removeValue()
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Deleted session: " + sessionId, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to delete session: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
