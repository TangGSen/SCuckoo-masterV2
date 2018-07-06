package com.ourcompany.widget.calender;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/21 下午4:26
 * Des    :
 */
public class CalenderLineyout extends LinearLayout {
    public CalenderLineyout(Context context) {
        super(context);
    }

    public CalenderLineyout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CalenderLineyout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //传入参数widthMeasureSpec、heightMeasureSpec
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(width>height){
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }else{
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }
}
