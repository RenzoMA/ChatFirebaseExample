package com.android.renzo.androidchat.contactlist.ui;

import com.android.renzo.androidchat.entities.User;

/**
 * Created by HOME on 12/06/2016.
 */
public interface ContactListView {
    void onContactAdded(User user);
    void onContactChanged(User user);
    void onContactRemoved(User user);
}
