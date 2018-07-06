package com.ourcompany.view.activity;

import com.ourcompany.bean.bmob.SUser;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/5/14 22:51
 * Des    :
 */

public interface UserClassifyDetailActView extends MvpView {
    void showContent(SUser mUser);


    void getUserDataError();
}
