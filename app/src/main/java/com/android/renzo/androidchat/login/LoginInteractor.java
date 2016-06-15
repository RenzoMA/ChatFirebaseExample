package com.android.renzo.androidchat.login;

/**
 * Created by HOME on 11/06/2016.
 */
public interface LoginInteractor {
    void checkSession();
    void doSignUp(String email,String password);
    void doSignIn(String email,String password);
}
