package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
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
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PrimaryPlaylistListener;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.List;

public final class HlsMediaSource implements MediaSource, PrimaryPlaylistListener {
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private final boolean allowChunklessPreparation;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private final EventDispatcher eventDispatcher;
    private final HlsExtractorFactory extractorFactory;
    private final Uri manifestUri;
    private final int minLoadableRetryCount;
    private final Parser<HlsPlaylist> playlistParser;
    private HlsPlaylistTracker playlistTracker;
    private Listener sourceListener;

    public static final class Factory implements MediaSourceFactory {
        private boolean allowChunklessPreparation;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
        private HlsExtractorFactory extractorFactory;
        private final HlsDataSourceFactory hlsDataSourceFactory;
        private boolean isCreateCalled;
        private int minLoadableRetryCount;
        @Nullable
        private Parser<HlsPlaylist> playlistParser;

        public Factory(com.google.android.exoplayer2.upstream.DataSource.Factory factory) {
            this(new DefaultHlsDataSourceFactory(factory));
        }

        public Factory(HlsDataSourceFactory hlsDataSourceFactory) {
            this.hlsDataSourceFactory = (HlsDataSourceFactory) Assertions.checkNotNull(hlsDataSourceFactory);
            this.extractorFactory = HlsExtractorFactory.DEFAULT;
            this.minLoadableRetryCount = 3;
            this.compositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
        }

        public Factory setExtractorFactory(HlsExtractorFactory hlsExtractorFactory) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.extractorFactory = (HlsExtractorFactory) Assertions.checkNotNull(hlsExtractorFactory);
            return this;
        }

