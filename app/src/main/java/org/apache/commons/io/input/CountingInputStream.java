package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends ProxyInputStream {
    private long count;

    public CountingInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public synchronized long skip(long j) throws IOException {
        j = super.skip(j);
        this.count += j;
        return j;
    }

    protected synchronized void afterRead(int i) {
        if (i != -1) {
            this.count += (long) i;
        }
    }

    public int getCount() {
        long byteCount = getByteCount();
        if (byteCount <= 2147483647L) {
            return (int) byteCount;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The byte count ");
        stringBuilder.append(byteCount);
        stringBuilder.append(" is too large to be converted to an int");
        throw new ArithmeticException(stringBuilder.toString());
    }

    public int resetCount() {
        long resetByteCount = resetByteCount();
        if (resetByteCount <= 2147483647L) {
            return (int) resetByteCount;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The byte count ");
        stringBuilder.append(resetByteCount);
        stringBuilder.append(" is too large to be converted to an int");
        throw new ArithmeticException(stringBuilder.toString());
    }

    public synchronized long getByteCount() {
        return this.count;
    }

    public synchronized long resetByteCount() {
        long j;
        j = this.count;
        this.count = 0;
        return j;
    }
}
