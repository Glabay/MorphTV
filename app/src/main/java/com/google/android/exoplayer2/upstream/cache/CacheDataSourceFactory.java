package com.google.android.exoplayer2.upstream.cache;

import com.google.android.exoplayer2.upstream.DataSink;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource.EventListener;

public final class CacheDataSourceFactory implements Factory {
    private final Cache cache;
    private final Factory cacheReadDataSourceFactory;
    private final DataSink.Factory cacheWriteDataSinkFactory;
    private final EventListener eventListener;
    private final int flags;
    private final Factory upstreamFactory;

    public CacheDataSourceFactory(Cache cache, Factory factory) {
        this(cache, factory, 0);
    }

    public CacheDataSourceFactory(Cache cache, Factory factory, int i) {
        this(cache, factory, i, 2097152);
    }

    public CacheDataSourceFactory(Cache cache, Factory factory, int i, long j) {
        this(cache, factory, new FileDataSourceFactory(), new CacheDataSinkFactory(cache, j), i, null);
    }

    public CacheDataSourceFactory(Cache cache, Factory factory, Factory factory2, DataSink.Factory factory3, int i, EventListener eventListener) {
        this.cache = cache;
        this.upstreamFactory = factory;
        this.cacheReadDataSourceFactory = factory2;
        this.cacheWriteDataSinkFactory = factory3;
        this.flags = i;
        this.eventListener = eventListener;
    }

    public CacheDataSource createDataSource() {
        return new CacheDataSource(this.cache, this.upstreamFactory.createDataSource(), this.cacheReadDataSourceFactory.createDataSource(), this.cacheWriteDataSinkFactory != null ? this.cacheWriteDataSinkFactory.createDataSink() : null, this.flags, this.eventListener);
    }
}