        public Factory setMinLoadableRetryCount(int i) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.minLoadableRetryCount = i;
            return this;
        }

        public Factory setPlaylistParser(Parser<HlsPlaylist> parser) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.playlistParser = (Parser) Assertions.checkNotNull(parser);
            return this;
        }

        public Factory setCompositeSequenceableLoaderFactory(CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.compositeSequenceableLoaderFactory = (CompositeSequenceableLoaderFactory) Assertions.checkNotNull(compositeSequenceableLoaderFactory);
            return this;
        }

        public Factory setAllowChunklessPreparation(boolean z) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.allowChunklessPreparation = z;
            return this;
        }

        public HlsMediaSource createMediaSource(Uri uri) {
            return createMediaSource(uri, null, null);
        }

        public HlsMediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener mediaSourceEventListener) {
            this.isCreateCalled = true;
            if (this.playlistParser == null) {
                this.playlistParser = new HlsPlaylistParser();
            }
            return new HlsMediaSource(uri, this.hlsDataSourceFactory, this.extractorFactory, this.compositeSequenceableLoaderFactory, this.minLoadableRetryCount, handler, mediaSourceEventListener, this.playlistParser, this.allowChunklessPreparation);
        }

        public int[] getSupportedTypes() {
            return new int[]{2};
        }
    }

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.hls");
    }

    @Deprecated
    public HlsMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this(uri, factory, 3, handler, mediaSourceEventListener);
    }

    @Deprecated
    public HlsMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory factory, int i, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this(uri, new DefaultHlsDataSourceFactory(factory), HlsExtractorFactory.DEFAULT, i, handler, mediaSourceEventListener, new HlsPlaylistParser());
    }

    @Deprecated
    public HlsMediaSource(Uri uri, HlsDataSourceFactory hlsDataSourceFactory, HlsExtractorFactory hlsExtractorFactory, int i, Handler handler, MediaSourceEventListener mediaSourceEventListener, Parser<HlsPlaylist> parser) {
        this(uri, hlsDataSourceFactory, hlsExtractorFactory, new DefaultCompositeSequenceableLoaderFactory(), i, handler, mediaSourceEventListener, parser, false);
    }

    private HlsMediaSource(Uri uri, HlsDataSourceFactory hlsDataSourceFactory, HlsExtractorFactory hlsExtractorFactory, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, int i, Handler handler, MediaSourceEventListener mediaSourceEventListener, Parser<HlsPlaylist> parser, boolean z) {
        this.manifestUri = uri;
        this.dataSourceFactory = hlsDataSourceFactory;
        this.extractorFactory = hlsExtractorFactory;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.minLoadableRetryCount = i;
        this.playlistParser = parser;
        this.allowChunklessPreparation = z;
        this.eventDispatcher = new EventDispatcher(handler, mediaSourceEventListener);
    }

    public void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener) {
        this.sourceListener = listener;
        this.playlistTracker = new HlsPlaylistTracker(this.manifestUri, this.dataSourceFactory, this.eventDispatcher, this.minLoadableRetryCount, this, this.playlistParser);
        this.playlistTracker.start();
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.playlistTracker.maybeThrowPrimaryPlaylistRefreshError();
    }

    public MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator) {
        Assertions.checkArgument(mediaPeriodId.periodIndex == null ? true : null);
        return new HlsMediaPeriod(this.extractorFactory, this.playlistTracker, this.dataSourceFactory, this.minLoadableRetryCount, this.eventDispatcher, allocator, this.compositeSequenceableLoaderFactory, this.allowChunklessPreparation);
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        ((HlsMediaPeriod) mediaPeriod).release();
    }

    public void releaseSource() {
        if (this.playlistTracker != null) {
            this.playlistTracker.release();
            this.playlistTracker = null;
        }
        this.sourceListener = null;
    }

    public void onPrimaryPlaylistRefreshed(HlsMediaPlaylist hlsMediaPlaylist) {
        long j;
        long j2;
        List list;
        long j3;
        long j4;
        Timeline singlePeriodTimeline;
        HlsMediaSource hlsMediaSource = this;
        HlsMediaPlaylist hlsMediaPlaylist2 = hlsMediaPlaylist;
        long usToMs = hlsMediaPlaylist2.hasProgramDateTime ? C0649C.usToMs(hlsMediaPlaylist2.startTimeUs) : C0649C.TIME_UNSET;
        if (hlsMediaPlaylist2.playlistType != 2) {
            if (hlsMediaPlaylist2.playlistType != 1) {
                j = C0649C.TIME_UNSET;
                j2 = hlsMediaPlaylist2.startOffsetUs;
                if (hlsMediaSource.playlistTracker.isLive()) {
                    Timeline singlePeriodTimeline2 = new SinglePeriodTimeline(j, usToMs, hlsMediaPlaylist2.durationUs, hlsMediaPlaylist2.durationUs, 0, j2 != C0649C.TIME_UNSET ? 0 : j2, true, false);
                } else {
                    long initialStartTimeUs = hlsMediaPlaylist2.startTimeUs - hlsMediaSource.playlistTracker.getInitialStartTimeUs();
                    long j5 = hlsMediaPlaylist2.hasEndTag ? initialStartTimeUs + hlsMediaPlaylist2.durationUs : C0649C.TIME_UNSET;
                    list = hlsMediaPlaylist2.segments;
                    if (j2 != C0649C.TIME_UNSET) {
                        if (list.isEmpty()) {
                            j3 = ((Segment) list.get(Math.max(0, list.size() - 3))).relativeStartTimeUs;
                        } else {
                            j3 = 0;
                        }
                        j4 = j3;
                    } else {
                        j4 = j2;
                    }
                    singlePeriodTimeline = new SinglePeriodTimeline(j, usToMs, j5, hlsMediaPlaylist2.durationUs, initialStartTimeUs, j4, true, hlsMediaPlaylist2.hasEndTag ^ 1);
                }
                hlsMediaSource.sourceListener.onSourceInfoRefreshed(hlsMediaSource, singlePeriodTimeline, new HlsManifest(hlsMediaSource.playlistTracker.getMasterPlaylist(), hlsMediaPlaylist2));
            }
        }
        j = usToMs;
        j2 = hlsMediaPlaylist2.startOffsetUs;
        if (hlsMediaSource.playlistTracker.isLive()) {
            if (j2 != C0649C.TIME_UNSET) {
            }
            Timeline singlePeriodTimeline22 = new SinglePeriodTimeline(j, usToMs, hlsMediaPlaylist2.durationUs, hlsMediaPlaylist2.durationUs, 0, j2 != C0649C.TIME_UNSET ? 0 : j2, true, false);
        } else {
            long initialStartTimeUs2 = hlsMediaPlaylist2.startTimeUs - hlsMediaSource.playlistTracker.getInitialStartTimeUs();
            if (hlsMediaPlaylist2.hasEndTag) {
            }
            list = hlsMediaPlaylist2.segments;
            if (j2 != C0649C.TIME_UNSET) {
                j4 = j2;
            } else {
                if (list.isEmpty()) {
                    j3 = ((Segment) list.get(Math.max(0, list.size() - 3))).relativeStartTimeUs;
                } else {
                    j3 = 0;
                }
                j4 = j3;
            }
            singlePeriodTimeline = new SinglePeriodTimeline(j, usToMs, j5, hlsMediaPlaylist2.durationUs, initialStartTimeUs2, j4, true, hlsMediaPlaylist2.hasEndTag ^ 1);
        }
        hlsMediaSource.sourceListener.onSourceInfoRefreshed(hlsMediaSource, singlePeriodTimeline, new HlsManifest(hlsMediaSource.playlistTracker.getMasterPlaylist(), hlsMediaPlaylist2));
    }
}
