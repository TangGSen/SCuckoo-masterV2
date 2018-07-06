package com.ourcompany.presenter.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.ourcompany.bean.ChatContacts;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.view.fragment.MessageContactsFrameView;

import java.util.ArrayList;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/3 22:44
 * Des    :
 */

public class ContactsFragPresenter extends MvpBasePresenter<MessageContactsFrameView> {

    private static final int MSG_GETTFRIENDS_SUCCESS = 0;
    private static final int MSG_GET0TFRIENDS_FAIL = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GETTFRIENDS_SUCCESS:
                    break;

                case MSG_GET0TFRIENDS_FAIL:
                    break;
            }
        }
    };

    public ContactsFragPresenter(Context context) {
        super(context);
    }

    /**
     * 刷新数据
     */
    public void refreshData(int offset, int pageSize) {
        loadData(offset, pageSize);
    }

    /**
     * User user 用户User
     int offset 当前页数
     int pageSize 每页数量
     OperationCallback callback 操作回调
     * @param offset
     * @param pageSize
     */
    public void loadData(int offset, int pageSize) {
        //先判断网络
        //loading
        MServiceManager.getInstance().getFriends(offset, pageSize, new OperationCallback<ArrayList<User>>() {
            @Override
            public void onSuccess(final ArrayList<User> users) {
                super.onSuccess(users);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ChatContacts chatContacts = new ChatContacts();
                        chatContacts.setList(users);
                        getView().showContentView(chatContacts);
                    }
                });
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().showLoadingFailed();
                    }
                });
            }
        });

    }
}
