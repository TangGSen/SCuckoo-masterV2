package com.ourcompany.app;

import android.app.Application;
import android.content.Context;

import com.mob.imsdk.MobIM;
import com.ourcompany.im.SimpleMobIMMessageReceiver;
import com.ourcompany.im.model.MsgReceiverListener;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;

import cn.bmob.v3.Bmob;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/16 16:59
 * Des    :
 */

public class MApplication extends Application {
    public static Context mContext;
    private SimpleMobIMMessageReceiver mobMsgRever = null;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        MServiceManager.getInstance().init(this);
        Bmob.initialize(this, Constant.BMOB_APPKEY);
        regMsgRev();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
      //  MultiDex.install(this);
    }

    public void regMsgRev() {
        if (mobMsgRever == null) {
            mobMsgRever = new SimpleMobIMMessageReceiver(this);
            MobIM.addMessageReceiver(mobMsgRever);
        }
    }

    public void onTerminate() {
        super.onTerminate();
        if (mobMsgRever != null) {
            MobIM.removeMessageReceiver(mobMsgRever);
        }
        mobMsgRever = null;
    }

    public void addMsgRever(MsgReceiverListener listener) {
        mobMsgRever.addMsgRever(listener);
    }

    public void removeMsgRever(MsgReceiverListener listener) {
        mobMsgRever.removeMsgRever(listener);
    }

    public void addGroupMsgRever(MsgReceiverListener listener) {
        mobMsgRever.addGroupMsgRever(listener);
    }

    public void removeGroupMsgRever(MsgReceiverListener listener) {
        mobMsgRever.removeGroupMsgRever(listener);
    }
}
