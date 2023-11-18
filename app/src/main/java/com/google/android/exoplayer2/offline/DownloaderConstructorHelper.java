package com.google.android.exoplayer2.offline;

import android.support.annotation.Nullable;
import com.google.android.exoplayer2.upstream.DataSink;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DummyDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.PriorityDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.PriorityTaskManager;

public final class DownloaderConstructorHelper {
    private final Cache cache;
    private final Factory cacheReadDataSourceFactory;
    private final DataSink.Factory cacheWriteDataSinkFactory;
    private final PriorityTaskManager priorityTaskManager;
    private final Factory upstreamDataSourceFactory;

    public DownloaderConstructorHelper(Cache cache, Factory factory) {
        this(cache, factory, null, null, null);
    }

    public DownloaderConstructorHelper(Cache cache, Factory factory, @Nullable Factory factory2, @Nullable DataSink.Factory factory3, @Nullable PriorityTaskManager priorityTaskManager) {
        Assertions.checkNotNull(factory);
        this.cache = cache;
        this.upstreamDataSourceFactory = factory;
        this.cacheReadDataSourceFactory = factory2;
        this.cacheWriteDataSinkFactory = factory3;
        this.priorityTaskManager = priorityTaskManager;
    }

    public Cache getCache() {
        return this.cache;
    }

    public PriorityTaskManager getPriorityTaskManager() {
        return this.priorityTaskManager != null ? this.priorityTaskManager : new PriorityTaskManager();
    }

    public CacheDataSource buildCacheDataSource(boolean z) {
        DataSource createDataSource = this.cacheReadDataSourceFactory != null ? this.cacheReadDataSourceFactory.createDataSource() : new FileDataSource();
        if (z) {
            return new CacheDataSource(this.cache, DummyDataSource.INSTANCE, createDataSource, null, 1, null);
        }
        DataSink createDataSink = this.cacheWriteDataSinkFactory ? this.cacheWriteDataSinkFactory.createDataSink() : new CacheDataSink(this.cache, 2097152);
        z = this.upstreamDataSourceFactory.createDataSource();
        return new CacheDataSource(this.cache, this.priorityTaskManager == null ? z : new PriorityDataSource(z, this.priorityTaskManager, -1000), createDataSource, createDataSink, 1, null);
    }
}
