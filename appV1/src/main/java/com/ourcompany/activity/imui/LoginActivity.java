package com.ourcompany.activity.imui;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ourcompany.R;
import com.ourcompany.bean.UserAccoutLoginRes;
import com.ourcompany.presenter.activity.LoginActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.PhoneTextWatcher;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.LoginActvityView;
import com.ourcompany.widget.LoadingViewAOV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;

public class LoginActivity extends MvpActivity<LoginActvityView, LoginActPresenter> implements LoginActvityView {


    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.find_password)
    Button findPassword;
    @BindView(R.id.singUp)
    Button singUp;

    @Override
    protected void initView() {
        super.initView();
        etUserName.setText("15915311453");
        etPassword.setText("12345678");
        btLogin.setEnabled(true);
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        EventBus.getDefault().register(this);
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
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) ) {
                    btLogin.setEnabled(true);
                } else {
                    btLogin.setEnabled(false);
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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginActvityView bindView() {
        return this;
    }

    @Override
    protected LoginActPresenter bindPresenter() {
        return new LoginActPresenter(this);
    }


    @OnClick({R.id.bt_login, R.id.find_password, R.id.singUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.find_password:
                Intent fintent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(fintent);
                break;
            case R.id.singUp:
                Intent intent = new Intent(LoginActivity.this, ResigisterAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_login:
                LoadingViewAOV.getInstance().with(LoginActivity.this,btLogin);
                String phones = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString();
                getPresenter().login( phones, password);

                break;
        }
    }


    @Override
    public void loading() {

    }

    @Override
    public void loaded() {

    }

    @Override
    public void loginSucess() {
        LoadingViewAOV.getInstance().close(LoginActivity.this,btLogin);
        EventBus.getDefault().post(new UserAccoutLoginRes().setLoginSuccess(true));
        overridePendingTransition(0,0);
        finish();
    }

    /**
     * 登陆成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventResigister(UserAccoutLoginRes res) {
        if (res.isLoginSuccess()) {
            overridePendingTransition(0, 0);
            finish();
        } else {
            String phone = res.getPhone();
            if (!TextUtils.isEmpty(phone)) {
                etUserName.setText(phone);
                //将光标移动到后面
                etUserName.setSelection(etUserName.getText().toString().length());
            }
        }
    }

    @Override
    public void loginFial() {
        showToastMsg(ResourceUtils.getString(R.string.login_fail));
        LoadingViewAOV.getInstance().close(LoginActivity.this,btLogin);
    }

    @Override
    public void verifyError() {
        LoadingViewAOV.getInstance().close(LoginActivity.this,btLogin);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
