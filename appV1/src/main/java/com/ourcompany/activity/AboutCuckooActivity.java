package com.ourcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.utils.AppUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.LoginFragmentView;

import butterknife.BindView;
import butterknife.ButterKnife;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.util.Utils;

public class AboutCuckooActivity extends MvpActivity<LoginFragmentView, LoginFragPresenter> implements LoginFragmentView {


    @BindView(R.id.iconLogo)
    ImageView iconLogo;
    @BindView(R.id.tvVersionName)
    TextView tvVersionName;
    @BindView(R.id.tvTermsOfService)
    TextView tvTermsOfService;
    @BindView(R.id.tvIntroduction)
    TextView tvIntroduction;
    @BindView(R.id.tvCopyright)
    TextView tvCopyright;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, AboutCuckooActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_cuckoo;
    }

    @Override
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, AboutCuckooActivity.this);

    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setTitle(ResourceUtils.getString(R.string.str_about_cuckoo));

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
                tvVersionName.setText("V " + AppUtils.getLocalApkInfo().getVersionName());
            }
        });

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected LoginFragmentView bindView() {
        return this;
    }

    @Override
    protected LoginFragPresenter bindPresenter() {
        return new LoginFragPresenter(this);
    }


    @Override
    public void showToastMsg(String string) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
