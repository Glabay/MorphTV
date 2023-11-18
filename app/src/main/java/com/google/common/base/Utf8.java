package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
public final class Utf8 {
    public static int encodedLength(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && charSequence.charAt(i) < '') {
            i++;
        }
        int i2 = length;
        while (i < length) {
            char charAt = charSequence.charAt(i);
            if (charAt >= 'ࠀ') {
                i2 += encodedLengthGeneral(charSequence, i);
                break;
            }
            i2 += (127 - charAt) >>> 31;
            i++;
        }
        if (i2 >= length) {
            return i2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UTF-8 length does not fit in int: ");
        stringBuilder.append(((long) i2) + 4294967296L);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static int encodedLengthGeneral(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = 0;
        while (i < length) {
            char charAt = charSequence.charAt(i);
            if (charAt < 'ࠀ') {
                i2 += (127 - charAt) >>> 31;
            } else {
                i2 += 2;
                if ('?' <= charAt && charAt <= '?') {
                    if (Character.codePointAt(charSequence, i) < 65536) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unpaired surrogate at index ");
                        stringBuilder.append(i);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                    i++;
                }
            }
            i++;
        }
        return i2;
    }

    public static boolean isWellFormed(byte[] bArr) {
        return isWellFormed(bArr, 0, bArr.length);
    }

    public static boolean isWellFormed(byte[] bArr, int i, int i2) {
        i2 += i;
        Preconditions.checkPositionIndexes(i, i2, bArr.length);
        while (i < i2) {
            if (bArr[i] < (byte) 0) {
                return isWellFormedSlowPath(bArr, i, i2);
            }
            i++;
        }
        return 1;
    }

    private static boolean isWellFormedSlowPath(byte[] bArr, int i, int i2) {
        while (i < i2) {
            int i3 = i + 1;
            i = bArr[i];
            if (i < 0) {
                if (i < -32) {
                    if (i3 != i2 && i >= -62) {
                        i = i3 + 1;
                        if (bArr[i3] > (byte) -65) {
                        }
                    }
                    return false;
                } else if (i < -16) {
                    int i4 = i3 + 1;
                    if (i4 >= i2) {
                        return false;
                    }
                    r0 = bArr[i3];
                    if (r0 <= (byte) -65 && ((i != -32 || r0 >= (byte) -96) && (i != -19 || (byte) -96 > r0))) {
                        i = i4 + 1;
                        if (bArr[i4] > (byte) -65) {
                        }
                    }
                    return false;
                } else if (i3 + 2 >= i2) {
                    return false;
                } else {
                    int i5 = i3 + 1;
                    r0 = bArr[i3];
                    if (r0 <= (byte) -65 && (((i << 28) + (r0 + 112)) >> 30) == 0) {
                        i = i5 + 1;
                        if (bArr[i5] <= (byte) -65) {
                            i3 = i + 1;
                            if (bArr[i] > -65) {
                            }
                        }
                    }
                    return false;
                }
            }
            i = i3;
        }
        return 1;
    }

    private Utf8() {
    }
}
