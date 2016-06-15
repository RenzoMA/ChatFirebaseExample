package com.android.renzo.androidchat.chat.ui.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.renzo.androidchat.R;
import com.android.renzo.androidchat.entities.ChatMessage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HOME on 13/06/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<ChatMessage> chatMessages;


    public ChatAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        String msg = chatMessage.getMsg();
        holder.txtMessage.setText(msg);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.txtMessage.getLayoutParams();

        int gravity = Gravity.LEFT;

        if(!chatMessage.isSentByMe()){
            gravity = Gravity.RIGHT;
            holder.txtMessage.setBackgroundResource(R.drawable.background_content_chat);
            params.leftMargin = (int)context.getResources().getDimension(R.dimen.activity_chat_message_margin);
            params.rightMargin = 0;
        }
        else{
            holder.txtMessage.setBackgroundResource(R.drawable.background_content_chat_recipient);
            params.rightMargin = (int)context.getResources().getDimension(R.dimen.activity_chat_message_margin);
            params.leftMargin = 0;
        }

        params.gravity = gravity;
        holder.txtMessage.setLayoutParams(params);

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
        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
