package com.ourcompany.fragment.tab_mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.Coupon;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.presenter.activity.CouponManagerActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.CouponManagerActView;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;
import com.ourcompany.widget.recycleview.headfooter.MFooter;
import com.ourcompany.widget.recycleview.headfooter.MHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 下午2:08
 * Des    :CouponManagerFragment 包含了已过期的和未过期的，
 */
public class CouponManagerFragmentV1 extends MvpFragment<CouponManagerActView, CouponManagerActPresenter> implements CouponManagerActView {


    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;

    Unbinder unbinder;
    private RecycleCommonAdapter<Coupon> recycleCommonAdapter;
    private List<Coupon> mUserList = new ArrayList<>();
    private int currentIndex;
    public static final int TYPE_NOT_OVERDUE=0;
    public static final int TYPE_OVERDUE=1;
    public static final String KEY_TYPE = "type";
    public int currentType = -1;
    //所属的公司的，或者是个人的优惠券，如果为空那么直接就为加载失败或者为空


    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        currentType = bundle.getInt(KEY_TYPE,currentType);
    }

    @Override
    protected void initStateLayout(View view) {
        super.initStateLayout(view);
        //初始化状态的布局
        layoutState.setLoadingViewLayoutId(R.layout.layout_loading_cencter);
        layoutState.setEmptyViewLayoutId(R.layout.layout_state_empty_with_retry);
        layoutState.changeState(StateFrameLayout.LOADING);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        initRecycleView();
        EventBus.getDefault().register(this);
    }


    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //解决嵌套在NestedScrollView 的滑动不顺的问题1
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        //解决嵌套在NestedScrollView 的滑动不顺的问题2
        recycleview.setNestedScrollingEnabled(true);

        refreshLayout.setEnableRefresh(true);
        recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider_padding, 1));

        recycleCommonAdapter = new RecycleCommonAdapter<Coupon>(
                MApplication.mContext, mUserList, R.layout.layout_item_coupon) {
            @Override
            public void bindItemData(SViewHolder holder, final Coupon itemData, int position) {
                holder.setText(R.id.tvName, itemData.getName());
                holder.setText(R.id.tvCouponMoney, "￥" + itemData.getCouponMoney());

                holder.setText(R.id.tvTime, itemData.getTimeInfo());
              //  holder.getView(R.id.groupState).setVisibility(View.VISIBLE);
                if(currentType==TYPE_OVERDUE){
                    //不要设置为Gone, 因为有点击领取这个字，作为高度一致
                    //加载过期的
                    holder.getView(R.id.rootView).setBackgroundResource(R.drawable.bg_gradient_tab4);
                    holder.setText(R.id.tvStates, ResourceUtils.getString(R.string.str_click_see));
                    ((ImageView)holder.getView(R.id.imageOverdue)).setImageDrawable(ResourceUtils.getDrawable(R.drawable.ic_overdue));
                }else{
                    holder.setText(R.id.tvStates, ResourceUtils.getString(R.string.str_click_edite));
                }
            }
        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                //  UserClassifyDetailActivity.gotoThis(CouponManagerActivity.this,mUserList.get(position));
            }
        });


        refreshLayout.setRefreshHeader(new MHeader(mActivity).setEnableLastTime(false).setTextSizeTitle(12).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(100));
        refreshLayout.setRefreshFooter(new MFooter(mActivity).setTextSizeTitle(12).setSpinnerStyle(SpinnerStyle.Scale).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(100));
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //按照时间来加载，刷新是加载比第一个更晚的时间，
                if (mUserList != null && mUserList.size() > 0) {
                    getPresenter().getDataOnReFresh(mUserList.get(0).getCreatedAt(),
                            mUserList.get(0).getObjectId(),
                            MServiceManager.getInstance().getLocalThirdPartyId(),currentType);
                }
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++currentIndex;
                getPresenter().getDataOnLoadMore(currentIndex, MServiceManager.getInstance().getLocalThirdPartyId(),currentType);
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getData(currentIndex, MServiceManager.getInstance().getLocalThirdPartyId(), false,currentType);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCouponSubmit(Coupon coupon) {
        layoutState.changeState(StateFrameLayout.SUCCESS);
        recycleCommonAdapter.addData(coupon,0);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.include_with_reflesh_recycleview;
    }

    @Override
    protected CouponManagerActView bindView() {
        return this;
    }

    @Override
    protected CouponManagerActPresenter bindPresenter() {
        return new CouponManagerActPresenter(MApplication.mContext);
    }

    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
    }

    @Override
    public void showContentView(List<Coupon> list) {
        recycleCommonAdapter.addDatasInLast(list);
        layoutState.changeState(StateFrameLayout.SUCCESS);
    }


    @Override
    public void showLoadView() {
        layoutState.changeState(StateFrameLayout.LOADING);

    }

    @Override
    public void showErrorView() {

        showToastMsg(ResourceUtils.getString(R.string.load_data_fail));
    }

    @Override
    public void showOnReflsh(List<Coupon> list) {
        refreshLayout.finishRefresh(Constant.CLOSE_LOAD_TIME, true);
        recycleCommonAdapter.addDatasInFirst(0, list);
        recycleview.scrollToPosition(0);

    }

    @Override
    public void showOnReflshNoNewsData() {
        showToastMsg(ResourceUtils.getString(R.string.str_reflsh_no_new_data));
        refreshLayout.finishRefresh(Constant.CLOSE_LOAD_TIME, true);
    }

    @Override
    public void showOnReflshError() {
        showToastMsg(ResourceUtils.getString(R.string.str_reflesh_error));
        refreshLayout.finishRefresh(Constant.CLOSE_LOAD_TIME, false);

    }

    @Override
    public void showOnLoadError() {
        refreshLayout.finishLoadMore(Constant.CLOSE_LOAD_TIME, false, false);
        showToastMsg(ResourceUtils.getString(R.string.str_onload_error));

    }

    @Override
    public void showOnLoadFinish() {
        //如果是没有更新的数据时需要停止刷新半分钟，防止频繁的刷新
        refreshLayout.finishLoadMore(Constant.CLOSE_LOAD_TIME, true, false);
    }

    @Override
    public void showOnloadMoreNoData() {
        refreshLayout.finishLoadMore(Constant.CLOSE_LOAD_TIME, true, true);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
