package com.ourcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.PostPositionChange;
import com.ourcompany.bean.bmob.Post;
import com.ourcompany.presenter.activity.CollectionsPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TimeFormatUtil;
import com.ourcompany.view.activity.CollectionActView;
import com.ourcompany.widget.NineGridlayout;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleMultiTypeAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;
import com.ourcompany.widget.recycleview.headfooter.MFooter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/29 14:21
 * Des    : 某个用户的收藏列表
 */

public class CollectionActivity extends MvpActivity<CollectionActView, CollectionsPresenter> implements CollectionActView {
    private static final String KEY_INTENT = "key_intent";
    private static final String KEY_BUNDLE = "key_bundle";
    private static final String KEY_BUNDLE_TOOLNAME = "key_bundle_toolname";

    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    private int currentIndex;
    private RecycleMultiTypeAdapter recycleCommonAdapter;
    private List<Post> mPostList = new ArrayList<>();
    private String mUserObjecteId;
    private static int NINELAYOUTTYPE_TEXT = 0;
    private static int NINELAYOUTTYPE_IMAGES = 1;
    private String mToolbarName;
    private PostPositionChange postChange;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_collections;
    }

    @Override
    protected void initStateLayout() {
        super.initStateLayout();
        //初始化状态的布局
        View emptyView = getLayoutInflater().inflate(R.layout.layout_state_empty_with_no_retry, (ViewGroup) findViewById(android.R.id.content), false);
        TextView tvTip = emptyView.findViewById(R.id.tv_empty_tip);
        tvTip.setText(ResourceUtils.getString(R.string.str_empty_collection));
        layoutState.setEmptyView(emptyView);
        layoutState.changeState(StateFrameLayout.LOADING);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(postChange!=null && recycleCommonAdapter!=null && !postChange.isCollection()){
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recycleCommonAdapter.removeItem(postChange.getPosition());
                        postChange=null;
                        if(recycleCommonAdapter.getItemCount()==0){
                            layoutState.changeState(StateFrameLayout.EMPTY);
                        }
                    }
                },1000);
        }
    }

    /**
     * 这个是在Pose详情页，用户在收藏功能变化时，接受的
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectionChange(PostPositionChange change) {
        this.postChange = change;
    }


    public static void gotoThis(Context context, String userObjId,String toolbarName) {
       if(context==null){
           return;
       }
        Intent intent = new Intent(context, CollectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE, userObjId);
        bundle.putString(KEY_BUNDLE_TOOLNAME, toolbarName);
        intent.putExtra(KEY_INTENT, bundle);
        context.startActivity(intent);
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        Bundle bun = getIntent().getBundleExtra(KEY_INTENT);
        if (bun != null) {
            mUserObjecteId =  bun.getString(KEY_BUNDLE);
            mToolbarName =  bun.getString(KEY_BUNDLE_TOOLNAME);
        }
        return super.initArgs(bundle);

    }


    @Override
    protected void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        commonToolbar.setTitle(TextUtils.isEmpty(mToolbarName)?"":mToolbarName);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

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
                holder.setText(R.id.tvTime, TimeFormatUtil.getIntervalFormString(itemData.getCreatedAt())+"*****"+position);
                holder.setImage(R.id.imgUser, itemData.getUser() == null ? "" : itemData.getUser().getImageUrl());
                holder.setText(R.id.likes, itemData.getLikeCount() != null ? itemData.getLikeCount() + "" : "0");

                holder.setText(R.id.comments, itemData.getCommentCount() != null ? itemData.getCommentCount() + "" : "0");
                if (getItemViewType(position) == NINELAYOUTTYPE_IMAGES) {
                    ((NineGridlayout) holder.getView(R.id.ivNineLayout)).setImagesData(itemData.getImageUrls(), position);
                    ((NineGridlayout) holder.getView(R.id.ivNineLayout)).setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            ImagesPreViewActvitity.gotoThis(CollectionActivity.this, (ArrayList<String>) itemData.getImageUrls(), index);
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
                LogUtils.e("sen","***"+position+"*****"+mPostList.size());
                PostDetailActivity.gotoThis(CollectionActivity.this, mPostList.get(position),position);
            }
        });
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshFooter(new MFooter(this).setTextSizeTitle(14).setSpinnerStyle(SpinnerStyle.Scale).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)));
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++currentIndex;
                getPresenter().getDataOnLoadMore(currentIndex, mUserObjecteId);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //加载评论
        getPresenter().loadDatas(currentIndex, mUserObjecteId, false);
    }


    @Override
    protected CollectionActView bindView() {
        return this;
    }


    @Override
    protected CollectionsPresenter bindPresenter() {
        return new CollectionsPresenter(MApplication.mContext);
    }


    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
    }


    @Override
    public void showContentView(List<Post> list) {
        recycleCommonAdapter.addDatasInLast(list);
        layoutState.changeState(StateFrameLayout.SUCCESS);
        if(list.size()< Constant.IM_PAGESIZE){
            showOnloadMoreNoData();
        }
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


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
