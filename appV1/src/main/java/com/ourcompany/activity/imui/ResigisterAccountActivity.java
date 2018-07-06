package com.ourcompany.activity.imui;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.ourcompany.R;
import com.ourcompany.adapter.ViewPagerAdapter;
import com.ourcompany.bean.AccoutRsigisterBean;
import com.ourcompany.fragment.user.ChoseUserTypeFragment;
import com.ourcompany.fragment.user.ResigisterAccoutFragment;
import com.ourcompany.presenter.activity.ResigisterActPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.ResigisterActView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpActivity;

public class ResigisterAccountActivity extends MvpActivity<ResigisterActView, ResigisterActPresenter> implements ResigisterActView {

    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private boolean isCanClose;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanClose) {
                    finish();
                    overridePendingTransition(0, 0);
                } else {
                    showDailog();
                }
            }
        });
        ResigisterAccoutFragment accoutFragment = new ResigisterAccoutFragment();
        ChoseUserTypeFragment userTypeFragment = new ChoseUserTypeFragment();
        fragments.add(accoutFragment);
        fragments.add(userTypeFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        EventBus.getDefault().register(this);
        isCanClose = true;
    }

    //还没设置用户的类型就想退出提示
    private void showDailog() {
        //如果不设置的话，那么就直接登录和创建bmob 用户
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(ResourceUtils.getString(R.string.str_eixt_user_setting));
        builder.setNegativeButton(R.string.str_futrue_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (fragments.get(1) != null) {
                    ( (ChoseUserTypeFragment) fragments.get(1)).exitUserSetting();
                }
            }
        });
        builder.setPositiveButton(R.string.str_now_setting ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_resigister;
    }

    @Override
    protected ResigisterActView bindView() {
        return this;
    }

    @Override
    protected ResigisterActPresenter bindPresenter() {
        return new ResigisterActPresenter(this);
    }


    @Override
    public void showToastMsg(String string) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventResigister(AccoutRsigisterBean user) {
        isCanClose = false;
        //这里只是切换Fragment
        mViewPager.setCurrentItem(1, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(isCanClose){
                showDailog();
                return false;
            }else{
                return super.onKeyDown(keyCode, event);
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
