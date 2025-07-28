package com.b07safetyplanapp.signup;

public interface SignupContract {

    interface View {
        void showLoading();
        void hideLoading();

        void showFullNameError(String message);
        void showEmailError(String message);
        void showPasswordError(String message);
        void showConfirmPasswordError(String message);
        void showSignupError(String message);

        void navigateToPinSetup();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();

        void onSignupClicked(String fullName, String email, String password, String confirmPassword);

        boolean isFullNameValid(String fullName);
        boolean isEmailValid(String email);
        boolean isPasswordValid(String password);
        boolean doPasswordsMatch(String password, String confirmPassword);
    }

    interface Model {
        interface OnSignupFinishedListener {
            void onSuccess();
            void onFailure(String errorMessage);
        }

        void signup(String fullName, String email, String password, OnSignupFinishedListener listener);
    }
}