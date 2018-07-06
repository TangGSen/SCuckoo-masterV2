package com.ourcompany.activity.user_class;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.TeamMember;
import com.ourcompany.fragment.user_class_detail.UserTeamCaseFragment;
import com.ourcompany.presenter.activity.UserTeamMemeberActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.UserTeamMemeberDetailActView;
import com.ourcompany.widget.recycleview.commadapter.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import company.com.commons.framework.view.impl.MvpActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserTeamMemeberDetailActivity extends MvpActivity<UserTeamMemeberDetailActView, UserTeamMemeberActPresenter> implements UserTeamMemeberDetailActView {


    public static final String KEY_INTENT = "key_intent";
    private static final String KEY_BUNDLE_USER = "key_bundle_data";
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvOtherInfo)
    TextView tvOtherInfo;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;

    private TeamMember mTeamMember;

    public static void gotoThis(Context context, TeamMember teamMember) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserTeamMemeberDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_BUNDLE_USER, teamMember);
        intent.putExtra(KEY_INTENT, bundle);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        commonToolbar.setTitle("");
        getWindow().setBackgroundDrawable(null);
        setSupportActionBar(commonToolbar);
        commonToolbar.setNavigationIcon(R.drawable.ic_back_v3);
        commonToolbar.setContentInsetStartWithNavigation(0);
        commonToolbar.setBackground(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        Bundle bun = getIntent().getBundleExtra(KEY_INTENT);
        if (bun != null) {
            mTeamMember = (TeamMember) bun.getSerializable(KEY_BUNDLE_USER);
        }
        return super.initArgs(bundle);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (mTeamMember != null) {
            toolbarTitle.setText("");

            imgUser.setTag(R.id.loading_image_url, mTeamMember.getMemberImage());
            ImageLoader.getImageLoader().loadImage(imgUser, "");
            tvUserName.setText(TextUtils.isEmpty(mTeamMember.getMemberName()) ?
                    ResourceUtils.getString(R.string.defualt_userName) : mTeamMember.getMemberName());
            tvOtherInfo.setText(mTeamMember.getOtherInfo());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            UserTeamCaseFragment caseFragment = new UserTeamCaseFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.KEY_TEAM_TYPE,Constant.TEAM_TYPE_DESINGE);
            bundle.putString(Constant.BMOB_SUSER_ID, mTeamMember.getUserId());
            bundle.putString(Constant.BMOB_USER_TEAM_MEMBER, mTeamMember.getObjectId());
            caseFragment.setArguments(bundle);
            transaction.replace(R.id.framelayout, caseFragment);
            transaction.commit();


        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_team_detail;
    }

    @Override
    protected UserTeamMemeberDetailActView bindView() {
        return this;
    }

    @Override
    protected UserTeamMemeberActPresenter bindPresenter() {
        return new UserTeamMemeberActPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
