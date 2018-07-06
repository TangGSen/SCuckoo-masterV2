package com.ourcompany.fragment.message;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.imsdk.model.IMConversation;
import com.ourcompany.R;
import com.ourcompany.activity.imui.friends.FriendRequestActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.ChatFragPresenter;
import com.ourcompany.utils.DateUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.ChatFrameView;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    : 最近聊天，会话功能页面
 */

public class ChatFragment extends MvpFragment<ChatFrameView, ChatFragPresenter> implements ChatFrameView {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.layout_new_friend)
    LinearLayout layoutNewFriend;
    @BindView(R.id.tvNewFriendCount)
    TextView tvNewFriendCount;
    private List<IMConversation> mMessages = new ArrayList<>();
    private RecycleCommonAdapter<IMConversation> recycleCommonAdapter;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void initView(View view) {
        super.initView(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider, 5));
        recycleCommonAdapter = new RecycleCommonAdapter<IMConversation>(
                MApplication.mContext, mMessages, R.layout.layout_item_message_chat) {
            @Override
            public void bindItemData(SViewHolder holder, IMConversation itemData, int position) {
                holder.setText(R.id.tvUserName, itemData.getOtherInfo().getNickname());
                holder.setText(R.id.tvMessage, getPresenter().getMessage(itemData.getLastMessage()));
                holder.setText(R.id.tvTime, DateUtils.getDateFormat(itemData.getCreateTime(), ResourceUtils.getString(R.string.date_format_HHmm)));
            }
        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                getPresenter().refreshData();
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();
        //先加载会话
        getPresenter().initIMReceiver();
        getPresenter().getNewFriendCount();
        getPresenter().refreshData();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_message_chat;
    }

    @Override
    protected ChatFrameView bindView() {
        return this;
    }

    @Override
    protected ChatFragPresenter bindPresenter() {
        return new ChatFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }


    @Override
    public void showLoadingFailed() {
        ToastUtils.showSimpleToast("showLoadingFailed");
        mRefreshLayout.setEnabled(true);
        if (mRefreshLayout.isRefreshing()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            }, 1000);

        }
    }


    @Override
    public void showContentView(List<IMConversation> get) {
        if (get != null && get.size() > 0) {
            mMessages.addAll(get);
            recycleCommonAdapter.notifyDataSetChanged();
        } else {
            showToastMsg("没有数据");
        }
        closeRefreshLayout();
    }

    @Override
    public void showContentView(List<IMConversation> newItem, Map<Integer, IMConversation> updateItem) {
        for (Map.Entry<Integer, IMConversation> entry : updateItem.entrySet()) {
            mMessages.set(entry.getKey(), entry.getValue());
            recycleCommonAdapter.notifyItemChanged(entry.getKey());
        }
        if (newItem.size() > 0) {
            int startIndex = mMessages.size();
            mMessages.addAll(newItem);
            int endIndex = mMessages.size();
            recycleCommonAdapter.notifyItemRangeChanged(startIndex, endIndex);
        }
        closeRefreshLayout();

    }

    private void closeRefreshLayout() {
        mRefreshLayout.setEnabled(true);
        if (mRefreshLayout.isRefreshing()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            }, 1000);

        }
    }

    @Override
    public void showNewFriendCount(int count) {
        tvNewFriendCount.setText("" + count);
    }

    @Override
    public void mergeData(List<IMConversation> get) {
        getPresenter().mergeData(mMessages, get);
    }

    @Override
    public void showNotData() {
        closeRefreshLayout();
    }


    @OnClick(R.id.layout_new_friend)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_new_friend:
                Intent intent = new Intent(mActivity, FriendRequestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
