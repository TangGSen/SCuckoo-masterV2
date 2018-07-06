package com.ourcompany.activity.tab_mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.Coupon;
import com.ourcompany.fragment.coupon.CouponEditextInfoFragment;
import com.ourcompany.presenter.activity.AddCouponActPresenter;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.AddCouponActView;
import com.ourcompany.widget.LoadingViewAOV;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.util.Utils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/4 下午10:42
 * Des    : 添加优惠券
 */
public class AddCouponActivity extends MvpActivity<AddCouponActView, AddCouponActPresenter> implements AddCouponActView {


    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.nextStep)
    Button nextStep;
    @BindView(R.id.tvFinish)
    TextView tvFinish;
    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.tvInstruction)
    TextView tvInstruction;
    //从这开始为第二页
    private CouponEditextInfoFragment couponEditextInfo;


    public static void gotoThis(Context context, Coupon coupon,boolean isCanEdi) {
        Intent intent = new Intent(context, AddCouponActivity.class);
        Bundle bundle = new Bundle();
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
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, AddCouponActivity.this);

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
        getRootView().post(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tvInstruction.getLayoutParams();
                layoutParams.rightMargin = tvFinish.getWidth()+ DisplayUtils.dip2px(16);
            }
        });


    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getRootView().post(new Runnable() {
            @Override
            public void run() {
                initFragments();
            }
        });


    }

    private void initFragments() {

        //第一个 输入名称的

        couponEditextInfo = new CouponEditextInfoFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.framelayout, couponEditextInfo);
        transaction.commit();

    }


    @Override
    protected AddCouponActView bindView() {
        return this;
    }

    @Override
    protected AddCouponActPresenter bindPresenter() {
        return new AddCouponActPresenter(MApplication.mContext);
    }


    @OnClick({R.id.nextStep, R.id.tvFinish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nextStep:
                if (couponEditextInfo == null) {
                    return;
                }
                if (nextStep.getText().toString().equals(ResourceUtils.getString(R.string.str_next_step))) {
                    couponEditextInfo.starAnimation(true);
                } else if (nextStep.getText().toString().equals(ResourceUtils.getString(R.string.str_pre_step))) {
                    couponEditextInfo.starAnimation(false);
                }

                break;
            case R.id.tvFinish:
                if (couponEditextInfo != null) {
                    LoadingViewAOV.getInstance().with(AddCouponActivity.this, tvFinish,
                            R.color.whiles, R.drawable.ic_loading_v4, Gravity.CENTER);
                    couponEditextInfo.submitInfo();
                }
                break;
        }
    }

    //提供给fragment 使用的
    public void changeAnimationState(String string) {
        if (!TextUtils.isEmpty(string)) {
            nextStep.setText(string);
        }

    }

    //提交失败或成功都调用
    public void submitEnd() {
        LoadingViewAOV.getInstance().close(AddCouponActivity.this, tvFinish);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
