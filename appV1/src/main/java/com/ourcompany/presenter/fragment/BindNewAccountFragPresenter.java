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
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.NetWorkUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.BindNewAccountFragView;

import java.util.ArrayList;
import java.util.HashMap;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class BindNewAccountFragPresenter extends MvpBasePresenter<BindNewAccountFragView> {
    public int currentTime = 0;
    //开始倒数
    private static final int MSG_COUNTING_TIME = 0;
    private Handler mHandler2 = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_COUNTING_TIME:
                    if (currentTime >= 0) {
                        String mess = String.format(ResourceUtils.getString(R.string.get_count_time), currentTime);
                        currentTime--;
                        getView().setSafetyBtnTextAndStutes(mess,false);
                        mHandler2.sendEmptyMessageDelayed(MSG_COUNTING_TIME, 1000);
                    } else {
                        getView().setSafetyBtnTextAndStutes(ResourceUtils.getString(R.string.get_safety_code),true);
                    }
                    break;
            }
        }
    };

    public BindNewAccountFragPresenter(Context context) {
        super(context);
    }


    public void verifyPhone(String curent, String newPhone) {
        curent = curent.replaceAll(" ", "");
        final String newPhoneStr = newPhone.replaceAll(" ", "");
        if (TextUtils.isEmpty(curent)) {
            showErrorMsg(ResourceUtils.getString(R.string.str_input_current_phone));
            return;
        }

        if (TextUtils.isEmpty(newPhoneStr)) {
            showErrorMsg(ResourceUtils.getString(R.string.str_input_new_phone));
            return;
        }


        if (Constant.CURRENT_USER == null) {
            showErrorMsg(ResourceUtils.getString(R.string.str_user_not_login));
            return;
        }
        //
        if (curent.equals(Constant.CURRENT_USER.phone.get())) {
            if (!newPhoneStr.equals(Constant.CURRENT_USER.phone.get())) {
                //需要再次校验这个账号有没存在
                EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetWorkUtils.isConnected(MApplication.mContext)) {
                            showErrorMsg(ResourceUtils.getString(R.string.net_unlink));
                            return;
                        }
                        final User me = new User();
                        // 从UMSSDK中获取一个用于查询用户信息的Query对象
                        Query q = UMSSDK.getQuery(QueryView.USERS);
                        // 设置查询条件：用户ID = 登录用户的ID
                        q.condition(Condition.eq(me.phone.getName(), Text.valueOf(newPhoneStr)));
                        // 解析查询结果为一个HashMap
                        HashMap<String, Object> res = null;
                        try {
                            if (q.query() != null) {
                                res = new Hashon().fromJson(q.query());
                                // 从HashMap中读取搜索结果集
                                if (res != null) {
                                    ArrayList<Object> list = (ArrayList<Object>) res.get("list");
                                    if (list != null && list.size() > 0) {
                                        // 其中第一个元素就是当前用户的资料
                                        HashMap<String, Object> info = (HashMap<String, Object>) list.get(0);
                                        // 将资料解析到User对象中
                                        if (info != null) {
                                            me.parseFromMap(info);
                                            if (TextUtils.isEmpty(me.id.get())) {
                                                //证明这个是可以注册的
                                                showSuccess(newPhoneStr);
                                                LogUtils.e("sen", "me.id.get()");
                                            } else {
                                                //这个是存在了，不能注册
                                                LogUtils.e("sen", "这个是存在了，不能注册");
                                                showErrorMsg(ResourceUtils.getString(R.string.str_bind_new_is_exsit));
                                            }
                                        } else {
                                            LogUtils.e("sen", "info==null");
                                            //info ==null
                                            showSuccess(newPhoneStr);
                                        }


                                    } else {
                                        LogUtils.e("sen", "list ==null 或者size<0");
                                        //list ==null 或者size<0
                                        showSuccess(newPhoneStr);
                                    }
                                } else {
                                    LogUtils.e("sen", "res ==null");
                                    //res ==null
                                    showSuccess(newPhoneStr);
                                }

                            } else {
                                LogUtils.e("sen", "q.query()==null");
                                //
                                showSuccess(newPhoneStr);
                            }


                        } catch (Throwable throwable) {
                            LogUtils.e("sen", "异常");
                            showErrorMsg(ResourceUtils.getString(R.string.str_bind_new_error));
                        }

                    }
                });


            } else {
                showErrorMsg(ResourceUtils.getString(R.string.str_input_new_phone_error));
            }
        } else {
            //校验
            showErrorMsg(ResourceUtils.getString(R.string.str_input_current_phone_error));
        }
    }

    /**
     * 显示错误的信息
     *
     * @param msg
     */
    private void showErrorMsg(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().checkPhoneError(msg);
            }
        });
    }

    private void showSuccess(final String newPhone) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().checkPhoneSuccess(newPhone);

            }
        });
    }

    /**
     * 获取验证码
     *
     * @param mPhone
     */
    public void getSafetyCode(String mPhone) {
        if (TextUtils.isEmpty(mPhone)) {
            getView().safetyCodeError(ResourceUtils.getString(R.string.net_exception));
        } else {
            if (NetWorkUtils.isConnected(MApplication.mContext)) {
                getView().setSafetyBtnTextAndStutes(ResourceUtils.getString(R.string.getting), false);
                //目前先默认使用国内
                mPhone = mPhone.replaceAll(" ", "");
                requestCode(Constant.COUNTRY_CODE, mPhone);
            } else {
                //网络未连接
                getView().safetyCodeError(ResourceUtils.getString(R.string.net_unlink));
            }

        }
    }

    /**
     * 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
     */

    public void requestCode(String country, String phone) {

        UMSSDK.sendVerifyCode(country, phone, new OperationCallback<Boolean>() {
            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                getView().safetyCodeError(ResourceUtils.getString(R.string.get_safety_code_error));
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                Message message = mHandler2.obtainMessage();
                currentTime = Constant.SAFETY_CODE_TIME_INTERVAL;
                message.what = MSG_COUNTING_TIME;
                message.sendToTarget();
            }
        });
    }


    public void submitSatyCode(String mPhone, String code) {
        if (TextUtils.isEmpty(mPhone)) {
            getView().sumitCodeError(ResourceUtils.getString(R.string.str_input_current_phone));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            getView().sumitCodeError(ResourceUtils.getString(R.string.str_input_code_null));
            return;
        }
        /**
         * String country 国家代码，如：中国对应86
         String phone 手机号码
         String vcode 验证码
         String password 密码
         OperationCallback callback 操作回调
         */
        UMSSDK.bindPhone(Constant.COUNTRY_CODE, mPhone, code, "12345678sen", new OperationCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                getView().updatePhoneSuccess();
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                getView().updatePhoneFaild();
            }
        });
    }
}
