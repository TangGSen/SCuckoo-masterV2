package com.ourcompany.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.ourcompany.R;
import com.ourcompany.activity.ImagesPreViewActvitity;
import com.ourcompany.activity.PostDetailActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.Post;
import com.ourcompany.presenter.fragment.UserDynamicFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TimeFormatUtil;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.FoundNewsFragmentView;
import com.ourcompany.widget.NineGridlayout;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleMultiTypeAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.headfooter.MFooter;
import com.ourcompany.widget.recycleview.headfooter.MHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/6 22:39
 * Des    : 显示最新的发布信息
 */

public class UserDynamicFragment extends MvpFragment<FoundNewsFragmentView, UserDynamicFragPresenter> implements FoundNewsFragmentView {
    private static int NINELAYOUTTYPE_TEXT = 0;
    private static int NINELAYOUTTYPE_IMAGES = 1;


    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    Unbinder unbinder1;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private RecycleMultiTypeAdapter recycleCommonAdapter;
    private List<Post> mPostList = new ArrayList<>();
    private int currentIndex;
    private String mUserId;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mUserId = bundle.getString(Constant.KEY_USER_ID, "");
    }

    @Override
    public void showToastMsg(String string) {
        if (!TextUtils.isEmpty(string)) {
            ToastUtils.showSimpleToast(string);
        }

    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        recycleCommonAdapter = new RecycleMultiTypeAdapter<Post>(
                MApplication.mContext, mPostList) {
            @Override
            protected int setViewType(int position) {
                if (mPostList.get(position).getImageUrls() == null || mPostList.get(position).getImageUrls().size() <= 0) {
                    return NINELAYOUTTYPE_TEXT;
                } else {
                    return NINELAYOUTTYPE_IMAGES;
                }
            }

            @Override
            protected int getLayoutId(int viewType) {
                if (viewType == NINELAYOUTTYPE_TEXT) {
                    return R.layout.layout_item_user_dynic_text;
                } else if (viewType == NINELAYOUTTYPE_IMAGES) {
                    return R.layout.layout_item_user_dynic_images_cardview;
                }
                return R.layout.layout_item_user_dynic_images;
            }

            @Override
            public void bindItemData(SViewHolder holder, final Post itemData, final int position) {

                holder.setText(R.id.tvContent, itemData.getContent());
                holder.setText(R.id.tvTime, TimeFormatUtil.getIntervalFormString(itemData.getCreatedAt()));
                holder.setText(R.id.likes, itemData.getLikeCount() != null ? itemData.getLikeCount() + "" : "0");
                holder.setText(R.id.comments, itemData.getCommentCount() != null ? itemData.getCommentCount() + "" : "0");
                if (getItemViewType(position) == NINELAYOUTTYPE_IMAGES) {
                    ((NineGridlayout) holder.getView(R.id.ivNineLayout)).setImagesData(itemData.getImageUrls(), position);
                    ((NineGridlayout) holder.getView(R.id.ivNineLayout)).setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            ImagesPreViewActvitity.gotoThis(mActivity, (ArrayList<String>) itemData.getImageUrls(), index);
                        }
                    });
                }

            }


        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                PostDetailActivity.gotoThis(mActivity, mPostList.get(position),position);
            }
        });
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //按照时间来加载，刷新是加载比第一个更晚的时间，
                if (mPostList != null && mPostList.size() > 0) {
                    getPresenter().getDataOnReFresh(mPostList.get(0).getCreatedAt(), mPostList.get(0).getObjectId());
                } else {
                    //传空默认传当前时间
                    getPresenter().getDataOnReFresh("", "");
                }

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++currentIndex;
                getPresenter().getDataOnLoadMore(currentIndex, mUserId);
            }
        });
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setRefreshHeader(new MHeader(mActivity).setEnableLastTime(false).setTextSizeTitle(14).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)));
        refreshLayout.setRefreshFooter(new MFooter(mActivity).setTextSizeTitle(14).setSpinnerStyle(SpinnerStyle.Scale).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)));
    }

    @Override
    protected void initData() {
        super.initData();
        //开始时以当前的时间来加载最新的

        getPresenter().getData(currentIndex, false, mUserId);
    }

    @Override
    protected void initStateLayout(View view) {
        super.initStateLayout(view);
        //初始化状态的布局
        View emptyView = getLayoutInflater().inflate(R.layout.layout_state_empty_with_retry, (ViewGroup) mActivity.findViewById(android.R.id.content), false);
        layoutState.setEmptyView(emptyView);
        layoutState.setLoadingViewLayoutId(R.layout.layout_loading_top);
        layoutState.changeState(StateFrameLayout.LOADING);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_news_found;
    }

    @Override
    protected FoundNewsFragmentView bindView() {
        return this;
    }

    @Override
    protected UserDynamicFragPresenter bindPresenter() {
        return new UserDynamicFragPresenter(MApplication.mContext);
    }


    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
        closeReflshView();
    }

    @Override
    public void showContentView(List<Post> list) {
        int start = mPostList.size();
        recycleCommonAdapter.addDatasInLast(list);
        layoutState.changeState(StateFrameLayout.SUCCESS);
    }

    private void closeReflshView() {
//        mRefreshLayout.setEnabled(true);
//        if (mRefreshLayout.isRefreshing()) {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mRefreshLayout.setRefreshing(false);
//                }
//            }, 1000);
//
//        }
    }

    @Override
    public void showLoadView() {
        layoutState.changeState(StateFrameLayout.LOADING);
        closeReflshView();
    }

    @Override
    public void showErrorView() {
        closeReflshView();
        showToastMsg(ResourceUtils.getString(R.string.load_data_fail));
    }

    @Override
    public void showOnReflsh(List<Post> list) {
        refreshLayout.finishRefresh(50, true);
        recycleCommonAdapter.addDatasInFirst(0, list);
        recycleview.scrollToPosition(0);

    }

    @Override
    public void showOnReflshNoNewsData() {
        LogUtils.e("sen", "showOnReflshNoNewsData");
        showToastMsg(ResourceUtils.getString(R.string.str_reflsh_no_new_data));
        refreshLayout.finishRefresh(50, true);
    }

    @Override
    public void showOnReflshError() {
        showToastMsg(ResourceUtils.getString(R.string.str_reflesh_error));
        refreshLayout.finishRefresh(50, false);

    }

    @Override
    public void showOnLoadError() {
        refreshLayout.finishLoadMore(50, false, false);
        showToastMsg(ResourceUtils.getString(R.string.str_onload_error));

    }

    @Override
    public void showOnLoadFinish() {
        //如果是没有更新的数据时需要停止刷新半分钟，防止频繁的刷新
        refreshLayout.finishLoadMore(50, true, false);
    }

    @Override
    public void showOnloadMoreNoData() {
        refreshLayout.finishLoadMore(50, true, true);
    }


}
