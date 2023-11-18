package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class ReaderInputStream extends InputStream {
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final CharsetEncoder encoder;
    private final CharBuffer encoderIn;
    private final ByteBuffer encoderOut;
    private boolean endOfInput;
    private CoderResult lastCoderResult;
    private final Reader reader;

    public ReaderInputStream(Reader reader, CharsetEncoder charsetEncoder) {
        this(reader, charsetEncoder, 1024);
    }

    public ReaderInputStream(Reader reader, CharsetEncoder charsetEncoder, int i) {
        this.reader = reader;
        this.encoder = charsetEncoder;
        this.encoderIn = CharBuffer.allocate(i);
        this.encoderIn.flip();
        this.encoderOut = ByteBuffer.allocate(128);
        this.encoderOut.flip();
    }

    public ReaderInputStream(Reader reader, Charset charset, int i) {
        this(reader, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), i);
    }

    public ReaderInputStream(Reader reader, Charset charset) {
        this(reader, charset, 1024);
    }

    public ReaderInputStream(Reader reader, String str, int i) {
        this(reader, Charset.forName(str), i);
    }

    public ReaderInputStream(Reader reader, String str) {
        this(reader, str, 1024);
    }

    @Deprecated
    public ReaderInputStream(Reader reader) {
        this(reader, Charset.defaultCharset());
    }

    private void fillBuffer() throws IOException {
        if (!this.endOfInput && (this.lastCoderResult == null || this.lastCoderResult.isUnderflow())) {
            this.encoderIn.compact();
            int position = this.encoderIn.position();
            int read = this.reader.read(this.encoderIn.array(), position, this.encoderIn.remaining());
            if (read == -1) {
                this.endOfInput = true;
            } else {
                this.encoderIn.position(position + read);
            }
            this.encoderIn.flip();
        }
        this.encoderOut.compact();
        this.lastCoderResult = this.encoder.encode(this.encoderIn, this.encoderOut, this.endOfInput);
        this.encoderOut.flip();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (bArr == null) {
            throw new NullPointerException("Byte array must not be null");
        }
        if (i2 >= 0 && i >= 0) {
            if (i + i2 <= bArr.length) {
                int i3 = 0;
                if (i2 == 0) {
                    return 0;
                }
                while (i2 > 0) {
                    if (!this.encoderOut.hasRemaining()) {
                        fillBuffer();
                        if (this.endOfInput && !this.encoderOut.hasRemaining()) {
                            break;
                        }
                    }
                    int min = Math.min(this.encoderOut.remaining(), i2);
                    this.encoderOut.get(bArr, i, min);
                    i += min;
                    i2 -= min;
                    i3 += min;
                }
                if (i3 == 0 && this.endOfInput != null) {
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

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read() throws IOException {
        while (!this.encoderOut.hasRemaining()) {
            fillBuffer();
            if (this.endOfInput && !this.encoderOut.hasRemaining()) {
                return -1;
            }
        }
        return this.encoderOut.get() & 255;
    }

    public void close() throws IOException {
        this.reader.close();
    }
}
