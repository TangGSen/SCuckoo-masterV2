package com.ourcompany.widget.recycleview.commadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.utils.ResourceUtils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/8/30 20:50
 * Des    :
 */

public class SViewHolder extends RecyclerView.ViewHolder {
    //减少viewfindbyid
    private SparseArray<View> viewCaches;

    public SViewHolder(View itemView) {
        super(itemView);
        viewCaches = new SparseArray<>();
    }

    public <V extends View> V getView(int viewId) {
        View view = viewCaches.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewCaches.put(viewId, view);
        }
        return (V) view;
    }

    /**
     * 设置TextView
     *
     * @param viewId
     * @param text
     * @return
     */
    public SViewHolder setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        } else {
            textView.setText("");
        }

        return this;
    }

    /**
     * 设置图片资源
     *
     * @param viewId
     * @param
     * @return
     */
    public SViewHolder setImage(int viewId, String url) {
        ImageView imageView = getView(viewId);
        imageView.setTag(R.id.loading_image_url, url);
        ImageLoader.getImageLoader().loadImage(imageView, url);
        return this;
    }

    /**
     * 设置圆角image
     * @param viewId
     * @param url
     * @return
     */
    public SViewHolder setImage(int viewId, String url,int round) {
        ImageView imageView = getView(viewId);
        imageView.setTag(R.id.loading_image_url, url);
        ImageLoader.getImageLoader().loadImageRound(imageView, url,round);
        return this;
    }

    /**
     * 设置圆角image
     * @param viewId
     * @param url
     * @return
     */
    public SViewHolder setImageWithErrorImage(int viewId, String url,int resid) {
        ImageView imageView = getView(viewId);
        imageView.setTag(R.id.loading_image_url, url);
        ImageLoader.getImageLoader().loadImage(imageView, url,resid);
        return this;
    }


    public SViewHolder setViewEnable(int viewId, boolean able) {
        View view = getView(viewId);
        view.setEnabled(able);
        return this;
    }

    public SViewHolder setImageResId(final int viewId, int resId) {
        ImageView imageView = getView(viewId);
       // imageView.setTag(R.id.loading_image_url, resId);
        imageView.setImageDrawable(ResourceUtils.getDrawable(resId));
        //ImageLoader.getImageLoader().loadImageRes(imageView, resId);

        return this;
    }
    //加载图片，定制流程，但是使用图片框架，看使用人

    public abstract static class HolderImageLoader{
        public String mPath;

        public HolderImageLoader() {
        }

        public abstract void loadImage(ImageView imageView, String path);
        public abstract void loadImage(ImageView imageView, String path,int resId);
        public abstract void loadImage(Context context, ImageView imageView, String path);

        public String getPath() {
            return mPath;
        }

        public abstract void loadImageRes(ImageView imageView, int resId);

        public abstract void loadImageRound(ImageView imageView, String url, int round);
    }


    /**
     * 设置item 监听
     * @param viewId
     * @param listener
     * @return
     */
    public SViewHolder setItemListener(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }
}
