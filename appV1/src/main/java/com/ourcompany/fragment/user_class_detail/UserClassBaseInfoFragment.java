package com.ourcompany.fragment.user_class_detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.LoginFragmentView;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/5/15 14:24
 * Des    : 用户的基础信息
 */

public class UserClassBaseInfoFragment extends MvpFragment<LoginFragmentView, LoginFragPresenter> implements LoginFragmentView {

    private static final String KEY_BUNDLE_USER = "bundle_user";
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvAddressV)
    TextView tvAddressV;
    @BindView(R.id.tvBusinessHours)
    TextView tvBusinessHours;
    @BindView(R.id.tvBusinessHoursV)
    TextView tvBusinessHoursV;
    @BindView(R.id.tvServiceArea)
    TextView tvServiceArea;
    @BindView(R.id.tvServiceAreaV)
    TextView tvServiceAreaV;
    @BindView(R.id.tvPriceRange)
    TextView tvPriceRange;
    @BindView(R.id.tvPriceRangeV)
    TextView tvPriceRangeV;
    Unbinder unbinder;
    private SUser mUser;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_class_base_info;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        Bundle res = getArguments();
        if (res != null) {
            mUser = (SUser) res.getSerializable(KEY_BUNDLE_USER);
        }
        super.initArgs(bundle);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }



    @Override
    protected void initData() {
        super.initData();
        tvAddress.setText(ResourceUtils.getString(R.string.info_address));
        tvBusinessHours.setText(ResourceUtils.getString(R.string.info_business_hours));
        tvServiceArea.setText(ResourceUtils.getString(R.string.info_service_area));
        tvPriceRange.setText(ResourceUtils.getString(R.string.info_price_range));
        if(mUser!=null) {
            tvAddressV.setText(TextUtils.isEmpty(mUser.getAddress())?"":mUser.getAddress());
            tvBusinessHoursV.setText(TextUtils.isEmpty(mUser.getBusinessHours())?"":mUser.getBusinessHours());
            tvServiceAreaV.setText(TextUtils.isEmpty(mUser.getServiceArea())?"":mUser.getServiceArea());
            tvPriceRangeV.setText(TextUtils.isEmpty(mUser.getPriceRange())?"":mUser.getPriceRange());
        }else{
            tvAddressV.setText("");
            tvBusinessHoursV.setText("");
            tvServiceAreaV.setText("");
            tvPriceRangeV.setText("");
        }
    }

    @Override
    protected LoginFragmentView bindView() {
        return this;
    }

    @Override
    protected LoginFragPresenter bindPresenter() {
        return new LoginFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {

    }



}
