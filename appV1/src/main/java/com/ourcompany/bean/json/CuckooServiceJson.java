package com.ourcompany.bean.json;

import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/5/1 21:06
 * Des    :
 */

public class CuckooServiceJson {


    private List<CuckooServiceBean> cuckooService;
    private List<CuckooClassBean> cuckooClass;

    public List<CuckooServiceBean> getCuckooService() {
        return cuckooService;
    }

    public void setCuckooService(List<CuckooServiceBean> cuckooService) {
        this.cuckooService = cuckooService;
    }

    public List<CuckooClassBean> getCuckooClass() {
        return cuckooClass;
    }

    public void setCuckooClass(List<CuckooClassBean> cuckooClass) {
        this.cuckooClass = cuckooClass;
    }

    public static class CuckooServiceBean {
        /**
         * key : 满意后付款
         */

        private String key;
        private boolean isSeleted =false;

        public boolean isSeleted() {
            return isSeleted;
        }

        public void setSeleted(boolean seleted) {
            isSeleted = seleted;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class CuckooClassBean {
        /**
         * classType : 全部
         */

        private String classType;
        private boolean isSeleted =false;

        public boolean isSeleted() {
            return isSeleted;
        }

        public void setSeleted(boolean seleted) {
            isSeleted = seleted;
        }

        public String getClassType() {
            return classType;
        }

        public void setClassType(String classType) {
            this.classType = classType;
        }
    }
}
