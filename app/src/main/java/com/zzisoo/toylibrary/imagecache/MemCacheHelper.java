package com.zzisoo.toylibrary.imagecache;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class MemCacheHelper {
	public static String getDiskCacheDir(Context context) {
		String cachePath = (Environment.getExternalStorageState() == "mounted")
				|| (!ImageUtils.isExternalStorageRemovable()) ? ImageUtils.getExternalCacheDir(context).getPath() : context.getExternalFilesDir(null)+"";
		return cachePath + File.separator + ".thumbnail"/*ImageLoader.IMAGE_CACHE_DIR*/;
	}
	
	//stan++;
	public static String getEncryptedFilePath(String key) {
		/*try {
			key = ImageUtils.encryptString(key);
			return "cache_"	+ URLEncoder.encode(key.replace("*", ""), "UTF-8").replace(" ", "");
		} catch (UnsupportedEncodingException e) {
			MyLog.e("MemCacheHelper", "createFilePath - " + e);
		}*/
		Md5FileNameGenerator generator = new Md5FileNameGenerator();
		return generator.generate(key);
		//return null;
	}
}
