package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.PrivilegedExceptionAction;
import java.util.Comparator;
import sun.misc.Unsafe;

public final class UnsignedBytes {
    public static final byte MAX_POWER_OF_TWO = Byte.MIN_VALUE;
    public static final byte MAX_VALUE = (byte) -1;
    private static final int UNSIGNED_MASK = 255;

    @VisibleForTesting
    static class LexicographicalComparatorHolder {
        static final Comparator<byte[]> BEST_COMPARATOR = getBestComparator();
        static final String UNSAFE_COMPARATOR_NAME;

        enum PureJavaComparator implements Comparator<byte[]> {
            INSTANCE;

            public int compare(byte[] bArr, byte[] bArr2) {
                int min = Math.min(bArr.length, bArr2.length);
                for (int i = 0; i < min; i++) {
                    int compare = UnsignedBytes.compare(bArr[i], bArr2[i]);
                    if (compare != 0) {
                        return compare;
                    }
                }
                return bArr.length - bArr2.length;
            }
        }

        @VisibleForTesting
        enum UnsafeComparator implements Comparator<byte[]> {
            INSTANCE;
            
            static final boolean BIG_ENDIAN = false;
            static final int BYTE_ARRAY_BASE_OFFSET = 0;
            static final Unsafe theUnsafe = null;

            /* renamed from: com.google.common.primitives.UnsignedBytes$LexicographicalComparatorHolder$UnsafeComparator$1 */
            static class C11781 implements PrivilegedExceptionAction<Unsafe> {
                C11781() {
                }

                public Unsafe run() throws Exception {
                    Class cls = Unsafe.class;
                    for (Field field : cls.getDeclaredFields()) {
                        field.setAccessible(true);
                        Object obj = field.get(null);
                        if (cls.isInstance(obj)) {
                            return (Unsafe) cls.cast(obj);
                        }
                    }
                    throw new NoSuchFieldError("the Unsafe");
                }
            }

            static {
                BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
                theUnsafe = getUnsafe();
                BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
                if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
                    throw new AssertionError();
                }
            }

