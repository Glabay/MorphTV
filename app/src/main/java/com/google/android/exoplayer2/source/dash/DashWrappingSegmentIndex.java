package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;

public final class DashWrappingSegmentIndex implements DashSegmentIndex {
    private final ChunkIndex chunkIndex;

    public long getFirstSegmentNum() {
        return 0;
    }

    public boolean isExplicit() {
        return true;
    }

    public DashWrappingSegmentIndex(ChunkIndex chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public int getSegmentCount(long j) {
        return this.chunkIndex.length;
    }

    public long getTimeUs(long j) {
        return this.chunkIndex.timesUs[(int) j];
    }

    public long getDurationUs(long j, long j2) {
        return this.chunkIndex.durationsUs[(int) j];
    }

    public RangedUri getSegmentUrl(long j) {
        j = (int) j;
        return new RangedUri(null, this.chunkIndex.offsets[j], (long) this.chunkIndex.sizes[j]);
    }

    public long getSegmentNum(long j, long j2) {
        return (long) this.chunkIndex.getChunkIndex(j);
    }
}
