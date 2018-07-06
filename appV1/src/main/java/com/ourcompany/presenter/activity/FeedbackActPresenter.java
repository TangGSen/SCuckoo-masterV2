package com.ourcompany.presenter.activity;

import android.content.Context;
import android.text.TextUtils;

import com.ourcompany.R;
import com.ourcompany.bean.bmob.FeedbackItem;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.FeedbackView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class FeedbackActPresenter extends MvpBasePresenter<FeedbackView> {

    public FeedbackActPresenter(Context context) {
        super(context);
    }

    public void submit(String content) {
        if (TextUtils.isEmpty(content)) {
            getView().vaifyTextError();
            return;
        }
        FeedbackItem item = new FeedbackItem();
        item.setContent(content);
        SUser user = new SUser();
        if (Constant.CURRENT_USER != null) {
            user.setUserName(Constant.CURRENT_USER.nickname.get());
            user.setObjectId(MServiceManager.getInstance().getLocalThirdPartyId());
        } else {
            user.setUserName(ResourceUtils.getString(R.string.anonymous));
        }
        item.setUser(user);
        item.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    getView().submitSuccess();
                } else {
                    getView().submitError();

                }
            }
        });

    }
}
