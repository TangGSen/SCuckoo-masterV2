package com.ourcompany.presenter.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.mob.jimu.query.Condition;
import com.mob.jimu.query.Query;
import com.mob.jimu.query.data.Text;
import com.mob.tools.utils.Hashon;
import com.mob.ums.OperationCallback;
import com.mob.ums.QueryView;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.AccoutRsigisterBean;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.NetWorkUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.ResigisterAccoutFragView;

import java.util.ArrayList;
import java.util.HashMap;

import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/17 21:14
 * Des    :
 */

public class ResigisterAccoutFragPresenter extends MvpBasePresenter<ResigisterAccoutFragView> {
    //开始倒数
    private static final int MSG_COUNTING_TIME = 0;
    private static final int MSG_ERROR_GET_CODE = 1;
    //验证成功
    private static final int MSG_RESIGISTER_SUCCESS = 2;
    private static final int MSG_RESIGISTER_FAIL = 3;
    private static String currentPhone = "";
    public int currentTime = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_COUNTING_TIME:
                    if (currentTime >= 0) {
                        String mess = String.format(ResourceUtils.getString(R.string.get_count_time), currentTime);
                        currentTime--;
                        getView().setSafetyBtnText(mess);
                        Message message = mHandler.obtainMessage();
                        mHandler.sendEmptyMessageDelayed(MSG_COUNTING_TIME, 1000);
                    } else {
                        getView().setSafetyBtnEnable(true);
                        getView().setSafetyBtnText(ResourceUtils.getString(R.string.get_safety_code));
                    }
                    break;
                case MSG_ERROR_GET_CODE:
                    getView().setSafetyBtnEnable(true);
                    getView().setSafetyBtnText(ResourceUtils.getString(R.string.get_safety_code));
                    getView().showToastMsg(ResourceUtils.getString(R.string.get_code_error));
                    break;
                case MSG_RESIGISTER_FAIL:
                    //失败和成功都得注销
                    String mes = (String) msg.obj;

                    getView().verifyFail(mes);
                    getView().verifyTextError();
                    break;
                case MSG_RESIGISTER_SUCCESS:
                    getView().showToastMsg(ResourceUtils.getString(R.string.resigister_success));
                    getView().verifySuccess();
                    break;


            }
        }
    };

    public ResigisterAccoutFragPresenter(Context context) {
        super(context);
    }

    //获取验证码
    public void getSafetyCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_username_hint));
        } else {
            if (NetWorkUtils.isConnected(MApplication.mContext)) {
                getView().setSafetyBtnEnable(false);
                getView().setSafetyBtnText(ResourceUtils.getString(R.string.getting));
                getView().getSafetyCodeing();
                //目前先默认使用国内
                phone = phone.replaceAll(" ", "");
                requestCode(Constant.COUNTRY_CODE, phone);
            } else {
                //网络未连接
                getView().showToastMsg(ResourceUtils.getString(R.string.net_unlink));
            }

        }

    }

    /**
     * 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
     */

    public void requestCode(String country, String phone) {


//        // 触发操作
//        SMSSDK.getVerificationCode(country, phone);
        UMSSDK.sendVerifyCode(country, phone, new OperationCallback<Boolean>() {
            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                Message message = mHandler.obtainMessage();
                message.what = MSG_ERROR_GET_CODE;
                message.sendToTarget();
                //   getView().showToastMsg(throwable.getMessage() + "\n***" + throwable.getLocalizedMessage());

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                Message message = mHandler.obtainMessage();
                currentTime = Constant.SAFETY_CODE_TIME_INTERVAL;
                message.what = MSG_COUNTING_TIME;
                message.sendToTarget();
            }
        });
    }

    //提交验证码
    public void sendSafetyCode(String phone, String code, String password) {
        if (TextUtils.isEmpty(phone)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_username_hint));
            getView().verifyTextError();
            return;
        }

        if (TextUtils.isEmpty(code)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_safety_hint));
            getView().verifyTextError();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_password_hint));
            getView().verifyTextError();
            return;
        }
        //再检查网络
        if (!NetWorkUtils.isConnected(MApplication.mContext)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.net_unlink));
            getView().verifyTextError();
            return;
        }
        //开始验证
        getView().sendSafetyCodeing();
        phone = phone.replaceAll(" ", "");
        submitCode(Constant.COUNTRY_CODE, phone, code, password);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, final String phone, String code, final String password) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
//        // 触发操作
//        SMSSDK.submitVerificationCode(country, phone, code);
        User user = new User();

        UMSSDK.registerWithPhoneNumber(country, phone, code, password, user, new OperationCallback<User>() {
            @Override
            public void onSuccess(final User user) {
                super.onSuccess(user);
                Message message = mHandler.obtainMessage();
                // 处理验证成功的结果
                message.what = MSG_RESIGISTER_SUCCESS;
                message.sendToTarget();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().logining(new AccoutRsigisterBean(user, password));
                    }
                });

            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                LogUtils.e("sen", "onFailed :" + throwable.getMessage());
                //  处理错误的结果
                Message message = mHandler.obtainMessage();
                message.what = MSG_RESIGISTER_FAIL;
                message.obj = ResourceUtils.getString(R.string.submit_info_error);
                message.sendToTarget();
            }
        });
    }


    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 检查用户是否存在
     *
     * @param phone
     */
    public void checkUserIsExist(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_username_hint));
            return;
        }
        final String phoneValue = phone.replaceAll(" ", "");
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                if (!NetWorkUtils.isConnected(MApplication.mContext)) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getView().showToastMsg(ResourceUtils.getString(R.string.net_unlink));
                        }
                    });
                    return;
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getView().setSafetyBtnEnable(false);
                            getView().setSafetyBtnText(ResourceUtils.getString(R.string.getting));
                            getView().getSafetyCodeing();
                        }
                    });
                }
                final User me = new User();
                LogUtils.e("sen", "开始获取到了:" + me.id.get());
                // 从UMSSDK中获取一个用于查询用户信息的Query对象
                Query q = UMSSDK.getQuery(QueryView.USERS);
                // 设置查询条件：用户ID = 登录用户的ID
                q.condition(Condition.eq(me.phone.getName(), Text.valueOf(phoneValue)));
                // 解析查询结果为一个HashMap
                HashMap<String, Object> res = null;
                try {
                    res = new Hashon().fromJson(q.query());
                    // 从HashMap中读取搜索结果集
                    ArrayList<Object> list = (ArrayList<Object>) res.get("list");
                    // 其中第一个元素就是当前用户的资料
                    HashMap<String, Object> info = (HashMap<String, Object>) list.get(0);
                    // 将资料解析到User对象中
                    me.parseFromMap(info);
                    LogUtils.e("sen", "获取到了：" + me.id.get());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(me.id.get())) {
                                getView().thisPhoneIsNotExist();
                            } else {
                                getView().thisPhoneIsExist();
                            }
                        }
                    });
                } catch (Throwable throwable) {

                }

            }
        });
    }
}
