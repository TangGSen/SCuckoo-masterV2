package com.ourcompany.widget.drawable;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/19 23:13
 * Des    :
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.ourcompany.R;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;
import com.scwang.smartrefresh.layout.internal.PaintDrawable;


/**
 * 箭头图像
 * Created by SCWANG on 2018/2/5.
 */

public class CouponAddBgDrawable extends PaintDrawable {

    private Path mPath = new Path();
    private float widthSize;
    private float heightSize;
    private float ridus;
    public CouponAddBgDrawable(int width, int height,int ridus){
        this.widthSize = width;
        this.heightSize = height;
        this.ridus = ridus;
        mPaint.setColor(ResourceUtils.getResColor(R.color.whiles));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //三角形的高
        int h = DisplayUtils.dip2px(8);
        int w = DisplayUtils.dip2px(8);
        int startX = (int) (widthSize/4*3-w);
        int middleX = (int) (widthSize/4*3);
        int endX = (int) (widthSize/4*3+w);
        //画矩形
        RectF rectF = new RectF(0, h,widthSize,heightSize);
        canvas.drawRoundRect(rectF,ridus,ridus,mPaint);
        //画三角形（这里是基于path路径的绘制）
        Path path = new Path();
        path.moveTo(startX, h);
        path.lineTo(middleX, 0);
        path.lineTo(endX, h);
        path.close();
        canvas.drawPath(path, mPaint);
    }
}

