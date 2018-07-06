package com.ourcompany.fragment.user;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.AccoutRsigisterBean;
import com.ourcompany.bean.UserAccoutLoginRes;
import com.ourcompany.presenter.fragment.ResigisterAccoutFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.PhoneTextWatcher;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.ResigisterAccoutFragView;
import com.ourcompany.widget.LoadingViewAOV;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

public class ResigisterAccoutFragment extends MvpFragment<ResigisterAccoutFragView, ResigisterAccoutFragPresenter> implements ResigisterAccoutFragView {


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
    Unbinder unbinder;

    @Override
    protected void initView(View view) {
        super.initView(view);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_resigister_account;
    }

    @Override
    protected ResigisterAccoutFragView bindView() {
        return this;
    }

    @Override
    protected ResigisterAccoutFragPresenter bindPresenter() {
        return new ResigisterAccoutFragPresenter(MApplication.mContext);
    }


    @OnClick({R.id.bt_safety_code, R.id.bt_resigister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_safety_code:
                String phone = etUserName.getText().toString();
                if (btSafetyCode.isEnabled()) {
                    //首先判断该用户是否已经注册过
                    getPresenter().checkUserIsExist(phone);
                    // getPresenter().getSafetyCode(phone);
                }
                break;
            case R.id.bt_resigister:
                LoadingViewAOV.getInstance().with(mActivity, btResigister);
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
        etUserName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constant.MAX_INPUT_FOR_USERNAME)});
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
    }

    @Override
    public void verifySuccess() {
    }

    @Override
    public void hasNotNet() {

    }

    @Override
    public void logining(AccoutRsigisterBean accoutRsigisterBean) {
        showToastMsg(ResourceUtils.getString(R.string.logining));
        EventBus.getDefault().post(accoutRsigisterBean);
        LoadingViewAOV.getInstance().close(mActivity, btResigister);
    }

    /**
     *
     */

    @Override
    public void verifyTextError() {
        LoadingViewAOV.getInstance().close(mActivity, btResigister);
    }

    @Override
    public void setCurrentUserName(String s) {

    }

    @Override
    public void thisPhoneIsExist() {
        showUserIsExistDialog();
    }

    /**
     * 显示用户已经存在的dialog
     */
    private void showUserIsExistDialog() {
        //如果不设置的话，那么就直接登录和创建bmob 用户
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(ResourceUtils.getString(R.string.str_phone_exist));
        builder.setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showToastMsg(ResourceUtils.getString(R.string.str_try_other_phone));
                setSafetyBtnEnable(true);
                setSafetyBtnText(ResourceUtils.getString(R.string.get_safety_code));
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String phone = etUserName.getText().toString();
                phone = phone.replaceAll(" ", "");
                EventBus.getDefault().post(new UserAccoutLoginRes().setLoginSuccess(false).setPhone(phone));
                dialogInterface.dismiss();
                mActivity.finish();
                mActivity.overridePendingTransition(0, 0);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void thisPhoneIsNotExist() {
        String phone = etUserName.getText().toString();
        getPresenter().getSafetyCode(phone);
    }


    @Override
    public void onDestroy() {
        getPresenter().onDestroy();
        super.onDestroy();
    }




}
