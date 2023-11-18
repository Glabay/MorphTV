package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class ReversedLinesFileReader implements Closeable {
    private final int avoidNewlineSplitBufferSize;
    private final int blockSize;
    private final int byteDecrement;
    private FilePart currentFilePart;
    private final Charset encoding;
    private final byte[][] newLineSequences;
    private final RandomAccessFile randomAccessFile;
    private final long totalBlockCount;
    private final long totalByteLength;
    private boolean trailingNewlineOfFileSkipped;

    private class FilePart {
        private int currentLastBytePos;
        private final byte[] data;
        private byte[] leftOver;
        private final long no;

        private FilePart(long j, int i, byte[] bArr) throws IOException {
            this.no = j;
            this.data = new byte[((bArr != null ? bArr.length : 0) + i)];
            long access$300 = (j - 1) * ((long) ReversedLinesFileReader.this.blockSize);
            if (j > 0) {
                ReversedLinesFileReader.this.randomAccessFile.seek(access$300);
                if (ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, i) != i) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
                }
            }
            if (bArr != null) {
                System.arraycopy(bArr, 0, this.data, i, bArr.length);
            }
            this.currentLastBytePos = this.data.length - 1;
            this.leftOver = null;
        }

        private FilePart rollOver() throws IOException {
            StringBuilder stringBuilder;
            if (this.currentLastBytePos > -1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=");
                stringBuilder.append(this.currentLastBytePos);
                throw new IllegalStateException(stringBuilder.toString());
            } else if (this.no > 1) {
                return new FilePart(this.no - 1, ReversedLinesFileReader.this.blockSize, this.leftOver);
            } else {
                if (this.leftOver == null) {
                    return null;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected leftover of the last block: leftOverOfThisFilePart=");
                stringBuilder.append(new String(this.leftOver, ReversedLinesFileReader.this.encoding));
                throw new IllegalStateException(stringBuilder.toString());
            }
        }

        private String readLine() throws IOException {
            String str;
            Object obj = this.no == 1 ? 1 : null;
            int i = this.currentLastBytePos;
            while (i > -1) {
                if (obj == null && i < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
                    createLeftOver();
                    break;
                }
                int newLineMatchByteCount = getNewLineMatchByteCount(this.data, i);
                if (newLineMatchByteCount <= 0) {
                    i -= ReversedLinesFileReader.this.byteDecrement;
                    if (i < 0) {
                        createLeftOver();
                        break;
                    }
                }
                int i2 = i + 1;
                int i3 = (this.currentLastBytePos - i2) + 1;
                if (i3 < 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected negative line length=");
                    stringBuilder.append(i3);
                    throw new IllegalStateException(stringBuilder.toString());
                }
                Object obj2 = new byte[i3];
                System.arraycopy(this.data, i2, obj2, 0, i3);
                str = new String(obj2, ReversedLinesFileReader.this.encoding);
                this.currentLastBytePos = i - newLineMatchByteCount;
                if (obj == null && this.leftOver != null) {
                    str = new String(this.leftOver, ReversedLinesFileReader.this.encoding);
                    this.leftOver = null;
                    return str;
                }
            }
            str = null;
            return obj == null ? str : str;
        }

        private void createLeftOver() {
            int i = this.currentLastBytePos + 1;
            if (i > 0) {
                this.leftOver = new byte[i];
                System.arraycopy(this.data, 0, this.leftOver, 0, i);
            } else {
                this.leftOver = null;
            }
            this.currentLastBytePos = -1;
        }

        private int getNewLineMatchByteCount(byte[] bArr, int i) {
            for (byte[] bArr2 : ReversedLinesFileReader.this.newLineSequences) {
                int i2 = 1;
                for (int length = bArr2.length - 1; length >= 0; length--) {
                    int length2 = (i + length) - (bArr2.length - 1);
                    length2 = (length2 < 0 || bArr[length2] != bArr2[length]) ? 0 : 1;
                    i2 &= length2;
                }
                if (i2 != 0) {
                    return bArr2.length;
                }
            }
            return 0;
        }
    }

    @Deprecated
    public ReversedLinesFileReader(File file) throws IOException {
        this(file, 4096, Charset.defaultCharset());
    }

    public ReversedLinesFileReader(File file, Charset charset) throws IOException {
        this(file, 4096, charset);
    }

    public ReversedLinesFileReader(File file, int i, Charset charset) throws IOException {
        int i2;
        this.trailingNewlineOfFileSkipped = false;
        this.blockSize = i;
        this.encoding = charset;
        Charset toCharset = Charsets.toCharset(charset);
        if (toCharset.newEncoder().maxBytesPerChar() == 1.0f) {
            this.byteDecrement = 1;
        } else if (toCharset == StandardCharsets.UTF_8) {
            this.byteDecrement = 1;
        } else {
            if (!(toCharset == Charset.forName("Shift_JIS") || toCharset == Charset.forName("windows-31j") || toCharset == Charset.forName("x-windows-949") || toCharset == Charset.forName("gbk"))) {
                if (toCharset != Charset.forName("x-windows-950")) {
                    if (toCharset != StandardCharsets.UTF_16BE) {
                        if (toCharset != StandardCharsets.UTF_16LE) {
                            if (toCharset == StandardCharsets.UTF_16) {
                                throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
                            }
                            i = new StringBuilder();
                            i.append("Encoding ");
                            i.append(charset);
                            i.append(" is not supported yet (feel free to submit a patch)");
                            throw new UnsupportedEncodingException(i.toString());
                        }
                    }
                    this.byteDecrement = 2;
                }
            }
            this.byteDecrement = 1;
        }
        this.newLineSequences = new byte[][]{IOUtils.LINE_SEPARATOR_WINDOWS.getBytes(charset), "\n".getBytes(charset), StringUtils.CR.getBytes(charset)};
        this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
        this.randomAccessFile = new RandomAccessFile(file, InternalZipConstants.READ_MODE);
        this.totalByteLength = this.randomAccessFile.length();
        long j = (long) i;
        file = (int) (this.totalByteLength % j);
        if (file > null) {
            this.totalBlockCount = (this.totalByteLength / j) + 1;
        } else {
            this.totalBlockCount = this.totalByteLength / j;
            if (this.totalByteLength > 0) {
                i2 = i;
                this.currentFilePart = new FilePart(this.totalBlockCount, i2, null);
            }
        }
        i2 = file;
        this.currentFilePart = new FilePart(this.totalBlockCount, i2, null);
    }

    public ReversedLinesFileReader(File file, int i, String str) throws IOException {
        this(file, i, Charsets.toCharset(str));
    }

    public String readLine() throws IOException {
        String access$100 = this.currentFilePart.readLine();
        while (access$100 == null) {
            this.currentFilePart = this.currentFilePart.rollOver();
            if (this.currentFilePart == null) {
                break;
            }
            access$100 = this.currentFilePart.readLine();
        }
        if (!"".equals(access$100) || this.trailingNewlineOfFileSkipped) {
            return access$100;
        }
        this.trailingNewlineOfFileSkipped = true;
        return readLine();
    }

    public void close() throws IOException {
        this.randomAccessFile.close();
    }
}
