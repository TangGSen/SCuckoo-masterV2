package com.ourcompany.activity.imui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mob.ums.User;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.activity.SearchActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.SearchActvityView;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/4 21:26
 * Des    : 使用Rxjava 来友好搜索
 */

public class SearchActivity extends MvpActivity<SearchActvityView, SearchActPresenter> implements SearchActvityView {
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.btCancle)
    TextView btCancle;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    private List<User> mUsers = new ArrayList<>();
    private RecycleCommonAdapter<User> recycleCommonAdapter;

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
    public void showSearchRes(ArrayList<User> users) {
        if (users != null) {
            mUsers.clear();
            mUsers.addAll(users);
            recycleCommonAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showError(String message) {
        showToastMsg(message);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.acticvity_layout_search;
    }

    @Override
    protected SearchActvityView bindView() {
        return this;
    }

    @Override
    protected SearchActPresenter bindPresenter() {
        return new SearchActPresenter(MApplication.mContext);
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider, 1));
        recycleCommonAdapter = new RecycleCommonAdapter<User>(
                MApplication.mContext, mUsers, R.layout.layout_search_user_item) {
            @Override
            public void bindItemData(SViewHolder holder, User itemData, int position) {
                holder.setText(R.id.tvName, itemData.nickname.get());
            }
        };
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                if (mUsers != null && mUsers.size() >= position) {
                    Constant.CURRENT_ITEM_USER =  mUsers.get(position);
                    UserInfoActivity.gotoThis(SearchActivity.this,true,Constant.CURRENT_ITEM_USER.id.get());
                }
            }
        });
        recycleview.setAdapter(recycleCommonAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //先初始化RxJava
        getPresenter().initPublishSubject();
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //搜索结果
                getPresenter().afterTextChanged(s.toString());
            }
        });

    }


    @OnClick({R.id.etSearch, R.id.btCancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.etSearch:
                break;
            case R.id.btCancle:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy();
        super.onDestroy();

    }
}
