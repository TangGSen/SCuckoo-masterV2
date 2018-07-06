package com.ourcompany.bean;

import com.mob.imsdk.model.IMConversation;

import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/3 17:18
 * Des    : 聊天会话
 */

public class ChatConversations {
    private List<IMConversation> list;

    public List<IMConversation> getList() {
        return list;
    }

    public void setList(List<IMConversation> list) {
        this.list = list;
    }
}
