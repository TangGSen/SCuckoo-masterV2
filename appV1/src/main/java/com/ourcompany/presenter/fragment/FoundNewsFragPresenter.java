package com.ourcompany.presenter.fragment;

import android.content.Context;

import com.ourcompany.bean.bmob.Post;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.view.fragment.FoundNewsFragmentView;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class FoundNewsFragPresenter extends MvpBasePresenter<FoundNewsFragmentView> {
    BmobDate bmobDate;
    public FoundNewsFragPresenter(Context context) {
        super(context);
    }

    /**
     * 正常的加载数据，比如应用第一次加载，和加载更多时就使用该方法，
     * 如果是刷新的的那么就走刷新的方法
     * 1.按照updateAt 来排序，
     * 2.按照updateAt 时间来查询
     */

    public void getData(final int start, final boolean isLoadMore) {
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.include(Constant.BMOB_POST_USER);
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);
        //查询playerName叫“比目”的数据
        if (bmobDate == null) {
            bmobDate = new BmobDate(new Date(System.currentTimeMillis()));
        } else {
            LogUtils.e("sen", "bmobDate 不为null");
        }
        query.addWhereLessThanOrEqualTo(Constant.BMOB_CREATE, bmobDate);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(Constant.IM_PAGESIZE);
        query.setSkip(start * Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(final List<Post> list, BmobException e) {
                if (e == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (start == 0 && list.size() <= 0) {
                                showEmptyView();
                            } else if (isLoadMore && list != null && list.size() == 0) {
                                getView().showOnloadMoreNoData();
                            } else {
                                getView().showContentView(list);
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

    private void showErrorView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().showErrorView();
            }
        });
    }

    /**
     * 刷新数据,不包含当前的
     */

    public void getDataOnReFresh(String moreTime,String NoInobjectId) {

        BmobQuery<Post> query = new BmobQuery<Post>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 先从缓存获取数据，如果没有，再从网络获取。
        query.include(Constant.BMOB_POST_USER);
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);
        query.addWhereGreaterThan(Constant.BMOB_CREATE, bmobDate);
        query.addWhereNotEqualTo(Constant.BMOB_OBJECT_ID,NoInobjectId);
        query.setLimit(Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(final List<Post> list, BmobException e) {
                LogUtils.e("sen","getDataOnReFresh");
                if (e == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e("sen","getDataOnReFresh 1");
                            if (list.size() > 0) {
                                LogUtils.e("sen","getDataOnReFresh 2");
                                getView().showOnReflsh(list);
                            } else {
                                LogUtils.e("sen","getDataOnReFresh 3");
                                getView().showOnReflshNoNewsData();
                            }

                        }
                    });
                } else {
                    LogUtils.e("sen","getDataOnReFresh 4");
                    getView().showOnReflshError();
                }
            }
        });
    }

    public void getDataOnLoadMore(int currentIndex) {
        getData(currentIndex, true);
    }
}
