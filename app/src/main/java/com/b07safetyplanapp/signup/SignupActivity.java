package com.b07safetyplanapp.signup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b07safetyplanapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity implements SignupContract.View {

    private TextInputEditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPass;
    private Button buttonSignUp;
    private TextView textLoginRedirect;

    private SignupContract.Presenter presenter;

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
            // TODO: Intent to LoginActivity
            Toast.makeText(this, "redirect to login", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        buttonSignUp.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        buttonSignUp.setEnabled(true);
    }

    @Override
    public void showFullNameError(String message) {
        editTextFullName.setError(message);
    }

    @Override
    public void showEmailError(String message) {
        editTextEmail.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        editTextPassword.setError(message);
    }

    @Override
    public void showConfirmPasswordError(String message) {
        editTextConfirmPass.setError(message);
    }

    @Override
    public void showSignupError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToPinSetup() {
        // TODO: Launch PIN setup screen
        Toast.makeText(this, "signup works", Toast.LENGTH_SHORT).show();
    }
}
