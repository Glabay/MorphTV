package okio;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class Timeout$1 extends Timeout {
    public Timeout deadlineNanoTime(long j) {
        return this;
    }

    public void throwIfReached() throws IOException {
    }

    public Timeout timeout(long j, TimeUnit timeUnit) {
        return this;
    }

    Timeout$1() {
    }
}
