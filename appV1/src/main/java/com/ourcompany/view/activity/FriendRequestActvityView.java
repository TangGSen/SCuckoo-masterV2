package com.ourcompany.view.activity;

import com.ourcompany.bean.FriendRequestItem;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/17 21:12
 * Des    : 登陆UI
 */

public interface FriendRequestActvityView extends MvpView {


    void showContent(List<FriendRequestItem> friendRequests);

    void showErrorView();

    void showEptyView();

    void friendRequestAccpetOk(int position);

    void friendRequestRefuseOk(int position);

    void friendRequestFaild();


}
