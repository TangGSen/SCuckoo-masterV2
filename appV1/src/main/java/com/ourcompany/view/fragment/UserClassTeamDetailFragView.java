package com.ourcompany.view.fragment;

import com.ourcompany.bean.bmob.TeamMember;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface UserClassTeamDetailFragView extends MvpView {
    void showErrorView();

    void showEmptyView();

    void showDataView(List<TeamMember> list);

    void showOnloadMoreNoData();

    void showOnLoadFinish();
}
