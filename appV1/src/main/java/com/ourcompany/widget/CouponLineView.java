package com.ourcompany.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ourcompany.R;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 下午5:21
 * Des    :
 */
public class CouponLineView extends View {

    private Paint mPaint;
    private Paint mCirclePaint;

    public CouponLineView(Context context) {
        this(context,null);
    }
    public CouponLineView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }
    public CouponLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setDither(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(ResourceUtils.getResColor(R.color.colorBg));

    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //处理前后的间距
        int strokeWidth = DisplayUtils.dip2px(1);
        int dash = strokeWidth*2;
        mPaint.setStrokeWidth( strokeWidth);
        mPaint.setPathEffect(new DashPathEffect(new float[] {dash, dash}, 0));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        canvas.drawLine(centerX, 0, centerX, getHeight(), mPaint);

        canvas.drawCircle(getWidth()/2,0,getWidth()/2,mCirclePaint);
        canvas.drawCircle(getWidth()/2,getHeight(),getWidth()/2,mCirclePaint);
    }

}