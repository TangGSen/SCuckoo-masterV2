package com.ourcompany.widget;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ourcompany.R;
import com.ourcompany.utils.ResourceUtils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/31 21:23
 * Des    :LoadingViewAOV  LoadingView at other view 是指Loadview 悬浮在其他的view上
 */

public class LoadingViewAOV {
    private volatile static LoadingViewAOV instance;
    private View loadView;
    private int mTargetViewId = -1;



    private LoadingViewAOV() {
    }

    public static LoadingViewAOV getInstance() {
        if (instance == null) {
            synchronized (LoadingViewAOV.class) {
                if (instance == null) {
                    instance = new LoadingViewAOV();
                }
            }
        }
        return instance;
    }
    //将view 的父控件换成自己的
    public void with(final Activity activity, final View view, int colorBg, int drawableResId, int gravity) {
        final ViewGroup viewParent = (ViewGroup) view.getParent();
        loadView = activity.getLayoutInflater().inflate(R.layout.layout_loading_aov, null);
        if (viewParent != null && loadView != null && loadView.getVisibility() == View.GONE) {
            loadView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams viewLayoutParams = view.getLayoutParams();
            ViewGroup mRootView = loadView.findViewById(R.id.rootView);
            ViewGroup loadingLayout = loadView.findViewById(R.id.loadingLayout);
            if (colorBg > 0) {
                loadingLayout.setBackgroundColor(ResourceUtils.getResColor(colorBg));
            } else {
                if (view.getBackground() != null) {
                    loadingLayout.setBackground(view.getBackground());
                }
            }
            view.setEnabled(false);
            //需要将id 也交换
            mTargetViewId = view.getId();
            mRootView.setId(mTargetViewId);
            mRootView.setLayoutParams(viewLayoutParams);
            viewParent.addView(loadView);
            viewParent.removeView(view);
            mRootView.addView(view, 0);
            ImageView animView = loadView.findViewById(R.id.imageLoadView);
            ViewGroup.LayoutParams layoutParams = animView.getLayoutParams();
            ((LinearLayout)(animView.getParent())).setGravity(gravity);
            if(drawableResId>0){
                animView.setImageDrawable(ResourceUtils.getDrawable(drawableResId));
            }
            Animation circle_anim = AnimationUtils.loadAnimation(activity, R.anim.anim_loading_imag);
            LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
            circle_anim.setInterpolator(interpolator);
            if (circle_anim != null) {
                animView.startAnimation(circle_anim);  //开始动画
            }

        }


    }


    //将view 的父控件换成自己的
    public void with(final Activity activity, final View view, int colorBg) {

        with(activity, view, colorBg,0,Gravity.CENTER);

    }

    public void with(final Activity activity, final View view) {
        with(activity, view, 0);
    }

    public void close(Activity activity, View view) {
        if (loadView != null) {
            View close = loadView.findViewById(R.id.imageLoadView);
            if (close != null) {
                close.clearAnimation();
            }
            ViewGroup mRootView = null;
            if (mTargetViewId > 0) {
                mRootView = loadView.findViewById(mTargetViewId);
                mRootView.setVisibility(View.GONE);
            }

            ViewGroup viewParent = null;
            if (mRootView != null) {
                viewParent = (ViewGroup) mRootView.getParent();
                mRootView.removeView(view);
            }
            if (viewParent != null) {
                view.setLayoutParams(mRootView.getLayoutParams());
                view.setId(mTargetViewId);
                viewParent.addView(view);
                viewParent.removeView(loadView);
            }

            view.setEnabled(true);
        }
    }
}
