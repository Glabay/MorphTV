package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;

final class Futures$WrappedCombiner<T> implements Callable<T> {
    final Callable<T> delegate;
    Futures$CombinerFuture<T> outputFuture;

    Futures$WrappedCombiner(Callable<T> callable) {
        this.delegate = (Callable) Preconditions.checkNotNull(callable);
    }

    public T call() throws java.lang.Exception {
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
        r2 = this;
        r0 = r2.delegate;	 Catch:{ ExecutionException -> 0x000e, CancellationException -> 0x0007 }
        r0 = r0.call();	 Catch:{ ExecutionException -> 0x000e, CancellationException -> 0x0007 }
        return r0;
    L_0x0007:
        r0 = r2.outputFuture;
        r1 = 0;
        r0.cancel(r1);
        goto L_0x0018;
    L_0x000e:
        r0 = move-exception;
        r1 = r2.outputFuture;
        r0 = r0.getCause();
        r1.setException(r0);
    L_0x0018:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Futures$WrappedCombiner.call():T");
    }
}
