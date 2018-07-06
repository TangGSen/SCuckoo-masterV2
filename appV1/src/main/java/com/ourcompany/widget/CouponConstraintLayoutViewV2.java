package com.ourcompany.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.ourcompany.R;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 下午5:21
 * Des    :
 */
public class CouponConstraintLayoutViewV2 extends ConstraintLayout {

    private  float mTypeRecfWidth;
    private  float mRoundRadius ;
    private Paint mPaint;
    /**
     * 圆间距
     */
    private float gap = 8;

    /**
     * 圆数量
     */
    private int circleNum;

    private float remain;
    private int heightSize;
    private int widthSize;
    private RectF bgRectF;
    private int mBgColor;
    private RectF typeRect;
    //分类的颜色标识
    private int mTypeClassColor;
    private float mCicleRadiusWidth;
    private int mCicleColor;
    private int dashRadius =10;


    public CouponConstraintLayoutViewV2(Context context) {
        this(context,null);
    }
    public CouponConstraintLayoutViewV2(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }
    public CouponConstraintLayoutViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mTypeRecfWidth = mRoundRadius = DisplayUtils.dip2px(8);
       // mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        mCicleColor = mBgColor = ResourceUtils.getResColor(R.color.whiles);
        mTypeClassColor = ResourceUtils.getResColor(R.color.gradient_end_red_v2);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CouponView);
      //  mShape = a.getInt(R.styleable.CouponView_shape, mShape);
        mRoundRadius = a.getDimension(R.styleable.CouponView_round_radius, mRoundRadius);
        mTypeRecfWidth = a.getDimension(R.styleable.CouponView_typeRecfWidth, mTypeRecfWidth);
        mCicleRadiusWidth = a.getDimension(R.styleable.CouponView_cicleRadiusWidth, mCicleRadiusWidth);
        mBgColor = a.getColor(R.styleable.CouponView_bgColor, mBgColor);
        mCicleColor = a.getColor(R.styleable.CouponView_cicleColor, mCicleColor);
        mTypeClassColor = a.getColor(R.styleable.CouponView_typeClassColor, mTypeClassColor);
        a.recycle();
    }

    public void setTypeClassColor(int color){
        this.mTypeClassColor = color ;
        postInvalidate();
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //画矩形
        bgRectF = new RectF(0.0f, 0.0f,w,h);
        typeRect = new RectF(0.0f, 0.0f,mTypeRecfWidth,getHeight());
        //处理前后的间距
        if (remain == 0) {
            remain = (int) (h - gap) % (2 * mCicleRadiusWidth + gap);
        }
        circleNum = (int) ((h - gap) / (2 * mCicleRadiusWidth + gap));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mBgColor);

        canvas.drawRoundRect(bgRectF,mRoundRadius,mRoundRadius,mPaint);


        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mPaint.setColor(mTypeClassColor);
        canvas.drawRect(typeRect,mPaint);
        mPaint.setXfermode(null);
        mPaint.setColor(mCicleColor);


        for (int i=0;i<circleNum;i++){
            float x = gap+mCicleRadiusWidth+remain/2+((gap+mCicleRadiusWidth*2)*i);
            canvas.drawCircle(0,x,mCicleRadiusWidth,mPaint);
          //  canvas.drawCircle(getWidth(),x,mCicleRadiusWidth,mPaint);
        }

    }

}