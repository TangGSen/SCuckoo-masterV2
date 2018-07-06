package com.ourcompany.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/18 19:23
 * Des    :
 */

public class SAppVersion extends BmobObject{
    private String updateContent;
    private Integer versionCode;

    private String versionName;
    private String fileName;
    private String fileUrl;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //是否强制更新
    private Boolean isFoceUpdate;

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Boolean getFoceUpdate() {
        return isFoceUpdate;
    }

    public void setFoceUpdate(Boolean foceUpdate) {
        isFoceUpdate = foceUpdate;
    }
}
