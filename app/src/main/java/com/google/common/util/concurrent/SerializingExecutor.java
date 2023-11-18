package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

final class SerializingExecutor implements Executor {
    private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
    private final Executor executor;
    private final Object internalLock = new C12381();
    @GuardedBy("internalLock")
    private boolean isThreadScheduled = false;
    private final TaskRunner taskRunner = new TaskRunner();
    @GuardedBy("internalLock")
    private final Queue<Runnable> waitQueue = new ArrayDeque();

    /* renamed from: com.google.common.util.concurrent.SerializingExecutor$1 */
    class C12381 {
        C12381() {
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SerializingExecutor lock: ");
            stringBuilder.append(super.toString());
            return stringBuilder.toString();
        }
    }

    private class TaskRunner implements Runnable {
        private TaskRunner() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r8 = this;
        L_0x0000:
            r0 = 0;
            r1 = 1;
            r2 = com.google.common.util.concurrent.SerializingExecutor.this;	 Catch:{ all -> 0x004f }
            r2 = r2.isThreadScheduled;	 Catch:{ all -> 0x004f }
            com.google.common.base.Preconditions.checkState(r2);	 Catch:{ all -> 0x004f }
            r2 = com.google.common.util.concurrent.SerializingExecutor.this;	 Catch:{ all -> 0x004f }
            r2 = r2.internalLock;	 Catch:{ all -> 0x004f }
            monitor-enter(r2);	 Catch:{ all -> 0x004f }
            r3 = com.google.common.util.concurrent.SerializingExecutor.this;	 Catch:{ all -> 0x004c }
            r3 = r3.waitQueue;	 Catch:{ all -> 0x004c }
            r3 = r3.poll();	 Catch:{ all -> 0x004c }
            r3 = (java.lang.Runnable) r3;	 Catch:{ all -> 0x004c }
            if (r3 != 0) goto L_0x002b;
        L_0x0020:
            r3 = com.google.common.util.concurrent.SerializingExecutor.this;	 Catch:{ all -> 0x004c }
            r3.isThreadScheduled = r0;	 Catch:{ all -> 0x004c }
            monitor-exit(r2);	 Catch:{ all -> 0x0027 }
            return;
        L_0x0027:
            r1 = move-exception;
            r3 = r1;
            r1 = 0;
            goto L_0x004d;
        L_0x002b:
            monitor-exit(r2);	 Catch:{ all -> 0x004c }
            r3.run();	 Catch:{ RuntimeException -> 0x0030 }
            goto L_0x0000;
        L_0x0030:
            r2 = move-exception;
            r4 = com.google.common.util.concurrent.SerializingExecutor.log;	 Catch:{ all -> 0x004f }
            r5 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x004f }
            r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004f }
            r6.<init>();	 Catch:{ all -> 0x004f }
            r7 = "Exception while executing runnable ";
            r6.append(r7);	 Catch:{ all -> 0x004f }
            r6.append(r3);	 Catch:{ all -> 0x004f }
            r3 = r6.toString();	 Catch:{ all -> 0x004f }
            r4.log(r5, r3, r2);	 Catch:{ all -> 0x004f }
            goto L_0x0000;
        L_0x004c:
            r3 = move-exception;
        L_0x004d:
            monitor-exit(r2);	 Catch:{ all -> 0x004c }
            throw r3;	 Catch:{ all -> 0x004f }
        L_0x004f:
            r2 = move-exception;
            if (r1 == 0) goto L_0x0063;
        L_0x0052:
            r1 = com.google.common.util.concurrent.SerializingExecutor.this;
            r1 = r1.internalLock;
            monitor-enter(r1);
            r3 = com.google.common.util.concurrent.SerializingExecutor.this;	 Catch:{ all -> 0x0060 }
            r3.isThreadScheduled = r0;	 Catch:{ all -> 0x0060 }
            monitor-exit(r1);	 Catch:{ all -> 0x0060 }
            goto L_0x0063;
        L_0x0060:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0060 }
            throw r0;
        L_0x0063:
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.SerializingExecutor.TaskRunner.run():void");
        }
    }

    public SerializingExecutor(Executor executor) {
        Preconditions.checkNotNull(executor, "'executor' must not be null.");
        this.executor = executor;
    }

    public void execute(Runnable runnable) {
        Preconditions.checkNotNull(runnable, "'r' must not be null.");
        synchronized (this.internalLock) {
            this.waitQueue.add(runnable);
            boolean z = true;
            if (this.isThreadScheduled == null) {
                this.isThreadScheduled = true;
            } else {
                z = false;
            }
        }
        if (z) {
            try {
                this.executor.execute(this.taskRunner);
            } catch (Throwable th) {
                synchronized (this.internalLock) {
                    this.isThreadScheduled = false;
                }
            }
        }
    }
}
