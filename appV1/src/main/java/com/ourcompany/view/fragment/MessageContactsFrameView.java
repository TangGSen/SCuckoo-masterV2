package com.ourcompany.view.fragment;

import com.ourcompany.bean.ChatContacts;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface MessageContactsFrameView extends MvpView {
    void showLoadingFailed();
    void showContentView(ChatContacts chatContacts);
}
