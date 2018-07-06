//package com.ourcompany.widget;
//
//import android.app.Activity;
//import android.support.v4.util.ArrayMap;
//import android.util.SparseArray;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.animation.LinearInterpolator;
//import android.widget.ImageView;
//
//import com.ourcompany.R;
//import com.ourcompany.utils.ResourceUtils;
//
///**
// * Author : 唐家森
// * Version: 1.0
// * On     : 2018/3/31 21:23
// * Des    :LoadingViewAOV  LoadingView at other view 是指Loadview 悬浮在其他的view上
// */
//
//public class LoadingViewAOVV2 {
//    private volatile static LoadingViewAOVV2 instance;
//    private View loadView;
//    private int mTargetViewId = -1;
//    SparseArray<View> viewValues = new SparseArray<>();
//    ArrayMap<Activity, SparseArray<View>> activityKeys = new ArrayMap<>();
//
//    /**
//     * 属性设置
//     */
//
//
//    private void setTarget(LoadViewItem builder) {
//        //首先
//
//    }
//
//
//    static class LoadViewItem {
//        private Activity activity;
//        private View targetView;
//        private int colorBg;
//        private int drawableResId;
//        private View loadView;
//        private int mTargetViewId = -1;
//        private boolean targetViewEnable;
//        private boolean targetViewParentEnable;
//
//        public LoadViewItem with(Activity activity) {
//            this.activity = activity;
//            return this;
//        }
//
//        public LoadViewItem targetView(View targetView) {
//            this.targetView = targetView;
//            return this;
//        }
//
//        public LoadViewItem targetViewParentEnable(boolean anable) {
//
//            this.targetViewParentEnable = anable;
//            return this;
//        }
//
//        public LoadViewItem targetViewEnable(boolean anable) {
//            this.targetViewEnable = anable;
//            return this;
//        }
//
//        public LoadViewItem colorBg(int colorBg) {
//            this.colorBg = colorBg;
//            return this;
//        }
//
//        public LoadViewItem drawableResId(int drawableResId) {
//            this.drawableResId = drawableResId;
//            return this;
//        }
//
//        public LoadingViewAOVV2 build() {
//            return getInstance();
//        }
//
//
//    }
//
//    public void start(LoadViewItem item){
//        //将view 的父控件换成自己的
//            if (targetView == null || activity == null) {
//                return;
//            }
//            final ViewGroup viewParent = (ViewGroup) targetView.getParent();
//            loadView = activity.getLayoutInflater().inflate(R.layout.layout_loading_aov, null);
//            if (viewParent != null && loadView != null && loadView.getVisibility() == View.GONE) {
//                loadView.setVisibility(View.VISIBLE);
//                ViewGroup.LayoutParams viewLayoutParams = targetView.getLayoutParams();
//                ViewGroup mRootView = loadView.findViewById(R.id.rootView);
//                ViewGroup loadingLayout = loadView.findViewById(R.id.loadingLayout);
//                if (colorBg > 0) {
//                    loadingLayout.setBackgroundColor(ResourceUtils.getResColor(colorBg));
//                } else {
//                    if (targetView.getBackground() != null) {
//                        loadingLayout.setBackground(targetView.getBackground());
//                    }
//                }
//                targetView.setEnabled(false);
//                //需要将id 也交换
//                mTargetViewId = targetView.getId();
//                mRootView.setId(mTargetViewId);
//                mRootView.setLayoutParams(viewLayoutParams);
//                viewParent.addView(loadView);
//                viewParent.removeView(targetView);
//                mRootView.addView(targetView, 0);
//                ImageView animView = loadView.findViewById(R.id.imageLoadView);
//                if (drawableResId > 0) {
//                    animView.setImageDrawable(ResourceUtils.getDrawable(drawableResId));
//                }
//                Animation circle_anim = AnimationUtils.loadAnimation(activity, R.anim.anim_loading_imag);
//                LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
//                circle_anim.setInterpolator(interpolator);
//                if (circle_anim != null) {
//                    animView.startAnimation(circle_anim);  //开始动画
//                }
//            }
//
//    }
//
//    private LoadingViewAOVV2() {
//    }
//
//    public static LoadingViewAOVV2 getInstance() {
//        if (instance == null) {
//            synchronized (LoadingViewAOVV2.class) {
//                if (instance == null) {
//                    instance = new LoadingViewAOVV2();
//                }
//            }
//        }
//        return instance;
//    }
//
//
//    public void close(Activity activity, View view) {
//        if (loadView != null) {
//            View close = loadView.findViewById(R.id.imageLoadView);
//            if (close != null) {
//                close.clearAnimation();
//            }
//            ViewGroup mRootView = null;
//            if (mTargetViewId > 0) {
//                mRootView = loadView.findViewById(mTargetViewId);
//                mRootView.setVisibility(View.GONE);
//            }
//
//            ViewGroup viewParent = null;
//            if (mRootView != null) {
//                viewParent = (ViewGroup) mRootView.getParent();
//                mRootView.removeView(view);
//            }
//            if (viewParent != null) {
//                view.setLayoutParams(mRootView.getLayoutParams());
//                view.setId(mTargetViewId);
//                viewParent.addView(view);
//                viewParent.removeView(loadView);
//            }
//
//            view.setEnabled(true);
//        }
//    }
//}
