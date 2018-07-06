package com.ourcompany.fragment.message;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mob.ums.User;
import com.ourcompany.R;
import com.ourcompany.activity.imui.SearchActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.ChatContacts;
import com.ourcompany.presenter.fragment.ContactsFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.MessageContactsFrameView;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    : 最近聊天，联系人页面
 */



public class MessageContactsFragment extends MvpFragment<MessageContactsFrameView, ContactsFragPresenter> implements MessageContactsFrameView {
    @BindView(R.id.recycleview)
    RecyclerView mRecycleview;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.btnSerach)
    TextView btnSerach;
    Unbinder unbinder;
    private RecycleCommonAdapter recycleCommonAdapter;
    private List<User> imUserList = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int currentLoading = 0;

    @Override
    protected void initView(View view) {
        super.initView(view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(linearLayoutManager);
        mRecycleview.setHasFixedSize(true);
        mRecycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider, 2));
        recycleCommonAdapter = new RecycleCommonAdapter<User>(
                MApplication.mContext, imUserList, R.layout.layout_item_message_chat) {
            @Override
            public void bindItemData(SViewHolder holder, User itemData, int position) {
                holder.setText(R.id.tvUserName, itemData.nickname.get());
                holder.setText(R.id.tvMessage, "我是Jsen");
                holder.setText(R.id.tvTime, "Time");
            }
        };
        mRecycleview.setAdapter(recycleCommonAdapter);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                currentLoading = 0;
                getPresenter().refreshData(currentLoading, Constant.IM_PAGESIZE);
            }
        });

        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().loadData(currentLoading, Constant.IM_PAGESIZE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_message_contacts;
    }

    @Override
    protected MessageContactsFrameView bindView() {
        return this;
    }

    @Override
    protected ContactsFragPresenter bindPresenter() {
        return new ContactsFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {

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
    public void showContentView(ChatContacts chatContacts) {

        if (chatContacts != null && chatContacts.getList() != null && chatContacts.getList().size() > 0) {
            showToastMsg("showContentView Message");
            List<User> userList = chatContacts.getList();
            imUserList.clear();
            imUserList.addAll(userList);
            recycleCommonAdapter.notifyDataSetChanged();
        } else {
            showToastMsg("没有数据");
        }
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


    @OnClick(R.id.btnSerach)
    public void onViewClicked(View view) {
        Intent intent = new Intent(mActivity, SearchActivity.class);
        startActivity(intent);
    }
}
