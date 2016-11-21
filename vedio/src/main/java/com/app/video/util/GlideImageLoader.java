package com.app.video.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by liubohua on 16/11/18.
 */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);

        //用fresco加载图片简单用法
        Uri uri = Uri.parse((String) path);
        imageView.setImageURI(uri);
    }

}
