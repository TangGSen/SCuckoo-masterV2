package com.ourcompany.fragment.setting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.BindNewAccountFragPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.BindNewAccountFragView;
import com.ourcompany.widget.LoadingViewAOV;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:02
 * Des    :
 */

public class BindNewAccountTwoFragment extends MvpFragment<BindNewAccountFragView, BindNewAccountFragPresenter> implements BindNewAccountFragView {
    private final static String KEY_PHONE = "key_phone";
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.bt_safety_code)
    Button btSafetyCode;
    @BindView(R.id.layout_safety_code)
    LinearLayout layoutSafetyCode;
    @BindView(R.id.btFinish)
    Button btFinish;
    Unbinder unbinder;
    private String mPhone;

    public static BindNewAccountTwoFragment newInstance(String phone) {
        BindNewAccountTwoFragment fragment = new BindNewAccountTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PHONE, phone);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        if (bundle != null) {
            mPhone = bundle.getString(KEY_PHONE);
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getSafetyCode(mPhone);
    }

    @Override
    protected void initLinstener() {
        super.initLinstener();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String code = etVerifyCode.getText().toString();
                if ( !TextUtils.isEmpty(code)) {
                    btFinish.setEnabled(true);
                } else {
                    btFinish.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etVerifyCode.addTextChangedListener(textWatcher);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_bindaccount_two;
    }

    @Override
    protected BindNewAccountFragView bindView() {
        return this;
    }

    @Override
    protected BindNewAccountFragPresenter bindPresenter() {
        return new BindNewAccountFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    public void checkPhoneSuccess(String newPhone) {

    }

    @Override
    public void checkPhoneError(String string) {

    }

    @Override
    public void safetyCodeError(String string) {
        btSafetyCode.setEnabled(true);
        btSafetyCode.setText(ResourceUtils.getString(R.string.get_safety_code));
        showToastMsg(string);
    }

    @Override
    public void setSafetyBtnTextAndStutes(String string, boolean state) {
        btSafetyCode.setEnabled(state);
        btSafetyCode.setText(string);
    }

    @Override
    public void safetyCodeSuccess() {
        btSafetyCode.setEnabled(true);
        btSafetyCode.setText(ResourceUtils.getString(R.string.get_safety_code));
    }

    @Override
    public void updatePhoneSuccess() {
        LoadingViewAOV.getInstance().close(mActivity,btFinish);
        mActivity.finish();
        mActivity.overridePendingTransition(0,0);
        ToastUtils.showSimpleToast("绑定新号码成功");
    }

    @Override
    public void updatePhoneFaild() {
        LoadingViewAOV.getInstance().close(mActivity,btFinish);
        ToastUtils.showSimpleToast("绑定新号码失败");
    }

    @Override
    public void sumitCodeError(String string) {
        LoadingViewAOV.getInstance().close(mActivity,btFinish);
        showToastMsg(string);
    }


    @OnClick({R.id.bt_safety_code, R.id.btFinish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_safety_code:
                getPresenter().getSafetyCode(mPhone);
                break;
            case R.id.btFinish:
                LoadingViewAOV.getInstance().with(mActivity,btFinish,R.color.colorPrimary);
                getPresenter().submitSatyCode(mPhone,etVerifyCode.getText().toString());
                break;
        }
    }


//    @OnClick({R.id.btNext})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btNext:
//
//                break;
//        }
//    }
}
