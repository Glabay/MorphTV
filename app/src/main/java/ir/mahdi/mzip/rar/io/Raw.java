package ir.mahdi.mzip.rar.io;

import android.support.v4.view.MotionEventCompat;

public class Raw {
    public static final short readShortBigEndian(byte[] bArr, int i) {
        return (short) ((bArr[i + 1] & 255) | ((short) (((short) ((bArr[i] & 255) | 0)) << 8)));
    }

    public static final int readIntBigEndian(byte[] bArr, int i) {
        return (bArr[i + 3] & 255) | (((((((bArr[i] & 255) | 0) << 8) | (bArr[i + 1] & 255)) << 8) | (bArr[i + 2] & 255)) << 8);
    }

    public static final long readLongBigEndian(byte[] bArr, int i) {
        return (long) ((bArr[i + 7] & 255) | (((((((((((((((bArr[i] & 255) | 0) << 8) | (bArr[i + 1] & 255)) << 8) | (bArr[i + 2] & 255)) << 8) | (bArr[i + 3] & 255)) << 8) | (bArr[i + 4] & 255)) << 8) | (bArr[i + 5] & 255)) << 8) | (bArr[i + 6] & 255)) << 8));
    }

    public static final short readShortLittleEndian(byte[] bArr, int i) {
        return (short) (((short) (((short) ((bArr[i + 1] & 255) + 0)) << 8)) + (bArr[i] & 255));
    }

    public static final int readIntLittleEndian(byte[] bArr, int i) {
        return (bArr[i] & 255) | ((((bArr[i + 3] & 255) << 24) | ((bArr[i + 2] & 255) << 16)) | ((bArr[i + 1] & 255) << 8));
    }

    public static final long readIntLittleEndianAsLong(byte[] bArr, int i) {
        return ((((((long) bArr[i + 3]) & 255) << 24) | ((((long) bArr[i + 2]) & 255) << 16)) | ((((long) bArr[i + 1]) & 255) << 8)) | (((long) bArr[i]) & 255);
    }

    public static final long readLongLittleEndian(byte[] bArr, int i) {
        return (long) (bArr[i] | (((((((((((((((bArr[i + 7] & 255) | 0) << 8) | (bArr[i + 6] & 255)) << 8) | (bArr[i + 5] & 255)) << 8) | (bArr[i + 4] & 255)) << 8) | (bArr[i + 3] & 255)) << 8) | (bArr[i + 2] & 255)) << 8) | (bArr[i + 1] & 255)) << 8));
    }

    public static final void writeShortBigEndian(byte[] bArr, int i, short s) {
        bArr[i] = (byte) (s >>> 8);
        bArr[i + 1] = (byte) (s & 255);
    }

    public static final void writeIntBigEndian(byte[] bArr, int i, int i2) {
        bArr[i] = (byte) ((i2 >>> 24) & 255);
        bArr[i + 1] = (byte) ((i2 >>> 16) & 255);
        bArr[i + 2] = (byte) ((i2 >>> 8) & 255);
        bArr[i + 3] = (byte) (i2 & 255);
    }

    public static final void writeLongBigEndian(byte[] bArr, int i, long j) {
        bArr[i] = (byte) ((int) (j >>> 56));
        bArr[i + 1] = (byte) ((int) (j >>> 48));
        bArr[i + 2] = (byte) ((int) (j >>> 40));
        bArr[i + 3] = (byte) ((int) (j >>> 32));
        bArr[i + 4] = (byte) ((int) (j >>> 24));
        bArr[i + 5] = (byte) ((int) (j >>> 16));
        bArr[i + 6] = (byte) ((int) (j >>> 8));
        bArr[i + 7] = (byte) ((int) (j & 255));
    }

    public static final void writeShortLittleEndian(byte[] bArr, int i, short s) {
        bArr[i + 1] = (byte) (s >>> 8);
        bArr[i] = (byte) (s & 255);
    }

    public static final void incShortLittleEndian(byte[] bArr, int i, int i2) {
        int i3 = i2 & 255;
        int i4 = ((bArr[i] & 255) + i3) >>> 8;
        bArr[i] = (byte) (bArr[i] + i3);
        if (i4 > 0 || (MotionEventCompat.ACTION_POINTER_INDEX_MASK & i2) != 0) {
            i++;
            bArr[i] = (byte) (bArr[i] + (((i2 >>> 8) & 255) + i4));
        }
    }

    public static final void writeIntLittleEndian(byte[] bArr, int i, int i2) {
        bArr[i + 3] = (byte) (i2 >>> 24);
        bArr[i + 2] = (byte) (i2 >>> 16);
        bArr[i + 1] = (byte) (i2 >>> 8);
        bArr[i] = (byte) (i2 & 255);
    }

    public static final void writeLongLittleEndian(byte[] bArr, int i, long j) {
        bArr[i + 7] = (byte) ((int) (j >>> 56));
        bArr[i + 6] = (byte) ((int) (j >>> 48));
        bArr[i + 5] = (byte) ((int) (j >>> 40));
        bArr[i + 4] = (byte) ((int) (j >>> 32));
        bArr[i + 3] = (byte) ((int) (j >>> 24));
        bArr[i + 2] = (byte) ((int) (j >>> 16));
        bArr[i + 1] = (byte) ((int) (j >>> 8));
        bArr[i] = (byte) ((int) (j & 255));
    }
}
