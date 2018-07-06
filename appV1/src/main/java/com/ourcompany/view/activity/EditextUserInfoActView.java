package com.ourcompany.view.activity;

import com.ourcompany.bean.json.UserType;

import java.util.HashMap;
import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface EditextUserInfoActView extends MvpView {
    void showUploadSuccess(HashMap<String, Object> res, String path);

    void showUploadFailed();

    void showUserTypeView(List<UserType.UserTypeBean> userType);

    void showErrorUseTypeView();

    void updateInfoFaild();

    void updateInfoSuccess();

    /**
     * 更新全部信息完成
     */
    void updateAllInfoFinish();

    /**
     * 处理图片出错
     *
     * @param errormsg
     */
    void dealImageError(String errormsg);
}
