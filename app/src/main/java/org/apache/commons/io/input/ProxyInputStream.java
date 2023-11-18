package org.apache.commons.io.input;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ProxyInputStream extends FilterInputStream {
    protected void afterRead(int i) throws IOException {
    }

    protected void beforeRead(int i) throws IOException {
    }

    public ProxyInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public int read() throws IOException {
        int i = 1;
        try {
            beforeRead(1);
            int read = this.in.read();
            if (read == -1) {
                i = -1;
            }
            afterRead(i);
            return read;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    public int read(byte[] bArr) throws IOException {
        int length;
        if (bArr != null) {
            try {
                length = bArr.length;
            } catch (byte[] bArr2) {
                handleIOException(bArr2);
                return -1;
            }
        }
        length = 0;
        beforeRead(length);
        bArr2 = this.in.read(bArr2);
        afterRead(bArr2);
        return bArr2;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        try {
            beforeRead(i2);
            bArr = this.in.read(bArr, i, i2);
            afterRead(bArr);
            return bArr;
        } catch (byte[] bArr2) {
            handleIOException(bArr2);
            return -1;
        }
    }

    public long skip(long j) throws IOException {
        try {
            return this.in.skip(j);
        } catch (long j2) {
            handleIOException(j2);
            return 0;
        }
    }

    public int available() throws IOException {
        try {
            return super.available();
        } catch (IOException e) {
            handleIOException(e);
            return 0;
        }
    }

    public void close() throws IOException {
        try {
            this.in.close();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    public synchronized void mark(int i) {
        this.in.mark(i);
    }

    public synchronized void reset() throws IOException {
        try {
            this.in.reset();
        } catch (IOException e) {
            handleIOException(e);
        }
        return;
    }

    public boolean markSupported() {
        return this.in.markSupported();
    }

    protected void handleIOException(IOException iOException) throws IOException {
        throw iOException;
    }
}
