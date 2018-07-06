package com.ourcompany.presenter.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.view.activity.SearchActvityView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import company.com.commons.framework.presenter.MvpBasePresenter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/17 21:14
 * Des    :
 */

public class SearchActPresenter extends MvpBasePresenter<SearchActvityView> {
    //开始倒数
    private static final int MSG_COUNTING_TIME = 0;
    private static final int MSG_ERROR_GET_CODE = 1;
    //验证成功
    private static final int MSG_RESIGISTER_SUCCESS = 2;
    private static final int MSG_RESIGISTER_FAIL = 3;
    private static final int MSG_LOGIN_SUCCESS = 4;
    private static final int MSG_LOGIN_FAIL = 5;
    private static String currentPhone = "";
    public int currentTime = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_COUNTING_TIME:

                    break;
                case MSG_ERROR_GET_CODE:

                    break;
                case MSG_RESIGISTER_FAIL:

                    break;
                case MSG_RESIGISTER_SUCCESS:

                    break;

                case MSG_LOGIN_SUCCESS:
                    break;
                case MSG_LOGIN_FAIL:
                    break;

            }
        }
    };
    private PublishSubject<String> mPublishSubject;
    private DisposableObserver<ArrayList<User>> mDisposableObserver;
    private CompositeDisposable mCompositeDisposable;

    public SearchActPresenter(Context context) {
        super(context);
    }

    /**
     * 解除绑定
     */

    public void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }

    }

    /**
     * 初始化Rxjava
     */

    public void initPublishSubject() {
        mPublishSubject = PublishSubject.create();
        mDisposableObserver = new DisposableObserver<ArrayList<User>>() {

            @Override
            public void onNext(ArrayList<User> users) {
                //显示结果
                getView().showSearchRes(users);
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtils.e("sen", "onError");
                getView().showError(throwable.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };

        mPublishSubject.debounce(200, TimeUnit.MILLISECONDS).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return s.length() > 0;
            }
        }).switchMap(new Function<String, ObservableSource<ArrayList<User>>>() {
            @Override
            public ObservableSource<ArrayList<User>> apply(String query) throws Exception {
                return getSearchObservable(query);
            }

        }).observeOn(AndroidSchedulers.mainThread()).subscribe(mDisposableObserver);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(mCompositeDisposable);
    }

    //获取可以搜索的
    private ObservableSource<ArrayList<User>> getSearchObservable(final String query) {
        return Observable.create(new ObservableOnSubscribe<ArrayList<User>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<User>> observableEmitter) throws Exception {
                LogUtils.e("sen", "开始请求，关键词为：" + query);
                //开启请求
                requestForSearchAuto(query, observableEmitter);
                LogUtils.e("sen", "结束请求，关键词为：" + query);

            }
        }).subscribeOn(Schedulers.io());

    }

    //请求获取结果,这个是主动搜索
    private void requestForSearchAuto(final String query, final ObservableEmitter<ArrayList<User>> observableEmitter) {
        MServiceManager.getInstance().requestSearch(query, 1, Constant.IM_PAGESIZE, new OperationCallback<ArrayList<User>>() {
            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                LogUtils.e("sen", "requestForSearchAuto onFailed");
                observableEmitter.onError(new Throwable("没有数据"));
            }

            @Override
            public void onSuccess(final ArrayList<User> users) {
                super.onSuccess(users);
                if (users != null && users.size() > 0) {
                    observableEmitter.onNext(users);
                }
                observableEmitter.onComplete();
            }
        });

    }

    /**
     * 开始搜索
     *
     * @param content
     */
    public void afterTextChanged(String content) {
        if (mPublishSubject != null) {
            mPublishSubject.onNext(content);
        }
    }


}
