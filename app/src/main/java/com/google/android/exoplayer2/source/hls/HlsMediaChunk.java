package com.google.android.exoplayer2.source.hls;

import android.util.Pair;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

final class HlsMediaChunk extends MediaChunk {
    private static final String PRIV_TIMESTAMP_FRAME_OWNER = "com.apple.streaming.transportStreamTimestamp";
    private static final AtomicInteger uidSource = new AtomicInteger();
    private int bytesLoaded;
    public final int discontinuitySequenceNumber;
    private final Extractor extractor;
    private final boolean hasGapTag;
    public final HlsUrl hlsUrl;
    private final ParsableByteArray id3Data;
    private final Id3Decoder id3Decoder;
    private boolean id3TimestampPeeked;
    private final DataSource initDataSource;
    private final DataSpec initDataSpec;
    private boolean initLoadCompleted;
    private int initSegmentBytesLoaded;
    private final boolean isEncrypted = (this.dataSource instanceof Aes128DataSource);
    private final boolean isMasterTimestampSource;
    private final boolean isPackedAudioExtractor;
    private volatile boolean loadCanceled;
    private volatile boolean loadCompleted;
    private HlsSampleStreamWrapper output;
    private final boolean reusingExtractor;
    private final boolean shouldSpliceIn;
    private final TimestampAdjuster timestampAdjuster;
    public final int uid;

    public HlsMediaChunk(HlsExtractorFactory hlsExtractorFactory, DataSource dataSource, DataSpec dataSpec, DataSpec dataSpec2, HlsUrl hlsUrl, List<Format> list, int i, Object obj, long j, long j2, long j3, int i2, boolean z, boolean z2, TimestampAdjuster timestampAdjuster, HlsMediaChunk hlsMediaChunk, DrmInitData drmInitData, byte[] bArr, byte[] bArr2) {
        Extractor extractor;
        DataSpec dataSpec3;
        DataSpec dataSpec4 = dataSpec2;
        HlsUrl hlsUrl2 = hlsUrl;
        HlsMediaChunk hlsMediaChunk2 = hlsMediaChunk;
        int i3 = i2;
        super(buildDataSource(dataSource, bArr, bArr2), dataSpec, hlsUrl2.format, i, obj, j, j2, j3);
        this.discontinuitySequenceNumber = i3;
        this.initDataSpec = dataSpec4;
        this.hlsUrl = hlsUrl2;
        this.isMasterTimestampSource = z2;
        TimestampAdjuster timestampAdjuster2 = timestampAdjuster;
        this.timestampAdjuster = timestampAdjuster2;
        this.hasGapTag = z;
        boolean z3 = true;
        HlsMediaChunk hlsMediaChunk3 = hlsMediaChunk;
        if (hlsMediaChunk3 != null) {
            Extractor extractor2;
            r12.shouldSpliceIn = hlsMediaChunk3.hlsUrl != hlsUrl2;
            if (hlsMediaChunk3.discontinuitySequenceNumber == i3) {
                if (!r12.shouldSpliceIn) {
                    extractor2 = hlsMediaChunk3.extractor;
                    extractor = extractor2;
                    dataSpec3 = dataSpec;
                }
            }
            extractor2 = null;
            extractor = extractor2;
            dataSpec3 = dataSpec;
        } else {
            r12.shouldSpliceIn = false;
            dataSpec3 = dataSpec;
            extractor = null;
        }
        Pair createExtractor = hlsExtractorFactory.createExtractor(extractor, dataSpec3.uri, r12.trackFormat, list, drmInitData, timestampAdjuster2);
        r12.extractor = (Extractor) createExtractor.first;
        r12.isPackedAudioExtractor = ((Boolean) createExtractor.second).booleanValue();
        r12.reusingExtractor = r12.extractor == extractor;
        if (!r12.reusingExtractor || dataSpec4 == null) {
            z3 = false;
        }
        r12.initLoadCompleted = z3;
        if (!r12.isPackedAudioExtractor) {
            r12.id3Decoder = null;
            r12.id3Data = null;
        } else if (hlsMediaChunk3 == null || hlsMediaChunk3.id3Data == null) {
            r12.id3Decoder = new Id3Decoder();
            r12.id3Data = new ParsableByteArray(10);
        } else {
            r12.id3Decoder = hlsMediaChunk3.id3Decoder;
            r12.id3Data = hlsMediaChunk3.id3Data;
        }
        r12.initDataSource = dataSource;
        r12.uid = uidSource.getAndIncrement();
    }

    public void init(HlsSampleStreamWrapper hlsSampleStreamWrapper) {
        this.output = hlsSampleStreamWrapper;
        hlsSampleStreamWrapper.init(this.uid, this.shouldSpliceIn, this.reusingExtractor);
        if (!this.reusingExtractor) {
            this.extractor.init(hlsSampleStreamWrapper);
        }
    }

    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    public long bytesLoaded() {
        return (long) this.bytesLoaded;
    }

    public void cancelLoad() {
        this.loadCanceled = true;
    }

    public boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    public void load() throws IOException, InterruptedException {
        maybeLoadInitData();
        if (!this.loadCanceled) {
            if (!this.hasGapTag) {
                loadMedia();
            }
            this.loadCompleted = true;
        }
    }

    private void maybeLoadInitData() throws IOException, InterruptedException {
        if (!this.initLoadCompleted) {
            if (this.initDataSpec != null) {
                DataSpec subrange = this.initDataSpec.subrange((long) this.initSegmentBytesLoaded);
                ExtractorInput defaultExtractorInput;
                try {
                    defaultExtractorInput = new DefaultExtractorInput(this.initDataSource, subrange.absoluteStreamPosition, this.initDataSource.open(subrange));
                    int i = 0;
                    while (i == 0) {
                        if (this.loadCanceled) {
                            break;
                        }
                        i = this.extractor.read(defaultExtractorInput, null);
                    }
                    this.initSegmentBytesLoaded = (int) (defaultExtractorInput.getPosition() - this.initDataSpec.absoluteStreamPosition);
                    Util.closeQuietly(this.dataSource);
                    this.initLoadCompleted = true;
                } catch (Throwable th) {
                    Util.closeQuietly(this.dataSource);
                }
            }
        }
    }

