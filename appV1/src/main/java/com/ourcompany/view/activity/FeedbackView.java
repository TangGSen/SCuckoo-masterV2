package com.ourcompany.view.activity;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface FeedbackView extends MvpView {
    /**
     *字符不合法
     */
    void vaifyTextError();

    void submitSuccess();

    void submitError();
}
