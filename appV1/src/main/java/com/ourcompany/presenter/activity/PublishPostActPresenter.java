package com.ourcompany.presenter.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.ourcompany.R;
import com.ourcompany.bean.CompressImage;
import com.ourcompany.bean.UserTypeLocal;
import com.ourcompany.bean.bmob.Post;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.bean.bmob.UploadType;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.PublishPostActView;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import company.com.commons.framework.presenter.MvpBasePresenter;
import imagepicker.ImagePicker;
import imagepicker.bean.ImageItem;


import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class PublishPostActPresenter extends MvpBasePresenter<PublishPostActView> {

    private UpdateProgressRunable updateProgressRunable;

    public PublishPostActPresenter(Context context) {
        super(context);
    }

    private List<String> imageList = new ArrayList<>();
    private String inputText = "";

    public void checkAllWorkflow(String text) {
        //1.先检查用户有没登录
        if (Constant.CURRENT_USER == null || TextUtils.isEmpty(Constant.CURRENT_USER.id.get())) {
            getView().showToastMsg(ResourceUtils.getString(R.string.str_user_not_login));
            getView().verifySubmitError();
            return;
        }
        //2.检查有没合法的填入信息
        if (TextUtils.isEmpty(text) && ImagePicker.getInstance().getSelectImageCount() <= 0) {
            getView().showToastMsg(ResourceUtils.getString(R.string.str_text_image_null));
            getView().verifySubmitError();
            return;
        }
        //3.上传的类型
        //(1.仅有文字的
        //需要提示

        this.inputText = text;
        if (ImagePicker.getInstance().getSelectImageCount() <= 0 && !TextUtils.isEmpty(text)) {
            uploadPost(null, UploadType.Text);
            getView().verifySubmitError();
            return;
        }
        //（2.有图片的，先处理图片
        imageList.clear();
        //显示处理进度
        showProgress(1, 1, ImagePicker.getInstance().getSelectImageCount(), 0);
        //压缩图片
        Observable.from(ImagePicker.getInstance().getSelectedImages()).
                flatMap(new Func1<ImageItem, Observable<CompressImage>>() {
                    @Override
                    public Observable<CompressImage> call(ImageItem imageItem) {
                        //图片选择器的imageItem 有时是文件的全路径，这样就没有问题了
                        File file = new File(imageItem.getPath());
                        String path = Constant.CACHE_DIR + file.getName();
                        File cacheFile = new File(path);
                        CompressImage image = new CompressImage(path, imageItem.getPath());
                        if (cacheFile.exists()) {
                            image.setExist(true);
                        } else {
                            image.setExist(false);
                        }
                        return Observable.just(image);
                    }
                }).observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<CompressImage>() {
            @Override
            public void onCompleted() {
                uploadFile();

            }

            @Override
            public void onError(Throwable throwable) {

                showUploadError(ResourceUtils.getString(R.string.str_deal_image_error));

            }

            @Override
            public void onNext(CompressImage compressImage) {
                imageList.add(compressImage.getCachePaht());
                if (compressImage.isExist()) {
                    return;
                }
                InputStream is = null;
                try {
                    is = new FileInputStream(compressImage.getSrcPaht());
                    //2.为位图设置100K的缓存
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inTempStorage = new byte[100 * 1024];
                    opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
                    opts.inPurgeable = true;
                    //5.设置位图缩放比例
                    //width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
                    opts.inSampleSize = 2;
                    //6.设置解码位图的尺寸信息
                    opts.inInputShareable = true;
                    //7.解码位图
                    Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
                    NativeUtil.compressBitmap(bitmap, 80,
                            compressImage.getCachePaht(), true);
                    is.close();
                } catch (FileNotFoundException e) {
                    LogUtils.e("sen", " FileNotFoundException call");
                } catch (IOException e) {
                    LogUtils.e("sen", " IOException call");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 批量上传文件
     */
    private void uploadFile() {
        //这里还是Rxjava 定义的线程
        if (imageList.size() <= 0) {
            return;
        }
        final String[] filePaths = imageList.toArray(new String[imageList.size()]);

        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, final List<String> urls) {
                //Thread Main
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    if (TextUtils.isEmpty(inputText)) {
                        uploadPost(urls, UploadType.Images);
                    } else {
                        uploadPost(urls, UploadType.ImagesAndText);
                    }

                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                showUploadError(errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                showProgress(curIndex, curPercent, total, totalPercent);
            }
        });
    }

    /**
     * 显示进度
     *
     * @param curIndex
     * @param curPercent
     * @param total
     * @param totalPercent
     */
    private void showProgress(int curIndex, int curPercent, int total, int totalPercent) {
        if (updateProgressRunable == null) {
            updateProgressRunable = new UpdateProgressRunable();

        }
        updateProgressRunable.setData(curIndex, curPercent, total, totalPercent);
        mHandler.post(updateProgressRunable);
    }

    public class UpdateProgressRunable implements Runnable {
        int curIndex;
        int curPercent;
        int total;
        int totalPercent;

        @Override
        public void run() {
            getView().uploadImageProgress(
                    ResourceUtils.getString(R.string.upload_image),
                     "("+curIndex+"/"+ total+")  "+ totalPercent+"%");
        }

        public void setData(int curIndex, int curPercent, int total, int totalPercent) {
            this.curIndex = curIndex;
            if (totalPercent == 100) {
                //这是为了传完照片还得上传文字等信息
                totalPercent = 99;
            }
            this.totalPercent = totalPercent;
            this.total = total;
        }
    }

    private void showUploadError(final String errormsg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().dealImageError(errormsg);
            }
        });
    }


    /**
     * 上传图文类型
     *
     * @param urls
     * @param type
     */
    public void uploadPost(final List<String> urls, final UploadType type) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                //do something
                if (Constant.CURRENT_USER == null || TextUtils.isEmpty(Constant.CURRENT_USER.id.get())) {
                    getView().showToastMsg(ResourceUtils.getString(R.string.str_user_not_login_pulish));
                    return;
                }
                final Post newPost = new Post();
                //设置Post 是否需要投票
                if (getIsPostNeedVote()) {
                    newPost.setNeedVote(Boolean.TRUE);
                    newPost.setmPostVoteDeadline(Boolean.FALSE);
                }
                newPost.setUserId(Constant.CURRENT_USER.id.get());
                SUser user = new SUser();
                user.setObjectId(MServiceManager.getInstance().getLocalThirdPartyId());
                user.setImageUrl(MServiceManager.getInstance().getLocalUserImage());
                user.setUserName(MServiceManager.getInstance().getLocalUserName());
                newPost.setUser(user);
                switch (type) {
                    case Text:
                        newPost.setContent(inputText);
                        newPost.setPostType(Constant.POST_TEXT);
                        break;
                    case Images:
                        newPost.setImageUrls(urls);
                        newPost.setPostType(Constant.POST_IMAGES);
                        break;
                    case ImagesAndText:
                        newPost.setPostType(Constant.POST_TEXT);
                        newPost.setContent(inputText);
                        newPost.setImageUrls(urls);
                        break;
                }
                newPost.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, final BmobException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (e == null) {
                                    getView().uploadSuccess(newPost);
                                } else {
                                    getView().dealImageError(ResourceUtils.getString(R.string.str_send_fial));
                                }
                            }
                        });

                    }

                });
            }
        });
    }

    /**
     * 判断是否需要投票，目前就是只有业主的身份需要投票
     *
     * @return
     */
    private boolean getIsPostNeedVote() {
       if( MServiceManager.getInstance().getLoginUserType()== UserTypeLocal.Owner.getValue()){
           return true;
       }
        return false;
    }
}
