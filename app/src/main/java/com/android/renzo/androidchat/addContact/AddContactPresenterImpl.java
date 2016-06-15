package com.android.renzo.androidchat.addContact;

import com.android.renzo.androidchat.addContact.AddContactPresenter;
import com.android.renzo.androidchat.addContact.events.AddContactEvent;
import com.android.renzo.androidchat.addContact.ui.AddContactView;
import com.android.renzo.androidchat.lib.EventBus;
import com.android.renzo.androidchat.lib.GreenRobotEventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by HOME on 12/06/2016.
 */
public class AddContactPresenterImpl implements AddContactPresenter {


    private AddContactView view;
    private EventBus eventBus;
    private AddContactInteractor interactor;

    public AddContactPresenterImpl(AddContactView view) {
        this.view = view;
        this.eventBus = GreenRobotEventBus.getInstance();
        this.interactor = new AddContactInteractorImpl();
    }

    @Override
    public void onShow() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void addContact(String email) {
        if(view != null){
            view.hideInput();
            view.hideProgress();
        }
        interactor.execute(email);
    }

    @Override
    @Subscribe
    public void onEventMainThread(AddContactEvent event) {
        if(view != null){
            view.hideProgress();
            view.showInput();
            if(event.isError()){
                view.contactNotAdded();
            }
            else{
                view.contactAdded();
            }
        }
    }
}
