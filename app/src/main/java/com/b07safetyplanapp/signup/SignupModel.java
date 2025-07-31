package com.b07safetyplanapp.signup;

import com.google.firebase.auth.FirebaseAuth;

public class SignupModel implements SignupContract.Model {

    private final FirebaseAuth firebaseAuth;

    public SignupModel() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signup(String fullName, String email, String password, OnSignupFinishedListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Signup failed";
                        listener.onFailure(error);
                    }
                });
    }
}
