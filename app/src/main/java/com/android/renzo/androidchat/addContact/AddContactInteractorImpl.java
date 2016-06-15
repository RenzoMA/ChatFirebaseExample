package com.android.renzo.androidchat.addContact;

/**
 * Created by HOME on 12/06/2016.
 */
public class AddContactInteractorImpl implements AddContactInteractor {

    AddContactRepository repository;

    public AddContactInteractorImpl() {
        repository = new AddContactRepositoryImpl();

    }

    @Override
    public void execute(String email) {
        repository.addContact(email);
    }
}
