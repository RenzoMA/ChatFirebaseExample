package com.android.renzo.androidchat.contactlist;

import com.android.renzo.androidchat.contactlist.events.ContactListEvent;
import com.android.renzo.androidchat.domain.FirebaseHelper;
import com.android.renzo.androidchat.entities.User;
import com.android.renzo.androidchat.lib.EventBus;
import com.android.renzo.androidchat.lib.GreenRobotEventBus;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;


/**
 * Created by HOME on 12/06/2016.
 */
public class ContactListRepositoryImpl implements ContactListRepository {

    private FirebaseHelper helper;
    private ChildEventListener contactEventListener;
    private EventBus eventBus;

    public ContactListRepositoryImpl() {
        this.helper = FirebaseHelper.getInstance();
        this.eventBus = GreenRobotEventBus.getInstance();
    }

    @Override
    public void signOff() {
        helper.signOff();
    }

    @Override
    public String getCurrentUserEmail() {
        return helper.getAuthUserEmail();
    }

    @Override
    public void removeContact(String email) {
        String currentUserEmail = helper.getAuthUserEmail();
        helper.getOneContactReference(currentUserEmail, email).removeValue();
        helper.getOneContactReference(email,currentUserEmail).removeValue();
    }

    @Override
    public void subscribeToContactListEvents() {

        if(contactEventListener == null){
            contactEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    handleContact(dataSnapshot,ContactListEvent.onContactAdded);
                }


                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    handleContact(dataSnapshot,ContactListEvent.onContactChanged);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    handleContact(dataSnapshot,ContactListEvent.onContactRemoved);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            };
        }
        helper.getMyContactsReference().addChildEventListener(contactEventListener);

    }

    @Override
    public void unsubscribeToContactListEvents() {

        if(contactEventListener != null) {
            helper.getMyContactsReference().removeEventListener(contactEventListener);

        }
    }

    private void handleContact(DataSnapshot dataSnapshot, int type) {
        String email = dataSnapshot.getKey();
        email = email.replace("_",".");
        boolean online = ((Boolean)dataSnapshot.getValue()).booleanValue();
        User user = new User();
        user.setEmail(email);
        user.setOnline(online);
        post(type, user);
    }

    private void post(int type, User user) {
        ContactListEvent event = new ContactListEvent();
        event.setEventType(type);
        event.setUser(user);
        eventBus.post(event);

    }

    @Override
    public void changeConnectionStatus(boolean online) {
        helper.changeUserConnectionStatus(online);
    }

    @Override
    public void destroyListener() {
        contactEventListener = null;
    }
}
