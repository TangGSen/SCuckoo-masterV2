package com.ourcompany.view.fragment;

import com.ourcompany.bean.AccoutRsigisterBean;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/17 21:12
 * Des    : 登陆UI
 */

public interface ResigisterAccoutFragView extends MvpView {
    /**
     * 获取验证码
     */
    void getSafetyCodeing();

    /**
     * 发送验证码
     */
    void sendSafetyCodeing();
    /**
     * 发送完毕，恢复按钮的状态
     */

    void sendSafetyCoded();

    /**
     * 设置获取验证码按钮的是否可用
     * @param enable
     */
    void setSafetyBtnEnable(boolean enable);

    /**
     * 设置获取验证码的按钮是否可用
     * @param msg
     */
    void setSafetyBtnText(String msg);

    /**
     * Toast
     * @param msg
     */
    void showToastMsg(String msg);

    /**
     * 验证失败
     */
    void verifyFail(String msg);

    /**
     * 验证成功
     */
    void verifySuccess();

    void hasNotNet();

    void logining(AccoutRsigisterBean accoutRsigisterBean);

    void verifyTextError();

    void setCurrentUserName(String s);

    void thisPhoneIsExist();

    void thisPhoneIsNotExist();
}
