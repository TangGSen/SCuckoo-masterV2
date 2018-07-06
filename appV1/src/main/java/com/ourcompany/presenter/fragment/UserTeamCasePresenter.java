package com.ourcompany.presenter.fragment;

import android.content.Context;
import android.text.TextUtils;

import com.ourcompany.bean.bmob.TeamCase;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.view.fragment.UserTeamCaseFragView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Created by Administrator on 2017/8/20.
 */

public class UserTeamCasePresenter extends MvpBasePresenter<UserTeamCaseFragView> {

    public UserTeamCasePresenter(Context context) {
        super(context);
    }

    /**
     * @param start
     * @param userId //属于个人或这个公司的
     * @param isLoadMore
     */
    public void getData(final int start, String userId, String teamMemberId, final boolean isLoadMore) {
        if (TextUtils.isEmpty(userId)) {
            getView().showErrorView();
            return;
        }
        BmobQuery<TeamCase> query = new BmobQuery<>();

        List<BmobQuery<TeamCase>> queryList = new ArrayList<>();
        BmobQuery<TeamCase> queryUserId = new BmobQuery<>();
        //根据归属userId查询
        queryUserId.addWhereEqualTo(Constant.BMOB_SUSER_ID, userId);
        queryList.add(queryUserId);

        //根据归属TeamId查询
        if(!TextUtils.isEmpty(teamMemberId)){
            //不需要查询teammeber
            BmobQuery<TeamCase> queryTeamMemberId = new BmobQuery<>();
            queryTeamMemberId.addWhereEqualTo(Constant.BMOB_USER_TEAM_MEMBER, teamMemberId);
            queryList.add(queryTeamMemberId);
        }else{
            //将team 也查询
            query.include(Constant.BMOB_USER_TEAM_MEMBER);
        }
        query.and(queryList);
        //按照最新的排序
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);
        query.setLimit(Constant.IM_PAGESIZE);
        query.setSkip(start * Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<TeamCase>() {
            @Override
            public void done(final List<TeamCase> list, BmobException e) {
                if (e == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (start == 0 && list.size() <= 0) {
                                showEmptyView();
                            } else if (isLoadMore && list != null && list.size() == 0) {
                                getView().showOnloadMoreNoData();
                            } else {
                                getView().showDataView(list);
                                if (isLoadMore) {
                                    getView().showOnLoadFinish();
                                }
                            }
                        }
                    });


                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //失败的情况
                            if (isLoadMore) {
                                getView().showOnLoadError();
                            } else {

                                getView().showErrorView();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showEmptyView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().showEmptyView();
            }
        });
    }

    public void getDataOnLoadMore(int currentIndex, String mUserId, String mTeamMemberId) {
        getData( currentIndex,  mUserId,  mTeamMemberId ,true);
    }
}
