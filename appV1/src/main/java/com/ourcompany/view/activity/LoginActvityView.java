package com.ourcompany.view.activity;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/17 21:12
 * Des    : 登陆UI
 */

public interface LoginActvityView extends MvpView {
    void loading();
    void loaded();
    void loginSucess();
    void loginFial();


    void verifyError();
}
