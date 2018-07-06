package com.ourcompany.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.activity.AccountSettingActPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.AccountSetingActView;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.util.Utils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/15 15:40
 * Des    :
 */

public class AccountSettingAcitivity extends MvpActivity<AccountSetingActView, AccountSettingActPresenter> implements AccountSetingActView {


    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvPhoneValue)
    TextView tvPhoneValue;
    @BindView(R.id.btChangePassword)
    TextView btChangePassword;
    @BindView(R.id.tvTipSocial)
    TextView tvTipSocial;
    @BindView(R.id.tvWeChat)
    TextView tvWeChat;
    @BindView(R.id.swith_wechat)
    Switch swithWechat;
    @BindView(R.id.tvQQ)
    TextView tvQQ;
    @BindView(R.id.swith_qq)
    Switch swithQq;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, AccountSettingAcitivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, AccountSettingAcitivity.this);

    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_account_setting;
    }

    @Override
    protected AccountSetingActView bindView() {
        return this;
    }

    @Override
    protected AccountSettingActPresenter bindPresenter() {
        return new AccountSettingActPresenter(MApplication.mContext);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setTitle(ResourceUtils.getString(R.string.str_account_setting));
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvPhoneValue.setText(getPresenter().getMyPhone());
            }
        }, 50);
    }

    @OnClick({R.id.tvPhone, R.id.btChangePassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvPhone:
                BindNewPhoneAcitivity.gotoThis(AccountSettingAcitivity.this);
                break;
            case R.id.btChangePassword:
                ResetPasswordAcitivity.gotoThis(AccountSettingAcitivity.this);
                break;
        }
    }


}
