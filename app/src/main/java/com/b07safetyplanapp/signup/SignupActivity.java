package com.b07safetyplanapp.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b07safetyplanapp.R;
import com.b07safetyplanapp.login.LoginActivity;
import com.b07safetyplanapp.pinsetup.PinSetupActivity;
import com.google.android.material.textfield.TextInputEditText;

/**
 * SignupActivity allows new users to create an account by providing their full name,
 * email, and password. The activity validates user input and forwards the user to
 * PIN setup upon successful signup.
 */
public class SignupActivity extends AppCompatActivity implements SignupContract.View {

    private TextInputEditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPass;
    private Button buttonSignUp;
    private TextView textLoginRedirect;

    private SignupContract.Presenter presenter;


    /**
     * Initializes the activity, binds views, sets up the presenter, and handles user interactions.
     *
     * @param savedInstanceState The saved instance state from a previous run (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Bind views
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPass = findViewById(R.id.editTextConfirmPass);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textLoginRedirect = findViewById(R.id.textLoginRedirect);

        // Set up presenter
        presenter = new SignupPresenter(new SignupModel());
        presenter.attachView(this);

        buttonSignUp.setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString();
            String confirmPassword = editTextConfirmPass.getText().toString();

            presenter.onSignupClicked(fullName, email, password, confirmPassword);
        });

        textLoginRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Detaches the presenter view when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    /**
     * Disables the sign-up button while processing.
     */
    @Override
    public void showLoading() {
        buttonSignUp.setEnabled(false);
    }

    /**
     * Re-enables the sign-up button after processing.
     */
    @Override
    public void hideLoading() {
        buttonSignUp.setEnabled(true);
    }

    /**
     * Displays an error message for the full name input field.
     *
     * @param message The error message to show.
     */
    @Override
    public void showFullNameError(String message) {
        editTextFullName.setError(message);
    }


    /**
     * Displays an error message for the email input field.
     *
     * @param message The error message to show.
     */
    @Override
    public void showEmailError(String message) {
        editTextEmail.setError(message);
    }


    /**
     * Displays an error message for the password input field.
     *
     * @param message The error message to show.
     */
    @Override
    public void showPasswordError(String message) {
        editTextPassword.setError(message);
    }

    /**
     * Displays an error message for the confirm password input field.
     *
     * @param message The error message to show.
     */
    @Override
    public void showConfirmPasswordError(String message) {
        editTextConfirmPass.setError(message);
    }

    /**
     * Displays a general sign-up error message as a Toast.
     *
     * @param message The error message to show.
     */
    @Override
    public void showSignupError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigates the user to the PIN setup screen after successful sign-up.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     */
    @Override
    public void navigateToPinSetup(String email, String password) {
        Intent intent = new Intent(SignupActivity.this, PinSetupActivity.class);
        intent.putExtra("user_email", email);
        intent.putExtra("user_password", password);
        startActivity(intent);
        finish();
    }

}
