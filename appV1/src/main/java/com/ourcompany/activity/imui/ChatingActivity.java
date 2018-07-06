package com.ourcompany.activity.imui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.imsdk.MobIM;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMUser;
import com.ourcompany.R;
import com.ourcompany.presenter.activity.ChatingActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.ChatingActivityView;
import com.ourcompany.widget.LoadingView;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/8 16:46
 * Des    :
 */

public class ChatingActivity extends MvpActivity<ChatingActivityView, ChatingActPresenter> implements ChatingActivityView {
    private static final String BUNDLE_KEY_GROUP = "isGroup";
    private static final String BUNDLE_KEY_UNREAD = "unread";
    private static final String BUNDLE_KEY_USER = "user";
    private static final String BUNDLE_KEY_ID_CONVERSATION = "conversationId";
    private static final String BUNDLE_KEY_IMUSER_ID = "imuser_id";
    private static final String BUNDLE_KEY = "chat_bundle";
    @BindView(R.id.loadingView)
    LoadingView loadingView;
    @BindView(R.id.tvNick)
    TextView tvNick;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnInfor)
    ImageView btnInfor;
    @BindView(R.id.lstChats)
    RecyclerView lstChats;
    @BindView(R.id.btnSpeak)
    ImageView btnSpeak;
    @BindView(R.id.edtInput)
    EditText edtInput;
    @BindView(R.id.btnRecorder)
    Button btnRecorder;
    @BindView(R.id.btnEmoj)
    ImageView btnEmoj;
    @BindView(R.id.btnAttach)
    ImageView btnAttach;
    @BindView(R.id.igvPic)
    ImageView igvPic;
    @BindView(R.id.igvCamera)
    ImageView igvCamera;
    @BindView(R.id.igvFile)
    ImageView igvFile;
    @BindView(R.id.layoutMore)
    LinearLayout layoutMore;
    @BindView(R.id.vp_horizontal_gridview)
    ViewPager vpHorizontalGridview;
    @BindView(R.id.ll_dot_container)
    LinearLayout llDotContainer;
    @BindView(R.id.txtEmojiSend)
    TextView txtEmojiSend;
    @BindView(R.id.layoutEmoji)
    LinearLayout layoutEmoji;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private String userId;
    private IMUser mImUser;

    /**
     * 进入与用户私聊界面
     *
     * @param conversation 聊天用户
     */
    public static void gotoUserChatPage(Context context, IMConversation conversation) {
        if (conversation == null) {
            return;
        }
        IMUser user = conversation.getOtherInfo();
        Intent intent = new Intent(context, ChatingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_KEY_GROUP, false);
        int msgcount = conversation.getUnreadMsgCount();
        bundle.putInt(BUNDLE_KEY_UNREAD, msgcount);
        bundle.putSerializable(BUNDLE_KEY_USER, user);
        bundle.putString(BUNDLE_KEY_ID_CONVERSATION, conversation.getId());
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        Bundle bundlev = getIntent().getBundleExtra(BUNDLE_KEY);
        userId = bundlev.getString(BUNDLE_KEY_IMUSER_ID);
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_chating;
    }

    @Override
    protected void initView() {
        super.initView();
        edtInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                }
                return false;
            }
        });
    }

    //发送消息
    private void sendMessage() {
        String message = edtInput.getText().toString();
        if (TextUtils.isEmpty(message)) {
            showToastMsg(ResourceUtils.getString(R.string.message_not_null));
            return;
        }
        //这个到时切割信息，分成多个消息
        if (message.length() > 5000) {
            showToastMsg(ResourceUtils.getString(R.string.message_too_long));
            return;
        }

        getPresenter().addMessage(MobIM.getChatManager().createTextMessage(Constant.CURRENT_ITEM_USER.id.get(), message, IMConversation.TYPE_USER));
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        loadingView.startAnimator();
        getPresenter().getImUserById(userId);
    }


    @Override
    protected ChatingActivityView bindView() {
        return this;
    }

    @Override
    protected ChatingActPresenter bindPresenter() {
        return new ChatingActPresenter(this);
    }

    @Override
    public void onLogining() {

    }

    @Override
    public void setIMUser(IMUser imUser) {
        mImUser = imUser;
        if (loadingView != null) {
            loadingView.stopAnimator();
        }
        tvNick.setText(Constant.CURRENT_ITEM_USER.nickname.get());

    }

    @Override
    public void getIMUserError() {
        showToastMsg("进入聊天失败");
        if (loadingView != null) {
            loadingView.stopAnimator();
        }
    }


    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    public static void gotoUserChatPageById(Context context, String userId) {
        Intent intent = new Intent(context, ChatingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_IMUSER_ID, userId);
        intent.putExtra(BUNDLE_KEY, bundle);
        context.startActivity(intent);
    }


    @OnClick({R.id.btnBack, R.id.btnInfor, R.id.btnSpeak, R.id.btnRecorder, R.id.btnEmoj, R.id.btnAttach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnInfor:

                break;
            case R.id.btnSpeak:

                break;
            case R.id.btnRecorder:
                break;
            case R.id.btnEmoj:
                break;
            case R.id.btnAttach:
                break;
        }
    }
}
