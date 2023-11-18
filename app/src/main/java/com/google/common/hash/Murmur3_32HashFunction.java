package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

final class Murmur3_32HashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static final long serialVersionUID = 0;
    private final int seed;

    private static final class Murmur3_32Hasher extends AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 4;
        private int h1;
        private int length = 0;

        Murmur3_32Hasher(int i) {
            super(4);
            this.h1 = i;
        }

        protected void process(ByteBuffer byteBuffer) {
            this.h1 = Murmur3_32HashFunction.mixH1(this.h1, Murmur3_32HashFunction.mixK1(byteBuffer.getInt()));
            this.length += 4;
        }

        protected void processRemaining(ByteBuffer byteBuffer) {
            this.length += byteBuffer.remaining();
            int i = 0;
            int i2 = 0;
            while (byteBuffer.hasRemaining()) {
                i ^= UnsignedBytes.toInt(byteBuffer.get()) << i2;
                i2 += 8;
            }
            this.h1 ^= Murmur3_32HashFunction.mixK1(i);
        }

        public HashCode makeHash() {
            return Murmur3_32HashFunction.fmix(this.h1, this.length);
        }
    }

    public int bits() {
        return 32;
    }

    Murmur3_32HashFunction(int i) {
        this.seed = i;
    }

    public Hasher newHasher() {
        return new Murmur3_32Hasher(this.seed);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hashing.murmur3_32(");
        stringBuilder.append(this.seed);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof Murmur3_32HashFunction)) {
            return false;
        }
        if (this.seed == ((Murmur3_32HashFunction) obj).seed) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return getClass().hashCode() ^ this.seed;
    }

    public HashCode hashInt(int i) {
        return fmix(mixH1(this.seed, mixK1(i)), 4);
    }

    public HashCode hashLong(long j) {
        int i = (int) j;
        j = (int) (j >>> 32);
        return fmix(mixH1(mixH1(this.seed, mixK1(i)), mixK1(j)), 8);
    }

    public HashCode hashUnencodedChars(CharSequence charSequence) {
        int i = this.seed;
        for (int i2 = 1; i2 < charSequence.length(); i2 += 2) {
            i = mixH1(i, mixK1(charSequence.charAt(i2 - 1) | (charSequence.charAt(i2) << 16)));
        }
        if ((charSequence.length() & 1) == 1) {
            i ^= mixK1(charSequence.charAt(charSequence.length() - 1));
        }
        return fmix(i, charSequence.length() * 2);
    }

    private static int mixK1(int i) {
        return Integer.rotateLeft(i * C1, 15) * C2;
    }

    private static int mixH1(int i, int i2) {
        return (Integer.rotateLeft(i ^ i2, 13) * 5) - 430675100;
    }

    private static HashCode fmix(int i, int i2) {
        i ^= i2;
        i = (i ^ (i >>> 16)) * -2048144789;
        i = (i ^ (i >>> 13)) * -1028477387;
        return HashCode.fromInt(i ^ (i >>> 16));
    }
}
