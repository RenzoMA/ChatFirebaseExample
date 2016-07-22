package com.android.renzo.androidchat.chat;

import com.android.renzo.androidchat.chat.events.ChatEvent;

/**
 * Created by HOME on 13/06/2016.
 */
public interface ChatPresenter {
    void onPause();
    void onResume();
    void onCreate();
    void onDestroy();

    void sendMessage(String msg,String type);
    void onEventMainThread(ChatEvent event);
    void setChatRecipient(String recipient);
    void backToList();

}
