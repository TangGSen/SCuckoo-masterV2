package com.ourcompany.fragment;

import android.os.Handler;
import android.os.Looper;
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
import com.ourcompany.presenter.fragment.FoundNewsFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TimeFormatUtil;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.FoundNewsFragmentView;
import com.ourcompany.widget.NineGridlayout;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleMultiTypeAdapter;
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
 * On     : 2018/3/6 22:39
 * Des    : 显示最新的发布信息
 */

public class FoundNewsFragment extends MvpFragment<FoundNewsFragmentView, FoundNewsFragPresenter> implements FoundNewsFragmentView {
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
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void showToastMsg(String string) {
        if (!TextUtils.isEmpty(string)) {
            ToastUtils.showSimpleToast(string);
        }

    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        EventBus.getDefault().register(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider_padding, 1));
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
                    return R.layout.layout_item_post_text;
                } else if (viewType == NINELAYOUTTYPE_IMAGES) {
                    return R.layout.layout_item_post_images;
                }
                return R.layout.layout_item_post_images;
            }

            @Override
            public void bindItemData(SViewHolder holder, final Post itemData, final int position) {

                holder.setText(R.id.tvUserName, itemData.getUser() == null ? ResourceUtils.getString(R.string.defualt_userName) : TextUtils.isEmpty(itemData.getUser().getUserName()) ? ResourceUtils.getString(R.string.defualt_userName) : itemData.getUser().getUserName());
                holder.setText(R.id.tvContent, itemData.getContent());
                holder.setText(R.id.tvTime, TimeFormatUtil.getIntervalFormString(itemData.getCreatedAt()));
                holder.setImage(R.id.imgUser, itemData.getUser() == null ? "" : itemData.getUser().getImageUrl());
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

            @Override
            public void setOnItemClickBg(View itemView) {
                itemView.setBackground(ResourceUtils.getDrawable(R.drawable.selector_click_bg));
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
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //按照时间来加载，刷新是加载比第一个更晚的时间，
                if (mPostList != null && mPostList.size() > 0) {
                    getPresenter().getDataOnReFresh(mPostList.get(0).getCreatedAt(),mPostList.get(0).getObjectId());
                } else {
                    //传空默认传当前时间
                    getPresenter().getDataOnReFresh("","");
                }

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++currentIndex;
                getPresenter().getDataOnLoadMore(currentIndex);
            }
        });
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setRefreshHeader(new MHeader(mActivity).setEnableLastTime(false).setTextSizeTitle(14).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(0));
        refreshLayout.setRefreshFooter(new MFooter(mActivity).setTextSizeTitle(14).setSpinnerStyle(SpinnerStyle.Scale).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(0));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostSubmit(Post post) {
        if(post==null && layoutState!=null && recycleCommonAdapter!=null){
            return;
        }
        layoutState.changeState(StateFrameLayout.SUCCESS);
        recycleCommonAdapter.addData(post,0);
    }




    @Override
    protected void initData() {
        super.initData();
        //开始时以当前的时间来加载最新的
        getPresenter().getData(currentIndex, false);
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
    protected int getLayoutId() {
        return R.layout.fragment_layout_news_found;
    }

    @Override
    protected FoundNewsFragmentView bindView() {
        return this;
    }

    @Override
    protected FoundNewsFragPresenter bindPresenter() {
        return new FoundNewsFragPresenter(MApplication.mContext);
    }


    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
        closeReflshView();
    }

    @Override
    public void showContentView(List<Post> list) {
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
