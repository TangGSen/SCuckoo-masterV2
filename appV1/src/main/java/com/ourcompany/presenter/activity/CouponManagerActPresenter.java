package com.ourcompany.presenter.activity;

import android.content.Context;
import android.text.TextUtils;

import com.ourcompany.bean.bmob.Coupon;
import com.ourcompany.fragment.tab_mine.CouponManagerFragment;
import com.ourcompany.utils.BombUtils;
import com.ourcompany.utils.Constant;
import com.ourcompany.view.activity.CouponManagerActView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 下午2:08
 * Des    :
 */

public class CouponManagerActPresenter extends MvpBasePresenter<CouponManagerActView> {
    private final String BMOB_ENDTIME = "endTime";

    public CouponManagerActPresenter(Context context) {
        super(context);
    }


    /**
     * 搜索添加有：1.搜索关于objectId ，2.加载的类型：过期或未过期
     * 过不过期：那当前的时间与券的结束时间对比
     *  @param start
     * @param objectId 所属的user id
     * @param isLoadMore
     * @param currentType
     */
    public void getData(final int start, String objectId, final boolean isLoadMore, int currentType) {

        //除了第一页
        if((TextUtils.isEmpty(objectId) && start==0)|| currentType==-1){
            getView().showEmptyView();
            return;
        }
        List<BmobQuery<Coupon>> queryList = new ArrayList<>();

        //条件0 以时间来标识是否过期类型查询
        BmobQuery<Coupon> queryCon1 = new BmobQuery<Coupon>();
        if(currentType== CouponManagerFragment.TYPE_NOT_OVERDUE){
            queryCon1.addWhereGreaterThan(BMOB_ENDTIME, BombUtils.getBombDate(null));
            queryList.add(queryCon1);
        }else if(currentType== CouponManagerFragment.TYPE_OVERDUE){
            queryCon1.addWhereLessThan(BMOB_ENDTIME, BombUtils.getBombDate(null));
            //查询不包含这个的
            queryList.add(queryCon1);
        }

        BmobQuery<Coupon> queryCon2 = new BmobQuery<Coupon>();
        queryCon2.addWhereEqualTo(Constant.BMOB_SUSER_ID, objectId);
        queryList.add(queryCon2);

        BmobQuery<Coupon> query = new BmobQuery<Coupon>();
        query.and(queryList);
        //显示最新的
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);
        //查询那个所属的数据
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(Constant.IM_PAGESIZE);
        query.setSkip(start * Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<Coupon>() {
            @Override
            public void done(final List<Coupon> list, BmobException e) {


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
     * @param createdAt
     * @param currentType 加载的类型
     */
    public void getDataOnReFresh(String createdAt, String fristId, String userId, int currentType) {
        if(TextUtils.isEmpty(userId)){
            getView().showOnReflshError();
            return;
        }
        List< BmobQuery<Coupon>> queryList = new ArrayList<>();
        //条件0 以时间来标识是否过期类型查询
        BmobQuery<Coupon> queryCon0 = new BmobQuery<Coupon>();
        if(currentType== CouponManagerFragment.TYPE_NOT_OVERDUE){
            queryCon0.addWhereGreaterThan(BMOB_ENDTIME, BombUtils.getBombDate(null));
            queryList.add(queryCon0);
        }else if(currentType== CouponManagerFragment.TYPE_OVERDUE){
            queryCon0.addWhereLessThan(BMOB_ENDTIME, BombUtils.getBombDate(null));
            //查询不包含这个的
            queryList.add(queryCon0);
        }

        //条件1
        BmobQuery<Coupon> queryCon1 = new BmobQuery<Coupon>();
        queryCon1.addWhereNotEqualTo(Constant.BMOB_OBJECT_ID, fristId);
        queryList.add(queryCon1);
        //条件2
        BmobQuery<Coupon> queryCon2 = new BmobQuery<Coupon>();
        queryCon2.addWhereGreaterThan(Constant.BMOB_CREATE, BombUtils.getBombDate(createdAt));
        queryList.add(queryCon2);
        //查询不包含这个的


        //条件3 查询所属的
        BmobQuery<Coupon> queryCon3 = new BmobQuery<Coupon>();
        queryCon3.addWhereEqualTo(Constant.BMOB_SUSER_ID, userId);
        queryList.add(queryCon3);

        BmobQuery<Coupon> query = new BmobQuery<Coupon>();
        query.and(queryList);
        //query.include(Constant.BMOB_POST_USER);
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);

        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<Coupon>() {
            @Override
            public void done(final List<Coupon> list, BmobException e) {

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
     * @param currentType
     */
    public void getDataOnLoadMore(int currentIndex, String objectId, int currentType) {
        getData(currentIndex,objectId, true, currentType);
    }
}
