package company.com.commons.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/15 14:36
 * Des    :
 */

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView==null){
            //mRootView 为空时，添加到container
            int layoutId = getLayoutId();
            View root = inflater.inflate(layoutId,container,false);
            initView(root);
            mRootView = root;
        }else{
            //mRootView 不为空时，先从父->移除
            if(mRootView.getParent()!=null){
                ((ViewGroup)mRootView.getParent()).removeView(mRootView);
            }
        }

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    /**
     * 获取布局的id
     * @return
     */
    protected abstract int getLayoutId() ;

    /**
     * 窗口的设置
     */
    protected void windowsSetting(){

    }

    /**
     * 初始化界面的相关参数
     * @param bundle
     * @return
     */
    protected void initArgs(Bundle bundle){
    }

    /**
     * 初始化控件
     */
    protected void initView(View view){
        ButterKnife.bind(this,view);
    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }



    /**
     * 如果fragment 处理了，并返回true ,activity 不需要finish
     * false activity 走自己的逻辑
     */
    public boolean onBackEnvent(){
        return false;
    }

}
