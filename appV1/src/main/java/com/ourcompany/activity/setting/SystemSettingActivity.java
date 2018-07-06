package com.ourcompany.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.aop.annotation.CheckIsLogin;
import com.ourcompany.bean.bmob.SAppVersion;
import com.ourcompany.bean.eventbus.UserLogout;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.presenter.activity.SystemSettingActPresenter;
import com.ourcompany.utils.AppVersionUpdate;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.SystemSettingActView;
import com.ourcompany.widget.LoadingViewAOV;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.util.Utils;

public class SystemSettingActivity extends MvpActivity<SystemSettingActView, SystemSettingActPresenter> implements SystemSettingActView {

    @BindView(R.id.btEditexInfo)
    TextView btEditexInfo;
    @BindView(R.id.btAccountSetting)
    TextView btAccountSetting;
    @BindView(R.id.btAccpetPush)
    TextView btAccpetPush;
    @BindView(R.id.swith_push)
    Switch swithPush;
    @BindView(R.id.btCurrentVersion)
    TextView btCurrentVersion;
    @BindView(R.id.btCheckNewVersion)
    TextView btCheckNewVersion;
    @BindView(R.id.btLogout)
    TextView btLogout;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    private MAppVersionUpdateListener mAppVersionListener;

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, SystemSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_systemsetting;
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setTitle(ResourceUtils.getString(R.string.str_system_setting));
        commonToolbar.setTitleTextColor(ResourceUtils.getResColor(R.color.colorFrist));
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        if(!MServiceManager.getInstance().getUserIsLogin()){
           btLogout.setVisibility(View.GONE);
        }


    }

    @Override
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, SystemSettingActivity.this);

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //延时获取版本号
        getPresenter().getLocalVersionName();
    }

    @Override
    protected SystemSettingActView bindView() {
        return this;
    }

    @Override
    protected SystemSettingActPresenter bindPresenter() {
        return new SystemSettingActPresenter(this);
    }


    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }


    @OnClick({R.id.btEditexInfo, R.id.btAccountSetting, R.id.btCheckNewVersion, R.id.btLogout})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btEditexInfo:
                checkGotoEditextUserInfo();
                break;
            case R.id.btAccountSetting:
                checkGotoAccountSetting();
                break;
            case R.id.btAccpetPush:
                break;
            case R.id.btCheckNewVersion:
                if(mAppVersionListener==null){
                    mAppVersionListener = new MAppVersionUpdateListener();
                    AppVersionUpdate.getInstance().setAppVersionUpdateListener(mAppVersionListener);
                }
                AppVersionUpdate.getInstance().update();
                break;
            case R.id.btLogout:
                LoadingViewAOV.getInstance().with(SystemSettingActivity.this,btLogout,R.color.whiles,R.drawable.ic_loading_v4,Gravity.CENTER);
                getPresenter().logout();
                break;
        }
    }
    @CheckIsLogin
    private void checkGotoEditextUserInfo() {
        EditextUserInfoActivity.gotoThis(SystemSettingActivity.this);
    }

    @CheckIsLogin
    private void checkGotoAccountSetting() {
        AccountSettingAcitivity.gotoThis(SystemSettingActivity.this);
    }


    class MAppVersionUpdateListener implements AppVersionUpdate.AppVersionUpdateListener{

        @Override
        public void onAppCheckVersioning() {
            btCheckNewVersion.setEnabled(false);
            LoadingViewAOV.getInstance().with(SystemSettingActivity.this,btCurrentVersion,R.color.whiles,R.drawable.ic_loading_v4, Gravity.RIGHT);
        }

        @Override
        public void onCheckVersionError() {
            updateFinish();
            showToastMsg(ResourceUtils.getString(R.string.str_app_version_get_error));
        }

        @Override
        public void onCheckVersionSuccess(SAppVersion version) {
            updateFinish();
            AppVersionUpdate.getInstance().showUpdateDialog(SystemSettingActivity.this,version);
        }

        @Override
        public void onVersionNotUpdate() {
            updateFinish();
            showToastMsg(ResourceUtils.getString(R.string.str_app_version_is_new));
        }

        @Override
        public void onDownloadApkSuccess(String path) {
            AppVersionUpdate.getInstance().showInstallDialog(SystemSettingActivity.this,path);
        }
    }

    /**
     * 更新完成
      */
    private void updateFinish(){
        btCheckNewVersion.setEnabled(true);
        LoadingViewAOV.getInstance().close(SystemSettingActivity.this,btCurrentVersion);
    }

    @Override
    protected void onDestroy() {
        AppVersionUpdate.getInstance().reset();
        super.onDestroy();
    }

    @Override
    public void setLocalVersionName(String localVersion) {
        btCurrentVersion.setText(localVersion);
    }

    @Override
    public void logoutSuccess() {
        LoadingViewAOV.getInstance().close(SystemSettingActivity.this,btLogout);
        EventBus.getDefault().post(new UserLogout(true));
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void logoutFail() {
        LoadingViewAOV.getInstance().close(SystemSettingActivity.this,btLogout);
        showToastMsg(ResourceUtils.getString(R.string.str_logout_fail));
    }
}
