package com.b07safetyplanapp.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class LoginModel implements LoginContract.Model {

    private static final String PREF_NAME = "safeplan_prefs";
    private static final String ENCRYPTED_PIN_KEY = "encrypted_pin";
    private static final String PIN_IV_KEY = "pin_iv";
    private static final String KEY_ALIAS = "safeplan_key";

    private final Context context;
    private final FirebaseAuth firebaseAuth;

    public LoginModel(Context context) {
        this.context = context.getApplicationContext();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void loginWithEmail(String email, String password, OnLoginFinishedListener listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storeUserDataToRealtimeDB();
                        listener.onSuccess();
                    } else {
                        listener.onFailure(task.getException() != null ?
                                task.getException().getMessage() :
                                "Login failed");
                    }
                });
    }

    @Override
    public void loginWithPin(String inputPin, OnLoginFinishedListener listener) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String encryptedBase64 = prefs.getString(ENCRYPTED_PIN_KEY, null);
            String ivBase64 = prefs.getString(PIN_IV_KEY, null);

            if (encryptedBase64 == null || ivBase64 == null) {
                listener.onFailure("No PIN found. Please log in using email and password.");
                return;
            }

            byte[] encrypted = Base64.decode(encryptedBase64, Base64.DEFAULT);
            byte[] iv = Base64.decode(ivBase64, Base64.DEFAULT);

            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            SecretKey secretKey = ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            byte[] decryptedBytes = cipher.doFinal(encrypted);
            String decryptedPin = new String(decryptedBytes, StandardCharsets.UTF_8);

            if (inputPin.equals(decryptedPin)) {
                String encryptedEmail = prefs.getString("encrypted_email", null);
                String emailIv = prefs.getString("email_iv", null);
                String encryptedPassword = prefs.getString("encrypted_password", null);
                String passwordIv = prefs.getString("password_iv", null);

                if (encryptedEmail == null || emailIv == null || encryptedPassword == null || passwordIv == null) {
                    listener.onFailure("Saved credentials missing. Please login with email/password.");
                    return;
                }

                // Decrypt email
                Cipher emailCipher = Cipher.getInstance("AES/GCM/NoPadding");
                GCMParameterSpec emailSpec = new GCMParameterSpec(128, Base64.decode(emailIv, Base64.DEFAULT));
                emailCipher.init(Cipher.DECRYPT_MODE, secretKey, emailSpec);
                String decryptedEmail = new String(emailCipher.doFinal(Base64.decode(encryptedEmail, Base64.DEFAULT)), StandardCharsets.UTF_8);

                // Decrypt password
                Cipher passwordCipher = Cipher.getInstance("AES/GCM/NoPadding");
                GCMParameterSpec passwordSpec = new GCMParameterSpec(128, Base64.decode(passwordIv, Base64.DEFAULT));
                passwordCipher.init(Cipher.DECRYPT_MODE, secretKey, passwordSpec);
                String decryptedPassword = new String(passwordCipher.doFinal(Base64.decode(encryptedPassword, Base64.DEFAULT)), StandardCharsets.UTF_8);

                firebaseAuth.signInWithEmailAndPassword(decryptedEmail, decryptedPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                storeUserDataToRealtimeDB();
                                listener.onSuccess();
                            } else {
                                listener.onFailure("Firebase login failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : ""));
                            }
                        });

            } else {
                listener.onFailure("Incorrect PIN.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("PIN verification failed.");
        }
    }

    // ✅ New method to store user profile to Realtime Database
    private void storeUserDataToRealtimeDB() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String email = user.getEmail();

            DatabaseReference userRef = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/")
                    .getReference("users")
                    .child(uid);

            Map<String, Object> userData = new HashMap<>();
            userData.put("email", email);
            userData.put("createdAt", ServerValue.TIMESTAMP);

            userRef.updateChildren(userData);
        }
    }
}
