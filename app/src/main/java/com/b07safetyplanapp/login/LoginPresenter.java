package com.b07safetyplanapp.login;

import android.content.Context;

/**
 * Presenter class for handling login operations in MVP architecture.
 * Coordinates between the View and Model, handling logic for email/password and PIN authentication.
 */
public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View view;
    private LoginContract.Model model;
    private final Context context;

    /**
     * Constructor for LoginPresenter.
     *
     * @param context the context used for operations like navigation and accessing system services
     * @param model   the model that handles authentication logic
     */
    public LoginPresenter(Context context, LoginContract.Model model) {
        this.context = context;
        this.model = model;
    }


    /**
     * Attaches the view to this presenter.
     *
     * @param view the view interface to be attached
     */
    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
    }


    /**
     * Detaches the view from this presenter to prevent memory leaks.
     */
    @Override
    public void detachView() {
        this.view = null;
    }


    /**
     * Handles logic when the user attempts to log in with email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     */
    @Override
    public void onEmailLoginClicked(String email, String password) {
        if (view == null) return;

        boolean isValid = true;

        if (!isEmailValid(email)) {
            view.showEmailError("Invalid email");
            isValid = false;
        }

        if (!isPasswordValid(password)) {
            view.showPasswordError("Password cannot be empty");
            isValid = false;
        }

        if (!isValid) return;

        view.showLoading();
        model.loginWithEmail(email, password, new LoginContract.Model.OnLoginFinishedListener() {
            @Override
            public void onSuccess() {
                view.hideLoading();
                view.navigateToDashboard();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showLoginError(errorMessage);

            }

            @Override
            public void onPinMismatchDetected(String email, String password) {
                view.hideLoading();
                view.navigateToPinSetupWithMismatch(context, email, password);

            }
        });
    }


    /**
     * Handles logic when the user attempts to log in using a PIN.
     *
     * @param pin the PIN entered by the user
     */
    @Override
    public void onPinLoginClicked(String pin) {
        if (view == null) return;

        if (!isPinValid(pin)) {
            view.showPinError("PIN must be 4 or 6 digits");
            return;
        }

        view.showLoading();
        model.loginWithPin(pin, new LoginContract.Model.OnLoginFinishedListener() {
            @Override
            public void onSuccess() {
                view.hideLoading();
                view.navigateToDashboard();

            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showLoginError(errorMessage);

            }

            @Override
            public void onPinMismatchDetected(String email, String password) {
                view.hideLoading();
                view.navigateToPinSetupWithMismatch(context, email, password);

            }
        });
    }


    /**
     * Validates whether the given email has a valid format.
     *
     * @param email the email address to validate
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isEmailValid(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }


    /**
     * Validates whether the given password is not empty.
     *
     * @param password the password string to validate
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isPasswordValid(String password) {
        return password != null && !password.isEmpty();
    }


    /**
     * Validates whether the given PIN is 4 or 6 digits.
     *
     * @param pin the PIN to validate
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isPinValid(String pin) {
        //all digits check
        return pin != null && (pin.length() == 4 || pin.length() == 6) && pin.matches("\\d+");
    }

}
