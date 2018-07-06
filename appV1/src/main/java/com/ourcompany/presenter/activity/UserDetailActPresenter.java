package com.ourcompany.presenter.activity;

import android.content.Context;
import android.text.TextUtils;

import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.view.activity.UserClassifyDetailActView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/5/14 22:53
 * Des    :
 */

public class UserDetailActPresenter extends MvpBasePresenter<UserClassifyDetailActView> {
    public UserDetailActPresenter(Context context) {
        super(context);
    }

    public void getUserData(SUser mUser, String mUserId) {
        if(mUser!=null){
            getView().showContent(mUser);
        }else{
            if(!TextUtils.isEmpty(mUserId)){
                getUserDataById(mUserId);
            }
        }
    }

    private void getUserDataById(String mUserId) {
        //查找Person表里面id为6b6c11c537的数据
        BmobQuery<SUser> bmobQuery = new BmobQuery<SUser>();
        bmobQuery.getObject(mUserId, new QueryListener<SUser>() {
            @Override
            public void done(SUser user,BmobException e) {
                if(e==null){
                    getView().showContent(user);
                }else{
                   getView().getUserDataError();
                }
            }
        });
    }
}
