package com.android.renzo.androidchat.chat;

/**
 * Created by HOME on 13/06/2016.
 */
public class ChatInteractorImpl implements ChatInteractor {

    ChatRepository repository;

    public ChatInteractorImpl() {
        this.repository = new ChatRepositoryImpl();
    }

    @Override
    public void sendMessage(String msg, String type) {
        if(!msg.trim().isEmpty()) {
            repository.sendMessage(msg,type);
        }
    }

    @Override
    public void setRecipient(String recipient) {
        repository.setRecipient(recipient);
    }

    @Override
    public void subscribe() {
        repository.subscribe();
    }

    @Override
    public void unsubscribe() {
        repository.unsubscribe();
    }


    @Override
    public void destroyListener() {
        repository.destroyListener();
    }
}
