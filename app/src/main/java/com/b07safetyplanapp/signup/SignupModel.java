package com.b07safetyplanapp.signup;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class SignupModel implements SignupContract.Model {

    private final FirebaseAuth firebaseAuth;
    private static final String TAG = "SignupModel";

    public SignupModel() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signup(String fullName, String email, String password, OnSignupFinishedListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            DatabaseReference userRef = FirebaseDatabase
                                    .getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/")
                                    .getReference("users")
                                    .child(uid);

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", email);
                            userData.put("fullName", fullName);
                            userData.put("createdAt", ServerValue.TIMESTAMP);

                            userRef.updateChildren(userData)
                                    .addOnCompleteListener(writeTask -> {
                                        if (writeTask.isSuccessful()) {
                                            Log.d(TAG, "✅ User data written to DB");
                                        } else {
                                            Log.e(TAG, "❌ Failed to write to DB", writeTask.getException());
                                        }
                                        listener.onSuccess();
                                    });
                        } else {
                            Log.e(TAG, "❌ FirebaseUser is null after signup");
                            listener.onFailure("Internal error: user is null");
                        }
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Signup failed";
                        Log.e(TAG, "❌ Signup failed: " + error);
                        listener.onFailure(error);
                    }
                });
    }
}
