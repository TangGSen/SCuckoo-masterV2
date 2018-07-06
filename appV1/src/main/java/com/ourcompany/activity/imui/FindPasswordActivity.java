package com.ourcompany.activity.imui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ourcompany.R;
import com.ourcompany.bean.AccoutRsigisterBean;
import com.ourcompany.presenter.activity.FindPasswordActPresenter;
import com.ourcompany.utils.PhoneTextWatcher;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.FindAccoutPswActView;
import com.ourcompany.widget.LoadingViewAOV;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/8 16:46
 * Des    :
 */

public class FindPasswordActivity extends MvpActivity<FindAccoutPswActView, FindPasswordActPresenter> implements FindAccoutPswActView {


    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.bt_safety_code)
    Button btSafetyCode;
    @BindView(R.id.layout_safety_code)
    LinearLayout layoutSafetyCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.layout_ets)
    LinearLayout layoutEts;
    @BindView(R.id.bt_resigister)
    Button btResigister;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setTitle(ResourceUtils.getString(R.string.reset_passwrod));
        btResigister.setText(ResourceUtils.getString(R.string.btn_ok));
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_findpassword;
    }

    @Override
    protected FindAccoutPswActView bindView() {
        return this;
    }

    @Override
    protected FindPasswordActPresenter bindPresenter() {
        return new FindPasswordActPresenter(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getPresenter().getCurrentLoginUser();
    }

    @OnClick({R.id.bt_safety_code, R.id.bt_resigister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_safety_code:
                String phone = etUserName.getText().toString();
                if (btSafetyCode.isEnabled()) {
                    getPresenter().getSafetyCode(phone);
                }
                break;
            case R.id.bt_resigister:
                LoadingViewAOV.getInstance().with(FindPasswordActivity.this, btResigister);
                String phones = etUserName.getText().toString().trim();
                String code = etVerifyCode.getText().toString().trim();
                String password = etPassword.getText().toString();
                getPresenter().sendSafetyCode(phones, code, password);
                break;
        }
    }

    @Override
    public void initLinstener() {
        super.initLinstener();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString();
                String code = etVerifyCode.getText().toString();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(code)) {
                    btResigister.setEnabled(true);
                } else {
                    btResigister.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etPassword.addTextChangedListener(textWatcher);
        etUserName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        etUserName.addTextChangedListener(new PhoneTextWatcher(etUserName));
        etUserName.addTextChangedListener(textWatcher);
        etVerifyCode.addTextChangedListener(textWatcher);

    }


    @Override
    public void getSafetyCodeing() {
        btSafetyCode.setEnabled(false);
    }

    @Override
    public void sendSafetyCodeing() {
        resetAllViewStatus(false);
        //将光标移动到etVerifyCode 好像不行
        etVerifyCode.setFocusable(true);
        etVerifyCode.requestFocus();
        etVerifyCode.setFocusableInTouchMode(true);
    }

    @Override
    public void sendSafetyCoded() {
        resetAllViewStatus(true);
    }

    public void resetAllViewStatus(boolean reset) {
        btSafetyCode.setEnabled(reset);
        btResigister.setEnabled(reset);
    }

    @Override
    public void setSafetyBtnEnable(boolean enable) {
        btSafetyCode.setEnabled(enable);
    }


    @Override
    public void setSafetyBtnText(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            btSafetyCode.setText(msg);
        } else {
            btSafetyCode.setText("");
        }

    }

    @Override
    public void showToastMsg(String msg) {
        ToastUtils.showSimpleToast(msg);
    }

    @Override
    public void verifyFail(String msg) {
        showToastMsg(msg);
        resetAllViewStatus(true);
        LoadingViewAOV.getInstance().close(FindPasswordActivity.this, btResigister);
    }

    @Override
    public void verifySuccess() {
        getView().showToastMsg(ResourceUtils.getString(R.string.str_update_password_success));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadingViewAOV.getInstance().close(FindPasswordActivity.this, btResigister);
                finish();
            }
        }, 1000);

    }

    @Override
    public void hasNotNet() {

    }

    @Override
    public void logining(AccoutRsigisterBean accoutRsigisterBean) {

    }

    @Override
    public void verifyTextError() {
        LoadingViewAOV.getInstance().close(FindPasswordActivity.this, btResigister);
    }

    @Override
    public void setCurrentUserName(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            etUserName.setText(phone);
            //将光标移动到后面
            etUserName.setSelection(etUserName.getText().toString().length());
            etUserName.setFocusable(false);
            //   etUserName.setNextFocusUpId(etVerifyCode);
        }
    }


    @Override
    protected void onDestroy() {
        LoadingViewAOV.getInstance().close(FindPasswordActivity.this, btResigister);
        getPresenter().onDestroy();
        super.onDestroy();

    }


}
