package com.b07safetyplanapp.login;

public interface LoginContract {

    interface View {
        void showLoading();
        void hideLoading();

        void showEmailError(String message);
        void showPasswordError(String message);
        void showPinError(String message);
        void showLoginError(String message);

        void navigateToDashboard();
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
        }

        void loginWithEmail(String email, String password, OnLoginFinishedListener listener);
        void loginWithPin(String pin, OnLoginFinishedListener listener);
    }
}
