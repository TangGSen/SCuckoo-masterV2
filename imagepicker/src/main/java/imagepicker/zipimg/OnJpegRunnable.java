package imagepicker.zipimg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import imagepicker.ImagePicker;
import imagepicker.bean.ImageItem;

public class OnJpegRunnable implements Runnable {
	// private ArrayList<ImageItem> selImageList;
	private String target;
	private Handler mHandler;
	private int what;
	private Context mContext;
	//默认是90
	private int quality =90;

	public OnJpegRunnable(String targetDir, Handler handler, int what,int quality) {
		this.mHandler = handler;
		this.what = what;
		this.target = targetDir;
		this.quality  = quality;
		if(quality<=0 || quality>100){
			this.quality = 90;
		}
	}

	@Override
	public void run() {
		ArrayList<ImageItem> selImageList = ImagePicker.getInstance()
				.getSelectedImages();
		File dirFile = new File(target);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		int count = selImageList.size();
		if (count <= 0) {
			mHandler.sendEmptyMessage(what);
			return;
		}
		try {

			for (int i = 0; i < count; i++) {

				// 先判断有没有压缩过
				String fileName = selImageList.get(i).name;
//				File desFile = new File(dirFile, fileName + ".thumb");
				File desFile = new File(dirFile, fileName );
				File srcFile = new File(selImageList.get(i).path);
				//少于0.1M 就不压了
//				if (srcFile.length()/1024/1024<1) {
//					continue;
//				}
				float fileSize = srcFile.length()/1024/1024;
				if (fileSize<0.5f) {
					quality =100;
				}else if(fileSize>=0.5f &&fileSize<0.8){
					quality =90;
				}else if(fileSize>=0.8f &&fileSize<1.5){
					quality =80;
				}else if(fileSize>=1.5f &&fileSize<6){
					quality =70;
				}else if(fileSize>=6){
					quality =60;
				}
				if (!desFile.exists()) {

					//quality = 100;// original
					// 可以与原生的压缩方法对比一下，同样设置成50效果如何
					File jpegTrueFile = new File(target, fileName);
					if (jpegTrueFile.exists()) {
						continue;
					}
					
					InputStream is = new FileInputStream(srcFile.getAbsolutePath());
					//2.为位图设置100K的缓存
					BitmapFactory.Options opts=new BitmapFactory.Options();
					opts.inTempStorage = new byte[100 * 1024];
					opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
					//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
					opts.inPurgeable = true;
					//5.设置位图缩放比例
					//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
					opts.inSampleSize = 2;
					//6.设置解码位图的尺寸信息
					opts.inInputShareable = true; 
					//7.解码位图
					Bitmap bitmap =BitmapFactory.decodeStream(is,null, opts);   
					NativeUtil.compressBitmap(bitmap, quality,
							jpegTrueFile.getAbsolutePath(), true);
					
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("sensen",e.getMessage());
			mHandler.sendEmptyMessage(what);
		}
		mHandler.sendEmptyMessage(what);

	}

}
