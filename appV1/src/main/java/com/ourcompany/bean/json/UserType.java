package com.ourcompany.bean.json;

import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/29 19:55
 * Des    :
 */

public class UserType {

    private List<UserTypeBean> userType;

    public List<UserTypeBean> getUserType() {
        return userType;
    }

    public void setUserType(List<UserTypeBean> userType) {
        this.userType = userType;
    }

    public static class UserTypeBean {
        /**
         * typeName : 暂无
         * type : 0
         */

        private String typeName;
        private String type;
        private boolean isChooese =false;

        public boolean isChooese() {
            return isChooese;
        }

        public void setChooese(boolean chooese) {
            isChooese = chooese;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
