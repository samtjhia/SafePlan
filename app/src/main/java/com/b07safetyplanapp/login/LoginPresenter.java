package com.b07safetyplanapp.login;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private LoginContract.Model model;

    public LoginPresenter(LoginContract.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

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
        });
    }

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
        });
    }

    @Override
    public boolean isEmailValid(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    @Override
    public boolean isPasswordValid(String password) {
        return password != null && !password.isEmpty();
    }

    @Override
    public boolean isPinValid(String pin) {
        //all digits check
        return pin != null && (pin.length() == 4 || pin.length() == 6) && pin.matches("\\d+");
    }
}
