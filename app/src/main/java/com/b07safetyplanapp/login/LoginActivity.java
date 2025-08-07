package com.b07safetyplanapp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.b07safetyplanapp.MainActivity;
import com.b07safetyplanapp.R;
import com.b07safetyplanapp.pinsetup.PinSetupActivity;
import com.b07safetyplanapp.signup.SignupActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Handles the login UI and user authentication using either PIN or email/password.
 * Supports forced logout from external triggers (e.g., reminder expiration).
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private TextInputEditText emailInput, passwordInput, pinInput;
    private Button loginButton;
    private TextView signupRedirect;
    private LoginContract.Presenter presenter;

    /**
     * Called when the activity is starting. Sets up the UI and listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Handle forced logout (e.g., from a triggered reminder)
        if (getIntent().getBooleanExtra("forceLogout", false)) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Session expired. Please sign in again.", Toast.LENGTH_SHORT).show();
        }

        // Initialize UI components
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        pinInput = findViewById(R.id.editTextPin);
        loginButton = findViewById(R.id.buttonLogin);
        signupRedirect = findViewById(R.id.textSignupRedirect);

        // Set up Presenter
        presenter = new LoginPresenter(this, new LoginModel(this));
        presenter.attachView(this);

        // Login button behavior: handle PIN login or email/password login
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String pin = pinInput.getText().toString();

            if (!pin.isEmpty()) {
                presenter.onPinLoginClicked(pin);
            } else {
                presenter.onEmailLoginClicked(email, password);
            }
        });

        // Redirect to sign-up page
        signupRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void showLoading() {
        loginButton.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        loginButton.setEnabled(true);
    }

    @Override
    public void showEmailError(String message) {
        emailInput.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        passwordInput.setError(message);
    }

    @Override
    public void showPinError(String message) {
        pinInput.setError(message);
    }

    @Override
    public void showLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigates to the main dashboard after successful login.
     */
    @Override
    public void navigateToDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Clean up when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    /**
     * Navigates the user to PIN setup if there's a mismatch between saved PIN and account.
     *
     * @param context          The context from which the navigation was triggered.
     * @param decryptedEmail   The decrypted email from local storage.
     * @param decryptedPassword The decrypted password from local storage.
     */
    @Override
    public void navigateToPinSetupWithMismatch(Context context, String decryptedEmail, String decryptedPassword) {
        Intent intent = new Intent(context, PinSetupActivity.class);
        intent.putExtra("update_pin_reason", "This device's saved PIN is tied to another account. Please set a new PIN to continue.");
        intent.putExtra("user_email", decryptedEmail);
        intent.putExtra("user_password", decryptedPassword);
        context.startActivity(intent);
    }
}
