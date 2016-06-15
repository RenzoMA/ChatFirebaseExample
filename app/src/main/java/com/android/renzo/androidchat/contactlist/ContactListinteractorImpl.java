package com.android.renzo.androidchat.contactlist;

/**
 * Created by HOME on 12/06/2016.
 */
public class ContactListinteractorImpl implements ContactListInteractor {

    ContactListRepository repository;

    public ContactListinteractorImpl() {
        repository = new ContactListRepositoryImpl();
    }

    @Override
    public void subscribe() {
        repository.subscribeToContactListEvents();
    }

    @Override
    public void unsubscribe() {
        repository.unsubscribeToContactListEvents();
    }

    @Override
    public void destroyListener() {
        repository.destroyListener();
    }

    @Override
    public void removeContact(String email) {
        repository.removeContact(email);
    }
}
