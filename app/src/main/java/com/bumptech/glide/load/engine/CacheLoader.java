package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import java.io.File;

class CacheLoader {
    private static final String TAG = "CacheLoader";
    private final DiskCache diskCache;

    public CacheLoader(DiskCache diskCache) {
        this.diskCache = diskCache;
    }

    public <Z> Resource<Z> load(Key key, ResourceDecoder<File, Z> resourceDecoder, int i, int i2) {
        File file = this.diskCache.get(key);
        if (file == null) {
            return null;
        }
        try {
            resourceDecoder = resourceDecoder.decode(file, i, i2);
        } catch (ResourceDecoder<File, Z> resourceDecoder2) {
            if (Log.isLoggable(TAG, 3) != 0) {
                Log.d(TAG, "Exception decoding image from cache", resourceDecoder2);
            }
            resourceDecoder2 = null;
        }
        if (resourceDecoder2 == null) {
            if (Log.isLoggable(TAG, 3) != 0) {
                Log.d(TAG, "Failed to decode image from cache or not present in cache");
            }
            this.diskCache.delete(key);
        }
        return resourceDecoder2;
    }
}
