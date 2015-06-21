package com.zzisoo.toylibrary;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.zzisoo.toylibrary.imagecache.MemCacheHelper;

import java.io.File;

/**
 * Created by yangjisoo on 15. 6. 21..
 */
public class App extends Application {
    private static ImageLoader m_ImageLoader;

    public static void setImageLoader(Context context) {

        DisplayImageOptions optionsForProfile = new DisplayImageOptions.Builder().displayer(new SimpleBitmapDisplayer())
                // .cacheInMemory(true)
                .cacheOnDisc(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).discCacheExtraOptions(960, 720, Bitmap.CompressFormat.PNG, 75, null).memoryCache(new WeakMemoryCache()).memoryCacheSizePercentage(20).defaultDisplayImageOptions(optionsForProfile) // default
                .discCache(new UnlimitedDiscCache(new File(MemCacheHelper.getDiskCacheDir(context)), new Md5FileNameGenerator()))
                .build();
        m_ImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        m_ImageLoader.init(config);
    }

    public static ImageLoader getImageLoader(Context context) {
        if (m_ImageLoader == null) {
            setImageLoader(context);
        }

        return m_ImageLoader;
    }


}
