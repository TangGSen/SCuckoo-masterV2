package com.ourcompany.manager;

import android.content.Context;
import android.text.TextUtils;

import com.mob.MobSDK;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.model.IMUser;
import com.mob.jimu.query.data.DataType;
import com.mob.jimu.query.data.Text;
import com.mob.ums.FriendRequest;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.im.biz.UserManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/30 21:58
 * Des    : 账号注册和登陆，聊天等的封装
 */

public class MServiceManager {
    private static final Executor EXECUTOR = Executors.newCachedThreadPool();
    private volatile static MServiceManager instance;

    private MServiceManager() {
    }

    public static MServiceManager getInstance() {
        if (instance == null) {
            synchronized (MServiceManager.class) {
                if (instance == null) {
                    instance = new MServiceManager();
                }
            }
        }
        return instance;
    }

    /**
     * 注册IM 的账号
     *
     * @param username
     * @param password
     */
    public int resigisterAccount(String username, String password) {
        return 0;
    }


    //注册成功需要登陆
    public void login(String userId, String username, String imagUrl) {
        //先做一些事情
        MobSDK.setUser(userId, username, imagUrl, null);
    }

    /**
     * 1.初始化 mob SDK
     */
    public void init(Context context) {
        MobSDK.init(context);
    }


    public boolean isUserLogin() {
        String userId = UMSSDK.getLoginUserId();
        if(TextUtils.isEmpty(userId)){
            return false;
        }else{
            return true;
        }
    }

    public void getUserInfos() {

    }

    private String getFriendsCachePath() {
        return new File(MobSDK.getContext().getCacheDir(), "friends").getPath();
    }

    /* 获取好友列表 */
    public synchronized void getFriends(final int start, final int count, final OperationCallback<ArrayList<User>> callback) {
        EXECUTOR.execute(new Runnable() {
            public void run() {
                UMSSDK.getFrinds(Constant.CURRENT_USER, start, count, callback);
            }
        });
    }

    /**
     * 回复好友请求
     */
    public void replyFriendsRequesting(String userId, boolean accept, OperationCallback callback) {
        UMSSDK.replyFriendRequesting(userId, accept, callback);

    }

    /**
     * 删除好友
     */
    public void deleteFriend(User target, String message, OperationCallback<Void> callback) {
        UMSSDK.deleteFriend(target, callback);
    }


    /**
     * 添加好友
     */
    public void addFriend(User target, String message, OperationCallback<Void> callback) {
        UMSSDK.addFriend(target, message, callback);
    }

    /**
     * 根据id 和手机号，昵称搜索User
     *
     * @param query
     * @param start
     * @param cound
     * @param callback
     */
    public void requestSearch(String query, int start, int cound, OperationCallback<ArrayList<User>> callback) {
        UMSSDK.search(query, start, cound, callback);
    }

    /**
     * 获取 好友关系
     */
    public void getFriendShip(Text[] ids, OperationCallback<Set<String>> callback) {
        UMSSDK.isMyFriends(ids, callback);
    }

    /**
     * 获取新朋友的id列表
     */
    public void getNewFriend(OperationCallback<ArrayList<String>> callback) {
        UMSSDK.getNewFriendsCount(callback);
    }

    /**
     * 获取
     */

