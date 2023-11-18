package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInts;
import java.io.Serializable;
import java.security.MessageDigest;
import javax.annotation.Nullable;

@Beta
public abstract class HashCode {
    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    private static final class BytesHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final byte[] bytes;

        BytesHashCode(byte[] bArr) {
            this.bytes = (byte[]) Preconditions.checkNotNull(bArr);
        }

        public int bits() {
            return this.bytes.length * 8;
        }

        public byte[] asBytes() {
            return (byte[]) this.bytes.clone();
        }

        public int asInt() {
            Preconditions.checkState(this.bytes.length >= 4, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", Integer.valueOf(this.bytes.length));
            return (((this.bytes[0] & 255) | ((this.bytes[1] & 255) << 8)) | ((this.bytes[2] & 255) << 16)) | ((this.bytes[3] & 255) << 24);
        }

        public long asLong() {
            Preconditions.checkState(this.bytes.length >= 8, "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", Integer.valueOf(this.bytes.length));
            return padToLong();
        }

        public long padToLong() {
            long j = (long) (this.bytes[0] & 255);
            int i = 1;
            while (i < Math.min(this.bytes.length, 8)) {
                i++;
                j |= (((long) this.bytes[i]) & 255) << (i * 8);
            }
            return j;
        }

        void writeBytesToImpl(byte[] bArr, int i, int i2) {
            System.arraycopy(this.bytes, 0, bArr, i, i2);
        }

        byte[] getBytesInternal() {
            return this.bytes;
        }

        boolean equalsSameBits(HashCode hashCode) {
            return MessageDigest.isEqual(this.bytes, hashCode.getBytesInternal());
        }
    }

    private static final class IntHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final int hash;

        public int bits() {
            return 32;
        }

        IntHashCode(int i) {
            this.hash = i;
        }

        public byte[] asBytes() {
            return new byte[]{(byte) this.hash, (byte) (this.hash >> 8), (byte) (this.hash >> 16), (byte) (this.hash >> 24)};
        }

        public int asInt() {
            return this.hash;
        }

        public long asLong() {
            throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
        }

        public long padToLong() {
            return UnsignedInts.toLong(this.hash);
        }

        void writeBytesToImpl(byte[] bArr, int i, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                bArr[i + i3] = (byte) (this.hash >> (i3 * 8));
            }
        }

        boolean equalsSameBits(HashCode hashCode) {
            return this.hash == hashCode.asInt() ? true : null;
        }
    }

    private static final class LongHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final long hash;

        public int bits() {
            return 64;
        }

        LongHashCode(long j) {
            this.hash = j;
        }

        public byte[] asBytes() {
            return new byte[]{(byte) ((int) this.hash), (byte) ((int) (this.hash >> 8)), (byte) ((int) (this.hash >> 16)), (byte) ((int) (this.hash >> 24)), (byte) ((int) (this.hash >> 32)), (byte) ((int) (this.hash >> 40)), (byte) ((int) (this.hash >> 48)), (byte) ((int) (this.hash >> 56))};
        }

        public int asInt() {
            return (int) this.hash;
        }

        public long asLong() {
            return this.hash;
        }

        public long padToLong() {
            return this.hash;
        }

        void writeBytesToImpl(byte[] bArr, int i, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                bArr[i + i3] = (byte) ((int) (this.hash >> (i3 * 8)));
            }
        }

        boolean equalsSameBits(HashCode hashCode) {
            return this.hash == hashCode.asLong() ? true : null;
        }
    }

    public abstract byte[] asBytes();

    public abstract int asInt();

    public abstract long asLong();

    public abstract int bits();

    abstract boolean equalsSameBits(HashCode hashCode);

    public abstract long padToLong();

    abstract void writeBytesToImpl(byte[] bArr, int i, int i2);

    HashCode() {
    }

    public int writeBytesTo(byte[] bArr, int i, int i2) {
        i2 = Ints.min(i2, bits() / 8);
        Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
        writeBytesToImpl(bArr, i, i2);
        return i2;
    }

    byte[] getBytesInternal() {
        return asBytes();
    }

    public static HashCode fromInt(int i) {
        return new IntHashCode(i);
    }

    public static HashCode fromLong(long j) {
        return new LongHashCode(j);
    }

    public static HashCode fromBytes(byte[] bArr) {
        boolean z = true;
        if (bArr.length < 1) {
            z = false;
        }
        Preconditions.checkArgument(z, "A HashCode must contain at least 1 byte.");
        return fromBytesNoCopy((byte[]) bArr.clone());
    }

    static HashCode fromBytesNoCopy(byte[] bArr) {
        return new BytesHashCode(bArr);
    }

    public static HashCode fromString(String str) {
        int i = 0;
        Preconditions.checkArgument(str.length() >= 2, "input string (%s) must have at least 2 characters", str);
        Preconditions.checkArgument(str.length() % 2 == 0, "input string (%s) must have an even number of characters", str);
        byte[] bArr = new byte[(str.length() / 2)];
        while (i < str.length()) {
            bArr[i / 2] = (byte) ((decode(str.charAt(i)) << 4) + decode(str.charAt(i + 1)));
            i += 2;
        }
        return fromBytesNoCopy(bArr);
    }

    private static int decode(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return (c - 'a') + 10;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal hexadecimal character: ");
        stringBuilder.append(c);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof HashCode)) {
            return false;
        }
        HashCode hashCode = (HashCode) obj;
        if (bits() == hashCode.bits() && equalsSameBits(hashCode) != null) {
            z = true;
        }
        return z;
    }

    public final int hashCode() {
        if (bits() >= 32) {
            return asInt();
        }
        byte[] asBytes = asBytes();
        int i = asBytes[0] & 255;
        for (int i2 = 1; i2 < asBytes.length; i2++) {
            i |= (asBytes[i2] & 255) << (i2 * 8);
        }
        return i;
    }

    public final String toString() {
        byte[] asBytes = asBytes();
        StringBuilder stringBuilder = new StringBuilder(asBytes.length * 2);
        for (byte b : asBytes) {
            stringBuilder.append(hexDigits[(b >> 4) & 15]);
            stringBuilder.append(hexDigits[b & 15]);
        }
        return stringBuilder.toString();
    }
}
