package com.android.renzo.androidchat.chat.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.renzo.androidchat.R;
import com.android.renzo.androidchat.chat.ui.adapters.ChatAdapter;
import com.android.renzo.androidchat.chat.ChatPresenter;
import com.android.renzo.androidchat.chat.ChatPresenterImpl;
import com.android.renzo.androidchat.domain.AvatarHelper;
import com.android.renzo.androidchat.entities.ChatMessage;
import com.android.renzo.androidchat.lib.GlideImageLoader;
import com.android.renzo.androidchat.lib.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements ChatView {

    @Bind(R.id.imgAvatar)
    CircleImageView imgAvatar;
    @Bind(R.id.txtUser)
    TextView txtUser;
    @Bind(R.id.txtStatus)
    TextView txtStatus;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.messageRecyclerView)
    RecyclerView messageRecyclerView;
    @Bind(R.id.editTxtMessage)
    EditText editTxtMessage;
    @Bind(R.id.btnSendMessage)
    ImageButton btnSendMessage;

    public final static String EMAIL_KEY = "email";
    public final static String ONLINE_KEY = "online";

    private ChatPresenter presenter;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        presenter = new ChatPresenterImpl(this);
        presenter.onCreate();
        
        setupAdapter();
        setupRecyclerView();
        setupToolbar(getIntent());

    }

    private void setupRecyclerView() {

        final View activityRootView = findViewById(R.id.activityRoot);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean keyboardIsShown = false;
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100 && !keyboardIsShown) {
                    messageRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    keyboardIsShown = true;
                }else{
                    keyboardIsShown = false;
                }
            }
        });
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupAdapter() {

        adapter = new ChatAdapter(this,new ArrayList<ChatMessage>());
        messageRecyclerView.setAdapter(adapter);
    }

    private void setupToolbar(Intent i) {
        String recipient = i.getStringExtra(EMAIL_KEY);
        presenter.setChatRecipient(recipient);

        boolean online = i.getBooleanExtra(ONLINE_KEY, false);
        String status = online ? "online" : "offline";
        int color = online ? Color.GREEN : Color.RED;

        txtUser.setText(recipient);
        txtStatus.setText(status);
        txtStatus.setTextColor(color);

        ImageLoader imageLoader = new GlideImageLoader(this);
        imageLoader.load(imgAvatar, AvatarHelper.getAvatarUrl(recipient));

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }



    @Override
    public void onMessageReceived(ChatMessage msg) {
        adapter.add(msg);
        messageRecyclerView.scrollToPosition(adapter.getItemCount() - 1);

    }

    @OnClick(R.id.btnSendMessage)
    public void sendMessage(){
        presenter.sendMessage(editTxtMessage.getText().toString());
        editTxtMessage.setText("");
    }
}
