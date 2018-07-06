package com.ourcompany.bean;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/31 19:32
 * Des    :
 */

public class UserAccoutLoginRes {
    private boolean isLoginSuccess;
    private String phone ;

    public String getPhone() {
        return phone;
    }

    public UserAccoutLoginRes setPhone(String phone) {
        this.phone = phone;
        return this;
    }



    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public UserAccoutLoginRes setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
        return this;
    }
}
