package com.ourcompany.utils;

import android.content.Context;

import com.ourcompany.R;
import com.ourcompany.widget.CustomerDialog;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/10 17:14
 * Des    :
 */

public class ProgressDialogUtils {

    private static CustomerDialog progressDialog;

    public static void showprogressDialog(Context context, int layoutId, boolean canCancel) {
        if (progressDialog == null) {
            progressDialog = new CustomerDialog(context, layoutId, R.style.DialogStyle, canCancel);
        } else if (progressDialog.getContext() != context) {
            cancelProgressDialog();
            progressDialog = new CustomerDialog(context, layoutId, R.style.DialogStyle, canCancel);
        }
        progressDialog.show();

    }

    public static void cancelProgressDialog() {
        if (progressDialog == null) {
            return;
        } else {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    public static boolean dialogIsShow() {
        return (progressDialog != null && progressDialog.isShowing());
    }
}
