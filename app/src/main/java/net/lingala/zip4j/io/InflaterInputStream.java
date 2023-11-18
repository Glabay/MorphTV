package net.lingala.zip4j.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.Inflater;
import net.lingala.zip4j.unzip.UnzipEngine;

public class InflaterInputStream extends PartInputStream {
    private byte[] buff = new byte[4096];
    private long bytesWritten;
    private Inflater inflater = new Inflater(true);
    private byte[] oneByteBuff = new byte[1];
    private long uncompressedSize;
    private UnzipEngine unzipEngine;

    public InflaterInputStream(RandomAccessFile randomAccessFile, long j, long j2, UnzipEngine unzipEngine) {
        super(randomAccessFile, j, j2, unzipEngine);
        this.unzipEngine = unzipEngine;
        this.bytesWritten = null;
        this.uncompressedSize = unzipEngine.getFileHeader().getUncompressedSize();
    }

    public int read() throws IOException {
        return read(this.oneByteBuff, 0, 1) == -1 ? -1 : this.oneByteBuff[0] & 255;
    }

    public int read(byte[] bArr) throws IOException {
        if (bArr != null) {
            return read(bArr, 0, bArr.length);
        }
        throw new NullPointerException("input buffer is null");
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (bArr == null) {
            throw new NullPointerException("input buffer is null");
        }
        if (i >= 0 && i2 >= 0) {
            if (i2 <= bArr.length - i) {
                if (i2 == 0) {
                    return null;
                }
                try {
                    if (this.bytesWritten >= this.uncompressedSize) {
                        finishInflating();
                        return -1;
                    }
                    while (true) {
                        int inflate = this.inflater.inflate(bArr, i, i2);
                        if (inflate != 0) {
                            this.bytesWritten += (long) inflate;
                            return inflate;
                        } else if (this.inflater.finished()) {
                            break;
                        } else if (this.inflater.needsDictionary()) {
                            break;
                        } else if (this.inflater.needsInput()) {
                            fill();
                        }
                    }
                    finishInflating();
                    return -1;
                } catch (byte[] bArr2) {
                    i = "Invalid ZLIB data format";
                    if (bArr2.getMessage() != 0) {
                        i = bArr2.getMessage();
                    }
                    if (!(this.unzipEngine == null || this.unzipEngine.getLocalFileHeader().isEncrypted() == null || this.unzipEngine.getLocalFileHeader().getEncryptionMethod() != null)) {
                        bArr2 = new StringBuffer(String.valueOf(i));
                        bArr2.append(" - Wrong Password?");
                        i = bArr2.toString();
                    }
                    throw new IOException(i);
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }

    private void finishInflating() throws IOException {
        do {
        } while (super.read(new byte[1024], 0, 1024) != -1);
        checkAndReadAESMacBytes();
    }

    private void fill() throws IOException {
        int read = super.read(this.buff, 0, this.buff.length);
        if (read == -1) {
            throw new EOFException("Unexpected end of ZLIB input stream");
        }
        this.inflater.setInput(this.buff, 0, read);
    }

    public long skip(long j) throws IOException {
        if (j < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        j = (int) Math.min(j, 2147483647L);
        byte[] bArr = new byte[512];
        int i = 0;
        while (i < j) {
            int i2 = j - i;
            if (i2 > bArr.length) {
                i2 = bArr.length;
            }
            i2 = read(bArr, 0, i2);
            if (i2 == -1) {
                break;
            }
            i += i2;
        }
        return (long) i;
    }

    public void seek(long j) throws IOException {
        super.seek(j);
    }

    public int available() {
        return this.inflater.finished() ^ 1;
    }

    public void close() throws IOException {
        this.inflater.end();
        super.close();
    }

    public UnzipEngine getUnzipEngine() {
        return super.getUnzipEngine();
    }
}
