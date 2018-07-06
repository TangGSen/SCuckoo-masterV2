package com.ourcompany.presenter.fragment;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mob.jimu.query.data.DataType;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.ourcompany.bean.json.UserType;
import com.ourcompany.bean.bmob.AppSettingJson;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.view.fragment.ChoseUserTypeView;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class ChoseUserTypePresenter extends MvpBasePresenter<ChoseUserTypeView> {



    public ChoseUserTypePresenter(Context context) {
        super(context);
    }

    public void getUserType() {
        BmobQuery<AppSettingJson> query = new BmobQuery<AppSettingJson>();
        query.addWhereEqualTo(Constant.KEY_BMOB_APP_SETTING,Constant.KEY_BMOB_USERTYPE);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(1);
        //执行查询方法
        query.findObjects(new FindListener<AppSettingJson>() {
            @Override
            public void done(final List<AppSettingJson> list, BmobException e) {
                if (e == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (list.size() <= 0) {
                                getView().showEmptyView();
                            } else {
                                String json = list.get(0).getContent();
                                LogUtils.e("sen", json);
                                Gson gson = new Gson();
                                UserType userType = gson.fromJson(json, UserType.class);
                                getView().showContentView(userType.getUserType());
                            }
                        }
                    });


                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            getView().showErrorView();
                        }
                    });
                }
            }
        });

    }

    /**
     * @param userTypeBean
     */
    public void updateUserType(String thridId, UserType.UserTypeBean userTypeBean) {
        HashMap<String, Object> objectMap = new HashMap<>();
        DataType<String> idType = new DataType<String>(thridId) {
        };
        DataType<String> userTypeName = new DataType<String>(userTypeBean.getTypeName()) {
        };
        DataType<String> userTypeValue = new DataType<String>(userTypeBean.getType()) {
        };
        objectMap.put(Constant.UMSDK_COMSTOR_KEY_THRID_ID, idType);
        objectMap.put(Constant.UMSDK_COMSTOR_KEY_USER_TYPE, userTypeName);
        objectMap.put(Constant.UMSDK_COMSTOR_KEY_USER_TYPE_VALUE, userTypeValue);
        MServiceManager.getInstance().updateUser(objectMap, new OperationCallback<Void>(){
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                getView().updateUserInfoSuccess();
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                getView().updateUserInfoFailed();
            }
        });
    }


    /**
     * 将用户也绑定到bmob 数据库上
     * 然后将bmob 的也绑定到mob 上
     *
     * @param user
     */
    public void saveUserToBmob(final User user) {
        if (user == null) {
            return;
        }
        /**
         * 先查
         */
        BmobQuery<SUser> query = new BmobQuery<SUser>();
        query.addWhereEqualTo(Constant.BMOB_SUSER_ID, user.id.get());
        //执行查询方法
        query.findObjects(new FindListener<SUser>() {
            @Override
            public void done(List<SUser> list, BmobException e) {
                    if (list != null && list.size() > 0) {
                        if (!TextUtils.isEmpty(list.get(0).getUserId())) {
                            getView().setThridId(list.get(0).getObjectId());
                            LogUtils.e("sen", "2已经有了" + list.get(0).getObjectId());
                        } else {
                            LogUtils.e("sen", "3查不到");
                            saveUserBmob(user);
                        }
                }else{
                    LogUtils.e("sen", "14查不到");
                    saveUserBmob(user);
                }
            }
        });


    }

    /**
     * 在bmob 创建对应的user 同时创建一下默认的信息
     *
     * @param user
     */
    private void saveUserBmob(User user) {
        SUser sUser = new SUser();
        sUser.setUserId( user.id.get());
        sUser.setUserName(user.nickname.get());
        sUser.setImageUrl(Constant.test_user_image);

        sUser.save(new SaveListener<String>() {
            @Override
            public void done(final String s, final BmobException e) {
                LogUtils.e("sen", "4保存了" + s);
                if (!TextUtils.isEmpty(s)) {
                    getView().setThridId(s);
                }
            }
        });
    }

    /**
     * 登录用户账号
     *
     * @param
     * @param password
     */
    public void loadUserAccout(String phone, String password) {
        UMSSDK.loginWithPhoneNumber(Constant.COUNTRY_CODE, phone, password, new OperationCallback<User>() {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                Constant.CURRENT_USER = user;
                MServiceManager.getInstance().login(user.id.get(), user.nickname.get(), "");
//              然后，更新用户的信息
                getView().loadUserSuccess();

            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                getView().loadUserFaild();
            }
        });
    }
}
