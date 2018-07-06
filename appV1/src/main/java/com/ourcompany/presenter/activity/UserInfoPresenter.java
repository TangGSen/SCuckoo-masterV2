package com.ourcompany.presenter.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.mob.jimu.query.data.Text;
import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.NetWorkUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.UserInfoActvityView;

import java.util.ArrayList;
import java.util.Set;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/8 16:49
 * Des    :
 */

public class UserInfoPresenter extends MvpBasePresenter<UserInfoActvityView> {
    public UserInfoPresenter(Context context) {
        super(context);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    //获取好友关系
    public void getFriendShip(final String id) {
        Text[] ids = new Text[1];
        Text value = new Text(id);
        ids[0] = value;
        MServiceManager.getInstance().getFriendShip(ids, new OperationCallback<Set<String>>() {
            @Override
            public void onSuccess(Set<String> strings) {
                super.onSuccess(strings);
                if (strings != null && strings.size() > 0) {
                    for (String str : strings) {
                        if (id.equals(str)) {

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getView().isMyFriend();
                                }
                            });

                            return;
                        }
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().isNotMyFriend();
                    }
                });

            }


            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().requestIsMyFriendFaild();
                    }
                });

            }
        });
    }




    /**
     * 添加好友
     *
     * @param message
     */
    public void addFrindByUserId(User target, String message) {
        if (target == null) {
            //其实该未知错误就是userId 传空了
            getView().onErrortToToast(ResourceUtils.getString(R.string.unkown_error));
            return;
        }

        if (TextUtils.isEmpty(message)) {
            message = ResourceUtils.getString(R.string.defualt_msg_add_friend);
        }

        MServiceManager.getInstance().addFriend(target, message, new OperationCallback<Void>() {
            @Override
            public void onFailed(final Throwable throwable) {
                super.onFailed(throwable);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (NetWorkUtils.isConnected(MApplication.mContext)) {
                            getView().showError(throwable.getMessage());
                        } else {
                            getView().showError(ResourceUtils.getString(R.string.net_exception));
                        }

                    }
                });
            }

            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().showSuccess(ResourceUtils.getString(R.string.send_request_add_friend_ok));
                    }
                });

            }
        });


    }

    public void getCurrentUserInfo(String mCurrentUserId) {
        if(TextUtils.isEmpty(mCurrentUserId)){
            getView().getUserInfoFailed();
            return;
        }
        MServiceManager.getInstance().getUserInfoByUserId(mCurrentUserId, new OperationCallback<ArrayList<User>>() {
            @Override
            public void onSuccess(final ArrayList<User> users) {
                super.onSuccess(users);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (users != null && users.size() > 0 && users.get(0) != null) {
                            getView().getUserInfoSuccess( users.get(0));
                        } else {
                            getView().getUserInfoFailed();
                        }
                    }
                });
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().getUserInfoFailed();
                    }
                });
            }
        });
    }
}
