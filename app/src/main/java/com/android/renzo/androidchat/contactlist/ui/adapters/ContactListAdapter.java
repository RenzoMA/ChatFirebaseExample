package com.android.renzo.androidchat.contactlist.ui.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.renzo.androidchat.R;
import com.android.renzo.androidchat.domain.AvatarHelper;
import com.android.renzo.androidchat.entities.User;
import com.android.renzo.androidchat.lib.ImageLoader;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HOME on 12/06/2016.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {


    private List<User> contactList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;

    public ContactListAdapter(List<User> contactList, ImageLoader imageLoading, OnItemClickListener onItemClickListener) {
        this.contactList = contactList;
        this.imageLoader = imageLoading;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_contact, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = contactList.get(position);
        holder.setClickListener(user, onItemClickListener);

        String email = user.getEmail();
        boolean online = user.isOnline();
        String status = online ? "online" : "offline";
        int color = online ? Color.GREEN : Color.RED;

        holder.txtUser.setText(email);
        holder.txtStatus.setText(status);
        holder.txtStatus.setTextColor(color);

        imageLoader.load(holder.imgAvatar, AvatarHelper.getAvatarUrl(email));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void add(User user) {
        if(!contactList.contains(user)){
            contactList.add(user);
            notifyDataSetChanged();
        }
    }

    public void update(User user) {
        if(contactList.contains(user)){
            int index = contactList.indexOf(user);
            contactList.set(index, user);
            notifyDataSetChanged();
        }
    }

    public void remove(User user) {
        if(contactList.contains(user)){
            contactList.remove(user);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imgAvatar)
        CircleImageView imgAvatar;
        @Bind(R.id.txtUser)
        TextView txtUser;
        @Bind(R.id.txtStatus)
        TextView txtStatus;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);

        }

        private void setClickListener(final User user,final OnItemClickListener listener){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(user);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(user);
                    return false;
                }
            });
        }
    }
}
