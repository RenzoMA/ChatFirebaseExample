package com.android.renzo.androidchat.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.renzo.androidchat.R;
import com.android.renzo.androidchat.contactlist.ui.ContactListActivity;
import com.android.renzo.androidchat.login.LoginPresenter;
import com.android.renzo.androidchat.login.LoginPresenterImpl;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {


    @Bind(R.id.editTxtEmail)
    EditText inputEmail;
    @Bind(R.id.editTxtPassword)
    EditText inputPassword;
    @Bind(R.id.btnSignin)
    Button btnSignin;
    @Bind(R.id.btnSignup)
    Button btnSignup;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.layoutMainContainer)
    RelativeLayout container;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginPresenter = new LoginPresenterImpl(this);
        loginPresenter.onCreate();
        loginPresenter.checkForAuthenticatedUser();
    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.btnSignin)
    public void handleSigin() {
        Log.e("AndroidChat", inputEmail.getText().toString());
    }

    @OnClick(R.id.btnSignup)
    public void handleSignup(){

    }

    @Override
    public void enableInputs() {
        setInputs(true);
    }

    @Override
    public void disableInputs() {
        setInputs(false);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnSignup)
    @Override
    public void handleSingup() {
        loginPresenter.registerNewUser(inputEmail.getText().toString(),
                                        inputPassword.getText().toString());
    }

    @OnClick(R.id.btnSignin)
    @Override
    public void handleSignin() {
        loginPresenter.validateLogin(inputEmail.getText().toString(),
                inputPassword.getText().toString());
    }

    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, ContactListActivity.class));
    }

    @Override
    public void loginError(String error) {
        inputPassword.setText("");
        String msgError = String.format(getString(R.string.login_error_message_signin),error);
        inputPassword.setError(msgError);
    }

    @Override
    public void newUserSuccess() {
        Snackbar.make(container,R.string.login_notice_message_signup, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void newUserError(String error) {
        inputPassword.setText("");
        String msgError = String.format(getString(R.string.login_error_message_signin),error);
        inputPassword.setError(msgError);

    }
    private void setInputs(boolean enabled){
        inputEmail.setEnabled(enabled);
        inputPassword.setEnabled(enabled);
        btnSignin.setEnabled(enabled);
        btnSignup.setEnabled(enabled);
    }
}
