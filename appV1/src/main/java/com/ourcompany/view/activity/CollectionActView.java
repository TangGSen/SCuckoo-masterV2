package com.ourcompany.view.activity;

import com.ourcompany.bean.bmob.Comment;
import com.ourcompany.bean.bmob.Post;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface CollectionActView extends MvpView {
    void showEmptyView();
    void showContentView(List<Post> list);
    void showLoadView();
    void showErrorView();
    void showOnLoadError();
    void showOnLoadFinish();
    void showOnloadMoreNoData();
}
