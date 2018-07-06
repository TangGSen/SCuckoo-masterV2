package com.ourcompany.presenter.fragment;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.ourcompany.R;
import com.ourcompany.bean.bmob.Coupon;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.EditextCouponInfoFragView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Created by Administrator on 2017/8/20.
 */

public class EditextCouponInfoFragPresenter extends MvpBasePresenter<EditextCouponInfoFragView> {

    private ClickableSpan startTimeSpan;
    private ClickableSpan endTimeSpan;
    //开始时间的标签
    public static final int START_TIME_TAG = 0;
    //结束时间的标签
    public static final int END_TIME_TAG = 1;

    public EditextCouponInfoFragPresenter(Context context) {
        super(context);
    }


    public SpannableStringBuilder getTime(final String startTime, final String endTime) {
        if (TextUtils.isEmpty(endTime) || TextUtils.isEmpty(endTime)) {
            return new SpannableStringBuilder("");
        }
        //定义点击每个部分文字的处理方法
//        String startTime = " "+startTimeStr+" ";
//        String endTime = " "+endTimeStr+" ";
        SpannableStringBuilder builder = new SpannableStringBuilder(startTime); //创建SpannableStringBuilder，并添加前面文案
        String other = " 至 ";
        builder.append(other);
        builder.append(endTime); //追加后面文案
        if(startTimeSpan ==null){
            initStartTimeClick();
        }
        builder.setSpan(startTimeSpan, 0, startTime.length(), 0);
      //  builder.setSpan(new BackgroundColorSpan(ResourceUtils.getResColor(R.color.whiles)), 0, startTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      //  builder.setSpan(new ForegroundColorSpan(ResourceUtils.getResColor(R.color.colorPrimary)), 0, startTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(endTimeSpan ==null){
            initEndTimeClick();
        }
        builder.setSpan(endTimeSpan, startTime.length() + other.length(), startTime.length() + other.length() + endTime.length(), 0);
     //   builder.setSpan(new BackgroundColorSpan(ResourceUtils.getResColor(R.color.whiles)), startTime.length() + other.length(), startTime.length() + other.length() + endTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
     //   builder.setSpan(new ForegroundColorSpan(ResourceUtils.getResColor(R.color.colorPrimary)), startTime.length() + other.length(), startTime.length() + other.length() + endTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;

    }

    private void initStartTimeClick() {
        startTimeSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                getView().showStartTimeDialog();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ResourceUtils.getResColor(R.color.whiles));
                ds.setUnderlineText(false);
            }
        };
    }

    private void initEndTimeClick() {
        endTimeSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                getView().showEndTimeDialog();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ResourceUtils.getResColor(R.color.whiles));
                ds.setUnderlineText(false);
            }
        };
    }

    public String getDate(long time,int currentType) {
        SimpleDateFormat sdf = null;
        if (currentType==START_TIME_TAG) {
            sdf = new SimpleDateFormat("yyyy-MM-dd 00:00");
        }else if(currentType==END_TIME_TAG){
            sdf = new SimpleDateFormat("yyyy-MM-dd 23:59");
        }
        Date d = new Date(time);
        return sdf.format(d);
    }

    //提交信息
    public void submitInfo(final Coupon coupon) {
       if(coupon ==null){
           getView().submitError();
           return;
       }
       coupon.save(new SaveListener<String>() {
           @Override
           public void done(String s, BmobException e) {
               if(e ==null){
                   getView().submitSuccess(coupon);
               }else{
                   getView().submitError();
               }
           }
       });
    }
}
