package com.ourcompany.view.activity;

import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.utils.LocationOption;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface UserClassifyActView extends MvpView {
    void showMLocation(LocationOption.MLocation mLocation, boolean isCityPick);

    void showLoactionError(boolean isCityPick);

    void showEmptyView();
    void showContentView(List<SUser> list);
    void showLoadView();

    void showErrorView();

    /**
     * 刷新有数据
     *
     * @param list
     */
    void showOnReflsh(List<SUser> list);

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
