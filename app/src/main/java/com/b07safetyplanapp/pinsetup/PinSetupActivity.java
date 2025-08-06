package com.b07safetyplanapp.pinsetup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.b07safetyplanapp.QuestionnaireActivity;
import com.b07safetyplanapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

public class PinSetupActivity extends AppCompatActivity {

    private RadioGroup digitSelector;
    private LinearLayout dotContainer;
    private GridLayout keypad;
    private Button savePinButton;

    private int pinLength = 4; // default
    private StringBuilder currentPin = new StringBuilder();

    private static final String PREF_NAME = "safeplan_prefs";
    private static final String ENCRYPTED_PIN_KEY = "encrypted_pin";
    private static final String PIN_IV_KEY = "pin_iv";
    private static final String KEY_ALIAS = "safeplan_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_setup);

        TextView reasonTextView = findViewById(R.id.reasonTextView);
        String reason = getIntent().getStringExtra("update_pin_reason");

        if (reason != null && !reason.isEmpty()) {
            reasonTextView.setText(reason);
            reasonTextView.setVisibility(View.VISIBLE);
        }


        digitSelector = findViewById(R.id.digitSelector);
        dotContainer = findViewById(R.id.dotContainer);
        keypad = findViewById(R.id.keypad);
        savePinButton = findViewById(R.id.buttonSavePin);

        digitSelector.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio4Digits) {
                pinLength = 4;
            } else if (checkedId == R.id.radio6Digits) {
                pinLength = 6;
            }
            currentPin.setLength(0); // clear pin input
            updateDotIndicators();
        });

        //set onboarding flags to false
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        prefs.edit()
                .putBoolean("pin_setup_complete", false)
                .putBoolean("questionnaire_complete", false)
                .apply();


        setupKeypad();
        updateDotIndicators();

        savePinButton.setOnClickListener(v -> {
            if (currentPin.length() == pinLength) {
                saveEncryptedPin(currentPin.toString());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = getIntent().getStringExtra("user_email"); // use this instead of user.getEmail()
                String password = getIntent().getStringExtra("user_password");

                if (email != null && password != null) {
                    try {
                        saveCredentials(email, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to save login credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Missing user credentials. Could not link PIN.", Toast.LENGTH_SHORT).show();
                }


                Intent intent = new Intent(PinSetupActivity.this, QuestionnaireActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Please enter a complete PIN", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateDotIndicators() {
        dotContainer.removeAllViews();
        for (int i = 0; i < pinLength; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageResource(i < currentPin.length() ? R.drawable.dot_filled : R.drawable.dot_empty);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotContainer.addView(dot);
        }
        savePinButton.setVisibility(currentPin.length() == pinLength ? View.VISIBLE : View.INVISIBLE);
    }

    private void setupKeypad() {
        for (int i = 0; i < keypad.getChildCount(); i++) {
            View view = keypad.getChildAt(i);
            if (view instanceof Button) {
                Button btn = (Button) view;
                String text = btn.getText().toString();

                if (text.equals("←")) {
                    btn.setOnClickListener(v -> {
                        if (currentPin.length() > 0) {
                            currentPin.deleteCharAt(currentPin.length() - 1);
                            updateDotIndicators();
                        }
                    });
                } else {
                    btn.setOnClickListener(v -> {
                        if (currentPin.length() < pinLength) {
                            currentPin.append(text);
                            updateDotIndicators();
                        }
                    });
                }
            }
        }
    }

    private void saveEncryptedPin(String pin) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build();
                keyGenerator.init(spec);
                keyGenerator.generateKey();
            }

            SecretKey secretKey = ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getIV();
            byte[] encrypted = cipher.doFinal(pin.getBytes(StandardCharsets.UTF_8));

            String encryptedBase64 = Base64.encodeToString(encrypted, Base64.DEFAULT);
            String ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT);

            SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            prefs.edit()
                    .putString(ENCRYPTED_PIN_KEY, encryptedBase64)
                    .putString(PIN_IV_KEY, ivBase64)
                    .putBoolean("pin_setup_complete", true)
                    .apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String EMAIL_KEY = "encrypted_email";
    private static final String PASSWORD_KEY = "encrypted_password";

    private void saveCredentials(String email, String password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        SecretKey secretKey = ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();

        // Encrypt email
        Cipher emailCipher = Cipher.getInstance("AES/GCM/NoPadding");
        emailCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] emailIv = emailCipher.getIV();
        byte[] encryptedEmail = emailCipher.doFinal(email.getBytes(StandardCharsets.UTF_8));

        // Encrypt password
        Cipher passwordCipher = Cipher.getInstance("AES/GCM/NoPadding");
        passwordCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] passwordIv = passwordCipher.getIV();
        byte[] encryptedPassword = passwordCipher.doFinal(password.getBytes(StandardCharsets.UTF_8));

        // Base64 encode everything
        String emailEncoded = Base64.encodeToString(encryptedEmail, Base64.DEFAULT);
        String emailIvEncoded = Base64.encodeToString(emailIv, Base64.DEFAULT);

        String passwordEncoded = Base64.encodeToString(encryptedPassword, Base64.DEFAULT);
        String passwordIvEncoded = Base64.encodeToString(passwordIv, Base64.DEFAULT);

        // Save to SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString("encrypted_email", emailEncoded)
                .putString("email_iv", emailIvEncoded)
                .putString("encrypted_password", passwordEncoded)
                .putString("password_iv", passwordIvEncoded)
                .apply();
    }


}