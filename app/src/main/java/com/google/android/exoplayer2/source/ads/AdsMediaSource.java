package com.google.android.exoplayer2.source.ads;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.source.CompositeMediaSource;
import com.google.android.exoplayer2.source.DeferredMediaPeriod;
import com.google.android.exoplayer2.source.DeferredMediaPeriod.PrepareErrorListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSource.Listener;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AdsMediaSource extends CompositeMediaSource<MediaPeriodId> {
    private static final String TAG = "AdsMediaSource";
    private long[][] adDurationsUs;
    private MediaSource[][] adGroupMediaSources;
    private final MediaSourceFactory adMediaSourceFactory;
    private AdPlaybackState adPlaybackState;
    private final ViewGroup adUiViewGroup;
    private final AdsLoader adsLoader;
    private ComponentListener componentListener;
    private Object contentManifest;
    private final MediaSource contentMediaSource;
    private Timeline contentTimeline;
    private final Map<MediaSource, List<DeferredMediaPeriod>> deferredMediaPeriodByAdMediaSource;
    @Nullable
    private final Handler eventHandler;
    @Nullable
    private final EventListener eventListener;
    private Listener listener;
    private final Handler mainHandler;
    private final Period period;

    public interface MediaSourceFactory {
        MediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener mediaSourceEventListener);

        int[] getSupportedTypes();
    }

    /* renamed from: com.google.android.exoplayer2.source.ads.AdsMediaSource$2 */
    class C07322 implements Runnable {
        C07322() {
        }

        public void run() {
            AdsMediaSource.this.adsLoader.detachPlayer();
        }
    }

    private final class AdPrepareErrorListener implements PrepareErrorListener {
        private final int adGroupIndex;
        private final int adIndexInAdGroup;

        public AdPrepareErrorListener(int i, int i2) {
            this.adGroupIndex = i;
            this.adIndexInAdGroup = i2;
        }

        public void onPrepareError(final IOException iOException) {
            AdsMediaSource.this.mainHandler.post(new Runnable() {
                public void run() {
                    AdsMediaSource.this.adsLoader.handlePrepareError(AdPrepareErrorListener.this.adGroupIndex, AdPrepareErrorListener.this.adIndexInAdGroup, iOException);
                }
            });
        }
    }

    private final class ComponentListener implements com.google.android.exoplayer2.source.ads.AdsLoader.EventListener {
        private final Handler playerHandler = new Handler();
        private volatile boolean released;

        /* renamed from: com.google.android.exoplayer2.source.ads.AdsMediaSource$ComponentListener$2 */
        class C07352 implements Runnable {
            C07352() {
            }

            public void run() {
                if (!ComponentListener.this.released) {
                    AdsMediaSource.this.eventListener.onAdClicked();
                }
            }
        }

        /* renamed from: com.google.android.exoplayer2.source.ads.AdsMediaSource$ComponentListener$3 */
        class C07363 implements Runnable {
            C07363() {
            }

            public void run() {
                if (!ComponentListener.this.released) {
                    AdsMediaSource.this.eventListener.onAdTapped();
                }
            }
        }

        public void release() {
            this.released = true;
            this.playerHandler.removeCallbacksAndMessages(null);
        }

        public void onAdPlaybackState(final AdPlaybackState adPlaybackState) {
            if (!this.released) {
                this.playerHandler.post(new Runnable() {
                    public void run() {
                        if (!ComponentListener.this.released) {
                            AdsMediaSource.this.onAdPlaybackState(adPlaybackState);
                        }
                    }
                });
            }
        }

        public void onAdClicked() {
            if (!(this.released || AdsMediaSource.this.eventHandler == null || AdsMediaSource.this.eventListener == null)) {
                AdsMediaSource.this.eventHandler.post(new C07352());
            }
        }

        public void onAdTapped() {
            if (!(this.released || AdsMediaSource.this.eventHandler == null || AdsMediaSource.this.eventListener == null)) {
                AdsMediaSource.this.eventHandler.post(new C07363());
            }
        }

        public void onAdLoadError(final IOException iOException) {
            if (!this.released) {
                Log.w(AdsMediaSource.TAG, "Ad load error", iOException);
                if (!(AdsMediaSource.this.eventHandler == null || AdsMediaSource.this.eventListener == null)) {
                    AdsMediaSource.this.eventHandler.post(new Runnable() {
                        public void run() {
                            if (!ComponentListener.this.released) {
                                AdsMediaSource.this.eventListener.onAdLoadError(iOException);
                            }
                        }
                    });
                }
            }
        }

        public void onInternalAdLoadError(final RuntimeException runtimeException) {
            if (!this.released) {
                Log.w(AdsMediaSource.TAG, "Internal ad load error", runtimeException);
                if (!(AdsMediaSource.this.eventHandler == null || AdsMediaSource.this.eventListener == null)) {
                    AdsMediaSource.this.eventHandler.post(new Runnable() {
                        public void run() {
                            if (!ComponentListener.this.released) {
                                AdsMediaSource.this.eventListener.onInternalAdLoadError(runtimeException);
                            }
                        }
                    });
                }
            }
        }
    }

    public interface EventListener extends MediaSourceEventListener {
        void onAdClicked();

        void onAdLoadError(IOException iOException);

        void onAdTapped();

        void onInternalAdLoadError(RuntimeException runtimeException);
    }

    public AdsMediaSource(MediaSource mediaSource, Factory factory, AdsLoader adsLoader, ViewGroup viewGroup) {
        this(mediaSource, factory, adsLoader, viewGroup, null, null);
    }

    public AdsMediaSource(MediaSource mediaSource, Factory factory, AdsLoader adsLoader, ViewGroup viewGroup, @Nullable Handler handler, @Nullable EventListener eventListener) {
        this(mediaSource, new ExtractorMediaSource.Factory(factory), adsLoader, viewGroup, handler, eventListener);
    }

    public AdsMediaSource(MediaSource mediaSource, MediaSourceFactory mediaSourceFactory, AdsLoader adsLoader, ViewGroup viewGroup, @Nullable Handler handler, @Nullable EventListener eventListener) {
        this.contentMediaSource = mediaSource;
        this.adMediaSourceFactory = mediaSourceFactory;
        this.adsLoader = adsLoader;
        this.adUiViewGroup = viewGroup;
        this.eventHandler = handler;
        this.eventListener = eventListener;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.deferredMediaPeriodByAdMediaSource = new HashMap();
        this.period = new Period();
        this.adGroupMediaSources = new MediaSource[0][];
        this.adDurationsUs = new long[0][];
        adsLoader.setSupportedContentTypes(mediaSourceFactory.getSupportedTypes());
    }

    public void prepareSource(final ExoPlayer exoPlayer, boolean z, Listener listener) {
        super.prepareSource(exoPlayer, z, listener);
        Assertions.checkArgument(z);
        z = new ComponentListener();
        this.listener = listener;
        this.componentListener = z;
        prepareChildSource(new MediaPeriodId(0), this.contentMediaSource);
        this.mainHandler.post(new Runnable() {
            public void run() {
                AdsMediaSource.this.adsLoader.attachPlayer(exoPlayer, z, AdsMediaSource.this.adUiViewGroup);
            }
        });
    }

    public MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator) {
        if (this.adPlaybackState.adGroupCount <= 0 || !mediaPeriodId.isAd()) {
            MediaPeriod deferredMediaPeriod = new DeferredMediaPeriod(this.contentMediaSource, mediaPeriodId, allocator);
            deferredMediaPeriod.createPeriod();
            return deferredMediaPeriod;
        }
        MediaSource createMediaSource;
        int i = mediaPeriodId.adGroupIndex;
        int i2 = mediaPeriodId.adIndexInAdGroup;
        if (this.adGroupMediaSources[i].length <= i2) {
            createMediaSource = this.adMediaSourceFactory.createMediaSource(this.adPlaybackState.adGroups[mediaPeriodId.adGroupIndex].uris[mediaPeriodId.adIndexInAdGroup], this.eventHandler, this.eventListener);
            int length = this.adGroupMediaSources[mediaPeriodId.adGroupIndex].length;
            if (i2 >= length) {
                int i3 = i2 + 1;
                this.adGroupMediaSources[i] = (MediaSource[]) Arrays.copyOf(this.adGroupMediaSources[i], i3);
                this.adDurationsUs[i] = Arrays.copyOf(this.adDurationsUs[i], i3);
                Arrays.fill(this.adDurationsUs[i], length, i3, C0649C.TIME_UNSET);
            }
            this.adGroupMediaSources[i][i2] = createMediaSource;
            this.deferredMediaPeriodByAdMediaSource.put(createMediaSource, new ArrayList());
            prepareChildSource(mediaPeriodId, createMediaSource);
        }
        createMediaSource = this.adGroupMediaSources[i][i2];
        MediaPeriod deferredMediaPeriod2 = new DeferredMediaPeriod(createMediaSource, new MediaPeriodId(0, mediaPeriodId.windowSequenceNumber), allocator);
        deferredMediaPeriod2.setPrepareErrorListener(new AdPrepareErrorListener(i, i2));
        List list = (List) this.deferredMediaPeriodByAdMediaSource.get(createMediaSource);
        if (list == null) {
            deferredMediaPeriod2.createPeriod();
        } else {
            list.add(deferredMediaPeriod2);
        }
        return deferredMediaPeriod2;
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        DeferredMediaPeriod deferredMediaPeriod = (DeferredMediaPeriod) mediaPeriod;
        List list = (List) this.deferredMediaPeriodByAdMediaSource.get(deferredMediaPeriod.mediaSource);
        if (list != null) {
            list.remove(deferredMediaPeriod);
        }
        deferredMediaPeriod.releasePeriod();
    }

    public void releaseSource() {
        super.releaseSource();
        this.componentListener.release();
        this.componentListener = null;
        this.deferredMediaPeriodByAdMediaSource.clear();
        this.contentTimeline = null;
        this.contentManifest = null;
        this.adPlaybackState = null;
        this.adGroupMediaSources = new MediaSource[0][];
        this.adDurationsUs = new long[0][];
        this.listener = null;
        this.mainHandler.post(new C07322());
    }

    protected void onChildSourceInfoRefreshed(MediaPeriodId mediaPeriodId, MediaSource mediaSource, Timeline timeline, @Nullable Object obj) {
        if (mediaPeriodId.isAd()) {
            onAdSourceInfoRefreshed(mediaSource, mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup, timeline);
        } else {
            onContentSourceInfoRefreshed(timeline, obj);
        }
    }

    private void onAdPlaybackState(AdPlaybackState adPlaybackState) {
        if (this.adPlaybackState == null) {
            this.adGroupMediaSources = new MediaSource[adPlaybackState.adGroupCount][];
            Arrays.fill(this.adGroupMediaSources, new MediaSource[0]);
            this.adDurationsUs = new long[adPlaybackState.adGroupCount][];
            Arrays.fill(this.adDurationsUs, new long[0]);
        }
        this.adPlaybackState = adPlaybackState;
        maybeUpdateSourceInfo();
    }

    private void onContentSourceInfoRefreshed(Timeline timeline, Object obj) {
        this.contentTimeline = timeline;
        this.contentManifest = obj;
        maybeUpdateSourceInfo();
    }

    private void onAdSourceInfoRefreshed(MediaSource mediaSource, int i, int i2, Timeline timeline) {
        int i3 = 0;
        boolean z = true;
        if (timeline.getPeriodCount() != 1) {
            z = false;
        }
        Assertions.checkArgument(z);
        this.adDurationsUs[i][i2] = timeline.getPeriod(0, this.period).getDurationUs();
        if (this.deferredMediaPeriodByAdMediaSource.containsKey(mediaSource) != 0) {
            List list = (List) this.deferredMediaPeriodByAdMediaSource.get(mediaSource);
            while (i3 < list.size()) {
                ((DeferredMediaPeriod) list.get(i3)).createPeriod();
                i3++;
            }
            this.deferredMediaPeriodByAdMediaSource.remove(mediaSource);
        }
        maybeUpdateSourceInfo();
    }

    private void maybeUpdateSourceInfo() {
        if (this.adPlaybackState != null && this.contentTimeline != null) {
            this.adPlaybackState = this.adPlaybackState.withAdDurationsUs(this.adDurationsUs);
            this.listener.onSourceInfoRefreshed(this, this.adPlaybackState.adGroupCount == 0 ? this.contentTimeline : new SinglePeriodAdTimeline(this.contentTimeline, this.adPlaybackState), this.contentManifest);
        }
    }
}
