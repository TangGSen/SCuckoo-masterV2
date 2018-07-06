package com.ourcompany.fragment.user_class_detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.ourcompany.R;
import com.ourcompany.activity.user_class.UserTeamCaseDetailActivity;
import com.ourcompany.activity.user_class.UserTeamMemeberDetailActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.TeamCase;
import com.ourcompany.presenter.fragment.UserTeamCasePresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.UserTeamCaseFragView;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.headfooter.MFooter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/5/15 14:24
 * Des    : 案例展示，全部，按最新的排序
 * 1.这个fragment 主要在案例中显示的话，就加载当前公司或个人的全部case mTeamMemberId 为空
 * 2.在teammember 中显示 只能搜索teammember 中的案例,mTeamMemberId 不为空
 */

public class UserTeamCaseFragment extends MvpFragment<UserTeamCaseFragView, UserTeamCasePresenter> implements UserTeamCaseFragView {


    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    private String mUserId;
    //如果mTeamMemberId 为空的话那么就搜索全部，如果不为空那么久只能搜team member 只属于它的
    private String mTeamMemberId;
    private int mCurrentIndex;
    private RecycleCommonAdapter<TeamCase> recycleCommonAdapter;
    private List<TeamCase> mTeamCaseList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_team_case;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        Bundle res = getArguments();
        if (res != null) {
            mUserId = bundle.getString(Constant.BMOB_SUSER_ID);
            mTeamMemberId = bundle.getString(Constant.BMOB_USER_TEAM_MEMBER);
        }
        super.initArgs(bundle);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //解决嵌套在NestedScrollView 的滑动不顺的问题1

        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int integer = (int) v.getTag(R.id.nine_layout_of_index);
                if (integer >= 0 && integer < mTeamCaseList.size()) {
                    UserTeamMemeberDetailActivity.gotoThis(mActivity, mTeamCaseList.get(integer).getTeamMember());
                }
            }
        };

        recycleCommonAdapter = new RecycleCommonAdapter<TeamCase>(
                MApplication.mContext, mTeamCaseList, R.layout.layout_item_team_case) {
            @Override
            public void bindItemData(SViewHolder holder, final TeamCase itemData, int position) {

                holder.setText(R.id.tvLabel, itemData.getStyleString());
                int size = itemData.getImageUrls() != null && itemData.getImageUrls().size() > 0 ? itemData.getImageUrls().size() : 0;
                if (size > 0) {
                    holder.setText(R.id.tvImageList, size + "");
                } else {
                    holder.getView(R.id.tvImageList).setVisibility(View.GONE);
                }

                //获取第一张来展示
                holder.setImageWithErrorImage(R.id.imageMain, itemData.getImageUrls() != null && itemData.getImageUrls().size() > 0 ? itemData.getImageUrls().get(0) : "",R.drawable.ic_loading_defualt);
                if (TextUtils.isEmpty(mTeamMemberId) && itemData.getTeamMember() != null) {
                    holder.setImage(R.id.imgUser, itemData.getTeamMember() != null ? itemData.getTeamMember().getMemberImage() : "");
                    holder.getView(R.id.imgUser).setTag(R.id.nine_layout_of_index, position);
                    holder.getView(R.id.imgUser).setOnClickListener(onClickListener);

                } else {
                    holder.getView(R.id.imgUser).setVisibility(View.GONE);
//                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.getView(R.id.tvLabel).getLayoutParams();
//                   // ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    int dip = DisplayUtils.dip2px(16);
//                    params.setMargins(dip, 0, 0, dip);
                    holder.getView(R.id.tvLabel).setPadding(0, 0, 0, dip);
                }
            }
        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {

                UserTeamCaseDetailActivity.gotoThis(mActivity,mTeamCaseList.get(position),mTeamMemberId);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mCurrentIndex;
                getPresenter().getDataOnLoadMore(mCurrentIndex, mUserId, mTeamMemberId);
            }
        });

        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshFooter(new MFooter(mActivity).setTextSizeTitle(14).setSpinnerStyle(SpinnerStyle.Scale).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(0));

    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getData(mCurrentIndex, mUserId, mTeamMemberId, true);

    }

    @Override
    protected UserTeamCaseFragView bindView() {
        return this;
    }

    @Override
    protected UserTeamCasePresenter bindPresenter() {
        return new UserTeamCasePresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {

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
    public void showErrorView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
    }

    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
    }

    @Override
    public void showOnloadMoreNoData() {
        refreshLayout.finishLoadMore(Constant.CLOSE_LOAD_TIME, true, true);
    }

    @Override
    public void showOnLoadFinish() {
        //如果是没有更新的数据时需要停止刷新半分钟，防止频繁的刷新
        refreshLayout.finishLoadMore(Constant.CLOSE_LOAD_TIME, true, false);
    }

    @Override
    public void showOnLoadError() {
        refreshLayout.finishLoadMore(Constant.CLOSE_LOAD_TIME, false, false);
        showToastMsg(ResourceUtils.getString(R.string.str_onload_error));
    }

    @Override
    public void showDataView(List<TeamCase> list) {
        recycleCommonAdapter.addDatasInLast(list);
        layoutState.changeState(StateFrameLayout.SUCCESS);
    }


}
