package com.android.renzo.androidchat.contactlist;

/**
 * Created by HOME on 12/06/2016.
 */
public class ContactListSessionInteractorImpl implements ContactListSessionInteractor {

    ContactListRepository repository;

    public ContactListSessionInteractorImpl() {
        repository = new ContactListRepositoryImpl();
    }

    @Override
    public void signOff() {
        repository.signOff();
    }

    @Override
    public String getCurrentUserEmail() {
        return repository.getCurrentUserEmail();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        repository.changeConnectionStatus(online);
    }
}
