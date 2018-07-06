package com.ourcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.adapter.ImagePagePreviewAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.activity.ImagePreviewActPresenter;
import com.ourcompany.view.activity.ImagePreviewActView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import imagepicker.ImagePicker;
import imagepicker.loader.GlideImageLoader;
import imagepicker.view.ViewPagerFixed;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/21 16:15
 * Des    :
 */

public class ImagesPreViewActvitity extends MvpActivity<ImagePreviewActView, ImagePreviewActPresenter> implements ImagePreviewActView {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.titleBar)
    RelativeLayout titleBar;
    @BindView(R.id.mViewPager)
    ViewPagerFixed mViewPager;
    @BindView(R.id.progressBar)
    ImageView progressBar;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    private ImagePagePreviewAdapter mAdapter;
    private int mCurrentPosition ;
    private int size = 0;
    private static final String KEY_INTENT = "key_intent";
    private static final String KEY_BUNDLE = "key_bundle";
    private static final String KEY_VALUE_INDEX = "key_value_index";
    private ArrayList<String> mUrls;

    @Override
    public void showToastMsg(String string) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mpreview_image;
    }

    @Override
    protected ImagePreviewActView bindView() {
        return this;
    }

    @Override
    protected ImagePreviewActPresenter bindPresenter() {
        return new ImagePreviewActPresenter(MApplication.mContext);
    }

    public static void gotoThis(Context context, ArrayList<String> urls, int index) {
        if (urls == null) {
            return;
        }
        Intent intent = new Intent(context, ImagesPreViewActvitity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_BUNDLE, urls);
        bundle.putInt(KEY_VALUE_INDEX, index);
        intent.putExtra(KEY_INTENT, bundle);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {

        Bundle bun = getIntent().getBundleExtra(KEY_INTENT);
        if (bun != null) {
            mUrls = bun.getStringArrayList(KEY_BUNDLE);
            mCurrentPosition = bun.getInt(KEY_VALUE_INDEX);
            mCurrentPosition =mCurrentPosition<=mUrls.size()-1 && mCurrentPosition>=0?mCurrentPosition:0;
        }
        if (mUrls == null) {
            mUrls = new ArrayList<>();
        }
        return super.initArgs(bundle);
    }

    @Override
    protected void initView() {
        super.initView();
        //
       // StatusBarUtil.setColor(this,R.color.black);
        // 滑动ViewPager的时候，根据外界的数据改变当前的选中状态和当前的图片的位置描述文本
        mViewPager
                .addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        tvNumber.setText((position + 1) + "/" + size);
                    }
                });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        size = mUrls.size();
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader()); // 设置图片加载器
        mAdapter = new ImagePagePreviewAdapter(this, mUrls);

        mAdapter.setPhotoViewClickListener(new ImagePagePreviewAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                //  onImageSingleTap();
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition, false);
        tvNumber.setText((mCurrentPosition + 1) + "/" + size);
    }

    @OnClick(R.id.btnBack)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
