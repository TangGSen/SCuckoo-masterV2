package com.ourcompany.view.fragment;

import com.ourcompany.bean.json.CuckooServiceJson;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface ClassSerachFragmentView extends MvpView {
    void showEmptyView();

    void showDataView(CuckooServiceJson serviceJson);

    void showErrorView();

    /**
     * 重置搜索条件成功
     */
    void resetSerachSuccuss();
}
