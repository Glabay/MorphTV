package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Beta
public final class Uninterruptibles {
    public static void awaitUninterruptibly(java.util.concurrent.CountDownLatch r1) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
    L_0x0001:
        r1.await();	 Catch:{ InterruptedException -> 0x0019, all -> 0x000e }
        if (r0 == 0) goto L_0x000d;
    L_0x0006:
        r1 = java.lang.Thread.currentThread();
        r1.interrupt();
    L_0x000d:
        return;
    L_0x000e:
        r1 = move-exception;
        if (r0 == 0) goto L_0x0018;
    L_0x0011:
        r0 = java.lang.Thread.currentThread();
        r0.interrupt();
    L_0x0018:
        throw r1;
    L_0x0019:
        r0 = 1;
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.awaitUninterruptibly(java.util.concurrent.CountDownLatch):void");
    }

    public static boolean awaitUninterruptibly(java.util.concurrent.CountDownLatch r5, long r6, java.util.concurrent.TimeUnit r8) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
        r6 = r8.toNanos(r6);	 Catch:{ all -> 0x0026 }
        r1 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0026 }
        r8 = 0;
        r3 = r1 + r6;
    L_0x000c:
        r8 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x001c }
        r6 = r5.await(r6, r8);	 Catch:{ InterruptedException -> 0x001c }
        if (r0 == 0) goto L_0x001b;
    L_0x0014:
        r5 = java.lang.Thread.currentThread();
        r5.interrupt();
    L_0x001b:
        return r6;
    L_0x001c:
        r0 = 1;
        r6 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0026 }
        r8 = 0;
        r1 = r3 - r6;
        r6 = r1;
        goto L_0x000c;
    L_0x0026:
        r5 = move-exception;
        if (r0 == 0) goto L_0x0030;
    L_0x0029:
        r6 = java.lang.Thread.currentThread();
        r6.interrupt();
    L_0x0030:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.awaitUninterruptibly(java.util.concurrent.CountDownLatch, long, java.util.concurrent.TimeUnit):boolean");
    }

    public static void joinUninterruptibly(java.lang.Thread r1) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
    L_0x0001:
        r1.join();	 Catch:{ InterruptedException -> 0x0019, all -> 0x000e }
        if (r0 == 0) goto L_0x000d;
    L_0x0006:
        r1 = java.lang.Thread.currentThread();
        r1.interrupt();
    L_0x000d:
        return;
    L_0x000e:
        r1 = move-exception;
        if (r0 == 0) goto L_0x0018;
    L_0x0011:
        r0 = java.lang.Thread.currentThread();
        r0.interrupt();
    L_0x0018:
        throw r1;
    L_0x0019:
        r0 = 1;
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.joinUninterruptibly(java.lang.Thread):void");
    }

    public static <V> V getUninterruptibly(java.util.concurrent.Future<V> r2) throws java.util.concurrent.ExecutionException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
    L_0x0001:
        r1 = r2.get();	 Catch:{ InterruptedException -> 0x001a, all -> 0x000f }
        if (r0 == 0) goto L_0x000e;
    L_0x0007:
        r2 = java.lang.Thread.currentThread();
        r2.interrupt();
    L_0x000e:
        return r1;
    L_0x000f:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0019;
    L_0x0012:
        r0 = java.lang.Thread.currentThread();
        r0.interrupt();
    L_0x0019:
        throw r2;
    L_0x001a:
        r0 = 1;
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(java.util.concurrent.Future):V");
    }

    public static <V> V getUninterruptibly(java.util.concurrent.Future<V> r5, long r6, java.util.concurrent.TimeUnit r8) throws java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
        r6 = r8.toNanos(r6);	 Catch:{ all -> 0x0026 }
        r1 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0026 }
        r8 = 0;
        r3 = r1 + r6;
    L_0x000c:
        r8 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x001c }
        r6 = r5.get(r6, r8);	 Catch:{ InterruptedException -> 0x001c }
        if (r0 == 0) goto L_0x001b;
    L_0x0014:
        r5 = java.lang.Thread.currentThread();
        r5.interrupt();
    L_0x001b:
        return r6;
    L_0x001c:
        r0 = 1;
        r6 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0026 }
        r8 = 0;
        r1 = r3 - r6;
        r6 = r1;
        goto L_0x000c;
    L_0x0026:
        r5 = move-exception;
        if (r0 == 0) goto L_0x0030;
    L_0x0029:
        r6 = java.lang.Thread.currentThread();
        r6.interrupt();
    L_0x0030:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(java.util.concurrent.Future, long, java.util.concurrent.TimeUnit):V");
    }

    public static void joinUninterruptibly(java.lang.Thread r5, long r6, java.util.concurrent.TimeUnit r8) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        com.google.common.base.Preconditions.checkNotNull(r5);
        r0 = 0;
        r6 = r8.toNanos(r6);	 Catch:{ all -> 0x0028 }
        r1 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0028 }
        r8 = 0;
        r3 = r1 + r6;
    L_0x000f:
        r8 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x001e }
        r8.timedJoin(r5, r6);	 Catch:{ InterruptedException -> 0x001e }
        if (r0 == 0) goto L_0x001d;
    L_0x0016:
        r5 = java.lang.Thread.currentThread();
        r5.interrupt();
    L_0x001d:
        return;
    L_0x001e:
        r0 = 1;
        r6 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0028 }
        r8 = 0;
        r1 = r3 - r6;
        r6 = r1;
        goto L_0x000f;
    L_0x0028:
        r5 = move-exception;
        if (r0 == 0) goto L_0x0032;
    L_0x002b:
        r6 = java.lang.Thread.currentThread();
        r6.interrupt();
    L_0x0032:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.joinUninterruptibly(java.lang.Thread, long, java.util.concurrent.TimeUnit):void");
    }

    public static <E> E takeUninterruptibly(java.util.concurrent.BlockingQueue<E> r2) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
    L_0x0001:
        r1 = r2.take();	 Catch:{ InterruptedException -> 0x001a, all -> 0x000f }
        if (r0 == 0) goto L_0x000e;
    L_0x0007:
        r2 = java.lang.Thread.currentThread();
        r2.interrupt();
    L_0x000e:
        return r1;
    L_0x000f:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0019;
    L_0x0012:
        r0 = java.lang.Thread.currentThread();
        r0.interrupt();
    L_0x0019:
        throw r2;
    L_0x001a:
        r0 = 1;
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.takeUninterruptibly(java.util.concurrent.BlockingQueue):E");
    }

    public static <E> void putUninterruptibly(java.util.concurrent.BlockingQueue<E> r1, E r2) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
    L_0x0001:
        r1.put(r2);	 Catch:{ InterruptedException -> 0x0019, all -> 0x000e }
        if (r0 == 0) goto L_0x000d;
    L_0x0006:
        r1 = java.lang.Thread.currentThread();
        r1.interrupt();
    L_0x000d:
        return;
    L_0x000e:
        r1 = move-exception;
        if (r0 == 0) goto L_0x0018;
    L_0x0011:
        r2 = java.lang.Thread.currentThread();
        r2.interrupt();
    L_0x0018:
        throw r1;
    L_0x0019:
        r0 = 1;
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.putUninterruptibly(java.util.concurrent.BlockingQueue, java.lang.Object):void");
    }

    public static void sleepUninterruptibly(long r5, java.util.concurrent.TimeUnit r7) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
        r5 = r7.toNanos(r5);	 Catch:{ all -> 0x0025 }
        r1 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0025 }
        r7 = 0;
        r3 = r1 + r5;
    L_0x000c:
        r7 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x001b }
        r7.sleep(r5);	 Catch:{ InterruptedException -> 0x001b }
        if (r0 == 0) goto L_0x001a;
    L_0x0013:
        r5 = java.lang.Thread.currentThread();
        r5.interrupt();
    L_0x001a:
        return;
    L_0x001b:
        r0 = 1;
        r5 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0025 }
        r7 = 0;
        r1 = r3 - r5;
        r5 = r1;
        goto L_0x000c;
    L_0x0025:
        r5 = move-exception;
        if (r0 == 0) goto L_0x002f;
    L_0x0028:
        r6 = java.lang.Thread.currentThread();
        r6.interrupt();
    L_0x002f:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly(long, java.util.concurrent.TimeUnit):void");
    }

    public static boolean tryAcquireUninterruptibly(Semaphore semaphore, long j, TimeUnit timeUnit) {
        return tryAcquireUninterruptibly(semaphore, 1, j, timeUnit);
    }

    public static boolean tryAcquireUninterruptibly(java.util.concurrent.Semaphore r5, int r6, long r7, java.util.concurrent.TimeUnit r9) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
        r7 = r9.toNanos(r7);	 Catch:{ all -> 0x0026 }
        r1 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0026 }
        r9 = 0;
        r3 = r1 + r7;
    L_0x000c:
        r9 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x001c }
        r7 = r5.tryAcquire(r6, r7, r9);	 Catch:{ InterruptedException -> 0x001c }
        if (r0 == 0) goto L_0x001b;
    L_0x0014:
        r5 = java.lang.Thread.currentThread();
        r5.interrupt();
    L_0x001b:
        return r7;
    L_0x001c:
        r0 = 1;
        r7 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0026 }
        r9 = 0;
        r1 = r3 - r7;
        r7 = r1;
        goto L_0x000c;
    L_0x0026:
        r5 = move-exception;
        if (r0 == 0) goto L_0x0030;
    L_0x0029:
        r6 = java.lang.Thread.currentThread();
        r6.interrupt();
    L_0x0030:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Uninterruptibles.tryAcquireUninterruptibly(java.util.concurrent.Semaphore, int, long, java.util.concurrent.TimeUnit):boolean");
    }

    private Uninterruptibles() {
    }
}
