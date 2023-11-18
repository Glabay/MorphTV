package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

final class SipHashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    /* renamed from: c */
    private final int f54c;
    /* renamed from: d */
    private final int f55d;
    private final long k0;
    private final long k1;

    private static final class SipHasher extends AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 8;
        /* renamed from: b */
        private long f51b = 0;
        /* renamed from: c */
        private final int f52c;
        /* renamed from: d */
        private final int f53d;
        private long finalM = 0;
        private long v0 = 8317987319222330741L;
        private long v1 = 7237128888997146477L;
        private long v2 = 7816392313619706465L;
        private long v3 = 8387220255154660723L;

        SipHasher(int i, int i2, long j, long j2) {
            super(8);
            this.f52c = i;
            this.f53d = i2;
            this.v0 ^= j;
            this.v1 ^= j2;
            this.v2 ^= j;
            this.v3 ^= j2;
        }

        protected void process(ByteBuffer byteBuffer) {
            this.f51b += 8;
            processM(byteBuffer.getLong());
        }

        protected void processRemaining(ByteBuffer byteBuffer) {
            this.f51b += (long) byteBuffer.remaining();
            long j = null;
            while (byteBuffer.hasRemaining()) {
                this.finalM ^= (((long) byteBuffer.get()) & 255) << j;
                j += 8;
            }
        }

        public HashCode makeHash() {
            this.finalM ^= this.f51b << 56;
            processM(this.finalM);
            this.v2 ^= 255;
            sipRound(this.f53d);
            return HashCode.fromLong(((this.v0 ^ this.v1) ^ this.v2) ^ this.v3);
        }

        private void processM(long j) {
            this.v3 ^= j;
            sipRound(this.f52c);
            this.v0 ^= j;
        }

        private void sipRound(int i) {
            for (int i2 = 0; i2 < i; i2++) {
                this.v0 += this.v1;
                this.v2 += this.v3;
                this.v1 = Long.rotateLeft(this.v1, 13);
                this.v3 = Long.rotateLeft(this.v3, 16);
                this.v1 ^= this.v0;
                this.v3 ^= this.v2;
                this.v0 = Long.rotateLeft(this.v0, 32);
                this.v2 += this.v1;
                this.v0 += this.v3;
                this.v1 = Long.rotateLeft(this.v1, 17);
                this.v3 = Long.rotateLeft(this.v3, 21);
                this.v1 ^= this.v2;
                this.v3 ^= this.v0;
                this.v2 = Long.rotateLeft(this.v2, 32);
            }
        }
    }

    public int bits() {
        return 64;
    }

    SipHashFunction(int i, int i2, long j, long j2) {
        Preconditions.checkArgument(i > 0, "The number of SipRound iterations (c=%s) during Compression must be positive.", Integer.valueOf(i));
        Preconditions.checkArgument(i2 > 0, "The number of SipRound iterations (d=%s) during Finalization must be positive.", Integer.valueOf(i2));
        this.f54c = i;
        this.f55d = i2;
        this.k0 = j;
        this.k1 = j2;
    }

    public Hasher newHasher() {
        return new SipHasher(this.f54c, this.f55d, this.k0, this.k1);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hashing.sipHash");
        stringBuilder.append(this.f54c);
        stringBuilder.append("");
        stringBuilder.append(this.f55d);
        stringBuilder.append("(");
        stringBuilder.append(this.k0);
        stringBuilder.append(", ");
        stringBuilder.append(this.k1);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof SipHashFunction)) {
            return false;
        }
        SipHashFunction sipHashFunction = (SipHashFunction) obj;
        if (this.f54c == sipHashFunction.f54c && this.f55d == sipHashFunction.f55d && this.k0 == sipHashFunction.k0 && this.k1 == sipHashFunction.k1) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return (int) ((((long) ((getClass().hashCode() ^ this.f54c) ^ this.f55d)) ^ this.k0) ^ this.k1);
    }
}
