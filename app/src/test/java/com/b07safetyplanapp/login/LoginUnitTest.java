package com.b07safetyplanapp.login;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTest {
    @Mock
    LoginContract.View view;
    @Mock
    LoginContract.Model model;
    LoginContract.Presenter presenter;

    //test detachView()
    //behaviour: presenter does nothings when detachView() is called
    @Test
    public void testViewIsNull() {
        presenter = new LoginPresenter(model);
        presenter.detachView();

        presenter.onEmailLoginClicked("user@example.com", "password123");

        verify(view, never()).showLoading();
        verify(view, never()).showEmailError(any());
        verify(view, never()).showPasswordError(any());
        verify(view, never()).navigateToDashboard();
        verify(model, never()).loginWithEmail(any(), any(), any());
    }

}