package com.ourcompany.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ourcompany.R;
import com.ourcompany.adapter.TabLayoutViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.fragment.TestMobFragment;
import com.ourcompany.fragment.message.ChatFragment;
import com.ourcompany.fragment.message.MessageContactsFragment;
import com.ourcompany.interfaces.MOnTabSelectedListener;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TabLayoutIndicatorWith;
import com.ourcompany.view.fragment.LoginFragmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    :
 */

public class MessageActivity extends MvpActivity<LoginFragmentView, LoginFragPresenter> implements LoginFragmentView {

    @BindView(R.id.message_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    Unbinder unbinder1;
    private List<Fragment> fragments;
    String mTiltes[];
    @Override
    protected void initView() {
        super.initView();
        TabLayoutIndicatorWith.resetWith(mTablayout);
        fragments  = new ArrayList<>();
        ChatFragment chatFragment = new ChatFragment();
        MessageContactsFragment contactsFragment = new MessageContactsFragment();
        TestMobFragment testMobFragment = new TestMobFragment();
        fragments.add(chatFragment);
        fragments.add(contactsFragment);
        fragments.add(testMobFragment);

        mTiltes = ResourceUtils.getStringArray( R.array.tabMessageItems);
        for (int i = 0;i<mTiltes.length;i++){
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
        }
        TabLayoutViewPagerAdapter viewPagerAdapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager(), mTiltes, fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new MOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(0);


    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_message;
    }

    @Override
    protected LoginFragmentView bindView() {
        return this;
    }

    @Override
    protected LoginFragPresenter bindPresenter() {
        return new LoginFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {

    }






}
