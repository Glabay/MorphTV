package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProxyOutputStream extends FilterOutputStream {
    protected void afterWrite(int i) throws IOException {
    }

    protected void beforeWrite(int i) throws IOException {
    }

    public ProxyOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    public void write(int i) throws IOException {
        try {
            beforeWrite(1);
            this.out.write(i);
            afterWrite(1);
        } catch (int i2) {
            handleIOException(i2);
        }
    }

    public void write(byte[] bArr) throws IOException {
        int length;
        if (bArr != null) {
            try {
                length = bArr.length;
            } catch (byte[] bArr2) {
                handleIOException(bArr2);
                return;
            }
        }
        length = 0;
        beforeWrite(length);
        this.out.write(bArr2);
        afterWrite(length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        try {
            beforeWrite(i2);
            this.out.write(bArr, i, i2);
            afterWrite(i2);
        } catch (byte[] bArr2) {
            handleIOException(bArr2);
        }
    }

    public void flush() throws IOException {
        try {
            this.out.flush();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    public void close() throws IOException {
        try {
            this.out.close();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    protected void handleIOException(IOException iOException) throws IOException {
        throw iOException;
    }
}
