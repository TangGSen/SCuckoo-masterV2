package com.ourcompany.presenter.activity;

import android.content.Context;
import android.text.TextUtils;

import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.ourcompany.R;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.ResetPasswordActView;

import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Created by Administrator on 2017/8/20.
 */

public class ResetPasswordActPresenter extends MvpBasePresenter<ResetPasswordActView> {

    public ResetPasswordActPresenter(Context context) {
        super(context);
    }

    public void resetPassword(String oldStr, String newStr) {
        if (TextUtils.isEmpty(oldStr)) {
            showErrorMsg(ResourceUtils.getString(R.string.hint_et_current_password));
            return;
        }

        if (TextUtils.isEmpty(newStr)) {
            showErrorMsg(ResourceUtils.getString(R.string.hint_et_new_password));
            return;
        }

        if (newStr.equals(oldStr)) {
            showErrorMsg(ResourceUtils.getString(R.string.str_new_old_password_same));
            return;
        }

        UMSSDK.changePassword(newStr, oldStr, new OperationCallback<Void>() {
            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                showErrorMsg(ResourceUtils.getString(R.string.str_change_passowrd_error));
            }

            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                getView().resetSuccess();
            }

        });
    }

    private void showErrorMsg(final String string) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().verifyPasswordError(string);
            }
        });
    }
}
