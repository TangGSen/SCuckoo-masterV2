package imagepicker.loader;

import android.app.Activity;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.imagepicker.R;

public class ProgressBarGlideImageLoader implements ImageLoader {
	private Handler handler;
	private int whatMsg;

	public ProgressBarGlideImageLoader(Handler handler,int whatMsg) {
		this.handler =  handler;
		this.whatMsg = whatMsg;
	}

	@Override
	public void displayImage(final Activity activity, String path,
			ImageView imageView, int width, int height) {
		Glide.with(activity)
				// 配置上下文
				.load(path)
				// 设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
				.error(R.drawable.defulat_image_bg)
				// 设置错误图片
				.placeholder(R.drawable.defulat_image_bg)
				// 设置占位图片
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				// 缓存全尺寸
				.listener(new RequestListener<String, GlideDrawable>() {

					@Override
					public boolean onException(Exception arg0, String arg1,
							Target<GlideDrawable> arg2, boolean arg3) {
						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable arg0,
							String arg1, Target<GlideDrawable> arg2,
							boolean arg3, boolean arg4) {
						if (handler != null) {
							handler.removeMessages(whatMsg);
							handler.sendEmptyMessage(whatMsg);
						}
						return false;
					}
					

				}).into(imageView);

		// Glide.with(activity) //配置上下文
		// .load(path) //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
		// .error(R.mipmap.default_image) //设置错误图片
		// .placeholder(R.mipmap.default_image) //设置占位图片
		// .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
		// .into(imageView);

		// else {
		// Glide.with(activity) //配置上下文
		// .load(Uri.fromFile(new File(path))) //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
		// .error(R.mipmap.default_image) //设置错误图片
		// .placeholder(R.mipmap.default_image) //设置占位图片
		// .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
		// .into(imageView);
		//
		// }

	}

	@Override
	public void clearMemoryCache() {
	}
}
