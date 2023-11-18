package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekMap.SeekPoints;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.SampleQueue.UpstreamFormatChangedListener;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.Loader.Callback;
import com.google.android.exoplayer2.upstream.Loader.Loadable;
import com.google.android.exoplayer2.upstream.Loader.ReleaseCallback;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ConditionVariable;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.Arrays;

final class ExtractorMediaPeriod implements MediaPeriod, ExtractorOutput, Callback<ExtractingLoadable>, ReleaseCallback, UpstreamFormatChangedListener {
    private static final long DEFAULT_LAST_SAMPLE_DURATION_US = 10000;
    private int actualMinLoadableRetryCount;
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final long continueLoadingCheckIntervalBytes;
    @Nullable
    private final String customCacheKey;
    private final DataSource dataSource;
    private long durationUs;
    private int enabledTrackCount;
    private final EventDispatcher eventDispatcher;
    private int extractedSamplesCountAtStartOfLoad;
    private final ExtractorHolder extractorHolder;
    private final Handler handler;
    private boolean haveAudioVideoTracks;
    private long lastSeekPositionUs;
    private long length;
    private final Listener listener;
    private final ConditionVariable loadCondition;
    private final Loader loader = new Loader("Loader:ExtractorMediaPeriod");
    private boolean loadingFinished;
    private final Runnable maybeFinishPrepareRunnable;
    private final int minLoadableRetryCount;
    private boolean notifyDiscontinuity;
    private final Runnable onContinueLoadingRequestedRunnable;
    private boolean pendingDeferredRetry;
    private long pendingResetPositionUs;
    private boolean prepared;
    private boolean released;
    private int[] sampleQueueTrackIds;
    private SampleQueue[] sampleQueues;
    private boolean sampleQueuesBuilt;
    private SeekMap seekMap;
    private boolean seenFirstTrackSelection;
    private boolean[] trackEnabledStates;
    private boolean[] trackFormatNotificationSent;
    private boolean[] trackIsAudioVideoFlags;
    private TrackGroupArray tracks;
    private final Uri uri;

    /* renamed from: com.google.android.exoplayer2.source.ExtractorMediaPeriod$1 */
    class C07201 implements Runnable {
        C07201() {
        }

        public void run() {
            ExtractorMediaPeriod.this.maybeFinishPrepare();
        }
    }

    /* renamed from: com.google.android.exoplayer2.source.ExtractorMediaPeriod$2 */
    class C07212 implements Runnable {
        C07212() {
        }

        public void run() {
            if (!ExtractorMediaPeriod.this.released) {
                ExtractorMediaPeriod.this.callback.onContinueLoadingRequested(ExtractorMediaPeriod.this);
            }
        }
    }

    final class ExtractingLoadable implements Loadable {
        private long bytesLoaded;
        private final DataSource dataSource;
        private DataSpec dataSpec;
        private final ExtractorHolder extractorHolder;
        private long length = -1;
        private volatile boolean loadCanceled;
        private final ConditionVariable loadCondition;
        private boolean pendingExtractorSeek = true;
        private final PositionHolder positionHolder = new PositionHolder();
        private long seekTimeUs;
        private final Uri uri;

        public ExtractingLoadable(Uri uri, DataSource dataSource, ExtractorHolder extractorHolder, ConditionVariable conditionVariable) {
            this.uri = (Uri) Assertions.checkNotNull(uri);
            this.dataSource = (DataSource) Assertions.checkNotNull(dataSource);
            this.extractorHolder = (ExtractorHolder) Assertions.checkNotNull(extractorHolder);
            this.loadCondition = conditionVariable;
        }

        public void setLoadPosition(long j, long j2) {
            this.positionHolder.position = j;
            this.seekTimeUs = j2;
            this.pendingExtractorSeek = 1;
        }

        public void cancelLoad() {
            this.loadCanceled = true;
        }

        public boolean isLoadCanceled() {
            return this.loadCanceled;
        }

