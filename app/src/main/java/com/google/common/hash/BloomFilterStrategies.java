package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.math.RoundingMode;
import java.util.Arrays;

enum BloomFilterStrategies implements Strategy {
    MURMUR128_MITZ_32 {
        public <T> boolean put(T t, Funnel<? super T> funnel, int i, BitArray bitArray) {
            long bitSize = bitArray.bitSize();
            t = Hashing.murmur3_128().hashObject(t, funnel).asLong();
            int i2 = (int) t;
            t = (int) (t >>> 32);
            boolean z = false;
            for (funnel = true; funnel <= i; funnel++) {
                int i3 = (funnel * t) + i2;
                if (i3 < 0) {
                    i3 ^= -1;
                }
                z |= bitArray.set(((long) i3) % bitSize);
            }
            return z;
        }

        public <T> boolean mightContain(T t, Funnel<? super T> funnel, int i, BitArray bitArray) {
            long bitSize = bitArray.bitSize();
            t = Hashing.murmur3_128().hashObject(t, funnel).asLong();
            int i2 = (int) t;
            t = (int) (t >>> 32);
            for (int i3 = 1; i3 <= i; i3++) {
                int i4 = (i3 * t) + i2;
                if (i4 < 0) {
                    i4 ^= -1;
                }
                if (!bitArray.get(((long) i4) % bitSize)) {
                    return null;
                }
            }
            return true;
        }
    },
    MURMUR128_MITZ_64 {
        public <T> boolean put(T t, Funnel<? super T> funnel, int i, BitArray bitArray) {
            long bitSize = bitArray.bitSize();
            t = Hashing.murmur3_128().hashObject(t, funnel).getBytesInternal();
            long lowerEight = lowerEight(t);
            t = upperEight(t);
            int i2 = 0;
            long j = lowerEight;
            boolean z = false;
            while (i2 < i) {
                z |= bitArray.set((j & Long.MAX_VALUE) % bitSize);
                i2++;
                j += t;
            }
            return z;
        }

        public <T> boolean mightContain(T t, Funnel<? super T> funnel, int i, BitArray bitArray) {
            long bitSize = bitArray.bitSize();
            t = Hashing.murmur3_128().hashObject(t, funnel).getBytesInternal();
            long lowerEight = lowerEight(t);
            t = upperEight(t);
            long j = lowerEight;
            int i2 = 0;
            while (i2 < i) {
                if (!bitArray.get((j & Long.MAX_VALUE) % bitSize)) {
                    return false;
                }
                i2++;
                j += t;
            }
            return true;
        }

        private long lowerEight(byte[] bArr) {
            return Longs.fromBytes(bArr[7], bArr[6], bArr[5], bArr[4], bArr[3], bArr[2], bArr[1], bArr[0]);
        }

        private long upperEight(byte[] bArr) {
            return Longs.fromBytes(bArr[15], bArr[14], bArr[13], bArr[12], bArr[11], bArr[10], bArr[9], bArr[8]);
        }
    };

    static final class BitArray {
        long bitCount;
        final long[] data;

        BitArray(long j) {
            this(new long[Ints.checkedCast(LongMath.divide(j, 64, RoundingMode.CEILING))]);
        }

        BitArray(long[] jArr) {
            int i = 0;
            Preconditions.checkArgument(jArr.length > 0, "data length is zero!");
            this.data = jArr;
            long j = 0;
            while (i < jArr.length) {
                i++;
                j += (long) Long.bitCount(jArr[i]);
            }
            this.bitCount = j;
        }

        boolean set(long j) {
            if (get(j)) {
                return 0;
            }
            long[] jArr = this.data;
            int i = (int) (j >>> 6);
            jArr[i] = jArr[i] | (1 << ((int) j));
            this.bitCount++;
            return 1;
        }

        boolean get(long j) {
            return (this.data[(int) (j >>> 6)] & (1 << ((int) j))) != 0 ? 1 : 0;
        }

        long bitSize() {
            return ((long) this.data.length) * 64;
        }

        long bitCount() {
            return this.bitCount;
        }

        BitArray copy() {
            return new BitArray((long[]) this.data.clone());
        }

        void putAll(BitArray bitArray) {
            int i = 0;
            Preconditions.checkArgument(this.data.length == bitArray.data.length, "BitArrays must be of equal length (%s != %s)", Integer.valueOf(this.data.length), Integer.valueOf(bitArray.data.length));
            this.bitCount = 0;
            while (i < this.data.length) {
                long[] jArr = this.data;
                jArr[i] = jArr[i] | bitArray.data[i];
                this.bitCount += (long) Long.bitCount(this.data[i]);
                i++;
            }
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof BitArray)) {
                return null;
            }
            return Arrays.equals(this.data, ((BitArray) obj).data);
        }

        public int hashCode() {
            return Arrays.hashCode(this.data);
        }
    }
}
