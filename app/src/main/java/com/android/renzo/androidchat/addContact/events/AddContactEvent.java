package com.android.renzo.androidchat.addContact.events;

/**
 * Created by HOME on 12/06/2016.
 */
public class AddContactEvent {
    boolean error = false;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
