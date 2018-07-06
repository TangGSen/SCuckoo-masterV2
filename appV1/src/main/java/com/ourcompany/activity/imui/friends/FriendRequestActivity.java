package com.ourcompany.activity.imui.friends;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mob.ums.FriendRequest;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.FriendRequestItem;
import com.ourcompany.presenter.activity.FriendRequestActPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.FriendRequestActvityView;
import com.ourcompany.widget.recycleview.commadapter.RecycleMultiTypeAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpActivity;

public class FriendRequestActivity extends MvpActivity<FriendRequestActvityView, FriendRequestActPresenter> implements FriendRequestActvityView {

    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    private RecycleMultiTypeAdapter<FriendRequestItem> recycleCommonAdapter;
    private List<FriendRequestItem> mFriendRequests = new ArrayList<>();
    private int currentPage;
    private AlertDialog alertDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_request;
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setTitle(ResourceUtils.getString(R.string.newFriend));
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider, 1));
        recycleCommonAdapter = new RecycleMultiTypeAdapter<FriendRequestItem>(
                MApplication.mContext, mFriendRequests) {
            @Override
            protected int setViewType(int position) {
                FriendRequestItem itemData = mFriendRequests.get(position);
                if (itemData.getFriendRequst().status.get().getStatus() == FriendRequest.RequestStatus.ACCEPT.getStatus()) {
                    return FriendRequest.RequestStatus.ACCEPT.getStatus();
                } else if (itemData.getFriendRequst().status.get().getStatus() == FriendRequest.RequestStatus.REFUSE.getStatus()) {
                    return FriendRequest.RequestStatus.REFUSE.getStatus();
                } else if (itemData.getFriendRequst().status.get().getStatus() == FriendRequest.RequestStatus.UNPROCESSED.getStatus()) {
                    return FriendRequest.RequestStatus.UNPROCESSED.getStatus();
                }
                return 0;

            }

            @Override
            protected int getLayoutId(int viewType) {
                if (viewType == FriendRequest.RequestStatus.ACCEPT.getStatus()) {
                    return R.layout.layout_item_friend_request_accpet;
                } else if (viewType == FriendRequest.RequestStatus.REFUSE.getStatus()) {
                    return R.layout.layout_item_friend_request_refuse;
                } else if (viewType == FriendRequest.RequestStatus.UNPROCESSED.getStatus()) {
                    return R.layout.layout_item_friend_request_undo;
                }
                return 0;
            }

            @Override
            public void bindItemData(final SViewHolder holder, FriendRequestItem itemData, final int position) {
                final int viewType = getItemViewType(position);
                holder.setText(R.id.tvUserName, itemData.getUserName());
                holder.setText(R.id.tvMessage, itemData.getFriendRequst().message.get());
                if (viewType == FriendRequest.RequestStatus.ACCEPT.getStatus()) {
                    holder.setText(R.id.state, ResourceUtils.getString(R.string.friend_state_accept));
                    holder.setViewEnable(R.id.state, false);
                } else if (viewType == FriendRequest.RequestStatus.REFUSE.getStatus()) {
                    holder.setText(R.id.state, ResourceUtils.getString(R.string.friend_state_refuse));
                    holder.setViewEnable(R.id.state, false);
                } else if (viewType == FriendRequest.RequestStatus.UNPROCESSED.getStatus()) {
//                    holder.setText(R.id.state, ResourceUtils.getString(R.string.friend_state_unprocessed));
//                    holder.setViewEnable(R.id.state, true);
                    holder.getView(R.id.imageState).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //showPopupWindow( holder.getView(R.id.imageState));
                            showRequestReply(mFriendRequests.get(position), position);
                        }
                    });
                }

//                findViewById(R.id.state).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (viewType == FriendRequest.RequestStatus.UNPROCESSED.getStatus()) {
//                            ToastUtils.showSimpleToast("UNPROCESSED");
//                        }
//                    }
//                });
            }
        };
        recycleview.setAdapter(recycleCommonAdapter);

    }


    private void showPopupWindow(View locationVeiw) {
        View mView = View.inflate(FriendRequestActivity.this, R.layout.layout_popup_friend_reuqest, null);
        PopupWindow mpopupWindow = new PopupWindow(locationVeiw,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView mCamera = mView.findViewById(R.id.refuse);
        mpopupWindow.setContentView(mView);
        //点击事件
        mpopupWindow.setOutsideTouchable(true);
        //弹出窗体可点击
        mpopupWindow.setTouchable(true);
        mpopupWindow.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x5e000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        mpopupWindow.setBackgroundDrawable(dw);
        int[] location = new int[2];
        locationVeiw.getLocationInWindow(location);


        //显示PopupWindow
        mpopupWindow.showAtLocation(locationVeiw, Gravity.RIGHT, 0, 0);
    }

    public void showRequestReply(final FriendRequestItem item, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.friend_request_title);
        builder.setMessage(String.format(ResourceUtils.getString(R.string.friend_request_from), item.getUserName()));
        builder.setNegativeButton(R.string.friend_request_refuse, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPresenter().replyFriendRequesting(position, item.getFriendRequst().requesterId.get(), false);
            }
        });
        builder.setPositiveButton(R.string.friend_request_accpet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPresenter().replyFriendRequesting(position, item.getFriendRequst().requesterId.get(), true);
            }
        });
        builder.setNeutralButton(R.string.friend_request_undo, null);
        alertDialog = builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getPresenter().getAddFriendRequests(currentPage);
    }

    @Override
    protected FriendRequestActvityView bindView() {
        return this;
    }

    @Override
    protected FriendRequestActPresenter bindPresenter() {
        return new FriendRequestActPresenter(this);
    }


    @Override
    public void showToastMsg(String string) {

    }


    @Override
    public void showContent(List<FriendRequestItem> friendRequests) {
        int start = mFriendRequests.size();
        mFriendRequests.addAll(friendRequests);
        int end = mFriendRequests.size();
        if (currentPage == 0) {
            recycleCommonAdapter.notifyDataSetChanged();
        } else {
            recycleCommonAdapter.notifyItemRangeChanged(start, end);
        }


    }

    @Override
    public void showErrorView() {
        showToastMsg(ResourceUtils.getString(R.string.unkown_error));
    }

    @Override
    public void showEptyView() {
        showToastMsg(ResourceUtils.getString(R.string.load_data_fail));
    }

    @Override
    public void friendRequestAccpetOk(int position) {
        mFriendRequests.get(position).getFriendRequst().status.set(FriendRequest.RequestStatus.ACCEPT);
        recycleCommonAdapter.notifyItemChanged(position);
    }

    @Override
    public void friendRequestRefuseOk(int position) {
        mFriendRequests.get(position).getFriendRequst().status.set(FriendRequest.RequestStatus.REFUSE);
        recycleCommonAdapter.notifyItemChanged(position);
    }

    @Override
    public void friendRequestFaild() {
        showToastMsg(ResourceUtils.getString(R.string.option_faild_and_try));
    }
}
