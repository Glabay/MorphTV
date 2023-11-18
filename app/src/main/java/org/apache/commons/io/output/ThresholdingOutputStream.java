package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ThresholdingOutputStream extends OutputStream {
    private final int threshold;
    private boolean thresholdExceeded;
    private long written;

    protected abstract OutputStream getStream() throws IOException;

    protected abstract void thresholdReached() throws IOException;

    public ThresholdingOutputStream(int i) {
        this.threshold = i;
    }

    public void write(int i) throws IOException {
        checkThreshold(1);
        getStream().write(i);
        this.written++;
    }

    public void write(byte[] bArr) throws IOException {
        checkThreshold(bArr.length);
        getStream().write(bArr);
        this.written += (long) bArr.length;
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        checkThreshold(i2);
        getStream().write(bArr, i, i2);
        this.written += (long) i2;
    }

    public void flush() throws IOException {
        getStream().flush();
    }

    public void close() throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r1 = this;
        r1.flush();	 Catch:{ IOException -> 0x0003 }
    L_0x0003:
        r0 = r1.getStream();
        r0.close();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.output.ThresholdingOutputStream.close():void");
    }

    public int getThreshold() {
        return this.threshold;
    }

    public long getByteCount() {
        return this.written;
    }

    public boolean isThresholdExceeded() {
        return this.written > ((long) this.threshold);
    }

    protected void checkThreshold(int i) throws IOException {
        if (!this.thresholdExceeded && this.written + ((long) i) > ((long) this.threshold)) {
            this.thresholdExceeded = true;
            thresholdReached();
        }
    }

    protected void resetByteCount() {
        this.thresholdExceeded = false;
        this.written = 0;
    }

    protected void setByteCount(long j) {
        this.written = j;
    }
}
