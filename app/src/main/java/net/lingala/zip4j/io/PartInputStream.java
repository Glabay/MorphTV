package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import net.lingala.zip4j.crypto.AESDecrypter;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.unzip.UnzipEngine;

public class PartInputStream extends BaseInputStream {
    private byte[] aesBlockByte = new byte[16];
    private int aesBytesReturned = 0;
    private long bytesRead;
    private int count = -1;
    private IDecrypter decrypter;
    private boolean isAESEncryptedFile = false;
    private long length;
    private byte[] oneByteBuff = new byte[1];
    private RandomAccessFile raf;
    private UnzipEngine unzipEngine;

    public PartInputStream(RandomAccessFile randomAccessFile, long j, long j2, UnzipEngine unzipEngine) {
        j = 1;
        this.raf = randomAccessFile;
        this.unzipEngine = unzipEngine;
        this.decrypter = unzipEngine.getDecrypter();
        this.bytesRead = 0;
        this.length = j2;
        if (unzipEngine.getFileHeader().isEncrypted() == null || unzipEngine.getFileHeader().getEncryptionMethod() != 99) {
            j = null;
        }
        this.isAESEncryptedFile = j;
    }

    public int available() {
        long j = this.length - this.bytesRead;
        return j > 2147483647L ? Integer.MAX_VALUE : (int) j;
    }

    public int read() throws IOException {
        int i = -1;
        if (this.bytesRead >= this.length) {
            return -1;
        }
        if (this.isAESEncryptedFile) {
            if (this.aesBytesReturned == 0 || this.aesBytesReturned == 16) {
                if (read(this.aesBlockByte) == -1) {
                    return -1;
                }
                this.aesBytesReturned = 0;
            }
            byte[] bArr = this.aesBlockByte;
            int i2 = this.aesBytesReturned;
            this.aesBytesReturned = i2 + 1;
            return bArr[i2] & 255;
        }
        if (read(this.oneByteBuff, 0, 1) != -1) {
            i = this.oneByteBuff[0] & 255;
        }
        return i;
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (((long) i2) > this.length - this.bytesRead) {
            i2 = (int) (this.length - this.bytesRead);
            if (i2 == 0) {
                checkAndReadAESMacBytes();
                return -1;
            }
        }
        if ((this.unzipEngine.getDecrypter() instanceof AESDecrypter) && this.bytesRead + ((long) i2) < this.length) {
            int i3 = i2 % 16;
            if (i3 != 0) {
                i2 -= i3;
            }
        }
        synchronized (this.raf) {
            this.count = this.raf.read(bArr, i, i2);
            if (this.count < i2 && this.unzipEngine.getZipModel().isSplitArchive()) {
                this.raf.close();
                this.raf = this.unzipEngine.startNextSplitFile();
                if (this.count < 0) {
                    this.count = 0;
                }
                i2 = this.raf.read(bArr, this.count, i2 - this.count);
                if (i2 > 0) {
                    this.count += i2;
                }
            }
        }
        if (this.count > 0) {
            if (this.decrypter != 0) {
                try {
                    this.decrypter.decryptData(bArr, i, this.count);
                } catch (byte[] bArr2) {
                    throw new IOException(bArr2.getMessage());
                }
            }
            this.bytesRead += (long) this.count;
        }
        if (this.bytesRead >= this.length) {
            checkAndReadAESMacBytes();
        }
        return this.count;
    }

    protected void checkAndReadAESMacBytes() throws IOException {
        if (this.isAESEncryptedFile && this.decrypter != null && (this.decrypter instanceof AESDecrypter) && ((AESDecrypter) this.decrypter).getStoredMac() == null) {
            byte[] bArr = new byte[10];
            int read = this.raf.read(bArr);
            if (read != 10) {
                if (this.unzipEngine.getZipModel().isSplitArchive()) {
                    this.raf.close();
                    this.raf = this.unzipEngine.startNextSplitFile();
                    this.raf.read(bArr, read, 10 - read);
                } else {
                    throw new IOException("Error occured while reading stored AES authentication bytes");
                }
            }
            ((AESDecrypter) this.unzipEngine.getDecrypter()).setStoredMac(bArr);
        }
    }

    public long skip(long j) throws IOException {
        if (j < 0) {
            throw new IllegalArgumentException();
        }
        if (j > this.length - this.bytesRead) {
            j = this.length - this.bytesRead;
        }
        this.bytesRead += j;
        return j;
    }

    public void close() throws IOException {
        this.raf.close();
    }

    public void seek(long j) throws IOException {
        this.raf.seek(j);
    }

    public UnzipEngine getUnzipEngine() {
        return this.unzipEngine;
    }
}
