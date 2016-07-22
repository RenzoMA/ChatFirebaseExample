package com.android.renzo.androidchat.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import com.android.renzo.androidchat.chat.events.ChatEvent;
import com.android.renzo.androidchat.domain.FirebaseHelper;
import com.android.renzo.androidchat.entities.ChatMessage;
import com.android.renzo.androidchat.lib.CloudinaryImageStorage;
import com.android.renzo.androidchat.lib.EventBus;
import com.android.renzo.androidchat.lib.GreenRobotEventBus;
import com.android.renzo.androidchat.lib.ImageStorage;
import com.android.renzo.androidchat.lib.ImageStorageFinishedListener;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by HOME on 13/06/2016.
 */
public class ChatRepositoryImpl implements ChatRepository {

    private String recipient;
    private FirebaseHelper helper;
    private ChildEventListener chatEventListener;
    private EventBus eventBus;
    private ImageStorage imageStorage;

    public ChatRepositoryImpl() {
        this.helper = FirebaseHelper.getInstance();
        this.eventBus = GreenRobotEventBus.getInstance();
        this.imageStorage = new CloudinaryImageStorage();

    }

    public Bitmap getBitmap(String path){
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(path, opts);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        return rotatedBitmap;
    }

    @Override
    public void sendMessage(String msg,String type) {
        final Firebase chatsReference = helper.getChatsReference(recipient);
        final String id= chatsReference.push().getKey();
        final ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(id);
        chatMessage.setSender(helper.getAuthUserEmail());
        chatMessage.setType(type);
        switch (type){
            case "txt":
                chatMessage.setMsg(msg);
                chatsReference.child(chatMessage.getId()).setValue(chatMessage);
                break;
            case "img":

                File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                Bitmap b= getBitmap(msg);

                int width = b.getWidth();
                int height = b.getHeight();
                while(width > 400){
                    width = width / 2;
                    height = height / 2;
                }


                Bitmap out = Bitmap.createScaledBitmap(b, width, height, true);

                File file = new File(dir,"temp.jpg");
                FileOutputStream fOut;
                try {
                    fOut = new FileOutputStream(file);
                    out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    b.recycle();
                    out.recycle();
                } catch (Exception e) {
                    String prueba = e.getLocalizedMessage();
                }

                String resizePath = file.getAbsolutePath();

                ImageStorageFinishedListener listener = new ImageStorageFinishedListener() {
                    @Override
                    public void onSuccess() {
                        String url = imageStorage.getImageUrl(id);
                        chatMessage.setMsg(url);
                        chatsReference.child(chatMessage.getId()).setValue(chatMessage);
                    }

                    @Override
                    public void onError(String error) {

                    }
                };
                imageStorage.upload(new File(resizePath), id, listener);
                break;
        }





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