    public void getAddFriendRequests(final int start, final int end, final OperationCallback<ArrayList<FriendRequest>> callback) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                UMSSDK.getAddFriendRequests(start, end, callback);
            }
        });

    }

    public void replyFriendRequesting(final String requesterId, final boolean isAccpet, final OperationCallback<Void> callback) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                UMSSDK.replyFriendRequesting(requesterId, isAccpet, callback);
            }
        });
    }

    public void saveImUserInfos(final String userId) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                MobIM.getUserManager().getUserInfo(userId, new MobIMCallback<IMUser>() {
                    @Override
                    public void onSuccess(IMUser user) {
                        UserManager.saveUserInfo(user);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        });
    }

    public void getUserInfoByUserId(final String mCurrentUserId, final OperationCallback<ArrayList<User>> callback) {
        final String[] ids = new String[]{mCurrentUserId};
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                UMSSDK.getUserListByIDs(ids,callback);
            }
        });
    }

    /**
     * 获取当前登陆的用户的userId
     * @return
     */
    public String getCurrentLoginUserId() {
        LogUtils.e("sen",UMSSDK.getLoginUserId());
        return UMSSDK.getLoginUserId();
    }

    /**
     * 修改用户的数据比如后台增加了第三方id 放在UMSDK中
     */

    public void saveThridPartyId(String id, OperationCallback<Void> callback) {
        HashMap<String, Object> objectMap = new HashMap<>();
        DataType<String> idType = new DataType<String>(id) {
        };
        DataType<Boolean> exist = new DataType<Boolean>(true) {
        };
        //objectMap.put(Constant.UMSDK_COMSTOR_KEY_THRID_ID, idType);
        objectMap.put(Constant.UMSDK_COMSTOR_KEY_EXIST_ID, exist);
        UMSSDK.updateUserInfo(objectMap, callback);
    }
    /************************************
     * 以下是Bomb Sdk
     */

    public SUser newBmobUser() {
        SUser user = new SUser();
        user.setUserName(Constant.CURRENT_USER.nickname.get());
        user.setObjectId(getLocalThirdPartyId());
        return null;
    }


    //设置本地第三方的id 绑定到user
    private void setLocalUserThridPartyId(String id) {
        if (Constant.CURRENT_USER != null) {
            DataType<Boolean> exist = new DataType<Boolean>(true) {
                @Override
                public Object value() {
                    return super.value();
                }
            };
            DataType<String> idstr = new DataType<String>(id) {
                @Override
                public Object value() {
                    return super.value();
                }
            };
            Constant.CURRENT_USER.setCustomField(Constant.UMSDK_COMSTOR_KEY_EXIST_ID, exist);
            Constant.CURRENT_USER.setCustomField(Constant.UMSDK_COMSTOR_KEY_THRID_ID, idstr);
        }
    }

    public String getLocalUserName() {
        if(Constant.CURRENT_USER!=null ){
            return Constant.CURRENT_USER.nickname.get();
        }
        return null;
    }

    /**
     * 获取登陆User的图片地址
     * @return
     */
    public String getLocalUserImage() {
        try {
            String url =  Constant.CURRENT_USER.avatar.get()[Constant.CURRENT_USER.avatar.get().length / 2];
            return url;
        }catch (Exception e){
        }
        return "";
    }

    /**
     * 获取第三方的id
     *
     * @return
     */

    public String getLocalThirdPartyId() {
        try {
            DataType<String> idType = (DataType<String>) Constant.CURRENT_USER.getCustomField(Constant.UMSDK_COMSTOR_KEY_THRID_ID);
            if (idType != null) {
                String id = (String) idType.value();
                return id;
            }
        }catch (Exception e){
            LogUtils.e("sen","getLocaTlhirdPartyId Exception: "+e.getMessage());
        }

        return "";

    }

    /**
     * 更新用户
     */
    public void updateUser(HashMap<String, Object> objectMap , OperationCallback<Void> callback ){

        UMSSDK.updateUserInfo(objectMap, callback);

    }


    /**
     * 用户的类型
     *
     * @return
     */
    public int getLoginUserType() {
        try {
            DataType<String> idType = (DataType<String>) Constant.CURRENT_USER.getCustomField(Constant.UMSDK_COMSTOR_KEY_USER_TYPE_VALUE);
            if (idType != null) {
                String type = (String) idType.value();
                return Integer.parseInt(type);
            }
        } catch (Exception e) {
            LogUtils.e("sen", "getLocaTlhirdPartyId Exception: " + e.getMessage());
        }

        return 0;

    }
/**
     * 用户的类型
     *
     * @return
     */
    public String getLoginUserTypeName() {
        try {
            DataType<String> idType = (DataType<String>) Constant.CURRENT_USER.getCustomField(Constant.UMSDK_COMSTOR_KEY_USER_TYPE);
            if (idType != null) {
                String type = (String) idType.value();
                return type;
            }
        } catch (Exception e) {
            LogUtils.e("sen", "getLocaTlhirdPartyId Exception: " + e.getMessage());
        }

        return "暂无";

    }

    /**
     * 保存第三方的id
     * @param id
     */

    public void saveThirdPartyImp(final String id) {
        saveThridPartyId(id, new OperationCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                setLocalUserThridPartyId(id);
                LogUtils.e("sen", "saveThridPartyId ok");
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                LogUtils.e("sen", "saveThridPartyId" + throwable.getLocalizedMessage() + throwable.getMessage());
            }
        });
    }

    public void findObjectIdFromSUser(String userId) {
        BmobQuery<SUser> query = new BmobQuery<SUser>();
        query.getObject(userId, new QueryListener<SUser>() {
            @Override
            public void done(SUser object, BmobException e) {
                if (e == null) {
                    //获得数据的objectId信息
                    LogUtils.e("sen", "找到了在存");
                    saveImUserInfos(object.getObjectId());
                } else {

                }
            }

        });
    }


    public boolean getUserIsLogin() {
        return UMSSDK.amILogin();
    }
}
