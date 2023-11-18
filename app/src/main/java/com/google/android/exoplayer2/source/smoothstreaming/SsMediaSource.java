package com.google.android.exoplayer2.source.smoothstreaming;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSource.Listener;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.SinglePeriodTimeline;
import com.google.android.exoplayer2.source.ads.AdsMediaSource.MediaSourceFactory;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest.StreamElement;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsUtil;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.Loader.Callback;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower.Dummy;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayList;

public final class SsMediaSource implements MediaSource, Callback<ParsingLoadable<SsManifest>> {
    public static final long DEFAULT_LIVE_PRESENTATION_DELAY_MS = 30000;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private static final int MINIMUM_MANIFEST_REFRESH_PERIOD_MS = 5000;
    private static final long MIN_LIVE_DEFAULT_START_POSITION_US = 5000000;
    private final com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final EventDispatcher eventDispatcher;
    private final long livePresentationDelayMs;
    private SsManifest manifest;
    private DataSource manifestDataSource;
    private final com.google.android.exoplayer2.upstream.DataSource.Factory manifestDataSourceFactory;
    private long manifestLoadStartTimestamp;
    private Loader manifestLoader;
    private LoaderErrorThrower manifestLoaderErrorThrower;
    private final Parser<? extends SsManifest> manifestParser;
    private Handler manifestRefreshHandler;
    private final Uri manifestUri;
    private final ArrayList<SsMediaPeriod> mediaPeriods;
    private final int minLoadableRetryCount;
    private final boolean sideloadedManifest;
    private Listener sourceListener;

    /* renamed from: com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource$1 */
    class C07501 implements Runnable {
        C07501() {
        }

        public void run() {
            SsMediaSource.this.startLoadingManifest();
        }
    }

    public static final class Factory implements MediaSourceFactory {
        private final com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
        private boolean isCreateCalled;
        private long livePresentationDelayMs = 30000;
        @Nullable
        private final com.google.android.exoplayer2.upstream.DataSource.Factory manifestDataSourceFactory;
        @Nullable
        private Parser<? extends SsManifest> manifestParser;
        private int minLoadableRetryCount = 3;

        public Factory(com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory factory, @Nullable com.google.android.exoplayer2.upstream.DataSource.Factory factory2) {
            this.chunkSourceFactory = (com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory) Assertions.checkNotNull(factory);
            this.manifestDataSourceFactory = factory2;
        }

        public Factory setMinLoadableRetryCount(int i) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.minLoadableRetryCount = i;
            return this;
        }

