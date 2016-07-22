package com.android.renzo.androidchat.chat.ui.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.renzo.androidchat.R;
import com.android.renzo.androidchat.entities.ChatMessage;
import com.android.renzo.androidchat.lib.GlideImageLoader;
import com.android.renzo.androidchat.lib.ImageLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HOME on 13/06/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<ChatMessage> chatMessages;
    private ImageLoader imageLoader;


    public ChatAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
        imageLoader = new GlideImageLoader(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ChatMessage chatMessage = chatMessages.get(position);
        String msg = chatMessage.getMsg();

        int gravity;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.txtMessage.getLayoutParams();

        if(!chatMessage.isSentByMe()){
            gravity = Gravity.LEFT;
            params.rightMargin = (int)context.getResources().getDimension(R.dimen.activity_chat_message_margin);
            params.leftMargin = 0;
        }
        else{
            gravity = Gravity.END;
            params.leftMargin = (int)context.getResources().getDimension(R.dimen.activity_chat_message_margin);
            params.rightMargin = 0;
        }
        params.gravity = gravity;



        if(chatMessage.getType().equals("txt")){

            holder.imgMsg.setVisibility(View.GONE);
            holder.txtMessage.setVisibility(View.VISIBLE);

            holder.txtMessage.setText(msg);
            if(!chatMessage.isSentByMe()){
                holder.txtMessage.setBackgroundResource(R.drawable.background_content_chat);
            }
            else{
                holder.txtMessage.setBackgroundResource(R.drawable.background_content_chat_recipient);
            }
            holder.txtMessage.setLayoutParams(params);
        }
        else
        {
            holder.txtMessage.setVisibility(View.GONE);
            holder.imgMsg.setVisibility(View.VISIBLE);

            imageLoader.loadImgChat(chatMessage.getMsg(),holder.imgMsg);
            holder.imgMsg.setLayoutParams(params);
        }


    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void add(ChatMessage msg) {
        if(!chatMessages.contains(msg)){
            chatMessages.add(msg);
            notifyDataSetChanged();
        }
    }
    private int fetchColor(int color){
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data,
                new int[] {color});
        int returnColor = a.getColor(0,0);
        a.recycle();
        return returnColor;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder {
        @Bind(R.id.txtMessage)
        TextView txtMessage;

        @Bind(R.id.imgMsg)
        ImageView imgMsg;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
