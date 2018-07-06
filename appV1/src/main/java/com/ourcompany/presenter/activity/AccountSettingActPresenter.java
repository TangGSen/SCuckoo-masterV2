package com.ourcompany.presenter.activity;

import android.content.Context;
import android.text.TextUtils;

import com.ourcompany.utils.Constant;
import com.ourcompany.utils.StringUtils;
import com.ourcompany.view.activity.AccountSetingActView;

import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Created by Administrator on 2017/8/20.
 */

public class AccountSettingActPresenter extends MvpBasePresenter<AccountSetingActView> {

    public AccountSettingActPresenter(Context context) {
        super(context);
    }


    public String getMyPhone() {
        if (Constant.CURRENT_USER == null || TextUtils.isEmpty(Constant.CURRENT_USER.phone.get())) {
            return "";
        }
        return StringUtils.getDealPhone(Constant.CURRENT_USER.phone.get());
    }
}
