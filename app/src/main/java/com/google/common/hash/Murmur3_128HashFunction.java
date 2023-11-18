package com.google.common.hash;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.annotation.Nullable;

final class Murmur3_128HashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    private final int seed;

    private static final class Murmur3_128Hasher extends AbstractStreamingHasher {
        private static final long C1 = -8663945395140668459L;
        private static final long C2 = 5545529020109919103L;
        private static final int CHUNK_SIZE = 16;
        private long h1;
        private long h2;
        private int length = 0;

        private static long fmix64(long j) {
            long j2 = (j ^ (j >>> 33)) * -49064778989728563L;
            long j3 = (j2 ^ (j2 >>> 33)) * -4265267296055464877L;
            return j3 ^ (j3 >>> 33);
        }

        Murmur3_128Hasher(int i) {
            super(16);
            long j = (long) i;
            this.h1 = j;
            this.h2 = j;
        }

        protected void process(ByteBuffer byteBuffer) {
            bmix64(byteBuffer.getLong(), byteBuffer.getLong());
            this.length += 16;
        }

        private void bmix64(long j, long j2) {
            this.h1 ^= mixK1(j);
            this.h1 = Long.rotateLeft(this.h1, 27);
            this.h1 += this.h2;
            this.h1 = (this.h1 * 5) + 1390208809;
            this.h2 ^= mixK2(j2);
            this.h2 = Long.rotateLeft(this.h2, 31);
            this.h2 += this.h1;
            this.h2 = (this.h2 * 5) + 944331445;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void processRemaining(java.nio.ByteBuffer r14) {
            /*
            r13 = this;
            r0 = r13.length;
            r1 = r14.remaining();
            r0 = r0 + r1;
            r13.length = r0;
            r0 = r14.remaining();
            r1 = 16;
            r2 = 24;
            r3 = 32;
            r4 = 40;
            r5 = 48;
            r6 = 0;
            r8 = 8;
            switch(r0) {
                case 1: goto L_0x00fe;
                case 2: goto L_0x00ef;
                case 3: goto L_0x00df;
                case 4: goto L_0x00cf;
                case 5: goto L_0x00bf;
                case 6: goto L_0x00af;
                case 7: goto L_0x00a1;
                case 8: goto L_0x0096;
                case 9: goto L_0x0089;
                case 10: goto L_0x0079;
                case 11: goto L_0x0068;
                case 12: goto L_0x0057;
                case 13: goto L_0x0046;
                case 14: goto L_0x0035;
                case 15: goto L_0x0026;
                default: goto L_0x001e;
            };
        L_0x001e:
            r14 = new java.lang.AssertionError;
            r0 = "Should never get here.";
            r14.<init>(r0);
            throw r14;
        L_0x0026:
            r0 = 14;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r9 = (long) r0;
            r9 = r9 << r5;
            r11 = r9 ^ r6;
            goto L_0x0036;
        L_0x0035:
            r11 = r6;
        L_0x0036:
            r0 = 13;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r9 = (long) r0;
            r4 = r9 << r4;
            r9 = r11 ^ r4;
            goto L_0x0047;
        L_0x0046:
            r9 = r6;
        L_0x0047:
            r0 = 12;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r4 = (long) r0;
            r3 = r4 << r3;
            r11 = r9 ^ r3;
            goto L_0x0058;
        L_0x0057:
            r11 = r6;
        L_0x0058:
            r0 = 11;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r3 = (long) r0;
            r2 = r3 << r2;
            r4 = r11 ^ r2;
            goto L_0x0069;
        L_0x0068:
            r4 = r6;
        L_0x0069:
            r0 = 10;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r2 = (long) r0;
            r0 = r2 << r1;
            r2 = r4 ^ r0;
            goto L_0x007a;
        L_0x0079:
            r2 = r6;
        L_0x007a:
            r0 = 9;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r0 = (long) r0;
            r0 = r0 << r8;
            r4 = r2 ^ r0;
            goto L_0x008a;
        L_0x0089:
            r4 = r6;
        L_0x008a:
            r0 = r14.get(r8);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r0 = (long) r0;
            r2 = r4 ^ r0;
            goto L_0x0097;
        L_0x0096:
            r2 = r6;
        L_0x0097:
            r0 = r14.getLong();
            r4 = r6 ^ r0;
            r6 = r2;
            r2 = r4;
            goto L_0x010b;
        L_0x00a1:
            r0 = 6;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r9 = (long) r0;
            r9 = r9 << r5;
            r11 = r9 ^ r6;
            goto L_0x00b0;
        L_0x00af:
            r11 = r6;
        L_0x00b0:
            r0 = 5;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r9 = (long) r0;
            r4 = r9 << r4;
            r9 = r11 ^ r4;
            goto L_0x00c0;
        L_0x00bf:
            r9 = r6;
        L_0x00c0:
            r0 = 4;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r4 = (long) r0;
            r3 = r4 << r3;
            r11 = r9 ^ r3;
            goto L_0x00d0;
        L_0x00cf:
            r11 = r6;
        L_0x00d0:
            r0 = 3;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r3 = (long) r0;
            r2 = r3 << r2;
            r4 = r11 ^ r2;
            goto L_0x00e0;
        L_0x00df:
            r4 = r6;
        L_0x00e0:
            r0 = 2;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r2 = (long) r0;
            r0 = r2 << r1;
            r2 = r4 ^ r0;
            goto L_0x00f0;
        L_0x00ef:
            r2 = r6;
        L_0x00f0:
            r0 = 1;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r0 = (long) r0;
            r0 = r0 << r8;
            r4 = r2 ^ r0;
            goto L_0x00ff;
        L_0x00fe:
            r4 = r6;
        L_0x00ff:
            r0 = 0;
            r14 = r14.get(r0);
            r14 = com.google.common.primitives.UnsignedBytes.toInt(r14);
            r0 = (long) r14;
            r2 = r4 ^ r0;
        L_0x010b:
            r0 = r13.h1;
            r2 = mixK1(r2);
            r4 = r0 ^ r2;
            r13.h1 = r4;
            r0 = r13.h2;
            r2 = mixK2(r6);
            r4 = r0 ^ r2;
            r13.h2 = r4;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.hash.Murmur3_128HashFunction.Murmur3_128Hasher.processRemaining(java.nio.ByteBuffer):void");
        }

        public HashCode makeHash() {
            this.h1 ^= (long) this.length;
            this.h2 ^= (long) this.length;
            this.h1 += this.h2;
            this.h2 += this.h1;
            this.h1 = fmix64(this.h1);
            this.h2 = fmix64(this.h2);
            this.h1 += this.h2;
            this.h2 += this.h1;
            return HashCode.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.h1).putLong(this.h2).array());
        }

        private static long mixK1(long j) {
            return Long.rotateLeft(j * C1, 31) * C2;
        }

        private static long mixK2(long j) {
            return Long.rotateLeft(j * C2, 33) * C1;
        }
    }

    public int bits() {
        return 128;
    }

    Murmur3_128HashFunction(int i) {
        this.seed = i;
    }

    public Hasher newHasher() {
        return new Murmur3_128Hasher(this.seed);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hashing.murmur3_128(");
        stringBuilder.append(this.seed);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof Murmur3_128HashFunction)) {
            return false;
        }
        if (this.seed == ((Murmur3_128HashFunction) obj).seed) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return getClass().hashCode() ^ this.seed;
    }
}
