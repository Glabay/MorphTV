package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public final class Queues {
    private Queues() {
    }

    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int i) {
        return new ArrayBlockingQueue(i);
    }

    public static <E> ArrayDeque<E> newArrayDeque() {
        return new ArrayDeque();
    }

    public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new ArrayDeque(Collections2.cast(iterable));
        }
        Object arrayDeque = new ArrayDeque();
        Iterables.addAll(arrayDeque, iterable);
        return arrayDeque;
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
        return new ConcurrentLinkedQueue();
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new ConcurrentLinkedQueue(Collections2.cast(iterable));
        }
        Object concurrentLinkedQueue = new ConcurrentLinkedQueue();
        Iterables.addAll(concurrentLinkedQueue, iterable);
        return concurrentLinkedQueue;
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
        return new LinkedBlockingDeque();
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int i) {
        return new LinkedBlockingDeque(i);
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new LinkedBlockingDeque(Collections2.cast(iterable));
        }
        Object linkedBlockingDeque = new LinkedBlockingDeque();
        Iterables.addAll(linkedBlockingDeque, iterable);
        return linkedBlockingDeque;
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue();
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int i) {
        return new LinkedBlockingQueue(i);
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new LinkedBlockingQueue(Collections2.cast(iterable));
        }
        Object linkedBlockingQueue = new LinkedBlockingQueue();
        Iterables.addAll(linkedBlockingQueue, iterable);
        return linkedBlockingQueue;
    }

    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
        return new PriorityBlockingQueue();
    }

    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new PriorityBlockingQueue(Collections2.cast(iterable));
        }
        Object priorityBlockingQueue = new PriorityBlockingQueue();
        Iterables.addAll(priorityBlockingQueue, iterable);
        return priorityBlockingQueue;
    }

    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
        return new PriorityQueue();
    }

    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new PriorityQueue(Collections2.cast(iterable));
        }
        Object priorityQueue = new PriorityQueue();
        Iterables.addAll(priorityQueue, iterable);
        return priorityQueue;
    }

    public static <E> SynchronousQueue<E> newSynchronousQueue() {
        return new SynchronousQueue();
    }

    @Beta
    public static <E> int drain(BlockingQueue<E> blockingQueue, Collection<? super E> collection, int i, long j, TimeUnit timeUnit) throws InterruptedException {
        Preconditions.checkNotNull(collection);
        long nanoTime = System.nanoTime() + timeUnit.toNanos(j);
        j = null;
        while (j < i) {
            j += blockingQueue.drainTo(collection, i - j);
            if (j < i) {
                Object poll = blockingQueue.poll(nanoTime - System.nanoTime(), TimeUnit.NANOSECONDS);
                if (poll == null) {
                    break;
                }
                collection.add(poll);
                j++;
            }
        }
        return j;
    }

    @com.google.common.annotations.Beta
    public static <E> int drainUninterruptibly(java.util.concurrent.BlockingQueue<E> r6, java.util.Collection<? super E> r7, int r8, long r9, java.util.concurrent.TimeUnit r11) {
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
        com.google.common.base.Preconditions.checkNotNull(r7);
        r0 = java.lang.System.nanoTime();
        r9 = r11.toNanos(r9);
        r2 = r0 + r9;
        r9 = 0;
        r10 = 0;
    L_0x000f:
        if (r9 >= r8) goto L_0x003d;
    L_0x0011:
        r11 = r8 - r9;
        r11 = r6.drainTo(r7, r11);	 Catch:{ all -> 0x0032 }
        r9 = r9 + r11;
        if (r9 >= r8) goto L_0x000f;
    L_0x001a:
        r0 = java.lang.System.nanoTime();	 Catch:{ InterruptedException -> 0x0030 }
        r11 = 0;	 Catch:{ InterruptedException -> 0x0030 }
        r4 = r2 - r0;	 Catch:{ InterruptedException -> 0x0030 }
        r11 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x0030 }
        r11 = r6.poll(r4, r11);	 Catch:{ InterruptedException -> 0x0030 }
        if (r11 != 0) goto L_0x002a;
    L_0x0029:
        goto L_0x003d;
    L_0x002a:
        r7.add(r11);	 Catch:{ all -> 0x0032 }
        r9 = r9 + 1;
        goto L_0x000f;
    L_0x0030:
        r10 = 1;
        goto L_0x001a;
    L_0x0032:
        r6 = move-exception;
        if (r10 == 0) goto L_0x003c;
    L_0x0035:
        r7 = java.lang.Thread.currentThread();
        r7.interrupt();
    L_0x003c:
        throw r6;
    L_0x003d:
        if (r10 == 0) goto L_0x0046;
    L_0x003f:
        r6 = java.lang.Thread.currentThread();
        r6.interrupt();
    L_0x0046:
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Queues.drainUninterruptibly(java.util.concurrent.BlockingQueue, java.util.Collection, int, long, java.util.concurrent.TimeUnit):int");
    }

    public static <E> Queue<E> synchronizedQueue(Queue<E> queue) {
        return Synchronized.queue(queue, null);
    }

    public static <E> Deque<E> synchronizedDeque(Deque<E> deque) {
        return Synchronized.deque(deque, null);
    }
}
