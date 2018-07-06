package com.ourcompany.utils;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourcompany.R;

import java.lang.reflect.Field;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/6 21:10
 * Des    :
 */

public class TabLayoutIndicatorWith {

    public static void  resetWith(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = DisplayUtils.dip2px( 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void resetHeight(final ViewGroup view,TabLayout tabLayout) {

        for (int i = 0; i < view.getChildCount(); i++) {
            View child = view.getChildAt(i);
            ViewGroup.MarginLayoutParams lp = ((ViewGroup.MarginLayoutParams) child.getLayoutParams());
            lp.height = 300;
            lp.width = 300;
            if (child instanceof ViewGroup) {

                child.setLayoutParams(lp);
                resetHeight((ViewGroup) child,tabLayout);
            }
            else if (child instanceof ImageView) {
                LogUtils.e("sen", "找到了i");
                lp.height = 200;
                lp.width = 200;
                child.setBackground(ResourceUtils.getDrawable(R.drawable.bg_main_ic_repair));
                child.setLayoutParams(lp);
                child.requestLayout();
                break;
            }
        }
        tabLayout.requestLayout();


    }
}
