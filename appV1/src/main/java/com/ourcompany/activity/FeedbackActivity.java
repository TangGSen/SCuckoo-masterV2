package com.ourcompany.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.activity.FeedbackActPresenter;
import com.ourcompany.utils.InputMethodUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.FeedbackView;
import com.ourcompany.widget.LoadingViewAOV;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.util.Utils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/12 19:06
 * Des    :
 */

public class FeedbackActivity extends MvpActivity<FeedbackView, FeedbackActPresenter> implements FeedbackView {

    @BindView(R.id.btPublish)
    TextView btPublish;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.etContent)
    EditText etContent;

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, FeedbackActivity.this);

    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_feedbacks;
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setTitle(ResourceUtils.getString(R.string.str_feedback));

        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.hideKeyboard(etContent);
                if (!TextUtils.isEmpty(etContent.getText().toString())) {
                    showExistDialog();
                } else {
                    finish();
                    overridePendingTransition(0, 0);
                }


            }
        });

    }

    @Override
    protected FeedbackView bindView() {
        return this;
    }

    @Override
    protected FeedbackActPresenter bindPresenter() {
        return new FeedbackActPresenter(MApplication.mContext);
    }


    @OnClick({R.id.btPublish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btPublish:
                InputMethodUtils.hideKeyboard(etContent);
                LoadingViewAOV.getInstance().with(FeedbackActivity.this, btPublish, R.color.colorPrimary);
                getPresenter().submit(etContent.getText().toString());
                break;

        }
    }

    @Override
    public void vaifyTextError() {
        showToastMsg(ResourceUtils.getString(R.string.input_not_null));
    }

    @Override
    public void submitSuccess() {
        LoadingViewAOV.getInstance().close(FeedbackActivity.this, btPublish);
        showToastMsg(ResourceUtils.getString(R.string.str_feedback_success));
        btPublish.setText("");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        }, 1000);
    }

    @Override
    public void submitError() {
        LoadingViewAOV.getInstance().close(FeedbackActivity.this, btPublish);
        showToastMsg(ResourceUtils.getString(R.string.net_exception));
    }


    private void showExistDialog() {

        //弹出选框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(ResourceUtils.getString(R.string.exist_eidt_publish));
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
                overridePendingTransition(0, 0);
            }
        });

        builder.setCancelable(false);
        Dialog dialog = builder.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.3f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        InputMethodUtils.hideKeyboard(etContent);
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (!TextUtils.isEmpty(etContent.getText().toString())) {
                showExistDialog();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
