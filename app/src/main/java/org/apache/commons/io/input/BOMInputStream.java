package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.ByteOrderMark;

public class BOMInputStream extends ProxyInputStream {
    private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator = new C14371();
    private final List<ByteOrderMark> boms;
    private ByteOrderMark byteOrderMark;
    private int fbIndex;
    private int fbLength;
    private int[] firstBytes;
    private final boolean include;
    private int markFbIndex;
    private boolean markedAtStart;

    /* renamed from: org.apache.commons.io.input.BOMInputStream$1 */
    static class C14371 implements Comparator<ByteOrderMark> {
        C14371() {
        }

        public int compare(ByteOrderMark byteOrderMark, ByteOrderMark byteOrderMark2) {
            byteOrderMark = byteOrderMark.length();
            byteOrderMark2 = byteOrderMark2.length();
            if (byteOrderMark > byteOrderMark2) {
                return -1;
            }
            return byteOrderMark2 > byteOrderMark ? 1 : null;
        }
    }

    public BOMInputStream(InputStream inputStream) {
        this(inputStream, false, ByteOrderMark.UTF_8);
    }

    public BOMInputStream(InputStream inputStream, boolean z) {
        this(inputStream, z, ByteOrderMark.UTF_8);
    }

    public BOMInputStream(InputStream inputStream, ByteOrderMark... byteOrderMarkArr) {
        this(inputStream, false, byteOrderMarkArr);
    }

    public BOMInputStream(InputStream inputStream, boolean z, ByteOrderMark... byteOrderMarkArr) {
        super(inputStream);
        if (byteOrderMarkArr != null) {
            if (byteOrderMarkArr.length != null) {
                this.include = z;
                inputStream = Arrays.asList(byteOrderMarkArr);
                Collections.sort(inputStream, ByteOrderMarkLengthComparator);
                this.boms = inputStream;
                return;
            }
        }
        throw new IllegalArgumentException("No BOMs specified");
    }

    public boolean hasBOM() throws IOException {
        return getBOM() != null;
    }

    public boolean hasBOM(ByteOrderMark byteOrderMark) throws IOException {
        if (this.boms.contains(byteOrderMark)) {
            getBOM();
            return (this.byteOrderMark == null || this.byteOrderMark.equals(byteOrderMark) == null) ? null : true;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stream not configure to detect ");
            stringBuilder.append(byteOrderMark);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public ByteOrderMark getBOM() throws IOException {
        if (this.firstBytes == null) {
            this.fbLength = 0;
            this.firstBytes = new int[((ByteOrderMark) this.boms.get(0)).length()];
            for (int i = 0; i < this.firstBytes.length; i++) {
                this.firstBytes[i] = this.in.read();
                this.fbLength++;
                if (this.firstBytes[i] < 0) {
                    break;
                }
            }
            this.byteOrderMark = find();
            if (!(this.byteOrderMark == null || this.include)) {
                if (this.byteOrderMark.length() < this.firstBytes.length) {
                    this.fbIndex = this.byteOrderMark.length();
                } else {
                    this.fbLength = 0;
                }
            }
        }
        return this.byteOrderMark;
    }

    public String getBOMCharsetName() throws IOException {
        getBOM();
        return this.byteOrderMark == null ? null : this.byteOrderMark.getCharsetName();
    }

    private int readFirstBytes() throws IOException {
        getBOM();
        if (this.fbIndex >= this.fbLength) {
            return -1;
        }
        int[] iArr = this.firstBytes;
        int i = this.fbIndex;
        this.fbIndex = i + 1;
        return iArr[i];
    }

    private ByteOrderMark find() {
        for (ByteOrderMark byteOrderMark : this.boms) {
            if (matches(byteOrderMark)) {
                return byteOrderMark;
            }
        }
        return null;
    }

    private boolean matches(ByteOrderMark byteOrderMark) {
        for (int i = 0; i < byteOrderMark.length(); i++) {
            if (byteOrderMark.get(i) != this.firstBytes[i]) {
                return false;
            }
        }
        return true;
    }

    public int read() throws IOException {
        int readFirstBytes = readFirstBytes();
        return readFirstBytes >= 0 ? readFirstBytes : this.in.read();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int i3 = 0;
        int i4 = 0;
        while (i2 > 0 && r0 >= 0) {
            i3 = readFirstBytes();
            if (i3 >= 0) {
                int i5 = i + 1;
                bArr[i] = (byte) (i3 & 255);
                i2--;
                i4++;
                i = i5;
            }
        }
        bArr = this.in.read(bArr, i, i2);
        if (bArr < null) {
            return i4 > 0 ? i4 : -1;
        } else {
            return i4 + bArr;
        }
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public synchronized void mark(int i) {
        this.markFbIndex = this.fbIndex;
        this.markedAtStart = this.firstBytes == null;
        this.in.mark(i);
    }

    public synchronized void reset() throws IOException {
        this.fbIndex = this.markFbIndex;
        if (this.markedAtStart) {
            this.firstBytes = null;
        }
        this.in.reset();
    }

    public long skip(long j) throws IOException {
        long j2;
        int i = 0;
        while (true) {
            j2 = (long) i;
            if (j > j2 && readFirstBytes() >= 0) {
                i++;
            }
        }
        return this.in.skip(j - j2) + j2;
    }
}
