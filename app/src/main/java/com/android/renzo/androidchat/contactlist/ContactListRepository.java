package com.android.renzo.androidchat.contactlist;

/**
 * Created by HOME on 12/06/2016.
 */
public interface ContactListRepository {
    void signOff();
    String getCurrentUserEmail();
    void removeContact(String email);
    void subscribeToContactListEvents();
    void unsubscribeToContactListEvents();
    void changeConnectionStatus(boolean online);
    void destroyListener();
}
