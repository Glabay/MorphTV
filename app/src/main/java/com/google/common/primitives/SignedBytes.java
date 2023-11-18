package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;

@GwtCompatible
public final class SignedBytes {
    public static final byte MAX_POWER_OF_TWO = (byte) 64;

    private enum LexicographicalComparator implements Comparator<byte[]> {
        INSTANCE;

        public int compare(byte[] bArr, byte[] bArr2) {
            int min = Math.min(bArr.length, bArr2.length);
            for (int i = 0; i < min; i++) {
                int compare = SignedBytes.compare(bArr[i], bArr2[i]);
                if (compare != 0) {
                    return compare;
                }
            }
            return bArr.length - bArr2.length;
        }
    }

    public static int compare(byte b, byte b2) {
        return b - b2;
    }

    public static byte saturatedCast(long j) {
        return j > 127 ? Byte.MAX_VALUE : j < -128 ? Byte.MIN_VALUE : (byte) ((int) j);
    }

    private SignedBytes() {
    }

    public static byte checkedCast(long j) {
        byte b = (byte) ((int) j);
        if (((long) b) == j) {
            return b;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Out of range: ");
        stringBuilder.append(j);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static byte min(byte... bArr) {
        Preconditions.checkArgument(bArr.length > 0);
        byte b = bArr[0];
        for (int i = 1; i < bArr.length; i++) {
            if (bArr[i] < b) {
                b = bArr[i];
            }
        }
        return b;
    }

    public static byte max(byte... bArr) {
        Preconditions.checkArgument(bArr.length > 0);
        byte b = bArr[0];
        for (int i = 1; i < bArr.length; i++) {
            if (bArr[i] > b) {
                b = bArr[i];
            }
        }
        return b;
    }

    public static String join(String str, byte... bArr) {
        Preconditions.checkNotNull(str);
        if (bArr.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 5);
        stringBuilder.append(bArr[0]);
        for (int i = 1; i < bArr.length; i++) {
            stringBuilder.append(str);
            stringBuilder.append(bArr[i]);
        }
        return stringBuilder.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
}
