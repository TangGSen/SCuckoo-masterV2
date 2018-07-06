package com.ourcompany.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.activity.ResetPasswordActPresenter;
import com.ourcompany.utils.InputMethodUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.ResetPasswordActView;
import com.ourcompany.widget.LoadingViewAOV;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.util.Utils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/15 15:40
 * Des    :绑定新的手机号码
 */

public class ResetPasswordAcitivity extends MvpActivity<ResetPasswordActView, ResetPasswordActPresenter> implements ResetPasswordActView {


    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.etCurrentPassword)
    EditText etCurrentPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.layout_ets)
    LinearLayout layoutEts;
    @BindView(R.id.btFinish)
    Button btFinish;

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, ResetPasswordAcitivity.class);
        context.startActivity(intent);
    }


    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }
    @Override
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, ResetPasswordAcitivity.this);

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_reset_password;
    }

    @Override
    protected ResetPasswordActView bindView() {
        return this;
    }

    @Override
    protected ResetPasswordActPresenter bindPresenter() {
        return new ResetPasswordActPresenter(MApplication.mContext);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setTitle(ResourceUtils.getString(R.string.str_change_password));
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

    }


    public void initLinstener() {
        super.initLinstener();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = etCurrentPassword.getText().toString().trim();
                String password = etNewPassword.getText().toString();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                    btFinish.setEnabled(true);
                } else {
                    btFinish.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etCurrentPassword.addTextChangedListener(textWatcher);
        etNewPassword.addTextChangedListener(textWatcher);
    }


    @OnClick({R.id.btFinish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btFinish:
                InputMethodUtils.hideKeyboard(etCurrentPassword);
                InputMethodUtils.hideKeyboard(etNewPassword);
                LoadingViewAOV.getInstance().with(ResetPasswordAcitivity.this, btFinish, R.color.colorPrimary);
                getPresenter().resetPassword(etCurrentPassword.getText().toString(), etNewPassword.getText().toString());
                break;
        }
    }

    @Override
    public void verifyPasswordError(String string) {
        LoadingViewAOV.getInstance().close(ResetPasswordAcitivity.this, btFinish);
        showToastMsg(string);
    }

    @Override
    public void resetSuccess() {
        //需要将一些Activity 关闭，然后退出登录，重新登录

        LoadingViewAOV.getInstance().close(ResetPasswordAcitivity.this, btFinish);
        showToastMsg(ResourceUtils.getString(R.string.str_change_passowrd_success));
        finish();
        overridePendingTransition(0, 0);
    }
}
