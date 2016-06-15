package com.android.renzo.androidchat.addContact.ui;

/**
 * Created by HOME on 12/06/2016.
 */
public interface AddContactView {
    void showInput();
    void hideInput();
    void showProgress();
    void hideProgress();

    void contactAdded();
    void contactNotAdded();

}
