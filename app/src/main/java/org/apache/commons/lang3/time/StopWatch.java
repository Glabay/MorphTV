package org.apache.commons.lang3.time;

import java.util.concurrent.TimeUnit;

public class StopWatch {
    private static final long NANO_2_MILLIS = 1000000;
    private State runningState = State.UNSTARTED;
    private SplitState splitState = SplitState.UNSPLIT;
    private long startTime;
    private long startTimeMillis;
    private long stopTime;

    private enum SplitState {
        SPLIT,
        UNSPLIT
    }

    private enum State {
        UNSTARTED {
            boolean isStarted() {
                return false;
            }

            boolean isStopped() {
                return true;
            }

            boolean isSuspended() {
                return false;
            }
        },
        RUNNING {
            boolean isStarted() {
                return true;
            }

            boolean isStopped() {
                return false;
            }

            boolean isSuspended() {
                return false;
            }
        },
        STOPPED {
            boolean isStarted() {
                return false;
            }

            boolean isStopped() {
                return true;
            }

            boolean isSuspended() {
                return false;
            }
        },
        SUSPENDED {
            boolean isStarted() {
                return true;
            }

            boolean isStopped() {
                return false;
            }

            boolean isSuspended() {
                return true;
            }
        };

        abstract boolean isStarted();

        abstract boolean isStopped();

        abstract boolean isSuspended();
    }

    public static StopWatch createStarted() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        return stopWatch;
    }

    public void start() {
        if (this.runningState == State.STOPPED) {
            throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
        } else if (this.runningState != State.UNSTARTED) {
            throw new IllegalStateException("Stopwatch already started. ");
        } else {
            this.startTime = System.nanoTime();
            this.startTimeMillis = System.currentTimeMillis();
            this.runningState = State.RUNNING;
        }
    }

    public void stop() {
        if (this.runningState == State.RUNNING || this.runningState == State.SUSPENDED) {
            if (this.runningState == State.RUNNING) {
                this.stopTime = System.nanoTime();
            }
            this.runningState = State.STOPPED;
            return;
        }
        throw new IllegalStateException("Stopwatch is not running. ");
    }

    public void reset() {
        this.runningState = State.UNSTARTED;
        this.splitState = SplitState.UNSPLIT;
    }

    public void split() {
        if (this.runningState != State.RUNNING) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        this.stopTime = System.nanoTime();
        this.splitState = SplitState.SPLIT;
    }

    public void unsplit() {
        if (this.splitState != SplitState.SPLIT) {
            throw new IllegalStateException("Stopwatch has not been split. ");
        }
        this.splitState = SplitState.UNSPLIT;
    }

    public void suspend() {
        if (this.runningState != State.RUNNING) {
            throw new IllegalStateException("Stopwatch must be running to suspend. ");
        }
        this.stopTime = System.nanoTime();
        this.runningState = State.SUSPENDED;
    }

    public void resume() {
        if (this.runningState != State.SUSPENDED) {
            throw new IllegalStateException("Stopwatch must be suspended to resume. ");
        }
        this.startTime += System.nanoTime() - this.stopTime;
        this.runningState = State.RUNNING;
    }

    public long getTime() {
        return getNanoTime() / 1000000;
    }

    public long getTime(TimeUnit timeUnit) {
        return timeUnit.convert(getNanoTime(), TimeUnit.NANOSECONDS);
    }

    public long getNanoTime() {
        if (this.runningState != State.STOPPED) {
            if (this.runningState != State.SUSPENDED) {
                if (this.runningState == State.UNSTARTED) {
                    return 0;
                }
                if (this.runningState == State.RUNNING) {
                    return System.nanoTime() - this.startTime;
                }
                throw new RuntimeException("Illegal running state has occurred.");
            }
        }
        return this.stopTime - this.startTime;
    }

    public long getSplitTime() {
        return getSplitNanoTime() / 1000000;
    }

    public long getSplitNanoTime() {
        if (this.splitState == SplitState.SPLIT) {
            return this.stopTime - this.startTime;
        }
        throw new IllegalStateException("Stopwatch must be split to get the split time. ");
    }

    public long getStartTime() {
        if (this.runningState != State.UNSTARTED) {
            return this.startTimeMillis;
        }
        throw new IllegalStateException("Stopwatch has not been started");
    }

    public String toString() {
        return DurationFormatUtils.formatDurationHMS(getTime());
    }

    public String toSplitString() {
        return DurationFormatUtils.formatDurationHMS(getSplitTime());
    }

    public boolean isStarted() {
        return this.runningState.isStarted();
    }

    public boolean isSuspended() {
        return this.runningState.isSuspended();
    }

    public boolean isStopped() {
        return this.runningState.isStopped();
    }
}
