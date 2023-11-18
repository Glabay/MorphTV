package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class CharSequenceInputStream extends InputStream {
    private static final int BUFFER_SIZE = 2048;
    private static final int NO_MARK = -1;
    private final ByteBuffer bbuf;
    private final CharBuffer cbuf;
    private final CharsetEncoder encoder;
    private int mark_bbuf;
    private int mark_cbuf;

    public void close() throws IOException {
    }

    public boolean markSupported() {
        return true;
    }

    public CharSequenceInputStream(CharSequence charSequence, Charset charset, int i) {
        this.encoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        charset = this.encoder.maxBytesPerChar();
        if (((float) i) < charset) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Buffer size ");
            stringBuilder.append(i);
            stringBuilder.append(" is less than maxBytesPerChar ");
            stringBuilder.append(charset);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.bbuf = ByteBuffer.allocate(i);
        this.bbuf.flip();
        this.cbuf = CharBuffer.wrap(charSequence);
        this.mark_cbuf = -1;
        this.mark_bbuf = -1;
    }

    public CharSequenceInputStream(CharSequence charSequence, String str, int i) {
        this(charSequence, Charset.forName(str), i);
    }

    public CharSequenceInputStream(CharSequence charSequence, Charset charset) {
        this(charSequence, charset, 2048);
    }

    public CharSequenceInputStream(CharSequence charSequence, String str) {
        this(charSequence, str, 2048);
    }

    private void fillBuffer() throws CharacterCodingException {
        this.bbuf.compact();
        CoderResult encode = this.encoder.encode(this.cbuf, this.bbuf, true);
        if (encode.isError()) {
            encode.throwException();
        }
        this.bbuf.flip();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (bArr == null) {
            throw new NullPointerException("Byte array is null");
        }
        if (i2 >= 0) {
            if (i + i2 <= bArr.length) {
                int i3 = 0;
                if (i2 == 0) {
                    return 0;
                }
                if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                    return -1;
                }
                while (i2 > 0) {
                    if (!this.bbuf.hasRemaining()) {
                        fillBuffer();
                        if (!(this.bbuf.hasRemaining() || this.cbuf.hasRemaining())) {
                            break;
                        }
                    }
                    int min = Math.min(this.bbuf.remaining(), i2);
                    this.bbuf.get(bArr, i, min);
                    i += min;
                    i2 -= min;
                    i3 += min;
                }
                if (i3 == 0 && this.cbuf.hasRemaining() == null) {
                    i3 = -1;
                }
                return i3;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Array Size=");
        stringBuilder.append(bArr.length);
        stringBuilder.append(", offset=");
        stringBuilder.append(i);
        stringBuilder.append(", length=");
        stringBuilder.append(i2);
        throw new IndexOutOfBoundsException(stringBuilder.toString());
    }

    public int read() throws IOException {
        while (!this.bbuf.hasRemaining()) {
            fillBuffer();
            if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                return -1;
            }
        }
        return this.bbuf.get() & 255;
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public long skip(long j) throws IOException {
        long j2 = 0;
        while (j > 0 && available() > 0) {
            read();
            j2++;
            j--;
        }
        return j2;
    }

    public int available() throws IOException {
        return this.bbuf.remaining() + this.cbuf.remaining();
    }

    public synchronized void mark(int i) {
        this.mark_cbuf = this.cbuf.position();
        this.mark_bbuf = this.bbuf.position();
        this.cbuf.mark();
        this.bbuf.mark();
    }

    public synchronized void reset() throws IOException {
        if (this.mark_cbuf != -1) {
            if (this.cbuf.position() != 0) {
                this.encoder.reset();
                this.cbuf.rewind();
                this.bbuf.rewind();
                this.bbuf.limit(0);
                while (this.cbuf.position() < this.mark_cbuf) {
                    this.bbuf.rewind();
                    this.bbuf.limit(0);
                    fillBuffer();
                }
            }
            if (this.cbuf.position() != this.mark_cbuf) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected CharBuffer postion: actual=");
                stringBuilder.append(this.cbuf.position());
                stringBuilder.append(" expected=");
                stringBuilder.append(this.mark_cbuf);
                throw new IllegalStateException(stringBuilder.toString());
            }
            this.bbuf.position(this.mark_bbuf);
            this.mark_cbuf = -1;
            this.mark_bbuf = -1;
        }
    }
}
