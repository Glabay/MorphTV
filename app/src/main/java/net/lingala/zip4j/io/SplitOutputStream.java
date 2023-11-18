package net.lingala.zip4j.io;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class SplitOutputStream extends OutputStream {
    private long bytesWrittenForThisPart;
    private int currSplitFileCounter;
    private File outFile;
    private RandomAccessFile raf;
    private long splitLength;
    private File zipFile;

    public void flush() throws IOException {
    }

    public SplitOutputStream(String str) throws FileNotFoundException, ZipException {
        this(Zip4jUtil.isStringNotNullAndNotEmpty(str) ? new File(str) : null);
    }

    public SplitOutputStream(File file) throws FileNotFoundException, ZipException {
        this(file, -1);
    }

    public SplitOutputStream(String str, long j) throws FileNotFoundException, ZipException {
        this(!Zip4jUtil.isStringNotNullAndNotEmpty(str) ? new File(str) : null, j);
    }

    public SplitOutputStream(File file, long j) throws FileNotFoundException, ZipException {
        if (j < 0 || j >= PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH) {
            this.raf = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
            this.splitLength = j;
            this.outFile = file;
            this.zipFile = file;
            this.currSplitFileCounter = null;
            this.bytesWrittenForThisPart = 0;
            return;
        }
        throw new ZipException("split length less than minimum allowed split length of 65536 Bytes");
    }

    public void write(int i) throws IOException {
        write(new byte[]{(byte) i}, 0, 1);
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (i2 > 0) {
            if (this.splitLength == -1) {
                this.raf.write(bArr, i, i2);
                this.bytesWrittenForThisPart += (long) i2;
            } else if (this.splitLength < PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH) {
                throw new IOException("split length less than minimum allowed split length of 65536 Bytes");
            } else if (this.bytesWrittenForThisPart >= this.splitLength) {
                startNextSplitFile();
                this.raf.write(bArr, i, i2);
                this.bytesWrittenForThisPart = (long) i2;
            } else {
                long j = (long) i2;
                if (this.bytesWrittenForThisPart + j <= this.splitLength) {
                    this.raf.write(bArr, i, i2);
                    this.bytesWrittenForThisPart += j;
                } else if (isHeaderData(bArr)) {
                    startNextSplitFile();
                    this.raf.write(bArr, i, i2);
                    this.bytesWrittenForThisPart = j;
                } else {
                    this.raf.write(bArr, i, (int) (this.splitLength - this.bytesWrittenForThisPart));
                    startNextSplitFile();
                    this.raf.write(bArr, i + ((int) (this.splitLength - this.bytesWrittenForThisPart)), (int) (j - (this.splitLength - this.bytesWrittenForThisPart)));
                    this.bytesWrittenForThisPart = j - (this.splitLength - this.bytesWrittenForThisPart);
                }
            }
        }
    }

    private void startNextSplitFile() throws IOException {
        try {
            Object obj;
            File file;
            String zipFileNameWithoutExt = Zip4jUtil.getZipFileNameWithoutExt(this.outFile.getName());
            String absolutePath = this.zipFile.getAbsolutePath();
            if (this.outFile.getParent() == null) {
                obj = "";
            } else {
                StringBuffer stringBuffer = new StringBuffer(String.valueOf(this.outFile.getParent()));
                stringBuffer.append(System.getProperty("file.separator"));
                obj = stringBuffer.toString();
            }
            StringBuffer stringBuffer2;
            if (this.currSplitFileCounter < 9) {
                stringBuffer2 = new StringBuffer(String.valueOf(obj));
                stringBuffer2.append(zipFileNameWithoutExt);
                stringBuffer2.append(".z0");
                stringBuffer2.append(this.currSplitFileCounter + 1);
                file = new File(stringBuffer2.toString());
            } else {
                stringBuffer2 = new StringBuffer(String.valueOf(obj));
                stringBuffer2.append(zipFileNameWithoutExt);
                stringBuffer2.append(".z");
                stringBuffer2.append(this.currSplitFileCounter + 1);
                file = new File(stringBuffer2.toString());
            }
            this.raf.close();
            if (file.exists()) {
                StringBuffer stringBuffer3 = new StringBuffer("split file: ");
                stringBuffer3.append(file.getName());
                stringBuffer3.append(" already exists in the current directory, cannot rename this file");
                throw new IOException(stringBuffer3.toString());
            } else if (this.zipFile.renameTo(file)) {
                this.zipFile = new File(absolutePath);
                this.raf = new RandomAccessFile(this.zipFile, InternalZipConstants.WRITE_MODE);
                this.currSplitFileCounter++;
            } else {
                throw new IOException("cannot rename newly created split file");
            }
        } catch (ZipException e) {
            throw new IOException(e.getMessage());
        }
    }

    private boolean isHeaderData(byte[] bArr) {
        if (bArr != null) {
            if (bArr.length >= 4) {
                bArr = Raw.readIntLittleEndian(bArr, 0);
                long[] allHeaderSignatures = Zip4jUtil.getAllHeaderSignatures();
                if (allHeaderSignatures != null && allHeaderSignatures.length > 0) {
                    int i = 0;
                    while (i < allHeaderSignatures.length) {
                        if (allHeaderSignatures[i] != 134695760 && allHeaderSignatures[i] == ((long) bArr)) {
                            return 1;
                        }
                        i++;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public boolean checkBuffSizeAndStartNextSplitFile(int i) throws ZipException {
        if (i < 0) {
            throw new ZipException("negative buffersize for checkBuffSizeAndStartNextSplitFile");
        } else if (isBuffSizeFitForCurrSplitFile(i) != 0) {
            return false;
        } else {
            try {
                startNextSplitFile();
                this.bytesWrittenForThisPart = 0;
                return true;
            } catch (Throwable e) {
                throw new ZipException(e);
            }
        }
    }

    public boolean isBuffSizeFitForCurrSplitFile(int i) throws ZipException {
        if (i < 0) {
            throw new ZipException("negative buffersize for isBuffSizeFitForCurrSplitFile");
        } else if (this.splitLength < PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH || this.bytesWrittenForThisPart + ((long) i) <= this.splitLength) {
            return true;
        } else {
            return false;
        }
    }

    public void seek(long j) throws IOException {
        this.raf.seek(j);
    }

    public void close() throws IOException {
        if (this.raf != null) {
            this.raf.close();
        }
    }

    public long getFilePointer() throws IOException {
        return this.raf.getFilePointer();
    }

    public boolean isSplitZipFile() {
        return this.splitLength != -1;
    }

    public long getSplitLength() {
        return this.splitLength;
    }

    public int getCurrSplitFileCounter() {
        return this.currSplitFileCounter;
    }
}
