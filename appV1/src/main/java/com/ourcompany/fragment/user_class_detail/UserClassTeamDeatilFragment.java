package com.ourcompany.fragment.user_class_detail;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.user_class.UserTeamMemeberDetailActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.TeamMember;
import com.ourcompany.presenter.activity.UserClassTeamDetailFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.UserClassTeamDetailFragView;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/5/16 10:29
 * Des    : 公司或个人的team,详细过程
 */

public class UserClassTeamDeatilFragment extends MvpFragment<UserClassTeamDetailFragView, UserClassTeamDetailFragPresenter> implements UserClassTeamDetailFragView {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    @BindView(R.id.mConstraintLayout)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.title)
    TextView mTitle;
    Unbinder unbinder;
    private int mCurrentIndex;
    private int teamType;

    private RecycleCommonAdapter<TeamMember> recycleCommonAdapter;
    private List<TeamMember> mTeamMemberList = new ArrayList<>();
    private String mUserId;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoadingMore;
    private boolean isHasNotMoreData;

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_class_team_detail;
    }

    @Override
    protected UserClassTeamDetailFragView bindView() {
        return this;
    }

    @Override
    protected UserClassTeamDetailFragPresenter bindPresenter() {
        return new UserClassTeamDetailFragPresenter(MApplication.mContext);
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        Bundle res = getArguments();
        if (res != null) {
            teamType = res.getInt(Constant.KEY_TEAM_TYPE);
            mUserId = bundle.getString(Constant.BMOB_SUSER_ID);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        LogUtils.e("sen","sen");
        getPresenter().getData(mCurrentIndex,teamType,mUserId, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        //回调父Fragment 的方法
//                List<Fragment>list=UserClassTeamDeatilFragment.this.getFragmentManager().getFragments();
//                for(Fragment f:list){
//                    if(f!=null&&f instanceof UserClassTeamFragment){
//                        ((UserClassTeamFragment) f).setLayoutHeight(mRootView.getWidth(),mRootView.getHeight());
//                        break;
//                    }
//                }
        String text = " ";
        if (teamType == Constant.TEAM_TYPE_DESINGE) {
            text = ResourceUtils.getString(R.string.str_team_design);
        } else if (teamType == Constant.TEAM_TYPE_WORKER) {
            text = ResourceUtils.getString(R.string.str_team_worker);
        }
        mTitle.setText(text);
        mLinearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //解决嵌套在NestedScrollView 的滑动不顺的问题1

        recycleview.setLayoutManager(mLinearLayoutManager);
        recycleview.setHasFixedSize(true);
        recycleCommonAdapter = new RecycleCommonAdapter<TeamMember>(
                MApplication.mContext, mTeamMemberList, R.layout.layout_item_user_team_meneber) {
            @Override
            public void bindItemData(SViewHolder holder, final TeamMember itemData, int position) {
                holder.setText(R.id.tvUserName, TextUtils.isEmpty(itemData.getMemberName()) ?
                        ResourceUtils.getString(R.string.defualt_userName) : itemData.getMemberName());
                holder.setImage(R.id.imgUser, itemData.getMemberImage());

                holder.setText(R.id.otherInfoText,itemData.getOtherInfo());

            }


        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {

                UserTeamMemeberDetailActivity.gotoThis(mActivity,mTeamMemberList.get(position));
            }
        });

        recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition  == recycleCommonAdapter.getItemCount()-1) {
                    if(isHasNotMoreData){
                        LogUtils.e("sen","不需要刷新了");
                        return;
                    }
                    mCurrentIndex++;
                    if (!isLoadingMore ) {
                         isLoadingMore = true;
                         LogUtils.e("sen","准备加载更多");
                        getPresenter().getData(mCurrentIndex,teamType,mUserId, true);
                    }else{
                        LogUtils.e("sen","加载更多中");
                    }
                }
            }
        });
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
    public void showDataView(List<TeamMember> list) {
        LogUtils.e("sen","team menber before："+mTeamMemberList.size());
        recycleCommonAdapter.addDatasInLast(list);
        LogUtils.e("sen","team menber after："+mTeamMemberList.size());
        layoutState.changeState(StateFrameLayout.SUCCESS);
    }

    @Override
    public void showOnloadMoreNoData() {
        isHasNotMoreData= true;
        LogUtils.e("sen","没有更多数据了");
    }

    @Override
    public void showOnLoadFinish() {
        isLoadingMore = false;
    }
}
