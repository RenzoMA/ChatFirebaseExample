package com.android.renzo.androidchat.contactlist;

/**
 * Created by HOME on 12/06/2016.
 */
public interface ContactListSessionInteractor {
    void signOff();
    String getCurrentUserEmail();
    void changeConnectionStatus(boolean online);
}
