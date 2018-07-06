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
import android.support.annotation.NonNull;

import com.ourcompany.R;
import com.ourcompany.utils.ResourceUtils;
import com.scwang.smartrefresh.layout.internal.PaintDrawable;


/**
 * 箭头图像
 * Created by SCWANG on 2018/2/5.
 */

public class UpdateBgDrawable extends PaintDrawable {

    private Path mPath = new Path();
    public UpdateBgDrawable(int width,int height){
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(width,0);
        mPath.lineTo(width,height*3/4);
        mPath.quadTo(width/2, height, 0, height*3/4);
        mPath.close();
        mPaint.setColor(ResourceUtils.getResColor(R.color.colorPrimary));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        canvas.drawPath(mPath, mPaint);
    }
}

