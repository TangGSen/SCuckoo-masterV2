package company.com.commons.framework.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import company.com.commons.framework.view.MvpView;


/**
 * Created by Administrator on 2017/8/21.
 * 抽象类统一管理解除和绑定视图
 */

public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    public static final Executor EXECUTOR = Executors.newCachedThreadPool();
    public Handler mHandler = new Handler(Looper.getMainLooper());
    private WeakReference<Context> weakRefContext;
    private WeakReference<V> weakRefView;
    private V mvpViewProxy;

    public V getView() {
        //返回代理对象V
        return mvpViewProxy;
    }

    //有时需要操作数据库，sd卡啥的需要上下文


    public MvpBasePresenter(Context context) {
        weakRefContext = new WeakReference<Context>(context);
    }

    public Context getContext() {
        if (weakRefContext == null) {
            return null;
        }
        return weakRefContext.get();
    }

    //判断是否绑定了View 才能操作
    public boolean isAttachView(){
        return weakRefView!=null && weakRefContext.get()!=null;


    }

    @Override
    public void attachView(V view) {
        weakRefView = new WeakReference<V>(view);
        //这里使用动态代理，这样就不用每次在Presenter 调用方法时都要判断空
        MvpViewInvocationHandler handler = new MvpViewInvocationHandler(view);
        mvpViewProxy = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(),view.getClass().getInterfaces(),handler);
    }

    @Override
    public void dettachView() {
        if (weakRefView != null) {
            weakRefView.clear();
            weakRefView = null;
        }
    }

    class MvpViewInvocationHandler implements InvocationHandler {
        //目标对象
        private V view;

        public MvpViewInvocationHandler(V view) {
            this.view = view;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //在这里判断空了
            if (isAttachView()){
                method.invoke(view,args);
            }
            return null;
        }
    }
}
