package com.ourcompany.view.activity;

import com.mob.ums.User;

import java.util.ArrayList;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/17 21:12
 * Des    : 搜索UI
 */

public interface SearchActvityView extends MvpView {
    void loading();
    void loaded();
    void showSearchRes(ArrayList<User> users);

    void showError(String message);
}
