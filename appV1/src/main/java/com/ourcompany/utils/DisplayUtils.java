package com.ourcompany.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;

import java.lang.reflect.Field;

/**
 * Created by Sen on 2016/3/1.
 */
public class DisplayUtils {

    private static DisplayUtils instance;
    private Activity mActivity;
    private DisplayUtils(Activity mActivity){
        this.mActivity=mActivity;
    }
    public static DisplayUtils getInstance(Activity mActivity){
        if(instance==null){
            instance=new DisplayUtils(mActivity);
        }
        return instance;
    }
    public final int[] getScreenSize(){
        int[] size=new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        size[0]=dm.widthPixels;
        size[1]=dm.heightPixels;
        return size;
    }
    public final static int getWindowWidth(Activity mActivity) {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public final static int getWindowHeight(Activity mActivity) {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
    public final static int getWindowHeight() {
        DisplayMetrics dm=MApplication.mContext.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public final static int getWindowWidth() {
        DisplayMetrics dm=MApplication.mContext.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = MApplication.mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取状态栏高度
     * @return 状态栏高度
     */
    public static int getStatusBarHeight() {
        final int defaultHeightInDp = 19;
        int height = DisplayUtils.dip2px(defaultHeightInDp);
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            height = MApplication.mContext.getResources().getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    public static int getDefaultKeyboardHeight() {
        return   MApplication.mContext.getResources().getDimensionPixelSize(R.dimen.default_keyboard_height);
    }
}