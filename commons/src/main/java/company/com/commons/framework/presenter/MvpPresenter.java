package company.com.commons.framework.presenter;


import company.com.commons.framework.view.MvpView;

/**
 * Created by Administrator on 2017/8/21.
 * Presenter 抽象接口
 */

public interface MvpPresenter<V extends MvpView> {
    //绑定View
    void attachView(V view);

    //解绑View
    void dettachView();
}
