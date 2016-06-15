package com.android.renzo.androidchat.contactlist.ui.adapters;

import com.android.renzo.androidchat.entities.User;

/**
 * Created by HOME on 12/06/2016.
 */
public interface OnItemClickListener {
    void onItemClick(User user);
    void onItemLongClick(User user);
}
