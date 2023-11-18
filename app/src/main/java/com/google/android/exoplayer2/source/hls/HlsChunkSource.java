package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.os.SystemClock;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.exoplayer2.source.chunk.DataChunk;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.trackselection.BaseTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

class HlsChunkSource {
    private final DataSource encryptionDataSource;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private HlsUrl expectedPlaylistUrl;
    private final HlsExtractorFactory extractorFactory;
    private IOException fatalError;
    private boolean independentSegments;
    private boolean isTimestampMaster;
    private long liveEdgeTimeUs = 1;
    private final DataSource mediaDataSource;
    private final List<Format> muxedCaptionFormats;
    private final HlsPlaylistTracker playlistTracker;
    private byte[] scratchSpace;
    private final TimestampAdjusterProvider timestampAdjusterProvider;
    private final TrackGroup trackGroup;
    private TrackSelection trackSelection;
    private final HlsUrl[] variants;

    private static final class EncryptionKeyChunk extends DataChunk {
        public final String iv;
        private byte[] result;

        public EncryptionKeyChunk(DataSource dataSource, DataSpec dataSpec, Format format, int i, Object obj, byte[] bArr, String str) {
            super(dataSource, dataSpec, 3, format, i, obj, bArr);
            this.iv = str;
        }

        protected void consume(byte[] bArr, int i) throws IOException {
            this.result = Arrays.copyOf(bArr, i);
        }

        public byte[] getResult() {
            return this.result;
        }
    }

    public static final class HlsChunkHolder {
        public Chunk chunk;
        public boolean endOfStream;
        public HlsUrl playlist;

        public HlsChunkHolder() {
            clear();
        }

        public void clear() {
            this.chunk = null;
            this.endOfStream = false;
            this.playlist = null;
        }
    }

    private static final class InitializationTrackSelection extends BaseTrackSelection {
        private int selectedIndex;

        public Object getSelectionData() {
            return null;
        }

        public int getSelectionReason() {
            return 0;
        }

        public InitializationTrackSelection(TrackGroup trackGroup, int[] iArr) {
            super(trackGroup, iArr);
            this.selectedIndex = indexOf((Format) trackGroup.getFormat(null));
        }

        public void updateSelectedTrack(long j, long j2, long j3) {
            j = SystemClock.elapsedRealtime();
            if (isBlacklisted(this.selectedIndex, j) != null) {
                j2 = this.length - 1;
                while (j2 >= null) {
                    if (isBlacklisted(j2, j)) {
                        j2--;
                    } else {
                        this.selectedIndex = j2;
                        return;
                    }
                }
                throw new IllegalStateException();
            }
        }

        public int getSelectedIndex() {
            return this.selectedIndex;
        }
    }

