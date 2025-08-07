package com.b07safetyplanapp.signup;

import android.util.Patterns;

/**
 * Presenter implementation for the Signup screen following the MVP pattern.
 * Handles validation and user interaction logic, delegating signup operations to the model.
 */
public class SignupPresenter implements SignupContract.Presenter {

    private SignupContract.View view;
    private final SignupContract.Model model;

    /**
     * Constructs the presenter with a reference to the model.
     *
     * @param model The model implementation to perform signup operations
     */
    public SignupPresenter(SignupContract.Model model) {
        this.model = model;
    }

    /**
     * Attaches the view to the presenter. Typically called in the Activity/Fragment's onCreate().
     *
     * @param view The View interface to bind to this presenter
     */
    @Override
    public void attachView(SignupContract.View view) {
        this.view = view;
    }

    /**
     * Detaches the view to avoid memory leaks. Typically called in onDestroy().
     */
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

    /**
     * Validates full name input.
     *
     * @param fullName The full name string
     * @return True if not empty, false otherwise
     */
    @Override
    public boolean isFullNameValid(String fullName) {
        return !fullName.isEmpty();
    }

    /**
     * Validates email format.
     *
     * @param email The email string
     * @return True if valid email format, false otherwise
     */
    @Override
    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validates password strength.
     *
     * @param password The password string
     * @return True if at least 6 characters long, false otherwise
     */
    @Override
    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Checks if both password inputs match.
     *
     * @param password        The original password
     * @param confirmPassword The repeated password input
     * @return True if both match, false otherwise
     */
    @Override
    public boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
