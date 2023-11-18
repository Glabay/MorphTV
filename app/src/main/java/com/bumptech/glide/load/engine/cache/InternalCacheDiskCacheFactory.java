package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import com.bumptech.glide.load.engine.cache.DiskCache.Factory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory.CacheDirectoryGetter;
import java.io.File;

public final class InternalCacheDiskCacheFactory extends DiskLruCacheFactory {

    /* renamed from: com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory$1 */
    class C05831 implements CacheDirectoryGetter {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$diskCacheName;

        C05831(Context context, String str) {
            this.val$context = context;
            this.val$diskCacheName = str;
        }

        public File getCacheDirectory() {
            File cacheDir = this.val$context.getCacheDir();
            if (cacheDir == null) {
                return null;
            }
            return this.val$diskCacheName != null ? new File(cacheDir, this.val$diskCacheName) : cacheDir;
        }
    }

    public InternalCacheDiskCacheFactory(Context context) {
        this(context, Factory.DEFAULT_DISK_CACHE_DIR, Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    public InternalCacheDiskCacheFactory(Context context, int i) {
        this(context, Factory.DEFAULT_DISK_CACHE_DIR, i);
    }

    public InternalCacheDiskCacheFactory(Context context, String str, int i) {
        super(new C05831(context, str), i);
    }
}
