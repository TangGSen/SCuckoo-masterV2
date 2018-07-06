package com.ourcompany.widget.recycleview.commadapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.ourcompany.R;
import com.ourcompany.utils.GlideRoundTransform;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/8/30 22:22
 * Des    :
 */

public class ImageLoader extends SViewHolder.HolderImageLoader {

    private volatile static ImageLoader imageLoader;

    private ImageLoader() {
    }

    public static ImageLoader getImageLoader(){
        if(imageLoader == null){
            synchronized (ImageLoader.class){
                if(imageLoader==null){
                    imageLoader = new ImageLoader();
                }
            }
        }
        return imageLoader;
    }

    public ImageLoader setPath (String path){
       this.mPath = path;
       return this;
    }

    @Override
    public void loadImage(ImageView imageView, String path) {
        Glide.with(imageView.getContext())
                .load((String) imageView.getTag(R.id.loading_image_url))
                .error(R.mipmap.photo)           //设置错误图片
                .dontAnimate()
                .placeholder(R.mipmap.photo)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

    @Override
    public void loadImage(ImageView imageView, String path, int resId) {
        Glide.with(imageView.getContext())
                .load((String) imageView.getTag(R.id.loading_image_url))
                .error(resId)           //设置错误图片
                .dontAnimate()
                .placeholder(resId)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String path) {
        Glide.with(context).load((String) imageView.getTag(R.id.loading_image_url)).error(R.mipmap.photo)           //设置错误图片
                .placeholder(R.mipmap.photo)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void loadImageRes(ImageView imageView, int resId) {
        Glide.with(imageView.getContext()).load(imageView.getTag(R.id.loading_image_url)).error(R.mipmap.photo)           //设置错误图片
                .placeholder(R.mipmap.photo)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void loadImageRound(ImageView imageView, String url, int round) {
        Glide.with(imageView.getContext()).load(imageView.getTag(R.id.loading_image_url)).error(R.mipmap.photo)           //设置错误图片
                .placeholder(R.mipmap.photo)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .transform(new CenterCrop(imageView.getContext()),new GlideRoundTransform(imageView.getContext(), round))
                .into(imageView);
    }
}
