package com.android.renzo.androidchat.lib;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HOME on 12/06/2016.
 */
public interface ImageLoader {
    void load(ImageView imgAvatar, String url);
    void loadImgChat(String url,ImageView imageView);
    void setOnFinishedImageLoadingListener(Object listener);

}
