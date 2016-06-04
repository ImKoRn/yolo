package com.korn.im.yolo;

import android.app.Application;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.korn.im.yolo.loaders.DataLoader;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by korn on 04.06.16.
 */
public class App extends Application {
    private static final String TAG = "App";
    private static final String CACHE_FOLDER_NAME = "images";
    private static final long DISK_CACHE_MAX_SIZE = 50 * 1024 * 1024;
    private static final int MEMORY_CACHE_MAX_SIZE = 20 * 1024 * 1024;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        initDataLoader();
    }

    private void initDataLoader() {
        DataLoader.getInstance().init(this);
    }

    private void initImageLoader() {
        LruDiskCache lruDiskCache = null;
        boolean cacheOnDisk = false;
        try {
            lruDiskCache = new LruDiskCache(getImagesCacheDir(), new HashCodeFileNameGenerator(), DISK_CACHE_MAX_SIZE);
            Log.e(TAG, "Disk cache initialized");
            cacheOnDisk = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Can't create disc cache");
        }

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(cacheOnDisk)
                .build();

        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(this)
                .diskCache(lruDiskCache)
                .defaultDisplayImageOptions(displayImageOptions)
                .memoryCache(new LRULimitedMemoryCache(MEMORY_CACHE_MAX_SIZE))
                .threadPoolSize(Runtime.getRuntime().availableProcessors())
                .build()
        );
    }

    public File getImagesCacheDir() {
        File discDir = new File(getApplicationInfo().dataDir + File.separator + CACHE_FOLDER_NAME);

        if(!discDir.exists())
            discDir.mkdirs();
        return discDir;
    }
}
