package com.ourcompany.bean;

import com.mob.ums.User;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/31 17:54
 * Des    :
 */

public class AccoutRsigisterBean {
    private User user;
    private String password;

    public AccoutRsigisterBean(User user, String password) {
        this.user = user;
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
