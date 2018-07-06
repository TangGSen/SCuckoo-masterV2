package com.ourcompany.fragment.coupon;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.constraint.Group;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.animation.Rotate3DAnimation;
import com.ourcompany.activity.tab_mine.AddCouponActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.Coupon;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.presenter.fragment.EditextCouponInfoFragPresenter;
import com.ourcompany.utils.InputMethodUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.EditextCouponInfoFragView;
import com.ourcompany.widget.CouponConstraintLayoutView;
import com.ourcompany.widget.calender.CalendarDialog;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.bmob.v3.datatype.BmobDate;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/5 下午3:52
 * Des    :
 */
public class CouponEditextInfoFragment extends MvpFragment<EditextCouponInfoFragView, EditextCouponInfoFragPresenter> implements EditextCouponInfoFragView {


    //金额限制输入位数
    private final int MONEY_INPUT_SIZE = 5;
    private final int NAME_INPUT_SIZE = 14;
    @BindView(R.id.tvCouponMoney)
    EditText tvCouponMoney;
    @BindView(R.id.tvName)
    EditText tvName;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvStates)
    TextView tvStates;
    @BindView(R.id.couponLayout)
    CouponConstraintLayoutView couponLayout;
    Unbinder unbinder;
    @BindView(R.id.groupBaseInfo)
    Group groupBaseInfo;
    @BindView(R.id.otherBaseInfo)
    Group otherBaseInfo;
    @BindView(R.id.edLimit)
    EditText edLimit;
    @BindView(R.id.tvUseWay)
    EditText tvUseWay;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.layoutLimit)
    LinearLayout layoutLimit;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.edCount)
    EditText edCount;
    Unbinder unbinder1;
    private String startTime = ResourceUtils.getString(R.string.str_counpon_strat_time);
    private String endTime = ResourceUtils.getString(R.string.str_counpon_end_time);

    private AlertDialog calendarDialog;
    private int currentTimeType = -1;
    private int centerX;
    private int centerY;
    private int depthZ = 500;
    private int duration = 500;
    private Rotate3DAnimation openAnimation;
    private Rotate3DAnimation closeAnimation;
    private Rotate3DAnimation onOpenAnimationEnd;
    private Rotate3DAnimation onCloseAnimationEnd;


    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);


    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        initInputView();

    }

    @Override
    protected void initData() {
        super.initData();
        tvTime.setText(getPresenter().getTime(startTime, endTime), TextView.BufferType.SPANNABLE);
        tvTime.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initInputView() {
        tvStates.setVisibility(View.INVISIBLE);
        tvCouponMoney.setFocusable(false);
        tvCouponMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MONEY_INPUT_SIZE)});
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    EditText editText = (EditText) view;
                    InputMethodUtils.toggleSoftInputForEt(editText);
                    editText.setSelection(editText.getText().toString().length());
                }

                return true;
            }
        };
        tvCouponMoney.setOnTouchListener(onTouchListener);
        //名称
        tvName.setFocusable(false);
        tvName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NAME_INPUT_SIZE)});
        tvName.setOnTouchListener(onTouchListener);

        //使用方式
        tvUseWay.setFocusable(false);
        tvUseWay.setOnTouchListener(onTouchListener);
        //限领多少
        edLimit.setFocusable(false);
        edCount.setFocusable(false);
        edCount.setOnTouchListener(onTouchListener);

        layoutLimit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodUtils.toggleSoftInputForEt(edLimit);
                    edLimit.setSelection(edLimit.getText().toString().length());
                    return true;
                }

                return false;
            }
        });

        tvCount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodUtils.toggleSoftInputForEt(edCount);
                    edCount.setSelection(edCount.getText().toString().length());
                    return true;
                }

                return false;
            }
        });


        InputMethodUtils.detectKeyboard(mActivity, new InputMethodUtils.OnKeyboardEventListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeight) {

            }

            @Override
            public void onSoftKeyboardClosed() {

                tvCouponMoney.setFocusable(false);
                tvName.setFocusable(false);
                tvUseWay.setFocusable(false);
                edLimit.setFocusable(false);
                edCount.setFocusable(false);
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

    public void starAnimation(boolean isOpen) {
        // InputMethodUtils.hideKeyboard();
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

              //  tvStates.setText(ResourceUtils.getString(R.string.str_coupon_other_info));
                ((AddCouponActivity) mActivity).changeAnimationState(ResourceUtils.getString(R.string.str_pre_step));
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

              //  tvStates.setText(ResourceUtils.getString(R.string.str_coupon_base_info));
                ((AddCouponActivity) mActivity).changeAnimationState(ResourceUtils.getString(R.string.str_next_step));
                if (onCloseAnimationEnd == null) {
                    initCloseAnimationEnd();
                }
                couponLayout.startAnimation(onCloseAnimationEnd);
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupon_editext_info_1;
    }

    @Override
    protected EditextCouponInfoFragView bindView() {
        return this;
    }

    @Override
    protected EditextCouponInfoFragPresenter bindPresenter() {
        return new EditextCouponInfoFragPresenter(MApplication.mContext);
    }

    @Override
    public void showEndTimeDialog() {
        InputMethodUtils.hideKeyboard(mRootView);
        currentTimeType = EditextCouponInfoFragPresenter.END_TIME_TAG;
        showTimeDialog();
    }

    @Override
    public void showStartTimeDialog() {
        InputMethodUtils.hideKeyboard(mRootView);
        currentTimeType = EditextCouponInfoFragPresenter.START_TIME_TAG;
        showTimeDialog();
    }

    @Override
    public void submitSuccess(Coupon coupon) {
        EventBus.getDefault().post(coupon);
        showToastMsg(ResourceUtils.getString(R.string.submit_success_v2));
        ((AddCouponActivity) mActivity).submitEnd();
        mActivity.finish();
    }

    @Override
    public void submitError() {
        showToastMsg(ResourceUtils.getString(R.string.submit_fail_v2));
        ((AddCouponActivity) mActivity).submitEnd();
    }

    private void showTimeDialog() {
        if (calendarDialog == null) {
            initCanlenderView();
        }

        if (calendarDialog.isShowing()) {
            return;
        }

        calendarDialog.show();
    }

    public void initCanlenderView() {
        final Calendar calendar = Calendar.getInstance();
        calendarDialog = new CalendarDialog().getCalendarDialog(mActivity, true, true, Calendar.getInstance().get(Calendar.YEAR) + 1, 1950, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new CalendarDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(long time, int year, int month, int day, boolean isLunar) {
                if (System.currentTimeMillis() > time) {
                    time = System.currentTimeMillis();
                }
                String date = getPresenter().getDate(time, currentTimeType);
                if (currentTimeType == EditextCouponInfoFragPresenter.START_TIME_TAG) {
                    startTime = date;
                } else if (currentTimeType == EditextCouponInfoFragPresenter.END_TIME_TAG) {
                    endTime = date;
                }
                tvTime.setText(getPresenter().getTime(startTime, endTime), TextView.BufferType.SPANNABLE);
            }
        });
    }

    //提交信息,需要检查信息的合法性
    public void submitInfo() {

//        EditText tvCouponMoney;
//        EditText tvName;
//        TextView tvTime;
//        EditText edLimit;
//        EditText tvUseWay;
//        EditText edCount;

        String useWay = tvUseWay.getText().toString();
        String count = edCount.getText().toString();

        String couponMoney = tvCouponMoney.getText().toString();
        String name = tvName.getText().toString();
        String limit = edLimit.getText().toString();

        boolean isFristError = false;
        boolean isSecondError = false;
        String fristErrorMsg = "";
        String secondErrorMsg = "";

        //正面
        if (TextUtils.isEmpty(startTime) || (!TextUtils.isEmpty(startTime) &&
                startTime.equals(ResourceUtils.getString(R.string.str_counpon_strat_time)))) {
            isFristError = true;
            fristErrorMsg = ResourceUtils.getString(R.string.str_input_counpon_strat_time);
        }
        if (TextUtils.isEmpty(endTime) || (!TextUtils.isEmpty(endTime) &&
                endTime.equals(ResourceUtils.getString(R.string.str_counpon_end_time)))) {
            isFristError = true;
            fristErrorMsg = ResourceUtils.getString(R.string.str_input_counpon_end_time);
        }

        if (TextUtils.isEmpty(couponMoney) || (!TextUtils.isEmpty(couponMoney) && Integer.parseInt(couponMoney) <= 0)) {
            isFristError = true;
            fristErrorMsg = ResourceUtils.getString(R.string.str_input_counpon_money);
        }
        if (TextUtils.isEmpty(name)) {
            isFristError = true;
            fristErrorMsg = ResourceUtils.getString(R.string.str_input_counpon_name);
        }

        //反面
        if (TextUtils.isEmpty(limit) || (!TextUtils.isEmpty(limit) && Integer.parseInt(limit) <= 0)) {
            isSecondError = true;
            secondErrorMsg = ResourceUtils.getString(R.string.str_input_counpon_limit);
        }
        if (TextUtils.isEmpty(useWay)) {
            isSecondError = true;
            secondErrorMsg = ResourceUtils.getString(R.string.str_input_counpon_useway);
        }
        if (TextUtils.isEmpty(count)) {
            isSecondError = true;
            secondErrorMsg = ResourceUtils.getString(R.string.str_input_counpon_count);
        }
        if (!TextUtils.isEmpty(count) && Integer.parseInt(count) <= 0) {
            isSecondError = true;
            secondErrorMsg = ResourceUtils.getString(R.string.str_input_counpon_count_error);
        }

        //应该先检查当前显示的页面
        if (isFristError || isSecondError) {
            ((AddCouponActivity) mActivity).submitEnd();
            //第一个在显示，并且有错的话那么就直接显示错误信息
            if (isFristError && groupBaseInfo.getVisibility() == View.VISIBLE) {
                showToastMsg(fristErrorMsg);
            } else if (isFristError && groupBaseInfo.getVisibility() != View.VISIBLE) {
                //第一个不显示，并且有错的话
                if (isSecondError) {
                    //直接显示第二个的错误
                    showToastMsg(secondErrorMsg);
                } else {
                    //
                    starAnimation(false);
                    showToastMsg(fristErrorMsg);
                }
            } else if (isSecondError && otherBaseInfo.getVisibility() == View.VISIBLE) {
                showToastMsg(secondErrorMsg);
            } else if (isSecondError && otherBaseInfo.getVisibility() != View.VISIBLE) {
                //第一个不显示，并且有错的话
                if (isFristError) {
                    //直接显示第二个的错误
                    showToastMsg(fristErrorMsg);
                } else {
                    //
                    starAnimation(true);
                    showToastMsg(secondErrorMsg);
                }

            }

        } else {
            //关闭输入法
            InputMethodUtils.hideKeyboard(mRootView);
            Coupon coupon = new Coupon();
            coupon.setName(name);
            coupon.setCouponMoney(Integer.parseInt(couponMoney));
            coupon.setCount(Integer.parseInt(count));
            coupon.setLimit(Integer.parseInt(limit));
            coupon.setUserId(MServiceManager.getInstance().getLocalThirdPartyId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                coupon.setStartTime(new BmobDate(sdf.parse(startTime)));
                coupon.setEndTime(new BmobDate(sdf.parse(endTime)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            getPresenter().submitInfo(coupon);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
