package com.ourcompany.view.activity;


import com.mob.imsdk.model.IMUser;

import company.com.commons.framework.view.MvpView;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/27 10:02
 * Des    :
 */

public interface ChatingActivityView extends MvpView {
    void onLogining();

    void setIMUser(IMUser imUser);

    void getIMUserError();
}
