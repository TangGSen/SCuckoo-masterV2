package com.ourcompany.fragment.user_class_detail;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.ourcompany.EmptyMvpPresenter;
import com.ourcompany.EmptyMvpView;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ToastUtils;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/5/16 10:29
 * Des    : 公司或个人的team
 */

public class UserClassTeamFragment extends MvpFragment<EmptyMvpView, EmptyMvpPresenter> implements EmptyMvpView {
    @BindView(R.id.layoutDesign)
    FrameLayout layoutDesign;
    @BindView(R.id.layoutWorker)
    FrameLayout layoutWorker;
    @BindView(R.id.mConstraintLayout)
    ConstraintLayout mConstraintLayout;
    Unbinder unbinder;
    private String mUserId;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mUserId = bundle.getString(Constant.BMOB_SUSER_ID);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_class_team;
    }

    @Override
    protected EmptyMvpView bindView() {
        return this;
    }

    @Override
    protected EmptyMvpPresenter bindPresenter() {
        return new EmptyMvpPresenter(MApplication.mContext);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        UserClassTeamDeatilFragment designFragment = new UserClassTeamDeatilFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_TEAM_TYPE,Constant.TEAM_TYPE_DESINGE);
        bundle.putString(Constant.BMOB_SUSER_ID, mUserId);
        designFragment.setArguments(bundle);
        transaction.replace(R.id.layoutDesign, designFragment);
        transaction.commit();


        transaction = getFragmentManager().beginTransaction();
        UserClassTeamDeatilFragment workerFragment = new UserClassTeamDeatilFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(Constant.KEY_TEAM_TYPE,Constant.TEAM_TYPE_WORKER);
        bundle2.putString(Constant.BMOB_SUSER_ID, mUserId);
        workerFragment.setArguments(bundle2);
        transaction.replace(R.id.layoutWorker, workerFragment);
        transaction.commit();
    }

    @Override
    protected void initData() {
        super.initData();
    }


}
