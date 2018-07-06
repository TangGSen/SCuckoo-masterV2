package com.ourcompany.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.bean.bmob.SAppVersion;
import com.ourcompany.widget.drawable.UpdateBgDrawable;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/19 10:02
 * Des    :
 */

public class AppVersionUpdate {
    private AppVersionUpdateListener listener;
    private boolean isNotNeedUpdate;
    private PopupWindow popupWindow;
    private String mPath;
    private String mFilePath;
    private ProgressBar mProgressBar;
    private TextView tvStates;
    private TextView tvProgress;
    private AlertDialog downloadDialog;
    private MDownloadlistener mDownloadlistener;
    private boolean isAutoInstall;
    private AlertDialog checkApkInfoDialog;

    public interface AppVersionUpdateListener {
        /**
         * 检查更新中
         */
        void onAppCheckVersioning();

        /**
         * 检查更新出错
         */
        void onCheckVersionError();

        /**
         * 需要更新
         *
         * @param version
         */
        void onCheckVersionSuccess(SAppVersion version);

        /**
         * 不需要更新
         */
        void onVersionNotUpdate();

        /**
         * 下载apk完成
         *
         * @param path
         */

        void onDownloadApkSuccess(String path);
    }

    public void setAppVersionUpdateListener(AppVersionUpdateListener listener) {
        this.listener = listener;
    }

    private volatile static AppVersionUpdate instance;

    private AppVersionUpdate() {
    }

    public static AppVersionUpdate getInstance() {
        if (instance == null) {
            synchronized (AppVersionUpdate.class) {
                if (instance == null) {
                    instance = new AppVersionUpdate();
                }
            }
        }
        return instance;
    }

    public void reset() {
        this.isNotNeedUpdate = false;
    }


