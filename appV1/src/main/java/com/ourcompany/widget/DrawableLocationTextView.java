package com.ourcompany.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/28 11:15
 * Des    :
 */

public class DrawableLocationTextView extends AppCompatTextView {
    public DrawableLocationTextView(Context context) {
        super(context);
    }

    public DrawableLocationTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableLocationTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取TextView的Drawable对象，返回的数组长度应该是4，对应左上右下
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawable = drawables[0];
            if (drawable != null) {
                // 当左边Drawable的不为空时，测量要绘制文本的宽度
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawable.getIntrinsicWidth();
                drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicWidth());
                // 计算总宽度（文本宽度 + drawablePadding + drawableWidth）
                setCompoundDrawables(drawable,null,null,null);
                // 移动画布开始绘制的X轴
            }
        }
        super.onDraw(canvas);
    }

}
