package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

final class ListenerCallQueue<L> implements Runnable {
    private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
    private final Executor executor;
    @GuardedBy("this")
    private boolean isThreadScheduled;
    private final L listener;
    @GuardedBy("this")
    private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();

    static abstract class Callback<L> {
        private final String methodCall;

        abstract void call(L l);

        Callback(String str) {
            this.methodCall = str;
        }

        void enqueueOn(Iterable<ListenerCallQueue<L>> iterable) {
            for (ListenerCallQueue add : iterable) {
                add.add(this);
            }
        }
    }

    ListenerCallQueue(L l, Executor executor) {
        this.listener = Preconditions.checkNotNull(l);
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    synchronized void add(Callback<L> callback) {
        this.waitQueue.add(callback);
    }

    void execute() {
        synchronized (this) {
            boolean z = true;
            if (this.isThreadScheduled) {
                z = false;
            } else {
                this.isThreadScheduled = true;
            }
        }
        if (z) {
            try {
                this.executor.execute(this);
            } catch (Throwable e) {
                synchronized (this) {
                    this.isThreadScheduled = false;
                    Logger logger = logger;
                    Level level = Level.SEVERE;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Exception while running callbacks for ");
                    stringBuilder.append(this.listener);
                    stringBuilder.append(" on ");
                    stringBuilder.append(this.executor);
                    logger.log(level, stringBuilder.toString(), e);
                    throw e;
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r8 = this;
    L_0x0000:
        r0 = 0;
        r1 = 1;
        monitor-enter(r8);	 Catch:{ all -> 0x004c }
        r2 = r8.isThreadScheduled;	 Catch:{ all -> 0x0049 }
        com.google.common.base.Preconditions.checkState(r2);	 Catch:{ all -> 0x0049 }
        r2 = r8.waitQueue;	 Catch:{ all -> 0x0049 }
        r2 = r2.poll();	 Catch:{ all -> 0x0049 }
        r2 = (com.google.common.util.concurrent.ListenerCallQueue.Callback) r2;	 Catch:{ all -> 0x0049 }
        if (r2 != 0) goto L_0x001a;
    L_0x0012:
        r8.isThreadScheduled = r0;	 Catch:{ all -> 0x0049 }
        monitor-exit(r8);	 Catch:{ all -> 0x0016 }
        return;
    L_0x0016:
        r1 = move-exception;
        r2 = r1;
        r1 = 0;
        goto L_0x004a;
    L_0x001a:
        monitor-exit(r8);	 Catch:{ all -> 0x0049 }
        r3 = r8.listener;	 Catch:{ RuntimeException -> 0x0021 }
        r2.call(r3);	 Catch:{ RuntimeException -> 0x0021 }
        goto L_0x0000;
    L_0x0021:
        r3 = move-exception;
        r4 = logger;	 Catch:{ all -> 0x004c }
        r5 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x004c }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004c }
        r6.<init>();	 Catch:{ all -> 0x004c }
        r7 = "Exception while executing callback: ";
        r6.append(r7);	 Catch:{ all -> 0x004c }
        r7 = r8.listener;	 Catch:{ all -> 0x004c }
        r6.append(r7);	 Catch:{ all -> 0x004c }
        r7 = ".";
        r6.append(r7);	 Catch:{ all -> 0x004c }
        r2 = r2.methodCall;	 Catch:{ all -> 0x004c }
        r6.append(r2);	 Catch:{ all -> 0x004c }
        r2 = r6.toString();	 Catch:{ all -> 0x004c }
        r4.log(r5, r2, r3);	 Catch:{ all -> 0x004c }
        goto L_0x0000;
    L_0x0049:
        r2 = move-exception;
    L_0x004a:
        monitor-exit(r8);	 Catch:{ all -> 0x0049 }
        throw r2;	 Catch:{ all -> 0x004c }
    L_0x004c:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0057;
    L_0x004f:
        monitor-enter(r8);
        r8.isThreadScheduled = r0;	 Catch:{ all -> 0x0054 }
        monitor-exit(r8);	 Catch:{ all -> 0x0054 }
        goto L_0x0057;
    L_0x0054:
        r0 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0054 }
        throw r0;
    L_0x0057:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.ListenerCallQueue.run():void");
    }
}
