package com.ourcompany.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/26 12:39
 * Des    :
 */

public class LocationOption {

    private volatile static LocationOption instance;

    private LocationOption() {
    }

    public static LocationOption getInstance() {
        if (instance == null) {
            synchronized (LocationOption.class) {
                if (instance == null) {
                    instance = new LocationOption();
                }
            }
        }
        return instance;
    }


    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    /**
     * 定位监听
     */
    private AMapLocationListener locationListener;
    private MLocationListener mLocationListener;

    public interface MLocationListener {
        void onSuccess(MLocation mLocation);

        void onFail();
    }

    public class MLocation {


        private String province;
        private String city;
        private String district;
        private String address;

        public String getCityCode() {
            return cityCode;
        }

        public MLocation setCityCode(String cityCode) {
            this.cityCode = cityCode;
            return this;
        }

        private String cityCode;

        public String getProvince() {
            return province;
        }

        public MLocation setProvince(String province) {
            this.province = province;
            return this;
        }

        public String getCity() {
            return city;
        }

        public MLocation setCity(String city) {
            this.city = city;
            return this;
        }

        public String getDistrict() {
            return district;
        }

        public MLocation setDistrict(String district) {
            this.district = district;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public MLocation setAddress(String address) {
            this.address = address;
            return this;
        }


    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void initLocation(Context context, final MLocationListener mLocationListener) {
        //初始化client
        locationClient = new AMapLocationClient(context.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);

        locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                if (mLocationListener == null) {
                    return;
                }
                if (null != location) {
                    //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                    if (location.getErrorCode() == 0) {
                        //定位成功
                        mLocationListener.onSuccess(new MLocation().setAddress(location.getAddress())
                                .setCity(location.getCity())
                                .setDistrict(location.getDistrict())
                                .setProvince(location.getProvince())
                        .setCityCode(location.getCityCode()));
                    } else {
                        //定位失败
                        LogUtils.e("sen","****0");
                        mLocationListener.onFail();
                    }
                } else {
                    LogUtils.e("sen","****1");
                    //定位失败
                    mLocationListener.onFail();
                }
            }
        };
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }


    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        // 设置是否单次定位
        locationOption.setOnceLocation(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        //设置是否使用传感器
        locationOption.setSensorEnable(true);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(1000);
        // 设置网络请求超时时间
        locationOption.setHttpTimeOut(8000);
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void startLocation() {
        //根据控件的选择，重新设置定位参数
        //resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void destroyLocation() {
        stopLocation();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
