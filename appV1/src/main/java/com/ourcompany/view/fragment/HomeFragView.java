package com.ourcompany.view.fragment;


import com.ourcompany.bean.json.AdvertisementData;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Created by Administrator on 2017/8/20.
 */

public interface HomeFragView extends MvpView {
    void showAdDatas(List<AdvertisementData.AdSettingBean> adDatas, List<String> urlStr);

    void showEmptyAdData();

}
