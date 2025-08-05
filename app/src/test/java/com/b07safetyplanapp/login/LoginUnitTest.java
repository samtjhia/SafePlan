package com.b07safetyplanapp.login;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTest {
    @Mock
    LoginContract.View view;
    @Mock
    LoginContract.Model model;
    LoginContract.Presenter presenter;
    @Captor
    ArgumentCaptor<LoginContract.Model.OnLoginFinishedListener> loginCallbackCaptor;

    private final String validEmail = "user@example.com";
    private final String validPassword = "validpassword";
    private final String invalidEmail = "invalid-email";
    private final String emptyPassword = "";
    private final String validPin = "1234";
    private final String invalidPin = "a48";


    //test detachView()
    //behaviour: presenter does nothing when detachView() is called
    @Test
    public void testViewIsNullForEmail() {
        presenter = new LoginPresenter(model);
        presenter.detachView();

        presenter.onEmailLoginClicked(validEmail, validPassword);

        verify(view, never()).showLoading();
        verify(model, never()).loginWithEmail(any(), any(), any());
    }

    // test onEmailLoginClicked()
    // behaviour: presenter shows email error if email is invalid
    @Test
    public void testInvalidEmail() {
        presenter = new LoginPresenter(model);
        presenter.attachView(view);

        presenter.onEmailLoginClicked(invalidEmail, validPassword);

        verify(view).showEmailError("Invalid email");
    }

    // test onEmailLoginClicked()
    // behaviour: presenter shows password error if password is empty
    @Test
    public void testEmptyPassword() {
        presenter = new LoginPresenter(model);
        presenter.attachView(view);

        presenter.onEmailLoginClicked(validEmail, emptyPassword);

        verify(view).showPasswordError("Password cannot be empty");
    }


    // test onEmailLoginClicked()
    // behaviour: valid inputs should call model
    @Test
    public void testValidInputsForEmail() {
        presenter = new LoginPresenter(model);
        presenter.attachView(view);

        presenter.onEmailLoginClicked(validEmail, validPassword);

        verify(model).loginWithEmail(eq(validEmail), eq(validPassword), any());
    }

    // test onEmailLoginClicked()
    // behaviour: model callback should trigger navigation on success
    @Test
    public void testSuccessEmailLogin() {
        presenter = new LoginPresenter(model);
        presenter.attachView(view);

        presenter.onEmailLoginClicked(validEmail, validPassword);

        verify(model).loginWithEmail(eq(validEmail), eq(validPassword), loginCallbackCaptor.capture());

        loginCallbackCaptor.getValue().onSuccess();

        verify(view).hideLoading();
        verify(view).navigateToDashboard();
    }

    // test onEmailLoginClicked()
    // behaviour: model callback should show error on failure
    @Test
    public void testFailedEmailLogin() {
        presenter = new LoginPresenter(model);
        presenter.attachView(view);

        presenter.onEmailLoginClicked(validEmail, validPassword);

        verify(model).loginWithEmail(eq(validEmail), eq(validPassword), loginCallbackCaptor.capture());

        loginCallbackCaptor.getValue().onFailure("Login failed");

        verify(view).hideLoading();
        verify(view).showLoginError("Login failed");
    }

    // test onPinLoginClicked()
    // behaviour: presenter should do nothing if view is null
    @Test
    public void testViewIsNullForPin() {
        presenter = new LoginPresenter(model);
        presenter.detachView();

        presenter.onPinLoginClicked(validPin);

        verify(view, never()).showLoading();
        verify(model, never()).loginWithPin(any(), any());
    }

    // test onPinLoginClicked()
    // behaviour: presenter should show error if PIN is invalid
    @Test
    public void testInvalidPin() {
        presenter = new LoginPresenter(model);
        presenter.attachView(view);

        presenter.onPinLoginClicked(invalidPin);

        verify(view).showPinError("PIN must be 4 or 6 digits");
    }

    // test onPinLoginClicked()
    // behaviour: valid PIN should call model.loginWithPin
    @Test
    public void testValidPin() {
        presenter = new LoginPresenter(model);
        presenter.attachView(view);

        presenter.onPinLoginClicked(validPin);

        verify(model).loginWithPin(eq(validPin), any());
    }

    // test onPinLoginClicked()
    // behaviour: success callback should navigate
    @Test
    public void testSuccessPinLogin() {
        presenter = new LoginPresenter(model);
        presenter.attachView(view);

        presenter.onPinLoginClicked(validPin);

        verify(model).loginWithPin(eq(validPin), loginCallbackCaptor.capture());
        loginCallbackCaptor.getValue().onSuccess();

        verify(view).hideLoading();
        verify(view).navigateToDashboard();
    }

    // test onPinLoginClicked()
    // behaviour: failure callback should hide loading and show error
    @Test
    public void testFailedPinLogin() {
        presenter = new LoginPresenter(model);
        presenter.attachView(view);

        presenter.onPinLoginClicked(validPin);

        verify(model).loginWithPin(eq(validPin), loginCallbackCaptor.capture());
        loginCallbackCaptor.getValue().onFailure("PIN login failed");

        verify(view).hideLoading();
        verify(view).showLoginError("PIN login failed");
    }

    // test isEmailValid()
    // behaviour: returns false for null email
    @Test
    public void invalidEmail_ReturnsFalse() {
        presenter = new LoginPresenter(model);
        assertFalse(presenter.isEmailValid(null));
    }

    // test isPasswordValid()
    // behaviour: returns false for null password
    @Test
    public void emptyOrNullPassword_ReturnsFalse() {
        presenter = new LoginPresenter(model);
        assertFalse(presenter.isPasswordValid(null));
    }

    // test isPinValid()
    // behaviour: returns false for null pin
    @Test
    public void invalidPin_ReturnsFalse() {
        presenter = new LoginPresenter(model);
        assertFalse(presenter.isPinValid(null));
    }

    // test isPinValid()
    // behaviour: returns true for 6 digit numeric pin
    @Test
    public void validPin_ReturnsTrue() {
        presenter = new LoginPresenter(model);
        assertTrue(presenter.isPinValid("123456"));
    }






}