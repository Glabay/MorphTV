package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.math.BigInteger;
import java.util.Comparator;

@GwtCompatible
@Beta
public final class UnsignedLongs {
    public static final long MAX_VALUE = -1;
    private static final int[] maxSafeDigits = new int[37];
    private static final long[] maxValueDivs = new long[37];
    private static final int[] maxValueMods = new int[37];

    enum LexicographicalComparator implements Comparator<long[]> {
        INSTANCE;

        public int compare(long[] jArr, long[] jArr2) {
            int min = Math.min(jArr.length, jArr2.length);
            for (int i = 0; i < min; i++) {
                if (jArr[i] != jArr2[i]) {
                    return UnsignedLongs.compare(jArr[i], jArr2[i]);
                }
            }
            return jArr.length - jArr2.length;
        }
    }

    private static long flip(long j) {
        return j ^ Long.MIN_VALUE;
    }

    private UnsignedLongs() {
    }

    public static int compare(long j, long j2) {
        return Longs.compare(flip(j), flip(j2));
    }

    public static long min(long... jArr) {
        Preconditions.checkArgument(jArr.length > 0);
        long flip = flip(jArr[0]);
        for (int i = 1; i < jArr.length; i++) {
            long flip2 = flip(jArr[i]);
            if (flip2 < flip) {
                flip = flip2;
            }
        }
        return flip(flip);
    }

    public static long max(long... jArr) {
        Preconditions.checkArgument(jArr.length > 0);
        long flip = flip(jArr[0]);
        for (int i = 1; i < jArr.length; i++) {
            long flip2 = flip(jArr[i]);
            if (flip2 > flip) {
                flip = flip2;
            }
        }
        return flip(flip);
    }

    public static String join(String str, long... jArr) {
        Preconditions.checkNotNull(str);
        if (jArr.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(jArr.length * 5);
        stringBuilder.append(toString(jArr[0]));
        for (int i = 1; i < jArr.length; i++) {
            stringBuilder.append(str);
            stringBuilder.append(toString(jArr[i]));
        }
        return stringBuilder.toString();
    }

    public static Comparator<long[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static long divide(long j, long j2) {
        if (j2 < 0) {
            return compare(j, j2) < null ? 0 : 1;
        } else {
            if (j >= 0) {
                return j / j2;
            }
            int i = 1;
            long j3 = ((j >>> 1) / j2) << 1;
            if (compare(j - (j3 * j2), j2) < null) {
                i = 0;
            }
            return j3 + ((long) i);
        }
    }

    public static long remainder(long j, long j2) {
        if (j2 < 0) {
            return compare(j, j2) < 0 ? j : j - j2;
        } else {
            if (j >= 0) {
                return j % j2;
            }
            long j3 = j - ((((j >>> 1) / j2) << 1) * j2);
            if (compare(j3, j2) < null) {
                j2 = 0;
            }
            return j3 - j2;
        }
    }

    public static long parseUnsignedLong(String str) {
        return parseUnsignedLong(str, 10);
    }

    public static long decode(String str) {
        ParseRequest fromString = ParseRequest.fromString(str);
        try {
            return parseUnsignedLong(fromString.rawValue, fromString.radix);
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error parsing value: ");
            stringBuilder.append(str);
            NumberFormatException numberFormatException = new NumberFormatException(stringBuilder.toString());
            numberFormatException.initCause(e);
            throw numberFormatException;
        }
    }

    public static long parseUnsignedLong(String str, int i) {
        Preconditions.checkNotNull(str);
        if (str.length() == 0) {
            throw new NumberFormatException("empty string");
        }
        StringBuilder stringBuilder;
        if (i >= 2) {
            if (i <= 36) {
                int i2 = maxSafeDigits[i] - 1;
                long j = 0;
                int i3 = 0;
                while (i3 < str.length()) {
                    int digit = Character.digit(str.charAt(i3), i);
                    if (digit == -1) {
                        throw new NumberFormatException(str);
                    } else if (i3 <= i2 || !overflowInParse(j, digit, i)) {
                        i3++;
                        j = (j * ((long) i)) + ((long) digit);
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Too large for unsigned long: ");
                        stringBuilder.append(str);
                        throw new NumberFormatException(stringBuilder.toString());
                    }
                }
                return j;
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("illegal radix: ");
        stringBuilder.append(i);
        throw new NumberFormatException(stringBuilder.toString());
    }

    private static boolean overflowInParse(long j, int i, int i2) {
        boolean z = true;
        if (j < 0) {
            return true;
        }
        if (j < maxValueDivs[i2]) {
            return false;
        }
        if (j > maxValueDivs[i2]) {
            return true;
        }
        if (i <= maxValueMods[i2]) {
            z = false;
        }
        return z;
    }

    public static String toString(long j) {
        return toString(j, 10);
    }

    public static String toString(long j, int i) {
        boolean z = i >= 2 && i <= 36;
        Preconditions.checkArgument(z, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", new Object[]{Integer.valueOf(i)});
        if (j == 0) {
            return "0";
        }
        char[] cArr = new char[64];
        int length = cArr.length;
        if (j < 0) {
            long j2 = (long) i;
            long divide = divide(j, j2);
            length--;
            cArr[length] = Character.forDigit((int) (j - (j2 * divide)), i);
            j = divide;
        }
        while (j > 0) {
            length--;
            j2 = (long) i;
            cArr[length] = Character.forDigit((int) (j % j2), i);
            j /= j2;
        }
        return new String(cArr, length, cArr.length - length);
    }

    static {
        BigInteger bigInteger = new BigInteger("10000000000000000", 16);
        for (int i = 2; i <= 36; i++) {
            long j = (long) i;
            maxValueDivs[i] = divide(-1, j);
            maxValueMods[i] = (int) remainder(-1, j);
            maxSafeDigits[i] = bigInteger.toString(i).length() - 1;
        }
    }
}
