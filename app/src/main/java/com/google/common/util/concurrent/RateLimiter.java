package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Beta
public abstract class RateLimiter {
    private volatile Object mutexDoNotUseDirectly;
    private final SleepingStopwatch stopwatch;

    @VisibleForTesting
    static abstract class SleepingStopwatch {

        /* renamed from: com.google.common.util.concurrent.RateLimiter$SleepingStopwatch$1 */
        static class C12361 extends SleepingStopwatch {
            final Stopwatch stopwatch = Stopwatch.createStarted();

            C12361() {
            }

            long readMicros() {
                return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
            }

            void sleepMicrosUninterruptibly(long j) {
                if (j > 0) {
                    Uninterruptibles.sleepUninterruptibly(j, TimeUnit.MICROSECONDS);
                }
            }
        }

        abstract long readMicros();

        abstract void sleepMicrosUninterruptibly(long j);

        SleepingStopwatch() {
        }

        static final SleepingStopwatch createFromSystemTimer() {
            return new C12361();
        }
    }

    abstract double doGetRate();

    abstract void doSetRate(double d, long j);

    abstract long queryEarliestAvailable(long j);

    abstract long reserveEarliestAvailable(int i, long j);

    public static RateLimiter create(double d) {
        return create(SleepingStopwatch.createFromSystemTimer(), d);
    }

    @VisibleForTesting
    static RateLimiter create(SleepingStopwatch sleepingStopwatch, double d) {
        RateLimiter smoothBursty = new SmoothBursty(sleepingStopwatch, 1.0d);
        smoothBursty.setRate(d);
        return smoothBursty;
    }

    public static RateLimiter create(double d, long j, TimeUnit timeUnit) {
        Preconditions.checkArgument(j >= 0, "warmupPeriod must not be negative: %s", new Object[]{Long.valueOf(j)});
        return create(SleepingStopwatch.createFromSystemTimer(), d, j, timeUnit, 3.0d);
    }

    @VisibleForTesting
    static RateLimiter create(SleepingStopwatch sleepingStopwatch, double d, long j, TimeUnit timeUnit, double d2) {
        RateLimiter smoothWarmingUp = new SmoothWarmingUp(sleepingStopwatch, j, timeUnit, d2);
        smoothWarmingUp.setRate(d);
        return smoothWarmingUp;
    }

    private Object mutex() {
        Object obj = this.mutexDoNotUseDirectly;
        if (obj == null) {
            synchronized (this) {
                obj = this.mutexDoNotUseDirectly;
                if (obj == null) {
                    obj = new Object();
                    this.mutexDoNotUseDirectly = obj;
                }
            }
        }
        return obj;
    }

    RateLimiter(SleepingStopwatch sleepingStopwatch) {
        this.stopwatch = (SleepingStopwatch) Preconditions.checkNotNull(sleepingStopwatch);
    }

    public final void setRate(double d) {
        boolean z = d > 0.0d && !Double.isNaN(d);
        Preconditions.checkArgument(z, "rate must be positive");
        synchronized (mutex()) {
            doSetRate(d, this.stopwatch.readMicros());
        }
    }

    public final double getRate() {
        double doGetRate;
        synchronized (mutex()) {
            doGetRate = doGetRate();
        }
        return doGetRate;
    }

    public double acquire() {
        return acquire(1);
    }

    public double acquire(int i) {
        long reserve = reserve(i);
        this.stopwatch.sleepMicrosUninterruptibly(reserve);
        return (((double) reserve) * 1.0d) / ((double) TimeUnit.SECONDS.toMicros(1));
    }

    final long reserve(int i) {
        long reserveAndGetWaitLength;
        checkPermits(i);
        synchronized (mutex()) {
            reserveAndGetWaitLength = reserveAndGetWaitLength(i, this.stopwatch.readMicros());
        }
        return reserveAndGetWaitLength;
    }

    public boolean tryAcquire(long j, TimeUnit timeUnit) {
        return tryAcquire(1, j, timeUnit);
    }

    public boolean tryAcquire(int i) {
        return tryAcquire(i, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire() {
        return tryAcquire(1, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire(int i, long j, TimeUnit timeUnit) {
        j = Math.max(timeUnit.toMicros(j), 0);
        checkPermits(i);
        synchronized (mutex()) {
            long readMicros = this.stopwatch.readMicros();
            if (canAcquire(readMicros, j) == null) {
                return false;
            }
            i = reserveAndGetWaitLength(i, readMicros);
            this.stopwatch.sleepMicrosUninterruptibly(i);
            return true;
        }
    }

    private boolean canAcquire(long j, long j2) {
        return queryEarliestAvailable(j) - j2 <= j ? 1 : 0;
    }

    final long reserveAndGetWaitLength(int i, long j) {
        return Math.max(reserveEarliestAvailable(i, j) - j, 0);
    }

    public String toString() {
        return String.format("RateLimiter[stableRate=%3.1fqps]", new Object[]{Double.valueOf(getRate())});
    }

    private static int checkPermits(int i) {
        Preconditions.checkArgument(i > 0, "Requested permits (%s) must be positive", new Object[]{Integer.valueOf(i)});
        return i;
    }
}
