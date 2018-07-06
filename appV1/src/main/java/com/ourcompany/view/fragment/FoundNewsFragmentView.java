package com.ourcompany.view.fragment;

import com.ourcompany.bean.bmob.Post;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/6 21:58
 * Des    :
 */

public interface FoundNewsFragmentView extends MvpView {
    void showEmptyView();
    void showContentView(List<Post> list);
    void showLoadView();

    void showErrorView();

    /**
     * 刷新有数据
     *
     * @param list
     */
    void showOnReflsh(List<Post> list);

    /**
     * 刷新时没有最新的数据了
     */
    void showOnReflshNoNewsData();

    /**
     * 刷新失败
     */
    void showOnReflshError();

    /**
     * 加载更多失败
     */

    void showOnLoadError();

    /**
     * 加载更多完成
     */
    void showOnLoadFinish();

    /**
     * 加载更多时没有数据了
     */
    void showOnloadMoreNoData();
}