        public void load() throws IOException, InterruptedException {
            Throwable th;
            int i = 0;
            while (i == 0 && !this.loadCanceled) {
                try {
                    long j = this.positionHolder.position;
                    this.dataSpec = new DataSpec(this.uri, j, -1, ExtractorMediaPeriod.this.customCacheKey);
                    this.length = this.dataSource.open(this.dataSpec);
                    if (this.length != -1) {
                        this.length += j;
                    }
                    ExtractorInput defaultExtractorInput = new DefaultExtractorInput(this.dataSource, j, this.length);
                    try {
                        Extractor selectExtractor = this.extractorHolder.selectExtractor(defaultExtractorInput, this.dataSource.getUri());
                        if (this.pendingExtractorSeek) {
                            selectExtractor.seek(j, this.seekTimeUs);
                            this.pendingExtractorSeek = false;
                        }
                        while (i == 0 && !this.loadCanceled) {
                            this.loadCondition.block();
                            int read = selectExtractor.read(defaultExtractorInput, this.positionHolder);
                            try {
                                if (defaultExtractorInput.getPosition() > j + ExtractorMediaPeriod.this.continueLoadingCheckIntervalBytes) {
                                    j = defaultExtractorInput.getPosition();
                                    this.loadCondition.close();
                                    ExtractorMediaPeriod.this.handler.post(ExtractorMediaPeriod.this.onContinueLoadingRequestedRunnable);
                                }
                                i = read;
                            } catch (Throwable th2) {
                                th = th2;
                                i = read;
                            }
                        }
                        if (i == 1) {
                            i = 0;
                        } else if (defaultExtractorInput != null) {
                            this.positionHolder.position = defaultExtractorInput.getPosition();
                            this.bytesLoaded = this.positionHolder.position - this.dataSpec.absoluteStreamPosition;
                        }
                        Util.closeQuietly(this.dataSource);
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    ExtractorInput extractorInput = null;
                }
            }
            return;
            if (i != 1) {
                if (extractorInput != null) {
                    this.positionHolder.position = extractorInput.getPosition();
                    this.bytesLoaded = this.positionHolder.position - this.dataSpec.absoluteStreamPosition;
                }
            }
            Util.closeQuietly(this.dataSource);
            throw th;
        }
    }

    private static final class ExtractorHolder {
        private Extractor extractor;
        private final ExtractorOutput extractorOutput;
        private final Extractor[] extractors;

        public ExtractorHolder(Extractor[] extractorArr, ExtractorOutput extractorOutput) {
            this.extractors = extractorArr;
            this.extractorOutput = extractorOutput;
        }

        public com.google.android.exoplayer2.extractor.Extractor selectExtractor(com.google.android.exoplayer2.extractor.ExtractorInput r6, android.net.Uri r7) throws java.io.IOException, java.lang.InterruptedException {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r5 = this;
            r0 = r5.extractor;
            if (r0 == 0) goto L_0x0007;
        L_0x0004:
            r6 = r5.extractor;
            return r6;
        L_0x0007:
            r0 = r5.extractors;
            r1 = r0.length;
            r2 = 0;
        L_0x000b:
            if (r2 >= r1) goto L_0x0026;
        L_0x000d:
            r3 = r0[r2];
            r4 = r3.sniff(r6);	 Catch:{ EOFException -> 0x0020, all -> 0x001b }
            if (r4 == 0) goto L_0x0020;	 Catch:{ EOFException -> 0x0020, all -> 0x001b }
        L_0x0015:
            r5.extractor = r3;	 Catch:{ EOFException -> 0x0020, all -> 0x001b }
            r6.resetPeekPosition();
            goto L_0x0026;
        L_0x001b:
            r7 = move-exception;
            r6.resetPeekPosition();
            throw r7;
        L_0x0020:
            r6.resetPeekPosition();
            r2 = r2 + 1;
            goto L_0x000b;
        L_0x0026:
            r6 = r5.extractor;
            if (r6 != 0) goto L_0x004c;
        L_0x002a:
            r6 = new com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r1 = "None of the available extractors (";
            r0.append(r1);
            r1 = r5.extractors;
            r1 = com.google.android.exoplayer2.util.Util.getCommaDelimitedSimpleClassNames(r1);
            r0.append(r1);
            r1 = ") could read the stream.";
            r0.append(r1);
            r0 = r0.toString();
            r6.<init>(r0, r7);
            throw r6;
        L_0x004c:
            r6 = r5.extractor;
            r7 = r5.extractorOutput;
            r6.init(r7);
            r6 = r5.extractor;
            return r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.ExtractorMediaPeriod.ExtractorHolder.selectExtractor(com.google.android.exoplayer2.extractor.ExtractorInput, android.net.Uri):com.google.android.exoplayer2.extractor.Extractor");
        }

