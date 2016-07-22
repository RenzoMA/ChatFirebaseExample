package com.android.renzo.androidchat.entities;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by HOME on 13/06/2016.
 */
@JsonIgnoreProperties({"sentByMe"})
public class ChatMessage {

    @JsonIgnore
    private String id;

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @JsonIgnore
    private String imagen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String msg;
    private String sender;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private boolean sentByMe;

    public ChatMessage() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }

    public void setSentByMe(boolean sentByMe) {
        this.sentByMe = sentByMe;
    }
    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        if(obj instanceof User){
            ChatMessage msg = (ChatMessage)obj;
            equal = this.sender.equals(msg.getSender()) && this.msg.equals(msg.getMsg())&& this.sentByMe == msg.isSentByMe();
        }
        return equal;
    }
}
