package com.ourcompany.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.ourcompany.R;
import com.ourcompany.activity.imui.UserInfoActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.Comment;
import com.ourcompany.presenter.fragment.CommentsPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TimeFormatUtil;
import com.ourcompany.view.fragment.CommentFragView;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
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
 * On     : 2018/3/29 14:21
 * Des    : 评论
 */

public class CommentFragment extends MvpFragment<CommentFragView, CommentsPresenter> implements CommentFragView {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    Unbinder unbinder;
    private int currentIndex;
    private RecycleCommonAdapter<Comment> recycleCommonAdapter;
    private List<Comment> mCommentList = new ArrayList<>();
    private String mPostId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_comment;
    }
    @Override
    protected void initStateLayout(View view) {
        super.initStateLayout(view);
        //初始化状态的布局
        View emptyView = getLayoutInflater().inflate(R.layout.layout_state_empty_with_retry, (ViewGroup)view.findViewById(android.R.id.content), false);
        layoutState.setEmptyView(emptyView);
        layoutState.changeState(StateFrameLayout.LOADING);

    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mPostId = bundle.getString(Constant.KEY_POST_ID);
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
        refreshLayout.setEnableRefresh(false);
      //  recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider_padding, 2));
        recycleCommonAdapter = new RecycleCommonAdapter<Comment>(
                MApplication.mContext, mCommentList, R.layout.layout_item_comment) {
            @Override
            public void bindItemData(SViewHolder holder, final Comment itemData, int position) {
                holder.setText(R.id.tvUserName, itemData.getUser() == null ? ResourceUtils.getString(R.string.defualt_userName) : TextUtils.isEmpty(itemData.getUser().getUserName()) ? ResourceUtils.getString(R.string.defualt_userName) : itemData.getUser().getUserName());
                holder.setText(R.id.tvContent, itemData.getContent());
                holder.setText(R.id.tvTime, TimeFormatUtil.getIntervalFormString(itemData.getCreatedAt()));
                holder.setImage(R.id.imgUser, itemData.getUser() == null ? "" : itemData.getUser().getImageUrl());
                holder.getView(R.id.imgUser).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userId = "";
                        if (itemData.getUser() != null && !TextUtils.isEmpty(itemData.getUser().getUserId())) {
                            userId = itemData.getUser().getUserId();
                        }
                        UserInfoActivity.gotoThis(mActivity, false, userId);
                    }
                });
            }


        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
            }
        });


        refreshLayout.setRefreshHeader(new MHeader(mActivity).setEnableLastTime(false).setTextSizeTitle(14).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(100));
        refreshLayout.setRefreshFooter(new MFooter(mActivity).setTextSizeTitle(14).setSpinnerStyle(SpinnerStyle.Scale).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(100));
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //按照时间来加载，刷新是加载比第一个更晚的时间，
                if (mCommentList != null && mCommentList.size() > 0) {
                    getPresenter().getDataOnReFresh(mCommentList.get(0).getCreatedAt(), mPostId);
                } else {
                    //传空默认传当前时间
                    getPresenter().getDataOnReFresh("", mPostId);
                }

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++currentIndex;
                getPresenter().getDataOnLoadMore(currentIndex, mPostId);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        //加载评论
        getPresenter().loadComments(currentIndex, mPostId, false);
    }

    @Override
    protected CommentFragView bindView() {
        return this;
    }


    @Override
    protected CommentsPresenter bindPresenter() {
        return new CommentsPresenter(MApplication.mContext);
    }


    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommeSubmit(Comment comment) {
        layoutState.changeState(StateFrameLayout.SUCCESS);
        recycleCommonAdapter.addData(comment,0);
    }
    @Override
    public void showContentView(List<Comment> list) {
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
    public void showToastMsg(String string) {

    }

    @Override
    public void showOnReflsh(List<Comment> list) {
        refreshLayout.finishRefresh(0, true);
        mCommentList.addAll(0, list);
        recycleCommonAdapter.notifyItemRangeChanged(0, list.size());

    }

    @Override
    public void showOnReflshNoNewsData() {
        refreshLayout.finishRefresh(0, true);
        showToastMsg(ResourceUtils.getString(R.string.str_reflsh_no_new_data));

    }

    @Override
    public void showOnReflshError() {
        refreshLayout.finishRefresh(0, false);
        showToastMsg(ResourceUtils.getString(R.string.str_reflesh_error));
    }

    @Override
    public void showOnLoadError() {
        refreshLayout.finishLoadMore(0, false, false);
        showToastMsg(ResourceUtils.getString(R.string.str_onload_error));

    }

    @Override
    public void showDiffResult(DiffUtil.DiffResult result) {
        result.dispatchUpdatesTo(recycleCommonAdapter);
    }

    @Override
    public void showOnLoadFinish() {
        //如果是没有更新的数据时需要停止刷新半分钟，防止频繁的刷新
        refreshLayout.finishLoadMore(0, true, false);
    }

    @Override
    public void showOnloadMoreNoData() {
        refreshLayout.finishLoadMore(0, true, true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
