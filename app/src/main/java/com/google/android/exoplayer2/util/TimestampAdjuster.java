package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.C0649C;

public final class TimestampAdjuster {
    public static final long DO_NOT_OFFSET = Long.MAX_VALUE;
    private static final long MAX_PTS_PLUS_ONE = 8589934592L;
    private long firstSampleTimestampUs;
    private volatile long lastSampleTimestamp = C0649C.TIME_UNSET;
    private long timestampOffsetUs;

    public TimestampAdjuster(long j) {
        setFirstSampleTimestampUs(j);
    }

    public synchronized void setFirstSampleTimestampUs(long j) {
        Assertions.checkState(this.lastSampleTimestamp == C0649C.TIME_UNSET);
        this.firstSampleTimestampUs = j;
    }

    public long getFirstSampleTimestampUs() {
        return this.firstSampleTimestampUs;
    }

    public long getLastAdjustedTimestampUs() {
        if (this.lastSampleTimestamp != C0649C.TIME_UNSET) {
            return this.lastSampleTimestamp;
        }
        return this.firstSampleTimestampUs != Long.MAX_VALUE ? this.firstSampleTimestampUs : C0649C.TIME_UNSET;
    }

    public long getTimestampOffsetUs() {
        if (this.firstSampleTimestampUs == Long.MAX_VALUE) {
            return 0;
        }
        return this.lastSampleTimestamp == C0649C.TIME_UNSET ? C0649C.TIME_UNSET : this.timestampOffsetUs;
    }

    public void reset() {
        this.lastSampleTimestamp = C0649C.TIME_UNSET;
    }

    public long adjustTsTimestamp(long j) {
        if (j == C0649C.TIME_UNSET) {
            return C0649C.TIME_UNSET;
        }
        if (this.lastSampleTimestamp != C0649C.TIME_UNSET) {
            long usToPts = usToPts(this.lastSampleTimestamp);
            long j2 = (usToPts + 4294967296L) / MAX_PTS_PLUS_ONE;
            long j3 = j + ((j2 - 1) * MAX_PTS_PLUS_ONE);
            long j4 = j + (j2 * MAX_PTS_PLUS_ONE);
            j = Math.abs(j3 - usToPts) < Math.abs(j4 - usToPts) ? j3 : j4;
        }
        return adjustSampleTimestamp(ptsToUs(j));
    }

    public long adjustSampleTimestamp(long j) {
        if (j == C0649C.TIME_UNSET) {
            return C0649C.TIME_UNSET;
        }
        if (this.lastSampleTimestamp != C0649C.TIME_UNSET) {
            this.lastSampleTimestamp = j;
        } else {
            if (this.firstSampleTimestampUs != Long.MAX_VALUE) {
                this.timestampOffsetUs = this.firstSampleTimestampUs - j;
            }
            synchronized (this) {
                this.lastSampleTimestamp = j;
                notifyAll();
            }
        }
        return j + this.timestampOffsetUs;
    }

    public synchronized void waitUntilInitialized() throws InterruptedException {
        while (this.lastSampleTimestamp == C0649C.TIME_UNSET) {
            wait();
        }
    }

    public static long ptsToUs(long j) {
        return (j * C0649C.MICROS_PER_SECOND) / 90000;
    }

    public static long usToPts(long j) {
        return (j * 90000) / C0649C.MICROS_PER_SECOND;
    }
}
