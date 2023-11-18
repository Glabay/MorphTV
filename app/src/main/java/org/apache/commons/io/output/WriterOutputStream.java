package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class WriterOutputStream extends OutputStream {
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final CharsetDecoder decoder;
    private final ByteBuffer decoderIn;
    private final CharBuffer decoderOut;
    private final boolean writeImmediately;
    private final Writer writer;

    public WriterOutputStream(Writer writer, CharsetDecoder charsetDecoder) {
        this(writer, charsetDecoder, 1024, false);
    }

    public WriterOutputStream(Writer writer, CharsetDecoder charsetDecoder, int i, boolean z) {
        this.decoderIn = ByteBuffer.allocate(128);
        checkIbmJdkWithBrokenUTF16(charsetDecoder.charset());
        this.writer = writer;
        this.decoder = charsetDecoder;
        this.writeImmediately = z;
        this.decoderOut = CharBuffer.allocate(i);
    }

    public WriterOutputStream(Writer writer, Charset charset, int i, boolean z) {
        this(writer, charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), i, z);
    }

    public WriterOutputStream(Writer writer, Charset charset) {
        this(writer, charset, 1024, false);
    }

    public WriterOutputStream(Writer writer, String str, int i, boolean z) {
        this(writer, Charset.forName(str), i, z);
    }

    public WriterOutputStream(Writer writer, String str) {
        this(writer, str, 1024, false);
    }

    @Deprecated
    public WriterOutputStream(Writer writer) {
        this(writer, Charset.defaultCharset(), 1024, false);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        while (i2 > 0) {
            int min = Math.min(i2, this.decoderIn.remaining());
            this.decoderIn.put(bArr, i, min);
            processInput(false);
            i2 -= min;
            i += min;
        }
        if (this.writeImmediately != null) {
            flushOutput();
        }
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(int i) throws IOException {
        write(new byte[]{(byte) i}, 0, 1);
    }

    public void flush() throws IOException {
        flushOutput();
        this.writer.flush();
    }

    public void close() throws IOException {
        processInput(true);
        flushOutput();
        this.writer.close();
    }

    private void processInput(boolean z) throws IOException {
        this.decoderIn.flip();
        while (true) {
            CoderResult decode = this.decoder.decode(this.decoderIn, this.decoderOut, z);
            if (!decode.isOverflow()) {
                break;
            }
            flushOutput();
        }
        if (decode.isUnderflow()) {
            this.decoderIn.compact();
            return;
        }
        throw new IOException("Unexpected coder result");
    }

    private void flushOutput() throws IOException {
        if (this.decoderOut.position() > 0) {
            this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
            this.decoderOut.rewind();
        }
    }

    private static void checkIbmJdkWithBrokenUTF16(java.nio.charset.Charset r7) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = "UTF-16";
        r1 = r7.name();
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x000d;
    L_0x000c:
        return;
    L_0x000d:
        r0 = "vés";
        r0 = r0.getBytes(r7);
        r7 = r7.newDecoder();
        r1 = 16;
        r1 = java.nio.ByteBuffer.allocate(r1);
        r2 = "vés";
        r2 = r2.length();
        r2 = java.nio.CharBuffer.allocate(r2);
        r3 = r0.length;
        r4 = 0;
        r5 = 0;
    L_0x002a:
        if (r5 >= r3) goto L_0x004c;
    L_0x002c:
        r6 = r0[r5];
        r1.put(r6);
        r1.flip();
        r6 = r3 + -1;
        if (r5 != r6) goto L_0x003a;
    L_0x0038:
        r6 = 1;
        goto L_0x003b;
    L_0x003a:
        r6 = 0;
    L_0x003b:
        r7.decode(r1, r2, r6);	 Catch:{ IllegalArgumentException -> 0x0044 }
        r1.compact();
        r5 = r5 + 1;
        goto L_0x002a;
    L_0x0044:
        r7 = new java.lang.UnsupportedOperationException;
        r0 = "UTF-16 requested when runninng on an IBM JDK with broken UTF-16 support. Please find a JDK that supports UTF-16 if you intend to use UF-16 with WriterOutputStream";
        r7.<init>(r0);
        throw r7;
    L_0x004c:
        r2.rewind();
        r7 = "vés";
        r0 = r2.toString();
        r7 = r7.equals(r0);
        if (r7 != 0) goto L_0x0063;
    L_0x005b:
        r7 = new java.lang.UnsupportedOperationException;
        r0 = "UTF-16 requested when runninng on an IBM JDK with broken UTF-16 support. Please find a JDK that supports UTF-16 if you intend to use UF-16 with WriterOutputStream";
        r7.<init>(r0);
        throw r7;
    L_0x0063:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.output.WriterOutputStream.checkIbmJdkWithBrokenUTF16(java.nio.charset.Charset):void");
    }
}
