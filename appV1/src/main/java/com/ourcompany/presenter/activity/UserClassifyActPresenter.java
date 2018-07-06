package com.ourcompany.presenter.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.manager.ClassSerachService;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LocationOption;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.PermissionsUitls;
import com.ourcompany.utils.TimeFormatUtil;
import com.ourcompany.view.activity.UserClassifyActView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Created by Administrator on 2017/8/20.
 */

public class UserClassifyActPresenter extends MvpBasePresenter<UserClassifyActView> {
    private static final int REQUEST_CAMERA_CODE = 0x001;
    String[] mPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private boolean isCityPick;
    private BmobQuery<SUser> bmobQuery;
    List<BmobQuery<SUser>> queries = new ArrayList<BmobQuery<SUser>>();
    private BmobQuery<SUser> mSecondClass;

    public UserClassifyActPresenter(Context context) {
        super(context);
    }

    /**
     * isCityPick 是控件里面的更新
     *
     * @param isCityPick
     * @param
     */
    public void getLoactionInfo(boolean isCityPick, Activity activity) {
        this.isCityPick = isCityPick;
        if (Constant.CURRENT_CITY != null) {
            LogUtils.e("sen", "获取本地的定位");
            getView().showMLocation(Constant.CURRENT_CITY, isCityPick);
            return;
        }
        // 检查摄像头权限是否已经有效
        if (PermissionsUitls.hasPermissions(activity, mPermissions)) {
            // 摄像头权限还未得到用户的同意
            // 权限有效
            gotoLocation(this.isCityPick);
        } else {
            // 如果用户已经拒绝过，那么提供额外的权限，说明那么应该弹出去设置
            //但是这种情况下，就是，当用户点击了禁止，并不再询问，
            ActivityCompat.requestPermissions(activity,
                    mPermissions,
                    REQUEST_CAMERA_CODE);
        }


    }

    public void gotoLocation(final boolean isCityPick) {
        LogUtils.e("sen", "获取网络定位");
        LocationOption.getInstance().initLocation(MApplication.mContext, new LocationOption.MLocationListener() {
            @Override
            public void onSuccess(final LocationOption.MLocation mLocation) {
                destoryLoaction();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mLocation != null) {
                            Constant.CURRENT_CITY = mLocation;
                            getView().showMLocation(mLocation, isCityPick);
                        }
                    }
                });
            }

            @Override
            public void onFail() {
                destoryLoaction();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().showLoactionError(isCityPick);
                    }
                });
            }
        });
        //开始定位
        LocationOption.getInstance().startLocation();
    }

    private void destoryLoaction() {
        LocationOption.getInstance().destroyLocation();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (REQUEST_CAMERA_CODE == requestCode) {
            if (grantResults.length == mPermissions.length
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[mPermissions.length - 1] == PackageManager.PERMISSION_GRANTED) {
                gotoLocation(this.isCityPick);
            } else {
                getView().showLoactionError(this.isCityPick);
            }
        }
    }

    /**
     * 正常的加载数据，比如应用第一次加载，和加载更多时就使用该方法，
     * 如果是刷新的的那么就走刷新的方法
     * 1.按照updateAt 来排序，
     * 2.按照updateAt 时间来查询
     */


    public void getData(final int start, final boolean isLoadMore) {
        createDefualQuery();
        bmobQuery.order(Constant.BMOB_CREATE);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(Constant.IM_PAGESIZE);
        bmobQuery.setSkip(start * Constant.IM_PAGESIZE);
        //执行查询方法
        bmobQuery.findObjects(new FindListener<SUser>() {
            @Override
            public void done(final List<SUser> list, BmobException e) {
                if (e == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (start == 0 && list.size() <= 0) {
                                showEmptyView();
                            } else if (isLoadMore && list != null && list.size() == 0) {
                                //没有更多的数据了
                                LogUtils.e("sen","没有更多的数据了");
                                getView().showOnloadMoreNoData();
                            } else {
                                //只有成功的时候才重置搜索条件
                                ClassSerachService.getInstance().saveCurrentSerach();
                                getView().showContentView(list);
                                if (isLoadMore) {
                                    getView().showOnLoadFinish();
                                }
                            }
                        }
                    });


                } else {
                    LogUtils.e("sen", "**3");
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
     * 创建默认的查询条件
     */
    private void createDefualQuery() {
        if (bmobQuery == null) {
            bmobQuery = new BmobQuery<SUser>();
            if (mSecondClass != null) {
                queries.clear();
                queries.add(mSecondClass);
            }
            if (queries.size() > 0) {
                bmobQuery.and(queries);
            }
        }
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
        createDefualQuery();

        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 先从缓存获取数据，如果没有，再从网络获取。
        bmobQuery.include(Constant.BMOB_POST_USER);
        bmobQuery.order(Constant.BMOB_CREATE);
        Date date =  TimeFormatUtil.getDateFormTimeString(moreTime);
        if(date!=null){
            BmobDate bmobDate =   new BmobDate(date);
            bmobQuery.addWhereLessThan(Constant.BMOB_CREATE, bmobDate);
        }else{
            getView().showOnReflshError();
            return;
        }

        bmobQuery.addWhereNotEqualTo(Constant.BMOB_OBJECT_ID, NoInobjectId);
        bmobQuery.setLimit(Constant.IM_PAGESIZE);
        //执行查询方法
        bmobQuery.findObjects(new FindListener<SUser>() {
            @Override
            public void done(final List<SUser> list, BmobException e) {
                LogUtils.e("sen","getDataOnReFresh");
                if (e == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e("sen","getDataOnReFresh 1");
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

    public void getDataOnLoadMore(int currentIndex) {
        getData(currentIndex, true);
    }


    /**
     * 加载数据策略
     *
     * @param currentIndex
     */
    public void loadDataFromClassSerach(int currentIndex) {
        //首先判断需要加载不
        if (ClassSerachService.getInstance().isAnbleLoadding()) {
            getView().showLoadView();
            LogUtils.e("sen", "可以加载了");
            BmobQuery<SUser> keyWordCondition = ClassSerachService.getInstance().getKeyWordCondition();
            BmobQuery<SUser> classCondition = ClassSerachService.getInstance().getClassCondition();

            queries.clear();
            if (mSecondClass != null) {
                queries.add(mSecondClass);
            }
            if (keyWordCondition != null) {
                queries.add(keyWordCondition);
            }
            if (classCondition != null) {
                queries.add(classCondition);
            }
            //新条件变了重新new
            bmobQuery = new BmobQuery<>();
            //大于0才把条件加入
            if (queries.size() > 0) {
                bmobQuery.and(queries);
            }

            getData(currentIndex, false);

        } else {
            LogUtils.e("sen", "不需要加载");
        }
    }

    public String getCuckooServiceString(List<String> cuckooService) {
        if (cuckooService == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int size = cuckooService.size();
        for (int i = 0; i < size; i++) {
            builder.append(cuckooService.get(i));
            if (i != size - 1) {
                builder.append("|");
            }
        }
        return builder.toString();
    }

    public void setSecondClass(String mTitle) {
        mSecondClass = ClassSerachService.getInstance().getSecondClassCondition(mTitle);
    }
}
