package com.b07safetyplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private Button privacyButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // Optional: Rename this to activity_settings.xml

        // Initialize buttons
        privacyButton = findViewById(R.id.btnSetting4); // Privacy Control
        logoutButton = findViewById(R.id.btnLogout);    // Log Out

        // Navigate to Privacy Control
        privacyButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PrivacyControlActivity.class);
            startActivity(intent);
        });

        // Log out and return to login screen
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            // Example: Navigate to login activity
            // startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            finish(); // Close settings activity
        });
    }
}
