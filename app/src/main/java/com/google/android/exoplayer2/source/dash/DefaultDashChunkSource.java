package com.google.android.exoplayer2.source.dash;

import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.rawcc.RawCcExtractor;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.chunk.ChunkHolder;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.exoplayer2.source.chunk.ContainerMediaChunk;
import com.google.android.exoplayer2.source.chunk.InitializationChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.SingleSampleMediaChunk;
import com.google.android.exoplayer2.source.dash.PlayerEmsgHandler.PlayerTrackEmsgHandler;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultDashChunkSource implements DashChunkSource {
    private final int[] adaptationSetIndices;
    private final DataSource dataSource;
    private final long elapsedRealtimeOffsetMs;
    private IOException fatalError;
    private long liveEdgeTimeUs = C0649C.TIME_UNSET;
    private DashManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int maxSegmentsPerLoad;
    private boolean missingLastSegment;
    private int periodIndex;
    @Nullable
    private final PlayerTrackEmsgHandler playerTrackEmsgHandler;
    protected final RepresentationHolder[] representationHolders;
    private final TrackSelection trackSelection;
    private final int trackType;

    public static final class Factory implements com.google.android.exoplayer2.source.dash.DashChunkSource.Factory {
        private final com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory;
        private final int maxSegmentsPerLoad;

        public Factory(com.google.android.exoplayer2.upstream.DataSource.Factory factory) {
            this(factory, 1);
        }

        public Factory(com.google.android.exoplayer2.upstream.DataSource.Factory factory, int i) {
            this.dataSourceFactory = factory;
            this.maxSegmentsPerLoad = i;
        }

        public DashChunkSource createDashChunkSource(LoaderErrorThrower loaderErrorThrower, DashManifest dashManifest, int i, int[] iArr, TrackSelection trackSelection, int i2, long j, boolean z, boolean z2, @Nullable PlayerTrackEmsgHandler playerTrackEmsgHandler) {
            return new DefaultDashChunkSource(loaderErrorThrower, dashManifest, i, iArr, trackSelection, i2, this.dataSourceFactory.createDataSource(), j, this.maxSegmentsPerLoad, z, z2, playerTrackEmsgHandler);
        }
    }

    protected static final class RepresentationHolder {
        final ChunkExtractorWrapper extractorWrapper;
        private long periodDurationUs;
        public Representation representation;
        public DashSegmentIndex segmentIndex;
        private long segmentNumShift;

        RepresentationHolder(long j, int i, Representation representation, boolean z, boolean z2, TrackOutput trackOutput) {
            this.periodDurationUs = j;
            this.representation = representation;
            j = representation.format.containerMimeType;
            if (mimeTypeIsRawText(j)) {
                this.extractorWrapper = null;
            } else {
                if (MimeTypes.APPLICATION_RAWCC.equals(j)) {
                    j = new RawCcExtractor(representation.format);
                } else if (mimeTypeIsWebm(j) != null) {
                    j = new MatroskaExtractor(1);
                } else {
                    int i2 = z ? 4 : 0;
                    if (z2) {
                        j = Collections.singletonList(Format.createTextSampleFormat(null, MimeTypes.APPLICATION_CEA608, 0, null));
                    } else {
                        j = Collections.emptyList();
                    }
                    FragmentedMp4Extractor fragmentedMp4Extractor = new FragmentedMp4Extractor(i2, null, null, null, j, trackOutput);
                }
                this.extractorWrapper = new ChunkExtractorWrapper(j, i, representation.format);
            }
            this.segmentIndex = representation.getIndex();
        }

        void updateRepresentation(long j, Representation representation) throws BehindLiveWindowException {
            DashSegmentIndex index = this.representation.getIndex();
            DashSegmentIndex index2 = representation.getIndex();
            this.periodDurationUs = j;
            this.representation = representation;
            if (index != null) {
                this.segmentIndex = index2;
                if (index.isExplicit() != null) {
                    j = index.getSegmentCount(this.periodDurationUs);
                    if (j != null) {
                        long firstSegmentNum = (index.getFirstSegmentNum() + ((long) j)) - 1;
                        long timeUs = index.getTimeUs(firstSegmentNum) + index.getDurationUs(firstSegmentNum, this.periodDurationUs);
                        long firstSegmentNum2 = index2.getFirstSegmentNum();
                        long timeUs2 = index2.getTimeUs(firstSegmentNum2);
                        if (timeUs == timeUs2) {
                            this.segmentNumShift += (firstSegmentNum + 1) - firstSegmentNum2;
                        } else if (timeUs < timeUs2) {
                            throw new BehindLiveWindowException();
                        } else {
                            this.segmentNumShift += index.getSegmentNum(timeUs2, this.periodDurationUs) - firstSegmentNum2;
                        }
                    }
                }
            }
        }

        public long getFirstSegmentNum() {
            return this.segmentIndex.getFirstSegmentNum() + this.segmentNumShift;
        }

        public int getSegmentCount() {
            return this.segmentIndex.getSegmentCount(this.periodDurationUs);
        }

        public long getSegmentStartTimeUs(long j) {
            return this.segmentIndex.getTimeUs(j - this.segmentNumShift);
        }

        public long getSegmentEndTimeUs(long j) {
            return getSegmentStartTimeUs(j) + this.segmentIndex.getDurationUs(j - this.segmentNumShift, this.periodDurationUs);
        }

        public long getSegmentNum(long j) {
            return this.segmentIndex.getSegmentNum(j, this.periodDurationUs) + this.segmentNumShift;
        }

        public RangedUri getSegmentUrl(long j) {
            return this.segmentIndex.getSegmentUrl(j - this.segmentNumShift);
        }

        private static boolean mimeTypeIsWebm(String str) {
            if (!(str.startsWith(MimeTypes.VIDEO_WEBM) || str.startsWith(MimeTypes.AUDIO_WEBM))) {
                if (str.startsWith(MimeTypes.APPLICATION_WEBM) == null) {
                    return null;
                }
            }
            return true;
        }

        private static boolean mimeTypeIsRawText(String str) {
            if (!MimeTypes.isText(str)) {
                if (MimeTypes.APPLICATION_TTML.equals(str) == null) {
                    return null;
                }
            }
            return true;
        }
    }

    public DefaultDashChunkSource(LoaderErrorThrower loaderErrorThrower, DashManifest dashManifest, int i, int[] iArr, TrackSelection trackSelection, int i2, DataSource dataSource, long j, int i3, boolean z, boolean z2, @Nullable PlayerTrackEmsgHandler playerTrackEmsgHandler) {
        TrackSelection trackSelection2 = trackSelection;
        this.manifestLoaderErrorThrower = loaderErrorThrower;
        this.manifest = dashManifest;
        this.adaptationSetIndices = iArr;
        this.trackSelection = trackSelection2;
        int i4 = i2;
        this.trackType = i4;
        this.dataSource = dataSource;
        this.periodIndex = i;
        this.elapsedRealtimeOffsetMs = j;
        this.maxSegmentsPerLoad = i3;
        PlayerTrackEmsgHandler playerTrackEmsgHandler2 = playerTrackEmsgHandler;
        this.playerTrackEmsgHandler = playerTrackEmsgHandler2;
        long periodDurationUs = dashManifest.getPeriodDurationUs(i);
        List representations = getRepresentations();
        this.representationHolders = new RepresentationHolder[trackSelection.length()];
        for (int i5 = 0; i5 < r0.representationHolders.length; i5++) {
            r0.representationHolders[i5] = new RepresentationHolder(periodDurationUs, i4, (Representation) representations.get(trackSelection2.getIndexInTrackGroup(i5)), z, z2, playerTrackEmsgHandler2);
        }
    }

    public long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters) {
        for (RepresentationHolder representationHolder : this.representationHolders) {
            if (representationHolder.segmentIndex != null) {
                long segmentNum = representationHolder.getSegmentNum(j);
                long segmentStartTimeUs = representationHolder.getSegmentStartTimeUs(segmentNum);
                long segmentStartTimeUs2 = (segmentStartTimeUs >= j || segmentNum >= ((long) (representationHolder.getSegmentCount() - 1))) ? segmentStartTimeUs : representationHolder.getSegmentStartTimeUs(segmentNum + 1);
                return Util.resolveSeekPositionUs(j, seekParameters, segmentStartTimeUs, segmentStartTimeUs2);
            }
        }
        return j;
    }

    public void updateManifest(DashManifest dashManifest, int i) {
        try {
            this.manifest = dashManifest;
            this.periodIndex = i;
            dashManifest = this.manifest.getPeriodDurationUs(this.periodIndex);
            List representations = getRepresentations();
            for (int i2 = 0; i2 < this.representationHolders.length; i2++) {
                this.representationHolders[i2].updateRepresentation(dashManifest, (Representation) representations.get(this.trackSelection.getIndexInTrackGroup(i2)));
            }
        } catch (DashManifest dashManifest2) {
            this.fatalError = dashManifest2;
        }
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        }
        this.manifestLoaderErrorThrower.maybeThrowError();
    }

    public int getPreferredQueueSize(long j, List<? extends MediaChunk> list) {
        if (this.fatalError == null) {
            if (this.trackSelection.length() >= 2) {
                return this.trackSelection.evaluateQueueSize(j, list);
            }
        }
        return list.size();
    }

    public void getNextChunk(MediaChunk mediaChunk, long j, long j2, ChunkHolder chunkHolder) {
        long j3 = j;
        long j4 = j2;
        ChunkHolder chunkHolder2 = chunkHolder;
        if (this.fatalError == null) {
            long j5 = j4 - j3;
            long resolveTimeToLiveEdgeUs = resolveTimeToLiveEdgeUs(j3);
            long msToUs = (C0649C.msToUs(r0.manifest.availabilityStartTimeMs) + C0649C.msToUs(r0.manifest.getPeriod(r0.periodIndex).startMs)) + j4;
            if (r0.playerTrackEmsgHandler == null || !r0.playerTrackEmsgHandler.maybeRefreshManifestBeforeLoadingNextChunk(msToUs)) {
                r0.trackSelection.updateSelectedTrack(j3, j5, resolveTimeToLiveEdgeUs);
                RepresentationHolder representationHolder = r0.representationHolders[r0.trackSelection.getSelectedIndex()];
                if (representationHolder.extractorWrapper != null) {
                    Representation representation = representationHolder.representation;
                    RangedUri initializationUri = representationHolder.extractorWrapper.getSampleFormats() == null ? representation.getInitializationUri() : null;
                    RangedUri indexUri = representationHolder.segmentIndex == null ? representation.getIndexUri() : null;
                    if (!(initializationUri == null && indexUri == null)) {
                        chunkHolder2.chunk = newInitializationChunk(representationHolder, r0.dataSource, r0.trackSelection.getSelectedFormat(), r0.trackSelection.getSelectionReason(), r0.trackSelection.getSelectionData(), initializationUri, indexUri);
                        return;
                    }
                }
                int segmentCount = representationHolder.getSegmentCount();
                if (segmentCount == 0) {
                    boolean z;
                    if (r0.manifest.dynamic) {
                        if (r0.periodIndex >= r0.manifest.getPeriodCount() - 1) {
                            z = false;
                            chunkHolder2.endOfStream = z;
                            return;
                        }
                    }
                    z = true;
                    chunkHolder2.endOfStream = z;
                    return;
                }
                long j6;
                long segmentNum;
                long constrainValue;
                boolean z2;
                j5 = representationHolder.getFirstSegmentNum();
                if (segmentCount == -1) {
                    long nowUnixTimeUs = (getNowUnixTimeUs() - C0649C.msToUs(r0.manifest.availabilityStartTimeMs)) - C0649C.msToUs(r0.manifest.getPeriod(r0.periodIndex).startMs);
                    if (r0.manifest.timeShiftBufferDepthMs != C0649C.TIME_UNSET) {
                        j5 = Math.max(j5, representationHolder.getSegmentNum(nowUnixTimeUs - C0649C.msToUs(r0.manifest.timeShiftBufferDepthMs)));
                    }
                    j6 = j5;
                    segmentNum = representationHolder.getSegmentNum(nowUnixTimeUs) - 1;
                } else {
                    segmentNum = (j5 + ((long) segmentCount)) - 1;
                    j6 = j5;
                }
                updateLiveEdgeTimeUs(representationHolder, segmentNum);
                if (mediaChunk == null) {
                    constrainValue = Util.constrainValue(representationHolder.getSegmentNum(j4), j6, segmentNum);
                } else {
                    constrainValue = mediaChunk.getNextChunkIndex();
                    if (constrainValue < j6) {
                        r0.fatalError = new BehindLiveWindowException();
                        return;
                    }
                }
                long j7 = constrainValue;
                if (j7 <= segmentNum) {
                    if (!r0.missingLastSegment || j7 < segmentNum) {
                        chunkHolder2.chunk = newMediaChunk(representationHolder, r0.dataSource, r0.trackType, r0.trackSelection.getSelectedFormat(), r0.trackSelection.getSelectionReason(), r0.trackSelection.getSelectionData(), j7, (int) Math.min((long) r0.maxSegmentsPerLoad, (segmentNum - j7) + 1));
                        return;
                    }
                }
                if (r0.manifest.dynamic) {
                    z2 = true;
                    if (r0.periodIndex >= r0.manifest.getPeriodCount() - 1) {
                        z2 = false;
                    }
                } else {
                    z2 = true;
                }
                chunkHolder2.endOfStream = z2;
            }
        }
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof InitializationChunk) {
            RepresentationHolder representationHolder = this.representationHolders[this.trackSelection.indexOf(((InitializationChunk) chunk).trackFormat)];
            if (representationHolder.segmentIndex == null) {
                SeekMap seekMap = representationHolder.extractorWrapper.getSeekMap();
                if (seekMap != null) {
                    representationHolder.segmentIndex = new DashWrappingSegmentIndex((ChunkIndex) seekMap);
                }
            }
        }
        if (this.playerTrackEmsgHandler != null) {
            this.playerTrackEmsgHandler.onChunkLoadCompleted(chunk);
        }
    }

    public boolean onChunkLoadError(Chunk chunk, boolean z, Exception exception) {
        if (!z) {
            return null;
        }
        if (this.playerTrackEmsgHandler && this.playerTrackEmsgHandler.maybeRefreshManifestOnLoadingError(chunk)) {
            return true;
        }
        if (!this.manifest.dynamic && (chunk instanceof MediaChunk) && (exception instanceof InvalidResponseCodeException) && ((InvalidResponseCodeException) exception).responseCode) {
            z = this.representationHolders[this.trackSelection.indexOf(chunk.trackFormat)];
            int segmentCount = z.getSegmentCount();
            if (!(segmentCount == -1 || segmentCount == 0)) {
                if (((MediaChunk) chunk).getNextChunkIndex() > (z.getFirstSegmentNum() + ((long) segmentCount)) - 1) {
                    this.missingLastSegment = true;
                    return true;
                }
            }
        }
        return ChunkedTrackBlacklistUtil.maybeBlacklistTrack(this.trackSelection, this.trackSelection.indexOf(chunk.trackFormat), exception);
    }

    private ArrayList<Representation> getRepresentations() {
        List list = this.manifest.getPeriod(this.periodIndex).adaptationSets;
        ArrayList<Representation> arrayList = new ArrayList();
        for (int i : this.adaptationSetIndices) {
            arrayList.addAll(((AdaptationSet) list.get(i)).representations);
        }
        return arrayList;
    }

    private void updateLiveEdgeTimeUs(RepresentationHolder representationHolder, long j) {
        this.liveEdgeTimeUs = this.manifest.dynamic ? representationHolder.getSegmentEndTimeUs(j) : 1;
    }

    private long getNowUnixTimeUs() {
        if (this.elapsedRealtimeOffsetMs != 0) {
            return (SystemClock.elapsedRealtime() + this.elapsedRealtimeOffsetMs) * 1000;
        }
        return System.currentTimeMillis() * 1000;
    }

    private long resolveTimeToLiveEdgeUs(long j) {
        Object obj = (!this.manifest.dynamic || this.liveEdgeTimeUs == C0649C.TIME_UNSET) ? null : 1;
        if (obj != null) {
            return this.liveEdgeTimeUs - j;
        }
        return C0649C.TIME_UNSET;
    }

    protected static Chunk newInitializationChunk(RepresentationHolder representationHolder, DataSource dataSource, Format format, int i, Object obj, RangedUri rangedUri, RangedUri rangedUri2) {
        String str = representationHolder.representation.baseUrl;
        if (rangedUri != null) {
            rangedUri2 = rangedUri.attemptMerge(rangedUri2, str);
            if (rangedUri2 == null) {
                rangedUri2 = rangedUri;
            }
        }
        return new InitializationChunk(dataSource, new DataSpec(rangedUri2.resolveUri(str), rangedUri2.start, rangedUri2.length, representationHolder.representation.getCacheKey()), format, i, obj, representationHolder.extractorWrapper);
    }

    protected static Chunk newMediaChunk(RepresentationHolder representationHolder, DataSource dataSource, int i, Format format, int i2, Object obj, long j, int i3) {
        RepresentationHolder representationHolder2 = representationHolder;
        long j2 = j;
        Representation representation = representationHolder2.representation;
        long segmentStartTimeUs = representationHolder2.getSegmentStartTimeUs(j2);
        RangedUri segmentUrl = representationHolder2.getSegmentUrl(j2);
        String str = representation.baseUrl;
        if (representationHolder2.extractorWrapper == null) {
            return new SingleSampleMediaChunk(dataSource, new DataSpec(segmentUrl.resolveUri(str), segmentUrl.start, segmentUrl.length, representation.getCacheKey()), format, i2, obj, segmentStartTimeUs, representationHolder2.getSegmentEndTimeUs(j2), j2, i, format);
        }
        int i4 = 1;
        RangedUri rangedUri = segmentUrl;
        int i5 = 1;
        int i6 = i3;
        while (i4 < i6) {
            RangedUri attemptMerge = rangedUri.attemptMerge(representationHolder2.getSegmentUrl(j2 + ((long) i4)), str);
            if (attemptMerge == null) {
                break;
            }
            i5++;
            i4++;
            rangedUri = attemptMerge;
        }
        long segmentEndTimeUs = representationHolder2.getSegmentEndTimeUs((j2 + ((long) i5)) - 1);
        Uri resolveUri = rangedUri.resolveUri(str);
        long j3 = rangedUri.start;
        long j4 = j3;
        long j5 = rangedUri.length;
        return new ContainerMediaChunk(dataSource, new DataSpec(resolveUri, j4, j5, representation.getCacheKey()), format, i2, obj, segmentStartTimeUs, segmentEndTimeUs, j, i5, -representation.presentationTimeOffsetUs, representationHolder2.extractorWrapper);
    }
}
