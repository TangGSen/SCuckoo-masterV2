package com.ourcompany.manager;

import com.mob.MobSDK;
import com.mob.imsdk.model.IMUser;
import com.mob.tools.utils.ResHelper;

import java.io.File;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/27 11:21
 * Des    : IM系统的用户管理
 */

public class IMUserManager {
    private static IMUser user = null;

    public static IMUser getUser() {
        if (user == null) {
            user = getCachedUser();
        }
        return user;
    }

    private static void saveUserInfo(IMUser imUser) {
        final String cacheFilePath = new File(MobSDK.getContext().getCacheDir(), "user").getPath();
        ResHelper.saveObjectToFile(cacheFilePath, imUser);
    }

    public synchronized static IMUser getCachedUser() {
        final String cacheFilePath = new File(MobSDK.getContext().getCacheDir(), "user").getPath();
        //从缓存中取用户
        final IMUser imUser = (IMUser) ResHelper.readObjectFromFile(cacheFilePath);
        return imUser;
    }


}
