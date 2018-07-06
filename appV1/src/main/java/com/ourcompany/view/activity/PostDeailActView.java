package com.ourcompany.view.activity;

import com.ourcompany.bean.bmob.Comment;
import com.ourcompany.bean.bmob.Vote;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface PostDeailActView extends MvpView {
    void showLoadView();


    void submitError();

    void submitOk(Comment comment);

    void userIsLikeThis(boolean isLike);
    void userChangeLikeThis(boolean isLike);

    /**
     * 用户是否投票了
     * @param isVote
     * @param vote
     */
    void showIsUserVote(boolean isVote, Vote vote);

    /**
     * 投票成功
     * @param vote
     */
    void addUserVoteSuccess(Vote vote);

    /**
     * 投票失败，请重试
     */
    void optionUserVoteFail();

    void deleteUserVoteSuccess();
}
