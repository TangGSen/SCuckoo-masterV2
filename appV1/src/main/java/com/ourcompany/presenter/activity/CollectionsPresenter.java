package com.ourcompany.presenter.activity;

import android.content.Context;
import android.text.TextUtils;

import com.ourcompany.bean.bmob.Post;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.utils.Constant;
import com.ourcompany.view.activity.CollectionActView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
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

public class CollectionsPresenter extends MvpBasePresenter<CollectionActView> implements MvpPresenter<CollectionActView> {

    public CollectionsPresenter(Context context) {
        super(context);
    }

    /**
     * 加载评论，首先加载最新的
     */
    public void loadDatas(final int start, String userObjectId,final boolean isLoadMore) {
        //除了第一页
        if(TextUtils.isEmpty(userObjectId) && start==0){
            getView().showEmptyView();
            return;
        }
        BmobQuery<Post> query = new BmobQuery<>();
        query.include(Constant.BMOB_LIKES);
        BmobQuery<SUser> innerQuery = new BmobQuery<>();
        innerQuery.addWhereEqualTo(Constant.BMOB_OBJECT_ID, userObjectId);
        // 注意第二个参数表名不要写错 是系统表
        query.addWhereMatchesQuery(Constant.BMOB_LIKES, Constant.BMOB_TABLE_SUSER,innerQuery);

        query.include(Constant.BMOB_POST_USER);
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);
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

          // 官网的例子没加上的 反过来查当前用户喜欢的所有帖子的实现  用内部查询

    }



    /**
     * 加载更多的数据
     *
     * @param currentIndex
     */
    public void getDataOnLoadMore(int currentIndex,String userObjectId) {
        loadDatas(currentIndex,userObjectId, true);
    }


}

