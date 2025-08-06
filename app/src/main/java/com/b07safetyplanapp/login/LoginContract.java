package com.b07safetyplanapp.login;

import android.content.Context;

public interface LoginContract {

    interface View {
        void showLoading();
        void hideLoading();

        void showEmailError(String message);
        void showPasswordError(String message);
        void showPinError(String message);
        void showLoginError(String message);

        void navigateToDashboard();
        void navigateToPinSetupWithMismatch(Context context, String decryptedEmail, String decryptedPassword);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();

        void onEmailLoginClicked(String email, String password);
        void onPinLoginClicked(String pin);
        boolean isEmailValid(String email);
        boolean isPasswordValid(String password);
        boolean isPinValid(String pin);

    }

    interface Model {
        interface OnLoginFinishedListener {
            void onSuccess();
            void onFailure(String errorMessage);
            void onPinMismatchDetected(String email, String password);

        }

        void loginWithEmail(String email, String password, OnLoginFinishedListener listener);
        void loginWithPin(String pin, OnLoginFinishedListener listener);
    }
}