    /**
     * 检查更新
     */
    public void update() {
        if (isNotNeedUpdate) {
            if (listener != null) {
                listener.onVersionNotUpdate();
                LogUtils.e("sen", "不需要更新");
            }
            return;
        }
        if (listener != null) {
            listener.onAppCheckVersioning();
        }
        BmobQuery<SAppVersion> query = new BmobQuery<SAppVersion>();
        //查询最新修改的那条
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_UPDATEAT);
        query.setLimit(1);
        query.findObjects(new FindListener<SAppVersion>() {
            @Override
            public void done(final List<SAppVersion> list, BmobException e) {
                if (e == null) {
                    getAppversionList(list);
                } else {
                    checkVersionError();
                }
            }


        });
    }

    private void getAppversionList(List<SAppVersion> list) {
        if (list != null && list.size() > 0) {
            //然后对比版本
            SAppVersion appVersion = list.get(0);
            if (appVersion != null) {
                if (AppUtils.getLocalVersion() < appVersion.getVersionCode()) {
                    if (listener != null) {
                        listener.onCheckVersionSuccess(list.get(0));
                    }
                } else {
                    //不需要更新
                    if (listener != null) {
                        isNotNeedUpdate = true;
                        listener.onVersionNotUpdate();
                    }
                }
            } else {
                checkVersionError();
            }
        } else {
            checkVersionError();
        }
    }

    public void onActivityStop() {
        //不自动安装
        isAutoInstall = false;
    }

    /**
     * 显示检查apk 和安装
     *
     * @param activity
     */
    public void showInstallDialog(final Activity activity, final String path) {
        if (activity == null || TextUtils.isEmpty(path)) {
            return;
        }
        //弹出选框
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(ResourceUtils.getString(R.string.str_check_apk_info));
        View root = View.inflate(activity, R.layout.layout_update_check_apkinfo, null);
        final TextView tvStates = root.findViewById(R.id.tvStates);
        final ImageView animView = root.findViewById(R.id.imageLoadView);
        Animation circle_anim = AnimationUtils.loadAnimation(activity, R.anim.anim_loading_imag);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            animView.startAnimation(circle_anim);  //开始动画
        }

        builder.setView(root);
        builder.setPositiveButton(" ", null);
        builder.setCancelable(false);
        checkApkInfoDialog = builder.show();
        checkApkInfoDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btText = checkApkInfoDialog.getButton(DialogInterface.BUTTON_POSITIVE).getText().toString();
                if (btText.equals(ResourceUtils.getString(R.string.str_download_install))) {
                    installApk(activity,path);
                } else if (btText.equals(ResourceUtils.getString(R.string.str_close))) {
                    checkApkInfoDialog.dismiss();
                }
            }
        });
        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                animView.clearAnimation();
                if (!new File(path).exists()) {
                    checkApkInfoDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(ResourceUtils.getString(R.string.str_close));
                    tvStates.setText(ResourceUtils.getString(R.string.str_download_is_deleted));
                    return;
                }
                AppUtils.ApkInfo local = AppUtils.getLocalApkInfo();
                AppUtils.ApkInfo pathInfo = AppUtils.getApkPackageName(path);
                if (local != null && pathInfo != null &&
                        !TextUtils.isEmpty(local.getPackgeName()) &&
                        !TextUtils.isEmpty(pathInfo.getPackgeName())) {
                    if (local.getPackgeName().equals(pathInfo.getPackgeName())) {
                        if (local.getVersionCode() < pathInfo.getVersionCode()) {
                            checkApkInfoDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(ResourceUtils.getString(R.string.str_download_install));
                            tvStates.setText(ResourceUtils.getString(R.string.str_download_apk_is_safy));
                        } else {
                            checkApkInfoDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(ResourceUtils.getString(R.string.str_close));
                            tvStates.setText(ResourceUtils.getString(R.string.str_download_apk_is_old));
                        }
                    } else {
                        checkApkInfoDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(ResourceUtils.getString(R.string.str_close));
                        tvStates.setText(ResourceUtils.getString(R.string.str_download_apk_not_belong));

                    }
                }else{
                    checkApkInfoDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(ResourceUtils.getString(R.string.str_close));
                    tvStates.setText(ResourceUtils.getString(R.string.str_download_apk_is_error));
                }
            }
        }, 1200);
    }

    /**
     * 安装apk
     * @param activity
     * @param path
     */
    private void installApk(Activity activity, String path) {
        if (activity == null || TextUtils.isEmpty(path)) {
            ToastUtils.showSimpleToast(ResourceUtils.getString(R.string.str_install_error));
            return;
        }
        File file = new File(path);
        if(!file.exists()){
            ToastUtils.showSimpleToast(ResourceUtils.getString(R.string.str_download_is_deleted));
            if(checkApkInfoDialog!=null && checkApkInfoDialog.isShowing()){
                checkApkInfoDialog.dismiss();
            }
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            Uri apkUri = FileProvider.getUriForFile(activity, Constant.URI_FOR_FILE, file);  //包名.fileprovider
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);

    }

    /**
     * 显示下载进度
     */
    private void showDownloadDialog(Activity activity) {

        if (activity == null) {
            return;
        }
        //弹出选框
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(ResourceUtils.getString(R.string.str_update_progress_title));
        View root = View.inflate(activity, R.layout.layout_update_version_progress, null);
        mProgressBar = root.findViewById(R.id.progressBar);
        mProgressBar = root.findViewById(R.id.progressBar);
        tvStates = root.findViewById(R.id.tvStates);
        tvProgress = root.findViewById(R.id.tvProgress);
        builder.setView(root);
        builder.setPositiveButton(" ", null);
        builder.setCancelable(false);
        downloadDialog = builder.show();
        downloadDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btText = downloadDialog.getButton(DialogInterface.BUTTON_POSITIVE).getText().toString();
                if (btText.equals(ResourceUtils.getString(R.string.str_download_redownload))) {
                    downloadDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(" ");
                    startDownload();
                }
            }
        });
        downloadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                DownloadUtils.getInstance().stop();
            }
        });
    }

    /**
     * 显示更新的对话框
     */
    public void showUpdateDialog(final Activity activity, final SAppVersion version) {
        if (activity == null || version == null) {
            return;
        }
        //弹出选框
        popupWindow = new PopupWindow(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_update_popuwindowns, null);
        ImageView btClose = view.findViewById(R.id.btClose);
        Button btUpdate = view.findViewById(R.id.btUpdate);
        final TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvContent = view.findViewById(R.id.tvContent);
        tvTitle.setText(ResourceUtils.getString(R.string.str_update_title));
        tvContent.setText(version.getUpdateContent().replace("\\n", "\n"));
        btUpdate.setText(ResourceUtils.getString(R.string.str_update_button));

        view.post(new Runnable() {
            @Override
            public void run() {
                UpdateBgDrawable updateBgDrawable = new UpdateBgDrawable(tvTitle.getWidth(), tvTitle.getHeight());
                tvTitle.setBackground(updateBgDrawable);
            }
        });
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDownloadDialog(activity);
                mFilePath = activity.getExternalCacheDir().getAbsolutePath()+Constant.APK_DIR+File.separator;
                mPath = version.getFileUrl();
                startDownload();
                popupWindow.dismiss();
            }
        });
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.bg_pupowidowns));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(((MvpActivity) activity).getRootView(), Gravity.BOTTOM, 0, 0);
    }

    class MDownloadlistener implements DownloadUtils.OnDownloadListener {

        @Override
        public void onDownloadSuccess(String path) {
            if (isDownloadDialog()) {
                downloadDialog.dismiss();
            }
            if (listener != null) {
                listener.onDownloadApkSuccess(path);
            }

        }

        @Override
        public void onDownloading(int progress, long current, long total) {
            if (tvProgress != null && mProgressBar != null && isDownloadDialog()) {
                tvProgress.setText(StringUtils.getFormatFileSize(current) + "/" + StringUtils.getFormatFileSize(total));
                mProgressBar.setProgress(progress);
            }
        }

        @Override
        public void onDownloadFailed() {
            if (tvStates != null && isDownloadDialog()) {
                tvStates.setText(ResourceUtils.getString(R.string.str_download_fail));
                downloadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(ResourceUtils.getString(R.string.str_download_redownload));
            }
        }

        @Override
        public void onDownloadStop() {
            if (tvStates != null && isDownloadDialog()) {
                tvStates.setText(ResourceUtils.getString(R.string.str_download_stop));
            }
        }
    }

    public void startDownload() {

        if (mDownloadlistener == null) {
            mDownloadlistener = new MDownloadlistener();
        }
        DownloadUtils.getInstance().download(mPath, mFilePath, mDownloadlistener);
    }

    /**
     * 判断是否还显示
     *
     * @return
     */
    public boolean isDownloadDialog() {
        return downloadDialog != null && downloadDialog.isShowing();
    }

    /**
     * 检查版本出错
     */

    private void checkVersionError() {
        if (listener != null) {
            listener.onCheckVersionError();
        }
    }


}
