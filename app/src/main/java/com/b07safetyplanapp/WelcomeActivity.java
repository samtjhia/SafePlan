package com.b07safetyplanapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.b07safetyplanapp.login.LoginActivity;
import com.b07safetyplanapp.pinsetup.PinSetupActivity;
import com.b07safetyplanapp.signup.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;

import android.view.View;
import android.widget.Button;

/**
 * WelcomeActivity serves as the entry point to the app.
 * It determines whether the user should proceed to onboarding, login, or directly to the main app,
 * based on Firebase authentication and shared preference flags for pin setup and questionnaire completion.
 */
public class WelcomeActivity extends AppCompatActivity {

    private Button getStartedButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already signed in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Check if onboarding is complete
            SharedPreferences prefs = getSharedPreferences("safeplan_prefs", MODE_PRIVATE);
            boolean pinSet = prefs.getBoolean("pin_setup_complete", false);
            boolean questionnaireDone = prefs.getBoolean("questionnaire_complete", false);

            if (pinSet && questionnaireDone) {
                // Fully onboarded so go to MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }

        // Otherwise, show welcome screen
        setContentView(R.layout.activity_welcome);

        getStartedButton = findViewById(R.id.get_started_button);
        loginButton = findViewById(R.id.login_button);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
            }
        });

        loginButton.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                // No user is signed in go to login screen
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            // If user is signed in, check onboarding flags
            SharedPreferences prefs = getSharedPreferences("safeplan_prefs", MODE_PRIVATE);
            boolean pinSet = prefs.getBoolean("pin_setup_complete", false);
            boolean questionnaireDone = prefs.getBoolean("questionnaire_complete", false);

            Intent intent;
            if (!pinSet) {
                intent = new Intent(this, PinSetupActivity.class);
            } else if (!questionnaireDone) {
                intent = new Intent(this, QuestionnaireActivity.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }

            startActivity(intent);
            finish();
        });


    }
}
