package com.ourcompany.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ourcompany.R;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/13 16:56
 * Des    :
 */

public class DivideLineView extends View {

    private int resColorId;
    private float shapStart, paintStrokeWidth;
    private Path mPath;
    private Paint mPaint;
    private Path mPath3;
    private Path mPath2;

    public DivideLineView(Context context) {
        this(context, null);
    }

    public DivideLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DivideLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Division);
        if (typedArray != null) {
            resColorId = typedArray.getColor(R.styleable.Division_lineColor, -1);
            shapStart = typedArray.getDimension(R.styleable.Division_shapeStart, 20);
            paintStrokeWidth = typedArray.getDimension(R.styleable.Division_paintStrokeWidth, 4);
            typedArray.recycle();
        }

        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);

        mPaint.setColor(resColorId);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //貌似在转角线条大小不一致，所以分开三段了
        mPath = new Path();
        mPath.moveTo(0, h);
        mPath.lineTo(shapStart - h , h);


        mPath2 = new Path();
        mPath2.moveTo(shapStart - h , h);
        mPath2.lineTo(shapStart, 0);
        mPath2.lineTo(shapStart + h , h);

        mPath3 = new Path();
        mPath3.moveTo(shapStart + h , h);
        mPath3.lineTo(w, h);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(paintStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(mPath3, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(paintStrokeWidth/2);
        canvas.drawPath(mPath2, mPaint);
    }


}
