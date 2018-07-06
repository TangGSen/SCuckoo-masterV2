package com.ourcompany.view.fragment;

import com.mob.imsdk.model.IMConversation;

import java.util.List;
import java.util.Map;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface ChatFrameView extends MvpView {
    void showLoadingFailed();
    void showContentView(List<IMConversation> get);
    void showContentView(List<IMConversation> newItem, Map<Integer,IMConversation> updateItem);

    void showNewFriendCount(int count);

    void mergeData(List<IMConversation> get);

    void showNotData();
}
