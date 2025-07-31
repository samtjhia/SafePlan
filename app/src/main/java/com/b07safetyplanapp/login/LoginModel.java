package com.b07safetyplanapp.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.firebase.auth.FirebaseAuth;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

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
                listener.onSuccess();
            } else {
                listener.onFailure("Incorrect PIN.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("PIN verification failed.");
        }
    }
}
