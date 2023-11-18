package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@GwtCompatible(emulated = true)
public final class BigIntegerMath {
    private static final double LN_10 = Math.log(10.0d);
    private static final double LN_2 = Math.log(2.0d);
    @VisibleForTesting
    static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
    @VisibleForTesting
    static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;

    /* renamed from: com.google.common.math.BigIntegerMath$1 */
    static /* synthetic */ class C11741 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = java.math.RoundingMode.values();
            r0 = r0.length;
            r0 = new int[r0];
            $SwitchMap$java$math$RoundingMode = r0;
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = java.math.RoundingMode.UNNECESSARY;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = 1;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = java.math.RoundingMode.DOWN;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2 = 2;	 Catch:{ NoSuchFieldError -> 0x001f }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = java.math.RoundingMode.FLOOR;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r2 = 3;	 Catch:{ NoSuchFieldError -> 0x002a }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r1 = java.math.RoundingMode.UP;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0035 }
            r2 = 4;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0035 }
        L_0x0035:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x0040 }
            r1 = java.math.RoundingMode.CEILING;	 Catch:{ NoSuchFieldError -> 0x0040 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0040 }
            r2 = 5;	 Catch:{ NoSuchFieldError -> 0x0040 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0040 }
        L_0x0040:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x004b }
            r1 = java.math.RoundingMode.HALF_DOWN;	 Catch:{ NoSuchFieldError -> 0x004b }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x004b }
            r2 = 6;	 Catch:{ NoSuchFieldError -> 0x004b }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x004b }
        L_0x004b:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x0056 }
            r1 = java.math.RoundingMode.HALF_UP;	 Catch:{ NoSuchFieldError -> 0x0056 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0056 }
            r2 = 7;	 Catch:{ NoSuchFieldError -> 0x0056 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0056 }
        L_0x0056:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x0062 }
            r1 = java.math.RoundingMode.HALF_EVEN;	 Catch:{ NoSuchFieldError -> 0x0062 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0062 }
            r2 = 8;	 Catch:{ NoSuchFieldError -> 0x0062 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0062 }
        L_0x0062:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.BigIntegerMath.1.<clinit>():void");
        }
    }

    public static boolean isPowerOfTwo(BigInteger bigInteger) {
        Preconditions.checkNotNull(bigInteger);
        return bigInteger.signum() > 0 && bigInteger.getLowestSetBit() == bigInteger.bitLength() - 1;
    }

    public static int log2(BigInteger bigInteger, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", (BigInteger) Preconditions.checkNotNull(bigInteger));
        int bitLength = bigInteger.bitLength() - 1;
        switch (C11741.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(bigInteger));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                if (isPowerOfTwo(bigInteger) == null) {
                    bitLength++;
                }
                return bitLength;
            case 6:
            case 7:
            case 8:
                if (bitLength < 256) {
                    return bigInteger.compareTo(SQRT2_PRECOMPUTED_BITS.shiftRight(256 - bitLength)) <= null ? bitLength : bitLength + 1;
                } else {
                    if (bigInteger.pow(2).bitLength() - 1 >= (bitLength * 2) + 1) {
                        bitLength++;
                    }
                    return bitLength;
                }
            default:
                throw new AssertionError();
        }
        return bitLength;
    }

    @GwtIncompatible("TODO")
    public static int log10(BigInteger bigInteger, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", bigInteger);
        if (fitsInLong(bigInteger)) {
            return LongMath.log10(bigInteger.longValue(), roundingMode);
        }
        int log2 = (int) ((((double) log2(bigInteger, RoundingMode.FLOOR)) * LN_2) / LN_10);
        BigInteger pow = BigInteger.TEN.pow(log2);
        int compareTo = pow.compareTo(bigInteger);
        if (compareTo > 0) {
            do {
                log2--;
                pow = pow.divide(BigInteger.TEN);
                compareTo = pow.compareTo(bigInteger);
            } while (compareTo > 0);
        } else {
            BigInteger multiply = BigInteger.TEN.multiply(pow);
            int compareTo2 = multiply.compareTo(bigInteger);
            BigInteger bigInteger2 = multiply;
            int i = compareTo;
            compareTo = compareTo2;
            BigInteger bigInteger3 = bigInteger2;
            while (compareTo <= 0) {
                log2++;
                pow = BigInteger.TEN.multiply(bigInteger3);
                i = pow.compareTo(bigInteger);
                bigInteger2 = bigInteger3;
                bigInteger3 = pow;
                pow = bigInteger2;
                int i2 = i;
                i = compareTo;
                compareTo = i2;
            }
            compareTo = i;
        }
        switch (C11741.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(compareTo == 0 ? true : null);
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                if (pow.equals(bigInteger) == null) {
                    log2++;
                }
                return log2;
            case 6:
            case 7:
            case 8:
                if (bigInteger.pow(2).compareTo(pow.pow(2).multiply(BigInteger.TEN)) > null) {
                    log2++;
                }
                return log2;
            default:
                throw new AssertionError();
        }
        return log2;
    }

    @GwtIncompatible("TODO")
    public static BigInteger sqrt(BigInteger bigInteger, RoundingMode roundingMode) {
        MathPreconditions.checkNonNegative("x", bigInteger);
        if (fitsInLong(bigInteger)) {
            return BigInteger.valueOf(LongMath.sqrt(bigInteger.longValue(), roundingMode));
        }
        BigInteger sqrtFloor = sqrtFloor(bigInteger);
        switch (C11741.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(sqrtFloor.pow(2).equals(bigInteger));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                roundingMode = sqrtFloor.intValue();
                bigInteger = (roundingMode * roundingMode != bigInteger.intValue() || sqrtFloor.pow(2).equals(bigInteger) == null) ? null : true;
                if (bigInteger == null) {
                    sqrtFloor = sqrtFloor.add(BigInteger.ONE);
                }
                return sqrtFloor;
            case 6:
            case 7:
            case 8:
                if (sqrtFloor.pow(2).add(sqrtFloor).compareTo(bigInteger) < null) {
                    sqrtFloor = sqrtFloor.add(BigInteger.ONE);
                }
                return sqrtFloor;
            default:
                throw new AssertionError();
        }
        return sqrtFloor;
    }

    @GwtIncompatible("TODO")
    private static BigInteger sqrtFloor(BigInteger bigInteger) {
        BigInteger sqrtApproxWithDoubles;
        int log2 = log2(bigInteger, RoundingMode.FLOOR);
        if (log2 < 1023) {
            sqrtApproxWithDoubles = sqrtApproxWithDoubles(bigInteger);
        } else {
            log2 = (log2 - 52) & -2;
            sqrtApproxWithDoubles = sqrtApproxWithDoubles(bigInteger.shiftRight(log2)).shiftLeft(log2 >> 1);
        }
        BigInteger shiftRight = sqrtApproxWithDoubles.add(bigInteger.divide(sqrtApproxWithDoubles)).shiftRight(1);
        if (sqrtApproxWithDoubles.equals(shiftRight)) {
            return sqrtApproxWithDoubles;
        }
        while (true) {
            sqrtApproxWithDoubles = shiftRight.add(bigInteger.divide(shiftRight)).shiftRight(1);
            if (sqrtApproxWithDoubles.compareTo(shiftRight) >= 0) {
                return shiftRight;
            }
            shiftRight = sqrtApproxWithDoubles;
        }
    }

    @GwtIncompatible("TODO")
    private static BigInteger sqrtApproxWithDoubles(BigInteger bigInteger) {
        return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(bigInteger)), RoundingMode.HALF_EVEN);
    }

    @GwtIncompatible("TODO")
    public static BigInteger divide(BigInteger bigInteger, BigInteger bigInteger2, RoundingMode roundingMode) {
        return new BigDecimal(bigInteger).divide(new BigDecimal(bigInteger2), null, roundingMode).toBigIntegerExact();
    }

    public static BigInteger factorial(int i) {
        int i2 = i;
        MathPreconditions.checkNonNegative("n", i2);
        if (i2 < LongMath.factorials.length) {
            return BigInteger.valueOf(LongMath.factorials[i2]);
        }
        List arrayList = new ArrayList(IntMath.divide(IntMath.log2(i2, RoundingMode.CEILING) * i2, 64, RoundingMode.CEILING));
        int length = LongMath.factorials.length;
        long j = LongMath.factorials[length - 1];
        int numberOfTrailingZeros = Long.numberOfTrailingZeros(j);
        j >>= numberOfTrailingZeros;
        int log2 = LongMath.log2(j, RoundingMode.FLOOR) + 1;
        long j2 = (long) length;
        length = LongMath.log2(j2, RoundingMode.FLOOR) + 1;
        int i3 = 1 << (length - 1);
        while (j2 <= ((long) i2)) {
            if ((j2 & ((long) i3)) != 0) {
                i3 <<= 1;
                length++;
            }
            int numberOfTrailingZeros2 = Long.numberOfTrailingZeros(j2);
            long j3 = j2 >> numberOfTrailingZeros2;
            numberOfTrailingZeros += numberOfTrailingZeros2;
            if ((length - numberOfTrailingZeros2) + log2 >= 64) {
                arrayList.add(BigInteger.valueOf(j));
                j = 1;
            }
            j *= j3;
            log2 = LongMath.log2(j, RoundingMode.FLOOR) + 1;
            j2++;
        }
        if (j > 1) {
            arrayList.add(BigInteger.valueOf(j));
        }
        return listProduct(arrayList).shiftLeft(numberOfTrailingZeros);
    }

    static BigInteger listProduct(List<BigInteger> list) {
        return listProduct(list, 0, list.size());
    }

    static BigInteger listProduct(List<BigInteger> list, int i, int i2) {
        switch (i2 - i) {
            case 0:
                return BigInteger.ONE;
            case 1:
                return (BigInteger) list.get(i);
            case 2:
                return ((BigInteger) list.get(i)).multiply((BigInteger) list.get(i + 1));
            case 3:
                return ((BigInteger) list.get(i)).multiply((BigInteger) list.get(i + 1)).multiply((BigInteger) list.get(i + 2));
            default:
                int i3 = (i2 + i) >>> 1;
                return listProduct(list, i, i3).multiply(listProduct(list, i3, i2));
        }
    }

    public static BigInteger binomial(int i, int i2) {
        MathPreconditions.checkNonNegative("n", i);
        MathPreconditions.checkNonNegative("k", i2);
        int i3 = 1;
        Preconditions.checkArgument(i2 <= i, "k (%s) > n (%s)", new Object[]{Integer.valueOf(i2), Integer.valueOf(i)});
        if (i2 > (i >> 1)) {
            i2 = i - i2;
        }
        if (i2 < LongMath.biggestBinomials.length && i <= LongMath.biggestBinomials[i2]) {
            return BigInteger.valueOf(LongMath.binomial(i, i2));
        }
        BigInteger bigInteger = BigInteger.ONE;
        long j = (long) i;
        long j2 = 1;
        int log2 = LongMath.log2(j, RoundingMode.CEILING);
        while (true) {
            long j3 = j2;
            long j4 = j;
            BigInteger bigInteger2 = bigInteger;
            int i4 = log2;
            while (i3 < i2) {
                int i5 = i - i3;
                i3++;
                i4 += log2;
                if (i4 >= 63) {
                    bigInteger = bigInteger2.multiply(BigInteger.valueOf(j4)).divide(BigInteger.valueOf(j3));
                    j = (long) i5;
                    j2 = (long) i3;
                } else {
                    j4 *= (long) i5;
                    j3 *= (long) i3;
                }
            }
            return bigInteger2.multiply(BigInteger.valueOf(j4)).divide(BigInteger.valueOf(j3));
        }
    }

    @GwtIncompatible("TODO")
    static boolean fitsInLong(BigInteger bigInteger) {
        return bigInteger.bitLength() <= 63 ? true : null;
    }

    private BigIntegerMath() {
    }
}
