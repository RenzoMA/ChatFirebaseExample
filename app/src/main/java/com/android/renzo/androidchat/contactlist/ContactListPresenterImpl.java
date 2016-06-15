package com.android.renzo.androidchat.contactlist;

import com.android.renzo.androidchat.contactlist.events.ContactListEvent;
import com.android.renzo.androidchat.contactlist.ui.ContactListView;
import com.android.renzo.androidchat.entities.User;
import com.android.renzo.androidchat.lib.EventBus;
import com.android.renzo.androidchat.lib.GreenRobotEventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by HOME on 12/06/2016.
 */
public class ContactListPresenterImpl implements  ContactListPresenter {

    private EventBus eventBus;
    private ContactListView view;
    private ContactListInteractor listInteractor;
    private ContactListSessionInteractor sessionInteractor;

    public ContactListPresenterImpl(ContactListView view) {
        this.view = view;
        eventBus = GreenRobotEventBus.getInstance();
        this.listInteractor = new ContactListinteractorImpl();
        this.sessionInteractor = new ContactListSessionInteractorImpl();

    }


    @Override
    public void onPause() {
        sessionInteractor.changeConnectionStatus(User.OFFLINE);
        listInteractor.unsubscribe();
    }

    @Override
    public void onResume() {
        sessionInteractor.changeConnectionStatus(User.ONLINE);
        listInteractor.subscribe();
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
        listInteractor.destroyListener();
    }

    @Override
    public void signOff() {
        sessionInteractor.changeConnectionStatus(User.OFFLINE);
        listInteractor.unsubscribe();
        listInteractor.destroyListener();
        sessionInteractor.signOff();
    }

    @Override
    public String getCurrentUserEmail() {
        return sessionInteractor.getCurrentUserEmail();
    }

    @Override
    public void removeContact(String email) {
        listInteractor.removeContact(email);
    }

    @Override
    @Subscribe
    public void onEventMainThread(ContactListEvent event) {
        User user = event.getUser();
        switch (event.getEventType()){
            case ContactListEvent.onContactAdded:
                onContactAdded(user);
                break;
            case ContactListEvent.onContactChanged:
                onContactChanged(user);
                break;
            case ContactListEvent.onContactRemoved:
                onContactRemoved(user);
                break;
        }
    }

    private void onContactAdded(User user){
        if(view != null){
            view.onContactAdded(user);
        }
    }
    private void onContactChanged(User user){
        if(view != null){
            view.onContactChanged(user);
        }
    }
    private void onContactRemoved(User user){
        if(view != null){
            view.onContactRemoved(user);
        }
    }
}
