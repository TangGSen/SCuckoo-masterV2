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

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;

    public ViewPagerAdapter(FragmentManager fm,  List<Fragment> fragmentList) {
        super(fm);
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


}

