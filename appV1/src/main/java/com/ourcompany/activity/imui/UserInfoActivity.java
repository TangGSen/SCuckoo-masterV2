package com.ourcompany.activity.imui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.ourcompany.R;
import com.ourcompany.adapter.TabLayoutViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.fragment.TestMobFragment;
import com.ourcompany.fragment.UserDynamicFragment;
import com.ourcompany.im.ui.ChatActivity;
import com.ourcompany.interfaces.MOnTabSelectedListener;
import com.ourcompany.presenter.activity.UserInfoPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TabLayoutIndicatorWith;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.UserInfoActvityView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/8 16:46
 * Des    :
 */

public class UserInfoActivity extends MvpActivity<UserInfoActvityView, UserInfoPresenter> implements UserInfoActvityView {


    @BindView(R.id.bg_head)
    ImageView bgHead;
    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.btnChat)
    TextView btnChat;
    @BindView(R.id.btnAddFriend)
    TextView btnAddFriend;
    @BindView(R.id.userSign)
    TextView userSign;
    @BindView(R.id.userImage)
    CircleImageView userImage;

    public static final String ACT_BUNDLE = "act_bundle";
    private static final String KEY_USEID = "user_id";
    private static final String KEY_USE_LOCAL = "is_use_local";
    @BindView(R.id.middle_layout)
    LinearLayout middleLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private User mUser;
    private boolean isUserLocal;
    private String mCurrentUserId;
    private String[] mTiltes;
    private ArrayList<Fragment> fragments;

    public static void gotoThis(Context context, boolean userIsLocal, String userId) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USEID, userId);
        bundle.putBoolean(KEY_USE_LOCAL, userIsLocal);
        intent.putExtra(ACT_BUNDLE, bundle);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        Bundle res = getIntent().getBundleExtra(ACT_BUNDLE);
        if (res != null) {
            isUserLocal = res.getBoolean(KEY_USE_LOCAL);
            if (isUserLocal && mUser != null) {
                mUser = Constant.CURRENT_ITEM_USER;
            }
            mCurrentUserId = res.getString(KEY_USEID);
        }
        return super.initArgs(bundle);
    }


    @Override
    protected void initView() {
        super.initView();
        initViewData();


    }

    /**
     * 初始化view 的数据
     */
    public void initViewData() {
        if (mUser != null) {
            tvNickName.setText(mUser.nickname.get());
            userSign.setText(mUser.signature.get());

            TabLayoutIndicatorWith.resetWith(mTablayout);
            fragments = new ArrayList<>();
            UserDynamicFragment testMobFragment = new UserDynamicFragment();

            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_USER_ID,mUser.id.get());
            testMobFragment.setArguments(bundle);
            TestMobFragment testMobFragment1 = new TestMobFragment();
            TestMobFragment testMobFragment2 = new TestMobFragment();
            fragments.add(testMobFragment);
            fragments.add(testMobFragment1);
            fragments.add(testMobFragment2);

            mTiltes = ResourceUtils.getStringArray(R.array.tabUserInfoItems);
            for (int i = 0; i < mTiltes.length; i++) {
                mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
            }
            TabLayoutViewPagerAdapter viewPagerAdapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager(), mTiltes, fragments);
            //tablayout 和viewpager 联动
            mViewPager.setAdapter(viewPagerAdapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
            mTablayout.addOnTabSelectedListener(new MOnTabSelectedListener(mViewPager));
            mViewPager.setCurrentItem(0);

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    userImage.setVisibility(View.VISIBLE);
                    userImage.setAlpha(1.0f - (-verticalOffset * 1.0f / appBarLayout.getTotalScrollRange()));
                    if (verticalOffset >= 0) {
                        userImage.setVisibility(View.VISIBLE);
                    } else if (verticalOffset == -appBarLayout.getTotalScrollRange()) {
                        userImage.setVisibility(View.GONE);
                    }
                }
            });

        }


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (mUser == null) {
            getPresenter().getCurrentUserInfo(mCurrentUserId);
        }

        getFriendShipFor();

    }

    /**
     * 获取当前的用户，与当前登陆的用户的关系
     */
    public void getFriendShipFor() {
        if (mUser == null) {
            return;
        }
        //相等

        if (UMSSDK.isMe(mUser)) {
            btnAddFriend.setVisibility(View.GONE);
            btnChat.setVisibility(View.GONE);
            tvNickName.setText("[我]" + mUser.nickname.get());
        } else {
            getPresenter().getFriendShip(mUser.id.get());
        }

    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    public void loading() {

    }

    @Override
    public void loaded() {

    }

    @Override
    public void showError(String message) {
        showToastMsg(message);
    }

    @Override
    public void isMyFriend() {
        btnChat.setVisibility(View.VISIBLE);
        btnChat.setEnabled(true);
        btnAddFriend.setVisibility(View.GONE);
        btnAddFriend.setEnabled(false);


    }

    @Override
    public void isNotMyFriend() {
        btnChat.setVisibility(View.GONE);
        btnChat.setEnabled(false);

        btnAddFriend.setVisibility(View.VISIBLE);
        btnAddFriend.setEnabled(true);
    }

    @Override
    public void requestIsMyFriendFaild() {
        showToastMsg(ResourceUtils.getString(R.string.net_exception));
    }

    @Override
    public void onErrortToToast(String string) {
        showToastMsg(string);
    }

    @Override
    public void showSuccess(String string) {
        showToastMsg(string);
    }

    @Override
    public void getUserInfoSuccess(User user) {
        mUser = user;
        initViewData();
        //在获取是否是朋友
        getFriendShipFor();
    }

    @Override
    public void getUserInfoFailed() {
        showToastMsg(ResourceUtils.getString(R.string.str_user_info_get_error));
        btnAddFriend.setVisibility(View.GONE);
        btnChat.setVisibility(View.GONE);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected UserInfoActvityView bindView() {
        return this;
    }

    @Override
    protected UserInfoPresenter bindPresenter() {
        return new UserInfoPresenter(MApplication.mContext);
    }


    @OnClick({R.id.btnChat, R.id.btnAddFriend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnChat:
                if (mUser != null && !TextUtils.isEmpty(mUser.id.get())) {
                    // ChatingActivity.gotoUserChatPageById(UserInfoActivity.this,mUser.id.get());
                    ChatActivity.gotoUserChatPage(UserInfoActivity.this, mUser.id.get());
                }

                break;
            case R.id.btnAddFriend:
                //弹出选框
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(ResourceUtils.getString(R.string.request_add_friend_msg));
                View root = View.inflate(UserInfoActivity.this, R.layout.layout_cunstomer_dialog, null);
                final EditText editText = root.findViewById(R.id.et_add_message);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constant.MAX_ADD_FRIEND_MESSAGE)});
                builder.setView(root);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        String message = editText.getText().toString();
                        getPresenter().addFrindByUserId(mUser, message);
                    }
                });
                builder.setCancelable(false);
                builder.show();
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
