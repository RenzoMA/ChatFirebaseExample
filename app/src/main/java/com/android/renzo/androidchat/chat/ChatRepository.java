package com.android.renzo.androidchat.chat;

/**
 * Created by HOME on 13/06/2016.
 */
public interface ChatRepository {

    void sendMessage(String msg);
    void setRecipient(String recipient);

    void subscribe();
    void unsubscribe();
    void destroyListener();
    void changeConnectionStatus(boolean online);
}
