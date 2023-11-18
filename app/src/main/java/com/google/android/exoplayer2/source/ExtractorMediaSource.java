package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource.Listener;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.ads.AdsMediaSource.MediaSourceFactory;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;

public final class ExtractorMediaSource implements MediaSource, Listener {
    public static final int DEFAULT_LOADING_CHECK_INTERVAL_BYTES = 1048576;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT_LIVE = 6;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT_ON_DEMAND = 3;
    public static final int MIN_RETRY_COUNT_DEFAULT_FOR_MEDIA = -1;
    private final int continueLoadingCheckIntervalBytes;
    private final String customCacheKey;
    private final com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory;
    private final EventDispatcher eventDispatcher;
    private final ExtractorsFactory extractorsFactory;
    private final int minLoadableRetryCount;
    private Listener sourceListener;
    private long timelineDurationUs;
    private boolean timelineIsSeekable;
    private final Uri uri;

    @Deprecated
    public interface EventListener {
        void onLoadError(IOException iOException);
    }

    private static final class EventListenerWrapper implements MediaSourceEventListener {
        private final EventListener eventListener;

        public void onDownstreamFormatChanged(int i, Format format, int i2, Object obj, long j) {
        }

        public void onLoadCanceled(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3, long j4, long j5) {
        }

        public void onLoadCompleted(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3, long j4, long j5) {
        }

        public void onLoadStarted(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3) {
        }

        public void onUpstreamDiscarded(int i, long j, long j2) {
        }

        public EventListenerWrapper(EventListener eventListener) {
            this.eventListener = (EventListener) Assertions.checkNotNull(eventListener);
        }

        public void onLoadError(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3, long j4, long j5, IOException iOException, boolean z) {
            this.eventListener.onLoadError(iOException);
        }
    }

    public static final class Factory implements MediaSourceFactory {
        private int continueLoadingCheckIntervalBytes = 1048576;
        @Nullable
        private String customCacheKey;
        private final com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory;
        @Nullable
        private ExtractorsFactory extractorsFactory;
        private boolean isCreateCalled;
        private int minLoadableRetryCount = -1;

        public Factory(com.google.android.exoplayer2.upstream.DataSource.Factory factory) {
            this.dataSourceFactory = factory;
        }

        public Factory setExtractorsFactory(ExtractorsFactory extractorsFactory) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.extractorsFactory = extractorsFactory;
            return this;
        }

        public Factory setCustomCacheKey(String str) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.customCacheKey = str;
            return this;
        }

        public Factory setMinLoadableRetryCount(int i) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.minLoadableRetryCount = i;
            return this;
        }

        public Factory setContinueLoadingCheckIntervalBytes(int i) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.continueLoadingCheckIntervalBytes = i;
            return this;
        }

        public ExtractorMediaSource createMediaSource(Uri uri) {
            return createMediaSource(uri, null, null);
        }

        public ExtractorMediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener mediaSourceEventListener) {
            this.isCreateCalled = true;
            if (this.extractorsFactory == null) {
                this.extractorsFactory = new DefaultExtractorsFactory();
            }
            return new ExtractorMediaSource(uri, this.dataSourceFactory, this.extractorsFactory, this.minLoadableRetryCount, handler, mediaSourceEventListener, this.customCacheKey, this.continueLoadingCheckIntervalBytes);
        }

        public int[] getSupportedTypes() {
            return new int[]{3};
        }
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
    }

    @Deprecated
    public ExtractorMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, ExtractorsFactory extractorsFactory, Handler handler, EventListener eventListener) {
        this(uri, factory, extractorsFactory, handler, eventListener, null);
    }

    @Deprecated
    public ExtractorMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, ExtractorsFactory extractorsFactory, Handler handler, EventListener eventListener, String str) {
        this(uri, factory, extractorsFactory, -1, handler, eventListener, str, 1048576);
    }

    @Deprecated
    public ExtractorMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, ExtractorsFactory extractorsFactory, int i, Handler handler, EventListener eventListener, String str, int i2) {
        EventListener eventListener2 = eventListener;
        this(uri, factory, extractorsFactory, i, handler, eventListener2 == null ? null : new EventListenerWrapper(eventListener2), str, i2);
    }

    private ExtractorMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, ExtractorsFactory extractorsFactory, int i, @Nullable Handler handler, @Nullable MediaSourceEventListener mediaSourceEventListener, @Nullable String str, int i2) {
        this.uri = uri;
        this.dataSourceFactory = factory;
        this.extractorsFactory = extractorsFactory;
        this.minLoadableRetryCount = i;
        this.eventDispatcher = new EventDispatcher(handler, mediaSourceEventListener);
        this.customCacheKey = str;
        this.continueLoadingCheckIntervalBytes = i2;
    }

    public void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener) {
        this.sourceListener = listener;
        notifySourceInfoRefreshed(1, null);
    }

    public MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator) {
        Assertions.checkArgument(mediaPeriodId.periodIndex == null ? true : null);
        return new ExtractorMediaPeriod(this.uri, this.dataSourceFactory.createDataSource(), this.extractorsFactory.createExtractors(), this.minLoadableRetryCount, this.eventDispatcher, this, allocator, this.customCacheKey, this.continueLoadingCheckIntervalBytes);
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        ((ExtractorMediaPeriod) mediaPeriod).release();
    }

    public void releaseSource() {
        this.sourceListener = null;
    }

    public void onSourceInfoRefreshed(long j, boolean z) {
        if (j == C0649C.TIME_UNSET) {
            j = this.timelineDurationUs;
        }
        if (this.timelineDurationUs != j || this.timelineIsSeekable != z) {
            notifySourceInfoRefreshed(j, z);
        }
    }

    private void notifySourceInfoRefreshed(long j, boolean z) {
        this.timelineDurationUs = j;
        this.timelineIsSeekable = z;
        this.sourceListener.onSourceInfoRefreshed(this, new SinglePeriodTimeline(this.timelineDurationUs, this.timelineIsSeekable, false), false);
    }
}
