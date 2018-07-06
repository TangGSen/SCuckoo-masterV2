package com.ourcompany.presenter.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.ourcompany.R;
import com.ourcompany.activity.CollectionActivity;
import com.ourcompany.activity.imui.LoginActivity;
import com.ourcompany.activity.setting.EditextUserInfoActivity;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.MineFragmentView;

import company.com.commons.framework.presenter.MvpBasePresenter;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 22:09
 * Des    : 我的 页面 Presenter
 */

public class MineFragPresenter extends MvpBasePresenter<MineFragmentView> {
    //开始倒数
    public static final int MSG_USER_LOGIN = 0;
    public static final int MSG_USER_LOGIN_FAIL = 1;
    public int currentTime = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_USER_LOGIN:
                    getView().showUserInfo();
                    getView().changeUserLoginState(true);
                    break;
                case MSG_USER_LOGIN_FAIL:
                    getView().changeUserLoginState(false);
                    break;
            }
        }
    };

    public MineFragPresenter(Context context) {
        super(context);
    }
    //点击用户头像
    public void onClickUserImage(Activity activity, MvpFragment mineFragment){
        if(activity==null){
            return;
        }
        if( Constant.CURRENT_USER!=null&&MServiceManager.getInstance().getUserIsLogin()){
            //去修改资料

            EditextUserInfoActivity.gotoThis(activity);
        }else{
            //去登陆
            Intent intent = new Intent(activity, LoginActivity.class);
            mineFragment.startActivity(intent);
        }

    }

    public void gotoCollection(Activity activity){
        if(activity==null){
            return;
        }

        if( Constant.CURRENT_USER!=null){
            //去修改资料
            CollectionActivity.gotoThis(activity, MServiceManager.getInstance().getLocalThirdPartyId(), ResourceUtils.getString(R.string.str_my_collection));
        }else{
            //去登陆
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }

    }

    public void getUserInfos() {
        if(Constant.CURRENT_USER !=null){
            Message message = mHandler.obtainMessage();
            message.what = MSG_USER_LOGIN;
            message.sendToTarget();
            return;
        }
        UMSSDK.getLoginUser(new OperationCallback<User>() {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                Constant.CURRENT_USER = user;
                Message message = mHandler.obtainMessage();
                message.what = MSG_USER_LOGIN;
                message.sendToTarget();
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                Message message = mHandler.obtainMessage();
                message.what = MSG_USER_LOGIN_FAIL;
                message.sendToTarget();
            }
        });
    }
}
