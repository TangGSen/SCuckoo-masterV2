package com.ourcompany.view.fragment;


import com.ourcompany.bean.bmob.Coupon;

import company.com.commons.framework.view.MvpView;

/**
 * Created by Administrator on 2017/8/20.
 */

public interface EditextCouponInfoFragView extends MvpView {

    void showEndTimeDialog();
    void showStartTimeDialog();

    void submitSuccess(Coupon coupon);

    void submitError();
}
