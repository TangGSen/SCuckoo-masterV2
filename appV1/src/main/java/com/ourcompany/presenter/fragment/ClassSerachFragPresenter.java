package com.ourcompany.presenter.fragment;

import android.content.Context;

import com.google.gson.Gson;
import com.ourcompany.bean.bmob.AppSettingJson;
import com.ourcompany.bean.json.CuckooServiceJson;
import com.ourcompany.manager.ClassSerachService;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.view.fragment.ClassSerachFragmentView;

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


public class ClassSerachFragPresenter extends MvpBasePresenter<ClassSerachFragmentView> {
    public ClassSerachFragPresenter(Context context) {
        super(context);
    }

    public void getCuckooService() {
        BmobQuery<AppSettingJson> query = new BmobQuery<AppSettingJson>();
        query.addWhereEqualTo(Constant.KEY_BMOB_APP_SETTING, Constant.KEY_BMOB_CUCKOO_SERVICE);
        query.setLimit(1);
        //执行查询方法
        query.findObjects(new FindListener<AppSettingJson>() {
            @Override
            public void done(final List<AppSettingJson> list, BmobException e) {
                if (e == null) {
                    if (list.size() <= 0) {
                        getEmptyData();
                    } else {
                        String json = list.get(0).getContent();
                        LogUtils.e("sen", json);
                        Gson gson = new Gson();
                        final CuckooServiceJson service = gson.fromJson(json, CuckooServiceJson.class);
                        if (service != null && service.getCuckooService() != null && service.getCuckooClass() != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getView().showDataView(service);
                                }
                            });
                        } else {
                            getEmptyData();
                        }

                    }

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


    private void getEmptyData() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().showEmptyView();
            }
        });
    }

    /**
     * 重置搜索条件
     */
    public void resetSerach() {
        ClassSerachService.getInstance().resetCurrentSerach();
        getView().resetSerachSuccuss();
    }
}
