package okhttp3.internal.http2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final class Ping {
    private final CountDownLatch latch = new CountDownLatch(1);
    private long received = -1;
    private long sent = -1;

    Ping() {
    }

    void send() {
        if (this.sent != -1) {
            throw new IllegalStateException();
        }
        this.sent = System.nanoTime();
    }

    void receive() {
        if (this.received == -1) {
            if (this.sent != -1) {
                this.received = System.nanoTime();
                this.latch.countDown();
                return;
            }
        }
        throw new IllegalStateException();
    }

    void cancel() {
        if (this.received == -1) {
            if (this.sent != -1) {
                this.received = this.sent - 1;
                this.latch.countDown();
                return;
            }
        }
        throw new IllegalStateException();
    }

    public long roundTripTime() throws InterruptedException {
        this.latch.await();
        return this.received - this.sent;
    }

    public long roundTripTime(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.latch.await(j, timeUnit) != null ? this.received - this.sent : -2;
    }
}
