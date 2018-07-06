package com.ourcompany.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.fragment.FoundFragment;
import com.ourcompany.fragment.HomeFragment;
import com.ourcompany.fragment.MessageFragment;
import com.ourcompany.fragment.MineFragment;
import com.ourcompany.presenter.activity.HomeActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.HomeAcitityView;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpActivity;

public class HomeActivity extends MvpActivity<HomeAcitityView,HomeActPresenter> implements HomeAcitityView{
    private final static int TAB_COUNT = 4;
    @BindView(R.id.home_layout_content)
    FrameLayout containerLayout;
   @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    //tab item name
    String tabTiles[];
    //tab item drawable
    private int tabItemDrawableNormal[];
    private int tabItemDrawableSelected[];
    private int tabCount;

    android.support.v4.app.FragmentManager mFragmentManager;

    private HomeFragment mHomeFragment;
    private FoundFragment mFoundFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mPersonalCenterFragment;

    private int currentFragPosition = 0;
    private final String FRAG_POSITION = "currentFragPosition";



    @Override
    protected void initView() {
        super.initView();



    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
       // Bmob.initialize(this,"2db1fc7ea509d3ea6639495a3a24066d");
        tabTiles = ResourceUtils.getStringArray( R.array.tabButtonItemName);
        tabItemDrawableNormal = new int[]{R.drawable.ic_main_normal, R.drawable.ic_classify_normal, R.drawable.ic_message_normal, R.drawable.ic_mine_normal};
        tabItemDrawableSelected = new int[]{R.drawable.ic_main_selected, R.drawable.ic_classify_selected, R.drawable.ic_message_selected, R.drawable.ic_mine_seleted};
        initTabView();
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {

            //取出上一次保存的数据
            currentFragPosition = savedInstanceState.getInt(FRAG_POSITION,0);
            Log.e("sen","恢复的状态"+currentFragPosition);
            mHomeFragment = (HomeFragment) mFragmentManager.findFragmentByTag(tabTiles[0]);
            mFoundFragment = (FoundFragment) mFragmentManager.findFragmentByTag(tabTiles[1]);
            mMessageFragment = (MessageFragment) mFragmentManager.findFragmentByTag(tabTiles[2]);
            mPersonalCenterFragment = (MineFragment) mFragmentManager.findFragmentByTag(tabTiles[3]);

            tabLayout.getTabAt(currentFragPosition).select();


        }

        setSelectedFragment(currentFragPosition);




    }

    private void setSelectedFragment(int position) {

        FragmentTransaction transaction = mFragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        hideAllFragments(transaction);
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.home_layout_content, mHomeFragment, tabTiles[position]);
                } else {
                    // 如果不为空，则直接将它显示出来
                    transaction.show(mHomeFragment);
                }
                break;
            case 1:
                if (mFoundFragment == null) {
                    mFoundFragment = new FoundFragment();
                    transaction.add(R.id.home_layout_content, mFoundFragment, tabTiles[position]);
                } else {
                    transaction.show(mFoundFragment);
                }
                break;
            case 2:
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    transaction.add(R.id.home_layout_content, mMessageFragment, tabTiles[position]);
                } else {
                    transaction.show(mMessageFragment);
                }
                break;
            case 3:
                if (mPersonalCenterFragment == null) {
                    mPersonalCenterFragment = new MineFragment();
                    transaction.add(R.id.home_layout_content, mPersonalCenterFragment, tabTiles[position]);
                } else {
                    transaction.show(mPersonalCenterFragment);
                }
                break;
        }
        currentFragPosition = position;
        transaction.commit();
    }

    private void hideAllFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mFoundFragment != null) {
            transaction.hide(mFoundFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }
        if (mPersonalCenterFragment != null) {
            transaction.hide(mPersonalCenterFragment);
        }
    }

    //系统销毁Activity 的时候保存Fragment 的状态
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存tab选中的状态
        Log.e("sen","保存tab选中的状态"+currentFragPosition);
        outState.putInt(FRAG_POSITION, currentFragPosition);
        super.onSaveInstanceState(outState);
    }



    private void initTabView() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabCount = tabItemDrawableNormal.length;
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(getTabView(tabItemDrawableNormal[i], tabTiles[i]));
            tabLayout.addTab(tab, i);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int positionTab = tab.getPosition();
                TextView textView = (TextView) tab.getCustomView();
                changeSelecteTabColor(textView, tabItemDrawableSelected[positionTab], true);
                setSelectedFragment(positionTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int positionTab = tab.getPosition();
                TextView textView = (TextView) tab.getCustomView();
                changeSelecteTabColor(textView, tabItemDrawableNormal[positionTab], false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //不知为啥 0 的不会调用setOnTabSelectedListener 的方法
        //  tabLayout.getTabAt(0).select();


        TextView textView = (TextView) tabLayout.getTabAt(0).getCustomView();

        changeSelecteTabColor(textView, tabItemDrawableSelected[0], true);

    }



    //自定义TabView
    public View getTabView(int id, String text) {
        TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_home_item, null);
        //设置图片
        Drawable topDrawable = ResourceUtils.getDrawable( id);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        view.setCompoundDrawables(null, topDrawable, null, null);

        view.setText(text);
        return view;
    }

    public void changeSelecteTabColor(TextView textView, int drawableId, boolean isSelected) {
        Drawable topDrawable = ResourceUtils.getDrawable( drawableId);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        textView.setCompoundDrawables(null, topDrawable, null, null);
       // textView.setSelected(isSelected);
        textView.setTextColor(isSelected?ResourceUtils.getResColor(R.color.colorPrimary):ResourceUtils.getResColor(R.color.text_gray));
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected HomeAcitityView bindView() {
        return this;
    }

    @Override
    protected HomeActPresenter bindPresenter() {
        return new HomeActPresenter(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String from = intent.getStringExtra(Constant.ACT_FROM);
        //如果是登陆成功的话，那么刷新当前页面
        if(!TextUtils.isEmpty(from)&&from.equals(Constant.ACT_FROM_LOGIN_SUCCESS)){
            if(mPersonalCenterFragment!=null){}
              //  mPersonalCenterFragment.reflesh();
        }
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }
}
