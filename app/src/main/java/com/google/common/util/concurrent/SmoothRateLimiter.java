package com.google.common.util.concurrent;

import java.util.concurrent.TimeUnit;

abstract class SmoothRateLimiter extends RateLimiter {
    double maxPermits;
    private long nextFreeTicketMicros;
    double stableIntervalMicros;
    double storedPermits;

    static final class SmoothBursty extends SmoothRateLimiter {
        final double maxBurstSeconds;

        long storedPermitsToWaitTime(double d, double d2) {
            return 0;
        }

        SmoothBursty(SleepingStopwatch sleepingStopwatch, double d) {
            super(sleepingStopwatch);
            this.maxBurstSeconds = d;
        }

        void doSetRate(double d, double d2) {
            d2 = this.maxPermits;
            this.maxPermits = this.maxBurstSeconds * d;
            if (d2 == Double.POSITIVE_INFINITY) {
                this.storedPermits = this.maxPermits;
                return;
            }
            d = 0.0d;
            if (d2 != 0.0d) {
                d = (this.storedPermits * this.maxPermits) / d2;
            }
            this.storedPermits = d;
        }

        double coolDownIntervalMicros() {
            return this.stableIntervalMicros;
        }
    }

    static final class SmoothWarmingUp extends SmoothRateLimiter {
        private double coldFactor;
        private double slope;
        private double thresholdPermits;
        private final long warmupPeriodMicros;

        SmoothWarmingUp(SleepingStopwatch sleepingStopwatch, long j, TimeUnit timeUnit, double d) {
            super(sleepingStopwatch);
            this.warmupPeriodMicros = timeUnit.toMicros(j);
            this.coldFactor = d;
        }

        void doSetRate(double d, double d2) {
            d = this.maxPermits;
            double d3 = this.coldFactor * d2;
            this.thresholdPermits = (((double) this.warmupPeriodMicros) * 0.5d) / d2;
            this.maxPermits = this.thresholdPermits + ((((double) this.warmupPeriodMicros) * 2.0d) / (d2 + d3));
            this.slope = (d3 - d2) / (this.maxPermits - this.thresholdPermits);
            if (d == Double.POSITIVE_INFINITY) {
                this.storedPermits = 0.0d;
            } else {
                this.storedPermits = d == 0.0d ? this.maxPermits : (this.storedPermits * this.maxPermits) / d;
            }
        }

        long storedPermitsToWaitTime(double d, double d2) {
            d -= this.thresholdPermits;
            if (d > 0.0d) {
                double min = Math.min(d, d2);
                d = (long) (((permitsToTime(d) + permitsToTime(d - min)) * min) / 2.0d);
                d2 -= min;
            } else {
                d = 0.0d;
            }
            return (long) (((double) d) + (this.stableIntervalMicros * d2));
        }

        private double permitsToTime(double d) {
            return this.stableIntervalMicros + (d * this.slope);
        }

        double coolDownIntervalMicros() {
            return ((double) this.warmupPeriodMicros) / this.maxPermits;
        }
    }

    abstract double coolDownIntervalMicros();

    abstract void doSetRate(double d, double d2);

    abstract long storedPermitsToWaitTime(double d, double d2);

    private SmoothRateLimiter(SleepingStopwatch sleepingStopwatch) {
        super(sleepingStopwatch);
        this.nextFreeTicketMicros = 0;
    }

    final void doSetRate(double d, long j) {
        resync(j);
        double toMicros = ((double) TimeUnit.SECONDS.toMicros(1)) / d;
        this.stableIntervalMicros = toMicros;
        doSetRate(d, toMicros);
    }

    final double doGetRate() {
        return ((double) TimeUnit.SECONDS.toMicros(1)) / this.stableIntervalMicros;
    }

    final long queryEarliestAvailable(long j) {
        return this.nextFreeTicketMicros;
    }

    final long reserveEarliestAvailable(int i, long j) {
        resync(j);
        j = this.nextFreeTicketMicros;
        double d = (double) i;
        double min = Math.min(d, this.storedPermits);
        this.nextFreeTicketMicros += storedPermitsToWaitTime(this.storedPermits, min) + ((long) ((d - min) * this.stableIntervalMicros));
        this.storedPermits -= min;
        return j;
    }

    private void resync(long j) {
        if (j > this.nextFreeTicketMicros) {
            this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (((double) (j - this.nextFreeTicketMicros)) / coolDownIntervalMicros()));
            this.nextFreeTicketMicros = j;
        }
    }
}
