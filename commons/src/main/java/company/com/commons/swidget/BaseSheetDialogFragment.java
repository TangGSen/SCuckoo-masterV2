package company.com.commons.swidget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import company.com.commons.R;
import company.com.commons.framework.presenter.MvpPresenter;
import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/21 下午6:36
 * Des    :
 */
public  abstract class BaseSheetDialogFragment<V extends MvpView, P extends MvpPresenter<V>> extends BottomSheetDialogFragment {
    /**
     * 顶部向下偏移量
     */
    private int topOffset = 0;
    private BottomSheetBehavior<FrameLayout> behavior;






    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return new BottomSheetDialog(getContext(), R.style.TransparentBottomSheetStyle);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置背景透明，才能显示出layout中诸如圆角的布局，否则会有白色底（框）
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置软键盘不自动弹出
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = getHeight();
            behavior = BottomSheetBehavior.from(bottomSheet);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
    }
    /**
     * 获取屏幕高度
     *
     * @return height
     */
    private int getHeight() {
        int height = 1920;
        if (getContext() != null) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            if (wm != null) {
                // 使用Point已经减去了状态栏高度
                wm.getDefaultDisplay().getSize(point);
                height = point.y - getTopOffset();
            }
        }
        return height;
    }
    public int getTopOffset() {
        return topOffset;
    }
    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }
    public BottomSheetBehavior<FrameLayout> getBehavior() {
        return behavior;
    }


    private V view;
    private P presenter;

    protected View mRootView;
    protected Activity mActivity;
    protected Unbinder mUnbinder;
    //是否已经加载过数据
    protected boolean mIsLoadData;
    //是否已经初始化视图
    protected boolean isPrepared ;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            //mRootView 为空时，添加到container
            int layoutId = getLayoutId();
            View root = inflater.inflate(layoutId, container, false);
            mRootView = root;
            initView(root);

        } else {
            //mRootView 不为空时，先从父->移除
            if (mRootView.getParent() != null) {
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);
            }
        }
        isPrepared = true;
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLinstener();

    }

    protected  void initLinstener(){}

    /**
     * 获取布局的id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 窗口的设置
     */
    protected void windowsSetting() {

    }

    /**
     * 初始化界面的相关参数
     *
     * @param bundle
     * @return
     */
    protected void initArgs(Bundle bundle) {
    }

    /**
     * 初始化控件
     */
    protected void initView(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        initStateLayout(view);

    }

    protected void initStateLayout(View view){

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();

    }

    private void isCanLoadData() {
        if (!isPrepared) {
            return;
        }
        if (getUserVisibleHint()) {
            //fragment可见时加载数据，用一个方法来实现
            if(!mIsLoadData){
                mIsLoadData=!mIsLoadData;
                initData();
            }else{
            }
        } else {
            //不可见时不执行操作
        }
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }


    /**
     * 如果fragment 处理了，并返回true ,activity 不需要finish
     * false activity 走自己的逻辑
     */
    public boolean onBackEnvent() {
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (presenter == null) {
            presenter = bindPresenter();
        }

        if (view == null) {
            view = bindView();
        }

        if (presenter != null && view != null) {
            presenter.attachView(view);
        }
        Log.e("sen", "onActivityCreated: 1" );
        //放在可见的情况下才加载数据
        // initData();
        isCanLoadData();
    }



    protected abstract V bindView();

    //绑定Presenter
    protected abstract P bindPresenter();

    protected P getPresenter() {
        return presenter;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mIsLoadData = false;
//        isPrepared = false;

    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.dettachView();
            presenter = null;
        }
        //这个目前有个bug ,待处理
        /*if(mUnbinder!=null){
            mUnbinder.unbind();
        }*/
        super.onDestroy();
    }
}
