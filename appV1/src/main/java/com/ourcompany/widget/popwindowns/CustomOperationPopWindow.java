package com.ourcompany.widget.popwindowns;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.TypeSelect;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.widget.drawable.CouponAddBgDrawable;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/19 上午11:20
 * Des    :
 */
public class CustomOperationPopWindow extends PopupWindow {

    private Context context;
    private View conentView;
    private View backgroundView;
    private Animation anim_backgroundView;
    RecyclerView recycleview;
    List<TypeSelect> typeSelectlist = new ArrayList<>();
    int[] location = new int[2];
    private OnItemListener onItemListener;
    private Handler mHandler = new Handler(Looper.myLooper());

    public interface OnItemListener {
         void OnItemListener(int position, TypeSelect typeSelect);
    }

    ;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public CustomOperationPopWindow(Context context, List<TypeSelect> typeSelects) {
        this.context = context;
        this.typeSelectlist = typeSelects;
        initView();
    }


    private void initView() {
        this.anim_backgroundView = AnimationUtils.loadAnimation(context, R.anim.alpha_show_anim);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.layout_common_pop, null);
        this.conentView.setVisibility(View.INVISIBLE);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x60000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
        this.recycleview = conentView.findViewById(R.id.lv_list);



        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (backgroundView != null) {
                    backgroundView.setVisibility(View.GONE);
                }
            }
        });


    }


    public void setRecycleAdapter(RecycleCommonAdapter<TypeSelect> recycleCommonAdapter) {
        if (recycleCommonAdapter == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //解决嵌套在NestedScrollView 的滑动不顺的问题1
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        //解决嵌套在NestedScrollView 的滑动不顺的问题2


        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                if(onItemListener!=null){
                    onItemListener.OnItemListener(position, typeSelectlist.get(position));
                }
                if (isShowing()) {
                    dismiss();
                }

            }
        });
    }

    /**
     * 没有半透明背景  显示popupWindow
     *
     * @param
     */
    public void showPopupWindows(View v,View view2) {
        v.getLocationOnScreen(location);  //获取控件的位置坐标
        int[] locationView2 = new int[2];
        view2.getLocationOnScreen(locationView2);

        //获取自身的长宽高
        //  conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        if (location[1] > DisplayUtils.getWindowHeight() / 2 + 100) {  // DisplayUtils.getWindowHeight() / 2 为屏幕的高度，方法可以自己写
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]), location[1] - conentView.getMeasuredHeight());
        } else {
           LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycleview.getLayoutParams();
           layoutParams.topMargin = locationView2[1];

            this.showAsDropDown(v, 0, -v.getHeight());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recycleview.setBackground(new CouponAddBgDrawable(recycleview.getWidth(), recycleview.getHeight(), DisplayUtils.dip2px(4)));
                    conentView.setVisibility(View.VISIBLE);
                    conentView.startAnimation(anim_backgroundView);
                }
            }, 100);
        }
    }

    /**
     * 携带半透明背景  显示popupWindow
     *
     * @param
     */
    public void showPopupWindow(View v, View backgroundView) {
        this.backgroundView = backgroundView;
        v.getLocationOnScreen(location);  //获取控件的位置坐标
        //获取自身的长宽高
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        backgroundView.setVisibility(View.VISIBLE);
        //对view执行动画
        backgroundView.startAnimation(anim_backgroundView);
        if (location[1] > DisplayUtils.getWindowHeight() / 2 / 2 + 100) {  //若是控件的y轴位置大于屏幕高度的一半，向上弹出
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]), location[1] - conentView.getMeasuredHeight());  //显示指定控件的上方
        } else {
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_down);  //反之向下弹出
            this.showAsDropDown(v, 0, 0);    //显示指定控件的下方
        }
    }

    /**
     * 显示popupWindow  根据特殊要求高度显示位置
     *
     * @param
     */
    public void showPopupWindow(View v, View backgroundView, int hight) {
        this.backgroundView = backgroundView;
        v.getLocationOnScreen(location);
        //获取自身的长宽高
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        backgroundView.setVisibility(View.VISIBLE);
        //对view执行动画
        backgroundView.startAnimation(anim_backgroundView);
        if (location[1] > DisplayUtils.getWindowHeight() / 2 / 2 + 100) {
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]), location[1] - conentView.getMeasuredHeight() - hight);
        } else {
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_down);
            this.showAsDropDown(v, 0, 0);
        }
    }
}
