package com.ourcompany.view.fragment;


import com.ourcompany.bean.bmob.TeamCase;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Created by Administrator on 2017/8/20.
 */

public interface UserTeamCaseFragView extends MvpView {

    void showErrorView();

    void showDataView(List<TeamCase> list);

    void showEmptyView();

    void showOnloadMoreNoData();

    void showOnLoadFinish();

    void showOnLoadError();
}
