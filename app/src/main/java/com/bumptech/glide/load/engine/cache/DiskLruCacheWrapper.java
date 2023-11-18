package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.disklrucache.DiskLruCache.Editor;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.cache.DiskCache.Writer;
import java.io.File;
import java.io.IOException;

public class DiskLruCacheWrapper implements DiskCache {
    private static final int APP_VERSION = 1;
    private static final String TAG = "DiskLruCacheWrapper";
    private static final int VALUE_COUNT = 1;
    private static DiskLruCacheWrapper wrapper;
    private final File directory;
    private DiskLruCache diskLruCache;
    private final int maxSize;
    private final SafeKeyGenerator safeKeyGenerator;
    private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();

    public static synchronized DiskCache get(File file, int i) {
        synchronized (DiskLruCacheWrapper.class) {
            if (wrapper == null) {
                wrapper = new DiskLruCacheWrapper(file, i);
            }
            file = wrapper;
        }
        return file;
    }

    protected DiskLruCacheWrapper(File file, int i) {
        this.directory = file;
        this.maxSize = i;
        this.safeKeyGenerator = new SafeKeyGenerator();
    }

    private synchronized DiskLruCache getDiskCache() throws IOException {
        if (this.diskLruCache == null) {
            this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, (long) this.maxSize);
        }
        return this.diskLruCache;
    }

    private synchronized void resetDiskCache() {
        this.diskLruCache = null;
    }

    public File get(Key key) {
        try {
            key = getDiskCache().get(this.safeKeyGenerator.getSafeKey(key));
            if (key != null) {
                return key.getFile(0);
            }
            return null;
        } catch (Key key2) {
            if (!Log.isLoggable(TAG, 5)) {
                return null;
            }
            Log.w(TAG, "Unable to get from disk cache", key2);
            return null;
        }
    }

    public void put(Key key, Writer writer) {
        String safeKey = this.safeKeyGenerator.getSafeKey(key);
        this.writeLocker.acquire(key);
        Editor edit;
        try {
            edit = getDiskCache().edit(safeKey);
            if (edit != null) {
                if (writer.write(edit.getFile(0)) != null) {
                    edit.commit();
                }
                edit.abortUnlessCommitted();
            }
        } catch (Writer writer2) {
            try {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Unable to put to disk cache", writer2);
                }
            } catch (Throwable th) {
                this.writeLocker.release(key);
            }
        } catch (Throwable th2) {
            edit.abortUnlessCommitted();
        }
        this.writeLocker.release(key);
    }

    public void delete(Key key) {
        try {
            getDiskCache().remove(this.safeKeyGenerator.getSafeKey(key));
        } catch (Key key2) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Unable to delete from disk cache", key2);
            }
        }
    }

    public synchronized void clear() {
        try {
            getDiskCache().delete();
            resetDiskCache();
        } catch (Throwable e) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Unable to clear disk cache", e);
            }
        }
    }
}
