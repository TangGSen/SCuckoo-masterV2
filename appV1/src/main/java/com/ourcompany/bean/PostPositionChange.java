package com.ourcompany.bean;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/9 20:41
 * Des    :
 */

public class PostPositionChange {
    private int position;
    private boolean isCollection;
    private boolean isUpdateComment;

    public int getPosition() {
        return position;
    }

    public PostPositionChange setPosition(int position) {
        this.position = position;
        return this;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public PostPositionChange setCollection(boolean collection) {
        isCollection = collection;
        return this;
    }

    public boolean isUpdateComment() {
        return isUpdateComment;
    }

    public PostPositionChange setUpdateComment(boolean updateComment) {
        isUpdateComment = updateComment;
        return this;
    }
}
