package com.ourcompany.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/3 11:07
 * Des    : 投票
 */

public class Vote extends BmobObject {

    private SUser user;//评论的用户，Pointer类型，一对一关系

    private Post post; //所评论的帖子，这里体现的是一对多的关系，一个评论只能属于一个微博
    private String userId; //作为唯一的键，投过不能在投了

    public SUser getUser() {
        return user;
    }

    public void setUser(SUser user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vote) {
            Vote other = (Vote) obj;
            return (getObjectId().equals(other.getObjectId()));
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getObjectId().hashCode();
    }
}
