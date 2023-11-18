package com.google.common.math;

import com.google.common.base.Preconditions;
import java.math.BigInteger;

final class DoubleUtils {
    static final int EXPONENT_BIAS = 1023;
    static final long EXPONENT_MASK = 9218868437227405312L;
    static final long IMPLICIT_BIT = 4503599627370496L;
    private static final long ONE_BITS = Double.doubleToRawLongBits(1.0d);
    static final int SIGNIFICAND_BITS = 52;
    static final long SIGNIFICAND_MASK = 4503599627370495L;
    static final long SIGN_MASK = Long.MIN_VALUE;

    private DoubleUtils() {
    }

    static double nextDown(double d) {
        return -Math.nextUp(-d);
    }

    static long getSignificand(double d) {
        Preconditions.checkArgument(isFinite(d), "not a normal value");
        long doubleToRawLongBits = Double.doubleToRawLongBits(d) & SIGNIFICAND_MASK;
        return Math.getExponent(d) == Double.NaN ? doubleToRawLongBits << Double.MIN_VALUE : doubleToRawLongBits | IMPLICIT_BIT;
    }

    static boolean isFinite(double d) {
        return Math.getExponent(d) <= EXPONENT_BIAS ? Double.MIN_VALUE : 0.0d;
    }

    static boolean isNormal(double d) {
        return Math.getExponent(d) >= -1022 ? Double.MIN_VALUE : 0.0d;
    }

    static double scaleNormalize(double d) {
        return Double.longBitsToDouble((Double.doubleToRawLongBits(d) & SIGNIFICAND_MASK) | ONE_BITS);
    }

    static double bigToDouble(BigInteger bigInteger) {
        BigInteger abs = bigInteger.abs();
        int i = 1;
        int bitLength = abs.bitLength() - 1;
        if (bitLength < 63) {
            return (double) bigInteger.longValue();
        }
        if (bitLength > EXPONENT_BIAS) {
            return ((double) bigInteger.signum()) * Double.POSITIVE_INFINITY;
        }
        int i2 = (bitLength - 52) - 1;
        long longValue = abs.shiftRight(i2).longValue();
        long j = (longValue >> 1) & SIGNIFICAND_MASK;
        if ((longValue & 1) != 0) {
            if ((j & 1) == 0) {
                if (abs.getLowestSetBit() < i2) {
                }
            }
            return Double.longBitsToDouble(((((long) (bitLength + EXPONENT_BIAS)) << 52) + (i == 0 ? j + 1 : j)) | (((long) bigInteger.signum()) & Long.MIN_VALUE));
        }
        i = 0;
        if (i == 0) {
        }
        return Double.longBitsToDouble(((((long) (bitLength + EXPONENT_BIAS)) << 52) + (i == 0 ? j + 1 : j)) | (((long) bigInteger.signum()) & Long.MIN_VALUE));
    }

    static double ensureNonNegative(double d) {
        Preconditions.checkArgument(Double.isNaN(d) ^ 1);
        return d > 0.0d ? d : 0.0d;
    }
}
