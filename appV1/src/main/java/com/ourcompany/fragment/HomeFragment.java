package com.ourcompany.fragment;

import android.support.design.widget.CollapsingToolbarLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.UserClassifyActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.json.AdvertisementData;
import com.ourcompany.presenter.fragment.HomeFragPresenter;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.HomeFragView;
import com.ourcompany.widget.ImageCycleView;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    :
 */

public class HomeFragment extends MvpFragment<HomeFragView, HomeFragPresenter> implements HomeFragView {

    @BindView(R.id.head_ImageCycle)
    ImageCycleView headImageCycle;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    Unbinder unbinder;
    @BindView(R.id.layoutItemRoot)
    LinearLayout layoutItemRoot;
    private List<AdvertisementData.AdSettingBean> adDatas;
    private String[] mTiltes;
    int resId[] = new int[]{R.drawable.ic_design, R.drawable.ic_working, R.drawable.ic_supervisor, R.drawable.ic_repair, R.drawable.ic_learning};
    int resIdBg[] = new int[]{R.drawable.bg_gradient_violet, R.drawable.bg_gradient_violet, R.drawable.bg_gradient_blue_oval,
            R.drawable.bg_gradient_blue_oval, R.drawable.bg_gradient_violet};
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UserClassifyActivity.gotoThis(mActivity,mTiltes[(Integer)view.getTag(R.id.nine_layout_of_index)]);
        }
    };

    @Override
    protected void initView(View view) {
        super.initView(view);
        int commomHeight = (int) (DisplayUtils.getInstance(getActivity()).getScreenSize()[1] / 4);

        ViewGroup.LayoutParams params = headImageCycle.getLayoutParams();
        params.height = commomHeight;
        headImageCycle.setLayoutParams(params);

        ViewGroup.LayoutParams collapsingLayoutParams = collapsingToolbarLayout.getLayoutParams();
        collapsingLayoutParams.height = commomHeight;
        collapsingToolbarLayout.setLayoutParams(collapsingLayoutParams);


        mTiltes = ResourceUtils.getStringArray(R.array.MainChooseItems);
        int size = mTiltes.length;

        for (int i = 0; i < size; i++) {
            layoutItemRoot.addView(generateItemView(i), i);
        }

    }


    private View generateItemView(int position) {
        View iv = LayoutInflater.from(mActivity).inflate(R.layout.layout_item_main_choose, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        iv.setLayoutParams(params);
        iv.setTag(R.id.nine_layout_of_index, position);
        ImageView imageView = iv.findViewById(R.id.image);
        imageView.setImageDrawable(ResourceUtils.getDrawable(resId[position]));
        imageView.setBackground((ResourceUtils.getDrawable(resIdBg[position])));
        TextView textView = iv.findViewById(R.id.tvItemName);
        textView.setText(mTiltes[position]);
        iv.setOnClickListener(onClickListener);
        return iv;
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getAdsData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_act_home;
    }

    @Override
    protected HomeFragView bindView() {
        return this;
    }

    @Override
    protected HomeFragPresenter bindPresenter() {
        return new HomeFragPresenter(MApplication.mContext);
    }


    private int getTabLayoutHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getContext().getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    @Override
    public void showToastMsg(String string) {

    }


    @Override
    public void showAdDatas(List<AdvertisementData.AdSettingBean> adDatas, List<String> urlStr) {
        headImageCycle.setImageResources(urlStr, new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {

            }
        });
    }

    @Override
    public void showEmptyAdData() {
        showToastMsg("获取失败");
    }
}
