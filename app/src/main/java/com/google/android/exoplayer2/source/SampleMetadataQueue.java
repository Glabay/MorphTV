package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.TrackOutput.CryptoData;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;

final class SampleMetadataQueue {
    private static final int SAMPLE_CAPACITY_INCREMENT = 1000;
    private int absoluteFirstIndex;
    private int capacity = 1000;
    private CryptoData[] cryptoDatas = new CryptoData[this.capacity];
    private int[] flags = new int[this.capacity];
    private Format[] formats = new Format[this.capacity];
    private long largestDiscardedTimestampUs = Long.MIN_VALUE;
    private long largestQueuedTimestampUs = Long.MIN_VALUE;
    private int length;
    private long[] offsets = new long[this.capacity];
    private int readPosition;
    private int relativeFirstIndex;
    private int[] sizes = new int[this.capacity];
    private int[] sourceIds = new int[this.capacity];
    private long[] timesUs = new long[this.capacity];
    private Format upstreamFormat;
    private boolean upstreamFormatRequired = true;
    private boolean upstreamKeyframeRequired = true;
    private int upstreamSourceId;

    public static final class SampleExtrasHolder {
        public CryptoData cryptoData;
        public long offset;
        public int size;
    }

    public void reset(boolean z) {
        this.length = 0;
        this.absoluteFirstIndex = 0;
        this.relativeFirstIndex = 0;
        this.readPosition = 0;
        this.upstreamKeyframeRequired = true;
        this.largestDiscardedTimestampUs = Long.MIN_VALUE;
        this.largestQueuedTimestampUs = Long.MIN_VALUE;
        if (z) {
            this.upstreamFormat = false;
            this.upstreamFormatRequired = true;
        }
    }

    public int getWriteIndex() {
        return this.absoluteFirstIndex + this.length;
    }

