package com.ourcompany.bean.bmob;

import android.text.TextUtils;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 下午4:00
 * Des    : 优惠券
 */
public class Coupon extends BmobObject {

    private String name;

    private BmobDate startTime;
    private BmobDate endTime;
    // 优惠金额
    private Integer couponMoney;
    //最低满足的条件 门槛
    private String leastMoney;
    //发行的总数
    private Integer count;
    //每个人可领总数
    private Integer limit;
    private String userId;
    //本地使用，将开始时间和结束时间拼接
    private String timeInfo;
    //本地使用，如果是选择模式的话，那么就
    private boolean isChooseType =true;
    private boolean isChoose =false;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isChooseType() {
        return isChooseType;
    }

    public void setChooseType(boolean chooseType) {
        isChooseType = chooseType;
    }

    public String getTimeInfo() {
        if (TextUtils.isEmpty(timeInfo)) {
            StringBuilder builder = new StringBuilder();
//            if(startTime!=null){
//               String startTimeStr = startTime.getDate();
//
//                if(!TextUtils.isEmpty(startTimeStr)){
//                    builder.append(startTimeStr.substring(0,startTimeStr.length()-3));
//                }
//
//                builder.append(" 至 ");
//            }
            builder.append("截止日期：");
            if(endTime!=null){
                String endTimeStr = endTime.getDate();
                if(!TextUtils.isEmpty(endTimeStr)){
                    builder.append(endTimeStr.substring(0,endTimeStr.length()-3));
                }
            }
            timeInfo = builder.toString();
        }
        return timeInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        if(TextUtils.isEmpty(name)){
           return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobDate getStartTime() {
        return startTime;
    }

    public void setStartTime(BmobDate startTime) {
        this.startTime = startTime;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

    public Integer getCouponMoney() {
        if (couponMoney == null || couponMoney < 0) {
            return 0;
        }
        return couponMoney;
    }

    public void setCouponMoney(Integer couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getLeastMoney() {
        return leastMoney;
    }

    public void setLeastMoney(String leastMoney) {
        this.leastMoney = leastMoney;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
