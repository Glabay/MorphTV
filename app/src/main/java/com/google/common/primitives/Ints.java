package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.CheckForNull;

@GwtCompatible(emulated = true)
public final class Ints {
    public static final int BYTES = 4;
    public static final int MAX_POWER_OF_TWO = 1073741824;
    private static final byte[] asciiDigits = new byte[128];

    public static int compare(int i, int i2) {
        return i < i2 ? -1 : i > i2 ? 1 : 0;
    }

    @GwtIncompatible("doesn't work")
    public static int fromBytes(byte b, byte b2, byte b3, byte b4) {
        return (((b << 24) | ((b2 & 255) << 16)) | ((b3 & 255) << 8)) | (b4 & 255);
    }

    public static int hashCode(int i) {
        return i;
    }

    public static int saturatedCast(long j) {
        return j > 2147483647L ? Integer.MAX_VALUE : j < -2147483648L ? Integer.MIN_VALUE : (int) j;
    }

    private Ints() {
    }

    public static int checkedCast(long j) {
        int i = (int) j;
        if (((long) i) == j) {
            return i;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Out of range: ");
        stringBuilder.append(j);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static boolean contains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return 1;
            }
        }
        return false;
    }

    public static int indexOf(int[] iArr, int i) {
        return indexOf(iArr, i, 0, iArr.length);
    }

    private static int indexOf(int[] iArr, int i, int i2, int i3) {
        while (i2 < i3) {
            if (iArr[i2] == i) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public static int indexOf(int[] iArr, int[] iArr2) {
        Preconditions.checkNotNull(iArr, SerializerHandler.TYPE_ARRAY);
        Preconditions.checkNotNull(iArr2, "target");
        if (iArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (iArr.length - iArr2.length) + 1) {
            int i2 = 0;
            while (i2 < iArr2.length) {
                if (iArr[i + i2] != iArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(int[] iArr, int i) {
        return lastIndexOf(iArr, i, 0, iArr.length);
    }

    private static int lastIndexOf(int[] iArr, int i, int i2, int i3) {
        for (i3--; i3 >= i2; i3--) {
            if (iArr[i3] == i) {
                return i3;
            }
        }
        return -1;
    }

    public static int min(int... iArr) {
        Preconditions.checkArgument(iArr.length > 0);
        int i = iArr[0];
        for (int i2 = 1; i2 < iArr.length; i2++) {
            if (iArr[i2] < i) {
                i = iArr[i2];
            }
        }
        return i;
    }

    public static int max(int... iArr) {
        Preconditions.checkArgument(iArr.length > 0);
        int i = iArr[0];
        for (int i2 = 1; i2 < iArr.length; i2++) {
            if (iArr[i2] > i) {
                i = iArr[i2];
            }
        }
        return i;
    }

    public static int[] concat(int[]... iArr) {
        int i = 0;
        for (int[] length : iArr) {
            i += length.length;
        }
        Object obj = new int[i];
        int i2 = 0;
        for (Object obj2 : iArr) {
            System.arraycopy(obj2, 0, obj, i2, obj2.length);
            i2 += obj2.length;
        }
        return obj;
    }

    @GwtIncompatible("doesn't work")
    public static byte[] toByteArray(int i) {
        return new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
    }

    @GwtIncompatible("doesn't work")
    public static int fromByteArray(byte[] bArr) {
        Preconditions.checkArgument(bArr.length >= 4, "array too small: %s < %s", Integer.valueOf(bArr.length), Integer.valueOf(4));
        return fromBytes(bArr[0], bArr[1], bArr[2], bArr[3]);
    }

    @Beta
    public static Converter<String, Integer> stringConverter() {
        return Ints$IntConverter.INSTANCE;
    }

    public static int[] ensureCapacity(int[] iArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", Integer.valueOf(i));
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", Integer.valueOf(i2));
        return iArr.length < i ? copyOf(iArr, i + i2) : iArr;
    }

    private static int[] copyOf(int[] iArr, int i) {
        Object obj = new int[i];
        System.arraycopy(iArr, 0, obj, 0, Math.min(iArr.length, i));
        return obj;
    }

    public static String join(String str, int... iArr) {
        Preconditions.checkNotNull(str);
        if (iArr.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(iArr.length * 5);
        stringBuilder.append(iArr[0]);
        for (int i = 1; i < iArr.length; i++) {
            stringBuilder.append(str);
            stringBuilder.append(iArr[i]);
        }
        return stringBuilder.toString();
    }

    public static Comparator<int[]> lexicographicalComparator() {
        return Ints$LexicographicalComparator.INSTANCE;
    }

    public static int[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof Ints$IntArrayAsList) {
            return ((Ints$IntArrayAsList) collection).toIntArray();
        }
        collection = collection.toArray();
        int length = collection.length;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            iArr[i] = ((Number) Preconditions.checkNotNull(collection[i])).intValue();
        }
        return iArr;
    }

    public static List<Integer> asList(int... iArr) {
        if (iArr.length == 0) {
            return Collections.emptyList();
        }
        return new Ints$IntArrayAsList(iArr);
    }

    static {
        Arrays.fill(asciiDigits, (byte) -1);
        for (int i = 0; i <= 9; i++) {
            asciiDigits[i + 48] = (byte) i;
        }
        for (int i2 = 0; i2 <= 26; i2++) {
            byte b = (byte) (i2 + 10);
            asciiDigits[i2 + 65] = b;
            asciiDigits[i2 + 97] = b;
        }
    }

    private static int digit(char c) {
        return c < '' ? asciiDigits[c] : '￿';
    }

    @CheckForNull
    @Beta
    public static Integer tryParse(String str) {
        return tryParse(str, 10);
    }

    @CheckForNull
    static Integer tryParse(String str, int i) {
        if (((String) Preconditions.checkNotNull(str)).isEmpty()) {
            return null;
        }
        if (i >= 2) {
            if (i <= 36) {
                int i2 = 0;
                if (str.charAt(0) == '-') {
                    i2 = 1;
                }
                if (i2 == str.length()) {
                    return null;
                }
                int i3 = i2 + 1;
                int digit = digit(str.charAt(i2));
                if (digit >= 0) {
                    if (digit < i) {
                        digit = -digit;
                        int i4 = Integer.MIN_VALUE / i;
                        while (i3 < str.length()) {
                            int i5 = i3 + 1;
                            i3 = digit(str.charAt(i3));
                            if (i3 >= 0 && i3 < i) {
                                if (digit >= i4) {
                                    digit *= i;
                                    if (digit < i3 - Integer.MIN_VALUE) {
                                        return null;
                                    }
                                    digit -= i3;
                                    i3 = i5;
                                }
                            }
                            return null;
                        }
                        if (i2 != 0) {
                            return Integer.valueOf(digit);
                        }
                        if (digit == Integer.MIN_VALUE) {
                            return null;
                        }
                        return Integer.valueOf(-digit);
                    }
                }
                return null;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("radix must be between MIN_RADIX and MAX_RADIX but was ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
