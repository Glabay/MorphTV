package com.google.android.exoplayer2.offline;

import android.net.Uri;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.offline.Downloader.ProgressListener;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.upstream.cache.CacheUtil.CachingCounters;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import java.io.IOException;

public final class ProgressiveDownloader implements Downloader {
    private static final int BUFFER_SIZE_BYTES = 131072;
    private final Cache cache;
    private final CachingCounters cachingCounters = new CachingCounters();
    private final CacheDataSource dataSource;
    private final DataSpec dataSpec;
    private final PriorityTaskManager priorityTaskManager;

    public ProgressiveDownloader(String str, String str2, DownloaderConstructorHelper downloaderConstructorHelper) {
        this.dataSpec = new DataSpec(Uri.parse(str), 0, -1, str2, 0);
        this.cache = downloaderConstructorHelper.getCache();
        this.dataSource = downloaderConstructorHelper.buildCacheDataSource(null);
        this.priorityTaskManager = downloaderConstructorHelper.getPriorityTaskManager();
    }

    public void init() {
        CacheUtil.getCached(this.dataSpec, this.cache, this.cachingCounters);
    }

    public void download(@Nullable ProgressListener progressListener) throws InterruptedException, IOException {
        this.priorityTaskManager.add(-1000);
        try {
            CacheUtil.cache(this.dataSpec, this.cache, this.dataSource, new byte[131072], this.priorityTaskManager, -1000, this.cachingCounters, true);
            if (progressListener != null) {
                progressListener.onDownloadProgress(this, 100.0f, this.cachingCounters.contentLength);
            }
            this.priorityTaskManager.remove(-1000);
        } catch (Throwable th) {
            this.priorityTaskManager.remove(-1000);
        }
    }

    public void remove() {
        CacheUtil.remove(this.cache, CacheUtil.getKey(this.dataSpec));
    }

    public long getDownloadedBytes() {
        return this.cachingCounters.totalCachedBytes();
    }

    public float getDownloadPercentage() {
        long j = this.cachingCounters.contentLength;
        if (j == -1) {
            return Float.NaN;
        }
        return (((float) this.cachingCounters.totalCachedBytes()) * 100.0f) / ((float) j);
    }
}
