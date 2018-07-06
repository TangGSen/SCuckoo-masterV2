package com.ourcompany.bean;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/9 15:23
 * Des    :
 */

public class CompressImage {
    public String cachePaht;
    public String srcPaht;
    public boolean isExist;

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public String getCachePaht() {
        return cachePaht;
    }

    public void setCachePaht(String cachePaht) {
        this.cachePaht = cachePaht;
    }

    public String getSrcPaht() {
        return srcPaht;
    }

    public void setSrcPaht(String srcPaht) {
        this.srcPaht = srcPaht;
    }

    public CompressImage(String cachePaht, String srcPaht) {
        this.cachePaht = cachePaht;
        this.srcPaht = srcPaht;
    }
}
