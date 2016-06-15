package com.android.renzo.androidchat.contactlist;

/**
 * Created by HOME on 12/06/2016.
 */
public interface ContactListInteractor {
    void subscribe();
    void unsubscribe();
    void destroyListener();
    void removeContact(String email);
}
