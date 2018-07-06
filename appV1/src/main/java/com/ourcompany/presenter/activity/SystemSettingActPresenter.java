package com.ourcompany.presenter.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.SystemSettingActView;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class SystemSettingActPresenter extends MvpBasePresenter<SystemSettingActView> {
    protected int  logoutCount = 0;
    public SystemSettingActPresenter(Context context) {
        super(context);
    }


    public void getLocalVersionName() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String localVersion = "";
                try {
                    PackageInfo packageInfo = MApplication.mContext
                            .getPackageManager()
                            .getPackageInfo(MApplication.mContext.getPackageName(), 0);
                    localVersion = String.format(ResourceUtils.getString(R.string.str_current_version), packageInfo.versionName);
                    getView().setLocalVersionName(localVersion);
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
        }, 1000);

    }




    public void logout() {
        UMSSDK.logout(new OperationCallback<Void>(){
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                showLogoutSuccess();
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                if(logoutCount==1){
                    showLogoutSuccess();
                }else{
                    getView().logoutFail();
                }
                logoutCount++;
            }
        });


    }

    public void showLogoutSuccess(){
        Constant.CURRENT_USER=null;
        logoutCount = 0;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().logoutSuccess();
            }
        },1500);
    }
}
