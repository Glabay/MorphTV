package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper.TrackOutputProvider;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public class ContainerMediaChunk extends BaseMediaChunk {
    private volatile int bytesLoaded;
    private final int chunkCount;
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;
    private volatile boolean loadCompleted;
    private final long sampleOffsetUs;

    public ContainerMediaChunk(DataSource dataSource, DataSpec dataSpec, Format format, int i, Object obj, long j, long j2, long j3, int i2, long j4, ChunkExtractorWrapper chunkExtractorWrapper) {
        super(dataSource, dataSpec, format, i, obj, j, j2, j3);
        this.chunkCount = i2;
        this.sampleOffsetUs = j4;
        this.extractorWrapper = chunkExtractorWrapper;
    }

    public long getNextChunkIndex() {
        return this.chunkIndex + ((long) this.chunkCount);
    }

    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    public final long bytesLoaded() {
        return (long) this.bytesLoaded;
    }

    public final void cancelLoad() {
        this.loadCanceled = true;
    }

    public final boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    public final void load() throws IOException, InterruptedException {
        DataSpec subrange = this.dataSpec.subrange((long) this.bytesLoaded);
        ExtractorInput defaultExtractorInput;
        try {
            defaultExtractorInput = new DefaultExtractorInput(this.dataSource, subrange.absoluteStreamPosition, this.dataSource.open(subrange));
            if (this.bytesLoaded == 0) {
                TrackOutputProvider output = getOutput();
                output.setSampleOffsetUs(this.sampleOffsetUs);
                this.extractorWrapper.init(output);
            }
            Extractor extractor = this.extractorWrapper.extractor;
            boolean z = false;
            int i = 0;
            while (i == 0 && !this.loadCanceled) {
                i = extractor.read(defaultExtractorInput, null);
            }
            if (i != 1) {
                z = true;
            }
            Assertions.checkState(z);
            this.bytesLoaded = (int) (defaultExtractorInput.getPosition() - this.dataSpec.absoluteStreamPosition);
            Util.closeQuietly(this.dataSource);
            this.loadCompleted = true;
        } catch (Throwable th) {
            Util.closeQuietly(this.dataSource);
        }
    }
}
