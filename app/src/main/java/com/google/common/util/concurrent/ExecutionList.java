package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

public final class ExecutionList {
    @VisibleForTesting
    static final Logger log = Logger.getLogger(ExecutionList.class.getName());
    @GuardedBy("this")
    private boolean executed;
    @GuardedBy("this")
    private ExecutionList$RunnableExecutorPair runnables;

    public void add(Runnable runnable, Executor executor) {
        Preconditions.checkNotNull(runnable, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        synchronized (this) {
            if (this.executed) {
                executeListener(runnable, executor);
                return;
            }
            this.runnables = new ExecutionList$RunnableExecutorPair(runnable, executor, this.runnables);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void execute() {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.executed;	 Catch:{ all -> 0x0029 }
        if (r0 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r4);	 Catch:{ all -> 0x0029 }
        return;
    L_0x0007:
        r0 = 1;
        r4.executed = r0;	 Catch:{ all -> 0x0029 }
        r0 = r4.runnables;	 Catch:{ all -> 0x0029 }
        r1 = 0;
        r4.runnables = r1;	 Catch:{ all -> 0x0029 }
        monitor-exit(r4);	 Catch:{ all -> 0x0029 }
        r3 = r1;
        r1 = r0;
        r0 = r3;
    L_0x0013:
        if (r1 == 0) goto L_0x001c;
    L_0x0015:
        r2 = r1.next;
        r1.next = r0;
        r0 = r1;
        r1 = r2;
        goto L_0x0013;
    L_0x001c:
        if (r0 == 0) goto L_0x0028;
    L_0x001e:
        r1 = r0.runnable;
        r2 = r0.executor;
        executeListener(r1, r2);
        r0 = r0.next;
        goto L_0x001c;
    L_0x0028:
        return;
    L_0x0029:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0029 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.ExecutionList.execute():void");
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (Throwable e) {
            Logger logger = log;
            Level level = Level.SEVERE;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RuntimeException while executing runnable ");
            stringBuilder.append(runnable);
            stringBuilder.append(" with executor ");
            stringBuilder.append(executor);
            logger.log(level, stringBuilder.toString(), e);
        }
    }
}