    public long discardUpstreamSamples(int i) {
        int writeIndex = getWriteIndex() - i;
        boolean z = writeIndex >= 0 && writeIndex <= this.length - this.readPosition;
        Assertions.checkArgument(z);
        this.length -= writeIndex;
        this.largestQueuedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(this.length));
        if (this.length == 0) {
            return 0;
        }
        i = getRelativeIndex(this.length - 1);
        return this.offsets[i] + ((long) this.sizes[i]);
    }

    public void sourceId(int i) {
        this.upstreamSourceId = i;
    }

    public int getFirstIndex() {
        return this.absoluteFirstIndex;
    }

    public int getReadIndex() {
        return this.absoluteFirstIndex + this.readPosition;
    }

    public int peekSourceId() {
        return hasNextSample() ? this.sourceIds[getRelativeIndex(this.readPosition)] : this.upstreamSourceId;
    }

    public synchronized boolean hasNextSample() {
        return this.readPosition != this.length;
    }

    public synchronized Format getUpstreamFormat() {
        return this.upstreamFormatRequired ? null : this.upstreamFormat;
    }

    public synchronized long getLargestQueuedTimestampUs() {
        return this.largestQueuedTimestampUs;
    }

    public synchronized long getFirstTimestampUs() {
        return this.length == 0 ? Long.MIN_VALUE : this.timesUs[this.relativeFirstIndex];
    }

    public synchronized void rewind() {
        this.readPosition = 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read(com.google.android.exoplayer2.FormatHolder r5, com.google.android.exoplayer2.decoder.DecoderInputBuffer r6, boolean r7, boolean r8, com.google.android.exoplayer2.Format r9, com.google.android.exoplayer2.source.SampleMetadataQueue.SampleExtrasHolder r10) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.hasNextSample();	 Catch:{ all -> 0x006a }
        r1 = -3;
        r2 = -5;
        r3 = -4;
        if (r0 != 0) goto L_0x0024;
    L_0x000a:
        if (r8 == 0) goto L_0x0012;
    L_0x000c:
        r5 = 4;
        r6.setFlags(r5);	 Catch:{ all -> 0x006a }
        monitor-exit(r4);
        return r3;
    L_0x0012:
        r6 = r4.upstreamFormat;	 Catch:{ all -> 0x006a }
        if (r6 == 0) goto L_0x0022;
    L_0x0016:
        if (r7 != 0) goto L_0x001c;
    L_0x0018:
        r6 = r4.upstreamFormat;	 Catch:{ all -> 0x006a }
        if (r6 == r9) goto L_0x0022;
    L_0x001c:
        r6 = r4.upstreamFormat;	 Catch:{ all -> 0x006a }
        r5.format = r6;	 Catch:{ all -> 0x006a }
        monitor-exit(r4);
        return r2;
    L_0x0022:
        monitor-exit(r4);
        return r1;
    L_0x0024:
        r8 = r4.readPosition;	 Catch:{ all -> 0x006a }
        r8 = r4.getRelativeIndex(r8);	 Catch:{ all -> 0x006a }
        if (r7 != 0) goto L_0x0062;
    L_0x002c:
        r7 = r4.formats;	 Catch:{ all -> 0x006a }
        r7 = r7[r8];	 Catch:{ all -> 0x006a }
        if (r7 == r9) goto L_0x0033;
    L_0x0032:
        goto L_0x0062;
    L_0x0033:
        r5 = r6.isFlagsOnly();	 Catch:{ all -> 0x006a }
        if (r5 == 0) goto L_0x003b;
    L_0x0039:
        monitor-exit(r4);
        return r1;
    L_0x003b:
        r5 = r4.timesUs;	 Catch:{ all -> 0x006a }
        r0 = r5[r8];	 Catch:{ all -> 0x006a }
        r6.timeUs = r0;	 Catch:{ all -> 0x006a }
        r5 = r4.flags;	 Catch:{ all -> 0x006a }
        r5 = r5[r8];	 Catch:{ all -> 0x006a }
        r6.setFlags(r5);	 Catch:{ all -> 0x006a }
        r5 = r4.sizes;	 Catch:{ all -> 0x006a }
        r5 = r5[r8];	 Catch:{ all -> 0x006a }
        r10.size = r5;	 Catch:{ all -> 0x006a }
        r5 = r4.offsets;	 Catch:{ all -> 0x006a }
        r6 = r5[r8];	 Catch:{ all -> 0x006a }
        r10.offset = r6;	 Catch:{ all -> 0x006a }
        r5 = r4.cryptoDatas;	 Catch:{ all -> 0x006a }
        r5 = r5[r8];	 Catch:{ all -> 0x006a }
        r10.cryptoData = r5;	 Catch:{ all -> 0x006a }
        r5 = r4.readPosition;	 Catch:{ all -> 0x006a }
        r5 = r5 + 1;
        r4.readPosition = r5;	 Catch:{ all -> 0x006a }
        monitor-exit(r4);
        return r3;
    L_0x0062:
        r6 = r4.formats;	 Catch:{ all -> 0x006a }
        r6 = r6[r8];	 Catch:{ all -> 0x006a }
        r5.format = r6;	 Catch:{ all -> 0x006a }
        monitor-exit(r4);
        return r2;
    L_0x006a:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.read(com.google.android.exoplayer2.FormatHolder, com.google.android.exoplayer2.decoder.DecoderInputBuffer, boolean, boolean, com.google.android.exoplayer2.Format, com.google.android.exoplayer2.source.SampleMetadataQueue$SampleExtrasHolder):int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int advanceTo(long r9, boolean r11, boolean r12) {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = r8.readPosition;	 Catch:{ all -> 0x0039 }
        r2 = r8.getRelativeIndex(r0);	 Catch:{ all -> 0x0039 }
        r0 = r8.hasNextSample();	 Catch:{ all -> 0x0039 }
        r7 = -1;
        if (r0 == 0) goto L_0x0037;
    L_0x000e:
        r0 = r8.timesUs;	 Catch:{ all -> 0x0039 }
        r3 = r0[r2];	 Catch:{ all -> 0x0039 }
        r0 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r0 < 0) goto L_0x0037;
    L_0x0016:
        r0 = r8.largestQueuedTimestampUs;	 Catch:{ all -> 0x0039 }
        r3 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1));
        if (r3 <= 0) goto L_0x001f;
    L_0x001c:
        if (r12 != 0) goto L_0x001f;
    L_0x001e:
        goto L_0x0037;
    L_0x001f:
        r12 = r8.length;	 Catch:{ all -> 0x0039 }
        r0 = r8.readPosition;	 Catch:{ all -> 0x0039 }
        r3 = r12 - r0;
        r1 = r8;
        r4 = r9;
        r6 = r11;
        r9 = r1.findSampleBefore(r2, r3, r4, r6);	 Catch:{ all -> 0x0039 }
        if (r9 != r7) goto L_0x0030;
    L_0x002e:
        monitor-exit(r8);
        return r7;
    L_0x0030:
        r10 = r8.readPosition;	 Catch:{ all -> 0x0039 }
        r10 = r10 + r9;
        r8.readPosition = r10;	 Catch:{ all -> 0x0039 }
        monitor-exit(r8);
        return r9;
    L_0x0037:
        monitor-exit(r8);
        return r7;
    L_0x0039:
        r9 = move-exception;
        monitor-exit(r8);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.advanceTo(long, boolean, boolean):int");
    }

    public synchronized int advanceToEnd() {
        int i;
        i = this.length - this.readPosition;
        this.readPosition = this.length;
        return i;
    }

    public synchronized boolean setReadPosition(int i) {
        if (this.absoluteFirstIndex > i || i > this.absoluteFirstIndex + this.length) {
            return false;
        }
        this.readPosition = i - this.absoluteFirstIndex;
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized long discardTo(long r10, boolean r12, boolean r13) {
        /*
        r9 = this;
        monitor-enter(r9);
        r0 = r9.length;	 Catch:{ all -> 0x0038 }
        r1 = -1;
        if (r0 == 0) goto L_0x0036;
    L_0x0007:
        r0 = r9.timesUs;	 Catch:{ all -> 0x0038 }
        r3 = r9.relativeFirstIndex;	 Catch:{ all -> 0x0038 }
        r3 = r0[r3];	 Catch:{ all -> 0x0038 }
        r0 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1));
        if (r0 >= 0) goto L_0x0012;
    L_0x0011:
        goto L_0x0036;
    L_0x0012:
        if (r13 == 0) goto L_0x001f;
    L_0x0014:
        r13 = r9.readPosition;	 Catch:{ all -> 0x0038 }
        r0 = r9.length;	 Catch:{ all -> 0x0038 }
        if (r13 == r0) goto L_0x001f;
    L_0x001a:
        r13 = r9.readPosition;	 Catch:{ all -> 0x0038 }
        r13 = r13 + 1;
        goto L_0x0021;
    L_0x001f:
        r13 = r9.length;	 Catch:{ all -> 0x0038 }
    L_0x0021:
        r5 = r13;
        r4 = r9.relativeFirstIndex;	 Catch:{ all -> 0x0038 }
        r3 = r9;
        r6 = r10;
        r8 = r12;
        r10 = r3.findSampleBefore(r4, r5, r6, r8);	 Catch:{ all -> 0x0038 }
        r11 = -1;
        if (r10 != r11) goto L_0x0030;
    L_0x002e:
        monitor-exit(r9);
        return r1;
    L_0x0030:
        r10 = r9.discardSamples(r10);	 Catch:{ all -> 0x0038 }
        monitor-exit(r9);
        return r10;
    L_0x0036:
        monitor-exit(r9);
        return r1;
    L_0x0038:
        r10 = move-exception;
        monitor-exit(r9);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.discardTo(long, boolean, boolean):long");
    }

    public synchronized long discardToRead() {
        if (this.readPosition == 0) {
            return -1;
        }
        return discardSamples(this.readPosition);
    }

    public synchronized long discardToEnd() {
        if (this.length == 0) {
            return -1;
        }
        return discardSamples(this.length);
    }

    public synchronized boolean format(Format format) {
        if (format == null) {
            this.upstreamFormatRequired = true;
            return false;
        }
        this.upstreamFormatRequired = false;
        if (Util.areEqual(format, this.upstreamFormat)) {
            return false;
        }
        this.upstreamFormat = format;
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void commitSample(long r6, int r8, long r9, int r11, com.google.android.exoplayer2.extractor.TrackOutput.CryptoData r12) {
        /*
        r5 = this;
        monitor-enter(r5);
        r0 = r5.upstreamKeyframeRequired;	 Catch:{ all -> 0x00cf }
        r1 = 0;
        if (r0 == 0) goto L_0x000e;
    L_0x0006:
        r0 = r8 & 1;
        if (r0 != 0) goto L_0x000c;
    L_0x000a:
        monitor-exit(r5);
        return;
    L_0x000c:
        r5.upstreamKeyframeRequired = r1;	 Catch:{ all -> 0x00cf }
    L_0x000e:
        r0 = r5.upstreamFormatRequired;	 Catch:{ all -> 0x00cf }
        r0 = r0 ^ 1;
        com.google.android.exoplayer2.util.Assertions.checkState(r0);	 Catch:{ all -> 0x00cf }
        r5.commitSampleTimestamp(r6);	 Catch:{ all -> 0x00cf }
        r0 = r5.length;	 Catch:{ all -> 0x00cf }
        r0 = r5.getRelativeIndex(r0);	 Catch:{ all -> 0x00cf }
        r2 = r5.timesUs;	 Catch:{ all -> 0x00cf }
        r2[r0] = r6;	 Catch:{ all -> 0x00cf }
        r6 = r5.offsets;	 Catch:{ all -> 0x00cf }
        r6[r0] = r9;	 Catch:{ all -> 0x00cf }
        r6 = r5.sizes;	 Catch:{ all -> 0x00cf }
        r6[r0] = r11;	 Catch:{ all -> 0x00cf }
        r6 = r5.flags;	 Catch:{ all -> 0x00cf }
        r6[r0] = r8;	 Catch:{ all -> 0x00cf }
        r6 = r5.cryptoDatas;	 Catch:{ all -> 0x00cf }
        r6[r0] = r12;	 Catch:{ all -> 0x00cf }
        r6 = r5.formats;	 Catch:{ all -> 0x00cf }
        r7 = r5.upstreamFormat;	 Catch:{ all -> 0x00cf }
        r6[r0] = r7;	 Catch:{ all -> 0x00cf }
        r6 = r5.sourceIds;	 Catch:{ all -> 0x00cf }
        r7 = r5.upstreamSourceId;	 Catch:{ all -> 0x00cf }
        r6[r0] = r7;	 Catch:{ all -> 0x00cf }
        r6 = r5.length;	 Catch:{ all -> 0x00cf }
        r6 = r6 + 1;
        r5.length = r6;	 Catch:{ all -> 0x00cf }
        r6 = r5.length;	 Catch:{ all -> 0x00cf }
        r7 = r5.capacity;	 Catch:{ all -> 0x00cf }
        if (r6 != r7) goto L_0x00cd;
    L_0x004a:
        r6 = r5.capacity;	 Catch:{ all -> 0x00cf }
        r6 = r6 + 1000;
        r7 = new int[r6];	 Catch:{ all -> 0x00cf }
        r8 = new long[r6];	 Catch:{ all -> 0x00cf }
        r9 = new long[r6];	 Catch:{ all -> 0x00cf }
        r10 = new int[r6];	 Catch:{ all -> 0x00cf }
        r11 = new int[r6];	 Catch:{ all -> 0x00cf }
        r12 = new com.google.android.exoplayer2.extractor.TrackOutput.CryptoData[r6];	 Catch:{ all -> 0x00cf }
        r0 = new com.google.android.exoplayer2.Format[r6];	 Catch:{ all -> 0x00cf }
        r2 = r5.capacity;	 Catch:{ all -> 0x00cf }
        r3 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00cf }
        r2 = r2 - r3;
        r3 = r5.offsets;	 Catch:{ all -> 0x00cf }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r3, r4, r8, r1, r2);	 Catch:{ all -> 0x00cf }
        r3 = r5.timesUs;	 Catch:{ all -> 0x00cf }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r3, r4, r9, r1, r2);	 Catch:{ all -> 0x00cf }
        r3 = r5.flags;	 Catch:{ all -> 0x00cf }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r3, r4, r10, r1, r2);	 Catch:{ all -> 0x00cf }
        r3 = r5.sizes;	 Catch:{ all -> 0x00cf }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r3, r4, r11, r1, r2);	 Catch:{ all -> 0x00cf }
        r3 = r5.cryptoDatas;	 Catch:{ all -> 0x00cf }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r3, r4, r12, r1, r2);	 Catch:{ all -> 0x00cf }
        r3 = r5.formats;	 Catch:{ all -> 0x00cf }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r3, r4, r0, r1, r2);	 Catch:{ all -> 0x00cf }
        r3 = r5.sourceIds;	 Catch:{ all -> 0x00cf }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r3, r4, r7, r1, r2);	 Catch:{ all -> 0x00cf }
        r3 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00cf }
        r4 = r5.offsets;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r4, r1, r8, r2, r3);	 Catch:{ all -> 0x00cf }
        r4 = r5.timesUs;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r4, r1, r9, r2, r3);	 Catch:{ all -> 0x00cf }
        r4 = r5.flags;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r4, r1, r10, r2, r3);	 Catch:{ all -> 0x00cf }
        r4 = r5.sizes;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r4, r1, r11, r2, r3);	 Catch:{ all -> 0x00cf }
        r4 = r5.cryptoDatas;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r4, r1, r12, r2, r3);	 Catch:{ all -> 0x00cf }
        r4 = r5.formats;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r4, r1, r0, r2, r3);	 Catch:{ all -> 0x00cf }
        r4 = r5.sourceIds;	 Catch:{ all -> 0x00cf }
        java.lang.System.arraycopy(r4, r1, r7, r2, r3);	 Catch:{ all -> 0x00cf }
        r5.offsets = r8;	 Catch:{ all -> 0x00cf }
        r5.timesUs = r9;	 Catch:{ all -> 0x00cf }
        r5.flags = r10;	 Catch:{ all -> 0x00cf }
        r5.sizes = r11;	 Catch:{ all -> 0x00cf }
        r5.cryptoDatas = r12;	 Catch:{ all -> 0x00cf }
        r5.formats = r0;	 Catch:{ all -> 0x00cf }
        r5.sourceIds = r7;	 Catch:{ all -> 0x00cf }
        r5.relativeFirstIndex = r1;	 Catch:{ all -> 0x00cf }
        r7 = r5.capacity;	 Catch:{ all -> 0x00cf }
        r5.length = r7;	 Catch:{ all -> 0x00cf }
        r5.capacity = r6;	 Catch:{ all -> 0x00cf }
    L_0x00cd:
        monitor-exit(r5);
        return;
    L_0x00cf:
        r6 = move-exception;
        monitor-exit(r5);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.commitSample(long, int, long, int, com.google.android.exoplayer2.extractor.TrackOutput$CryptoData):void");
    }

    public synchronized void commitSampleTimestamp(long j) {
        this.largestQueuedTimestampUs = Math.max(this.largestQueuedTimestampUs, j);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean attemptSplice(long r8) {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = r7.length;	 Catch:{ all -> 0x004a }
        r1 = 0;
        r2 = 1;
        if (r0 != 0) goto L_0x0010;
    L_0x0007:
        r3 = r7.largestDiscardedTimestampUs;	 Catch:{ all -> 0x004a }
        r0 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1));
        if (r0 <= 0) goto L_0x000e;
    L_0x000d:
        r1 = 1;
    L_0x000e:
        monitor-exit(r7);
        return r1;
    L_0x0010:
        r3 = r7.largestDiscardedTimestampUs;	 Catch:{ all -> 0x004a }
        r0 = r7.readPosition;	 Catch:{ all -> 0x004a }
        r5 = r7.getLargestTimestamp(r0);	 Catch:{ all -> 0x004a }
        r3 = java.lang.Math.max(r3, r5);	 Catch:{ all -> 0x004a }
        r0 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));
        if (r0 < 0) goto L_0x0022;
    L_0x0020:
        monitor-exit(r7);
        return r1;
    L_0x0022:
        r0 = r7.length;	 Catch:{ all -> 0x004a }
        r1 = r7.length;	 Catch:{ all -> 0x004a }
        r1 = r1 - r2;
        r1 = r7.getRelativeIndex(r1);	 Catch:{ all -> 0x004a }
    L_0x002b:
        r3 = r7.readPosition;	 Catch:{ all -> 0x004a }
        if (r0 <= r3) goto L_0x0042;
    L_0x002f:
        r3 = r7.timesUs;	 Catch:{ all -> 0x004a }
        r4 = r3[r1];	 Catch:{ all -> 0x004a }
        r3 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r3 < 0) goto L_0x0042;
    L_0x0037:
        r0 = r0 + -1;
        r1 = r1 + -1;
        r3 = -1;
        if (r1 != r3) goto L_0x002b;
    L_0x003e:
        r1 = r7.capacity;	 Catch:{ all -> 0x004a }
        r1 = r1 - r2;
        goto L_0x002b;
    L_0x0042:
        r8 = r7.absoluteFirstIndex;	 Catch:{ all -> 0x004a }
        r8 = r8 + r0;
        r7.discardUpstreamSamples(r8);	 Catch:{ all -> 0x004a }
        monitor-exit(r7);
        return r2;
    L_0x004a:
        r8 = move-exception;
        monitor-exit(r7);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.attemptSplice(long):boolean");
    }

    private int findSampleBefore(int i, int i2, long j, boolean z) {
        int i3 = i;
        int i4 = -1;
        for (i = 0; i < i2 && this.timesUs[i3] <= j; i++) {
            if (!(z && (this.flags[i3] & 1) == 0)) {
                i4 = i;
            }
            i3++;
            if (i3 == this.capacity) {
                i3 = 0;
            }
        }
        return i4;
    }

    private long discardSamples(int i) {
        this.largestDiscardedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(i));
        this.length -= i;
        this.absoluteFirstIndex += i;
        this.relativeFirstIndex += i;
        if (this.relativeFirstIndex >= this.capacity) {
            this.relativeFirstIndex -= this.capacity;
        }
        this.readPosition -= i;
        if (this.readPosition < 0) {
            this.readPosition = 0;
        }
        if (this.length != 0) {
            return this.offsets[this.relativeFirstIndex];
        }
        i = (this.relativeFirstIndex == 0 ? this.capacity : this.relativeFirstIndex) - 1;
        return this.offsets[i] + ((long) this.sizes[i]);
    }

    private long getLargestTimestamp(int i) {
        long j = Long.MIN_VALUE;
        if (i == 0) {
            return Long.MIN_VALUE;
        }
        int relativeIndex = getRelativeIndex(i - 1);
        for (int i2 = 0; i2 < i; i2++) {
            j = Math.max(j, this.timesUs[relativeIndex]);
            if ((this.flags[relativeIndex] & 1) != 0) {
                break;
            }
            relativeIndex--;
            if (relativeIndex == -1) {
                relativeIndex = this.capacity - 1;
            }
        }
        return j;
    }

    private int getRelativeIndex(int i) {
        int i2 = this.relativeFirstIndex + i;
        return i2 < this.capacity ? i2 : i2 - this.capacity;
    }
}
