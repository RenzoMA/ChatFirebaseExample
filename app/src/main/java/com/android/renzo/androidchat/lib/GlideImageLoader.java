package com.android.renzo.androidchat.lib;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

/**
 * Created by HOME on 12/06/2016.
 */
public class GlideImageLoader implements ImageLoader {

    private RequestManager requestManager;
    private RequestListener onFinishedLoadingListener;

    public GlideImageLoader(Context context) {
        this.requestManager = Glide.with(context);

    }

    @Override
    public void load(ImageView imgAvatar, String url) {
        requestManager.load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(onFinishedLoadingListener).into(imgAvatar);
    }
    @Override
    public void loadImgChat(String url,ImageView imageView) {
        requestManager.load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    @Override
    public void setOnFinishedImageLoadingListener(Object listener) {
        if(listener instanceof RequestListener) {
            this.onFinishedLoadingListener = (RequestListener) listener;
        }
    }
}
