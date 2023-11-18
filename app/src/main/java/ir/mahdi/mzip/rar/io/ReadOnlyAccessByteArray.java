package ir.mahdi.mzip.rar.io;

import java.io.EOFException;
import java.io.IOException;

public class ReadOnlyAccessByteArray implements IReadOnlyAccess {
    private byte[] file;
    private int positionInFile;

    public void close() throws IOException {
    }

    public ReadOnlyAccessByteArray(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException("file must not be null!!");
        }
        this.file = bArr;
        this.positionInFile = null;
    }

    public long getPosition() throws IOException {
        return (long) this.positionInFile;
    }

    public void setPosition(long j) throws IOException {
        if (j >= ((long) this.file.length) || j < 0) {
            throw new EOFException();
        }
        this.positionInFile = (int) j;
    }

    public int read() throws IOException {
        byte[] bArr = this.file;
        int i = this.positionInFile;
        this.positionInFile = i + 1;
        return bArr[i];
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        i2 = Math.min(i2, this.file.length - this.positionInFile);
        System.arraycopy(this.file, this.positionInFile, bArr, i, i2);
        this.positionInFile += i2;
        return i2;
    }

    public int readFully(byte[] bArr, int i) throws IOException {
        if (bArr == null) {
            throw new NullPointerException("buffer must not be null");
        } else if (i == 0) {
            throw new IllegalArgumentException("cannot read 0 bytes ;-)");
        } else {
            i = Math.min(i, (this.file.length - this.positionInFile) - 1);
            System.arraycopy(this.file, this.positionInFile, bArr, 0, i);
            this.positionInFile += i;
            return i;
        }
    }
}
