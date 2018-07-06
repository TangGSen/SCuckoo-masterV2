package com.ourcompany.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/12 19:39
 * Des    :
 */

public class FeedbackItem  extends BmobObject {
    private String content;// 帖子内容
    //这个对应着mob 的UserId
    private SUser user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SUser getUser() {
        return user;
    }

    public void setUser(SUser user) {
        this.user = user;
    }
}
