package com.ourcompany.activity.certification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.adapter.ViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.fragment.certification.UploadIDCardFragment;
import com.ourcompany.presenter.activity.CertivicationCenterPresenter;
import com.ourcompany.view.activity.CertificationCentreActView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.swidget.NoScrollViewPager;
import company.com.commons.util.Utils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/30 上午8:52
 * Des    : 申请认证V 流程  作为信用加分项
 * 1.身份证   必须
 * 2.
 */
public class CertificationCentreActivity extends MvpActivity<CertificationCentreActView, CertivicationCenterPresenter> implements CertificationCentreActView {


    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.mViewPager)
    NoScrollViewPager mViewPager;
    private List<Fragment> fragments;

    @Override
    public void showToastMsg(String string) {

    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        //需要加个标识是未过期，已过期的类型标识,0 代表未过期，1代表已过期
        UploadIDCardFragment idCardFragment = new UploadIDCardFragment();

        fragments.add(idCardFragment);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (position == 0) {
//                    checkOverdueCoupon.setText(ResourceUtils.getString(R.string.str_check_overdue_coupon));
//                }else{
//                    checkOverdueCoupon.setText(ResourceUtils.getString(R.string.str_check_effective_coupon));
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
    }

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, CertificationCentreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, CertificationCentreActivity.this);

    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        initViewPager();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification_centre;
    }

    @Override
    protected CertificationCentreActView bindView() {
        return this;
    }

    @Override
    protected CertivicationCenterPresenter bindPresenter() {
        return new CertivicationCenterPresenter(MApplication.mContext);
    }


//    @OnClick({R.id.tvFinishAndNext})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tvFinishAndNext:
//                break;
//
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
