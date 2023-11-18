package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.Validate;

public class TimedSemaphore {
    public static final int NO_LIMIT = 0;
    private static final int THREAD_POOL_SIZE = 1;
    private int acquireCount;
    private final ScheduledExecutorService executorService;
    private int lastCallsPerPeriod;
    private int limit;
    private final boolean ownExecutor;
    private final long period;
    private long periodCount;
    private boolean shutdown;
    private ScheduledFuture<?> task;
    private long totalAcquireCount;
    private final TimeUnit unit;

    /* renamed from: org.apache.commons.lang3.concurrent.TimedSemaphore$1 */
    class C14621 implements Runnable {
        C14621() {
        }

        public void run() {
            TimedSemaphore.this.endOfPeriod();
        }
    }

    public TimedSemaphore(long j, TimeUnit timeUnit, int i) {
        this(null, j, timeUnit, i);
    }

    public TimedSemaphore(ScheduledExecutorService scheduledExecutorService, long j, TimeUnit timeUnit, int i) {
        Validate.inclusiveBetween(1, Long.MAX_VALUE, j, "Time period must be greater than 0!");
        this.period = j;
        this.unit = timeUnit;
        if (scheduledExecutorService != null) {
            this.executorService = scheduledExecutorService;
            this.ownExecutor = false;
        } else {
            scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
            scheduledExecutorService.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
            scheduledExecutorService.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            this.executorService = scheduledExecutorService;
            this.ownExecutor = true;
        }
        setLimit(i);
    }

    public final synchronized int getLimit() {
        return this.limit;
    }

    public final synchronized void setLimit(int i) {
        this.limit = i;
    }

    public synchronized void shutdown() {
        if (!this.shutdown) {
            if (this.ownExecutor) {
                getExecutorService().shutdownNow();
            }
            if (this.task != null) {
                this.task.cancel(false);
            }
            this.shutdown = true;
        }
    }

    public synchronized boolean isShutdown() {
        return this.shutdown;
    }

    public synchronized void acquire() throws InterruptedException {
        prepareAcquire();
        boolean acquirePermit;
        do {
            acquirePermit = acquirePermit();
            if (!acquirePermit) {
                wait();
                continue;
            }
        } while (!acquirePermit);
    }

    public synchronized boolean tryAcquire() {
        prepareAcquire();
        return acquirePermit();
    }

    public synchronized int getLastAcquiresPerPeriod() {
        return this.lastCallsPerPeriod;
    }

    public synchronized int getAcquireCount() {
        return this.acquireCount;
    }

    public synchronized int getAvailablePermits() {
        return getLimit() - getAcquireCount();
    }

    public synchronized double getAverageCallsPerPeriod() {
        return this.periodCount == 0 ? 0.0d : ((double) this.totalAcquireCount) / ((double) this.periodCount);
    }

    public long getPeriod() {
        return this.period;
    }

    public TimeUnit getUnit() {
        return this.unit;
    }

    protected ScheduledExecutorService getExecutorService() {
        return this.executorService;
    }

    protected ScheduledFuture<?> startTimer() {
        return getExecutorService().scheduleAtFixedRate(new C14621(), getPeriod(), getPeriod(), getUnit());
    }

    synchronized void endOfPeriod() {
        this.lastCallsPerPeriod = this.acquireCount;
        this.totalAcquireCount += (long) this.acquireCount;
        this.periodCount++;
        this.acquireCount = 0;
        notifyAll();
    }

    private void prepareAcquire() {
        if (isShutdown()) {
            throw new IllegalStateException("TimedSemaphore is shut down!");
        } else if (this.task == null) {
            this.task = startTimer();
        }
    }

    private boolean acquirePermit() {
        if (getLimit() > 0) {
            if (this.acquireCount >= getLimit()) {
                return false;
            }
        }
        this.acquireCount++;
        return true;
    }
}
