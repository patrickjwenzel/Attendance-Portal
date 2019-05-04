package com.example.loginscreen.net_utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * LruBitmapCache class
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {

    /**
     * Gets the default Lru cache size
     * @return Default Lru cache size
     */
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()
                / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }

    /**
     * Constructs a new LruBitmapCache
     */
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    /**
     * Constructs a new LruBitmapCache
     * @param sizeInKiloBytes Size you want the LruBitmapCache to be
     */
    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    /**
     * Gets the size of the LruBitmapCache
     * @param key Key
     * @param value The LruBitmapCache you want the size of
     * @return The size of the LruBitmapCache
     */
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    /**
     * Gets the Bitmap
     * @param url The url the Bitmap is being used
     * @return The desired Bitmap from the url
     */
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    /**
     * Puts a Bitmap at a given url
     * @param url The given url
     * @param bitmap The Bitmap you want to put at the url
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}