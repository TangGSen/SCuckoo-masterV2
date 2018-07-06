package com.ourcompany.activity;

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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.imui.UserInfoActivity;
import com.ourcompany.adapter.TabLayoutViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.PostPositionChange;
import com.ourcompany.bean.VoteChage;
import com.ourcompany.bean.bmob.Comment;
import com.ourcompany.bean.bmob.Post;
import com.ourcompany.bean.bmob.Vote;
import com.ourcompany.fragment.CommentFragment;
import com.ourcompany.fragment.VoteFragment;
import com.ourcompany.interfaces.MOnTabSelectedListener;
import com.ourcompany.presenter.activity.PostDeailActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.InputMethodUtils;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TabLayoutIndicatorWith;
import com.ourcompany.utils.TimeFormatUtil;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.PostDeailActView;
import com.ourcompany.widget.NineGridlayout;
import com.ourcompany.widget.recycleview.commadapter.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/13 14:59
 * Des    :
 */

public class PostDetailActivity extends MvpActivity<PostDeailActView, PostDeailActPresenter> implements PostDeailActView, InputMethodUtils.OnKeyboardEventListener {
    private static final String KEY_INTENT = "key_intent";
    private static final String KEY_BUNDLE = "key_bundle";
    private static final String KEY_BUNDLE_POSITION = "key_bundle_position";
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.ivNineLayout)
    NineGridlayout ivNineLayout;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.likes)
    TextView likes;
    @BindView(R.id.comments)
    TextView comments;
    @BindView(R.id.imgUserTop)
    CircleImageView imgUserTop;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.etInputFor)
    TextView etInputFor;
    @BindView(R.id.imgLove)
    ImageView imgLove;
    @BindView(R.id.layoutBottom)
    LinearLayout layoutBottom;
    @BindView(R.id.inputOutSildeView)
    View inputOutSildeView;
    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.btn_a_t)
    TextView btnAT;
    @BindView(R.id.imageKeyBorad)
    ImageView imageKeyBorad;
    @BindView(R.id.btnSend)
    TextView btnSend;
    @BindView(R.id.v_panel)
    View vPanel;
    @BindView(R.id.layoutInpts)
    LinearLayout layoutInpts;
    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    @BindView(R.id.btVote)
    TextView btVote;


    private Post mPost;
    private int mPostPosition;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private String[] mTiltes;
    private Vote mVote;

    public static void gotoThis(Context context, Post post, int position) {
        if (post == null) {
            return;
        }
        Intent intent = new Intent(context, PostDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_BUNDLE, post);
        bundle.putInt(KEY_BUNDLE_POSITION, position);
        intent.putExtra(KEY_INTENT, bundle);
        context.startActivity(intent);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_deail;
    }

    @Override
    protected PostDeailActView bindView() {
        return this;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        Bundle bun = getIntent().getBundleExtra(KEY_INTENT);
        if (bun != null) {
            mPost = (Post) bun.getSerializable(KEY_BUNDLE);
            mPostPosition = bun.getInt(KEY_BUNDLE_POSITION);
        }
        return super.initArgs(bundle);

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
            }
        });


        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendTxtMsg();
                }
                return true;
            }
        });
        InputMethodUtils.detectKeyboard(this, this);
        imgUserTop.setAlpha(0.0f);
        imgUserTop.setEnabled(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                imgUserTop.setAlpha(-verticalOffset * 1.0f / appBarLayout.getTotalScrollRange());
                if (verticalOffset >= 0) {
                    imgUserTop.setEnabled(false);
                } else if (verticalOffset == -appBarLayout.getTotalScrollRange()) {
                    imgUserTop.setEnabled(true);
                }
            }
        });


    }


    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        layoutInpts.setVisibility(View.VISIBLE);
        layoutBottom.setVisibility(View.GONE);
        vPanel.setVisibility(View.GONE);
        InputMethodUtils.toggleSoftInputForEt(etInput);
        ViewGroup.LayoutParams params = vPanel.getLayoutParams();
        if (params != null && params.height != keyboardHeight) {
            params.height = keyboardHeight;
            vPanel.setLayoutParams(params);
        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        if (!isEmotionPanelShowing()) {
            layoutInpts.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.VISIBLE);
            vPanel.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean isEmotionPanelShowing() {
        return vPanel.getVisibility() == View.VISIBLE;
    }

    @Override
    public void hideEmotionPanel() {
        if (vPanel.getVisibility() != View.GONE) {
            vPanel.setVisibility(View.GONE);
            InputMethodUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    public void showEmotionPanel() {
        vPanel.removeCallbacks(mHideEmotionPanelTask);
        vPanel.setVisibility(View.VISIBLE);
        InputMethodUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        InputMethodUtils.hideKeyboard();

    }

    private void sendTxtMsg() {
        getPresenter().submitComment(etInput.getText().toString(), mPost.getObjectId());
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (mPost != null) {
            tvUserName.setText(mPost.getUser() == null ? ResourceUtils.getString(R.string.defualt_userName) : TextUtils.isEmpty(mPost.getUser().getUserName()) ? ResourceUtils.getString(R.string.defualt_userName) : mPost.getUser().getUserName());
            tvContent.setText(mPost.getContent());
            tvTime.setText(TimeFormatUtil.getIntervalFormString(mPost.getCreatedAt()));
            imgUser.setTag(R.id.loading_image_url, mPost.getUser() == null ? "" : mPost.getUser().getImageUrl());
            ImageLoader.getImageLoader().loadImage(imgUser, "");

            imgUserTop.setTag(R.id.loading_image_url, mPost.getUser() == null ? "" : mPost.getUser().getImageUrl());
            ImageLoader.getImageLoader().loadImage(imgUserTop, "");
            ivNineLayout.setImagesData(mPost.getImageUrls(), 0);
            ivNineLayout.setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
                @Override
                public void onItemClick(int index) {
                    ImagesPreViewActvitity.gotoThis(PostDetailActivity.this, (ArrayList<String>) mPost.getImageUrls(), index);

                }
            });

            if (mPost.getLikeCount() == null) {
                mPost.setLikeCount(0);
            }
            if (mPost.getCommentCount() == null) {
                mPost.setCommentCount(0);
            }
            likes.setText(mPost.getLikeCount() + "");
            comments.setText(mPost.getCommentCount() + "");

            if (mPost.getNeedVote() != null && mPost.getNeedVote() && mPost.getmPostVoteDeadline() != null && !mPost.getmPostVoteDeadline()) {
                //btVote.setVisibility(View.VISIBLE);
                //获取自己有没投票
                getPresenter().loadIsUserVote(mPost.getObjectId());
            } else {
                btVote.setVisibility(View.GONE);
            }
            //查看用户是否喜欢这个帖子
            TabLayoutIndicatorWith.resetWith(mTablayout);
            getPresenter().loadIsUserLike(mPost.getObjectId());
            CommentFragment commentFragment = new CommentFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_POST_ID, mPost.getObjectId());
            commentFragment.setArguments(bundle);
            fragments.add(commentFragment);

            mTiltes = ResourceUtils.getStringArray(R.array.tabPostDealtItems);

            int tabLength = 0;
            if (mPost.getNeedVote() != null && mPost.getNeedVote()) {
                tabLength = mTiltes.length;
                VoteFragment voteFragment = new VoteFragment();
                voteFragment.setArguments(bundle);
                fragments.add(voteFragment);
            } else {
                tabLength = mTiltes.length - 1;
            }
            for (int i = 0; i < tabLength; i++) {
                mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
            }
            TabLayoutViewPagerAdapter viewPagerAdapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager(), mTiltes, fragments);
            //tablayout 和viewpager 联动
            mViewPager.setAdapter(viewPagerAdapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
            mTablayout.addOnTabSelectedListener(new MOnTabSelectedListener(mViewPager));
            mViewPager.setCurrentItem(0);
        }

    }

    @Override
    protected PostDeailActPresenter bindPresenter() {
        return new PostDeailActPresenter(MApplication.mContext);
    }


    @Override
    public void showLoadView() {
    }


    @Override
    public void submitError() {
        LogUtils.e("sen", "submitError");
        getView().showToastMsg(ResourceUtils.getString(R.string.submit_fail));
    }

    @Override
    public void submitOk(Comment comment) {
        getView().showToastMsg(ResourceUtils.getString(R.string.submit_success));
        etInput.setText("");
        int count = mPost.getCommentCount() + 1;
        comments.setText(count + "");
        mPost.setCommentCount(count);
        showLayoutButtomView();
        //刷新Fragment
        EventBus.getDefault().post(comment);
    }

    @Override
    public void userIsLikeThis(boolean isLike) {
        imgLove.setSelected(isLike);
    }

    @Override
    public void userChangeLikeThis(boolean isLike) {
        imgLove.setSelected(isLike);
        int count = mPost.getLikeCount();
        count = isLike ? ++count : --count;
        count = count < 0 ? 0 : count;
        mPost.setLikeCount(count);
        likes.setText(count + "");
        EventBus.getDefault().post(new PostPositionChange().setPosition(mPostPosition).setCollection(isLike));
    }

    @Override
    public void showIsUserVote(boolean isVote, Vote vote) {
        this.mVote = vote;
        changeBtnVote( isVote);
    }

    @Override
    public void addUserVoteSuccess(Vote vote) {
        changeBtnVote( true);
        EventBus.getDefault().post(new VoteChage().setAdd(true).setmVote(vote));
        this.mVote = vote;
    }

    @Override
    public void optionUserVoteFail() {
        showToastMsg(ResourceUtils.getString(R.string.net_exception));
    }

    @Override
    public void deleteUserVoteSuccess() {
        changeBtnVote(  false);
        EventBus.getDefault().post(new VoteChage().setAdd(false).setmVote(mVote));
    }

    private void changeBtnVote( boolean isVote){
        btVote.setSelected(isVote);
        btVote.setVisibility(View.VISIBLE);
        String tip ="";
        if(isVote){
            tip =  ResourceUtils.getString(R.string.str_vote_success);
        }else{
            tip = ResourceUtils.getString(R.string.str_vote_normal);
        }
        btVote.setText(tip);
    }


    @OnClick({R.id.imgUser, R.id.imgLove, R.id.etInputFor, R.id.btnSend,
            R.id.imageKeyBorad, R.id.inputOutSildeView,
            R.id.imgUserTop, R.id.btVote})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgLove:
                getPresenter().updateUserLove(imgLove.isSelected(), mPost.getObjectId());
                break;
            case R.id.etInputFor:
                if (isEmotionPanelShowing()) {
                    vPanel.postDelayed(mHideEmotionPanelTask, 500);
                } else {
                    layoutInpts.setVisibility(View.VISIBLE);
                    InputMethodUtils.toggleSoftInputForEt(etInput);
                }
                InputMethodUtils.setKeyboardShowing(true);
                break;


            case R.id.btnSend:
                sendTxtMsg();
                break;

            case R.id.imageKeyBorad:
                if (isEmotionPanelShowing()) {
                    InputMethodUtils.toggleSoftInput(getCurrentFocus());
                    vPanel.postDelayed(mHideEmotionPanelTask, 500);
                } else {
                    showEmotionPanel();
                }
                break;
            case R.id.inputOutSildeView:
                showLayoutButtomView();
                break;

            case R.id.imgUserTop:
            case R.id.imgUser:
                String userId;
                if (mPost.getUser() != null && !TextUtils.isEmpty(mPost.getUser().getUserId())) {
                    userId = mPost.getUser().getUserId();
                } else {
                    userId = mPost.getUserId();
                }
                UserInfoActivity.gotoThis(PostDetailActivity.this, false, userId);
                break;
            case R.id.btVote:
                if (btVote.isSelected()) {
                    showDialogVote();
                } else {
                    getPresenter().addUserVote(mPost.getObjectId());
                }

                break;


        }
    }

    private void showDialogVote() {
        //弹出选框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(ResourceUtils.getString(R.string.str_title_vote));
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getPresenter().deleteUserVote(mVote);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 显示底部的view
     */
    public void showLayoutButtomView() {
        InputMethodUtils.hideKeyboard();
        InputMethodUtils.hideEmotionPanel();
        layoutInpts.setVisibility(View.GONE);
        layoutBottom.setVisibility(View.VISIBLE);
    }


    //隐藏的任务
    private Runnable mHideEmotionPanelTask = new Runnable() {
        @Override
        public void run() {
            hideEmotionPanel();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (layoutInpts.getVisibility() == View.VISIBLE) {
                showLayoutButtomView();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


}



