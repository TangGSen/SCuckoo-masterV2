package com.ourcompany.view.fragment;

import android.support.v7.util.DiffUtil;

import com.ourcompany.bean.bmob.Vote;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface VoteFragView extends MvpView {
    void showEmptyView();
    void showContentView(List<Vote> list);
    void showLoadView();

    void showErrorView();
    void showDiffResult(DiffUtil.DiffResult result);
    void showOnReflshError();
    void showOnLoadError();
    void showOnLoadFinish();
    void showOnloadMoreNoData();
    void showOnReflshNoNewsData();
    void showOnReflsh(List<Vote> list);
}
