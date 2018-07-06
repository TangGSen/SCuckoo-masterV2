package com.ourcompany.bean.eventbus;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/20 14:44
 * Des    :
 */

public class UserLogout {
    private boolean isLogout;

    public UserLogout(boolean isLogout) {
        this.isLogout = isLogout;
    }

    public boolean isLogout() {
        return isLogout;
    }

    public void setLogout(boolean logout) {
        isLogout = logout;
    }
}
