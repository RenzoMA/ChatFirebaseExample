package com.android.renzo.androidchat.login;

/**
 * Created by HOME on 11/06/2016.
 */
public interface LoginRepository {
    void signUp(String email,String password);
    void signIn(String email,String password);
    void checkSession();
}