    public HlsChunkSource(HlsExtractorFactory hlsExtractorFactory, HlsPlaylistTracker hlsPlaylistTracker, HlsUrl[] hlsUrlArr, HlsDataSourceFactory hlsDataSourceFactory, TimestampAdjusterProvider timestampAdjusterProvider, List<Format> list) {
        this.extractorFactory = hlsExtractorFactory;
        this.playlistTracker = hlsPlaylistTracker;
        this.variants = hlsUrlArr;
        this.timestampAdjusterProvider = timestampAdjusterProvider;
        this.muxedCaptionFormats = list;
        hlsExtractorFactory = new Format[hlsUrlArr.length];
        hlsPlaylistTracker = new int[hlsUrlArr.length];
        for (timestampAdjusterProvider = null; timestampAdjusterProvider < hlsUrlArr.length; timestampAdjusterProvider++) {
            hlsExtractorFactory[timestampAdjusterProvider] = hlsUrlArr[timestampAdjusterProvider].format;
            hlsPlaylistTracker[timestampAdjusterProvider] = timestampAdjusterProvider;
        }
        this.mediaDataSource = hlsDataSourceFactory.createDataSource(1);
        this.encryptionDataSource = hlsDataSourceFactory.createDataSource(3);
        this.trackGroup = new TrackGroup(hlsExtractorFactory);
        this.trackSelection = new InitializationTrackSelection(this.trackGroup, hlsPlaylistTracker);
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        } else if (this.expectedPlaylistUrl != null) {
            this.playlistTracker.maybeThrowPlaylistRefreshError(this.expectedPlaylistUrl);
        }
    }

    public TrackGroup getTrackGroup() {
        return this.trackGroup;
    }

    public void selectTracks(TrackSelection trackSelection) {
        this.trackSelection = trackSelection;
    }

    public TrackSelection getTrackSelection() {
        return this.trackSelection;
    }

    public void reset() {
        this.fatalError = null;
    }

    public void setIsTimestampMaster(boolean z) {
        this.isTimestampMaster = z;
    }

    public void getNextChunk(HlsMediaChunk hlsMediaChunk, long j, long j2, HlsChunkHolder hlsChunkHolder) {
        int i;
        long j3;
        long j4;
        long durationUs;
        long max;
        HlsChunkSource hlsChunkSource = this;
        HlsMediaChunk hlsMediaChunk2 = hlsMediaChunk;
        long j5 = j;
        HlsChunkHolder hlsChunkHolder2 = hlsChunkHolder;
        if (hlsMediaChunk2 == null) {
            i = -1;
        } else {
            i = hlsChunkSource.trackGroup.indexOf(hlsMediaChunk2.trackFormat);
        }
        hlsChunkSource.expectedPlaylistUrl = null;
        long j6 = j2 - j5;
        long resolveTimeToLiveEdgeUs = resolveTimeToLiveEdgeUs(j5);
        if (hlsMediaChunk2 == null || hlsChunkSource.independentSegments) {
            j3 = resolveTimeToLiveEdgeUs;
            j4 = j6;
        } else {
            durationUs = hlsMediaChunk.getDurationUs();
            max = Math.max(0, j6 - durationUs);
            if (resolveTimeToLiveEdgeUs != C0649C.TIME_UNSET) {
                j4 = max;
                j3 = Math.max(0, resolveTimeToLiveEdgeUs - durationUs);
            } else {
                j4 = max;
                j3 = resolveTimeToLiveEdgeUs;
            }
        }
        hlsChunkSource.trackSelection.updateSelectedTrack(j5, j4, j3);
        int selectedIndexInTrackGroup = hlsChunkSource.trackSelection.getSelectedIndexInTrackGroup();
        boolean z = false;
        Object obj = i != selectedIndexInTrackGroup ? 1 : null;
        HlsUrl hlsUrl = hlsChunkSource.variants[selectedIndexInTrackGroup];
        if (hlsChunkSource.playlistTracker.isSnapshotValid(hlsUrl)) {
            long nextChunkIndex;
            HlsUrl hlsUrl2;
            Segment segment;
            Uri resolveToUri;
            Segment segment2;
            DataSpec dataSpec;
            long initialStartTimeUs;
            int i2;
            TimestampAdjuster adjuster;
            DataSpec dataSpec2;
            HlsExtractorFactory hlsExtractorFactory;
            DataSource dataSource;
            List list;
            int selectionReason;
            Object selectionData;
            int i3;
            long j7;
            boolean z2;
            boolean z3;
            DrmInitData drmInitData;
            byte[] bArr;
            byte[] bArr2;
            HlsChunkHolder hlsChunkHolder3;
            List list2;
            Object valueOf;
            HlsMediaPlaylist playlistSnapshot = hlsChunkSource.playlistTracker.getPlaylistSnapshot(hlsUrl);
            hlsChunkSource.independentSegments = playlistSnapshot.hasIndependentSegmentsTag;
            updateLiveEdgeTimeUs(playlistSnapshot);
            if (hlsMediaChunk2 != null) {
                if (obj == null) {
                    nextChunkIndex = hlsMediaChunk.getNextChunkIndex();
                    hlsUrl2 = hlsUrl;
                    i = selectedIndexInTrackGroup;
                    if (nextChunkIndex < playlistSnapshot.mediaSequence) {
                        hlsChunkSource.fatalError = new BehindLiveWindowException();
                        return;
                    }
                    selectedIndexInTrackGroup = (int) (nextChunkIndex - playlistSnapshot.mediaSequence);
                    if (selectedIndexInTrackGroup >= playlistSnapshot.segments.size()) {
                        if (playlistSnapshot.hasEndTag) {
                            hlsChunkHolder2.endOfStream = true;
                        } else {
                            hlsChunkHolder2.playlist = hlsUrl2;
                            hlsChunkSource.expectedPlaylistUrl = hlsUrl2;
                        }
                        return;
                    }
                    segment = (Segment) playlistSnapshot.segments.get(selectedIndexInTrackGroup);
                    if (segment.fullSegmentEncryptionKeyUri != null) {
                        resolveToUri = UriUtil.resolveToUri(playlistSnapshot.baseUri, segment.fullSegmentEncryptionKeyUri);
                        if (resolveToUri.equals(hlsChunkSource.encryptionKeyUri)) {
                            hlsChunkHolder2.chunk = newEncryptionKeyChunk(resolveToUri, segment.encryptionIV, i, hlsChunkSource.trackSelection.getSelectionReason(), hlsChunkSource.trackSelection.getSelectionData());
                            return;
                        } else if (!Util.areEqual(segment.encryptionIV, hlsChunkSource.encryptionIvString)) {
                            setEncryptionData(resolveToUri, segment.encryptionIV, hlsChunkSource.encryptionKey);
                        }
                    } else {
                        clearEncryptionData();
                    }
                    segment2 = playlistSnapshot.initializationSegment;
                    dataSpec = segment2 != null ? new DataSpec(UriUtil.resolveToUri(playlistSnapshot.baseUri, segment2.url), segment2.byterangeOffset, segment2.byterangeLength, null) : null;
                    initialStartTimeUs = (playlistSnapshot.startTimeUs - hlsChunkSource.playlistTracker.getInitialStartTimeUs()) + segment.relativeStartTimeUs;
                    i2 = playlistSnapshot.discontinuitySequence + segment.relativeDiscontinuitySequence;
                    adjuster = hlsChunkSource.timestampAdjusterProvider.getAdjuster(i2);
                    dataSpec2 = new DataSpec(UriUtil.resolveToUri(playlistSnapshot.baseUri, segment.url), segment.byterangeOffset, segment.byterangeLength, null);
                    hlsExtractorFactory = hlsChunkSource.extractorFactory;
                    dataSource = hlsChunkSource.mediaDataSource;
                    list = hlsChunkSource.muxedCaptionFormats;
                    selectionReason = hlsChunkSource.trackSelection.getSelectionReason();
                    selectionData = hlsChunkSource.trackSelection.getSelectionData();
                    i3 = selectionReason;
                    j7 = initialStartTimeUs + segment.durationUs;
                    z2 = segment.hasGapTag;
                    z3 = hlsChunkSource.isTimestampMaster;
                    drmInitData = playlistSnapshot.drmInitData;
                    bArr = hlsChunkSource.encryptionKey;
                    bArr2 = hlsChunkSource.encryptionIv;
                    hlsChunkHolder3 = hlsChunkHolder;
                    hlsChunkHolder3.chunk = new HlsMediaChunk(hlsExtractorFactory, dataSource, dataSpec2, dataSpec, hlsUrl2, list, i3, selectionData, initialStartTimeUs, j7, nextChunkIndex, i2, z2, z3, adjuster, hlsMediaChunk2, drmInitData, bArr, bArr2);
                    return;
                }
            }
            if (hlsMediaChunk2 != null) {
                if (!hlsChunkSource.independentSegments) {
                    resolveTimeToLiveEdgeUs = hlsMediaChunk2.startTimeUs;
                    if (!playlistSnapshot.hasEndTag || resolveTimeToLiveEdgeUs < playlistSnapshot.getEndTimeUs()) {
                        list2 = playlistSnapshot.segments;
                        valueOf = Long.valueOf(resolveTimeToLiveEdgeUs);
                        if (!hlsChunkSource.playlistTracker.isLive() || hlsMediaChunk2 == null) {
                            z = true;
                        }
                        max = ((long) Util.binarySearchFloor(list2, valueOf, true, z)) + playlistSnapshot.mediaSequence;
                        if (max < playlistSnapshot.mediaSequence || hlsMediaChunk2 == null) {
                            durationUs = max;
                        } else {
                            hlsUrl = hlsChunkSource.variants[i];
                            HlsMediaPlaylist playlistSnapshot2 = hlsChunkSource.playlistTracker.getPlaylistSnapshot(hlsUrl);
                            durationUs = hlsMediaChunk.getNextChunkIndex();
                            playlistSnapshot = playlistSnapshot2;
                            selectedIndexInTrackGroup = i;
                        }
                    } else {
                        durationUs = playlistSnapshot.mediaSequence + ((long) playlistSnapshot.segments.size());
                    }
                    i = selectedIndexInTrackGroup;
                    nextChunkIndex = durationUs;
                    hlsUrl2 = hlsUrl;
                    if (nextChunkIndex < playlistSnapshot.mediaSequence) {
                        selectedIndexInTrackGroup = (int) (nextChunkIndex - playlistSnapshot.mediaSequence);
                        if (selectedIndexInTrackGroup >= playlistSnapshot.segments.size()) {
                            segment = (Segment) playlistSnapshot.segments.get(selectedIndexInTrackGroup);
                            if (segment.fullSegmentEncryptionKeyUri != null) {
                                clearEncryptionData();
                            } else {
                                resolveToUri = UriUtil.resolveToUri(playlistSnapshot.baseUri, segment.fullSegmentEncryptionKeyUri);
                                if (resolveToUri.equals(hlsChunkSource.encryptionKeyUri)) {
                                    hlsChunkHolder2.chunk = newEncryptionKeyChunk(resolveToUri, segment.encryptionIV, i, hlsChunkSource.trackSelection.getSelectionReason(), hlsChunkSource.trackSelection.getSelectionData());
                                    return;
                                } else if (Util.areEqual(segment.encryptionIV, hlsChunkSource.encryptionIvString)) {
                                    setEncryptionData(resolveToUri, segment.encryptionIV, hlsChunkSource.encryptionKey);
                                }
                            }
                            segment2 = playlistSnapshot.initializationSegment;
                            if (segment2 != null) {
                            }
                            initialStartTimeUs = (playlistSnapshot.startTimeUs - hlsChunkSource.playlistTracker.getInitialStartTimeUs()) + segment.relativeStartTimeUs;
                            i2 = playlistSnapshot.discontinuitySequence + segment.relativeDiscontinuitySequence;
                            adjuster = hlsChunkSource.timestampAdjusterProvider.getAdjuster(i2);
                            dataSpec2 = new DataSpec(UriUtil.resolveToUri(playlistSnapshot.baseUri, segment.url), segment.byterangeOffset, segment.byterangeLength, null);
                            hlsExtractorFactory = hlsChunkSource.extractorFactory;
                            dataSource = hlsChunkSource.mediaDataSource;
                            list = hlsChunkSource.muxedCaptionFormats;
                            selectionReason = hlsChunkSource.trackSelection.getSelectionReason();
                            selectionData = hlsChunkSource.trackSelection.getSelectionData();
                            i3 = selectionReason;
                            j7 = initialStartTimeUs + segment.durationUs;
                            z2 = segment.hasGapTag;
                            z3 = hlsChunkSource.isTimestampMaster;
                            drmInitData = playlistSnapshot.drmInitData;
                            bArr = hlsChunkSource.encryptionKey;
                            bArr2 = hlsChunkSource.encryptionIv;
                            hlsChunkHolder3 = hlsChunkHolder;
                            hlsChunkHolder3.chunk = new HlsMediaChunk(hlsExtractorFactory, dataSource, dataSpec2, dataSpec, hlsUrl2, list, i3, selectionData, initialStartTimeUs, j7, nextChunkIndex, i2, z2, z3, adjuster, hlsMediaChunk2, drmInitData, bArr, bArr2);
                            return;
                        }
                        if (playlistSnapshot.hasEndTag) {
                            hlsChunkHolder2.playlist = hlsUrl2;
                            hlsChunkSource.expectedPlaylistUrl = hlsUrl2;
                        } else {
                            hlsChunkHolder2.endOfStream = true;
                        }
                        return;
                    }
                    hlsChunkSource.fatalError = new BehindLiveWindowException();
                    return;
                }
            }
            resolveTimeToLiveEdgeUs = j2;
            if (playlistSnapshot.hasEndTag) {
            }
            list2 = playlistSnapshot.segments;
            valueOf = Long.valueOf(resolveTimeToLiveEdgeUs);
            z = true;
            max = ((long) Util.binarySearchFloor(list2, valueOf, true, z)) + playlistSnapshot.mediaSequence;
            if (max < playlistSnapshot.mediaSequence) {
            }
            durationUs = max;
            i = selectedIndexInTrackGroup;
            nextChunkIndex = durationUs;
            hlsUrl2 = hlsUrl;
            if (nextChunkIndex < playlistSnapshot.mediaSequence) {
                hlsChunkSource.fatalError = new BehindLiveWindowException();
                return;
            }
            selectedIndexInTrackGroup = (int) (nextChunkIndex - playlistSnapshot.mediaSequence);
            if (selectedIndexInTrackGroup >= playlistSnapshot.segments.size()) {
                if (playlistSnapshot.hasEndTag) {
                    hlsChunkHolder2.endOfStream = true;
                } else {
                    hlsChunkHolder2.playlist = hlsUrl2;
                    hlsChunkSource.expectedPlaylistUrl = hlsUrl2;
                }
                return;
            }
            segment = (Segment) playlistSnapshot.segments.get(selectedIndexInTrackGroup);
            if (segment.fullSegmentEncryptionKeyUri != null) {
                resolveToUri = UriUtil.resolveToUri(playlistSnapshot.baseUri, segment.fullSegmentEncryptionKeyUri);
                if (resolveToUri.equals(hlsChunkSource.encryptionKeyUri)) {
                    hlsChunkHolder2.chunk = newEncryptionKeyChunk(resolveToUri, segment.encryptionIV, i, hlsChunkSource.trackSelection.getSelectionReason(), hlsChunkSource.trackSelection.getSelectionData());
                    return;
                } else if (Util.areEqual(segment.encryptionIV, hlsChunkSource.encryptionIvString)) {
                    setEncryptionData(resolveToUri, segment.encryptionIV, hlsChunkSource.encryptionKey);
                }
            } else {
                clearEncryptionData();
            }
            segment2 = playlistSnapshot.initializationSegment;
            if (segment2 != null) {
            }
            initialStartTimeUs = (playlistSnapshot.startTimeUs - hlsChunkSource.playlistTracker.getInitialStartTimeUs()) + segment.relativeStartTimeUs;
            i2 = playlistSnapshot.discontinuitySequence + segment.relativeDiscontinuitySequence;
            adjuster = hlsChunkSource.timestampAdjusterProvider.getAdjuster(i2);
            dataSpec2 = new DataSpec(UriUtil.resolveToUri(playlistSnapshot.baseUri, segment.url), segment.byterangeOffset, segment.byterangeLength, null);
            hlsExtractorFactory = hlsChunkSource.extractorFactory;
            dataSource = hlsChunkSource.mediaDataSource;
            list = hlsChunkSource.muxedCaptionFormats;
            selectionReason = hlsChunkSource.trackSelection.getSelectionReason();
            selectionData = hlsChunkSource.trackSelection.getSelectionData();
            i3 = selectionReason;
            j7 = initialStartTimeUs + segment.durationUs;
            z2 = segment.hasGapTag;
            z3 = hlsChunkSource.isTimestampMaster;
            drmInitData = playlistSnapshot.drmInitData;
            bArr = hlsChunkSource.encryptionKey;
            bArr2 = hlsChunkSource.encryptionIv;
            hlsChunkHolder3 = hlsChunkHolder;
            hlsChunkHolder3.chunk = new HlsMediaChunk(hlsExtractorFactory, dataSource, dataSpec2, dataSpec, hlsUrl2, list, i3, selectionData, initialStartTimeUs, j7, nextChunkIndex, i2, z2, z3, adjuster, hlsMediaChunk2, drmInitData, bArr, bArr2);
            return;
        }
        hlsChunkHolder2.playlist = hlsUrl;
        hlsChunkSource.expectedPlaylistUrl = hlsUrl;
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof EncryptionKeyChunk) {
            EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk) chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.iv, encryptionKeyChunk.getResult());
        }
    }

    public boolean onChunkLoadError(Chunk chunk, boolean z, IOException iOException) {
        return (!z || ChunkedTrackBlacklistUtil.maybeBlacklistTrack(this.trackSelection, this.trackSelection.indexOf(this.trackGroup.indexOf(chunk.trackFormat)), iOException) == null) ? null : true;
    }

    public void onPlaylistBlacklisted(HlsUrl hlsUrl, long j) {
        int indexOf = this.trackGroup.indexOf(hlsUrl.format);
        if (indexOf != -1) {
            hlsUrl = this.trackSelection.indexOf(indexOf);
            if (hlsUrl != -1) {
                this.trackSelection.blacklist(hlsUrl, j);
            }
        }
    }

    private long resolveTimeToLiveEdgeUs(long j) {
        if ((this.liveEdgeTimeUs != C0649C.TIME_UNSET ? 1 : null) != null) {
            return this.liveEdgeTimeUs - j;
        }
        return C0649C.TIME_UNSET;
    }

    private void updateLiveEdgeTimeUs(HlsMediaPlaylist hlsMediaPlaylist) {
        this.liveEdgeTimeUs = hlsMediaPlaylist.hasEndTag ? C0649C.TIME_UNSET : hlsMediaPlaylist.getEndTimeUs();
    }

    private EncryptionKeyChunk newEncryptionKeyChunk(Uri uri, String str, int i, int i2, Object obj) {
        return new EncryptionKeyChunk(this.encryptionDataSource, new DataSpec(uri, 0, -1, null, 1), this.variants[i].format, i2, obj, this.scratchSpace, str);
    }

    private void setEncryptionData(Uri uri, String str, byte[] bArr) {
        Object toByteArray = new BigInteger(Util.toLowerInvariant(str).startsWith("0x") ? str.substring(2) : str, 16).toByteArray();
        Object obj = new byte[16];
        int length = toByteArray.length > 16 ? toByteArray.length - 16 : 0;
        System.arraycopy(toByteArray, length, obj, (obj.length - toByteArray.length) + length, toByteArray.length - length);
        this.encryptionKeyUri = uri;
        this.encryptionKey = bArr;
        this.encryptionIvString = str;
        this.encryptionIv = obj;
    }

    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }
}
