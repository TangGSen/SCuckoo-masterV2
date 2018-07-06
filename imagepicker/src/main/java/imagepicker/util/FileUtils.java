package imagepicker.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileUtils {
	public static void createDir(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

	}
	
	
	
	
	public static String getDiskCacheDir(Context context) {  
	    String cachePath = null;  
	    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
	            || !Environment.isExternalStorageRemovable()) {  
	        cachePath = context.getExternalCacheDir().getPath();  
	    } else {  
	        cachePath = context.getCacheDir().getPath();  
	    }  
	    return cachePath;  
	} 
	
	
	
	public static void deleteAllFiles(File root) { 
		try {
			File files[] = root.listFiles();  
	        if (files != null)  
	            for (File f : files) {  
	                if (f.isDirectory()) { // 判断是否为文件夹  
	                    deleteAllFiles(f);  
	                    try {  
	                        f.delete();  
	                    } catch (Exception e) {  
	                    }  
	                } else {  
	                    if (f.exists()) { // 判断是否存在  
	                        deleteAllFiles(f);  
	                        try {  
	                            f.delete();  
	                        } catch (Exception e) {  
	                        }  
	                    }  
	                }  
	            }  
	    }  
			
		 catch (Exception e) {
			// TODO: handle exception
		}
	}
}
