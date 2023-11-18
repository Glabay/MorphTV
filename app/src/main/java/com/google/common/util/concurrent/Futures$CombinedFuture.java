package com.google.common.util.concurrent;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

class Futures$CombinedFuture<V, C> extends AbstractFuture<C> {
    private static final Logger logger = Logger.getLogger(Futures$CombinedFuture.class.getName());
    final boolean allMustSucceed;
    Futures$FutureCombiner<V, C> combiner;
    ImmutableCollection<? extends ListenableFuture<? extends V>> futures;
    final AtomicInteger remaining;
    Set<Throwable> seenExceptions;
    final Object seenExceptionsLock = new Object();
    List<Optional<V>> values;

    /* renamed from: com.google.common.util.concurrent.Futures$CombinedFuture$1 */
    class C12251 implements Runnable {
        C12251() {
        }

        public void run() {
            if (Futures$CombinedFuture.this.isCancelled()) {
                Iterator it = Futures$CombinedFuture.this.futures.iterator();
                while (it.hasNext()) {
                    ((ListenableFuture) it.next()).cancel(Futures$CombinedFuture.this.wasInterrupted());
                }
            }
            Futures$CombinedFuture.this.futures = null;
            Futures$CombinedFuture.this.values = null;
            Futures$CombinedFuture.this.combiner = null;
        }
    }