            private static sun.misc.Unsafe getUnsafe() {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
                /*
                r0 = sun.misc.Unsafe.getUnsafe();	 Catch:{ SecurityException -> 0x0005 }
                return r0;
            L_0x0005:
                r0 = new com.google.common.primitives.UnsignedBytes$LexicographicalComparatorHolder$UnsafeComparator$1;	 Catch:{ PrivilegedActionException -> 0x0011 }
                r0.<init>();	 Catch:{ PrivilegedActionException -> 0x0011 }
                r0 = java.security.AccessController.doPrivileged(r0);	 Catch:{ PrivilegedActionException -> 0x0011 }
                r0 = (sun.misc.Unsafe) r0;	 Catch:{ PrivilegedActionException -> 0x0011 }
                return r0;
            L_0x0011:
                r0 = move-exception;
                r1 = new java.lang.RuntimeException;
                r2 = "Could not initialize intrinsics";
                r0 = r0.getCause();
                r1.<init>(r2, r0);
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.common.primitives.UnsignedBytes.LexicographicalComparatorHolder.UnsafeComparator.getUnsafe():sun.misc.Unsafe");
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public int compare(byte[] r13, byte[] r14) {
                /*
                r12 = this;
                r0 = r13.length;
                r1 = r14.length;
                r0 = java.lang.Math.min(r0, r1);
                r1 = r0 / 8;
                r2 = 0;
            L_0x0009:
                r3 = r1 * 8;
                if (r2 >= r3) goto L_0x004a;
            L_0x000d:
                r3 = theUnsafe;
                r4 = BYTE_ARRAY_BASE_OFFSET;
                r4 = (long) r4;
                r6 = (long) r2;
                r8 = r4 + r6;
                r3 = r3.getLong(r13, r8);
                r5 = theUnsafe;
                r8 = BYTE_ARRAY_BASE_OFFSET;
                r8 = (long) r8;
                r10 = r8 + r6;
                r5 = r5.getLong(r14, r10);
                r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
                if (r7 == 0) goto L_0x0047;
            L_0x0028:
                r13 = BIG_ENDIAN;
                if (r13 == 0) goto L_0x0031;
            L_0x002c:
                r13 = com.google.common.primitives.UnsignedLongs.compare(r3, r5);
                return r13;
            L_0x0031:
                r13 = r3 ^ r5;
                r13 = java.lang.Long.numberOfTrailingZeros(r13);
                r13 = r13 & -8;
                r0 = r3 >>> r13;
                r2 = 255; // 0xff float:3.57E-43 double:1.26E-321;
                r7 = r0 & r2;
                r13 = r5 >>> r13;
                r0 = r13 & r2;
                r13 = r7 - r0;
                r13 = (int) r13;
                return r13;
            L_0x0047:
                r2 = r2 + 8;
                goto L_0x0009;
            L_0x004a:
                if (r3 >= r0) goto L_0x005a;
            L_0x004c:
                r1 = r13[r3];
                r2 = r14[r3];
                r1 = com.google.common.primitives.UnsignedBytes.compare(r1, r2);
                if (r1 == 0) goto L_0x0057;
            L_0x0056:
                return r1;
            L_0x0057:
                r3 = r3 + 1;
                goto L_0x004a;
            L_0x005a:
                r13 = r13.length;
                r14 = r14.length;
                r13 = r13 - r14;
                return r13;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.common.primitives.UnsignedBytes.LexicographicalComparatorHolder.UnsafeComparator.compare(byte[], byte[]):int");
            }
        }

        LexicographicalComparatorHolder() {
        }

        static {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LexicographicalComparatorHolder.class.getName());
            stringBuilder.append("$UnsafeComparator");
            UNSAFE_COMPARATOR_NAME = stringBuilder.toString();
        }

        static java.util.Comparator<byte[]> getBestComparator() {
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
            r0 = UNSAFE_COMPARATOR_NAME;	 Catch:{ Throwable -> 0x0010 }
            r0 = java.lang.Class.forName(r0);	 Catch:{ Throwable -> 0x0010 }
            r0 = r0.getEnumConstants();	 Catch:{ Throwable -> 0x0010 }
            r1 = 0;	 Catch:{ Throwable -> 0x0010 }
            r0 = r0[r1];	 Catch:{ Throwable -> 0x0010 }
            r0 = (java.util.Comparator) r0;	 Catch:{ Throwable -> 0x0010 }
            return r0;
        L_0x0010:
            r0 = com.google.common.primitives.UnsignedBytes.lexicographicalComparatorJavaImpl();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.primitives.UnsignedBytes.LexicographicalComparatorHolder.getBestComparator():java.util.Comparator<byte[]>");
        }
    }

    public static int toInt(byte b) {
        return b & 255;
    }

    private UnsignedBytes() {
    }

    public static byte checkedCast(long j) {
        if ((j >> 8) == 0) {
            return (byte) ((int) j);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Out of range: ");
        stringBuilder.append(j);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static byte saturatedCast(long j) {
        if (j > ((long) toInt((byte) -1))) {
            return (byte) -1;
        }
        return j < 0 ? 0 : (byte) ((int) j);
    }

    public static int compare(byte b, byte b2) {
        return toInt(b) - toInt(b2);
    }

    public static byte min(byte... bArr) {
        Preconditions.checkArgument(bArr.length > 0);
        int toInt = toInt(bArr[0]);
        for (int i = 1; i < bArr.length; i++) {
            int toInt2 = toInt(bArr[i]);
            if (toInt2 < toInt) {
                toInt = toInt2;
            }
        }
        return (byte) toInt;
    }

    public static byte max(byte... bArr) {
        Preconditions.checkArgument(bArr.length > 0);
        int toInt = toInt(bArr[0]);
        for (int i = 1; i < bArr.length; i++) {
            int toInt2 = toInt(bArr[i]);
            if (toInt2 > toInt) {
                toInt = toInt2;
            }
        }
        return (byte) toInt;
    }

    @Beta
    public static String toString(byte b) {
        return toString(b, 10);
    }

    @Beta
    public static String toString(byte b, int i) {
        boolean z = i >= 2 && i <= 36;
        Preconditions.checkArgument(z, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", new Object[]{Integer.valueOf(i)});
        return Integer.toString(toInt(b), i);
    }

    @Beta
    public static byte parseUnsignedByte(String str) {
        return parseUnsignedByte(str, 10);
    }

    @Beta
    public static byte parseUnsignedByte(String str, int i) {
        str = Integer.parseInt((String) Preconditions.checkNotNull(str), i);
        if ((str >> 8) == 0) {
            return (byte) str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("out of range: ");
        stringBuilder.append(str);
        throw new NumberFormatException(stringBuilder.toString());
    }

    public static String join(String str, byte... bArr) {
        Preconditions.checkNotNull(str);
        if (bArr.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(bArr.length * (str.length() + 3));
        stringBuilder.append(toInt(bArr[0]));
        for (int i = 1; i < bArr.length; i++) {
            stringBuilder.append(str);
            stringBuilder.append(toString(bArr[i]));
        }
        return stringBuilder.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparatorHolder.BEST_COMPARATOR;
    }

    @VisibleForTesting
    static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
        return PureJavaComparator.INSTANCE;
    }
}
