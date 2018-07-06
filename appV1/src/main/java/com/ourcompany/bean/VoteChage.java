package com.ourcompany.bean;

import com.ourcompany.bean.bmob.Vote;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/11 18:52
 * Des    :
 */

public class VoteChage {
    private Vote mVote;
    private boolean isAdd;

    public Vote getmVote() {
        return mVote;
    }

    public VoteChage setmVote(Vote mVote) {
        this.mVote = mVote;
        return this;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public VoteChage setAdd(boolean add) {
        isAdd = add;
        return this;
    }
}
