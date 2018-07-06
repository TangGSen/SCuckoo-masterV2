package com.ourcompany.activity.user_class;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.UserClassifyDetailActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.TeamCase;
import com.ourcompany.presenter.activity.UserTeamCaseDetailPresenter;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.UserTeamCaseDetailView;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/5/28 上午9:12
 * Des    :
 */
public class UserTeamCaseDetailActivity extends MvpActivity<UserTeamCaseDetailView, UserTeamCaseDetailPresenter> implements UserTeamCaseDetailView {


    public static final String KEY_INTENT = "key_intent";
    private static final String KEY_BUNDLE_DATA = "key_bundle_data";
    private static final String KEY_BUNDLE_DATA_MEMBERID = "menber_id";
    @BindView(R.id.btAppointment)
    TextView btAppointment;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.tvCoupon)
    TextView tvCoupon;
    private String[] tabTiles;
    private int tabItemDrawableNormal[];
    private int tabCount;
    private RecycleCommonAdapter<String> recycleCommonAdapter;
    private TeamCase mTeamCase;
    private String mMemberId;

    public static void gotoThis(Context context, TeamCase teamCase, String mTeamMemberId) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserTeamCaseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE_DATA_MEMBERID, mTeamMemberId);
        bundle.putSerializable(KEY_BUNDLE_DATA, teamCase);
        intent.putExtra(KEY_INTENT, bundle);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        Bundle res = getIntent().getBundleExtra(KEY_INTENT);
        if (res != null) {
            mTeamCase = (TeamCase) res.getSerializable(KEY_BUNDLE_DATA);
            mMemberId = res.getString(KEY_BUNDLE_DATA_MEMBERID);
            if (mTeamCase == null) {
                finish();
            }
        } else {
            finish();
        }
        return super.initArgs(bundle);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected void initView() {
        super.initView();
        getWindow().setBackgroundDrawable(null);
        initTabView();
        getRootView().post(new Runnable() {
            @Override
            public void run() {
                initRecycleView();
                initViewData();
            }


        });
    }

    private void initViewData() {
        content.setText(mTeamCase.getContent());
    }

    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //解决嵌套在NestedScrollView 的滑动不顺的问题1
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        //解决嵌套在NestedScrollView 的滑动不顺的问题2
        recycleview.setNestedScrollingEnabled(false);
        //  recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider_padding, 2));
        recycleCommonAdapter = new RecycleCommonAdapter<String>(
                MApplication.mContext, mTeamCase.getImageUrls(), R.layout.layout_item_imageview) {
            @Override
            public void bindItemData(SViewHolder holder, final String itemData, int position) {
                holder.setImageWithErrorImage(R.id.itemImage, itemData, R.drawable.ic_loading_defualt);

            }


        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
            }
        });
    }

    private void initTabView() {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabTiles = ResourceUtils.getStringArray(R.array.tabUserTeamCaseDetailItems);
        tabItemDrawableNormal = new int[]{R.drawable.ic_home_small, R.drawable.ic_chat_customer, R.drawable.ic_collection_v2};

        tabCount = tabTiles.length;
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(getTabView(tabItemDrawableNormal[i], tabTiles[i], i));

            tabLayout.addTab(tab, i);
            tab.getCustomView().setOnTouchListener(onTabTouchListener);
        }



    }

    //自定义TabView
    public View getTabView(int id, String text, int position) {
        TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_home_item_small, null);
        //设置图片
        Drawable topDrawable = ResourceUtils.getDrawable(id);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        view.setCompoundDrawables(null, topDrawable, null, null);
        view.setTextSize(12);
        if(position ==0){
            view.setTextColor(ResourceUtils.getResColor(R.color.colorPrimary));
        }else{
            view.setTextColor(ResourceUtils.getResColor(R.color.colorFrist));
        }

        view.setText(text);
        view.setTag(R.id.nine_layout_of_index, position);
        return view;
    }

    /**
     * tab 点击触摸事件
     */


    private View.OnTouchListener onTabTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Integer index = (Integer) view.getTag(R.id.nine_layout_of_index);
            LogUtils.e("sen", "index:" + index);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (index != null) {
                    switch (index) {
                        case 0:
                            LogUtils.e("sen", "index:" + index);
                            if (TextUtils.isEmpty(mMemberId)) {
                                //是空的话，那么是从主页过来的，那么直接关闭即可
                                finish();
                            } else {
                                UserClassifyDetailActivity.gotoThis(UserTeamCaseDetailActivity.this, mTeamCase.getUserId());
                            }
                            break;
                    }
                    //tabLayout.getTabAt(index).select();
                }

            }
            return true;
        }
    };


    public void changeSelecteTabColor(TextView textView, int drawableId,
                                      boolean isSelected) {
        Drawable topDrawable = ResourceUtils.getDrawable(drawableId);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        textView.setCompoundDrawables(null, topDrawable, null, null);
        // textView.setSelected(isSelected);
        textView.setTextColor(isSelected ? ResourceUtils.getResColor(R.color.colorPrimary) : ResourceUtils.getResColor(R.color.colorFrist));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_team_case_detail;
    }

    @Override
    protected UserTeamCaseDetailView bindView() {
        return this;
    }

    @Override
    protected UserTeamCaseDetailPresenter bindPresenter() {
        return new UserTeamCaseDetailPresenter(MApplication.mContext);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
