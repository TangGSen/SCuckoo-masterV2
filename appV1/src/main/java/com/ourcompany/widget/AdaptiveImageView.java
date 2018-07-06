package com.ourcompany.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/5/28 下午4:26
 * Des    :
 */
public class AdaptiveImageView extends AppCompatImageView {
    public AdaptiveImageView(Context context) {//java代码new对象使用
        super(context);
    }

    public AdaptiveImageView(Context context, @Nullable AttributeSet attrs) {
        //xml中布局使用
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        float drawHeight = drawable.getIntrinsicHeight();
        float drawWidth = drawable.getIntrinsicWidth();
        // 控件的宽度width = 屏幕的宽度 MeasureSpec.getSize(widthMeasureSpec);
        // 控件的高度 = 控件的宽度width*图片的宽高比 drawHeight / drawWidth；
        int height = (int) Math.ceil(width * (drawHeight / drawWidth));
        setMeasuredDimension(width, height);
    }
}
