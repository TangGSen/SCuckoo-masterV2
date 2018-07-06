package com.ourcompany.fragment.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.AccoutRsigisterBean;
import com.ourcompany.bean.UserAccoutLoginRes;
import com.ourcompany.bean.json.UserType;
import com.ourcompany.presenter.fragment.ChoseUserTypePresenter;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.ChoseUserTypeView;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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

public class ChoseUserTypeFragment extends MvpFragment<ChoseUserTypeView, ChoseUserTypePresenter> implements ChoseUserTypeView {


    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    Unbinder unbinder;
    @BindView(R.id.btFinish)
    Button btFinish;
    private RecycleCommonAdapter<UserType.UserTypeBean> recycleCommonAdapter;
    private List<UserType.UserTypeBean> mUserTypeList = new ArrayList<>();
    private int currentChoose = -1;
    private static String bmobUserId;
    private AccoutRsigisterBean mAccoutRsigister;
    private boolean isLoginSuccess;
    private boolean isFutureSetting;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_user_type;
    }

    @Override
    protected ChoseUserTypeView bindView() {
        return this;
    }

    @Override
    protected ChoseUserTypePresenter bindPresenter() {
        return new ChoseUserTypePresenter(MApplication.mContext);
    }

    @Override
    protected void initStateLayout(View view) {
        super.initStateLayout(view);
        //初始化状态的布局
        View emptyView = getLayoutInflater().inflate(R.layout.layout_state_empty_with_retry, (ViewGroup) mActivity.findViewById(android.R.id.content), false);
        layoutState.setEmptyView(emptyView);
        layoutState.changeState(StateFrameLayout.LOADING);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        EventBus.getDefault().register(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //解决嵌套在NestedScrollView 的滑动不顺的问题1

        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        //解决嵌套在NestedScrollView 的滑动不顺的问题2
        recycleview.setNestedScrollingEnabled(true);
        recycleCommonAdapter = new RecycleCommonAdapter<UserType.UserTypeBean>(
                MApplication.mContext, mUserTypeList, R.layout.layout_item_user_type) {
            @Override
            public void bindItemData(SViewHolder holder, final UserType.UserTypeBean itemData, int position) {
                holder.setText(R.id.tvTypeName, itemData.getTypeName());
                ((ImageView) holder.getView(R.id.imgChoose)).setSelected(itemData.isChooese());
            }
        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                if (position == currentChoose) {
                    return;
                }
                if (currentChoose != -1) {
                    mUserTypeList.get(currentChoose).setChooese(false);
                    recycleCommonAdapter.notifyItemChanged(currentChoose);
                }
                currentChoose = position;
                mUserTypeList.get(position).setChooese(true);
                recycleCommonAdapter.notifyItemChanged(position);
                btFinish.setEnabled(true);
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getUserType();
    }

    @Override
    public void showToastMsg(String string) {

    }


    @Override
    public void showEmptyView() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showContentView(List<UserType.UserTypeBean> list) {
        recycleCommonAdapter.addDatasInLast(list);
        layoutState.changeState(StateFrameLayout.SUCCESS);
    }

    @Override
    public void setThridId(String s) {
        bmobUserId = s;
        LogUtils.e("sen", "5setThridId" + s);
    }

    /**
     * 登录成功
     */
    @Override
    public void loadUserSuccess() {
        //登录成功才去更新用户的数据
        isLoginSuccess = true;
        if (isFutureSetting) {
            //正在退出
            LogUtils.e("sen", "10设置UserInfo正在退出,默认提交第一个");
            getPresenter().updateUserType(bmobUserId, mUserTypeList.get(0));
        } else {
            LogUtils.e("sen", "11设置UserInfo");
            getPresenter().updateUserType(bmobUserId, mUserTypeList.get(currentChoose));
        }

    }

    /**
     * 登录失败
     */
    @Override
    public void loadUserFaild() {
        //登录失败
        //但是如果用户的点击以后设置，需要退从的话，那么就去登陆的页面
        if (isFutureSetting) {
            LogUtils.e("sen", "9 loginUser");
            EventBus.getDefault().post(new UserAccoutLoginRes().setLoginSuccess(false).setPhone(mAccoutRsigister.getUser().phone.get()));
            mActivity.finish();
            mActivity.overridePendingTransition(0, 0);
        }
    }

    @Override
    public void updateUserInfoFailed() {
        if (isFutureSetting) {
            //正在退出
            LogUtils.e("sen", "updateUserInfoFailed");
            EventBus.getDefault().post(new UserAccoutLoginRes().setLoginSuccess(true));
            mActivity.finish();
            mActivity.overridePendingTransition(0, 0);
        } else {
            showToastMsg(ResourceUtils.getString(R.string.net_exception));
        }

    }

    @Override
    public void updateUserInfoSuccess() {
        LogUtils.e("sen", "12更新成功updateUserInfoSuccess");
        EventBus.getDefault().post(new UserAccoutLoginRes().setLoginSuccess(true));
        mActivity.finish();
        mActivity.overridePendingTransition(0, 0);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventResigister(AccoutRsigisterBean bean) {
        LogUtils.e("sen", "1-onEventResigister");
        mAccoutRsigister = bean;
        getPresenter().saveUserToBmob(mAccoutRsigister.getUser());
    }

    @OnClick(R.id.btFinish)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btFinish:
                if (TextUtils.isEmpty(bmobUserId)) {
                    if (mAccoutRsigister != null && mAccoutRsigister.getUser() != null) {
                        LogUtils.e("sen", "7 to get bmobUserId" + bmobUserId);
                        getPresenter().saveUserToBmob(mAccoutRsigister.getUser());
                    }
                    return;
                }
                LogUtils.e("sen", "6bmobUserId" + bmobUserId);
                if (currentChoose != -1) {
                    //首先登录
                    if (isLoginSuccess) {
                        LogUtils.e("sen", "8 isLoginSuccess" + bmobUserId);
                        getPresenter().updateUserType(bmobUserId, mUserTypeList.get(currentChoose));
                    } else {
                        LogUtils.e("sen", "9 loginUser");
                        loginUser();
                    }
                }
                break;
        }
    }

    /**
     * 先登录用户
     */
    private void loginUser() {
        if (mAccoutRsigister != null && mAccoutRsigister.getUser() != null) {
            getPresenter().loadUserAccout(mAccoutRsigister.getUser().phone.get(), mAccoutRsigister.getPassword());
        }
    }

    //退出用户设置，那么得，先登录
    public void exitUserSetting() {
        isFutureSetting = true;
        showToastMsg(ResourceUtils.getString(R.string.str_send_wait));
        loginUser();
    }
}
