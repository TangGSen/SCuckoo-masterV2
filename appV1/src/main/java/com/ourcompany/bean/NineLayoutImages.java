package com.ourcompany.bean;


import android.util.Log;

public class NineLayoutImages {
    private String url;

    public NineLayoutImages(String url) {
        this.url = url;
        Log.i("Image",toString());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



}
