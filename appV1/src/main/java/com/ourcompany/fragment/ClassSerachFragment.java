package com.ourcompany.fragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.UserClassifyActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.json.CuckooServiceJson;
import com.ourcompany.manager.ClassSerachService;
import com.ourcompany.presenter.fragment.ClassSerachFragPresenter;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.ClassSerachFragmentView;
import com.ourcompany.widget.recycleview.commadapter.GridItemDecoration;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:02
 * Des    :
 */

public class ClassSerachFragment extends MvpFragment<ClassSerachFragmentView, ClassSerachFragPresenter> implements ClassSerachFragmentView {


    Unbinder unbinder1;
    @BindView(R.id.viewOfTitle)
    View viewOfTitle;
    @BindView(R.id.cuckooService)
    TextView cuckooService;
    @BindView(R.id.recyclerView)
    RecyclerView recycleview;
    @BindView(R.id.viewOfTitle2)
    View viewOfTitle2;
    @BindView(R.id.allClass)
    TextView allClass;
    @BindView(R.id.classGroup)
    RadioGroup classGroup;
    @BindView(R.id.navContent)
    ConstraintLayout navContent;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.btReset)
    TextView btReset;
    @BindView(R.id.btFinish)
    TextView btFinish;
    Unbinder unbinder;
    private RecycleCommonAdapter<CuckooServiceJson.CuckooServiceBean> recycleCommonAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_class_serach;
    }

    @Override
    protected ClassSerachFragmentView bindView() {
        return this;
    }

    @Override
    protected ClassSerachFragPresenter bindPresenter() {
        return new ClassSerachFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    public static ClassSerachFragment getInstance() {
        //通过id 获得 用户信息 or Group 信息
        ClassSerachFragment fragment = new ClassSerachFragment();
        Bundle bundle = new Bundle();
//        bundle.putBoolean("isgroup", isGroup);
//        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(MApplication.mContext, 3);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.addItemDecoration(new GridItemDecoration(MApplication.mContext, R.drawable.grid_item_decoration));

        //解决嵌套在NestedScrollView 的滑动不顺的问题2
        recycleview.setNestedScrollingEnabled(true);
        recycleview.setHasFixedSize(true);
        recycleCommonAdapter = new RecycleCommonAdapter<CuckooServiceJson.CuckooServiceBean>(
                MApplication.mContext, ClassSerachService.getInstance().getKeyWordList(), R.layout.layout_item_cuckoo_service) {
            @Override
            public void bindItemData(SViewHolder holder, final CuckooServiceJson.CuckooServiceBean itemData, int position) {
                holder.setText(R.id.tvKeyWord, itemData.getKey());
                holder.getView(R.id.tvKeyWord).setSelected(itemData.isSeleted());
            }
        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                //改变key list 的数据
                ClassSerachService.getInstance().changeKeyWordList(position);
                 recycleCommonAdapter.notifyItemChanged(position);
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        ClassSerachService.getInstance().clear();
        getPresenter().getCuckooService();
    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void showDataView(CuckooServiceJson serviceJson) {
        if (serviceJson.getCuckooClass() != null) {
            int length = serviceJson.getCuckooClass().size();
            ClassSerachService.getInstance().getClassList().addAll(serviceJson.getCuckooClass());
            for (int i = 0; i < length; i++) {
                RadioButton radioView = (RadioButton) View.inflate(MApplication.mContext, R.layout.layout_item_radiobutton, null);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(0, 0, DisplayUtils.dip2px(8), 0);
                radioView.setLayoutParams(params);
                radioView.setText(serviceJson.getCuckooClass().get(i).getClassType());
                radioView.setTag(R.id.tag_position, i);
                radioView.setId(i);
                if (i == 0) {
                    radioView.setChecked(true);
                    ClassSerachService.getInstance().changeClassData(0,true);
                }
                classGroup.addView(radioView, params);
            }
            classGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                    //找到对应的button
                    RadioButton button = radioGroup.findViewById(checkId);
                    ClassSerachService.getInstance().changeClassData((Integer) button.getTag(R.id.tag_position),false);
                }
            });
        }
        if (serviceJson.getCuckooService() != null) {
            ClassSerachService.getInstance().getKeyWordList().addAll(serviceJson.getCuckooService());
            recycleCommonAdapter.notifyDataSetChanged();
        }


    }



    @Override
    public void showErrorView() {
    }

    @Override
    public void resetSerachSuccuss() {
        recycleCommonAdapter.notifyDataSetChanged();
        ((RadioButton) classGroup.getChildAt(0)).setChecked(true);
    }


    @OnClick({R.id.btReset, R.id.btFinish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btReset:
                getPresenter().resetSerach();
                break;
            case R.id.btFinish:
                ((UserClassifyActivity) mActivity).closeDrawer();
                break;
        }
    }


}
