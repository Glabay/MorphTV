package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class SingleSampleMediaChunk extends BaseMediaChunk {
    private volatile int bytesLoaded;
    private volatile boolean loadCanceled;
    private volatile boolean loadCompleted;
    private final Format sampleFormat;
    private final int trackType;

    public SingleSampleMediaChunk(DataSource dataSource, DataSpec dataSpec, Format format, int i, Object obj, long j, long j2, long j3, int i2, Format format2) {
        super(dataSource, dataSpec, format, i, obj, j, j2, j3);
        this.trackType = i2;
        this.sampleFormat = format2;
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
        try {
            long open = this.dataSource.open(this.dataSpec.subrange((long) this.bytesLoaded));
            ExtractorInput defaultExtractorInput = new DefaultExtractorInput(this.dataSource, (long) this.bytesLoaded, open != -1 ? open + ((long) this.bytesLoaded) : open);
            BaseMediaChunkOutput output = getOutput();
            output.setSampleOffsetUs(0);
            int i = 0;
            TrackOutput track = output.track(0, this.trackType);
            track.format(this.sampleFormat);
            while (i != -1) {
                this.bytesLoaded += i;
                i = track.sampleData(defaultExtractorInput, Integer.MAX_VALUE, true);
            }
            track.sampleMetadata(this.startTimeUs, 1, this.bytesLoaded, 0, null);
            this.loadCompleted = true;
        } finally {
            Util.closeQuietly(this.dataSource);
        }
    }
}
