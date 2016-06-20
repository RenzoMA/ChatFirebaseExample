package com.android.renzo.androidchat.chat.ui;

import com.android.renzo.androidchat.entities.ChatMessage;

/**
 * Created by HOME on 13/06/2016.
 */
public interface ChatView {
    void onMessageReceived(ChatMessage msg);
    void backToList();

}
