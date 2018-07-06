package com.ourcompany.bean.json;

import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/23 17:03
 * Des    :
 */

public class AdvertisementData{


    private List<AdSettingBean> adSetting;

    public List<AdSettingBean> getAdSetting() {
        return adSetting;
    }

    public void setAdSetting(List<AdSettingBean> adSetting) {
        this.adSetting = adSetting;
    }

    public static class AdSettingBean {
        /**
         * describe : 这是测试的
         * imageUrl : http://5.595818.com/2015/pic/000/372/8e32ba756b80507414dcfdd2e0ffec40.jpg
         */

        private String describe;
        private String imageUrl;

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
