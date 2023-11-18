package com.google.common.math;

import com.google.android.exoplayer2.extractor.ogg.DefaultOggSeeker;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.RoundingMode;

@GwtCompatible(emulated = true)
public final class IntMath {
    @VisibleForTesting
    static final int FLOOR_SQRT_MAX_INT = 46340;
    @VisibleForTesting
    static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
    @VisibleForTesting
    static int[] biggestBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33};
    private static final int[] factorials = new int[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};
    @VisibleForTesting
    static final int[] halfPowersOf10 = new int[]{3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE};
    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros = new byte[]{(byte) 9, (byte) 9, (byte) 9, (byte) 8, (byte) 8, (byte) 8, (byte) 7, (byte) 7, (byte) 7, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 4, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
    @VisibleForTesting
    static final int[] powersOf10 = new int[]{1, 10, 100, 1000, 10000, DefaultOggSeeker.MATCH_BYTE_RANGE, 1000000, 10000000, 100000000, 1000000000};

    /* renamed from: com.google.common.math.IntMath$1 */
    static /* synthetic */ class C11761 {
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
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.IntMath.1.<clinit>():void");
        }
    }

    public static boolean isPowerOfTwo(int i) {
        int i2 = 0;
        int i3 = i > 0 ? 1 : 0;
        if ((i & (i - 1)) == 0) {
            i2 = 1;
        }
        return i3 & i2;
    }

    @VisibleForTesting
    static int lessThanBranchFree(int i, int i2) {
        return (((i - i2) ^ -1) ^ -1) >>> 31;
    }

    public static int mean(int i, int i2) {
        return (i & i2) + ((i ^ i2) >> 1);
    }

    public static int log2(int i, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", i);
        switch (C11761.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(i));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return 32 - Integer.numberOfLeadingZeros(i - 1);
            case 6:
            case 7:
            case 8:
                roundingMode = Integer.numberOfLeadingZeros(i);
                return (31 - roundingMode) + lessThanBranchFree(MAX_POWER_OF_SQRT2_UNSIGNED >>> roundingMode, i);
            default:
                throw new AssertionError();
        }
        return 31 - Integer.numberOfLeadingZeros(i);
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int log10(int i, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", i);
        int log10Floor = log10Floor(i);
        int i2 = powersOf10[log10Floor];
        switch (C11761.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(i == i2 ? 1 : 0);
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return log10Floor + lessThanBranchFree(i2, i);
            case 6:
            case 7:
            case 8:
                return log10Floor + lessThanBranchFree(halfPowersOf10[log10Floor], i);
            default:
                throw new AssertionError();
        }
        return log10Floor;
    }

    private static int log10Floor(int i) {
        byte b = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(i)];
        return b - lessThanBranchFree(i, powersOf10[b]);
    }

    @GwtIncompatible("failing tests")
    public static int pow(int i, int i2) {
        MathPreconditions.checkNonNegative("exponent", i2);
        int i3 = 0;
        int i4 = 1;
        switch (i) {
            case -2:
                if (i2 >= 32) {
                    return 0;
                }
                return (i2 & 1) == 0 ? 1 << i2 : -(1 << i2);
            case -1:
                if ((i2 & 1) != 0) {
                    i4 = -1;
                }
                return i4;
            case 0:
                if (i2 == 0) {
                    i3 = 1;
                }
                return i3;
            case 1:
                return 1;
            case 2:
                if (i2 < 32) {
                    i3 = 1 << i2;
                }
                return i3;
            default:
                int i5 = i;
                i = 1;
                while (true) {
                    switch (i2) {
                        case 0:
                            return i;
                        case 1:
                            return i5 * i;
                        default:
                            i *= (i2 & 1) == 0 ? 1 : i5;
                            i5 *= i5;
                            i2 >>= 1;
                    }
                }
        }
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int sqrt(int i, RoundingMode roundingMode) {
        MathPreconditions.checkNonNegative("x", i);
        int sqrtFloor = sqrtFloor(i);
        switch (C11761.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(sqrtFloor * sqrtFloor == i ? 1 : 0);
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, i);
            case 6:
            case 7:
            case 8:
                return sqrtFloor + lessThanBranchFree((sqrtFloor * sqrtFloor) + sqrtFloor, i);
            default:
                throw new AssertionError();
        }
        return sqrtFloor;
    }

    private static int sqrtFloor(int i) {
        return (int) Math.sqrt((double) i);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int divide(int r5, int r6, java.math.RoundingMode r7) {
        /*
        com.google.common.base.Preconditions.checkNotNull(r7);
        if (r6 != 0) goto L_0x000d;
    L_0x0005:
        r5 = new java.lang.ArithmeticException;
        r6 = "/ by zero";
        r5.<init>(r6);
        throw r5;
    L_0x000d:
        r0 = r5 / r6;
        r1 = r6 * r0;
        r1 = r5 - r1;
        if (r1 != 0) goto L_0x0016;
    L_0x0015:
        return r0;
    L_0x0016:
        r5 = r5 ^ r6;
        r5 = r5 >> 31;
        r2 = 1;
        r5 = r5 | r2;
        r3 = com.google.common.math.IntMath.C11761.$SwitchMap$java$math$RoundingMode;
        r4 = r7.ordinal();
        r3 = r3[r4];
        r4 = 0;
        switch(r3) {
            case 1: goto L_0x0058;
            case 2: goto L_0x005f;
            case 3: goto L_0x0055;
            case 4: goto L_0x0060;
            case 5: goto L_0x0052;
            case 6: goto L_0x002d;
            case 7: goto L_0x002d;
            case 8: goto L_0x002d;
            default: goto L_0x0027;
        };
    L_0x0027:
        r5 = new java.lang.AssertionError;
        r5.<init>();
        throw r5;
    L_0x002d:
        r1 = java.lang.Math.abs(r1);
        r6 = java.lang.Math.abs(r6);
        r6 = r6 - r1;
        r1 = r1 - r6;
        if (r1 != 0) goto L_0x004f;
    L_0x0039:
        r6 = java.math.RoundingMode.HALF_UP;
        if (r7 == r6) goto L_0x0060;
    L_0x003d:
        r6 = java.math.RoundingMode.HALF_EVEN;
        if (r7 != r6) goto L_0x0043;
    L_0x0041:
        r6 = 1;
        goto L_0x0044;
    L_0x0043:
        r6 = 0;
    L_0x0044:
        r7 = r0 & 1;
        if (r7 == 0) goto L_0x004a;
    L_0x0048:
        r7 = 1;
        goto L_0x004b;
    L_0x004a:
        r7 = 0;
    L_0x004b:
        r6 = r6 & r7;
        if (r6 == 0) goto L_0x005f;
    L_0x004e:
        goto L_0x0060;
    L_0x004f:
        if (r1 <= 0) goto L_0x005f;
    L_0x0051:
        goto L_0x0060;
    L_0x0052:
        if (r5 <= 0) goto L_0x005f;
    L_0x0054:
        goto L_0x0060;
    L_0x0055:
        if (r5 >= 0) goto L_0x005f;
    L_0x0057:
        goto L_0x0060;
    L_0x0058:
        if (r1 != 0) goto L_0x005b;
    L_0x005a:
        goto L_0x005c;
    L_0x005b:
        r2 = 0;
    L_0x005c:
        com.google.common.math.MathPreconditions.checkRoundingUnnecessary(r2);
    L_0x005f:
        r2 = 0;
    L_0x0060:
        if (r2 == 0) goto L_0x0063;
    L_0x0062:
        r0 = r0 + r5;
    L_0x0063:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.IntMath.divide(int, int, java.math.RoundingMode):int");
    }

    public static int mod(int i, int i2) {
        if (i2 <= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Modulus ");
            stringBuilder.append(i2);
            stringBuilder.append(" must be > 0");
            throw new ArithmeticException(stringBuilder.toString());
        }
        i %= i2;
        return i >= 0 ? i : i + i2;
    }

    public static int gcd(int i, int i2) {
        MathPreconditions.checkNonNegative("a", i);
        MathPreconditions.checkNonNegative("b", i2);
        if (i == 0) {
            return i2;
        }
        if (i2 == 0) {
            return i;
        }
        int numberOfTrailingZeros = Integer.numberOfTrailingZeros(i);
        i >>= numberOfTrailingZeros;
        int numberOfTrailingZeros2 = Integer.numberOfTrailingZeros(i2);
        i2 >>= numberOfTrailingZeros2;
        while (i != i2) {
            i -= i2;
            int i3 = (i >> 31) & i;
            i = (i - i3) - i3;
            i2 += i3;
            i >>= Integer.numberOfTrailingZeros(i);
        }
        return i << Math.min(numberOfTrailingZeros, numberOfTrailingZeros2);
    }

    public static int checkedAdd(int i, int i2) {
        long j = ((long) i) + ((long) i2);
        i = (int) j;
        MathPreconditions.checkNoOverflow(j == ((long) i) ? 1 : 0);
        return i;
    }

    public static int checkedSubtract(int i, int i2) {
        long j = ((long) i) - ((long) i2);
        i = (int) j;
        MathPreconditions.checkNoOverflow(j == ((long) i) ? 1 : 0);
        return i;
    }

    public static int checkedMultiply(int i, int i2) {
        long j = ((long) i) * ((long) i2);
        i = (int) j;
        MathPreconditions.checkNoOverflow(j == ((long) i) ? 1 : 0);
        return i;
    }

    public static int checkedPow(int i, int i2) {
        MathPreconditions.checkNonNegative("exponent", i2);
        int i3 = -1;
        boolean z = false;
        switch (i) {
            case -2:
                if (i2 < 32) {
                    z = true;
                }
                MathPreconditions.checkNoOverflow(z);
                return (i2 & 1) == 0 ? 1 << i2 : -1 << i2;
            case -1:
                if ((i2 & 1) == 0) {
                    i3 = 1;
                }
                return i3;
            case 0:
                int i4;
                if (i2 == 0) {
                    i4 = 1;
                }
                return i4;
            case 1:
                return 1;
            case 2:
                if (i2 < 31) {
                    z = true;
                }
                MathPreconditions.checkNoOverflow(z);
                return 1 << i2;
            default:
                i3 = i;
                i = 1;
                while (true) {
                    switch (i2) {
                        case 0:
                            return i;
                        case 1:
                            return checkedMultiply(i, i3);
                        default:
                            if ((i2 & 1) != 0) {
                                i = checkedMultiply(i, i3);
                            }
                            i2 >>= 1;
                            if (i2 > 0) {
                                MathPreconditions.checkNoOverflow((-46340 <= i3 ? 1 : 0) & (i3 <= FLOOR_SQRT_MAX_INT ? 1 : 0));
                                i3 *= i3;
                            }
                    }
                }
        }
    }

    public static int factorial(int i) {
        MathPreconditions.checkNonNegative("n", i);
        return i < factorials.length ? factorials[i] : Integer.MAX_VALUE;
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int binomial(int i, int i2) {
        MathPreconditions.checkNonNegative("n", i);
        MathPreconditions.checkNonNegative("k", i2);
        int i3 = 0;
        Preconditions.checkArgument(i2 <= i, "k (%s) > n (%s)", new Object[]{Integer.valueOf(i2), Integer.valueOf(i)});
        if (i2 > (i >> 1)) {
            i2 = i - i2;
        }
        if (i2 < biggestBinomials.length) {
            if (i <= biggestBinomials[i2]) {
                switch (i2) {
                    case 0:
                        return 1;
                    case 1:
                        return i;
                    default:
                        long j = 1;
                        while (i3 < i2) {
                            i3++;
                            j = (j * ((long) (i - i3))) / ((long) i3);
                        }
                        return (int) j;
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private IntMath() {
    }
}
