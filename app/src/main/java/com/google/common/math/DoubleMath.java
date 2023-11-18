package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;

@GwtCompatible(emulated = true)
public final class DoubleMath {
    private static final double LN_2 = Math.log(2.0d);
    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18d;
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9d;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18d;
    @VisibleForTesting
    static final double[] everySixteenthFactorial = new double[]{1.0d, 2.0922789888E13d, 2.631308369336935E35d, 1.2413915592536073E61d, 1.2688693218588417E89d, 7.156945704626381E118d, 9.916779348709496E149d, 1.974506857221074E182d, 3.856204823625804E215d, 5.5502938327393044E249d, 4.7147236359920616E284d};

    /* renamed from: com.google.common.math.DoubleMath$1 */
    static /* synthetic */ class C11751 {
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
            r1 = java.math.RoundingMode.FLOOR;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2 = 2;	 Catch:{ NoSuchFieldError -> 0x001f }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = java.math.RoundingMode.CEILING;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r2 = 3;	 Catch:{ NoSuchFieldError -> 0x002a }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r1 = java.math.RoundingMode.DOWN;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0035 }
            r2 = 4;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0035 }
        L_0x0035:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x0040 }
            r1 = java.math.RoundingMode.UP;	 Catch:{ NoSuchFieldError -> 0x0040 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0040 }
            r2 = 5;	 Catch:{ NoSuchFieldError -> 0x0040 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0040 }
        L_0x0040:
            r0 = $SwitchMap$java$math$RoundingMode;	 Catch:{ NoSuchFieldError -> 0x004b }
            r1 = java.math.RoundingMode.HALF_EVEN;	 Catch:{ NoSuchFieldError -> 0x004b }
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
            r1 = java.math.RoundingMode.HALF_DOWN;	 Catch:{ NoSuchFieldError -> 0x0062 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0062 }
            r2 = 8;	 Catch:{ NoSuchFieldError -> 0x0062 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0062 }
        L_0x0062:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.DoubleMath.1.<clinit>():void");
        }
    }

    @GwtIncompatible("com.google.common.math.DoubleUtils")
    private static final class MeanAccumulator {
        private long count;
        private double mean;

        private MeanAccumulator() {
            this.count = 0;
            this.mean = DoubleMath.LN_2;
        }

        void add(double d) {
            Preconditions.checkArgument(DoubleUtils.isFinite(d));
            this.count++;
            this.mean += (d - this.mean) / ((double) this.count);
        }

        double mean() {
            Preconditions.checkArgument(this.count > 0, "Cannot take mean of 0 values");
            return this.mean;
        }
    }

    @GwtIncompatible("#isMathematicalInteger, com.google.common.math.DoubleUtils")
    static double roundIntermediate(double d, RoundingMode roundingMode) {
        if (DoubleUtils.isFinite(d)) {
            double rint;
            switch (C11751.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
                case 1:
                    MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(d));
                    return d;
                case 2:
                    if (d < LN_2) {
                        if (isMathematicalInteger(d) == null) {
                            return d - 1.0d;
                        }
                    }
                    return d;
                case 3:
                    if (d > LN_2) {
                        if (isMathematicalInteger(d) == null) {
                            return d + 1.0d;
                        }
                    }
                    return d;
                case 4:
                    return d;
                case 5:
                    if (isMathematicalInteger(d) != null) {
                        return d;
                    }
                    return d + Math.copySign(1.0d, d);
                case 6:
                    return Math.rint(d);
                case 7:
                    rint = Math.rint(d);
                    return Math.abs(d - rint) == 0.5d ? d + Math.copySign(0.5d, d) : rint;
                case 8:
                    rint = Math.rint(d);
                    return Math.abs(d - rint) == 0.5d ? d : rint;
                default:
                    throw new AssertionError();
            }
        }
        throw new ArithmeticException("input is infinite or NaN");
    }

    @GwtIncompatible("#roundIntermediate")
    public static int roundToInt(double d, RoundingMode roundingMode) {
        d = roundIntermediate(d, roundingMode);
        int i = 0;
        roundingMode = d > -2.147483649E9d ? true : null;
        if (d < 2.147483648E9d) {
            i = 1;
        }
        MathPreconditions.checkInRange(roundingMode & i);
        return (int) d;
    }

    @GwtIncompatible("#roundIntermediate")
    public static long roundToLong(double d, RoundingMode roundingMode) {
        d = roundIntermediate(d, roundingMode);
        int i = 0;
        roundingMode = MIN_LONG_AS_DOUBLE - d < 1.0d ? true : null;
        if (d < MAX_LONG_AS_DOUBLE_PLUS_ONE) {
            i = 1;
        }
        MathPreconditions.checkInRange(roundingMode & i);
        return (long) d;
    }

    @GwtIncompatible("#roundIntermediate, java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static BigInteger roundToBigInteger(double d, RoundingMode roundingMode) {
        d = roundIntermediate(d, roundingMode);
        int i = 0;
        roundingMode = MIN_LONG_AS_DOUBLE - d < 1.0d ? true : null;
        if (d < MAX_LONG_AS_DOUBLE_PLUS_ONE) {
            i = 1;
        }
        if ((roundingMode & i) != null) {
            return BigInteger.valueOf((long) d);
        }
        roundingMode = BigInteger.valueOf(DoubleUtils.getSignificand(d)).shiftLeft(Math.getExponent(d) - 52);
        if (d < LN_2) {
            roundingMode = roundingMode.negate();
        }
        return roundingMode;
    }

    @GwtIncompatible("com.google.common.math.DoubleUtils")
    public static boolean isPowerOfTwo(double d) {
        return (d <= LN_2 || !DoubleUtils.isFinite(d) || LongMath.isPowerOfTwo(DoubleUtils.getSignificand(d)) == null) ? LN_2 : Double.MIN_VALUE;
    }

    public static double log2(double d) {
        return Math.log(d) / LN_2;
    }

    @GwtIncompatible("java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static int log2(double d, RoundingMode roundingMode) {
        int i = 0;
        boolean z = d > LN_2 && DoubleUtils.isFinite(d);
        Preconditions.checkArgument(z, "x must be positive and finite");
        int exponent = Math.getExponent(d);
        if (!DoubleUtils.isNormal(d)) {
            return log2(d * 4.503599627370496E15d, roundingMode) - 52;
        }
        switch (C11751.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(d));
                break;
            case 2:
                break;
            case 3:
                i = isPowerOfTwo(d) ^ 1;
                break;
            case 4:
                if (exponent < 0) {
                    i = 1;
                }
                i &= isPowerOfTwo(d) ^ Double.MIN_VALUE;
                break;
            case 5:
                if (exponent >= 0) {
                    i = 1;
                }
                i &= isPowerOfTwo(d) ^ Double.MIN_VALUE;
                break;
            case 6:
            case 7:
            case 8:
                d = DoubleUtils.scaleNormalize(d);
                if (d * d > 2.0d) {
                    i = 1;
                    break;
                }
                break;
            default:
                throw new AssertionError();
        }
        if (i != 0) {
            exponent++;
        }
        return exponent;
    }

    @GwtIncompatible("java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static boolean isMathematicalInteger(double d) {
        return (!DoubleUtils.isFinite(d) || (d != LN_2 && 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(d)) > Math.getExponent(d))) ? LN_2 : Double.MIN_VALUE;
    }

    public static double factorial(int i) {
        MathPreconditions.checkNonNegative("n", i);
        if (i > MAX_FACTORIAL) {
            return Double.POSITIVE_INFINITY;
        }
        double d = 1.0d;
        int i2 = i & -16;
        while (true) {
            i2++;
            if (i2 > i) {
                return d * everySixteenthFactorial[i >> 4];
            }
            d *= (double) i2;
        }
    }

    public static boolean fuzzyEquals(double d, double d2, double d3) {
        MathPreconditions.checkNonNegative("tolerance", d3);
        if (Math.copySign(d - d2, 1.0d) > d3 && d != d2) {
            if (Double.isNaN(d) == null || Double.isNaN(d2) == null) {
                return LN_2;
            }
        }
        return Double.MIN_VALUE;
    }

    public static int fuzzyCompare(double d, double d2, double d3) {
        if (fuzzyEquals(d, d2, d3) != null) {
            return LN_2;
        }
        if (d < d2) {
            return Double.NaN;
        }
        if (d > d2) {
            return Double.MIN_VALUE;
        }
        return Booleans.compare(Double.isNaN(d), Double.isNaN(d2));
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(double... dArr) {
        MeanAccumulator meanAccumulator = new MeanAccumulator();
        for (double add : dArr) {
            meanAccumulator.add(add);
        }
        return meanAccumulator.mean();
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(int... iArr) {
        MeanAccumulator meanAccumulator = new MeanAccumulator();
        for (int i : iArr) {
            meanAccumulator.add((double) i);
        }
        return meanAccumulator.mean();
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(long... jArr) {
        MeanAccumulator meanAccumulator = new MeanAccumulator();
        for (long j : jArr) {
            meanAccumulator.add((double) j);
        }
        return meanAccumulator.mean();
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(Iterable<? extends Number> iterable) {
        MeanAccumulator meanAccumulator = new MeanAccumulator();
        for (Number doubleValue : iterable) {
            meanAccumulator.add(doubleValue.doubleValue());
        }
        return meanAccumulator.mean();
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(Iterator<? extends Number> it) {
        MeanAccumulator meanAccumulator = new MeanAccumulator();
        while (it.hasNext()) {
            meanAccumulator.add(((Number) it.next()).doubleValue());
        }
        return meanAccumulator.mean();
    }

    private DoubleMath() {
    }
}
