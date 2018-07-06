package com.ourcompany.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.eventbus.ChangeNewPhone;
import com.ourcompany.fragment.setting.BindNewAccountOneFragment;
import com.ourcompany.fragment.setting.BindNewAccountTwoFragment;
import com.ourcompany.presenter.activity.BindNewPhonePresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.StringUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.BindNewPhoneActView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.util.Utils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/15 15:40
 * Des    :绑定新的手机号码
 */

public class BindNewPhoneAcitivity extends MvpActivity<BindNewPhoneActView, BindNewPhonePresenter> implements BindNewPhoneActView {


    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.layoutContent)
    FrameLayout layoutContent;
    @BindView(R.id.tipNewPhone)
    TextView tipNewPhone;

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, BindNewPhoneAcitivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, BindNewPhoneAcitivity.this);

    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_new_phone;
    }

    @Override
    protected BindNewPhoneActView bindView() {
        return this;
    }

    @Override
    protected BindNewPhonePresenter bindPresenter() {
        return new BindNewPhonePresenter(MApplication.mContext);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setTitle(ResourceUtils.getString(R.string.str_bind_newphone));
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.layoutContent, BindNewAccountOneFragment.newInstance());
        transaction.commit();
        EventBus.getDefault().register(this);

        tipNewPhone.setText(ResourceUtils.getString(R.string.str_bindnew_step_1));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangePhone(ChangeNewPhone phone) {
        if (phone == null) {
            return;
        }
        tipNewPhone.setText(String.format(ResourceUtils.getString(R.string.str_bindnew_step_2), StringUtils.getDealPhone(phone.getPhone())));
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.layoutContent, BindNewAccountTwoFragment.newInstance(phone.getPhone()));
        transaction.commit();
    }


//    @OnClick({R.id.bt_resigister})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.bt_resigister:
//
//                break;
//
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
