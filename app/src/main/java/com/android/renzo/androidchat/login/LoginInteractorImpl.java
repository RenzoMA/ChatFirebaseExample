package com.android.renzo.androidchat.login;

/**
 * Created by HOME on 11/06/2016.
 */
public class LoginInteractorImpl implements LoginInteractor {
    private LoginRepository loginRepository;

    public LoginInteractorImpl() {
        loginRepository = new LoginRepositoryImpl();
    }

    @Override
    public void checkSession() {
        loginRepository.checkSession();
    }

    @Override
    public void doSignUp(String email, String password) {
        loginRepository.signUp(email,password);
    }

    @Override
    public void doSignIn(String email, String password) {
        loginRepository.signIn(email,password);
    }
}
