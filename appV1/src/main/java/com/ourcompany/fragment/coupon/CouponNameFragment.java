package com.ourcompany.fragment.coupon;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ourcompany.EmptyMvpPresenter;
import com.ourcompany.EmptyMvpView;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.utils.ToastUtils;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/5 下午3:52
 * Des    :
 */
public class CouponNameFragment extends MvpFragment<EmptyMvpView, EmptyMvpPresenter> implements EmptyMvpView {
    public static final String KEY_CURRENT_INDEX = "current_index";
    public static final String KEY_COUNT = "count";
    public static final String KEY_INTPUT_COUNT = "input_count";
    public static final String KEY_INPUT_TITLE = "input_title";
    //如果为1 就设置为数字类型
    public static final String KEY_INPUT_TYPE = "input_type";
    public static final int INPUT_TYPE_NUMBER = 1;

    @BindView(R.id.tvPosition)
    TextView tvPosition;
    @BindView(R.id.tvInputTip)
    TextView tvInputTip;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvInputCount)
    TextView tvInputCount;
    Unbinder unbinder;
    private int currentIndex;
    private int count;
    private  int inputCount = 8;
    private String inputTitle;
    private int inputType;

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        currentIndex = bundle.getInt(KEY_CURRENT_INDEX);
        count = bundle.getInt(KEY_COUNT);

        //限制多个字数
        inputCount = bundle.getInt(KEY_INTPUT_COUNT);
        inputTitle = bundle.getString(KEY_INPUT_TITLE);
        inputType = bundle.getInt(KEY_INPUT_TYPE);

    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        tvPosition.setText(currentIndex+"/"+count);
        tvInputCount.setText(0+"/"+inputCount);
        tvInputTip.setText(inputTitle);
        if(inputType ==INPUT_TYPE_NUMBER){
            etName.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = etName.getText().toString().trim();
                tvInputCount.setText(name.length()+"/"+inputCount);
               // ( (AddCouponActivity) mActivity).setInputTextChanged(currentIndex,name);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etName.addTextChangedListener(textWatcher);
        etName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputCount)});
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupon_add_name;
    }

    @Override
    protected EmptyMvpView bindView() {
        return this;
    }

    @Override
    protected EmptyMvpPresenter bindPresenter() {
        return new EmptyMvpPresenter(MApplication.mContext);
    }


}
