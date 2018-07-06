package com.ourcompany.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/31 21:08
 * Des    : 消息页面
 */

public class TabLayoutViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titleList;
    private List<Fragment> fragmentList;

    public TabLayoutViewPagerAdapter(FragmentManager fm, String[] titleList, List<Fragment> fragmentList) {
        super(fm);
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }




    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }
}

