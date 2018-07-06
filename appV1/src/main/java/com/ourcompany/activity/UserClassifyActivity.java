package com.ourcompany.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.fragment.ClassSerachFragment;
import com.ourcompany.manager.ClassSerachService;
import com.ourcompany.presenter.activity.UserClassifyActPresenter;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.LocationOption;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.UserClassifyActView;
import com.ourcompany.widget.FlowLayout;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.headfooter.MFooter;
import com.ourcompany.widget.recycleview.headfooter.MHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/24 10:46
 * Des    : 设计，施工，监理维修的分类列表+搜索
 */

public class UserClassifyActivity extends MvpActivity<UserClassifyActView, UserClassifyActPresenter> implements UserClassifyActView {

    private static final String KEY_INTENT = "key_intent";
    private static final String KEY_BUNDLE_TITLE = "key_bundle_position";
    @BindView(R.id.btAddress)
    TextView btAddress;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btSerach)
    ImageView btSerach;
    @BindView(R.id.titleBar)
    RelativeLayout titleBar;
    @BindView(R.id.btClassSerach)
    TextView btClassSerach;
    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    @BindView(R.id.rightClassSerach)
    FrameLayout rightClassSerach;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.mPupoPositionView)
    View mPupoPositionView;


    private String mTitle;
    private String[] mTiltes;
    private LocationOption.MLocation mLocation;
    private RecycleCommonAdapter<SUser> recycleCommonAdapter;
    private List<SUser> mUserList = new ArrayList<>();
    private int currentIndex;
    private int tabResSeletedId[] = new int[]{R.drawable.ic_triangle_one_up, -1, R.drawable.ic_triangle_two};
    private int tabResNormalId[] = new int[]{R.drawable.ic_triangle_one_up_normal, -1, R.drawable.ic_triangle_two_normal};
    private PopupWindow popupWindow;
    private int currentTabSeleted = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_classify;
    }

    @Override
    protected void initView() {
        super.initView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //解决嵌套在NestedScrollView 的滑动不顺的问题1
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        //解决嵌套在NestedScrollView 的滑动不顺的问题2
        recycleview.setNestedScrollingEnabled(true);

        refreshLayout.setEnableRefresh(true);
        // recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider_padding, 1));

        recycleCommonAdapter = new RecycleCommonAdapter<SUser>(
                MApplication.mContext, mUserList, R.layout.layout_item_class_suser) {
            @Override
            public void bindItemData(SViewHolder holder, final SUser itemData, int position) {
                holder.setText(R.id.tvUserName, TextUtils.isEmpty(itemData.getUserName()) ? ResourceUtils.getString(R.string.defualt_userName) : itemData.getUserName());
                holder.setImage(R.id.imageUser, itemData.getImageUrl(), DisplayUtils.dip2px(2));
                if (itemData.getAuthenV() != null && itemData.getAuthenV()) {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_authen_v);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    ((TextView) holder.getView(R.id.tvUserName)).setCompoundDrawables(null, null, drawable, null);
                    ((TextView) holder.getView(R.id.tvUserName)).setCompoundDrawablePadding(DisplayUtils.dip2px(4));
                }
                ((XLHRatingBar) holder.getView(R.id.ratingBar)).setCountSelected(itemData.getEvaluation());
                if (itemData.getCuckooService() != null && itemData.getCuckooService().size() > 0) {
                    int size = itemData.getCuckooService().size();
                    setFlowlayoutItem(((FlowLayout) holder.getView(R.id.layoutCuckooService)), position, size, itemData.getCuckooService());
                }
            }
        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                UserClassifyDetailActivity.gotoThis(UserClassifyActivity.this,mUserList.get(position));
            }
        });


        refreshLayout.setRefreshHeader(new MHeader(UserClassifyActivity.this).setEnableLastTime(false).setTextSizeTitle(12).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(100));
        refreshLayout.setRefreshFooter(new MFooter(UserClassifyActivity.this).setTextSizeTitle(12).setSpinnerStyle(SpinnerStyle.Scale).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(100));
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //按照时间来加载，刷新是加载比第一个更晚的时间，
                if (mUserList != null && mUserList.size() > 0) {
                    getPresenter().getDataOnReFresh(mUserList.get(mUserList.size() - 1).getCreatedAt(), mUserList.get(mUserList.size() - 1).getObjectId());
                } else {
                    //传空默认传当前时间
                    getPresenter().getDataOnReFresh("", "");
                }
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++currentIndex;
                getPresenter().getDataOnLoadMore(currentIndex);
            }
        });

        //mDrawerLayout的监听器
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
             * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
             */
            @Override
            public void onDrawerStateChanged(int arg0) {
            }

            /**
             * 当抽屉被滑动的时候调用此方法
             * arg1 表示 滑动的幅度（0-1）
             */
            @Override
            public void onDrawerSlide(View arg0, float arg1) {
            }

            /**
             * 当一个抽屉被完全打开的时候被调用
             */
            @Override
            public void onDrawerOpened(View arg0) {
            }

            /**
             * 当一个抽屉完全关闭的时候调用此方法
             */
            @Override
            public void onDrawerClosed(View arg0) {
                currentIndex = 0;
                //使用这个来设置上拉加载更多的开关
                refreshLayout.setNoMoreData(false);
                getPresenter().loadDataFromClassSerach(currentIndex);
            }
        });


    }

    /**
     * @param flowLayout
     * @param position      当前flowlayout处于的位置
     * @param newViewCount  最新的view的数量，需要跟cuckooService size一致
     * @param cuckooService
     */
    private void setFlowlayoutItem(FlowLayout flowLayout, int position, int newViewCount, List<String> cuckooService) {
        //view的创建，这里做一个view的重用
        if (flowLayout == null)
            return;
        flowLayout.setTag(R.id.nine_layout_of_index, position);
        if (flowLayout.getChildCount() <= 0) {

            int i = 0;
            while (i < newViewCount) {
                TextView textView = generateItemView(i);
                flowLayout.addView(textView, generateDefaultLayoutParams());
                i++;
            }

        } else {
            int oldViewCount = flowLayout.getChildCount();
            if (oldViewCount > newViewCount) {
                try {
                    flowLayout.removeViews(newViewCount, oldViewCount - newViewCount);
                } catch (Exception e) {
                }
            } else if (oldViewCount < newViewCount) {
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    //这是增加的view
                    TextView iv = generateItemView(i + oldViewCount);
                    flowLayout.addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        //赋值
        if (flowLayout.getChildCount() > 0 && flowLayout.getChildCount() == newViewCount) {
            for (int i = 0; i < newViewCount; i++) {
                ((TextView) flowLayout.getChildAt(i)).setText(cuckooService.get(i));
            }
        }

    }

    /**
     * FlowLayout相关的
     */


    private View.OnClickListener onFlowLayoutItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //获取当前的父控件的
            Integer parent = (Integer) ((ViewGroup) v.getParent()).getTag(R.id.nine_layout_of_index);
            if (parent != null && parent < mUserList.size()) {
                Integer child = (Integer) v.getTag(R.id.nine_layout_of_index);
                if (child != null && mUserList.get(parent).getCuckooService() != null && child < mUserList.get(parent).getCuckooService().size()) {
                    showToastMsg(mUserList.get(parent).getCuckooService().get(child));
                }
            }
        }
    };
    //设置layout params

    private TextView generateItemView(int position) {
        TextView textView = (TextView) View.inflate(MApplication.mContext, R.layout.layout_item_textview, null);
        textView.setTag(R.id.nine_layout_of_index, position);
        textView.setOnClickListener(onFlowLayoutItemClick);
        return textView;
    }

    private ViewGroup.MarginLayoutParams generateDefaultLayoutParams() {

        int leftPx = 0;
        int rightPx = 0;
        int topPx = 0;
        int bottomPx = 0;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        //不存在时创建一个新的参数
        marginParams = new ViewGroup.MarginLayoutParams(params);

        //根据DP与PX转换计算值
        rightPx = DisplayUtils.dip2px(4);
        bottomPx = DisplayUtils.dip2px(4);
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        return marginParams;
    }

    /**
     * Flowlayout相关的结束
     */

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                mDrawerLayout.closeDrawers();
            } else super.onBackPressed();
        } else super.onBackPressed();
    }

    public static void gotoThis(Context context, String title) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserClassifyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE_TITLE, title);
        intent.putExtra(KEY_INTENT, bundle);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {


        return super.initArgs(bundle);
    }

    @Override
    protected void initStateLayout() {
        super.initStateLayout();
        //初始化状态的布局
        View emptyView = getLayoutInflater().inflate(R.layout.layout_state_empty_with_retry, (ViewGroup) this.findViewById(android.R.id.content), false);
        layoutState.setEmptyView(emptyView);
        layoutState.changeState(StateFrameLayout.LOADING);

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        tvTitle.setText(TextUtils.isEmpty(mTitle) ? "" : mTitle);


        mTiltes = ResourceUtils.getStringArray(R.array.tabUserClassInfoItems);
        int size = mTiltes.length;
        for (int i = 0; i < size; i++) {
            View newTab = LayoutInflater.from(this).inflate(R.layout.layout_tab_textview, null);
            TextView tv = (TextView) newTab.findViewById(R.id.tabText);
            ImageView im = (ImageView) newTab.findViewById(R.id.tabIcon);
            if (tabResSeletedId[i] > 0) {
                if (i == 0) {
                    im.setImageResource(tabResSeletedId[i]);
                } else {
                    im.setImageResource(tabResNormalId[i]);
                }
            } else {
                im.setVisibility(View.GONE);
            }

            tv.setText(mTiltes[i]);
            if (i == 0) {
                tv.setTextColor(ResourceUtils.getResColor(R.color.colorPrimary));
            } else {
                tv.setTextColor(ResourceUtils.getResColor(R.color.colorFrist));
            }
            mTablayout.addTab(mTablayout.newTab().setCustomView(newTab));
            newTab.setTag(R.id.nine_layout_of_index, i);
            newTab.setOnTouchListener(onTabTouchListener);
        }
        mTablayout.addOnTabSelectedListener(new MOnTabSelectedListener());
        //开始定位
        getRootView().post(new Runnable() {
            @Override
            public void run() {
                //侧菜单栏
                ClassSerachFragment serachFragment = ClassSerachFragment.getInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.rightClassSerach, serachFragment).commit();
                getPresenter().getLoactionInfo(false, UserClassifyActivity.this);
            }
        });
        getPresenter().setSecondClass(mTitle);
        getPresenter().getData(currentIndex, false);

    }

    /**
     * tab 点击触摸事件
     */


    private View.OnTouchListener onTabTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Integer index = (Integer) view.getTag(R.id.nine_layout_of_index);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (index != null) {

                    if (index == 0) {
                        if (mTablayout.getTabAt(index).isSelected()) {
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                            } else {
                                ImageView imageView = mTablayout.getTabAt(0).getCustomView().findViewById(R.id.tabIcon);
                                ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0, 180);
                                animator.setDuration(200);
                                animator.setInterpolator(new DecelerateInterpolator());
                                animator.start();
                                showSortDialog();
                            }

                        }
                    } else {
                        //点击其他的就关闭
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                    mTablayout.getTabAt(index).select();
                }
            }
            return true;
        }
    };

    private void showSortDialog() {
        //弹出选框
        popupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_pupo_tab_content, null);
        View dismissView = view.findViewById(R.id.mDismissView);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        dismissView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        final Drawable drawable = ResourceUtils.getDrawable(R.drawable.ic_checkbox_check_style_v2);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        final String[] tabItems = ResourceUtils.getStringArray(R.array.tabSortItems);
        int size = tabItems.length;
        for (int i = 0; i < size; i++) {
            RadioButton radioView = (RadioButton) View.inflate(MApplication.mContext, R.layout.layout_item_popup_for_tab, null);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    DisplayUtils.dip2px(38));
            radioView.setLayoutParams(params);
            radioView.setText(tabItems[i]);
            radioView.setTag(R.id.tag_position, i);
            radioView.setId(i);
            if (i == currentTabSeleted) {
                radioView.setCompoundDrawables(null, null, drawable, null);
                radioView.setChecked(true);
            }
            radioGroup.addView(radioView, params);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                int size = radioGroup.getChildCount();
                   for (int i = 0; i < size; i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if (radioButton.getId() == checkId) {
                        currentTabSeleted =checkId;
                        radioButton.setCompoundDrawables(null, null, drawable, null);
                    } else {
                        radioButton.setCompoundDrawables(null, null, null, null);
                    }
                       ((TextView) mTablayout.getTabAt(0).getCustomView().findViewById(R.id.tabText)).setText(tabItems[checkId].substring(0,2));
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                    }
                }
            }
        });
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.bg_pupowidowns));
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //将第一个tab 复原
                ImageView imageView = mTablayout.getTabAt(0).getCustomView().findViewById(R.id.tabIcon);
                ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", -180, 0);
                animator.setDuration(200);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.start();
            }
        });
        popupWindow.showAsDropDown(mPupoPositionView, 0, 0);
    }

    @Override
    public void showMLocation(LocationOption.MLocation location, boolean isCityPick) {
        mLocation = location;
        if (mLocation != null) {
            showAddressText(location.getCity(), isCityPick);
        } else {
            showAddressText(null, isCityPick);
        }
    }