        public Factory setLivePresentationDelayMs(long j) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.livePresentationDelayMs = j;
            return this;
        }

        public Factory setManifestParser(Parser<? extends SsManifest> parser) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.manifestParser = (Parser) Assertions.checkNotNull(parser);
            return this;
        }

        public Factory setCompositeSequenceableLoaderFactory(CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.compositeSequenceableLoaderFactory = (CompositeSequenceableLoaderFactory) Assertions.checkNotNull(compositeSequenceableLoaderFactory);
            return this;
        }

        public SsMediaSource createMediaSource(SsManifest ssManifest, @Nullable Handler handler, @Nullable MediaSourceEventListener mediaSourceEventListener) {
            SsManifest ssManifest2 = ssManifest;
            Assertions.checkArgument(ssManifest2.isLive ^ true);
            this.isCreateCalled = true;
            return new SsMediaSource(ssManifest2, null, null, null, this.chunkSourceFactory, this.compositeSequenceableLoaderFactory, this.minLoadableRetryCount, this.livePresentationDelayMs, handler, mediaSourceEventListener);
        }

        public SsMediaSource createMediaSource(Uri uri) {
            return createMediaSource(uri, null, null);
        }

        public SsMediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener mediaSourceEventListener) {
            this.isCreateCalled = true;
            if (this.manifestParser == null) {
                r0.manifestParser = new SsManifestParser();
            }
            return new SsMediaSource(null, (Uri) Assertions.checkNotNull(uri), r0.manifestDataSourceFactory, r0.manifestParser, r0.chunkSourceFactory, r0.compositeSequenceableLoaderFactory, r0.minLoadableRetryCount, r0.livePresentationDelayMs, handler, mediaSourceEventListener);
        }

        public int[] getSupportedTypes() {
            return new int[]{1};
        }
    }

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.smoothstreaming");
    }

    @Deprecated
    public SsMediaSource(SsManifest ssManifest, com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory factory, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this(ssManifest, factory, 3, handler, mediaSourceEventListener);
    }

    @Deprecated
    public SsMediaSource(SsManifest ssManifest, com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory factory, int i, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this(ssManifest, null, null, null, factory, new DefaultCompositeSequenceableLoaderFactory(), i, 30000, handler, mediaSourceEventListener);
    }

    @Deprecated
    public SsMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory factory2, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this(uri, factory, factory2, 3, 30000, handler, mediaSourceEventListener);
    }

    @Deprecated
    public SsMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory factory2, int i, long j, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this(uri, factory, new SsManifestParser(), factory2, i, j, handler, mediaSourceEventListener);
    }

    @Deprecated
    public SsMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, Parser<? extends SsManifest> parser, com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory factory2, int i, long j, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this(null, uri, factory, parser, factory2, new DefaultCompositeSequenceableLoaderFactory(), i, j, handler, mediaSourceEventListener);
    }

    private SsMediaSource(SsManifest ssManifest, Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, Parser<? extends SsManifest> parser, com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory factory2, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, int i, long j, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        boolean z;
        boolean z2 = true;
        if (ssManifest != null) {
            if (ssManifest.isLive) {
                z = false;
                Assertions.checkState(z);
                this.manifest = ssManifest;
                if (uri != null) {
                    uri = null;
                } else {
                    uri = SsUtil.fixManifestUri(uri);
                }
                this.manifestUri = uri;
                this.manifestDataSourceFactory = factory;
                this.manifestParser = parser;
                this.chunkSourceFactory = factory2;
                this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
                this.minLoadableRetryCount = i;
                this.livePresentationDelayMs = j;
                this.eventDispatcher = new EventDispatcher(handler, mediaSourceEventListener);
                if (ssManifest != null) {
                    z2 = false;
                }
                this.sideloadedManifest = z2;
                this.mediaPeriods = new ArrayList();
            }
        }
        z = true;
        Assertions.checkState(z);
        this.manifest = ssManifest;
        if (uri != null) {
            uri = SsUtil.fixManifestUri(uri);
        } else {
            uri = null;
        }
        this.manifestUri = uri;
        this.manifestDataSourceFactory = factory;
        this.manifestParser = parser;
        this.chunkSourceFactory = factory2;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.minLoadableRetryCount = i;
        this.livePresentationDelayMs = j;
        this.eventDispatcher = new EventDispatcher(handler, mediaSourceEventListener);
        if (ssManifest != null) {
            z2 = false;
        }
        this.sideloadedManifest = z2;
        this.mediaPeriods = new ArrayList();
    }

    public void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener) {
        this.sourceListener = listener;
        if (this.sideloadedManifest != null) {
            this.manifestLoaderErrorThrower = new Dummy();
            processManifest();
            return;
        }
        this.manifestDataSource = this.manifestDataSourceFactory.createDataSource();
        this.manifestLoader = new Loader("Loader:Manifest");
        this.manifestLoaderErrorThrower = this.manifestLoader;
        this.manifestRefreshHandler = new Handler();
        startLoadingManifest();
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.manifestLoaderErrorThrower.maybeThrowError();
    }

    public MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator) {
        Assertions.checkArgument(mediaPeriodId.periodIndex == null ? true : null);
        MediaPeriodId ssMediaPeriod = new SsMediaPeriod(this.manifest, this.chunkSourceFactory, this.compositeSequenceableLoaderFactory, this.minLoadableRetryCount, this.eventDispatcher, this.manifestLoaderErrorThrower, allocator);
        this.mediaPeriods.add(ssMediaPeriod);
        return ssMediaPeriod;
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        ((SsMediaPeriod) mediaPeriod).release();
        this.mediaPeriods.remove(mediaPeriod);
    }

    public void releaseSource() {
        this.sourceListener = null;
        this.manifest = this.sideloadedManifest ? this.manifest : null;
        this.manifestDataSource = null;
        this.manifestLoadStartTimestamp = 0;
        if (this.manifestLoader != null) {
            this.manifestLoader.release();
            this.manifestLoader = null;
        }
        if (this.manifestRefreshHandler != null) {
            this.manifestRefreshHandler.removeCallbacksAndMessages(null);
            this.manifestRefreshHandler = null;
        }
    }

    public void onLoadCompleted(ParsingLoadable<SsManifest> parsingLoadable, long j, long j2) {
        this.eventDispatcher.loadCompleted(parsingLoadable.dataSpec, parsingLoadable.type, j, j2, parsingLoadable.bytesLoaded());
        this.manifest = (SsManifest) parsingLoadable.getResult();
        this.manifestLoadStartTimestamp = j - j2;
        processManifest();
        scheduleManifestRefresh();
    }

    public void onLoadCanceled(ParsingLoadable<SsManifest> parsingLoadable, long j, long j2, boolean z) {
        this.eventDispatcher.loadCanceled(parsingLoadable.dataSpec, parsingLoadable.type, j, j2, parsingLoadable.bytesLoaded());
    }

    public int onLoadError(ParsingLoadable<SsManifest> parsingLoadable, long j, long j2, IOException iOException) {
        ParsingLoadable<SsManifest> parsingLoadable2 = parsingLoadable;
        IOException iOException2 = iOException;
        boolean z = iOException2 instanceof ParserException;
        this.eventDispatcher.loadError(parsingLoadable2.dataSpec, parsingLoadable2.type, j, j2, parsingLoadable2.bytesLoaded(), iOException2, z);
        return z ? 3 : 0;
    }

    private void processManifest() {
        SsMediaSource ssMediaSource = this;
        for (int i = 0; i < ssMediaSource.mediaPeriods.size(); i++) {
            ((SsMediaPeriod) ssMediaSource.mediaPeriods.get(i)).updateManifest(ssMediaSource.manifest);
        }
        long j = Long.MIN_VALUE;
        long j2 = Long.MAX_VALUE;
        for (StreamElement streamElement : ssMediaSource.manifest.streamElements) {
            if (streamElement.chunkCount > 0) {
                long min = Math.min(j2, streamElement.getStartTimeUs(0));
                j = Math.max(j, streamElement.getStartTimeUs(streamElement.chunkCount - 1) + streamElement.getChunkDurationUs(streamElement.chunkCount - 1));
                j2 = min;
            }
        }
        if (j2 == Long.MAX_VALUE) {
            Timeline singlePeriodTimeline = new SinglePeriodTimeline(ssMediaSource.manifest.isLive ? C0649C.TIME_UNSET : 0, 0, 0, 0, true, ssMediaSource.manifest.isLive);
        } else if (ssMediaSource.manifest.isLive) {
            if (ssMediaSource.manifest.dvrWindowLengthUs != C0649C.TIME_UNSET && ssMediaSource.manifest.dvrWindowLengthUs > 0) {
                j2 = Math.max(j2, j - ssMediaSource.manifest.dvrWindowLengthUs);
            }
            long j3 = j2;
            long j4 = j - j3;
            long msToUs = j4 - C0649C.msToUs(ssMediaSource.livePresentationDelayMs);
            Timeline singlePeriodTimeline2 = new SinglePeriodTimeline(C0649C.TIME_UNSET, j4, j3, msToUs < MIN_LIVE_DEFAULT_START_POSITION_US ? Math.min(MIN_LIVE_DEFAULT_START_POSITION_US, j4 / 2) : msToUs, true, true);
        } else {
            long j5 = ssMediaSource.manifest.durationUs != C0649C.TIME_UNSET ? ssMediaSource.manifest.durationUs : j - j2;
            Timeline singlePeriodTimeline3 = new SinglePeriodTimeline(j2 + j5, j5, j2, 0, true, false);
        }
        ssMediaSource.sourceListener.onSourceInfoRefreshed(ssMediaSource, r1, ssMediaSource.manifest);
    }

    private void scheduleManifestRefresh() {
        if (this.manifest.isLive) {
            this.manifestRefreshHandler.postDelayed(new C07501(), Math.max(0, (this.manifestLoadStartTimestamp + 5000) - SystemClock.elapsedRealtime()));
        }
    }

    private void startLoadingManifest() {
        ParsingLoadable parsingLoadable = new ParsingLoadable(this.manifestDataSource, this.manifestUri, 4, this.manifestParser);
        this.eventDispatcher.loadStarted(parsingLoadable.dataSpec, parsingLoadable.type, this.manifestLoader.startLoading(parsingLoadable, this, this.minLoadableRetryCount));
    }
}
