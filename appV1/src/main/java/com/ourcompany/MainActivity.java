package com.ourcompany;

import android.os.Bundle;
import android.widget.TextView;

import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.view.fragment.LoginFragmentView;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpActivity;

public class MainActivity extends MvpActivity<LoginFragmentView,LoginFragPresenter> implements LoginFragmentView {
    @BindView(R.id.test_id)
    TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();

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
}
