package com.ourcompany.bean;

import com.mob.ums.User;

import java.io.Serializable;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/2 16:06
 * Des    :
 */

public class MUser implements Serializable{
    private User user;

    public MUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
