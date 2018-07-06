package com.ourcompany.activity.tab_mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ourcompany.EmptyMvpPresenter;
import com.ourcompany.EmptyMvpView;
import com.ourcompany.R;
import com.ourcompany.adapter.ViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.TypeSelect;
import com.ourcompany.fragment.HistoryCouponListDialog;
import com.ourcompany.fragment.tab_mine.CouponManagerFragment;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.widget.popwindowns.CustomOperationPopWindow;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.util.Utils;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 下午2:08
 * Des    :
 */
public class CouponManagerActivity extends MvpActivity<EmptyMvpView, EmptyMvpPresenter> implements EmptyMvpView {


    @BindView(R.id.addCoupon)
    TextView addCoupon;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.checkOverdueCoupon)
    TextView checkOverdueCoupon;
    private ArrayList<Fragment> fragments;
    private String[] tabTiles;
    //所属的公司的，或者是个人的优惠券，如果为空那么直接就为加载失败或者为空


    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, CouponManagerActivity.class);
        context.startActivity(intent);

    }


    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        initTabView();
    }

    private void initTabView() {
        fragments = new ArrayList<>();
        //需要加个标识是未过期，已过期的类型标识,0 代表未过期，1代表已过期
        CouponManagerFragment notOverdueFrag = new CouponManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CouponManagerFragment.KEY_TYPE, CouponManagerFragment.TYPE_NOT_OVERDUE);
        notOverdueFrag.setArguments(bundle);
        fragments.add(notOverdueFrag);

        CouponManagerFragment overdueFrag = new CouponManagerFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(CouponManagerFragment.KEY_TYPE, CouponManagerFragment.TYPE_OVERDUE);
        overdueFrag.setArguments(bundle2);
        fragments.add(overdueFrag);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    checkOverdueCoupon.setText(ResourceUtils.getString(R.string.str_check_overdue_coupon));
                }else{
                    checkOverdueCoupon.setText(ResourceUtils.getString(R.string.str_check_effective_coupon));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    //    /**
//     * 改变SVG图片着色
//     * @param imageView
//
//     */
//    public void changeSVGColor(TextView imageView){
//
//       // drawable1.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//
//        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_coupon,getTheme());
//        //你需要改变的颜色
//        vectorDrawableCompat.setTint(ResourceUtils.getResColor(R.color.colorPrimary));
//
//        imageView.setCompoundDrawables(vectorDrawableCompat,null,null,null);
//    }


    @Override
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, CouponManagerActivity.this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupon_manager;
    }

    @Override
    protected EmptyMvpView bindView() {
        return this;
    }

    @Override
    protected EmptyMvpPresenter bindPresenter() {
        return new EmptyMvpPresenter(MApplication.mContext);
    }


    @OnClick({R.id.addCoupon,R.id.checkOverdueCoupon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addCoupon:

                tabTiles = ResourceUtils.getStringArray(R.array.tabAddCouponType);
                int resid[] = new int[]{R.drawable.ic_coupon_add_new, R.drawable.ic_coupon_history, R.drawable.ic_coupon_template};
                int size = tabTiles.length;
                List<TypeSelect> typeSelects = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    TypeSelect select = new TypeSelect();
                    select.setName(tabTiles[i]);
                    select.setResDrawable(resid[i]);
                    typeSelects.add(select);
                }
                CustomOperationPopWindow customOperationPopWindow = new CustomOperationPopWindow(this, typeSelects);

                RecycleCommonAdapter<TypeSelect> recycleCommonAdapter = new RecycleCommonAdapter<TypeSelect>(MApplication.mContext, typeSelects, R.layout.layout_item_popup_for_coupon) {
                    @Override
                    public void bindItemData(SViewHolder holder, TypeSelect itemData, int position) {

                        holder.setText(R.id.titles, itemData.getName());
                        if (itemData.getResDrawable() > 0) {
                            Drawable dra = getResources().getDrawable(itemData.getResDrawable());
                            dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                            ((TextView) holder.getView(R.id.titles)).setCompoundDrawables(dra, null, null, null);
                            ((TextView) holder.getView(R.id.titles)).setCompoundDrawablePadding(DisplayUtils.dip2px(4));
                        }

                    }

                };

                customOperationPopWindow.setOnItemListener(new CustomOperationPopWindow.OnItemListener() {
                    @Override
                    public void OnItemListener(int position, TypeSelect typeSelect) {
                        //此处实现列表点击所要进行的操作
                        switch (position) {
                            case 0:
                                AddCouponActivity.gotoThis(CouponManagerActivity.this, null,true);
                                break;
                            case 1:
                                HistoryCouponListDialog dialog = HistoryCouponListDialog.newInstance();
                                dialog.show(getSupportFragmentManager(), "dialog");


                                break;

                        }
                    }
                });
                customOperationPopWindow.setRecycleAdapter(recycleCommonAdapter);
                customOperationPopWindow.showPopupWindows(commonToolbar, addCoupon);//可以传个半透明view v_background过去根据业务需要显示隐藏

                break;
            case R.id.checkOverdueCoupon:
                if(mViewPager.getCurrentItem()==0){
                    mViewPager.setCurrentItem(1);
                }else if(mViewPager.getCurrentItem()==1){
                    mViewPager.setCurrentItem(0);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
