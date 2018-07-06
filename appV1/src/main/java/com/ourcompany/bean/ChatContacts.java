package com.ourcompany.bean;

import com.mob.ums.User;

import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/3 17:18
 * Des    : 聊天联系人，好友
 */

public class ChatContacts {
    private List<User> list;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
