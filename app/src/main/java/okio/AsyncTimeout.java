package okio;

import com.google.android.exoplayer2.C0649C;
import com.tonyodev.fetch2.util.FetchErrorStrings;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class AsyncTimeout extends Timeout {
    private static final long IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60);
    private static final long IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
    private static final int TIMEOUT_WRITE_SIZE = 65536;
    @Nullable
    static AsyncTimeout head;
    private boolean inQueue;
    @Nullable
    private AsyncTimeout next;
    private long timeoutAt;

    protected void timedOut() {
    }

    public final void enter() {
        if (this.inQueue) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        long timeoutNanos = timeoutNanos();
        boolean hasDeadline = hasDeadline();
        if (timeoutNanos != 0 || hasDeadline) {
            this.inQueue = true;
            scheduleTimeout(this, timeoutNanos, hasDeadline);
        }
    }

    private static synchronized void scheduleTimeout(AsyncTimeout asyncTimeout, long j, boolean z) {
        synchronized (AsyncTimeout.class) {
            if (head == null) {
                head = new AsyncTimeout();
                new AsyncTimeout$Watchdog().start();
            }
            long nanoTime = System.nanoTime();
            if (j != 0 && z) {
                asyncTimeout.timeoutAt = nanoTime + Math.min(j, asyncTimeout.deadlineNanoTime() - nanoTime);
            } else if (j != 0) {
                asyncTimeout.timeoutAt = nanoTime + j;
            } else if (z) {
                asyncTimeout.timeoutAt = asyncTimeout.deadlineNanoTime();
            } else {
                throw new AssertionError();
            }
            j = asyncTimeout.remainingNanos(nanoTime);
            z = head;
            while (z.next != null) {
                if (j < z.next.remainingNanos(nanoTime)) {
                    break;
                }
                z = z.next;
            }
            asyncTimeout.next = z.next;
            z.next = asyncTimeout;
            if (z == head) {
                AsyncTimeout.class.notify();
            }
        }
    }

    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return cancelScheduledTimeout(this);
    }

    private static synchronized boolean cancelScheduledTimeout(AsyncTimeout asyncTimeout) {
        synchronized (AsyncTimeout.class) {
            for (AsyncTimeout asyncTimeout2 = head; asyncTimeout2 != null; asyncTimeout2 = asyncTimeout2.next) {
                if (asyncTimeout2.next == asyncTimeout) {
                    asyncTimeout2.next = asyncTimeout.next;
                    asyncTimeout.next = null;
                    return null;
                }
            }
            return true;
        }
    }

    private long remainingNanos(long j) {
        return this.timeoutAt - j;
    }

    public final Sink sink(Sink sink) {
        return new AsyncTimeout$1(this, sink);
    }

    public final Source source(Source source) {
        return new AsyncTimeout$2(this, source);
    }

    final void exit(boolean z) throws IOException {
        if (exit() && z) {
            throw newTimeoutException(false);
        }
    }

    final IOException exit(IOException iOException) throws IOException {
        if (exit()) {
            return newTimeoutException(iOException);
        }
        return iOException;
    }

    protected IOException newTimeoutException(@Nullable IOException iOException) {
        IOException interruptedIOException = new InterruptedIOException(FetchErrorStrings.CONNECTION_TIMEOUT);
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    @Nullable
    static AsyncTimeout awaitTimeout() throws InterruptedException {
        AsyncTimeout asyncTimeout = head.next;
        AsyncTimeout asyncTimeout2 = null;
        if (asyncTimeout == null) {
            long nanoTime = System.nanoTime();
            AsyncTimeout.class.wait(IDLE_TIMEOUT_MILLIS);
            if (head.next == null && System.nanoTime() - nanoTime >= IDLE_TIMEOUT_NANOS) {
                asyncTimeout2 = head;
            }
            return asyncTimeout2;
        }
        nanoTime = asyncTimeout.remainingNanos(System.nanoTime());
        if (nanoTime > 0) {
            long j = nanoTime / C0649C.MICROS_PER_SECOND;
            AsyncTimeout.class.wait(j, (int) (nanoTime - (C0649C.MICROS_PER_SECOND * j)));
            return null;
        }
        head.next = asyncTimeout.next;
        asyncTimeout.next = null;
        return asyncTimeout;
    }
}
