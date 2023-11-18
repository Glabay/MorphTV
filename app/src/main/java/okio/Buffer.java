package okio;

import android.support.v4.media.session.PlaybackStateCompat;
import com.google.android.exoplayer2.C0649C;
import com.google.android.gms.cast.framework.media.NotificationOptions;
import com.google.android.gms.common.util.AndroidUtilsLight;
import com.google.common.base.Ascii;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import kotlin.jvm.internal.CharCompanionObject;
import net.lingala.zip4j.util.InternalZipConstants;

public final class Buffer implements BufferedSource, BufferedSink, Cloneable {
    private static final byte[] DIGITS = new byte[]{(byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102};
    static final int REPLACEMENT_CHARACTER = 65533;
    @Nullable
    Segment head;
    long size;

    public Buffer buffer() {
        return this;
    }

    public void close() {
    }

    public BufferedSink emit() {
        return this;
    }

    public Buffer emitCompleteSegments() {
        return this;
    }

    public void flush() {
    }

    public long size() {
        return this.size;
    }

    public OutputStream outputStream() {
        return new Buffer$1(this);
    }

    public boolean exhausted() {
        return this.size == 0;
    }

    public void require(long j) throws EOFException {
        if (this.size < j) {
            throw new EOFException();
        }
    }

    public boolean request(long j) {
        return this.size >= j ? 1 : 0;
    }

    public InputStream inputStream() {
        return new Buffer$2(this);
    }

    public Buffer copyTo(OutputStream outputStream) throws IOException {
        return copyTo(outputStream, 0, this.size);
    }

    public Buffer copyTo(OutputStream outputStream, long j, long j2) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, j, j2);
        if (j2 == 0) {
            return this;
        }
        Segment segment = this.head;
        while (j >= ((long) (segment.limit - segment.pos))) {
            long j3 = j - ((long) (segment.limit - segment.pos));
            segment = segment.next;
            j = j3;
        }
        while (j2 > 0) {
            j = (int) (((long) segment.pos) + j);
            int min = (int) Math.min((long) (segment.limit - j), j2);
            outputStream.write(segment.data, j, min);
            long j4 = j2 - ((long) min);
            segment = segment.next;
            j = 0;
            j2 = j4;
        }
        return this;
    }

    public Buffer copyTo(Buffer buffer, long j, long j2) {
        if (buffer == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, j, j2);
        if (j2 == 0) {
            return this;
        }
        buffer.size += j2;
        Segment segment = this.head;
        while (j >= ((long) (segment.limit - segment.pos))) {
            long j3 = j - ((long) (segment.limit - segment.pos));
            segment = segment.next;
            j = j3;
        }
        while (j2 > 0) {
            Segment segment2 = new Segment(segment);
            segment2.pos = (int) (((long) segment2.pos) + j);
            segment2.limit = Math.min(segment2.pos + ((int) j2), segment2.limit);
            if (buffer.head == null) {
                segment2.prev = segment2;
                segment2.next = segment2;
                buffer.head = segment2;
            } else {
                buffer.head.prev.push(segment2);
            }
            long j4 = j2 - ((long) (segment2.limit - segment2.pos));
            segment = segment.next;
            j = 0;
            j2 = j4;
        }
        return this;
    }

    public Buffer writeTo(OutputStream outputStream) throws IOException {
        return writeTo(outputStream, this.size);
    }

    public Buffer writeTo(OutputStream outputStream, long j) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, 0, j);
        Segment segment = this.head;
        while (j > 0) {
            int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
            outputStream.write(segment.data, segment.pos, min);
            segment.pos += min;
            long j2 = (long) min;
            this.size -= j2;
            long j3 = j - j2;
            if (segment.pos == segment.limit) {
                j = segment.pop();
                this.head = j;
                SegmentPool.recycle(segment);
                segment = j;
            }
            j = j3;
        }
        return this;
    }

    public Buffer readFrom(InputStream inputStream) throws IOException {
        readFrom(inputStream, Long.MAX_VALUE, true);
        return this;
    }

    public Buffer readFrom(InputStream inputStream, long j) throws IOException {
        if (j < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("byteCount < 0: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        readFrom(inputStream, j, false);
        return this;
    }

    private void readFrom(InputStream inputStream, long j, boolean z) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        while (true) {
            if (j <= 0) {
                if (!z) {
                    return;
                }
            }
            Segment writableSegment = writableSegment(1);
            int read = inputStream.read(writableSegment.data, writableSegment.limit, (int) Math.min(j, (long) (8192 - writableSegment.limit)));
            if (read == -1) {
                break;
            }
            writableSegment.limit += read;
            long j2 = (long) read;
            this.size += j2;
            j -= j2;
        }
        if (!z) {
            throw new EOFException();
        }
    }

    public long completeSegmentByteCount() {
        long j = this.size;
        if (j == 0) {
            return 0;
        }
        Segment segment = this.head.prev;
        if (segment.limit < 8192 && segment.owner) {
            j -= (long) (segment.limit - segment.pos);
        }
        return j;
    }

    public byte readByte() {
        if (this.size == 0) {
            throw new IllegalStateException("size == 0");
        }
        Segment segment = this.head;
        int i = segment.pos;
        int i2 = segment.limit;
        int i3 = i + 1;
        byte b = segment.data[i];
        this.size--;
        if (i3 == i2) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = i3;
        }
        return b;
    }

    public byte getByte(long j) {
        Util.checkOffsetAndCount(this.size, j, 1);
        Segment segment = this.head;
        while (true) {
            long j2 = (long) (segment.limit - segment.pos);
            if (j < j2) {
                return segment.data[segment.pos + ((int) j)];
            }
            long j3 = j - j2;
            segment = segment.next;
            j = j3;
        }
    }

    public short readShort() {
        if (this.size < 2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("size < 2: ");
            stringBuilder.append(this.size);
            throw new IllegalStateException(stringBuilder.toString());
        }
        Segment segment = this.head;
        int i = segment.pos;
        int i2 = segment.limit;
        if (i2 - i < 2) {
            return (short) (((readByte() & 255) << 8) | (readByte() & 255));
        }
        byte[] bArr = segment.data;
        int i3 = i + 1;
        int i4 = i3 + 1;
        i = ((bArr[i] & 255) << 8) | (bArr[i3] & 255);
        this.size -= 2;
        if (i4 == i2) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = i4;
        }
        return (short) i;
    }

    public int readInt() {
        if (this.size < 4) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("size < 4: ");
            stringBuilder.append(this.size);
            throw new IllegalStateException(stringBuilder.toString());
        }
        Segment segment = this.head;
        int i = segment.pos;
        int i2 = segment.limit;
        if (i2 - i < 4) {
            return ((((readByte() & 255) << 24) | ((readByte() & 255) << 16)) | ((readByte() & 255) << 8)) | (readByte() & 255);
        }
        byte[] bArr = segment.data;
        int i3 = i + 1;
        int i4 = i3 + 1;
        i = ((bArr[i] & 255) << 24) | ((bArr[i3] & 255) << 16);
        i3 = i4 + 1;
        i |= (bArr[i4] & 255) << 8;
        i4 = i3 + 1;
        i |= bArr[i3] & 255;
        this.size -= 4;
        if (i4 == i2) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = i4;
        }
        return i;
    }

    public long readLong() {
        if (this.size < 8) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("size < 8: ");
            stringBuilder.append(r0.size);
            throw new IllegalStateException(stringBuilder.toString());
        }
        Segment segment = r0.head;
        int i = segment.pos;
        int i2 = segment.limit;
        if (i2 - i < 8) {
            return ((((long) readInt()) & InternalZipConstants.ZIP_64_LIMIT) << 32) | (((long) readInt()) & InternalZipConstants.ZIP_64_LIMIT);
        }
        byte[] bArr = segment.data;
        int i3 = i + 1;
        i = i3 + 1;
        i3 = i + 1;
        i = i3 + 1;
        int i4 = i + 1;
        i = i4 + 1;
        i4 = i + 1;
        i = i4 + 1;
        long j = ((((((((((long) bArr[i]) & 255) << 56) | ((((long) bArr[i3]) & 255) << 48)) | ((((long) bArr[i]) & 255) << 40)) | ((((long) bArr[i3]) & 255) << 32)) | ((((long) bArr[i]) & 255) << 24)) | ((((long) bArr[i4]) & 255) << 16)) | ((((long) bArr[i]) & 255) << 8)) | (((long) bArr[i4]) & 255);
        r0.size -= 8;
        if (i == i2) {
            r0.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = i;
        }
        return j;
    }

    public short readShortLe() {
        return Util.reverseBytesShort(readShort());
    }

    public int readIntLe() {
        return Util.reverseBytesInt(readInt());
    }

    public long readLongLe() {
        return Util.reverseBytesLong(readLong());
    }

    public long readDecimalLong() {
        long j = 0;
        if (this.size == 0) {
            throw new IllegalStateException("size == 0");
        }
        int i = 0;
        long j2 = -7;
        Object obj = null;
        Object obj2 = null;
        do {
            Segment segment = r0.head;
            byte[] bArr = segment.data;
            int i2 = segment.pos;
            int i3 = segment.limit;
            while (i2 < i3) {
                byte b = bArr[i2];
                if (b >= (byte) 48 && b <= (byte) 57) {
                    int i4 = 48 - b;
                    if (j >= -922337203685477580L) {
                        if (j != -922337203685477580L || ((long) i4) >= j2) {
                            j = (j * 10) + ((long) i4);
                        }
                    }
                    Buffer writeByte = new Buffer().writeDecimalLong(j).writeByte((int) b);
                    if (obj == null) {
                        writeByte.readByte();
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Number too large: ");
                    stringBuilder.append(writeByte.readUtf8());
                    throw new NumberFormatException(stringBuilder.toString());
                } else if (b != (byte) 45 || i != 0) {
                    if (i != 0) {
                        obj2 = 1;
                        if (i2 != i3) {
                            r0.head = segment.pop();
                            SegmentPool.recycle(segment);
                        } else {
                            segment.pos = i2;
                        }
                        if (obj2 == null) {
                            break;
                        }
                    } else {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Expected leading [0-9] or '-' character but was 0x");
                        stringBuilder2.append(Integer.toHexString(b));
                        throw new NumberFormatException(stringBuilder2.toString());
                    }
                } else {
                    j2--;
                    obj = 1;
                }
                i2++;
                i++;
            }
            if (i2 != i3) {
                segment.pos = i2;
            } else {
                r0.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            if (obj2 == null) {
                break;
            }
        } while (r0.head != null);
        r0.size -= (long) i;
        return obj != null ? j : -j;
    }

    public long readHexadecimalUnsignedLong() {
        if (this.size == 0) {
            throw new IllegalStateException("size == 0");
        }
        int i = 0;
        long j = 0;
        Object obj = null;
        do {
            Segment segment = r0.head;
            byte[] bArr = segment.data;
            int i2 = segment.pos;
            int i3 = segment.limit;
            while (i2 < i3) {
                int i4;
                int i5 = bArr[i2];
                if (i5 >= (byte) 48 && i5 <= (byte) 57) {
                    i4 = i5 - 48;
                } else if (i5 >= (byte) 97 && i5 <= (byte) 102) {
                    i4 = (i5 - 97) + 10;
                } else if (i5 < (byte) 65 || i5 > (byte) 70) {
                    if (i != 0) {
                        obj = 1;
                        if (i2 != i3) {
                            r0.head = segment.pop();
                            SegmentPool.recycle(segment);
                        } else {
                            segment.pos = i2;
                        }
                        if (obj == null) {
                            break;
                        }
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Expected leading [0-9a-fA-F] character but was 0x");
                        stringBuilder.append(Integer.toHexString(i5));
                        throw new NumberFormatException(stringBuilder.toString());
                    }
                } else {
                    i4 = (i5 - 65) + 10;
                }
                if ((j & -1152921504606846976L) != 0) {
                    Buffer writeByte = new Buffer().writeHexadecimalUnsignedLong(j).writeByte(i5);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Number too large: ");
                    stringBuilder2.append(writeByte.readUtf8());
                    throw new NumberFormatException(stringBuilder2.toString());
                }
                i2++;
                i++;
                j = (j << 4) | ((long) i4);
            }
            if (i2 != i3) {
                segment.pos = i2;
            } else {
                r0.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            if (obj == null) {
                break;
            }
        } while (r0.head != null);
        r0.size -= (long) i;
        return j;
    }

    public ByteString readByteString() {
        return new ByteString(readByteArray());
    }

    public ByteString readByteString(long j) throws EOFException {
        return new ByteString(readByteArray(j));
    }

    public int select(Options options) {
        Segment segment = this.head;
        if (segment == null) {
            return options.indexOf(ByteString.EMPTY);
        }
        options = options.byteStrings;
        int length = options.length;
        for (int i = 0; i < length; i++) {
            ByteString byteString = options[i];
            if (this.size >= ((long) byteString.size())) {
                if (rangeEquals(segment, segment.pos, byteString, 0, byteString.size())) {
                    try {
                        skip((long) byteString.size());
                        return i;
                    } catch (Options options2) {
                        throw new AssertionError(options2);
                    }
                }
            }
        }
        return -1;
    }

    int selectPrefix(Options options) {
        Segment segment = this.head;
        options = options.byteStrings;
        int length = options.length;
        int i = 0;
        while (i < length) {
            ByteString byteString = options[i];
            int min = (int) Math.min(this.size, (long) byteString.size());
            if (min != 0) {
                if (!rangeEquals(segment, segment.pos, byteString, 0, min)) {
                    i++;
                }
            }
            return i;
        }
        return -1;
    }

    public void readFully(Buffer buffer, long j) throws EOFException {
        if (this.size < j) {
            buffer.write(this, this.size);
            throw new EOFException();
        } else {
            buffer.write(this, j);
        }
    }

    public long readAll(Sink sink) throws IOException {
        long j = this.size;
        if (j > 0) {
            sink.write(this, j);
        }
        return j;
    }

    public String readUtf8() {
        try {
            return readString(this.size, Util.UTF_8);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public String readUtf8(long j) throws EOFException {
        return readString(j, Util.UTF_8);
    }

    public String readString(Charset charset) {
        try {
            return readString(this.size, charset);
        } catch (Charset charset2) {
            throw new AssertionError(charset2);
        }
    }

    public String readString(long j, Charset charset) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0, j);
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        } else if (j > 2147483647L) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("byteCount > Integer.MAX_VALUE: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (j == 0) {
            return "";
        } else {
            Segment segment = this.head;
            if (((long) segment.pos) + j > ((long) segment.limit)) {
                return new String(readByteArray(j), charset);
            }
            String str = new String(segment.data, segment.pos, (int) j, charset);
            segment.pos = (int) (((long) segment.pos) + j);
            this.size -= j;
            if (segment.pos == segment.limit) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            return str;
        }
    }

    @Nullable
    public String readUtf8Line() throws EOFException {
        long indexOf = indexOf((byte) 10);
        if (indexOf != -1) {
            return readUtf8Line(indexOf);
        }
        return this.size != 0 ? readUtf8(this.size) : null;
    }

    public String readUtf8LineStrict() throws EOFException {
        return readUtf8LineStrict(Long.MAX_VALUE);
    }

    public String readUtf8LineStrict(long j) throws EOFException {
        if (j < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("limit < 0: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        long j2 = Long.MAX_VALUE;
        if (j != Long.MAX_VALUE) {
            j2 = j + 1;
        }
        long indexOf = indexOf((byte) 10, 0, j2);
        if (indexOf != -1) {
            return readUtf8Line(indexOf);
        }
        if (j2 < size() && getByte(j2 - 1) == Ascii.CR && getByte(j2) == (byte) 10) {
            return readUtf8Line(j2);
        }
        Buffer buffer = new Buffer();
        copyTo(buffer, 0, Math.min(32, size()));
        stringBuilder = new StringBuilder();
        stringBuilder.append("\\n not found: limit=");
        stringBuilder.append(Math.min(size(), j));
        stringBuilder.append(" content=");
        stringBuilder.append(buffer.readByteString().hex());
        stringBuilder.append(8230);
        throw new EOFException(stringBuilder.toString());
    }

    String readUtf8Line(long j) throws EOFException {
        if (j > 0) {
            long j2 = j - 1;
            if (getByte(j2) == Ascii.CR) {
                j = readUtf8(j2);
                skip(2);
                return j;
            }
        }
        j = readUtf8(j);
        skip(1);
        return j;
    }

    public int readUtf8CodePoint() throws EOFException {
        if (this.size == 0) {
            throw new EOFException();
        }
        int i;
        int i2;
        byte b = getByte(0);
        int i3 = 1;
        int i4;
        if ((b & 128) == 0) {
            i = b & 127;
            i2 = 1;
            i4 = 0;
        } else if ((b & 224) == 192) {
            i = b & 31;
            i2 = 2;
            i4 = 128;
        } else if ((b & 240) == 224) {
            i = b & 15;
            i2 = 3;
            i4 = 2048;
        } else if ((b & 248) == 240) {
            i = b & 7;
            i2 = 4;
            i4 = 65536;
        } else {
            skip(1);
            return REPLACEMENT_CHARACTER;
        }
        long j = (long) i2;
        if (this.size < j) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("size < ");
            stringBuilder.append(i2);
            stringBuilder.append(": ");
            stringBuilder.append(this.size);
            stringBuilder.append(" (to read code point prefixed 0x");
            stringBuilder.append(Integer.toHexString(b));
            stringBuilder.append(")");
            throw new EOFException(stringBuilder.toString());
        }
        while (i3 < i2) {
            long j2 = (long) i3;
            b = getByte(j2);
            if ((b & 192) == 128) {
                i = (i << 6) | (b & 63);
                i3++;
            } else {
                skip(j2);
                return REPLACEMENT_CHARACTER;
            }
        }
        skip(j);
        if (i > 1114111) {
            return REPLACEMENT_CHARACTER;
        }
        if ((i < 55296 || i > 57343) && i >= r6) {
            return i;
        }
        return REPLACEMENT_CHARACTER;
    }

    public byte[] readByteArray() {
        try {
            return readByteArray(this.size);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public byte[] readByteArray(long j) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0, j);
        if (j > 2147483647L) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("byteCount > Integer.MAX_VALUE: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        j = new byte[((int) j)];
        readFully(j);
        return j;
    }

    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    public void readFully(byte[] bArr) throws EOFException {
        int i = 0;
        while (i < bArr.length) {
            int read = read(bArr, i, bArr.length - i);
            if (read == -1) {
                throw new EOFException();
            }
            i += read;
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        i2 = Math.min(i2, segment.limit - segment.pos);
        System.arraycopy(segment.data, segment.pos, bArr, i, i2);
        segment.pos += i2;
        this.size -= (long) i2;
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return i2;
    }

    public void clear() {
        try {
            skip(this.size);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public void skip(long j) throws EOFException {
        while (j > 0) {
            if (this.head == null) {
                throw new EOFException();
            }
            int min = (int) Math.min(j, (long) (this.head.limit - this.head.pos));
            long j2 = (long) min;
            this.size -= j2;
            long j3 = j - j2;
            j = this.head;
            j.pos += min;
            if (this.head.pos == this.head.limit) {
                j = this.head;
                this.head = j.pop();
                SegmentPool.recycle(j);
            }
            j = j3;
        }
    }

    public Buffer write(ByteString byteString) {
        if (byteString == null) {
            throw new IllegalArgumentException("byteString == null");
        }
        byteString.write(this);
        return this;
    }

    public Buffer writeUtf8(String str) {
        return writeUtf8(str, 0, str.length());
    }

    public Buffer writeUtf8(String str, int i, int i2) {
        if (str == null) {
            throw new IllegalArgumentException("string == null");
        } else if (i < 0) {
            i2 = new StringBuilder();
            i2.append("beginIndex < 0: ");
            i2.append(i);
            throw new IllegalArgumentException(i2.toString());
        } else if (i2 < i) {
            r0 = new StringBuilder();
            r0.append("endIndex < beginIndex: ");
            r0.append(i2);
            r0.append(" < ");
            r0.append(i);
            throw new IllegalArgumentException(r0.toString());
        } else if (i2 > str.length()) {
            r0 = new StringBuilder();
            r0.append("endIndex > string.length: ");
            r0.append(i2);
            r0.append(" > ");
            r0.append(str.length());
            throw new IllegalArgumentException(r0.toString());
        } else {
            while (i < i2) {
                char charAt = str.charAt(i);
                int i3;
                int min;
                int i4;
                if (charAt < '') {
                    Segment writableSegment = writableSegment(1);
                    byte[] bArr = writableSegment.data;
                    i3 = writableSegment.limit - i;
                    min = Math.min(i2, 8192 - i3);
                    int i5 = i + 1;
                    bArr[i + i3] = (byte) charAt;
                    while (i5 < min) {
                        i = str.charAt(i5);
                        if (i >= 128) {
                            break;
                        }
                        i4 = i5 + 1;
                        bArr[i5 + i3] = (byte) i;
                        i5 = i4;
                    }
                    i3 = (i3 + i5) - writableSegment.limit;
                    writableSegment.limit += i3;
                    this.size += (long) i3;
                    i = i5;
                } else if (charAt < 'ࠀ') {
                    writeByte((charAt >> 6) | 192);
                    writeByte((charAt & 63) | 128);
                    i++;
                } else {
                    if (charAt >= '?') {
                        if (charAt <= '?') {
                            i3 = i + 1;
                            if (i3 < i2) {
                                min = str.charAt(i3);
                            } else {
                                min = 0;
                            }
                            if (charAt <= CharCompanionObject.MAX_HIGH_SURROGATE && min >= 56320) {
                                if (min <= 57343) {
                                    i4 = (((charAt & -55297) << 10) | (-56321 & min)) + 65536;
                                    writeByte((i4 >> 18) | 240);
                                    writeByte(((i4 >> 12) & 63) | 128);
                                    writeByte(((i4 >> 6) & 63) | 128);
                                    writeByte((i4 & 63) | 128);
                                    i += 2;
                                }
                            }
                            writeByte(63);
                            i = i3;
                        }
                    }
                    writeByte((charAt >> 12) | 224);
                    writeByte(((charAt >> 6) & 63) | 128);
                    writeByte((charAt & 63) | 128);
                    i++;
                }
            }
            return this;
        }
    }

    public Buffer writeUtf8CodePoint(int i) {
        if (i < 128) {
            writeByte(i);
        } else if (i < 2048) {
            writeByte((i >> 6) | 192);
            writeByte((i & 63) | 128);
        } else if (i < 65536) {
            if (i < 55296 || i > 57343) {
                writeByte((i >> 12) | 224);
                writeByte(((i >> 6) & 63) | 128);
                writeByte((i & 63) | 128);
            } else {
                writeByte(63);
            }
        } else if (i <= 1114111) {
            writeByte((i >> 18) | 240);
            writeByte(((i >> 12) & 63) | 128);
            writeByte(((i >> 6) & 63) | 128);
            writeByte((i & 63) | 128);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected code point: ");
            stringBuilder.append(Integer.toHexString(i));
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        return this;
    }

    public Buffer writeString(String str, Charset charset) {
        return writeString(str, 0, str.length(), charset);
    }

    public Buffer writeString(String str, int i, int i2, Charset charset) {
        if (str == null) {
            throw new IllegalArgumentException("string == null");
        } else if (i < 0) {
            i2 = new StringBuilder();
            i2.append("beginIndex < 0: ");
            i2.append(i);
            throw new IllegalAccessError(i2.toString());
        } else if (i2 < i) {
            charset = new StringBuilder();
            charset.append("endIndex < beginIndex: ");
            charset.append(i2);
            charset.append(" < ");
            charset.append(i);
            throw new IllegalArgumentException(charset.toString());
        } else if (i2 > str.length()) {
            charset = new StringBuilder();
            charset.append("endIndex > string.length: ");
            charset.append(i2);
            charset.append(" > ");
            charset.append(str.length());
            throw new IllegalArgumentException(charset.toString());
        } else if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        } else if (charset.equals(Util.UTF_8)) {
            return writeUtf8(str, i, i2);
        } else {
            byte[] bytes = str.substring(i, i2).getBytes(charset);
            return write(bytes, 0, bytes.length);
        }
    }

    public Buffer write(byte[] bArr) {
        if (bArr != null) {
            return write(bArr, 0, bArr.length);
        }
        throw new IllegalArgumentException("source == null");
    }

    public Buffer write(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("source == null");
        }
        long j = (long) i2;
        Util.checkOffsetAndCount((long) bArr.length, (long) i, j);
        i2 += i;
        while (i < i2) {
            Segment writableSegment = writableSegment(1);
            int min = Math.min(i2 - i, 8192 - writableSegment.limit);
            System.arraycopy(bArr, i, writableSegment.data, writableSegment.limit, min);
            i += min;
            writableSegment.limit += min;
        }
        this.size += j;
        return this;
    }

    public long writeAll(Source source) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long j = 0;
        while (true) {
            long read = source.read(this, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
            if (read == -1) {
                return j;
            }
            j += read;
        }
    }

    public BufferedSink write(Source source, long j) throws IOException {
        while (j > 0) {
            long read = source.read(this, j);
            if (read == -1) {
                throw new EOFException();
            }
            j -= read;
        }
        return this;
    }

    public Buffer writeByte(int i) {
        Segment writableSegment = writableSegment(1);
        byte[] bArr = writableSegment.data;
        int i2 = writableSegment.limit;
        writableSegment.limit = i2 + 1;
        bArr[i2] = (byte) i;
        this.size++;
        return this;
    }

    public Buffer writeShort(int i) {
        Segment writableSegment = writableSegment(2);
        byte[] bArr = writableSegment.data;
        int i2 = writableSegment.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        i2 = i3 + 1;
        bArr[i3] = (byte) (i & 255);
        writableSegment.limit = i2;
        this.size += 2;
        return this;
    }

    public Buffer writeShortLe(int i) {
        return writeShort(Util.reverseBytesShort((short) i));
    }

    public Buffer writeInt(int i) {
        Segment writableSegment = writableSegment(4);
        byte[] bArr = writableSegment.data;
        int i2 = writableSegment.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        i2 = i3 + 1;
        bArr[i3] = (byte) ((i >>> 16) & 255);
        i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        i2 = i3 + 1;
        bArr[i3] = (byte) (i & 255);
        writableSegment.limit = i2;
        this.size += 4;
        return this;
    }

    public Buffer writeIntLe(int i) {
        return writeInt(Util.reverseBytesInt(i));
    }

    public Buffer writeLong(long j) {
        Segment writableSegment = writableSegment(8);
        byte[] bArr = writableSegment.data;
        int i = writableSegment.limit;
        int i2 = i + 1;
        bArr[i] = (byte) ((int) ((j >>> 56) & 255));
        i = i2 + 1;
        bArr[i2] = (byte) ((int) ((j >>> 48) & 255));
        i2 = i + 1;
        bArr[i] = (byte) ((int) ((j >>> 40) & 255));
        i = i2 + 1;
        bArr[i2] = (byte) ((int) ((j >>> 32) & 255));
        i2 = i + 1;
        bArr[i] = (byte) ((int) ((j >>> 24) & 255));
        i = i2 + 1;
        bArr[i2] = (byte) ((int) ((j >>> 16) & 255));
        i2 = i + 1;
        bArr[i] = (byte) ((int) ((j >>> 8) & 255));
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((int) (j & 255));
        writableSegment.limit = i3;
        this.size += 8;
        return this;
    }

    public Buffer writeLongLe(long j) {
        return writeLong(Util.reverseBytesLong(j));
    }

    public Buffer writeDecimalLong(long j) {
        if (j == 0) {
            return writeByte((int) 48);
        }
        Object obj = null;
        int i = 1;
        if (j < 0) {
            j = -j;
            if (j < 0) {
                return writeUtf8("-9223372036854775808");
            }
            obj = 1;
        }
        if (j >= 100000000) {
            i = j < 1000000000000L ? j < 10000000000L ? j < C0649C.NANOS_PER_SECOND ? 9 : 10 : j < 100000000000L ? 11 : 12 : j < 1000000000000000L ? j < 10000000000000L ? 13 : j < 100000000000000L ? 14 : 15 : j < 100000000000000000L ? j < 10000000000000000L ? 16 : 17 : j < 1000000000000000000L ? 18 : 19;
        } else if (j >= NotificationOptions.SKIP_STEP_TEN_SECONDS_IN_MS) {
            i = j < C0649C.MICROS_PER_SECOND ? j < 100000 ? 5 : 6 : j < 10000000 ? 7 : 8;
        } else if (j >= 100) {
            i = j < 1000 ? 3 : 4;
        } else if (j >= 10) {
            i = 2;
        }
        if (obj != null) {
            i++;
        }
        Segment writableSegment = writableSegment(i);
        byte[] bArr = writableSegment.data;
        int i2 = writableSegment.limit + i;
        while (j != 0) {
            i2--;
            bArr[i2] = DIGITS[(int) (j % 10)];
            j /= 10;
        }
        if (obj != null) {
            bArr[i2 - 1] = 45;
        }
        writableSegment.limit += i;
        this.size += (long) i;
        return this;
    }

    public Buffer writeHexadecimalUnsignedLong(long j) {
        if (j == 0) {
            return writeByte((int) 48);
        }
        int numberOfTrailingZeros = (Long.numberOfTrailingZeros(Long.highestOneBit(j)) / 4) + 1;
        Segment writableSegment = writableSegment(numberOfTrailingZeros);
        byte[] bArr = writableSegment.data;
        int i = writableSegment.limit;
        for (int i2 = (writableSegment.limit + numberOfTrailingZeros) - 1; i2 >= i; i2--) {
            bArr[i2] = DIGITS[(int) (j & 15)];
            j >>>= 4;
        }
        writableSegment.limit += numberOfTrailingZeros;
        this.size += (long) numberOfTrailingZeros;
        return this;
    }

    Segment writableSegment(int i) {
        if (i >= 1) {
            if (i <= 8192) {
                Segment segment;
                if (this.head == null) {
                    this.head = SegmentPool.take();
                    i = this.head;
                    Segment segment2 = this.head;
                    segment = this.head;
                    segment2.prev = segment;
                    i.next = segment;
                    return segment;
                }
                segment = this.head.prev;
                if (segment.limit + i > 8192 || segment.owner == 0) {
                    segment = segment.push(SegmentPool.take());
                }
                return segment;
            }
        }
        throw new IllegalArgumentException();
    }

    public void write(Buffer buffer, long j) {
        if (buffer == null) {
            throw new IllegalArgumentException("source == null");
        } else if (buffer == this) {
            throw new IllegalArgumentException("source == this");
        } else {
            Util.checkOffsetAndCount(buffer.size, 0, j);
            while (j > 0) {
                Segment segment;
                if (j < ((long) (buffer.head.limit - buffer.head.pos))) {
                    segment = this.head != null ? this.head.prev : null;
                    if (segment != null && segment.owner) {
                        int i;
                        long j2 = j + ((long) segment.limit);
                        if (segment.shared) {
                            i = 0;
                        } else {
                            i = segment.pos;
                        }
                        if (j2 - ((long) i) <= PlaybackStateCompat.ACTION_PLAY_FROM_URI) {
                            buffer.head.writeTo(segment, (int) j);
                            buffer.size -= j;
                            this.size += j;
                            return;
                        }
                    }
                    buffer.head = buffer.head.split((int) j);
                }
                segment = buffer.head;
                long j3 = (long) (segment.limit - segment.pos);
                buffer.head = segment.pop();
                if (this.head == null) {
                    this.head = segment;
                    segment = this.head;
                    Segment segment2 = this.head;
                    Segment segment3 = this.head;
                    segment2.prev = segment3;
                    segment.next = segment3;
                } else {
                    this.head.prev.push(segment).compact();
                }
                buffer.size -= j3;
                this.size += j3;
                j -= j3;
            }
        }
    }

    public long read(Buffer buffer, long j) {
        if (buffer == null) {
            throw new IllegalArgumentException("sink == null");
        } else if (j < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("byteCount < 0: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (this.size == 0) {
            return -1;
        } else {
            if (j > this.size) {
                j = this.size;
            }
            buffer.write(this, j);
            return j;
        }
    }

    public long indexOf(byte b) {
        return indexOf(b, 0, Long.MAX_VALUE);
    }

    public long indexOf(byte b, long j) {
        return indexOf(b, j, Long.MAX_VALUE);
    }

    public long indexOf(byte b, long j, long j2) {
        Buffer buffer = this;
        long j3 = 0;
        if (j >= 0) {
            if (j2 >= j) {
                long j4 = j2 > buffer.size ? buffer.size : j2;
                if (j == j4) {
                    return -1;
                }
                Segment segment = buffer.head;
                if (segment == null) {
                    return -1;
                }
                if (buffer.size - j >= j) {
                    while (true) {
                        long j5 = j3 + ((long) (segment.limit - segment.pos));
                        if (j5 >= j) {
                            break;
                        }
                        segment = segment.next;
                        j3 = j5;
                    }
                } else {
                    j3 = buffer.size;
                    while (j3 > j) {
                        segment = segment.prev;
                        j3 -= (long) (segment.limit - segment.pos);
                    }
                }
                long j6 = j;
                while (j3 < j4) {
                    byte[] bArr = segment.data;
                    int min = (int) Math.min((long) segment.limit, (((long) segment.pos) + j4) - j3);
                    for (int i = (int) ((((long) segment.pos) + j6) - j3); i < min; i++) {
                        if (bArr[i] == b) {
                            return ((long) (i - segment.pos)) + j3;
                        }
                    }
                    byte b2 = b;
                    long j7 = j3 + ((long) (segment.limit - segment.pos));
                    segment = segment.next;
                    j6 = j7;
                    j3 = j6;
                }
                return -1;
            }
        }
        throw new IllegalArgumentException(String.format("size=%s fromIndex=%s toIndex=%s", new Object[]{Long.valueOf(buffer.size), Long.valueOf(j), Long.valueOf(j2)}));
    }

    public long indexOf(ByteString byteString) throws IOException {
        return indexOf(byteString, 0);
    }

    public long indexOf(ByteString byteString, long j) throws IOException {
        Buffer buffer = this;
        if (byteString.size() == 0) {
            throw new IllegalArgumentException("bytes is empty");
        }
        long j2 = 0;
        if (j < 0) {
            throw new IllegalArgumentException("fromIndex < 0");
        }
        Segment segment = buffer.head;
        long j3 = -1;
        if (segment == null) {
            return -1;
        }
        if (buffer.size - j >= j) {
            while (true) {
                long j4 = j2 + ((long) (segment.limit - segment.pos));
                if (j4 >= j) {
                    break;
                }
                segment = segment.next;
                j2 = j4;
            }
        } else {
            j2 = buffer.size;
            while (j2 > j) {
                segment = segment.prev;
                j2 -= (long) (segment.limit - segment.pos);
            }
        }
        ByteString byteString2 = byteString;
        byte b = byteString2.getByte(0);
        int size = byteString.size();
        long j5 = (buffer.size - ((long) size)) + 1;
        long j6 = j;
        long j7 = j2;
        Segment segment2 = segment;
        while (j7 < j5) {
            Segment segment3;
            byte[] bArr = segment2.data;
            int min = (int) Math.min((long) segment2.limit, (((long) segment2.pos) + j5) - j7);
            int i = (int) ((((long) segment2.pos) + j6) - j7);
            while (i < min) {
                byte[] bArr2;
                if (bArr[i] == b) {
                    Buffer buffer2 = buffer;
                    bArr2 = bArr;
                    segment3 = segment2;
                    if (buffer2.rangeEquals(segment2, i + 1, byteString2, 1, size)) {
                        return ((long) (i - segment3.pos)) + j7;
                    }
                } else {
                    bArr2 = bArr;
                    segment3 = segment2;
                }
                i++;
                segment2 = segment3;
                bArr = bArr2;
                buffer = this;
            }
            segment3 = segment2;
            j2 = j7 + ((long) (segment3.limit - segment3.pos));
            segment2 = segment3.next;
            j6 = j2;
            j7 = j6;
            buffer = this;
            j3 = -1;
        }
        return j3;
    }

    public long indexOfElement(ByteString byteString) {
        return indexOfElement(byteString, 0);
    }

    public long indexOfElement(ByteString byteString, long j) {
        long j2 = 0;
        if (j < 0) {
            throw new IllegalArgumentException("fromIndex < 0");
        }
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        if (this.size - j >= j) {
            while (true) {
                long j3 = j2 + ((long) (segment.limit - segment.pos));
                if (j3 >= j) {
                    break;
                }
                segment = segment.next;
                j2 = j3;
            }
        } else {
            j2 = this.size;
            while (j2 > j) {
                segment = segment.prev;
                j2 -= (long) (segment.limit - segment.pos);
            }
        }
        int i;
        if (byteString.size() == 2) {
            byte b = byteString.getByte(0);
            byteString = byteString.getByte(1);
            while (j2 < this.size) {
                byte[] bArr = segment.data;
                j = (int) ((((long) segment.pos) + j) - j2);
                i = segment.limit;
                while (j < i) {
                    byte b2 = bArr[j];
                    if (b2 != b) {
                        if (b2 != byteString) {
                            j++;
                        }
                    }
                    return ((long) (j - segment.pos)) + j2;
                }
                long j4 = j2 + ((long) (segment.limit - segment.pos));
                segment = segment.next;
                j = j4;
                j2 = j;
            }
        } else {
            byteString = byteString.internalArray();
            while (j2 < this.size) {
                byte[] bArr2 = segment.data;
                i = segment.limit;
                for (j = (int) ((((long) segment.pos) + j) - j2); j < i; j++) {
                    byte b3 = bArr2[j];
                    for (byte b4 : byteString) {
                        if (b3 == b4) {
                            return ((long) (j - segment.pos)) + j2;
                        }
                    }
                }
                long j5 = j2 + ((long) (segment.limit - segment.pos));
                segment = segment.next;
                j = j5;
                j2 = j;
            }
        }
        return -1;
    }

    public boolean rangeEquals(long j, ByteString byteString) {
        return rangeEquals(j, byteString, 0, byteString.size());
    }

    public boolean rangeEquals(long j, ByteString byteString, int i, int i2) {
        if (j >= 0 && i >= 0 && i2 >= 0 && this.size - j >= ((long) i2)) {
            if (byteString.size() - i >= i2) {
                for (int i3 = 0; i3 < i2; i3++) {
                    if (getByte(j + ((long) i3)) != byteString.getByte(i + i3)) {
                        return false;
                    }
                }
                return 1;
            }
        }
        return false;
    }

    private boolean rangeEquals(Segment segment, int i, ByteString byteString, int i2, int i3) {
        int i4 = segment.limit;
        byte[] bArr = segment.data;
        while (i2 < i3) {
            if (i == i4) {
                segment = segment.next;
                i = segment.data;
                i4 = segment.pos;
                bArr = i;
                i = i4;
                i4 = segment.limit;
            }
            if (bArr[i] != byteString.getByte(i2)) {
                return null;
            }
            i++;
            i2++;
        }
        return true;
    }

    public Timeout timeout() {
        return Timeout.NONE;
    }

    List<Integer> segmentSizes() {
        if (this.head == null) {
            return Collections.emptyList();
        }
        List<Integer> arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(this.head.limit - this.head.pos));
        Segment segment = this.head;
        while (true) {
            segment = segment.next;
            if (segment == this.head) {
                return arrayList;
            }
            arrayList.add(Integer.valueOf(segment.limit - segment.pos));
        }
    }

    public ByteString md5() {
        return digest("MD5");
    }

    public ByteString sha1() {
        return digest("SHA-1");
    }

    public ByteString sha256() {
        return digest("SHA-256");
    }

    public ByteString sha512() {
        return digest(AndroidUtilsLight.DIGEST_ALGORITHM_SHA512);
    }

    private okio.ByteString digest(java.lang.String r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r6 = java.security.MessageDigest.getInstance(r6);	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r0 = r5.head;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        if (r0 == 0) goto L_0x0031;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
    L_0x0008:
        r0 = r5.head;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r0 = r0.data;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r1 = r5.head;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r1 = r1.pos;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r2 = r5.head;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r2 = r2.limit;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r3 = r5.head;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r3 = r3.pos;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r2 = r2 - r3;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r6.update(r0, r1, r2);	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r0 = r5.head;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
    L_0x001e:
        r0 = r0.next;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r1 = r5.head;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        if (r0 == r1) goto L_0x0031;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
    L_0x0024:
        r1 = r0.data;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r2 = r0.pos;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r3 = r0.limit;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r4 = r0.pos;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r3 = r3 - r4;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r6.update(r1, r2, r3);	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        goto L_0x001e;	 Catch:{ NoSuchAlgorithmException -> 0x003a }
    L_0x0031:
        r6 = r6.digest();	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        r6 = okio.ByteString.of(r6);	 Catch:{ NoSuchAlgorithmException -> 0x003a }
        return r6;
    L_0x003a:
        r6 = new java.lang.AssertionError;
        r6.<init>();
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.digest(java.lang.String):okio.ByteString");
    }

    public ByteString hmacSha1(ByteString byteString) {
        return hmac("HmacSHA1", byteString);
    }

    public ByteString hmacSha256(ByteString byteString) {
        return hmac("HmacSHA256", byteString);
    }

    public ByteString hmacSha512(ByteString byteString) {
        return hmac("HmacSHA512", byteString);
    }

    private okio.ByteString hmac(java.lang.String r5, okio.ByteString r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = javax.crypto.Mac.getInstance(r5);	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r1 = new javax.crypto.spec.SecretKeySpec;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r6 = r6.toByteArray();	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r1.<init>(r6, r5);	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r0.init(r1);	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r5 = r4.head;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        if (r5 == 0) goto L_0x003d;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
    L_0x0014:
        r5 = r4.head;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r5 = r5.data;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r6 = r4.head;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r6 = r6.pos;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r1 = r4.head;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r1 = r1.limit;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r2 = r4.head;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r2 = r2.pos;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r1 = r1 - r2;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r0.update(r5, r6, r1);	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r5 = r4.head;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
    L_0x002a:
        r5 = r5.next;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r6 = r4.head;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        if (r5 == r6) goto L_0x003d;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
    L_0x0030:
        r6 = r5.data;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r1 = r5.pos;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r2 = r5.limit;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r3 = r5.pos;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r2 = r2 - r3;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r0.update(r6, r1, r2);	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        goto L_0x002a;	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
    L_0x003d:
        r5 = r0.doFinal();	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        r5 = okio.ByteString.of(r5);	 Catch:{ NoSuchAlgorithmException -> 0x004d, InvalidKeyException -> 0x0046 }
        return r5;
    L_0x0046:
        r5 = move-exception;
        r6 = new java.lang.IllegalArgumentException;
        r6.<init>(r5);
        throw r6;
    L_0x004d:
        r5 = new java.lang.AssertionError;
        r5.<init>();
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.hmac(java.lang.String, okio.ByteString):okio.ByteString");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Buffer)) {
            return false;
        }
        Buffer buffer = (Buffer) obj;
        if (this.size != buffer.size) {
            return false;
        }
        long j = 0;
        if (this.size == 0) {
            return true;
        }
        Segment segment = this.head;
        obj = buffer.head;
        int i = segment.pos;
        int i2 = obj.pos;
        while (j < this.size) {
            long min = (long) Math.min(segment.limit - i, obj.limit - i2);
            int i3 = i2;
            i2 = i;
            i = 0;
            while (((long) i) < min) {
                int i4 = i2 + 1;
                int i5 = i3 + 1;
                if (segment.data[i2] != obj.data[i3]) {
                    return false;
                }
                i++;
                i2 = i4;
                i3 = i5;
            }
            if (i2 == segment.limit) {
                segment = segment.next;
                i = segment.pos;
            } else {
                i = i2;
            }
            if (i3 == obj.limit) {
                obj = obj.next;
                i2 = obj.pos;
            } else {
                i2 = i3;
            }
            j += min;
        }
        return true;
    }

    public int hashCode() {
        Segment segment = this.head;
        if (segment == null) {
            return 0;
        }
        int i = 1;
        do {
            for (int i2 = segment.pos; i2 < segment.limit; i2++) {
                i = (i * 31) + segment.data[i2];
            }
            segment = segment.next;
        } while (segment != this.head);
        return i;
    }

    public String toString() {
        return snapshot().toString();
    }

    public Buffer clone() {
        Buffer buffer = new Buffer();
        if (this.size == 0) {
            return buffer;
        }
        buffer.head = new Segment(this.head);
        Segment segment = buffer.head;
        Segment segment2 = buffer.head;
        Segment segment3 = buffer.head;
        segment2.prev = segment3;
        segment.next = segment3;
        segment = this.head;
        while (true) {
            segment = segment.next;
            if (segment != this.head) {
                buffer.head.prev.push(new Segment(segment));
            } else {
                buffer.size = this.size;
                return buffer;
            }
        }
    }

    public ByteString snapshot() {
        if (this.size <= 2147483647L) {
            return snapshot((int) this.size);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("size > Integer.MAX_VALUE: ");
        stringBuilder.append(this.size);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public ByteString snapshot(int i) {
        if (i == 0) {
            return ByteString.EMPTY;
        }
        return new SegmentedByteString(this, i);
    }
}
