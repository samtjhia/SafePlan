package com.b07safetyplanapp.signup;

import android.util.Patterns;

public class SignupPresenter implements SignupContract.Presenter {

    private SignupContract.View view;
    private final SignupContract.Model model;

    public SignupPresenter(SignupContract.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(SignupContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    /**
     * Handles the sign-up button click by validating input and initiating the signup process.
     *
     * @param fullName        User's full name
     * @param email           Email address
     * @param password        Password input
     * @param confirmPassword Confirm password input
     */
    @Override
    public void onSignupClicked(String fullName, String email, String password, String confirmPassword) {
        if (view == null) return;

        boolean hasError = false;

        if (!isFullNameValid(fullName)) {
            view.showFullNameError("Full name is required");
            hasError = true;
        }

        if (!isEmailValid(email)) {
            view.showEmailError("Invalid email format");
            hasError = true;
        }

        if (!isPasswordValid(password)) {
            view.showPasswordError("Password must be at least 6 characters");
            hasError = true;
        }

        if (!doPasswordsMatch(password, confirmPassword)) {
            view.showConfirmPasswordError("Passwords do not match");
            hasError = true;
        }

        if (hasError) return;

        view.showLoading();

        model.signup(fullName, email, password, new SignupContract.Model.OnSignupFinishedListener() {
            @Override
            public void onSuccess() {
                if (view != null) {
                    view.hideLoading();
                    view.navigateToPinSetup(email, password);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) {
                    view.hideLoading();
                    view.showSignupError(errorMessage);
                }
            }
        });
    }

    @Override
    public boolean isFullNameValid(String fullName) {
        return !fullName.isEmpty();
    }

    @Override
    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    @Override
    public boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
