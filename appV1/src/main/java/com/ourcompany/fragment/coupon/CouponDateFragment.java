package com.ourcompany.fragment.coupon;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ourcompany.EmptyMvpPresenter;
import com.ourcompany.EmptyMvpView;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.widget.calender.CalendarDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/5 下午3:52
 * Des    :
 */
public class CouponDateFragment extends MvpFragment<EmptyMvpView, EmptyMvpPresenter> implements EmptyMvpView {
    public static final String KEY_CURRENT_INDEX = "current_index";
    public static final String KEY_COUNT = "count";
    public static final String KEY_INPUT_TITLE = "input_title";


    @BindView(R.id.tvPosition)
    TextView tvPosition;
    @BindView(R.id.tvInputTip)
    TextView tvInputTip;
    @BindView(R.id.tvChooseDate)
    TextView tvChooseDate;
    Unbinder unbinder1;
    private int currentIndex;
    private int count;
    private String inputTitle;
    private AlertDialog calendarDialog;

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        currentIndex = bundle.getInt(KEY_CURRENT_INDEX);
        count = bundle.getInt(KEY_COUNT);

        //限制多个字数
        inputTitle = bundle.getString(KEY_INPUT_TITLE);


    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        tvPosition.setText(currentIndex + "/" + count);
        long time = System.currentTimeMillis();

        tvChooseDate.setText(getDate(time));
        tvInputTip.setText(inputTitle);


    }

    private String getDate(long time) {
        SimpleDateFormat sdf = null;
        if (inputTitle.equals(ResourceUtils.getString(R.string.str_input_counpon_end_time))) {
            sdf = new SimpleDateFormat("yyyy-MM-dd 23:59");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd 00:00");
        }
        Date d = new Date(time);
        return sdf.format(d);
    }


    public void initCanlenderView() {
        final Calendar calendar = Calendar.getInstance();
        calendarDialog = new CalendarDialog().getCalendarDialog(mActivity, true, true, Calendar.getInstance().get(Calendar.YEAR) + 1, 1950, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new CalendarDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(long time, int year, int month, int day, boolean isLunar) {
                if (System.currentTimeMillis() > time) {
                    time = System.currentTimeMillis();
                }

                String date = getDate(time);
                tvChooseDate.setText(date);
              //  ((AddCouponActivity) mActivity).changeDate(currentIndex,date);

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupon_choose_date;
    }

    @Override
    protected EmptyMvpView bindView() {
        return this;
    }

    @Override
    protected EmptyMvpPresenter bindPresenter() {
        return new EmptyMvpPresenter(MApplication.mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (calendarDialog != null && calendarDialog.isShowing())
            calendarDialog.dismiss();
    }






    @OnClick({R.id.tvChooseDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvChooseDate:
                if (calendarDialog == null) {
                    initCanlenderView();
                }
                if (calendarDialog.isShowing()) {
                    return;
                }
                calendarDialog.show();
                break;
        }
    }
}
