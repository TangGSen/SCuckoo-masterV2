package com.ourcompany.presenter.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.ourcompany.R;
import com.ourcompany.bean.CompressImage;
import com.ourcompany.bean.json.UserType;
import com.ourcompany.bean.bmob.AppSettingJson;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.EditextUserInfoActView;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import company.com.commons.framework.presenter.MvpBasePresenter;

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

public class EditextActPresenter extends MvpBasePresenter<EditextUserInfoActView> {

    private BmobDate bmobDate;
    private String mCurrentUserUrl;

    public EditextActPresenter(Context context) {
        super(context);
    }

    public void dealUserImage(String itemSrcPath) {
        Observable.just(itemSrcPath).flatMap(new Func1<String, Observable<CompressImage>>() {
            @Override
            public Observable<CompressImage> call(String itemSrcPath) {
                //图片选择器的imageItem 有时是文件的全路径，这样就没有问题了
                File file = new File(itemSrcPath);
                String path = Constant.CACHE_DIR + file.getName();
                File cacheFile = new File(path);
                CompressImage image = new CompressImage(path, itemSrcPath);
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
                uploadImage(mCurrentUserUrl);

            }

            @Override
            public void onError(Throwable throwable) {

                showUploadError(ResourceUtils.getString(R.string.str_deal_image_error));

            }

            @Override
            public void onNext(CompressImage compressImage) {
                mCurrentUserUrl = compressImage.getCachePaht();
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

    private void showUploadError(final String errormsg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().dealImageError(errormsg);
            }
        });
    }


    private void uploadImage(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        LogUtils.e("sen", "path:" + path);
        UMSSDK.uploadAvatar(path, new OperationCallback<HashMap<String, Object>>() {
            @Override
            public void onSuccess(HashMap<String, Object> res) {
                super.onSuccess(res);
                if (res.size() > 0) {
                    // String url = (String) res.get(Constant.CURRENT_USER.avatar.getName());
                    String photoId = (String) res.get("id");
                    Object urls = res.get("avatar");
                    if (urls != null) {
                        String[] photoUrl = null;
                        if (urls instanceof ArrayList) {
                            ArrayList<String> list = (ArrayList) urls;
                            photoUrl = (String[]) list.toArray(new String[list.size()]);
                        } else if (urls instanceof String) {
                            photoUrl = new String[]{(String) urls};
                        }
                        HashMap<String, Object> imageRes = new HashMap<>();
                        imageRes.put(Constant.CURRENT_USER.avatarId.getName(), photoId);
                        imageRes.put(Constant.CURRENT_USER.avatar.getName(), photoUrl);
                        getView().showUploadSuccess(imageRes, path);


                    } else {
                        getView().showUploadSuccess(res, path);
                    }

                }
            }


            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                getView().showUploadFailed();
            }
        });
    }

    public void getIdentityName() {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                BmobQuery<AppSettingJson> query = new BmobQuery<AppSettingJson>();
                query.addWhereEqualTo(Constant.KEY_BMOB_APP_SETTING,Constant.KEY_BMOB_USERTYPE);
                query.setLimit(1);
                //执行查询方法
                query.findObjects(new FindListener<AppSettingJson>() {
                    @Override
                    public void done(final List<AppSettingJson> list, BmobException e) {
                        if (e == null) {
                            if (list.size() <= 0) {
                                getEmptyUserType();
                            } else {
                                String json = list.get(0).getContent();
                                Gson gson = new Gson();
                                final UserType userType = gson.fromJson(json, UserType.class);
                                if (userType != null) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            getView().showUserTypeView(userType.getUserType());
                                        }
                                    });
                                } else {
                                    getEmptyUserType();
                                }

                            }

                        } else {
                            getEmptyUserType();
                        }
                    }
                });
            }
        });


    }

    private void getEmptyUserType() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().showErrorUseTypeView();
            }
        });
    }

    /**
     * 提交信息
     *
     * @param objectMap
     */
    public void sumbmitInfo(final HashMap<String, Object> objectMap) {
        MServiceManager.getInstance().updateUser(objectMap, new OperationCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                Constant.CURRENT_USER.parseFromMap(objectMap);
                getView().updateInfoSuccess();
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                LogUtils.e("sen", throwable.getMessage() + "**" + throwable.getLocalizedMessage());
                getView().updateInfoFaild();
            }
        });
    }

    public void updateImageForBmobUser() {
        //更新用户的信息
        SUser mUser = new SUser();
        mUser.setObjectId(MServiceManager.getInstance().getLocalThirdPartyId());
        mUser.setUserId(MServiceManager.getInstance().getCurrentLoginUserId());
        mUser.setImageUrl(MServiceManager.getInstance().getLocalUserImage());
        mUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                getView().updateAllInfoFinish();
            }
        });

    }
}
