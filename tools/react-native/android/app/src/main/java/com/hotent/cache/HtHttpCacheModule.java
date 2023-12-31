package com.hotent.cache;

import android.content.Intent;

import com.facebook.cache.disk.DiskStorageCache;
import com.facebook.cache.disk.FileCache;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.network.OkHttpClientProvider;
import okhttp3.Cache;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.io.File;


/**
 * Created by tdzl2_000 on 2015-10-10.
 */
public class HtHttpCacheModule extends ReactContextBaseJavaModule {
    public HtHttpCacheModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "RCTHttpCache";
    }

    // @ReactMethod
    // public void clearCache(Promise promise){
    //     try {
    //         Cache cache = OkHttpClientProvider.getOkHttpClient().cache();
    //         if (cache != null) {
    //             cache.delete();
    //         }
    //         promise.resolve(null);
    //     } catch(IOException e){
    //         promise.reject(e);
    //     }
    // }

    @ReactMethod
    public void getHttpCacheSize(Promise promise){
        try {
            Cache cache = OkHttpClientProvider.getOkHttpClient().cache();
            promise.resolve(cache != null ? ((double)cache.size()) : 0);
        } catch(IOException e){
            promise.reject(e);
        }
    }

    static Method update;
    private void updateCacheSize(DiskStorageCache cache) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (update == null){
            update = DiskStorageCache.class.getDeclaredMethod("maybeUpdateFileCacheSize");
            update.setAccessible(true);
        }
        update.invoke(cache);
    }

    @ReactMethod
    public void getImageCacheSize(Promise promise){
        FileCache cache1 = ImagePipelineFactory.getInstance().getMainFileCache();
        long size1 = cache1.getSize();
        if (size1 < 0){
            try {
                updateCacheSize((DiskStorageCache)cache1);
            } catch (Exception e){
                promise.reject(e);
                return;
            }
            size1 = cache1.getSize();
        }
        FileCache cache2 = ImagePipelineFactory.getInstance().getSmallImageFileCache();
        long size2 = cache2.getSize();
        if (size2 < 0){
            try {
                updateCacheSize((DiskStorageCache)cache2);
            } catch (Exception e){
                promise.reject(e);
                return;
            }
            size2 = cache2.getSize();
        }
        promise.resolve(((double)(size1+size2)));
    }

    @ReactMethod
    public void getCacheSize(Promise promise){
        FileCache cache1 = ImagePipelineFactory.getInstance().getMainFileCache();
        long size1 = cache1.getSize();
        if (size1 < 0){
            try {
                updateCacheSize((DiskStorageCache)cache1);
            } catch (Exception e){
                promise.reject(e);
                return;
            }
            size1 = cache1.getSize();
        }
        FileCache cache2 = ImagePipelineFactory.getInstance().getSmallImageFileCache();
        long size2 = cache2.getSize();
        if (size2 < 0){
            try {
                updateCacheSize((DiskStorageCache)cache2);
            } catch (Exception e){
                promise.reject(e);
                return;
            }
            size2 = cache2.getSize();
        }

        File cacheDir = getReactApplicationContext().getCacheDir();// /data/data/package_name/cache
        long size3 = getDirSize(cacheDir);

        // File filesDir = getReactApplicationContext().getFilesDir();// /data/data/package_name/files
        // long size4 = getDirSize(filesDir);

        promise.resolve(((double)(size1+size2+size3)));
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    private long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    @ReactMethod
    public void clearImageCache(Promise promise){
        FileCache cache1 = ImagePipelineFactory.getInstance().getMainFileCache();
        cache1.clearAll();
        FileCache cache2 = ImagePipelineFactory.getInstance().getSmallImageFileCache();
        cache2.clearAll();


        promise.resolve(null);
    }

     /**
     * 清除缓存目录
     * 目录
     * 当前系统时间
     */
    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    @ReactMethod
    public void clearCache(Promise promise){
        FileCache cache1 = ImagePipelineFactory.getInstance().getMainFileCache();
        cache1.clearAll();
        FileCache cache2 = ImagePipelineFactory.getInstance().getSmallImageFileCache();
        cache2.clearAll();

        getReactApplicationContext().deleteDatabase("webview.db");
        getReactApplicationContext().deleteDatabase("webview.db-shm");
        getReactApplicationContext().deleteDatabase("webview.db-wal");
        getReactApplicationContext().deleteDatabase("webviewCache.db");
        getReactApplicationContext().deleteDatabase("webviewCache.db-shm");
        getReactApplicationContext().deleteDatabase("webviewCache.db-wal");
        //清除数据缓存
        //clearCacheFolder(getReactApplicationContext().getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(getReactApplicationContext().getCacheDir(), System.currentTimeMillis());
    
        promise.resolve(null);
    }
}