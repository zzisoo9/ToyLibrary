package com.zzisoo.toylibrary.imagecache;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileNotFoundException;

public class ImageUtils {
	public static final int IO_BUFFER_SIZE = 8192;

	public static void disableConnectionReuseIfNecessary() {
		if (hasHttpConnectionBug())
			System.setProperty("http.keepAlive", "false");
	}

	@SuppressLint({ "NewApi" })
	public static int getBitmapSize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= 12) {
			return bitmap.getByteCount();
		}

		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	@SuppressLint({ "NewApi" })
	public static boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= 9) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	@SuppressLint({ "NewApi" })
	public static File getExternalCacheDir(Context context) {
		if (hasExternalCacheDir()) {
			return context.getExternalFilesDir(null);
		}

		String filesDir = "/Android/data/" + context.getPackageName() + "/files/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ filesDir);
	}
	
	

	@SuppressLint({ "NewApi" })
	public static long getUsableSpace(File path) {
		if (Build.VERSION.SDK_INT >= 9) {
			return path.getUsableSpace();
		}
		StatFs stats = new StatFs(path.getPath());
		return stats.getBlockSize() * stats.getAvailableBlocks();
	}

	public static int getMemoryClass(Context context) {
		return ((ActivityManager) context.getSystemService("activity"))
				.getMemoryClass();
	}

	public static boolean hasHttpConnectionBug() {
		return Build.VERSION.SDK_INT < 8;
	}

	public static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= 8;
	}

	public static boolean hasActionBar() {
		return Build.VERSION.SDK_INT >= 11;
	}

	public static String encryptString(String str) {
		String key = "toytoy";
		StringBuffer sb = new StringBuffer(str);

		int lenStr = str.length();
		int lenKey = key.length();

		int i = 0;
		for (int j = 0; i < lenStr; j++) {
			if (j >= lenKey)
				j = 0;
			sb.setCharAt(i, (char) (str.charAt(i) ^ key.charAt(j)));

			i++;
		}

		return sb.toString();
	}

	public static String getFilePathFromUri(Context context, Uri uri)
			throws Exception {
		if (uri == null) {
			throw new FileNotFoundException("Uri is null.");
		}
		if (context == null) {
			throw new NullPointerException("Context is null.");
		}
		if ("file".equals(uri.getScheme())) {
			return uri.getPath();
		}
		String[] proj = { "_data", "orientation" };

		String filePath = null;
		Cursor cursor = context.getContentResolver().query(uri, proj, null,
				null, null);
		try {
			if ((cursor != null) && (cursor.moveToFirst())) {
				int columnIndex = cursor.getColumnIndex(proj[0]);
				filePath = cursor.getString(columnIndex);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (filePath == null) {
			if (uri.getPath() == null) {
				throw new FileNotFoundException("File path is null.");
			}
			filePath = uri.getPath();
		}

		return filePath;
	}

}