    private void loadMedia() throws IOException, InterruptedException {
        DataSpec dataSpec;
        Object obj;
        ExtractorInput defaultExtractorInput;
        int i = 0;
        if (this.isEncrypted) {
            dataSpec = this.dataSpec;
            if (this.bytesLoaded != 0) {
                obj = 1;
                if (!this.isMasterTimestampSource) {
                    this.timestampAdjuster.waitUntilInitialized();
                } else if (this.timestampAdjuster.getFirstSampleTimestampUs() == Long.MAX_VALUE) {
                    this.timestampAdjuster.setFirstSampleTimestampUs(this.startTimeUs);
                }
                defaultExtractorInput = new DefaultExtractorInput(this.dataSource, dataSpec.absoluteStreamPosition, this.dataSource.open(dataSpec));
                if (this.isPackedAudioExtractor && !this.id3TimestampPeeked) {
                    long peekId3PrivTimestamp = peekId3PrivTimestamp(defaultExtractorInput);
                    this.id3TimestampPeeked = true;
                    this.output.setSampleOffsetUs(peekId3PrivTimestamp == C0649C.TIME_UNSET ? this.timestampAdjuster.adjustTsTimestamp(peekId3PrivTimestamp) : this.startTimeUs);
                }
                if (obj != null) {
                    defaultExtractorInput.skipFully(this.bytesLoaded);
                }
                while (i == 0) {
                    if (!this.loadCanceled) {
                        break;
                    }
                    i = this.extractor.read(defaultExtractorInput, null);
                }
                this.bytesLoaded = (int) (defaultExtractorInput.getPosition() - this.dataSpec.absoluteStreamPosition);
                Util.closeQuietly(this.dataSource);
            }
        }
        dataSpec = this.dataSpec.subrange((long) this.bytesLoaded);
        obj = null;
        if (!this.isMasterTimestampSource) {
            this.timestampAdjuster.waitUntilInitialized();
        } else if (this.timestampAdjuster.getFirstSampleTimestampUs() == Long.MAX_VALUE) {
            this.timestampAdjuster.setFirstSampleTimestampUs(this.startTimeUs);
        }
        try {
            defaultExtractorInput = new DefaultExtractorInput(this.dataSource, dataSpec.absoluteStreamPosition, this.dataSource.open(dataSpec));
            long peekId3PrivTimestamp2 = peekId3PrivTimestamp(defaultExtractorInput);
            this.id3TimestampPeeked = true;
            if (peekId3PrivTimestamp2 == C0649C.TIME_UNSET) {
            }
            this.output.setSampleOffsetUs(peekId3PrivTimestamp2 == C0649C.TIME_UNSET ? this.timestampAdjuster.adjustTsTimestamp(peekId3PrivTimestamp2) : this.startTimeUs);
            if (obj != null) {
                defaultExtractorInput.skipFully(this.bytesLoaded);
            }
            while (i == 0) {
                if (!this.loadCanceled) {
                    break;
                }
                i = this.extractor.read(defaultExtractorInput, null);
            }
            this.bytesLoaded = (int) (defaultExtractorInput.getPosition() - this.dataSpec.absoluteStreamPosition);
            Util.closeQuietly(this.dataSource);
        } catch (Throwable th) {
            Util.closeQuietly(this.dataSource);
        }
    }

    private long peekId3PrivTimestamp(ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.resetPeekPosition();
        if (!extractorInput.peekFully(this.id3Data.data, 0, 10, true)) {
            return C0649C.TIME_UNSET;
        }
        this.id3Data.reset(10);
        if (this.id3Data.readUnsignedInt24() != Id3Decoder.ID3_TAG) {
            return C0649C.TIME_UNSET;
        }
        this.id3Data.skipBytes(3);
        int readSynchSafeInt = this.id3Data.readSynchSafeInt();
        int i = readSynchSafeInt + 10;
        if (i > this.id3Data.capacity()) {
            Object obj = this.id3Data.data;
            this.id3Data.reset(i);
            System.arraycopy(obj, 0, this.id3Data.data, 0, 10);
        }
        if (extractorInput.peekFully(this.id3Data.data, 10, readSynchSafeInt, true) == null) {
            return C0649C.TIME_UNSET;
        }
        extractorInput = this.id3Decoder.decode(this.id3Data.data, readSynchSafeInt);
        if (extractorInput == null) {
            return C0649C.TIME_UNSET;
        }
        readSynchSafeInt = extractorInput.length();
        for (int i2 = 0; i2 < readSynchSafeInt; i2++) {
            Entry entry = extractorInput.get(i2);
            if (entry instanceof PrivFrame) {
                PrivFrame privFrame = (PrivFrame) entry;
                if (PRIV_TIMESTAMP_FRAME_OWNER.equals(privFrame.owner)) {
                    System.arraycopy(privFrame.privateData, 0, this.id3Data.data, 0, 8);
                    this.id3Data.reset(8);
                    return this.id3Data.readLong() & 8589934591L;
                }
            }
        }
        return C0649C.TIME_UNSET;
    }

    private static DataSource buildDataSource(DataSource dataSource, byte[] bArr, byte[] bArr2) {
        return bArr != null ? new Aes128DataSource(dataSource, bArr, bArr2) : dataSource;
    }
}
