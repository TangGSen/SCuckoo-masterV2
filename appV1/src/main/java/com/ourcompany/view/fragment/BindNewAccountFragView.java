package com.ourcompany.view.fragment;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface BindNewAccountFragView extends MvpView {
    void checkPhoneSuccess(String newPhone);

    void checkPhoneError(String string);

    void safetyCodeError(String string);

    void setSafetyBtnTextAndStutes(String string, boolean state);

    void safetyCodeSuccess();

    void updatePhoneSuccess();

    void updatePhoneFaild();
    //提交时出现失败
    void sumitCodeError(String string);
}
