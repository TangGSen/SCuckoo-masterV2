package company.com.commons.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/15 14:26
 * Des    :
 */

public abstract class BaseActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        windowsSetting();
        if(initArgs(getIntent().getExtras())){
            //初始化相关参数正确才能进入界面
            setContentView(getLayoutId());
            initView();
            initData();
        }else{
            finish();
        }
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
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 初始化控件
     */
    protected void initView(){
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    /**
     *  点击了界面导航返回时，finish
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return super.onSupportNavigateUp();
    }



    /**
     * activity 的返回事件，
     * 如果fragment 处理了，并返回true ,activity 不需要finish
     */
    @Override
    public void onBackPressed() {
       List<Fragment> fragmentList =  getSupportFragmentManager().getFragments();
       if(fragmentList!=null && fragmentList.size()>0){
           for (Fragment fragemnt :fragmentList ) {
               //再判断是否是自己的fragment
               if(fragemnt instanceof BaseFragment){
                   //在判断是否拦截了返回按钮
                   if(((BaseFragment)fragemnt).onBackEnvent()){
                       //拦截了，就让她处理
                       return;
                   }
               }
           }
       }
        super.onBackPressed();
       finish();
    }
}
