package com.ourcompany.interfaces;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/27 16:59
 * Des    :
 */

public class MOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
     ViewPager mViewPager;
    public  MOnTabSelectedListener(ViewPager viewPager){
        this.mViewPager = viewPager;
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
