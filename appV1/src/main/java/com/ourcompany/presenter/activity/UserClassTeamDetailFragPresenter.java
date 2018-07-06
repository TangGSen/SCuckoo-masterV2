package com.ourcompany.presenter.activity;

import android.content.Context;
import android.text.TextUtils;

import com.ourcompany.bean.bmob.TeamMember;
import com.ourcompany.utils.Constant;
import com.ourcompany.view.fragment.UserClassTeamDetailFragView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class UserClassTeamDetailFragPresenter extends MvpBasePresenter<UserClassTeamDetailFragView> {
    public UserClassTeamDetailFragPresenter(Context context) {
        super(context);
    }




    public void getData(final int start, int dataType, String userId, final boolean isLoadMore)  {
        if(TextUtils.isEmpty(userId)) {
            getView().showErrorView();
            return;
        }
        List< BmobQuery<TeamMember>> queryList = new ArrayList<>();
        BmobQuery<TeamMember> queryCon1 = new BmobQuery<TeamMember>();
        //根据条件查询 类型查询
        queryCon1.addWhereEqualTo(Constant.BMOB_MEMBERTYPE, dataType);
        queryList.add(queryCon1);

        BmobQuery<TeamMember> queryCon2 = new BmobQuery<TeamMember>();

        //根据归属userId查询
        queryCon2.addWhereEqualTo(Constant.BMOB_SUSER_ID, userId);
        queryList.add(queryCon2);

        BmobQuery<TeamMember> query = new BmobQuery<TeamMember>();

        query.and(queryList);
        //按照作品的多少排序
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CASECOUNT);
        query.setLimit(Constant.IM_PAGESIZE);
        query.setSkip(start * Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<TeamMember>() {
            @Override
            public void done(final List<TeamMember> list, BmobException e) {
                if (e == null) {
//                    if ((list!=null &&list.size() <= 0)|| list==null) {
//                        getEmptyData();
//                    } else if(list!=null &&list.size() > 0){
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    getView().showDataView(list);
//                                }
//                            });
//
//                    }

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
                            getView().showErrorView();
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
}