    Futures$CombinedFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> immutableCollection, boolean z, Executor executor, Futures$FutureCombiner<V, C> futures$FutureCombiner) {
        this.futures = immutableCollection;
        this.allMustSucceed = z;
        this.remaining = new AtomicInteger(immutableCollection.size());
        this.combiner = futures$FutureCombiner;
        this.values = Lists.newArrayListWithCapacity(immutableCollection.size());
        init(executor);
    }

    protected void init(Executor executor) {
        addListener(new C12251(), MoreExecutors.directExecutor());
        if (this.futures.isEmpty()) {
            set(this.combiner.combine(ImmutableList.of()));
            return;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.futures.size(); i2++) {
            this.values.add(null);
        }
        Iterator it = this.futures.iterator();
        while (it.hasNext()) {
            final ListenableFuture listenableFuture = (ListenableFuture) it.next();
            int i3 = i + 1;
            listenableFuture.addListener(new Runnable() {
                public void run() {
                    Futures$CombinedFuture.this.setOneValue(i, listenableFuture);
                }
            }, executor);
            i = i3;
        }
    }

    private void setExceptionAndMaybeLog(Throwable th) {
        boolean exception;
        boolean add;
        if (this.allMustSucceed) {
            exception = super.setException(th);
            synchronized (this.seenExceptionsLock) {
                if (this.seenExceptions == null) {
                    this.seenExceptions = Sets.newHashSet();
                }
                add = this.seenExceptions.add(th);
            }
        } else {
            exception = false;
            add = true;
        }
        if ((th instanceof Error) || (this.allMustSucceed && !r0 && r2)) {
            logger.log(Level.SEVERE, "input future failed.", th);
        }
    }

    private void setOneValue(int r6, java.util.concurrent.Future<? extends V> r7) {
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
        r5 = this;
        r0 = r5.values;
        r1 = r5.isDone();
        r2 = 1;
        r3 = 0;
        if (r1 != 0) goto L_0x000c;
    L_0x000a:
        if (r0 != 0) goto L_0x001f;
    L_0x000c:
        r1 = r5.allMustSucceed;
        if (r1 != 0) goto L_0x0019;
    L_0x0010:
        r1 = r5.isCancelled();
        if (r1 == 0) goto L_0x0017;
    L_0x0016:
        goto L_0x0019;
    L_0x0017:
        r1 = 0;
        goto L_0x001a;
    L_0x0019:
        r1 = 1;
    L_0x001a:
        r4 = "Future was done before all dependencies completed";
        com.google.common.base.Preconditions.checkState(r1, r4);
    L_0x001f:
        r1 = r7.isDone();	 Catch:{ CancellationException -> 0x009b, ExecutionException -> 0x007b, Throwable -> 0x005f }
        r4 = "Tried to set value from future which is not done";	 Catch:{ CancellationException -> 0x009b, ExecutionException -> 0x007b, Throwable -> 0x005f }
        com.google.common.base.Preconditions.checkState(r1, r4);	 Catch:{ CancellationException -> 0x009b, ExecutionException -> 0x007b, Throwable -> 0x005f }
        r7 = com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(r7);	 Catch:{ CancellationException -> 0x009b, ExecutionException -> 0x007b, Throwable -> 0x005f }
        if (r0 == 0) goto L_0x0035;	 Catch:{ CancellationException -> 0x009b, ExecutionException -> 0x007b, Throwable -> 0x005f }
    L_0x002e:
        r7 = com.google.common.base.Optional.fromNullable(r7);	 Catch:{ CancellationException -> 0x009b, ExecutionException -> 0x007b, Throwable -> 0x005f }
        r0.set(r6, r7);	 Catch:{ CancellationException -> 0x009b, ExecutionException -> 0x007b, Throwable -> 0x005f }
    L_0x0035:
        r6 = r5.remaining;
        r6 = r6.decrementAndGet();
        if (r6 < 0) goto L_0x003e;
    L_0x003d:
        goto L_0x003f;
    L_0x003e:
        r2 = 0;
    L_0x003f:
        r7 = "Less than 0 remaining futures";
        com.google.common.base.Preconditions.checkState(r2, r7);
        if (r6 != 0) goto L_0x00ba;
    L_0x0046:
        r6 = r5.combiner;
        if (r6 == 0) goto L_0x0055;
    L_0x004a:
        if (r0 == 0) goto L_0x0055;
    L_0x004c:
        r6 = r6.combine(r0);
        r5.set(r6);
        goto L_0x00ba;
    L_0x0055:
        r6 = r5.isDone();
        com.google.common.base.Preconditions.checkState(r6);
        goto L_0x00ba;
    L_0x005d:
        r6 = move-exception;
        goto L_0x00bb;
    L_0x005f:
        r6 = move-exception;
        r5.setExceptionAndMaybeLog(r6);	 Catch:{ all -> 0x005d }
        r6 = r5.remaining;
        r6 = r6.decrementAndGet();
        if (r6 < 0) goto L_0x006c;
    L_0x006b:
        goto L_0x006d;
    L_0x006c:
        r2 = 0;
    L_0x006d:
        r7 = "Less than 0 remaining futures";
        com.google.common.base.Preconditions.checkState(r2, r7);
        if (r6 != 0) goto L_0x00ba;
    L_0x0074:
        r6 = r5.combiner;
        if (r6 == 0) goto L_0x0055;
    L_0x0078:
        if (r0 == 0) goto L_0x0055;
    L_0x007a:
        goto L_0x004c;
    L_0x007b:
        r6 = move-exception;
        r6 = r6.getCause();	 Catch:{ all -> 0x005d }
        r5.setExceptionAndMaybeLog(r6);	 Catch:{ all -> 0x005d }
        r6 = r5.remaining;
        r6 = r6.decrementAndGet();
        if (r6 < 0) goto L_0x008c;
    L_0x008b:
        goto L_0x008d;
    L_0x008c:
        r2 = 0;
    L_0x008d:
        r7 = "Less than 0 remaining futures";
        com.google.common.base.Preconditions.checkState(r2, r7);
        if (r6 != 0) goto L_0x00ba;
    L_0x0094:
        r6 = r5.combiner;
        if (r6 == 0) goto L_0x0055;
    L_0x0098:
        if (r0 == 0) goto L_0x0055;
    L_0x009a:
        goto L_0x004c;
    L_0x009b:
        r6 = r5.allMustSucceed;	 Catch:{ all -> 0x005d }
        if (r6 == 0) goto L_0x00a2;	 Catch:{ all -> 0x005d }
    L_0x009f:
        r5.cancel(r3);	 Catch:{ all -> 0x005d }
    L_0x00a2:
        r6 = r5.remaining;
        r6 = r6.decrementAndGet();
        if (r6 < 0) goto L_0x00ab;
    L_0x00aa:
        goto L_0x00ac;
    L_0x00ab:
        r2 = 0;
    L_0x00ac:
        r7 = "Less than 0 remaining futures";
        com.google.common.base.Preconditions.checkState(r2, r7);
        if (r6 != 0) goto L_0x00ba;
    L_0x00b3:
        r6 = r5.combiner;
        if (r6 == 0) goto L_0x0055;
    L_0x00b7:
        if (r0 == 0) goto L_0x0055;
    L_0x00b9:
        goto L_0x004c;
    L_0x00ba:
        return;
    L_0x00bb:
        r7 = r5.remaining;
        r7 = r7.decrementAndGet();
        if (r7 < 0) goto L_0x00c4;
    L_0x00c3:
        goto L_0x00c5;
    L_0x00c4:
        r2 = 0;
    L_0x00c5:
        r1 = "Less than 0 remaining futures";
        com.google.common.base.Preconditions.checkState(r2, r1);
        if (r7 != 0) goto L_0x00e1;
    L_0x00cc:
        r7 = r5.combiner;
        if (r7 == 0) goto L_0x00da;
    L_0x00d0:
        if (r0 == 0) goto L_0x00da;
    L_0x00d2:
        r7 = r7.combine(r0);
        r5.set(r7);
        goto L_0x00e1;
    L_0x00da:
        r7 = r5.isDone();
        com.google.common.base.Preconditions.checkState(r7);
    L_0x00e1:
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Futures$CombinedFuture.setOneValue(int, java.util.concurrent.Future):void");
    }
}
