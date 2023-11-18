package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Supplier;
import java.util.concurrent.atomic.AtomicLong;

@GwtCompatible(emulated = true)
final class LongAddables {
    private static final Supplier<LongAddable> SUPPLIER;

    /* renamed from: com.google.common.cache.LongAddables$1 */
    static class C08541 implements Supplier<LongAddable> {
        C08541() {
        }

        public LongAddable get() {
            return new LongAdder();
        }
    }

    /* renamed from: com.google.common.cache.LongAddables$2 */
    static class C08552 implements Supplier<LongAddable> {
        C08552() {
        }

        public LongAddable get() {
            return new PureJavaLongAddable();
        }
    }

    private static final class PureJavaLongAddable extends AtomicLong implements LongAddable {
        private PureJavaLongAddable() {
        }

        public void increment() {
            getAndIncrement();
        }

        public void add(long j) {
            getAndAdd(j);
        }

        public long sum() {
            return get();
        }
    }

    LongAddables() {
    }

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
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = new com.google.common.cache.LongAdder;	 Catch:{ Throwable -> 0x000b }
        r0.<init>();	 Catch:{ Throwable -> 0x000b }
        r0 = new com.google.common.cache.LongAddables$1;	 Catch:{ Throwable -> 0x000b }
        r0.<init>();	 Catch:{ Throwable -> 0x000b }
        goto L_0x0010;
    L_0x000b:
        r0 = new com.google.common.cache.LongAddables$2;
        r0.<init>();
    L_0x0010:
        SUPPLIER = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.cache.LongAddables.<clinit>():void");
    }

    public static LongAddable create() {
        return (LongAddable) SUPPLIER.get();
    }
}
