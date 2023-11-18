package ir.mahdi.mzip.rar.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamReadOnlyAccessFile implements IReadOnlyAccess {
    private RandomAccessStream is;

    public InputStreamReadOnlyAccessFile(InputStream inputStream) {
        this.is = new RandomAccessStream(new BufferedInputStream(inputStream));
    }

    public long getPosition() throws IOException {
        return this.is.getLongFilePointer();
    }

    public void setPosition(long j) throws IOException {
        this.is.seek(j);
    }

    public int read() throws IOException {
        return this.is.read();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        return this.is.read(bArr, i, i2);
    }

    public int readFully(byte[] bArr, int i) throws IOException {
        this.is.readFully(bArr, i);
        return i;
    }

    public void close() throws IOException {
        this.is.close();
    }
}
