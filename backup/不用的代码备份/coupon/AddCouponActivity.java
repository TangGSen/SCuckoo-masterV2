package com.ourcompany.activity.tab_mine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.animation.Rotate3DAnimation;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.activity.AddCouponActPresenter;
import com.ourcompany.utils.InputMethodUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.AddCouponActView;
import com.ourcompany.widget.CouponConstraintLayoutView;
import com.ourcompany.widget.calender.CalendarDialog;

import java.util.Calendar;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/4 下午10:42
 * Des    : 添加优惠券
 */
public class AddCouponActivity extends MvpActivity<AddCouponActView, AddCouponActPresenter> implements AddCouponActView {


    @BindView(R.id.tvCouponMoney)
    EditText tvCouponMoney;
    @BindView(R.id.tvName)
    EditText tvName;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.couponLayout)
    CouponConstraintLayoutView couponLayout;

    @BindView(R.id.tvAddCoupon)
    TextView tvAddCoupon;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;

    @BindView(R.id.tvStates)
    TextView tvStates;
    @BindView(R.id.groupBaseInfo)
    Group groupBaseInfo;
    @BindView(R.id.otherBaseInfo)
    Group otherBaseInfo;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    private int centerX;
    private int centerY;
    private int depthZ = 400;
    private int duration = 600;
    private Rotate3DAnimation openAnimation;
    private Rotate3DAnimation closeAnimation;
    private boolean isOpen;
    private Rotate3DAnimation onOpenAnimationEnd;
    private Rotate3DAnimation onCloseAnimationEnd;
    private int lastPostion;
    //从这开始为第二页
    private int middlePosition = 5;

    private final int INDEX_COUPON_NAME = 1;
    private final int INDEX_COUPON_MONEY = 2;

    private final int INDEX_COUPON_STARTTIME = 3;
    private final int INDEX_COUPON_ENDTIME = 4;
    //金额限制输入位数
    private final int MONEY_INPUT_SIZE = 5;
    private final int NAME_INPUT_SIZE = 14;
    private String startTime = ResourceUtils.getString(R.string.str_counpon_strat_time);
    private String endTime = ResourceUtils.getString(R.string.str_counpon_end_time);
    private AlertDialog calendarDialog;
    private int currentTimeType = -1;

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, AddCouponActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_coupon;
    }

    @Override
    protected void initView() {
        super.initView();
        getWindow().setBackgroundDrawable(null);
        setSupportActionBar(commonToolbar);
        commonToolbar.setBackground(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        initInputView();


}

    private void initInputView() {
        tvCouponMoney.setFocusable(false);
        tvCouponMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MONEY_INPUT_SIZE)});
        tvCouponMoney.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvCouponMoney.setSelection(tvCouponMoney.getText().toString().length());
                InputMethodUtils.toggleSoftInputForEt(tvCouponMoney);

                return false;
            }
        });

        tvName.setFocusable(false);
        tvName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NAME_INPUT_SIZE)});
        tvName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvName.setSelection(tvName.getText().toString().length());
                InputMethodUtils.toggleSoftInputForEt(tvName);

                return false;
            }
        });
        InputMethodUtils.detectKeyboard(this, new InputMethodUtils.OnKeyboardEventListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeight) {

            }

            @Override
            public void onSoftKeyboardClosed() {

                tvCouponMoney.setFocusable(false);
                tvName.setFocusable(false);
            }

            @Override
            public boolean isEmotionPanelShowing() {
                return false;
            }

            @Override
            public void hideEmotionPanel() {

            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        tvTime.setText(getPresenter().getTime(startTime,endTime), TextView.BufferType.SPANNABLE);
        tvTime.setMovementMethod(LinkMovementMethod.getInstance());
    }



    private void initOpenAnim() {
        //从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
        openAnimation = new Rotate3DAnimation(0, 90, centerX, centerY, depthZ, true);
        openAnimation.setDuration(duration);
        openAnimation.setFillAfter(true);
        openAnimation.setInterpolator(new AccelerateInterpolator());
        openAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                groupBaseInfo.setVisibility(View.GONE);
                otherBaseInfo.setVisibility(View.VISIBLE);
                tvStates.setText(ResourceUtils.getString(R.string.str_coupon_other_info));
                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                if (onOpenAnimationEnd == null) {
                    initOpenAnimationEnd();
                }
                couponLayout.startAnimation(onOpenAnimationEnd);
            }
        });
    }

    public void initOpenAnimationEnd() {
        onOpenAnimationEnd = new Rotate3DAnimation(270, 360, centerX, centerY, depthZ, false);
        onOpenAnimationEnd.setDuration(duration);
        onOpenAnimationEnd.setFillAfter(true);
        onOpenAnimationEnd.setInterpolator(new DecelerateInterpolator());
    }

    public void initCloseAnimationEnd() {
        onCloseAnimationEnd = new Rotate3DAnimation(90, 0, centerX, centerY, depthZ, false);
        onCloseAnimationEnd.setDuration(duration);
        onCloseAnimationEnd.setFillAfter(true);
        onCloseAnimationEnd.setInterpolator(new DecelerateInterpolator());
    }

    /**
     * 卡牌文本介绍关闭效果：旋转角度与打开时逆行即可
     */
    private void initCloseAnim() {
        closeAnimation = new Rotate3DAnimation(360, 270, centerX, centerY, depthZ, true);
        closeAnimation.setDuration(duration);
        closeAnimation.setFillAfter(true);
        closeAnimation.setInterpolator(new AccelerateInterpolator());
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                groupBaseInfo.setVisibility(View.VISIBLE);
                otherBaseInfo.setVisibility(View.GONE);
                tvStates.setText(ResourceUtils.getString(R.string.str_coupon_base_info));
                if (onCloseAnimationEnd == null) {
                    initCloseAnimationEnd();
                }
                couponLayout.startAnimation(onCloseAnimationEnd);
            }
        });
    }


    @Override
    protected AddCouponActView bindView() {
        return this;
    }

    @Override
    protected AddCouponActPresenter bindPresenter() {
        return new AddCouponActPresenter(MApplication.mContext);
    }


    private void starAnimation() {
        //以旋转对象的中心点为旋转中心点，这里主要不要再onCreate方法中获取，因为视图初始绘制时，获取的宽高为0
        centerX = couponLayout.getWidth() / 2;
        centerY = couponLayout.getHeight() / 2;
        if (openAnimation == null) {
            initOpenAnim();
            initCloseAnim();
        }
        //用作判断当前点击事件发生时动画是否正在执行
        if (openAnimation.hasStarted() && !openAnimation.hasEnded()) {
            return;
        }
        if (closeAnimation.hasStarted() && !closeAnimation.hasEnded()) {
            return;
        }
        //判断动画执行
        if (isOpen) {
            couponLayout.startAnimation(openAnimation);
        } else {
            couponLayout.startAnimation(closeAnimation);

        }

    }


    public void setInputTextChanged(int currentIndex, String content) {
        if (TextUtils.isEmpty(content)) {
            content = "";
        }
        switch (currentIndex) {
            case INDEX_COUPON_NAME:
                tvName.setText(content);
                break;
            case INDEX_COUPON_MONEY:
                if (TextUtils.isEmpty(content)) {
                    content = "0";
                }
                tvCouponMoney.setText("￥" + content);
                break;
        }
    }

    public void changeDate(int currentIndex, String date) {
        if (TextUtils.isEmpty(date)) {
            return;
        }
        switch (currentIndex) {
            case INDEX_COUPON_STARTTIME:
                this.startTime = date;
                break;
            case INDEX_COUPON_ENDTIME:
                this.endTime = date;

                break;
        }
        tvTime.setText(startTime + " 至 " + endTime);
    }

    @Override
    public void showEndTimeDialog() {
        currentTimeType = AddCouponActPresenter.END_TIME_TAG;
        showTimeDialog();
    }

    @Override
    public void showStartTimeDialog() {
        currentTimeType = AddCouponActPresenter.START_TIME_TAG;
        showTimeDialog();
    }

    private void showTimeDialog(){
        if(calendarDialog==null){
            initCanlenderView();
        }

        if(calendarDialog.isShowing()){
            return;
        }

        calendarDialog.show();
    }

    public void initCanlenderView() {
        final Calendar calendar = Calendar.getInstance();
        calendarDialog = new CalendarDialog().getCalendarDialog(AddCouponActivity.this, true, true, Calendar.getInstance().get(Calendar.YEAR) + 1, 1950, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new CalendarDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(long time, int year, int month, int day, boolean isLunar) {
                if (System.currentTimeMillis() > time) {
                    time = System.currentTimeMillis();
                }
                String date = getPresenter().getDate(time,currentTimeType);
                if(currentTimeType == AddCouponActPresenter.START_TIME_TAG){
                    startTime =date;
                }else if(currentTimeType == AddCouponActPresenter.END_TIME_TAG){
                    endTime =date;
                }
                tvTime.setText(getPresenter().getTime(startTime,endTime), TextView.BufferType.SPANNABLE);
            }
        });
    }

//    @OnClick({R.id.tvAniation})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tvAniation:
//
//                break;
//        }
//    }
}
