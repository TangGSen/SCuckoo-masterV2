package com.ourcompany.bean.bmob;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/7 18:08
 * Des    :帖子
 */

public class Post extends BmobObject {
    private String content;// 帖子内容
    //这个对应着mob 的UserId
    private SUser user;
    //带有一个userid ,双层保险，当没有保存user 成功时，可以用这个替代
    private String userId;
    private List<BmobFile> files;//帖子图片
    private List<String> imageUrls;//帖子图片
    private BmobRelation likes;//多对多关系：用于存储喜欢该帖子的所有用户
    //喜欢的总数，投票的总数
    private Integer likeCount;
    //评论的总数
    private Integer commentCount;
    //投票总数
    private Integer voteCount;
    //该帖子的胜利者或者是中标者
    private SUser mWinningBidder;
    //是否允许投票
    private Boolean mPostVoteDeadline;
    //是否需要投票，只有是业主发表的的需求才为ture
    private Boolean isNeedVote;

    public Boolean getmPostVoteDeadline() {
        return mPostVoteDeadline;
    }

    public void setmPostVoteDeadline(Boolean mPostVoteDeadline) {
        this.mPostVoteDeadline = mPostVoteDeadline;
    }


    public Boolean getNeedVote() {
        return isNeedVote;
    }

    public void setNeedVote(Boolean needVote) {
        isNeedVote = needVote;
    }


    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public SUser getmWinningBidder() {
        return mWinningBidder;
    }

    public void setmWinningBidder(SUser mWinningBidder) {
        this.mWinningBidder = mWinningBidder;
    }



    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    private int postType;

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SUser getUser() {
        return user;
    }

    public void setUser(SUser user) {
        this.user = user;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<BmobFile> getFiles() {
        return files;
    }

    public void setFiles(List<BmobFile> files) {
        this.files = files;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }
}
