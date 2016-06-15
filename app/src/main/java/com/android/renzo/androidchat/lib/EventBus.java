package com.android.renzo.androidchat.lib;

/**
 * Created by HOME on 11/06/2016.
 */
public interface EventBus {
    void register(Object subscriber);
    void unregister(Object subscriber);
    void post(Object event);
}
