package com.ourcompany.presenter.fragment;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.ourcompany.bean.bmob.Comment;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.BombUtils;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.MDiffCallback;
import com.ourcompany.view.fragment.CommentFragView;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import company.com.commons.framework.presenter.MvpBasePresenter;
import company.com.commons.framework.presenter.MvpPresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class CommentsPresenter extends MvpBasePresenter<CommentFragView> implements MvpPresenter<CommentFragView> {
    private BmobDate bmobDate;

    public CommentsPresenter(Context context) {
        super(context);
    }

    /**
     * 加载评论，首先加载最新的
     */
    public void loadComments(final int start, String objectId,final boolean isLoadMore) {
        //除了第一页
        if(TextUtils.isEmpty(objectId) && start==0){
            getView().showEmptyView();
            return;
        }
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.include(Constant.BMOB_POST_USER);
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);
        if (bmobDate == null) {
            bmobDate = new BmobDate(new Date(System.currentTimeMillis()));
        }
        query.addWhereLessThanOrEqualTo(Constant.BMOB_CREATE, bmobDate);
        query.addWhereEqualTo(Constant.BMOB_POST, objectId);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(Constant.IM_PAGESIZE);
        query.setSkip(start * Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(final List<Comment> list, BmobException e) {


                if (e == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (start == 0 && list.size() <= 0) {
                                getView().showEmptyView();
                            } else if (isLoadMore && list != null && list.size() == 0) {
                                getView().showOnloadMoreNoData();
                            } else {
                                if (isLoadMore) {
                                    getView().showOnLoadFinish();
                                }
                                getView().showContentView(list);

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


    /**
     * 如果是刷新的话，那么就不需要查询自己发的了，因为本地发的话，那么在本地添加了
     * @param createdAt
     * @param postId
     */
    public void getDataOnReFresh(String createdAt, String postId) {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.include(Constant.BMOB_POST_USER);
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);
        //查询playerName叫“比目”的数据
        query.addWhereGreaterThan(Constant.BMOB_CREATE, BombUtils.getBombDate(createdAt));
        query.addWhereEqualTo(Constant.BMOB_POST, postId);
        query.addWhereNotEqualTo(Constant.BMOB_POST_USER,MServiceManager.getInstance().getLocalThirdPartyId());
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(final List<Comment> list, BmobException e) {

                if (e == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (list.size() > 0) {
                                getView().showOnReflsh(list);
                            } else {
                                getView().showOnReflshNoNewsData();
                            }

                        }
                    });
                } else {
                    getView().showOnReflshError();
                }
            }
        });
    }

    /**
     * 加载更多的数据
     *
     * @param currentIndex
     */
    public void getDataOnLoadMore(int currentIndex,String objectId) {
        loadComments(currentIndex,objectId, true);
    }

    public void diffAdapter(final List<Comment> oldList, final List<Comment> newDatas) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                final DiffUtil.DiffResult result =  DiffUtil.calculateDiff(new MDiffCallback(oldList, newDatas), true);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().showDiffResult(result);
                    }
                });
            }
        });
    }
}

