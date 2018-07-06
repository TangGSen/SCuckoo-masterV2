package com.ourcompany.view.activity;

import com.ourcompany.bean.bmob.Post;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface PublishPostActView extends MvpView {
    void uploadSuccess(Post newPost);


    /**
     * 压缩图片，上传图片等操作的进度
     *
     * @param tip
     * @param progress
     */
    void uploadImageProgress(String tip, String progress);

    /**
     * rxjava 处理图片是出错
     *
     * @param errormsg
     */

    void dealImageError(String errormsg);

    void verifySubmitError();
}
