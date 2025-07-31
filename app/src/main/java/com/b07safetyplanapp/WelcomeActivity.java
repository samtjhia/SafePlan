package com.b07safetyplanapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.b07safetyplanapp.login.LoginActivity;
import com.b07safetyplanapp.signup.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;

import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button getStartedButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already signed in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // User is signed in, go to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });
    }
}