        public void release() {
            if (this.extractor != null) {
                this.extractor.release();
                this.extractor = null;
            }
        }
    }

    interface Listener {
        void onSourceInfoRefreshed(long j, boolean z);
    }

    private final class SampleStreamImpl implements SampleStream {
        private final int track;

        public SampleStreamImpl(int i) {
            this.track = i;
        }

        public boolean isReady() {
            return ExtractorMediaPeriod.this.isReady(this.track);
        }

        public void maybeThrowError() throws IOException {
            ExtractorMediaPeriod.this.maybeThrowError();
        }

        public int readData(FormatHolder formatHolder, DecoderInputBuffer decoderInputBuffer, boolean z) {
            return ExtractorMediaPeriod.this.readData(this.track, formatHolder, decoderInputBuffer, z);
        }

        public int skipData(long j) {
            return ExtractorMediaPeriod.this.skipData(this.track, j);
        }
    }

    public void reevaluateBuffer(long j) {
    }

    public ExtractorMediaPeriod(Uri uri, DataSource dataSource, Extractor[] extractorArr, int i, EventDispatcher eventDispatcher, Listener listener, Allocator allocator, @Nullable String str, int i2) {
        this.uri = uri;
        this.dataSource = dataSource;
        this.minLoadableRetryCount = i;
        this.eventDispatcher = eventDispatcher;
        this.listener = listener;
        this.allocator = allocator;
        this.customCacheKey = str;
        this.continueLoadingCheckIntervalBytes = (long) i2;
        this.extractorHolder = new ExtractorHolder(extractorArr, this);
        this.loadCondition = new ConditionVariable();
        this.maybeFinishPrepareRunnable = new C07201();
        this.onContinueLoadingRequestedRunnable = new C07212();
        this.handler = new Handler();
        this.sampleQueueTrackIds = new int[0];
        this.sampleQueues = new SampleQueue[0];
        this.pendingResetPositionUs = C0649C.TIME_UNSET;
        this.length = -1;
        this.durationUs = C0649C.TIME_UNSET;
        if (i == -1) {
            i = 3;
        }
        this.actualMinLoadableRetryCount = i;
    }

    public void release() {
        if (this.prepared) {
            for (SampleQueue discardToEnd : this.sampleQueues) {
                discardToEnd.discardToEnd();
            }
        }
        this.loader.release(this);
        this.handler.removeCallbacksAndMessages(null);
        this.released = true;
    }

    public void onLoaderReleased() {
        for (SampleQueue reset : this.sampleQueues) {
            reset.reset();
        }
        this.extractorHolder.release();
    }

    public void prepare(MediaPeriod.Callback callback, long j) {
        this.callback = callback;
        this.loadCondition.open();
        startLoading();
    }

    public void maybeThrowPrepareError() throws IOException {
        maybeThrowError();
    }

