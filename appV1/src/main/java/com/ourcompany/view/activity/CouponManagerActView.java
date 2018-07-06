package com.ourcompany.view.activity;


import com.ourcompany.bean.bmob.Coupon;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 下午2:08
 * Des    :
 */
public interface CouponManagerActView extends MvpView {

    void showEmptyView();

    void showOnloadMoreNoData();

    void showOnLoadFinish();

    void showContentView(List<Coupon> list);

    void showOnLoadError();

    void showErrorView();

    void showOnReflsh(List<Coupon> list);

    void showOnReflshNoNewsData();

    void showOnReflshError();

    void showLoadView();

}
