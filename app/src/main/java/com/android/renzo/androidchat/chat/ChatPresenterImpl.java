package com.android.renzo.androidchat.chat;

import com.android.renzo.androidchat.chat.events.ChatEvent;
import com.android.renzo.androidchat.chat.ui.ChatView;
import com.android.renzo.androidchat.entities.User;
import com.android.renzo.androidchat.lib.EventBus;
import com.android.renzo.androidchat.lib.GreenRobotEventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by HOME on 13/06/2016.
 */
public class ChatPresenterImpl implements  ChatPresenter {

    private EventBus eventBus;
    private ChatView view;
    private ChatInteractor chatInteractor;
    private ChatSessionInteractor sessionInteractor;

    public ChatPresenterImpl(ChatView view) {
        this.view = view;
        this.eventBus = GreenRobotEventBus.getInstance();
        this.chatInteractor = new ChatInteractorImpl();
        this.sessionInteractor = new ChatSessionInteractorImpl();
    }

    @Override
    public void onPause() {
        chatInteractor.unsubscribe();
        sessionInteractor.changeConnectionStatus(User.OFFLINE);
    }

    @Override
    public void onResume() {
        chatInteractor.subscribe();
        sessionInteractor.changeConnectionStatus(User.ONLINE);
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        chatInteractor.destroyListener();
        view = null;
    }

    @Override
    public void sendMessage(String msg) {
        chatInteractor.sendMessage(msg);
    }

    @Override
    @Subscribe
    public void onEventMainThread(ChatEvent event) {
        if(view != null){
            view.onMessageReceived(event.getMessage());
        }
    }

    @Override
    public void setChatRecipient(String recipient) {
        chatInteractor.setRecipient(recipient);
    }
}
