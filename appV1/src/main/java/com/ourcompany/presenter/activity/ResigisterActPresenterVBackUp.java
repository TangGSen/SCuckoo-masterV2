//package com.ourcompany.presenter.activity;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.ourcompany.R;
//import com.ourcompany.app.MApplication;
//import com.ourcompany.utils.Constant;
//import com.ourcompany.utils.NetWorkUtils;
//import com.ourcompany.utils.ResourceUtils;
//import com.ourcompany.view.activity.ResigisterActView;
//
//import cn.smssdk.EventHandler;
//import cn.smssdk.SMSSDK;
//import company.com.commons.framework.presenter.MvpBasePresenter;
//
//
///**
// * Author : 唐家森
// * Version: 1.0
// * On     : 2018/1/17 21:14
// * Des    : 备份
// */
//
//public class ResigisterActPresenterVBackUp extends MvpBasePresenter<ResigisterActView> {
//    //开始倒数
//    public static final int  MSG_COUNTING_TIME = 0;
//    public static final int  MSG_ERROR_GET_CODE= 1;
//    //验证成功
//    public static final int  MSG_VERIFY_SUCCESS= 2;
//    public static final int  MSG_VERIFY_FAIL= 3;
//    public int currentTime = 0;
//    private Handler mHandler = new Handler(Looper.getMainLooper()){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case MSG_COUNTING_TIME:
//                    if(currentTime>=0){
//                        String mess = String.format(ResourceUtils.getString(R.string.get_count_time),currentTime);
//                        currentTime--;
//                        getView().setSafetyBtnText(mess);
//                        Message message =  mHandler.obtainMessage();
//                        mHandler.sendEmptyMessageDelayed(MSG_COUNTING_TIME,1000);
//                    }else{
//                        getView().setSafetyBtnEnable(true);
//                        getView().setSafetyBtnText(ResourceUtils.getString(R.string.get_safety_code));
//                    }
//                    break;
//                case MSG_ERROR_GET_CODE:
//                    getView().setSafetyBtnEnable(true);
//                    getView().setSafetyBtnText(ResourceUtils.getString(R.string.get_safety_code));
//                    getView().showToastMsg(ResourceUtils.getString(R.string.get_code_error));
//
//                    break;
//                case MSG_VERIFY_FAIL:
//                    //失败和成功都得注销
//                    getView().verifyFail();
//
//                    break;
//                case MSG_VERIFY_SUCCESS:
//
//                    getView().showToastMsg(ResourceUtils.getString(R.string.verify_success));
//                    getView().verifySuccess();
//                    break;
//
//            }
//        }
//    };
//    public ResigisterActPresenterVBackUp(Context context) {
//        super(context);
//    }
//
//    //获取验证码
//    public void getSafetyCode(String phone) {
//        if(TextUtils.isEmpty(phone)){
//            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_username_hint));
//        }else{
//            if(NetWorkUtils.isConnected(MApplication.mContext)){
//                getView().setSafetyBtnEnable(false);
//                getView().setSafetyBtnText(ResourceUtils.getString(R.string.getting));
//                getView().getSafetyCodeing();
//                //目前先默认使用国内
//                requestCode(Constant.COUNTRY_CODE,  phone);
//            }else{
//                //网络未连接
//                getView().showToastMsg(ResourceUtils.getString(R.string.net_unlink));
//            }
//
//        }
//
//    }
//    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
//    public void requestCode(String country, String phone) {
//        SMSSDK.unregisterAllEventHandler();
//        Log.e("sen","phone:"+phone);
//        // 注册一个事件回调，用于处理发送验证码操作的结果
//        SMSSDK.registerEventHandler(new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//                Message message =  mHandler.obtainMessage();
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    //  处理成功得到验证码的结果
//                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
//                    //然后,倒数三十秒
//                    currentTime = Constant.SAFETY_CODE_TIME_INTERVAL;
//                   message.what = MSG_COUNTING_TIME;
//                   message.sendToTarget();
//                } else{
//                    //  处理错误的结果
//                    message.what = MSG_ERROR_GET_CODE;
//                    message.sendToTarget();
//
//                }
//
//            }
//        });
//        // 触发操作
//        SMSSDK.getVerificationCode(country, phone);
//    }
//
//    //提交验证码
//    public void sendSafetyCode(String country, String phone, String code){
//        SMSSDK.unregisterAllEventHandler();
//        if(TextUtils.isEmpty(country)){
//            getView().showToastMsg(ResourceUtils.getString(R.string.unkown_error));
//            return;
//        }
//        if(TextUtils.isEmpty(phone)){
//            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_username_hint));
//            return;
//        }
//
//        if(TextUtils.isEmpty(code)){
//            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_safety_hint));
//            return;
//        }
//        //再检查网络
//        if(!NetWorkUtils.isConnected(MApplication.mContext)){
//            getView().showToastMsg(ResourceUtils.getString(R.string.net_unlink));
//            return;
//        }
//        //开始验证
//        getView().sendSafetyCodeing();
//        submitCode( country,  phone,  code);
//    }
//    // 提交验证码，其中的code表示验证码，如“1357”
//    public void submitCode(String country, String phone, String code) {
//        // 注册一个事件回调，用于处理提交验证码操作的结果
//        SMSSDK.registerEventHandler(new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//                Message message = mHandler.obtainMessage();
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    // 处理验证成功的结果
//                    message.what = MSG_VERIFY_SUCCESS;
//                    message.sendToTarget();
//                } else{
//                    //  处理错误的结果
//                    message.what = MSG_VERIFY_FAIL;
//                    message.sendToTarget();
//                }
//
//            }
//        });
//        // 触发操作
//        SMSSDK.submitVerificationCode(country, phone, code);
//    }
//}
