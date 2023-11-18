package com.google.common.util.concurrent;

import com.google.common.util.concurrent.Futures.ChainingListenableFuture;

class Futures$ChainingListenableFuture$1 implements Runnable {
    final /* synthetic */ ChainingListenableFuture this$0;
    final /* synthetic */ ListenableFuture val$outputFuture;

    Futures$ChainingListenableFuture$1(ChainingListenableFuture chainingListenableFuture, ListenableFuture listenableFuture) {
        this.this$0 = chainingListenableFuture;
        this.val$outputFuture = listenableFuture;
    }

    public void run() {
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
        r3 = this;
        r0 = 0;
        r1 = r3.this$0;	 Catch:{ CancellationException -> 0x0020, ExecutionException -> 0x0014 }
        r2 = r3.val$outputFuture;	 Catch:{ CancellationException -> 0x0020, ExecutionException -> 0x0014 }
        r2 = com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(r2);	 Catch:{ CancellationException -> 0x0020, ExecutionException -> 0x0014 }
        r1.set(r2);	 Catch:{ CancellationException -> 0x0020, ExecutionException -> 0x0014 }
    L_0x000c:
        r1 = r3.this$0;
        com.google.common.util.concurrent.Futures.ChainingListenableFuture.access$302(r1, r0);
        goto L_0x001f;
    L_0x0012:
        r1 = move-exception;
        goto L_0x002c;
    L_0x0014:
        r1 = move-exception;
        r2 = r3.this$0;	 Catch:{ all -> 0x0012 }
        r1 = r1.getCause();	 Catch:{ all -> 0x0012 }
        r2.setException(r1);	 Catch:{ all -> 0x0012 }
        goto L_0x000c;	 Catch:{ all -> 0x0012 }
    L_0x001f:
        return;	 Catch:{ all -> 0x0012 }
    L_0x0020:
        r1 = r3.this$0;	 Catch:{ all -> 0x0012 }
        r2 = 0;	 Catch:{ all -> 0x0012 }
        r1.cancel(r2);	 Catch:{ all -> 0x0012 }
        r1 = r3.this$0;
        com.google.common.util.concurrent.Futures.ChainingListenableFuture.access$302(r1, r0);
        return;
    L_0x002c:
        r2 = r3.this$0;
        com.google.common.util.concurrent.Futures.ChainingListenableFuture.access$302(r2, r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Futures$ChainingListenableFuture$1.run():void");
    }
}
