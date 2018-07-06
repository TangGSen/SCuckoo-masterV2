package com.ourcompany.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 下午5:21
 * Des    :
 */
public class CouponConstraintLayoutView  extends ConstraintLayout {

    private Paint mPaint;
    /**
     * 圆间距
     */
    private float gap = 8;
    /**
     * 半径
     */
    private float radius = 10;
    /**
     * 圆数量
     */
    private int circleNum;

    private float remain;


    public CouponConstraintLayoutView(Context context) {
        this(context,null);
    }
    public CouponConstraintLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }
    public CouponConstraintLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

       // mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //处理前后的间距
        if (remain == 0) {
            remain = (int) (h - gap) % (2 * radius + gap);
        }
        circleNum = (int) ((h - gap) / (2 * radius + gap));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i=0;i<circleNum;i++){
            float x = gap+radius+remain/2+((gap+radius*2)*i);
            //画横向的凹凸
//            canvas.drawCircle(x,0,radius,mPaint);
//            canvas.drawCircle(x,getHeight(),radius,mPaint);
            //画纵向的凹凸

            canvas.drawCircle(0,x,radius,mPaint);
            canvas.drawCircle(getWidth(),x,radius,mPaint);

        }

    }

}