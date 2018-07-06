package com.ourcompany.fragment;

import com.ourcompany.R;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.view.fragment.LoginFragmentView;

import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:02
 * Des    :
 */

public class MallFragment extends MvpFragment<LoginFragmentView,LoginFragPresenter> implements LoginFragmentView {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_mall;
    }

    @Override
    protected LoginFragmentView bindView() {
        return this;
    }

    @Override
    protected LoginFragPresenter bindPresenter() {
        return new LoginFragPresenter(getContext());
    }

    @Override
    public void showToastMsg(String string) {

    }
}
