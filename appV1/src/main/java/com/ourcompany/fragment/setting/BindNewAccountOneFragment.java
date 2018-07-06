package com.ourcompany.fragment.setting;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.eventbus.ChangeNewPhone;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.presenter.fragment.BindNewAccountFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.PhoneTextWatcher;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.BindNewAccountFragView;
import com.ourcompany.widget.LoadingViewAOV;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

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

public class BindNewAccountOneFragment extends MvpFragment<BindNewAccountFragView, BindNewAccountFragPresenter> implements BindNewAccountFragView {


    @BindView(R.id.etCurrentPhone)
    EditText etCurrentPhone;
    @BindView(R.id.etNewPhone)
    EditText etNewPhone;
    @BindView(R.id.layout_ets)
    LinearLayout layoutEts;
    @BindView(R.id.btNext)
    Button btNext;
    Unbinder unbinder;
    User user ;
    public static BindNewAccountOneFragment newInstance() {
        BindNewAccountOneFragment fragment = new BindNewAccountOneFragment();
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        user = Constant.CURRENT_USER;
    }
    public void initLinstener() {
        super.initLinstener();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = etCurrentPhone.getText().toString().trim();
                String password = etNewPhone.getText().toString();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) ) {
                    btNext.setEnabled(true);
                } else {
                    btNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etCurrentPhone.addTextChangedListener(textWatcher);
        etCurrentPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constant.MAX_INPUT_FOR_USERNAME)});
        etCurrentPhone.addTextChangedListener(new PhoneTextWatcher(etCurrentPhone));
        etNewPhone.addTextChangedListener(textWatcher);
        etNewPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constant.MAX_INPUT_FOR_USERNAME)});
        etNewPhone.addTextChangedListener(new PhoneTextWatcher(etNewPhone));
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_bindaccount_one;
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


    @OnClick({R.id.btNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btNext:
               // LoadingViewAOV.getInstance().with(mActivity, btNext, R.color.colorPrimary);
                getPresenter().verifyPhone(etCurrentPhone.getText().toString(), etNewPhone.getText().toString());
                HashMap<String, Object> userInfos = new HashMap<>();
                userInfos.put(user.phone.getName(), "18929709326");
                MServiceManager.getInstance().updateUser(userInfos,new OperationCallback<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        super.onSuccess(aVoid);
                        LogUtils.e("sen","成功了");
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        super.onFailed(throwable);
                        LogUtils.e("sen","sen:"+throwable.getLocalizedMessage());
                    }
                });
                break;
        }
    }

    @Override
    public void checkPhoneSuccess(String newPhone) {
        LoadingViewAOV.getInstance().close(mActivity, btNext);
        EventBus.getDefault().post(new ChangeNewPhone().setPhone(newPhone));
    }

    @Override
    public void checkPhoneError(String string) {
        LoadingViewAOV.getInstance().close(mActivity, btNext);
        showToastMsg(string);
    }

    @Override
    public void safetyCodeError(String string) {

    }

    @Override
    public void setSafetyBtnTextAndStutes(String string, boolean state) {

    }

    @Override
    public void safetyCodeSuccess() {

    }

    @Override
    public void updatePhoneSuccess() {

    }

    @Override
    public void updatePhoneFaild() {

    }

    @Override
    public void sumitCodeError(String string) {

    }


}
