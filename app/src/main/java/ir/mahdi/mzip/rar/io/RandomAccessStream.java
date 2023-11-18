package ir.mahdi.mzip.rar.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Vector;
import net.lingala.zip4j.util.InternalZipConstants;

public final class RandomAccessStream extends InputStream {
    private static final int BLOCK_MASK = 511;
    private static final int BLOCK_SHIFT = 9;
    private static final int BLOCK_SIZE = 512;
    private Vector data;
    private boolean foundEOS;
    private int length;
    private long pointer;
    private RandomAccessFile ras;
    private InputStream src;

    public RandomAccessStream(InputStream inputStream) {
        this.pointer = 0;
        this.data = new Vector();
        this.length = 0;
        this.foundEOS = false;
        this.src = inputStream;
    }

    public RandomAccessStream(RandomAccessFile randomAccessFile) {
        this.ras = randomAccessFile;
    }

    public int getFilePointer() throws IOException {
        if (this.ras != null) {
            return (int) this.ras.getFilePointer();
        }
        return (int) this.pointer;
    }

    public long getLongFilePointer() throws IOException {
        if (this.ras != null) {
            return this.ras.getFilePointer();
        }
        return this.pointer;
    }

    public int read() throws IOException {
        if (this.ras != null) {
            return this.ras.read();
        }
        long j = this.pointer + 1;
        if (readUntil(j) < j) {
            return -1;
        }
        byte[] bArr = (byte[]) this.data.elementAt((int) (this.pointer >> 9));
        j = this.pointer;
        this.pointer = j + 1;
        return bArr[(int) (j & 511)] & 255;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (bArr == null) {
            throw new NullPointerException();
        } else if (this.ras != null) {
            return this.ras.read(bArr, i, i2);
        } else {
            if (i >= 0 && i2 >= 0) {
                if (i + i2 <= bArr.length) {
                    if (i2 == 0) {
                        return null;
                    }
                    if (readUntil(this.pointer + ((long) i2)) <= this.pointer) {
                        return -1;
                    }
                    byte[] bArr2 = (byte[]) this.data.elementAt((int) (this.pointer >> 9));
                    i2 = Math.min(i2, 512 - ((int) (this.pointer & 511)));
                    System.arraycopy(bArr2, (int) (this.pointer & 511), bArr, i, i2);
                    this.pointer += (long) i2;
                    return i2;
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    public final void readFully(byte[] bArr) throws IOException {
        readFully(bArr, bArr.length);
    }

    public final void readFully(byte[] bArr, int i) throws IOException {
        int i2 = 0;
        do {
            int read = read(bArr, i2, i - i2);
            if (read >= 0) {
                i2 += read;
            } else {
                return;
            }
        } while (i2 < i);
    }

    private long readUntil(long j) throws IOException {
        if (j < ((long) this.length)) {
            return j;
        }
        if (this.foundEOS) {
            return (long) this.length;
        }
        j = (int) (j >> 9);
        for (int i = this.length >> 9; i <= j; i++) {
            int i2 = 512;
            Object obj = new byte[512];
            this.data.addElement(obj);
            int i3 = 0;
            while (i2 > 0) {
                int read = this.src.read(obj, i3, i2);
                if (read == -1) {
                    this.foundEOS = 1;
                    return (long) this.length;
                }
                i3 += read;
                i2 -= read;
                this.length += read;
            }
        }
        return (long) this.length;
    }

    public void seek(long j) throws IOException {
        if (this.ras != null) {
            this.ras.seek(j);
            return;
        }
        if (j < 0) {
            this.pointer = 0;
        } else {
            this.pointer = j;
        }
    }

    public void seek(int i) throws IOException {
        long j = ((long) i) & InternalZipConstants.ZIP_64_LIMIT;
        if (this.ras != 0) {
            this.ras.seek(j);
            return;
        }
        if (j < 0) {
            this.pointer = 0;
        } else {
            this.pointer = j;
        }
    }

    public final int readInt() throws IOException {
        int read = read();
        int read2 = read();
        int read3 = read();
        int read4 = read();
        if ((((read | read2) | read3) | read4) >= 0) {
            return (((read << 24) + (read2 << 16)) + (read3 << 8)) + read4;
        }
        throw new EOFException();
    }

    public final long readLong() throws IOException {
        return (((long) readInt()) << 32) + (((long) readInt()) & InternalZipConstants.ZIP_64_LIMIT);
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public final short readShort() throws IOException {
        int read = read();
        int read2 = read();
        if ((read | read2) >= 0) {
            return (short) ((read << 8) + read2);
        }
        throw new EOFException();
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public void close() throws IOException {
        if (this.ras != null) {
            this.ras.close();
            return;
        }
        this.data.removeAllElements();
        this.src.close();
    }
}
