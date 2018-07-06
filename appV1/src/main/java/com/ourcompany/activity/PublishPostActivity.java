package com.ourcompany.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.adapter.ImagePickerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.Post;
import com.ourcompany.presenter.activity.PublishPostActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.InputMethodUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.PublishPostActView;
import com.ourcompany.widget.LoadingViewAOV;
import com.ourcompany.widget.recycleview.commadapter.GridItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import imagepicker.ImagePicker;
import imagepicker.bean.ImageItem;
import imagepicker.loader.GlideImageLoader;
import imagepicker.ui.ImageGridActivity;
import imagepicker.ui.ImagePreviewDelActivity;
import imagepicker.view.CropImageView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/8 09:59
 * Des    :
 */

public class PublishPostActivity extends MvpActivity<PublishPostActView, PublishPostActPresenter> implements PublishPostActView, ImagePickerAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.btPublish)
    Button btPublish;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    private ImagePicker imagePicker;
    private ArrayList<ImageItem> selImageList;
    private ImagePickerAdapter adapter;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int REQUEST_CODE_SELECT = 100;
    private Dialog dialogProgress;
    private TextView tvTip;
    private TextView tvProgress;

    @Override
    public void showToastMsg(String string) {
        if (!TextUtils.isEmpty(string)) {
            ToastUtils.showSimpleToast(string);
        }

    }

    @Override
    protected void initView() {
        super.initView();

        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImagePicker.getInstance().getSelectImageCount()>0 || !TextUtils.isEmpty(etContent.getText().toString())){
                    showExistDialog();
                }else{
                    finish();
                }


            }
        });

        selImageList = new ArrayList<ImageItem>();
        adapter = new ImagePickerAdapter(this, selImageList, Constant.MAX_IMAGE_COUNT, true);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.addItemDecoration(new GridItemDecoration(MApplication.mContext, R.drawable.grid_item_decoration));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);
    }

    private void showExistDialog() {

        //弹出选框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(ResourceUtils.getString(R.string.exist_eidt_publish));
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
                overridePendingTransition(0, 0);
            }
        });

        builder.setCancelable(false);
        Dialog dialog = builder.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.3f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initImagePicker();
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_post;
    }

    @Override
    protected PublishPostActView bindView() {
        return this;
    }

    @Override
    protected PublishPostActPresenter bindPresenter() {
        return new PublishPostActPresenter(MApplication.mContext);
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                ImagePicker.getInstance().setSelectLimit(
                        Constant.MAX_IMAGE_COUNT);


                Intent intent = new Intent(PublishPostActivity.this,
                        ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);

                overridePendingTransition(com.imagepicker.R.anim.fade_in, com.imagepicker.R.anim.fade_out);

                break;
            default:
                // 打开预览
                Intent intentPreview = new Intent(this,
                        ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS,
                        (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION,
                        position);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
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
                selImageList.clear();
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            // 预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        } else if (resultCode == RESULT_OK
                && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
            // 发送广播通知图片增加了
            ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
            ImageItem imageItem = new ImageItem();
            imageItem.path = imagePicker.getTakeImageFile().getAbsolutePath();
            imageItem.name = imagePicker.getTakeImageFile().getName();
            //选中图片后拍照选中bug  2016-10-15
            //imagePicker.clearSelectedImages();
            imagePicker.addSelectedImageItem(0, imageItem, true);
            //修改拍照后的选中bug   2016-10-15 修改前的
            //selImageList.add(imageItem);
            //修改拍照后的选中bug   2016-10-15  修改后的
            selImageList.clear();
            selImageList.addAll(imagePicker.getSelectedImages());
            adapter.setImages(selImageList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imagePicker != null) {
            imagePicker.clear();
        }
        if(mHandler!=null)
        mHandler.removeCallbacksAndMessages(null);
    }


    @OnClick(R.id.btPublish)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btPublish:
                InputMethodUtils.hideKeyboard(etContent);
                //检查所有的工作流程，包括压缩图片，文字检查，图片检查，完毕后批量上传
                LoadingViewAOV.getInstance().with(PublishPostActivity.this,btPublish,R.color.colorPrimary);
                String text = etContent.getText().toString();
                getPresenter().checkAllWorkflow(text);

                break;
        }
    }

    public void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(ResourceUtils.getString(R.string.str_send_wait));
        View view = View.inflate(this, R.layout.layout_dialog_upload_progress, null);
        tvTip = view.findViewById(R.id.tvTip);
        tvProgress = view.findViewById(R.id.tvProgress);

        builder.setView(view);
//        builder.setNegativeButton("取消", null);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//                finish();
//                overridePendingTransition(0, 0);
//            }
//        });

        builder.setCancelable(false);
        dialogProgress = builder.show();
        WindowManager.LayoutParams lp = dialogProgress.getWindow().getAttributes();
        lp.dimAmount = 0.3f;
        dialogProgress.getWindow().setAttributes(lp);
        dialogProgress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void uploadSuccess(Post newPost) {
        LoadingViewAOV.getInstance().close(PublishPostActivity.this,btPublish);
        if (dialogProgress != null && dialogProgress.isShowing()) {
            dialogProgress.dismiss();
        }
        //发送给FragmenNews
        EventBus.getDefault().post(newPost);
        showToastMsg("发送成功");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);

    }

    @Override
    public void uploadImageProgress(String tip, String progress) {
        if (TextUtils.isEmpty(tip) || TextUtils.isEmpty(progress)) {
            return;
        }

        if (dialogProgress != null && dialogProgress.isShowing() ) {

        }else{
            showProgressDialog();
        }
        if( tvTip != null && tvProgress != null){
            tvTip.setText(tip);
            tvProgress.setText(progress);
        }
    }

    @Override
    public void dealImageError(String errormsg) {
        if (dialogProgress != null && dialogProgress.isShowing()) {
            dialogProgress.dismiss();
        }
        showToastMsg(errormsg);
    }

    @Override
    public void verifySubmitError() {
        LoadingViewAOV.getInstance().close(PublishPostActivity.this,btPublish);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(ImagePicker.getInstance().getSelectImageCount()>0 || !TextUtils.isEmpty(etContent.getText().toString())){
                showExistDialog();
                return false;
            }else{
                return super.onKeyDown(keyCode, event);
            }

        }else {
            return super.onKeyDown(keyCode, event);
        }
    }


}
