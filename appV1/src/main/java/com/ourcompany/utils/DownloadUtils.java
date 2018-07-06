package com.ourcompany.utils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/19 17:34
 * Des    :
 */


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DownloadUtils {
    public static final int DOWNLOAD_FAIL = 0;
    public static final int DOWNLOAD_PROGRESS = 1;
    public static final int DOWNLOAD_SUCCESS = 2;
    private static final int DOWNLOAD_STOP = 3;
    private static DownloadUtils downloadUtil;
    private final OkHttpClient okHttpClient;
    private long total;
    private long current = 0;
    private volatile boolean isStop;
    private volatile boolean isDownload;


    public static DownloadUtils getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtils();
        }
        return downloadUtil;
    }

    private DownloadUtils() {
        okHttpClient = new OkHttpClient();
    }

    public void stop() {
        isStop = true;
        isDownload = false;
    }

    /**
     *
     */
    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        if (isDownload) {
            LogUtils.e("sen","已经在下了");
            return;
        }

        isStop = false;
        isDownload = true;
        current =0;
        total=0;
        this.listener = listener;
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = DOWNLOAD_FAIL;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                //储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    if (file.exists()) {
                        LogUtils.e("sen", "文件是存在的0");
                        file.delete();
                        if (file.exists()) {
                            LogUtils.e("sen", "文件还没删除1");
                        } else {
                            LogUtils.e("sen", "文件被删了2");
                        }
                    } else {
                        LogUtils.e("sen", "文件是不存在的3");
                    }
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        if (isStop) {
                            LogUtils.e("sen", "正在退出");
                            break;
                        }
                        fos.write(buf, 0, len);
                        current += len;
                        int progress = (int) (current * 1.0f / total * 100);
                        //下载中
                        Message message = Message.obtain();
                        message.what = DOWNLOAD_PROGRESS;
                        message.obj = progress;
                        mHandler.sendMessage(message);

                    }

                    fos.flush();
                    //下载完成
                    if(isStop){
                        Message message = Message.obtain();
                        message.what = DOWNLOAD_STOP;
                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = DOWNLOAD_SUCCESS;
                        message.obj = file.getAbsolutePath();
                        mHandler.sendMessage(message);
                    }

                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = DOWNLOAD_FAIL;
                    mHandler.sendMessage(message);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {

                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }
                }
            }
        });
    }

    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    private String isExistDir(String saveDir) throws IOException {
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }


    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (listener == null) {
                return;
            }
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD_PROGRESS:
                    listener.onDownloading((Integer) msg.obj, current, total);
                    break;
                case DOWNLOAD_FAIL:
                    isDownload = false;
                    listener.onDownloadFailed();
                    break;
                case DOWNLOAD_SUCCESS:
                    isDownload = false;
                    listener.onDownloadSuccess((String) msg.obj);
                    break;
                case DOWNLOAD_STOP:
                    isDownload = false;
                    listener.onDownloadStop();
                    break;
            }
        }
    };


    OnDownloadListener listener;

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path);

        /**
         * 下载进度
         *
         * @param progress
         */
        void onDownloading(int progress, long current, long total);

        /**
         * 下载失败
         */
        void onDownloadFailed();

        /**
         * 停止下载
         */
        void onDownloadStop();
    }
}
