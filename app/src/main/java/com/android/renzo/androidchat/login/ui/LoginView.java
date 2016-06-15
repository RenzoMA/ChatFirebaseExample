package com.android.renzo.androidchat.login.ui;

/**
 * Created by HOME on 11/06/2016.
 */
public interface LoginView {
    void enableInputs();
    void disableInputs();
    void showProgress();
    void hideProgress();

    void handleSingup();
    void handleSignin();

    void navigateToMainScreen();
    void loginError(String error);

    void newUserSuccess();
    void newUserError(String error);
}
