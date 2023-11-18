package com.google.common.cache;

import de.timroes.axmlrpc.XMLRPCClient;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import sun.misc.Unsafe;

abstract class Striped64 extends Number {
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;
    static final Random rng = new Random();
    static final ThreadLocal<int[]> threadHashCode = new ThreadLocal();
    volatile transient long base;
    volatile transient int busy;
    volatile transient Cell[] cells;

    /* renamed from: com.google.common.cache.Striped64$1 */
    static class C08631 implements PrivilegedExceptionAction<Unsafe> {
        C08631() {
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

    static final class Cell {
        private static final Unsafe UNSAFE;
        private static final long valueOffset;
        volatile long p0;
        volatile long p1;
        volatile long p2;
        volatile long p3;
        volatile long p4;
        volatile long p5;
        volatile long p6;
        volatile long q0;
        volatile long q1;
        volatile long q2;
        volatile long q3;
        volatile long q4;
        volatile long q5;
        volatile long q6;
        volatile long value;

        Cell(long j) {
            this.value = j;
        }

        final boolean cas(long j, long j2) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, j, j2);
        }

        static {
            try {
                UNSAFE = Striped64.getUnsafe();
                valueOffset = UNSAFE.objectFieldOffset(Cell.class.getDeclaredField(XMLRPCClient.VALUE));
            } catch (Throwable e) {
                throw new Error(e);
            }
        }
    }

    abstract long fn(long j, long j2);

    static {
        try {
            UNSAFE = getUnsafe();
            Class cls = Striped64.class;
            baseOffset = UNSAFE.objectFieldOffset(cls.getDeclaredField("base"));
            busyOffset = UNSAFE.objectFieldOffset(cls.getDeclaredField("busy"));
        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    Striped64() {
    }

    final boolean casBase(long j, long j2) {
        return UNSAFE.compareAndSwapLong(this, baseOffset, j, j2);
    }

    final boolean casBusy() {
        return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
    }

    final void retryUpdate(long j, int[] iArr, boolean z) {
        int i;
        int[] iArr2;
        Throwable th;
        Striped64 striped64 = this;
        long j2 = j;
        int i2 = 0;
        if (iArr == null) {
            Object obj = new int[1];
            threadHashCode.set(obj);
            int nextInt = rng.nextInt();
            if (nextInt == 0) {
                nextInt = 1;
            }
            obj[0] = nextInt;
            Object obj2 = obj;
            i = nextInt;
            iArr2 = obj2;
        } else {
            i = iArr[0];
            iArr2 = iArr;
        }
        boolean z2 = z;
        while (true) {
            Object obj3 = null;
            while (true) {
                int length;
                int i3;
                Cell[] cellArr = striped64.cells;
                if (cellArr != null) {
                    length = cellArr.length;
                    if (length > 0) {
                        Cell cell = cellArr[(length - 1) & i];
                        if (cell != null) {
                            if (z2) {
                                long j3 = cell.value;
                                if (!cell.cas(j3, fn(j3, j2))) {
                                    if (length < NCPU) {
                                        if (striped64.cells == cellArr) {
                                            if (obj3 == null) {
                                                obj3 = 1;
                                            } else if (striped64.busy == 0 && casBusy()) {
                                                try {
                                                    break;
                                                } catch (Throwable th2) {
                                                    i3 = 0;
                                                    th = th2;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    return;
                                }
                            }
                            z2 = true;
                            i3 = (i << 13) ^ i;
                            i3 ^= i3 >>> 17;
                            i3 ^= i3 << 5;
                            iArr2[0] = i3;
                            i = i3;
                            i2 = 0;
                        } else if (striped64.busy == 0) {
                            Cell cell2 = new Cell(j2);
                            if (striped64.busy == 0 && casBusy()) {
                                try {
                                    Object obj4;
                                    Cell[] cellArr2 = striped64.cells;
                                    if (cellArr2 != null) {
                                        int length2 = cellArr2.length;
                                        if (length2 > 0) {
                                            length2 = (length2 - 1) & i;
                                            if (cellArr2[length2] == null) {
                                                cellArr2[length2] = cell2;
                                                obj4 = 1;
                                                striped64.busy = i2;
                                                if (obj4 != null) {
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    obj4 = null;
                                    striped64.busy = i2;
                                    if (obj4 != null) {
                                        return;
                                    }
                                } catch (Throwable th22) {
                                    th = th22;
                                    striped64.busy = i2;
                                }
                            }
                        }
                        obj3 = null;
                        i3 = (i << 13) ^ i;
                        i3 ^= i3 >>> 17;
                        i3 ^= i3 << 5;
                        iArr2[0] = i3;
                        i = i3;
                        i2 = 0;
                    }
                }
                if (striped64.busy == 0 && striped64.cells == cellArr && casBusy()) {
                    try {
                        Object obj5;
                        if (striped64.cells == cellArr) {
                            try {
                                Cell[] cellArr3 = new Cell[2];
                                cellArr3[i & 1] = new Cell(j2);
                                striped64.cells = cellArr3;
                                i3 = 0;
                                obj5 = 1;
                            } catch (Throwable th222) {
                                th = th222;
                                i3 = 0;
                            }
                        } else {
                            i3 = 0;
                            obj5 = null;
                        }
                        striped64.busy = i3;
                        if (obj5 != null) {
                            return;
                        }
                        i2 = 0;
                    } catch (Throwable th2222) {
                        i3 = 0;
                        th = th2222;
                    }
                } else {
                    long j4 = striped64.base;
                    if (casBase(j4, fn(j4, j2))) {
                        return;
                    }
                    i2 = 0;
                }
            }
            if (striped64.cells == cellArr) {
                try {
                    cellArr3 = new Cell[(length << 1)];
                    for (i2 = 0; i2 < length; i2++) {
                        cellArr3[i2] = cellArr[i2];
                    }
                    striped64.cells = cellArr3;
                } catch (Throwable th22222) {
                    th = th22222;
                    i3 = 0;
                }
            }
            striped64.busy = 0;
            i2 = 0;
        }
        striped64.busy = i3;
        throw th;
        striped64.busy = i3;
        throw th;
    }

    final void internalReset(long j) {
        Cell[] cellArr = this.cells;
        this.base = j;
        if (cellArr != null) {
            for (Cell cell : cellArr) {
                if (cell != null) {
                    cell.value = j;
                }
            }
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
        r0 = new com.google.common.cache.Striped64$1;	 Catch:{ PrivilegedActionException -> 0x0011 }
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
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.cache.Striped64.getUnsafe():sun.misc.Unsafe");
    }
}
