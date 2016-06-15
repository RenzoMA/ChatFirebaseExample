package com.android.renzo.androidchat.chat;

import com.android.renzo.androidchat.chat.events.ChatEvent;
import com.android.renzo.androidchat.domain.FirebaseHelper;
import com.android.renzo.androidchat.entities.ChatMessage;
import com.android.renzo.androidchat.lib.EventBus;
import com.android.renzo.androidchat.lib.GreenRobotEventBus;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by HOME on 13/06/2016.
 */
public class ChatRepositoryImpl implements ChatRepository {

    private String recipient;
    private FirebaseHelper helper;
    private ChildEventListener chatEventListener;
    private EventBus eventBus;

    public ChatRepositoryImpl() {
        this.helper = FirebaseHelper.getInstance();
        this.eventBus = GreenRobotEventBus.getInstance();

    }

    @Override
    public void sendMessage(String msg) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(helper.getAuthUserEmail());
        chatMessage.setMsg(msg);

        Firebase chatsReference = helper.getChatsReference(recipient);
        chatsReference.push().setValue(chatMessage);
    }

    @Override
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public void subscribe() {
        if(chatEventListener == null){
            chatEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    String msgSender = chatMessage.getSender();
                    chatMessage.setSentByMe(msgSender.equals(helper.getAuthUserEmail()));

                    ChatEvent chatEvent = new ChatEvent();
                    chatEvent.setMessage(chatMessage);
                    eventBus.post(chatEvent);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
            helper.getChatsReference(recipient).addChildEventListener(chatEventListener);
        }

    }

    @Override
    public void unsubscribe() {
        if(chatEventListener != null){
            helper.getChatsReference(recipient).removeEventListener(chatEventListener);
        }
    }

    @Override
    public void destroyListener() {
        chatEventListener = null;
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        helper.changeUserConnectionStatus(online);
    }
}
