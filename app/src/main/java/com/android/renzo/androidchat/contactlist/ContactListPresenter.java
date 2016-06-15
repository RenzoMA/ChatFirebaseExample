package com.android.renzo.androidchat.contactlist;

import com.android.renzo.androidchat.contactlist.events.ContactListEvent;

/**
 * Created by HOME on 12/06/2016.
 */
public interface ContactListPresenter {
    void onPause();
    void onResume();
    void onCreate();
    void onDestroy();
    void signOff();
    String getCurrentUserEmail();
    void removeContact(String email);
    void onEventMainThread(ContactListEvent event);
}
