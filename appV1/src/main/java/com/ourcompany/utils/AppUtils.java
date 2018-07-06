package com.ourcompany.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ourcompany.app.MApplication;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/20 15:22
 * Des    :
 */

public class AppUtils {
    /**
     * @return 当前应用的版本号
     */
    public static int getLocalVersion() {
        try {
            PackageManager manager = MApplication.mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(MApplication.mContext.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static class ApkInfo {
        private String packgeName;
        private int versionCode;
        private String versionName;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getPackgeName() {
            return packgeName;
        }

        public void setPackgeName(String packgeName) {
            this.packgeName = packgeName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }
    }

    /**
     * 通过APK地址获取此APP的包名和版本等信息
     */
    public static ApkInfo getApkPackageName(String path) {
        ApkInfo apkInfo = null;
        try {
            PackageManager pm = MApplication.mContext.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                apkInfo = new  ApkInfo();
                apkInfo.setPackgeName(appInfo.packageName);
                apkInfo.setVersionCode(info.versionCode);
            }
        } catch (Exception e) {

        }

        return apkInfo;
    }

    /**
     * @return 当前应用的版本号
     */
    public static ApkInfo getLocalApkInfo() {
        ApkInfo apkInfo = null;
        try {
            PackageManager manager = MApplication.mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(MApplication.mContext.getPackageName(), 0);
            apkInfo = new ApkInfo();
            apkInfo.setPackgeName(info.packageName);
            apkInfo.setVersionCode(info.versionCode);
            apkInfo.setVersionName(info.versionName);
        } catch (Exception e) {
        }
        return apkInfo;
    }
}
