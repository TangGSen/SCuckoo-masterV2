package com.ourcompany.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/30 18:23
 * Des    :app 所有的设置
 */

public class AppSettingJson extends BmobObject {
    public String content;
    public String key;
    public String belong;

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