    public TrackGroupArray getTrackGroups() {
        return this.tracks;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long selectTracks(com.google.android.exoplayer2.trackselection.TrackSelection[] r7, boolean[] r8, com.google.android.exoplayer2.source.SampleStream[] r9, boolean[] r10, long r11) {
        /*
        r6 = this;
        r0 = r6.prepared;
        com.google.android.exoplayer2.util.Assertions.checkState(r0);
        r0 = r6.enabledTrackCount;
        r1 = 0;
        r2 = 0;
    L_0x0009:
        r3 = r7.length;
        r4 = 1;
        if (r2 >= r3) goto L_0x0037;
    L_0x000d:
        r3 = r9[r2];
        if (r3 == 0) goto L_0x0034;
    L_0x0011:
        r3 = r7[r2];
        if (r3 == 0) goto L_0x0019;
    L_0x0015:
        r3 = r8[r2];
        if (r3 != 0) goto L_0x0034;
    L_0x0019:
        r3 = r9[r2];
        r3 = (com.google.android.exoplayer2.source.ExtractorMediaPeriod.SampleStreamImpl) r3;
        r3 = r3.track;
        r5 = r6.trackEnabledStates;
        r5 = r5[r3];
        com.google.android.exoplayer2.util.Assertions.checkState(r5);
        r5 = r6.enabledTrackCount;
        r5 = r5 - r4;
        r6.enabledTrackCount = r5;
        r4 = r6.trackEnabledStates;
        r4[r3] = r1;
        r3 = 0;
        r9[r2] = r3;
    L_0x0034:
        r2 = r2 + 1;
        goto L_0x0009;
    L_0x0037:
        r8 = r6.seenFirstTrackSelection;
        if (r8 == 0) goto L_0x0041;
    L_0x003b:
        if (r0 != 0) goto L_0x003f;
    L_0x003d:
        r8 = 1;
        goto L_0x0048;
    L_0x003f:
        r8 = 0;
        goto L_0x0048;
    L_0x0041:
        r2 = 0;
        r8 = (r11 > r2 ? 1 : (r11 == r2 ? 0 : -1));
        if (r8 == 0) goto L_0x003f;
    L_0x0047:
        goto L_0x003d;
    L_0x0048:
        r0 = r8;
        r8 = 0;
    L_0x004a:
        r2 = r7.length;
        if (r8 >= r2) goto L_0x00af;
    L_0x004d:
        r2 = r9[r8];
        if (r2 != 0) goto L_0x00ac;
    L_0x0051:
        r2 = r7[r8];
        if (r2 == 0) goto L_0x00ac;
    L_0x0055:
        r2 = r7[r8];
        r3 = r2.length();
        if (r3 != r4) goto L_0x005f;
    L_0x005d:
        r3 = 1;
        goto L_0x0060;
    L_0x005f:
        r3 = 0;
    L_0x0060:
        com.google.android.exoplayer2.util.Assertions.checkState(r3);
        r3 = r2.getIndexInTrackGroup(r1);
        if (r3 != 0) goto L_0x006b;
    L_0x0069:
        r3 = 1;
        goto L_0x006c;
    L_0x006b:
        r3 = 0;
    L_0x006c:
        com.google.android.exoplayer2.util.Assertions.checkState(r3);
        r3 = r6.tracks;
        r2 = r2.getTrackGroup();
        r2 = r3.indexOf(r2);
        r3 = r6.trackEnabledStates;
        r3 = r3[r2];
        r3 = r3 ^ r4;
        com.google.android.exoplayer2.util.Assertions.checkState(r3);
        r3 = r6.enabledTrackCount;
        r3 = r3 + r4;
        r6.enabledTrackCount = r3;
        r3 = r6.trackEnabledStates;
        r3[r2] = r4;
        r3 = new com.google.android.exoplayer2.source.ExtractorMediaPeriod$SampleStreamImpl;
        r3.<init>(r2);
        r9[r8] = r3;
        r10[r8] = r4;
        if (r0 != 0) goto L_0x00ac;
    L_0x0095:
        r0 = r6.sampleQueues;
        r0 = r0[r2];
        r0.rewind();
        r2 = r0.advanceTo(r11, r4, r4);
        r3 = -1;
        if (r2 != r3) goto L_0x00ab;
    L_0x00a3:
        r0 = r0.getReadIndex();
        if (r0 == 0) goto L_0x00ab;
    L_0x00a9:
        r0 = 1;
        goto L_0x00ac;
    L_0x00ab:
        r0 = 0;
    L_0x00ac:
        r8 = r8 + 1;
        goto L_0x004a;
    L_0x00af:
        r7 = r6.enabledTrackCount;
        if (r7 != 0) goto L_0x00df;
    L_0x00b3:
        r6.pendingDeferredRetry = r1;
        r6.notifyDiscontinuity = r1;
        r7 = r6.loader;
        r7 = r7.isLoading();
        if (r7 == 0) goto L_0x00d2;
    L_0x00bf:
        r7 = r6.sampleQueues;
        r8 = r7.length;
    L_0x00c2:
        if (r1 >= r8) goto L_0x00cc;
    L_0x00c4:
        r9 = r7[r1];
        r9.discardToEnd();
        r1 = r1 + 1;
        goto L_0x00c2;
    L_0x00cc:
        r7 = r6.loader;
        r7.cancelLoading();
        goto L_0x00f1;
    L_0x00d2:
        r7 = r6.sampleQueues;
        r8 = r7.length;
    L_0x00d5:
        if (r1 >= r8) goto L_0x00f1;
    L_0x00d7:
        r9 = r7[r1];
        r9.reset();
        r1 = r1 + 1;
        goto L_0x00d5;
    L_0x00df:
        if (r0 == 0) goto L_0x00f1;
    L_0x00e1:
        r11 = r6.seekToUs(r11);
    L_0x00e5:
        r7 = r9.length;
        if (r1 >= r7) goto L_0x00f1;
    L_0x00e8:
        r7 = r9[r1];
        if (r7 == 0) goto L_0x00ee;
    L_0x00ec:
        r10[r1] = r4;
    L_0x00ee:
        r1 = r1 + 1;
        goto L_0x00e5;
    L_0x00f1:
        r6.seenFirstTrackSelection = r4;
        return r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.ExtractorMediaPeriod.selectTracks(com.google.android.exoplayer2.trackselection.TrackSelection[], boolean[], com.google.android.exoplayer2.source.SampleStream[], boolean[], long):long");
    }

    public void discardBuffer(long j, boolean z) {
        int length = this.sampleQueues.length;
        for (int i = 0; i < length; i++) {
            this.sampleQueues[i].discardTo(j, z, this.trackEnabledStates[i]);
        }
    }

    public boolean continueLoading(long j) {
        if (this.loadingFinished == null && this.pendingDeferredRetry == null) {
            if (this.prepared == null || this.enabledTrackCount != null) {
                j = this.loadCondition.open();
                if (!this.loader.isLoading()) {
                    startLoading();
                    j = 1;
                }
                return j;
            }
        }
        return 0;
    }

    public long getNextLoadPositionUs() {
        return this.enabledTrackCount == 0 ? Long.MIN_VALUE : getBufferedPositionUs();
    }

    public long readDiscontinuity() {
        if (!this.notifyDiscontinuity || (!this.loadingFinished && getExtractedSamplesCount() <= this.extractedSamplesCountAtStartOfLoad)) {
            return C0649C.TIME_UNSET;
        }
        this.notifyDiscontinuity = false;
        return this.lastSeekPositionUs;
    }

    public long getBufferedPositionUs() {
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long j;
        if (this.haveAudioVideoTracks) {
            j = Long.MAX_VALUE;
            int length = this.sampleQueues.length;
            for (int i = 0; i < length; i++) {
                if (this.trackIsAudioVideoFlags[i]) {
                    j = Math.min(j, this.sampleQueues[i].getLargestQueuedTimestampUs());
                }
            }
        } else {
            j = getLargestQueuedTimestampUs();
        }
        if (j == Long.MIN_VALUE) {
            j = this.lastSeekPositionUs;
        }
        return j;
    }

    public long seekToUs(long j) {
        if (!this.seekMap.isSeekable()) {
            j = 0;
        }
        this.lastSeekPositionUs = j;
        int i = 0;
        this.notifyDiscontinuity = false;
        if (!isPendingReset() && seekInsideBufferUs(j)) {
            return j;
        }
        this.pendingDeferredRetry = false;
        this.pendingResetPositionUs = j;
        this.loadingFinished = false;
        if (this.loader.isLoading()) {
            this.loader.cancelLoading();
        } else {
            SampleQueue[] sampleQueueArr = this.sampleQueues;
            int length = sampleQueueArr.length;
            while (i < length) {
                sampleQueueArr[i].reset();
                i++;
            }
        }
        return j;
    }

    public long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters) {
        if (!this.seekMap.isSeekable()) {
            return 0;
        }
        SeekPoints seekPoints = this.seekMap.getSeekPoints(j);
        return Util.resolveSeekPositionUs(j, seekParameters, seekPoints.first.timeUs, seekPoints.second.timeUs);
    }

    boolean isReady(int i) {
        return !suppressRead() && (this.loadingFinished || this.sampleQueues[i].hasNextSample() != 0);
    }

    void maybeThrowError() throws IOException {
        this.loader.maybeThrowError(this.actualMinLoadableRetryCount);
    }

    int readData(int i, FormatHolder formatHolder, DecoderInputBuffer decoderInputBuffer, boolean z) {
        if (suppressRead()) {
            return -3;
        }
        formatHolder = this.sampleQueues[i].read(formatHolder, decoderInputBuffer, z, this.loadingFinished, this.lastSeekPositionUs);
        if (formatHolder == -4) {
            maybeNotifyTrackFormat(i);
        } else if (formatHolder == -3) {
            maybeStartDeferredRetry(i);
        }
        return formatHolder;
    }

    int skipData(int i, long j) {
        int i2 = 0;
        if (suppressRead()) {
            return 0;
        }
        SampleQueue sampleQueue = this.sampleQueues[i];
        if (!this.loadingFinished || j <= sampleQueue.getLargestQueuedTimestampUs()) {
            j = sampleQueue.advanceTo(j, true, true);
            if (j != -1) {
                i2 = j;
            }
        } else {
            i2 = sampleQueue.advanceToEnd();
        }
        if (i2 > 0) {
            maybeNotifyTrackFormat(i);
        } else {
            maybeStartDeferredRetry(i);
        }
        return i2;
    }

    private void maybeNotifyTrackFormat(int i) {
        if (!this.trackFormatNotificationSent[i]) {
            Format format = this.tracks.get(i).getFormat(0);
            this.eventDispatcher.downstreamFormatChanged(MimeTypes.getTrackType(format.sampleMimeType), format, 0, null, this.lastSeekPositionUs);
            this.trackFormatNotificationSent[i] = true;
        }
    }

    private void maybeStartDeferredRetry(int i) {
        if (this.pendingDeferredRetry && this.trackIsAudioVideoFlags[i]) {
            if (this.sampleQueues[i].hasNextSample() == 0) {
                this.pendingResetPositionUs = 0;
                i = 0;
                this.pendingDeferredRetry = false;
                this.notifyDiscontinuity = true;
                this.lastSeekPositionUs = 0;
                this.extractedSamplesCountAtStartOfLoad = 0;
                SampleQueue[] sampleQueueArr = this.sampleQueues;
                int length = sampleQueueArr.length;
                while (i < length) {
                    sampleQueueArr[i].reset();
                    i++;
                }
                this.callback.onContinueLoadingRequested(this);
            }
        }
    }

    private boolean suppressRead() {
        if (!this.notifyDiscontinuity) {
            if (!isPendingReset()) {
                return false;
            }
        }
        return true;
    }

    public void onLoadCompleted(ExtractingLoadable extractingLoadable, long j, long j2) {
        if (this.durationUs == C0649C.TIME_UNSET) {
            long largestQueuedTimestampUs = getLargestQueuedTimestampUs();
            r0.durationUs = largestQueuedTimestampUs == Long.MIN_VALUE ? 0 : largestQueuedTimestampUs + 10000;
            r0.listener.onSourceInfoRefreshed(r0.durationUs, r0.seekMap.isSeekable());
        }
        r0.eventDispatcher.loadCompleted(extractingLoadable.dataSpec, 1, -1, null, 0, null, extractingLoadable.seekTimeUs, r0.durationUs, j, j2, extractingLoadable.bytesLoaded);
        copyLengthFromLoader(extractingLoadable);
        r0.loadingFinished = true;
        r0.callback.onContinueLoadingRequested(r0);
    }

    public void onLoadCanceled(ExtractingLoadable extractingLoadable, long j, long j2, boolean z) {
        this.eventDispatcher.loadCanceled(extractingLoadable.dataSpec, 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, j, j2, extractingLoadable.bytesLoaded);
        if (!z) {
            copyLengthFromLoader(extractingLoadable);
            for (SampleQueue reset : r0.sampleQueues) {
                reset.reset();
            }
            if (r0.enabledTrackCount > 0) {
                r0.callback.onContinueLoadingRequested(r0);
            }
        }
    }

    public int onLoadError(ExtractingLoadable extractingLoadable, long j, long j2, IOException iOException) {
        boolean isLoadableExceptionFatal = isLoadableExceptionFatal(iOException);
        this.eventDispatcher.loadError(extractingLoadable.dataSpec, 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, j, j2, extractingLoadable.bytesLoaded, iOException, isLoadableExceptionFatal);
        copyLengthFromLoader(extractingLoadable);
        if (isLoadableExceptionFatal) {
            return 3;
        }
        ExtractingLoadable extractingLoadable2;
        Object obj;
        int extractedSamplesCount = getExtractedSamplesCount();
        int i = 0;
        if (extractedSamplesCount > r0.extractedSamplesCountAtStartOfLoad) {
            extractingLoadable2 = extractingLoadable;
            obj = 1;
        } else {
            extractingLoadable2 = extractingLoadable;
            obj = null;
        }
        if (!configureRetry(extractingLoadable2, extractedSamplesCount)) {
            i = 2;
        } else if (obj != null) {
            i = 1;
        }
        return i;
    }

    public TrackOutput track(int i, int i2) {
        i2 = this.sampleQueues.length;
        for (int i3 = 0; i3 < i2; i3++) {
            if (this.sampleQueueTrackIds[i3] == i) {
                return this.sampleQueues[i3];
            }
        }
        TrackOutput sampleQueue = new SampleQueue(this.allocator);
        sampleQueue.setUpstreamFormatChangeListener(this);
        int i4 = i2 + 1;
        this.sampleQueueTrackIds = Arrays.copyOf(this.sampleQueueTrackIds, i4);
        this.sampleQueueTrackIds[i2] = i;
        this.sampleQueues = (SampleQueue[]) Arrays.copyOf(this.sampleQueues, i4);
        this.sampleQueues[i2] = sampleQueue;
        return sampleQueue;
    }

    public void endTracks() {
        this.sampleQueuesBuilt = true;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    public void seekMap(SeekMap seekMap) {
        this.seekMap = seekMap;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    public void onUpstreamFormatChanged(Format format) {
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    private void maybeFinishPrepare() {
        if (!(this.released || this.prepared || this.seekMap == null)) {
            if (this.sampleQueuesBuilt) {
                SampleQueue[] sampleQueueArr = this.sampleQueues;
                int length = sampleQueueArr.length;
                int i = 0;
                while (i < length) {
                    if (sampleQueueArr[i].getUpstreamFormat() != null) {
                        i++;
                    } else {
                        return;
                    }
                }
                this.loadCondition.close();
                int length2 = this.sampleQueues.length;
                TrackGroup[] trackGroupArr = new TrackGroup[length2];
                this.trackIsAudioVideoFlags = new boolean[length2];
                this.trackEnabledStates = new boolean[length2];
                this.trackFormatNotificationSent = new boolean[length2];
                this.durationUs = this.seekMap.getDurationUs();
                i = 0;
                while (true) {
                    boolean z = true;
                    if (i >= length2) {
                        break;
                    }
                    trackGroupArr[i] = new TrackGroup(this.sampleQueues[i].getUpstreamFormat());
                    String str = r5.sampleMimeType;
                    if (!MimeTypes.isVideo(str)) {
                        if (!MimeTypes.isAudio(str)) {
                            z = false;
                        }
                    }
                    this.trackIsAudioVideoFlags[i] = z;
                    this.haveAudioVideoTracks = z | this.haveAudioVideoTracks;
                    i++;
                }
                this.tracks = new TrackGroupArray(trackGroupArr);
                if (this.minLoadableRetryCount == -1 && this.length == -1 && this.seekMap.getDurationUs() == C0649C.TIME_UNSET) {
                    this.actualMinLoadableRetryCount = 6;
                }
                this.prepared = true;
                this.listener.onSourceInfoRefreshed(this.durationUs, this.seekMap.isSeekable());
                this.callback.onPrepared(this);
            }
        }
    }

    private void copyLengthFromLoader(ExtractingLoadable extractingLoadable) {
        if (this.length == -1) {
            this.length = extractingLoadable.length;
        }
    }

    private void startLoading() {
        ExtractingLoadable extractingLoadable = new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this.loadCondition);
        if (this.prepared) {
            Assertions.checkState(isPendingReset());
            if (r6.durationUs == C0649C.TIME_UNSET || r6.pendingResetPositionUs < r6.durationUs) {
                extractingLoadable.setLoadPosition(r6.seekMap.getSeekPoints(r6.pendingResetPositionUs).first.position, r6.pendingResetPositionUs);
                r6.pendingResetPositionUs = C0649C.TIME_UNSET;
            } else {
                r6.loadingFinished = true;
                r6.pendingResetPositionUs = C0649C.TIME_UNSET;
                return;
            }
        }
        r6.extractedSamplesCountAtStartOfLoad = getExtractedSamplesCount();
        r6.eventDispatcher.loadStarted(extractingLoadable.dataSpec, 1, -1, null, 0, null, extractingLoadable.seekTimeUs, r6.durationUs, r6.loader.startLoading(extractingLoadable, r6, r6.actualMinLoadableRetryCount));
    }

    private boolean configureRetry(ExtractingLoadable extractingLoadable, int i) {
        if (this.length == -1) {
            if (this.seekMap == null || this.seekMap.getDurationUs() == C0649C.TIME_UNSET) {
                int i2 = 0;
                if (this.prepared == 0 || suppressRead() != 0) {
                    this.notifyDiscontinuity = this.prepared;
                    this.lastSeekPositionUs = 0;
                    this.extractedSamplesCountAtStartOfLoad = 0;
                    i = this.sampleQueues;
                    int length = i.length;
                    while (i2 < length) {
                        i[i2].reset();
                        i2++;
                    }
                    extractingLoadable.setLoadPosition(0, 0);
                    return true;
                }
                this.pendingDeferredRetry = true;
                return false;
            }
        }
        this.extractedSamplesCountAtStartOfLoad = i;
        return true;
    }

    private boolean seekInsideBufferUs(long j) {
        int length = this.sampleQueues.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                return true;
            }
            SampleQueue sampleQueue = this.sampleQueues[i];
            sampleQueue.rewind();
            if (sampleQueue.advanceTo(j, true, false) == -1) {
                z = false;
            }
            if (z || (!this.trackIsAudioVideoFlags[i] && this.haveAudioVideoTracks)) {
                i++;
            }
        }
        return false;
    }

    private int getExtractedSamplesCount() {
        int i = 0;
        for (SampleQueue writeIndex : this.sampleQueues) {
            i += writeIndex.getWriteIndex();
        }
        return i;
    }

    private long getLargestQueuedTimestampUs() {
        long j = Long.MIN_VALUE;
        for (SampleQueue largestQueuedTimestampUs : this.sampleQueues) {
            j = Math.max(j, largestQueuedTimestampUs.getLargestQueuedTimestampUs());
        }
        return j;
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != C0649C.TIME_UNSET;
    }

    private static boolean isLoadableExceptionFatal(IOException iOException) {
        return iOException instanceof UnrecognizedInputFormatException;
    }
}
