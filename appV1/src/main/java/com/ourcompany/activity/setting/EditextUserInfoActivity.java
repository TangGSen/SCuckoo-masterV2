package com.ourcompany.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mob.jimu.query.data.DataType;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.UserAccoutLoginRes;
import com.ourcompany.bean.json.UserType;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.presenter.activity.EditextActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.InputMethodUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.EditextUserInfoActView;
import com.ourcompany.widget.LoadingViewAOV;
import com.ourcompany.widget.recycleview.commadapter.ImageLoader;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import company.com.commons.util.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import imagepicker.ImagePicker;
import imagepicker.bean.ImageItem;
import imagepicker.loader.GlideImageLoader;
import imagepicker.ui.ImageGridActivity;
import imagepicker.view.CropImageView;

/**
 * /storage/emulated/0/games/chess/img/nopack/download/7BF6F90C11B258EE5ACF3C36D3504B8D.png
 */

public class EditextUserInfoActivity extends MvpActivity<EditextUserInfoActView, EditextActPresenter> implements EditextUserInfoActView {
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    @BindView(R.id.tvImage)
    TextView tvImage;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.tvNick)
    TextView tvNick;
    @BindView(R.id.ed_nickName)
    EditText edNickName;
    @BindView(R.id.tvIdentity)
    TextView tvIdentity;
    @BindView(R.id.tvIdentityName)
    TextView tvIdentityName;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.btSubmit)
    TextView btSubmit;
    private ImagePicker imagePicker;
    private String imagUrl;
    private List<UserType.UserTypeBean> mUserTypeList = new ArrayList<>();
    private int currentChoose = -1;
    private RecycleCommonAdapter recycleCommonAdapter;
    private PopupWindow popupWindow;
    private HashMap<String, Object> userInfos;
    private boolean isChangeImageInfo;

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, EditextUserInfoActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void windowsSetting() {
        super.windowsSetting();
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(true, EditextUserInfoActivity.this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_ed_info;
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setTitle(ResourceUtils.getString(R.string.str_ed_my_info));
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        edNickName.setFocusable(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvIdentityName.setText(MServiceManager.getInstance().getLoginUserTypeName());
                imgUser.setTag(R.id.loading_image_url, MServiceManager.getInstance().getLocalUserImage());
                ImageLoader.getImageLoader().loadImage(imgUser, "");
                edNickName.setText(MServiceManager.getInstance().getLocalUserName());
            }
        }, 50);

        btSubmit.setVisibility(View.GONE);
        edNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                checkIsCanSubmit();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        imagePicker.setCrop(true); // 允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); // 是否按矩形区域保存
        imagePicker.setSelectLimit(Constant.MAX_IMAGE_COUNT); // 选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE); // 裁剪框的形状
        imagePicker.setFocusWidth(800); // 裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800); // 裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000); // 保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000); // 保存文件的高度。单位像素
        imagePicker.setSelectLimit(1);
    }

    @Override
    protected EditextUserInfoActView bindView() {
        return this;
    }

    @Override
    protected EditextActPresenter bindPresenter() {
        return new EditextActPresenter(this);
    }


    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            // 添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                //清除当前界面的以选中的图片list   2016-10-15
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    uploadImage(images.get(0).getPath());
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            // 预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null && images.size() > 0) {
                    uploadImage(images.get(0).getPath());
                }
            }
        } else if (resultCode == RESULT_OK
                && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
            // 发送广播通知图片增加了
            ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
            String path = imagePicker.getTakeImageFile().getAbsolutePath();
            uploadImage(path);
        }
    }


    private void uploadImage(String path) {

        LoadingViewAOV.getInstance().with(EditextUserInfoActivity.this, imgUser, android.R.color.transparent);
        getPresenter().dealUserImage(path);
    }

    @OnClick({R.id.tvImage, R.id.tvNick, R.id.tvIdentity, R.id.btSubmit, R.id.ed_nickName})
    public void onViewClicked(View view) {
        if (!MServiceManager.getInstance().getUserIsLogin()) {
            showToastMsg(ResourceUtils.getString(R.string.str_user_not_login));
            return;
        }
        if (view.getId() != R.id.tvNick || view.getId() != R.id.ed_nickName) {
            //将控件恢复状态
            InputMethodUtils.hideKeyboard(edNickName);
            edNickName.setFocusable(false);
            if (TextUtils.isEmpty(edNickName.getText().toString())) {
                String localName = MServiceManager.getInstance().getLocalUserName();
                edNickName.setText(localName);
            }
        }


        switch (view.getId()) {
            case R.id.tvImage:
                imagePicker.clear();
                Intent intent = new Intent(EditextUserInfoActivity.this,
                        ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                overridePendingTransition(com.imagepicker.R.anim.fade_in, com.imagepicker.R.anim.fade_out);
                break;
            case R.id.ed_nickName:
            case R.id.tvNick:
                InputMethodUtils.toggleSoftInputForEt(edNickName);
                //将光标移动到后面
                edNickName.setSelection(edNickName.getText().toString().length());
                break;
            case R.id.tvIdentity:
                if (mUserTypeList != null && mUserTypeList.size() > 0) {
                    showUserTypeDialog();
                } else {
                    LoadingViewAOV.getInstance().with(EditextUserInfoActivity.this, tvIdentityName, R.color.whiles, R.drawable.ic_loading_v4, Gravity.RIGHT);
                    getPresenter().getIdentityName();
                }
                break;
            case R.id.btSubmit:

                submitInfo();
                break;
        }
    }

    /**
     * 提交信息
     */
    private void submitInfo() {
        edNickName.setFocusable(false);
        LoadingViewAOV.getInstance().with(EditextUserInfoActivity.this, btSubmit, R.color.colorPrimary);
        if(userInfos==null){
            userInfos = new HashMap<>();
        }
        if (currentChoose != -1 &&
                currentChoose != MServiceManager.getInstance().getLoginUserType() && mUserTypeList != null && mUserTypeList.size() > 0) {
            /**一定为String*/
            DataType<String> userTypeName = new DataType<String>(mUserTypeList.get(currentChoose).getTypeName()) {
            };
            DataType<String> userTypeValue = new DataType<String>(currentChoose + "") {
            };
            userInfos.put(Constant.UMSDK_COMSTOR_KEY_USER_TYPE_VALUE, userTypeValue);
            userInfos.put(Constant.UMSDK_COMSTOR_KEY_USER_TYPE, userTypeName);

        }
        String localName = MServiceManager.getInstance().getLocalUserName();
        String edText = edNickName.getText().toString();
        if (!TextUtils.isEmpty(edText)) {
            if (!edText.equals(localName)) {
                userInfos.put(Constant.CURRENT_USER.nickname.getName(), edText);
            }

        } else {
            edNickName.setText(localName);
        }
        if (userInfos.size() > 0) {
            getPresenter().sumbmitInfo(userInfos);
        } else {
            LoadingViewAOV.getInstance().close(EditextUserInfoActivity.this, btSubmit);
            finish();
        }


    }

    @Override
    public void showUploadSuccess(HashMap<String, Object> res, final String path) {
        //可以显示
        isChangeImageInfo = true;
        checkIsCanSubmit();
        imgUser.setTag(R.id.loading_image_url, path);
        ImageLoader.getImageLoader().loadImage(EditextUserInfoActivity.this, imgUser, "");
        LoadingViewAOV.getInstance().close(EditextUserInfoActivity.this, imgUser);
        this.userInfos = res;
    }

    @Override
    public void showUploadFailed() {
        LoadingViewAOV.getInstance().close(EditextUserInfoActivity.this, imgUser);
        showToastMsg("头像上传失败");
    }

    @Override
    public void showUserTypeView(List<UserType.UserTypeBean> userType) {
        this.mUserTypeList = userType;
        int current = MServiceManager.getInstance().getLoginUserType();
        if (current >= 0 && current < userType.size()) {
            currentChoose = current;
            mUserTypeList.get(current).setChooese(true);
        }
        showUserTypeDialog();
        LoadingViewAOV.getInstance().close(EditextUserInfoActivity.this, tvIdentityName);

    }

    /**
     * 获取身份info
     */
    private void showUserTypeDialog() {
        //弹出选框
        popupWindow = new PopupWindow(EditextUserInfoActivity.this);
        View view = LayoutInflater.from(EditextUserInfoActivity.this).inflate(R.layout.layout_item_popupwindows, null);
        final RecyclerView recycleview = view.findViewById(R.id.recycleview);
        view.findViewById(R.id.outSideView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycleview.clearAnimation();
                popupWindow.dismiss();
            }
        });
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.bg_pupowidowns));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //解决嵌套在NestedScrollView 的滑动不顺的问题1

        recycleview.setLayoutManager(linearLayoutManager);
        if (recycleCommonAdapter == null) {
            recycleCommonAdapter = new RecycleCommonAdapter<UserType.UserTypeBean>(
                    MApplication.mContext, mUserTypeList, R.layout.layout_item_user_type) {
                @Override
                public void bindItemData(SViewHolder holder, final UserType.UserTypeBean itemData, int position) {
                    holder.setText(R.id.tvTypeName, itemData.getTypeName());
                    ((ImageView) holder.getView(R.id.imgChoose)).setSelected(itemData.isChooese());
                }
            };
            recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
                @Override
                public void itemOnclickLinstener(int position) {
                    if (currentChoose != -1) {
                        mUserTypeList.get(currentChoose).setChooese(false);
                        recycleCommonAdapter.notifyItemChanged(currentChoose);
                    }
                    currentChoose = position;
                    mUserTypeList.get(position).setChooese(true);
                    recycleCommonAdapter.notifyItemChanged(position);
                    tvIdentityName.setText(mUserTypeList.get(position).getTypeName());
                    popupWindow.dismiss();
                    checkIsCanSubmit();
                }
            });
        }
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleview.startAnimation(AnimationUtils.loadAnimation(
                EditextUserInfoActivity.this, R.anim.fade_in));
        popupWindow.showAtLocation(getRootView(), Gravity.BOTTOM, 0, 0);

    }


    @Override
    public void showErrorUseTypeView() {

    }

    @Override
    public void updateInfoFaild() {
        LoadingViewAOV.getInstance().close(EditextUserInfoActivity.this, btSubmit);

    }

    /**
     * 检查完成的bt 是否可以提交
     */
    private void checkIsCanSubmit() {
        boolean isNickNameChange = false;
        String localName = MServiceManager.getInstance().getLocalUserName();
        String edText = edNickName.getText().toString();
        if (!TextUtils.isEmpty(edText)) {
            if (!edText.equals(localName)) {
                isNickNameChange = true;
            }
        }
        if (isChangeImageInfo || currentChoose != -1 && currentChoose != MServiceManager.getInstance().getLoginUserType() || isNickNameChange) {
            //可以显示
            btSubmit.setVisibility(View.VISIBLE);
        } else {
            btSubmit.setVisibility(View.GONE);

        }

    }

    @Override
    public void updateInfoSuccess() {
        //更新成功后那么关闭
        getPresenter().updateImageForBmobUser();
        //同时更新User本地User
    }

    /**
     * 更新 bmob user mob user 的所有信息，完毕的效果
     */
    @Override
    public void updateAllInfoFinish() {
        LoadingViewAOV.getInstance().close(EditextUserInfoActivity.this, btSubmit);
        EventBus.getDefault().post(new UserAccoutLoginRes().setLoginSuccess(true));
        finish();
        overridePendingTransition(0, 0);

    }

    /**
     * 压缩图片出现问题
     *
     * @param errormsg
     */
    @Override
    public void dealImageError(String errormsg) {
        LoadingViewAOV.getInstance().close(EditextUserInfoActivity.this, imgUser);
        showToastMsg(errormsg);
    }


}
