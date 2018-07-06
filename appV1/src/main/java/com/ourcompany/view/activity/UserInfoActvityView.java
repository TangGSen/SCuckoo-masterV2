package com.ourcompany.view.activity;

import com.mob.ums.User;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/17 21:12
 * Des    : 搜索UI
 */

public interface UserInfoActvityView extends MvpView {
    void loading();
    void loaded();
    void showError(String message);

    void isMyFriend();

    void isNotMyFriend();

    void requestIsMyFriendFaild();

    void onErrortToToast(String string);
    void showSuccess(String string);

    void getUserInfoSuccess(User user);

    void getUserInfoFailed();
}
