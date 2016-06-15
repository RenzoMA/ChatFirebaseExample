package com.android.renzo.androidchat.chat.events;

import com.android.renzo.androidchat.entities.ChatMessage;

/**
 * Created by HOME on 13/06/2016.
 */
public class ChatEvent {

    ChatMessage message;

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }
}
