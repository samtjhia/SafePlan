package com.b07safetyplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.b07safetyplanapp.reminders.ReminderScheduleActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private Button privacyButton, logoutButton, reminderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize buttons
        privacyButton = findViewById(R.id.btnSetting4); // Privacy Control
        logoutButton = findViewById(R.id.btnLogout);    // Log Out
        reminderButton = findViewById(R.id.btnReminder); // Reminder button



        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Navigate to Privacy Control
        privacyButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PrivacyControlActivity.class);
            startActivity(intent);
        });

        // Navigate to Reminder Schedule Activity
        reminderButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ReminderScheduleActivity.class);
            startActivity(intent);
        });

        // Log out and return to login screen
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            finish();
        });
    }
}
