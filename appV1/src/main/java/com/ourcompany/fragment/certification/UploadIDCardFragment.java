package com.ourcompany.fragment.certification;

import android.content.Intent;
import android.support.constraint.Guideline;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.CertifiUploadIDCardPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.UploadIDCardFragmentView;
import com.ourcompany.widget.recycleview.commadapter.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;
import imagepicker.ImagePicker;
import imagepicker.bean.ImageItem;
import imagepicker.loader.GlideImageLoader;
import imagepicker.ui.ImageGridActivity;
import imagepicker.view.CropImageView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/30 上午9:33
 * Des    : 图片的Url 放在view setTag 取得时候方便
 */
public class UploadIDCardFragment extends MvpFragment<UploadIDCardFragmentView, CertifiUploadIDCardPresenter> implements UploadIDCardFragmentView {
    public static final int REQUEST_CODE_SELECT = 100;
    @BindView(R.id.tvUploadTip)
    TextView tvUploadTip;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.fristImage)
    ImageView fristImage;
    @BindView(R.id.tvFristTip)
    TextView tvFristTip;
    @BindView(R.id.secondImage)
    ImageView secondImage;
    @BindView(R.id.tvSecondTip)
    TextView tvSecondTip;
    Unbinder unbinder;
    @BindView(R.id.btNext)
    Button btNext;
    Unbinder unbinder1;
    private ImagePicker imagePicker;

    private int currentImageId;

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cerifi_upload_id;
    }

    @Override
    protected void initData() {
        super.initData();
        initImagePicker();
    }

    @Override
    protected UploadIDCardFragmentView bindView() {
        return this;
    }

    @Override
    protected CertifiUploadIDCardPresenter bindPresenter() {
        return new CertifiUploadIDCardPresenter(MApplication.mContext);
    }


    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader()); // 设置图片加载器
        imagePicker.setShowCamera(true); // 显示拍照按钮
        imagePicker.setCrop(false); // 允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); // 是否按矩形区域保存
        imagePicker.setSelectLimit(Constant.MAX_IMAGE_COUNT); // 选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE); // 裁剪框的形状
        imagePicker.setFocusWidth(800); // 裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800); // 裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000); // 保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000); // 保存文件的高度。单位像素
    }


    @OnClick({R.id.fristImage, R.id.secondImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fristImage:
            case R.id.secondImage:
                ImagePicker.getInstance().clear();
                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent = new Intent(mActivity, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                mActivity.overridePendingTransition(com.imagepicker.R.anim.fade_in, com.imagepicker.R.anim.fade_out);
                currentImageId = view.getId();

                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            // 添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                //清除当前界面的以选中的图片list   2016-10-15
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    showImageView(images.get(0).path);
                }
            } else if (resultCode == mActivity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
                // 发送广播通知图片增加了
                ImagePicker.galleryAddPic(mActivity, imagePicker.getTakeImageFile());
                ImageItem imageItem = new ImageItem();
                imageItem.path = imagePicker.getTakeImageFile().getAbsolutePath();
                imageItem.name = imagePicker.getTakeImageFile().getName();
                imagePicker.addSelectedImageItem(0, imageItem, true);
                showImageView(imagePicker.getSelectedImages().get(0).path);

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imagePicker != null) {
            imagePicker.clear();
        }
    }

    private void showImageView(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (currentImageId == R.id.fristImage) {
            tvFristTip.setVisibility(View.GONE);
            fristImage.setTag(R.id.loading_image_url, path);
            ImageLoader.getImageLoader().loadImage(fristImage, path, R.drawable.ic_loading_defualt);
        } else if (currentImageId == R.id.secondImage) {
            tvSecondTip.setVisibility(View.GONE);
            secondImage.setTag(R.id.loading_image_url, path);
            ImageLoader.getImageLoader().loadImage(secondImage, path, R.drawable.ic_loading_defualt);
        }

        if (fristImage.getTag(R.id.loading_image_url) != null && secondImage.getTag(R.id.loading_image_url) != null) {
            btNext.setEnabled(true);
        }else{
            btNext.setEnabled(false     );
        }

    }

    
}
