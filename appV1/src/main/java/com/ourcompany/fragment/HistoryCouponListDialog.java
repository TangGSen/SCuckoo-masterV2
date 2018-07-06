package com.ourcompany.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ourcompany.EmptyMvpPresenter;
import com.ourcompany.EmptyMvpView;
import com.ourcompany.R;
import com.ourcompany.adapter.ViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.fragment.coupon.CouponHistoryFragment;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.swidget.BaseSheetDialogFragment;
import company.com.commons.swidget.NoScrollViewPager;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/21 下午6:41
 * Des    :
 */
public class HistoryCouponListDialog extends BaseSheetDialogFragment<EmptyMvpView, EmptyMvpPresenter> implements EmptyMvpView {


    @BindView(R.id.imageClose)
    ImageView imageClose;
    @BindView(R.id.useCouponWay)
    TextView useCouponWay;
    @BindView(R.id.btFinish)
    TextView btFinish;
    @BindView(R.id.bottom_sheet)
    NestedScrollView bottomSheet;
    @BindView(R.id.mViewPager)
    NoScrollViewPager mViewPager;
    private ArrayList<Fragment> fragments;


    public static HistoryCouponListDialog newInstance() {
        return new HistoryCouponListDialog();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupon_history_dialog;
    }

    @Override
    protected EmptyMvpView bindView() {
        return this;
    }

    @Override
    protected EmptyMvpPresenter bindPresenter() {
        return new EmptyMvpPresenter(MApplication.mContext);
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        initTabView();
    }

    private void initTabView() {
        fragments = new ArrayList<>();
        //需要加个标识是未过期，已过期的类型标识,0 代表未过期，1代表已过期
        CouponHistoryFragment notOverdueFrag = new CouponHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CouponHistoryFragment.KEY_TYPE, CouponHistoryFragment.TYPE_NOT_OVERDUE);
        notOverdueFrag.setArguments(bundle);
        fragments.add(notOverdueFrag);

        CouponHistoryFragment overdueFrag = new CouponHistoryFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(CouponHistoryFragment.KEY_TYPE, CouponHistoryFragment.TYPE_OVERDUE);
        overdueFrag.setArguments(bundle2);
        fragments.add(overdueFrag);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    useCouponWay.setText(ResourceUtils.getString(R.string.str_use_overdue_coupon));
                } else {
                    useCouponWay.setText(ResourceUtils.getString(R.string.str_check_effective_coupon));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(0);
        setTopOffset(DisplayUtils.getWindowHeight() / 3);
    }



    @OnClick({R.id.useCouponWay, R.id.btFinish, R.id.imageClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btFinish:
                closeDialog();
                break;
            case R.id.imageClose:
                closeDialog();
                break;

            case R.id.useCouponWay:
                if (mViewPager.getCurrentItem() == 0) {
                    mViewPager.setCurrentItem(1);
                } else if (mViewPager.getCurrentItem() == 1) {
                    mViewPager.setCurrentItem(0);
                }
                break;
        }
    }


    public void closeDialog() {
        if (getBehavior() != null) {
            getBehavior().setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }


    @Override
    public void showToastMsg(String string) {

    }


}
