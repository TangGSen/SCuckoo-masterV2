//package com.ourcompany.activity.imui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.InputFilter;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//
//import com.mob.ums.User;
//import com.ourcompany.R;
//import com.ourcompany.activity.HomeActivity;
//import com.ourcompany.presenter.fragment.ResigisterAccoutFragPresenter;
//import com.ourcompany.utils.Constant;
//import com.ourcompany.utils.PhoneTextWatcher;
//import com.ourcompany.utils.ResourceUtils;
//import com.ourcompany.utils.ToastUtils;
//import com.ourcompany.view.fragment.ResigisterAccoutFragView;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import company.com.commons.framework.view.impl.MvpActivity;
//
//public class ResigisterActivity extends MvpActivity<ResigisterAccoutFragView, ResigisterAccoutFragPresenter> implements ResigisterAccoutFragView {
//
//    @BindView(R.id.common_toolbar)
//    Toolbar commonToolbar;
//    @BindView(R.id.et_user_name)
//    EditText etUserName;
//    @BindView(R.id.et_verify_code)
//    EditText etVerifyCode;
//    @BindView(R.id.bt_safety_code)
//    Button btSafetyCode;
//    @BindView(R.id.layout_safety_code)
//    LinearLayout layoutSafetyCode;
//    @BindView(R.id.et_password)
//    EditText etPassword;
//    @BindView(R.id.layout_ets)
//    LinearLayout layoutEts;
//    @BindView(R.id.bt_resigister)
//    Button btResigister;
//
//    @Override
//    protected void initView() {
//        super.initView();
//        setSupportActionBar(commonToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransition(0, 0);
//            }
//        });
//
//
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_resigister;
//    }
//
//    @Override
//    protected ResigisterAccoutFragView bindView() {
//        return this;
//    }
//
//    @Override
//    protected ResigisterAccoutFragPresenter bindPresenter() {
//        return new ResigisterAccoutFragPresenter(this);
//    }
//
//
//    @OnClick({R.id.bt_safety_code, R.id.bt_resigister})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.bt_safety_code:
//                String phone = etUserName.getText().toString();
//                if (btSafetyCode.isEnabled()) {
//                    getPresenter().getSafetyCode(phone);
//                }
//                break;
//            case R.id.bt_resigister:
//                String phones = etUserName.getText().toString().trim();
//                String code = etVerifyCode.getText().toString().trim();
//                String password = etVerifyCode.getText().toString();
//                getPresenter().sendSafetyCode( phones, code,password);
//                break;
//        }
//    }
//
//    @Override
//    public void initLinstener() {
//        super.initLinstener();
//
//        TextWatcher textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String userName = etUserName.getText().toString().trim();
//                String password = etPassword.getText().toString();
//                String code = etVerifyCode.getText().toString();
//                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(code)) {
//                    btResigister.setEnabled(true);
//                } else {
//                    btResigister.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        };
//        etPassword.addTextChangedListener(textWatcher);
//        etUserName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constant.MAX_INPUT_FOR_USERNAME)});
//        etUserName.addTextChangedListener(new PhoneTextWatcher(etUserName));
//        etUserName.addTextChangedListener(textWatcher);
//        etVerifyCode.addTextChangedListener(textWatcher);
//    }
//
//
//    @Override
//    public void getSafetyCodeing() {
//        btSafetyCode.setEnabled(false);
//    }
//
//    @Override
//    public void sendSafetyCodeing() {
//        resetAllViewStatus(false);
//        //将光标移动到etVerifyCode 好像不行
//        etVerifyCode.setFocusable(true);
//        etVerifyCode.requestFocus();
//        etVerifyCode.setFocusableInTouchMode(true);
//    }
//
//    @Override
//    public void sendSafetyCoded() {
//        resetAllViewStatus(true);
//    }
//
//    public void resetAllViewStatus(boolean reset) {
//        btSafetyCode.setEnabled(reset);
//        btResigister.setEnabled(reset);
//    }
//
//    @Override
//    public void setSafetyBtnEnable(boolean enable) {
//        btSafetyCode.setEnabled(enable);
//    }
//
//
//    @Override
//    public void setSafetyBtnText(String msg) {
//        if (!TextUtils.isEmpty(msg)) {
//            btSafetyCode.setText(msg);
//        } else {
//            btSafetyCode.setText("");
//        }
//
//    }
//
//    @Override
//    public void showToastMsg(String msg) {
//        ToastUtils.showSimpleToast(msg);
//    }
//
//    @Override
//    public void verifyFail(String msg) {
//        showToastMsg(msg);
//        resetAllViewStatus(true);
//    }
//
//    @Override
//    public void verifySuccess() {
//        showToastMsg("ok");
//        finish();
//    }
//
//    @Override
//    public void hasNotNet() {
//
//    }
//
//    @Override
//    public void logining(User phone, String password) {
//        showToastMsg(ResourceUtils.getString(R.string.logining));
//    }
//
//    @Override
//    public void loginFail(String userInfos) {
//        //自定动登陆失败，那就让用户，手动登陆，不过得将用户的手机号码回传，体验比较好
//        Intent intent = new Intent(ResigisterActivity.this,LoginActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constant.ACT_LOGIN_PHONE,userInfos);
//        intent.putExtra(Constant.ACT_LOGIN_BUNDLE,bundle);
//        intent.putExtra(Constant.ACT_FROM,Constant.ACT_FROM_RESIGISTER);
//        startActivity(intent);
//        finish();
//        overridePendingTransition(0,0);
//    }
//
//    @Override
//    public void loginSuccess() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra(Constant.ACT_FROM,Constant.ACT_FROM_LOGIN_SUCCESS);
//        startActivity(intent);
//        overridePendingTransition(0,0);
//        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        getPresenter().onDestroy();
//        super.onDestroy();
//
//    }
//}
