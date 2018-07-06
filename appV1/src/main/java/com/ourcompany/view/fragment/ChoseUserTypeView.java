package com.ourcompany.view.fragment;

import com.ourcompany.bean.json.UserType;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface ChoseUserTypeView extends MvpView {
    void showEmptyView();

    void showErrorView();

    void showContentView(List<UserType.UserTypeBean> userType);

    void setThridId(String s);

    void loadUserSuccess();

    void loadUserFaild();

    void updateUserInfoFailed();

    void updateUserInfoSuccess();
}