//    onConfigurationChanged

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.e("sen", "onConfigurationChanged");
    }

    /**
     * 显示地址
     *
     * @param city
     * @param isCityPick
     */
    private void showAddressText(String city, boolean isCityPick) {
        if (!TextUtils.isEmpty(city)) {
            LogUtils.e("sen", "city 实质显示");
            btAddress.setText(city);
            if (isCityPick && mLocation != null) {
                LogUtils.e("sen", "city  pick success ");
                CityPicker.getInstance()
                        .locateComplete(new LocatedCity(mLocation.getCity(), mLocation.getProvince(), mLocation.getCityCode()),
                                LocateState.SUCCESS);
            }
        } else {
            if (isCityPick) {
                CityPicker.getInstance()
                        .locateComplete(new LocatedCity(ResourceUtils.getString(R.string.str_address_error), "0", "0"),
                                LocateState.FAILURE);
            }
            btAddress.setText(ResourceUtils.getString(R.string.str_address_error));
        }
    }

    @Override
    public void showLoactionError(boolean isCityPick) {
        showAddressText(null, isCityPick);
    }




    class MOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        public MOnTabSelectedListener() {
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int tabPositon = tab.getPosition();
            TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tabText);
            ImageView im = (ImageView) tab.getCustomView().findViewById(R.id.tabIcon);
            tv.setTextColor(ResourceUtils.getResColor(R.color.colorPrimary));
            if (tabResSeletedId[tabPositon] > 0) {
                im.setImageResource(tabResSeletedId[tabPositon]);
            } else {
                im.setVisibility(View.GONE);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tabText);
            tv.setTextColor(ResourceUtils.getResColor(R.color.colorFrist));
            int tabPositon = tab.getPosition();
            ImageView im = (ImageView) tab.getCustomView().findViewById(R.id.tabIcon);
            if (tabResNormalId[tabPositon] > 0) {
                im.setImageResource(tabResNormalId[tabPositon]);
            } else {
                im.setVisibility(View.GONE);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    @Override
    protected UserClassifyActView bindView() {
        return this;
    }

    @Override
    protected UserClassifyActPresenter bindPresenter() {
        return new UserClassifyActPresenter(MApplication.mContext);
    }


    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @OnClick({R.id.btAddress, R.id.btSerach, R.id.btClassSerach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btAddress:
                showAddress();
                break;
            case R.id.btSerach:
                break;
            case R.id.btClassSerach:
                if (!mDrawerLayout.isDrawerOpen(Gravity.END)) {
                    mDrawerLayout.openDrawer(Gravity.END);
                }
                break;
        }
    }

    /**
     * 关闭侧边菜单栏
     */
    public void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
            mDrawerLayout.closeDrawers();
        }
    }


    private void showAddress() {
        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())
                .enableAnimation(true)
                .setAnimationStyle(R.style.DefaultCityPickerAnimation)
                .setLocatedCity(mLocation != null ? new LocatedCity(mLocation.getCity(), mLocation.getProvince(), mLocation.getCityCode()) : null)
                .setHotCities(null)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        if (data == null) {
                            if (mLocation != null) {
                                showAddressText(mLocation.getCity(), false);
                            } else {
                                showAddressText(null, false);
                            }
                        } else {
                            showAddressText(data.getName(), false);
                        }
                    }

                    @Override
                    public void onLocate() {
                        //开始定位
                        getPresenter().getLoactionInfo(true, UserClassifyActivity.this);
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getPresenter().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
    }

    @Override
    public void showContentView(List<SUser> list) {
        recycleCommonAdapter.addDatasInLast(list);
        //修改，recycleCommonAdapter 重新加载数据，不从顶部开始显示
        if (currentIndex == 0) {
            recycleview.scrollToPosition(0);

        }
        layoutState.changeState(StateFrameLayout.SUCCESS);
    }


    @Override
    public void showLoadView() {
        layoutState.changeState(StateFrameLayout.LOADING);
        recycleCommonAdapter.clearData();
    }

    @Override
    public void showErrorView() {
        showToastMsg(ResourceUtils.getString(R.string.load_data_fail));
    }

    @Override
    public void showOnReflsh(List<SUser> list) {
        refreshLayout.finishRefresh(0, true);
        recycleCommonAdapter.addDatasInFirst(0, list);
        recycleview.scrollToPosition(0);
    }

    @Override
    public void showOnReflshNoNewsData() {
        showToastMsg(ResourceUtils.getString(R.string.str_reflsh_no_new_data));
        refreshLayout.finishRefresh(0, true);
    }

    @Override
    public void showOnReflshError() {
        showToastMsg(ResourceUtils.getString(R.string.str_reflesh_error));
        refreshLayout.finishRefresh(0, false);

    }

    @Override
    public void showOnLoadError() {
        refreshLayout.finishLoadMore(0, false, false);
        showToastMsg(ResourceUtils.getString(R.string.str_onload_error));

    }

    @Override
    public void showOnLoadFinish() {
        //如果是没有更新的数据时需要停止刷新半分钟，防止频繁的刷新
        refreshLayout.finishLoadMore(0, true, false);
    }

    @Override
    protected void onDestroy() {
        ClassSerachService.getInstance().clear();
        super.onDestroy();
    }

    @Override
    public void showOnloadMoreNoData() {
        // refreshLayout.finishLoadMore(0, true, true);
        refreshLayout.finishLoadMoreWithNoMoreData();
        // refreshLayout.finishLoadMore(true);
    }

}
