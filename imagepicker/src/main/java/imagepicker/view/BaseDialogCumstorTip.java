package imagepicker.view;

import android.R.integer;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagepicker.R;

/**
 * Created by Administrator on 2016/3/21.
 */
public class BaseDialogCumstorTip {
	private DialogButtonOnclickLinster mDialogListener = null;

	private DialogOnClickLinster mDialogOnClickLinster = null;

	private volatile static BaseDialogCumstorTip single;

	private CustomerDialog loadingDialog;

	private ImageView loading_image;

	private Animation loadingAnimation;

	private BaseDialogCumstorTip() {
	}

	public static BaseDialogCumstorTip getDefault() {
		if (single == null) {
			synchronized (BaseDialogCumstorTip.class) {
				if (single == null) {
					single = new BaseDialogCumstorTip();
				}
			}
		}
		return single;
	}

	public interface DialogButtonOnclickLinster {
		void onButtonClick(CustomerDialog dialog);

	}

	public interface DialogOnClickLinster {
		void onLeftClick(CustomerDialog dialog);

		void onRightClick(CustomerDialog dialog);

	}

	public void showOneTMsgBtn(DialogButtonOnclickLinster dialogListener,
			Context context, String msg, String btnString) {
		this.mDialogListener = dialogListener;
		final CustomerDialog btnDialog = new CustomerDialog(context, 220, 140,
				R.layout.base_one_dialog, R.style.Theme_dialog);
		btnDialog.setCanceledOnTouchOutside(false);
		btnDialog.setCancelable(false);
		btnDialog.show();
		TextView txt_message = (TextView) btnDialog
				.findViewById(R.id.tv_one_msg);
		TextView btn_ok = (TextView) btnDialog.findViewById(R.id.one_btn_ok);
		txt_message.setText(msg);
		btn_ok.setText(btnString);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mDialogListener != null) {
					mDialogListener.onButtonClick(btnDialog);
				}
			}
		});

	}

	public void showOneTMsgBtnNet(DialogButtonOnclickLinster dialogListener,
			Context context, String msg, String btnString, int drawable) {
		this.mDialogListener = dialogListener;
		final CustomerDialog btnDialog = new CustomerDialog(context, 280, 220,
				R.layout.base_iamg_dialog, R.style.Theme_dialog);
		btnDialog.setCanceledOnTouchOutside(false);
		btnDialog.setCancelable(false);
		btnDialog.show();
		ImageView imageView = (ImageView) btnDialog.findViewById(R.id.image);
		imageView
				.setImageDrawable(context.getResources().getDrawable(drawable));
		TextView txt_message = (TextView) btnDialog
				.findViewById(R.id.tv_one_msg);
		TextView btn_ok = (TextView) btnDialog.findViewById(R.id.one_btn_ok);
		txt_message.setText(msg);
		btn_ok.setText(btnString);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mDialogListener != null) {
					mDialogListener.onButtonClick(btnDialog);
				}
			}
		});

	}

	public void showMsg(DialogOnClickLinster dialogListener, Context context,
			String msg, String leftBtn, String rigthBtn) {
		this.mDialogOnClickLinster = dialogListener;
		final CustomerDialog btnDialog = new CustomerDialog(context, 280, 140,
				R.layout.base_twobtn_dialog, R.style.Theme_dialog);
		btnDialog.setCanceledOnTouchOutside(false);
		btnDialog.setCancelable(false);
		btnDialog.show();
		TextView txt_message = (TextView) btnDialog
				.findViewById(R.id.twobtn_txt_msg);
		TextView btn_ok = (TextView) btnDialog.findViewById(R.id.dialog_btn_ok);
		TextView btn_cancle = (TextView) btnDialog
				.findViewById(R.id.btn_cancle);
		txt_message.setText(msg);
		btn_ok.setText(leftBtn);
		btn_cancle.setText(rigthBtn);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mDialogOnClickLinster != null) {
					mDialogOnClickLinster.onLeftClick(btnDialog);
				}
			}
		});

		btn_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mDialogOnClickLinster != null) {
					mDialogOnClickLinster.onRightClick(btnDialog);
				}
			}
		});

	}

	public void showLoadingWiat(Context context, int anim, int drawable) {
		loadingDialog = new CustomerDialog(context, 56, 56,
				R.layout.base_loading_dialog, R.style.Theme_dialog);
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setCancelable(false);
		loadingDialog.show();
		loading_image = (ImageView) loadingDialog.findViewById(R.id.loading_image);
		loading_image.setImageDrawable(context.getResources().getDrawable(
				drawable));
		loadingAnimation = AnimationUtils.loadAnimation(context, anim);
		LinearInterpolator interpolator = new LinearInterpolator(); // 设置匀速旋转，在xml文件中设置会出现卡顿
		loadingAnimation.setInterpolator(interpolator);
		if (loadingAnimation != null) {
			loading_image.startAnimation(loadingAnimation); // 开始动画
		}
	}

	public void stopLoadingWait() {
		if (loadingAnimation != null && loadingDialog != null
				&& loadingDialog.isShowing()) {
			loading_image.clearAnimation();

			loadingDialog.dismiss();
		}

	}

}
