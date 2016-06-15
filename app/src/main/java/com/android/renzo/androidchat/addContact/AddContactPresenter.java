package com.android.renzo.androidchat.addContact;

import com.android.renzo.androidchat.addContact.events.AddContactEvent;

/**
 * Created by HOME on 12/06/2016.
 */
public interface AddContactPresenter {
    void onShow();
    void onDestroy();

    void addContact(String email);
    void onEventMainThread(AddContactEvent event);
}
