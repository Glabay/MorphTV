package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public abstract class ProxyReader extends FilterReader {
    protected void afterRead(int i) throws IOException {
    }

    protected void beforeRead(int i) throws IOException {
    }

    public ProxyReader(Reader reader) {
        super(reader);
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

    public int read(char[] cArr) throws IOException {
        int length;
        if (cArr != null) {
            try {
                length = cArr.length;
            } catch (char[] cArr2) {
                handleIOException(cArr2);
                return -1;
            }
        }
        length = 0;
        beforeRead(length);
        cArr2 = this.in.read(cArr2);
        afterRead(cArr2);
        return cArr2;
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        try {
            beforeRead(i2);
            cArr = this.in.read(cArr, i, i2);
            afterRead(cArr);
            return cArr;
        } catch (char[] cArr2) {
            handleIOException(cArr2);
            return -1;
        }
    }

    public int read(CharBuffer charBuffer) throws IOException {
        int length;
        if (charBuffer != null) {
            try {
                length = charBuffer.length();
            } catch (CharBuffer charBuffer2) {
                handleIOException(charBuffer2);
                return -1;
            }
        }
        length = 0;
        beforeRead(length);
        charBuffer2 = this.in.read(charBuffer2);
        afterRead(charBuffer2);
        return charBuffer2;
    }

    public long skip(long j) throws IOException {
        try {
            return this.in.skip(j);
        } catch (long j2) {
            handleIOException(j2);
            return 0;
        }
    }

    public boolean ready() throws IOException {
        try {
            return this.in.ready();
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    public void close() throws IOException {
        try {
            this.in.close();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    public synchronized void mark(int i) throws IOException {
        try {
            this.in.mark(i);
        } catch (int i2) {
            handleIOException(i2);
        }
        return;
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
