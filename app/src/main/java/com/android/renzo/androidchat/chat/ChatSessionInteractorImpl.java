package com.android.renzo.androidchat.chat;

/**
 * Created by HOME on 13/06/2016.
 */
public class ChatSessionInteractorImpl implements ChatSessionInteractor {
    ChatRepository repository;

    public ChatSessionInteractorImpl() {
        this.repository = new ChatRepositoryImpl();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        repository.changeConnectionStatus(online);
    }
}
