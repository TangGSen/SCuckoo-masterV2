package com.ourcompany.bean;

import com.mob.ums.FriendRequest;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/24 17:32
 * Des    :
 */

public class FriendRequestItem {
    private FriendRequest FriendRequst;
    private String userName;

    public FriendRequest getFriendRequst() {
        return FriendRequst;
    }

    public void setFriendRequst(FriendRequest friendRequst) {
        FriendRequst = friendRequst;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
