package com.ourcompany.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ourcompany.app.MApplication;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/26 15:41
 * Des    :
 */

public class ToastUtils {

    private static Toast mSimpleToast;
    private static Toast mImageToast;

    public static void showSimpleToast(String content){
        if(TextUtils.isEmpty(content))return;
        if(mSimpleToast==null){
            mSimpleToast= Toast.makeText(MApplication.mContext,content,Toast.LENGTH_SHORT);
        }else{
            mSimpleToast.setText(content);
        }
        mSimpleToast.show();
    }

    public static void  showImageToast( LinearLayout ll){
        if(mImageToast==null){
            mImageToast=new Toast(MApplication.mContext);
            mImageToast.setGravity(Gravity.BOTTOM,0,0);
            mImageToast.setView(ll);
            mImageToast.setDuration(Toast.LENGTH_SHORT);
        }else {
            mImageToast.setView(ll);
        }

        mImageToast.show();
    }
}
