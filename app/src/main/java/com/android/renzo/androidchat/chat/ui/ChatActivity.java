package com.android.renzo.androidchat.chat.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.renzo.androidchat.R;
import com.android.renzo.androidchat.chat.ChatPresenter;
import com.android.renzo.androidchat.chat.ChatPresenterImpl;
import com.android.renzo.androidchat.chat.ui.adapters.ChatAdapter;
import com.android.renzo.androidchat.domain.AvatarHelper;
import com.android.renzo.androidchat.entities.ChatMessage;
import com.android.renzo.androidchat.lib.GlideImageLoader;
import com.android.renzo.androidchat.lib.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Bind(R.id.activityRoot)
    CoordinatorLayout activityRoot;

    boolean subscribed=false;
    private String photoPath;
    public final static String EMAIL_KEY = "email";
    public final static String ONLINE_KEY = "online";

    private static final int REQUEST_PICTURE = 1;
    private static final int REQUEST_RESOLVE_ERROR = 0;
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
        setupSendButton();
    }

    private void setupRecyclerView() {

        final View activityRootView = findViewById(R.id.activityRoot);
        final int initialHeight = activityRootView.getHeight();
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean keyboardIsShown = false;

            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - initialHeight;
                if (heightDiff > 1 && !keyboardIsShown) {
                    messageRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    keyboardIsShown = true;
                } else {
                    keyboardIsShown = false;
                }
            }
        });
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupAdapter() {

        adapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
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
        presenter.onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(subscribed == false) {


        }

    }

    @Override
    protected void onDestroy() {
        //presenter.onDestroy();
        presenter.onPause();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //presenter.onPause();
       // presenter.onDestroy();
        //subscribed = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == REQUEST_PICTURE){
            if(resultCode == RESULT_OK){
                boolean fromCamera = (data == null || data.getData() == null);
                if(fromCamera){
                    addToGallery();
                } else {
                    photoPath = getRealPathFromURI(data.getData());
                }

                presenter.sendMessage(photoPath, "img");
            }
        }
    }

    public void setupSendButton() {
        btnSendMessage.setBackgroundResource(android.R.drawable.ic_menu_camera);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        editTxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTxtMessage.getText().toString().isEmpty()) {
                    btnSendMessage.setBackgroundResource(android.R.drawable.ic_menu_camera);
                    btnSendMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            takePicture();
                        }
                    });
                } else {
                    btnSendMessage.setBackgroundResource(android.R.drawable.ic_menu_send);
                    btnSendMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            presenter.sendMessage(editTxtMessage.getText().toString(),"txt");
                            editTxtMessage.setText("");
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onMessageReceived(ChatMessage msg) {
        adapter.add(msg);
        messageRecyclerView.scrollToPosition(adapter.getItemCount() - 1);

    }

    @OnClick(R.id.btnBack)
    @Override
    public void backToList() {
        finish();
    }

    public void takePicture() {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra("return-data", true);
        File photoFile = getFile();
        if (photoFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                intentList = addIntentsToList(intentList, cameraIntent);
            }
        }

        if (pickIntent.resolveActivity(getPackageManager()) != null) {
            intentList = addIntentsToList(intentList, pickIntent);
        }

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    getString(R.string.chat_message_picture_source));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        if (chooserIntent != null) {
            startActivityForResult(chooserIntent, REQUEST_PICTURE);
        }

    }

    private File getFile() {
        File photoFile = null;
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            showSnackbar(R.string.chat_error_dispatch_camera);
        }
        photoPath = photoFile.getAbsolutePath();
        return photoFile;
    }

    private List<Intent> addIntentsToList(List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetIntent = new Intent(intent);
            targetIntent.setPackage(packageName);
            list.add(targetIntent);
        }
        return list;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = null;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            if (contentURI.toString().contains("mediaKey")) {
                cursor.close();

                try {
                    File file = File.createTempFile("tempImg", ".jpg", getCacheDir());
                    InputStream input = getContentResolver().openInputStream(contentURI);
                    OutputStream output = new FileOutputStream(file);

                    try {
                        byte[] buffer = new byte[4 * 1024];
                        int read;

                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        result = file.getAbsolutePath();
                    } finally {
                        output.close();
                        input.close();
                    }

                } catch (Exception e) {
                }
            } else {
                cursor.moveToFirst();
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(dataColumn);
                cursor.close();
            }

        }
        return result;
    }

    private void addToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(photoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

    }

    private void showSnackbar(String msg) {
        Snackbar.make(activityRoot, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(int strResource) {
        Snackbar.make(activityRoot, strResource, Snackbar.LENGTH_SHORT).show();
    }

}
