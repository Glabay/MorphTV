package org.apache.commons.io.output;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.input.ClosedInputStream;

public class ByteArrayOutputStream extends OutputStream {
    static final int DEFAULT_SIZE = 1024;
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private final List<byte[]> buffers;
    private int count;
    private byte[] currentBuffer;
    private int currentBufferIndex;
    private int filledBufferSum;
    private boolean reuseBuffers;

    public void close() throws IOException {
    }

    public ByteArrayOutputStream() {
        this(1024);
    }

    public ByteArrayOutputStream(int i) {
        this.buffers = new ArrayList();
        this.reuseBuffers = true;
        if (i < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Negative initial size: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        synchronized (this) {
            needNewBuffer(i);
        }
    }

    private void needNewBuffer(int i) {
        if (this.currentBufferIndex < this.buffers.size() - 1) {
            this.filledBufferSum += this.currentBuffer.length;
            this.currentBufferIndex++;
            this.currentBuffer = (byte[]) this.buffers.get(this.currentBufferIndex);
            return;
        }
        if (this.currentBuffer == null) {
            this.filledBufferSum = 0;
        } else {
            i = Math.max(this.currentBuffer.length << 1, i - this.filledBufferSum);
            this.filledBufferSum += this.currentBuffer.length;
        }
        this.currentBufferIndex++;
        this.currentBuffer = new byte[i];
        this.buffers.add(this.currentBuffer);
    }

    public void write(byte[] bArr, int i, int i2) {
        if (i >= 0 && i <= bArr.length && i2 >= 0) {
            i += i2;
            if (i <= bArr.length) {
                if (i >= 0) {
                    if (i2 != 0) {
                        synchronized (this) {
                            int i3 = this.count + i2;
                            int i4 = this.count - this.filledBufferSum;
                            while (i2 > 0) {
                                int min = Math.min(i2, this.currentBuffer.length - i4);
                                System.arraycopy(bArr, i - i2, this.currentBuffer, i4, min);
                                i2 -= min;
                                if (i2 > 0) {
                                    needNewBuffer(i3);
                                    i4 = 0;
                                }
                            }
                            this.count = i3;
                        }
                        return;
                    }
                    return;
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public synchronized void write(int i) {
        int i2 = this.count - this.filledBufferSum;
        if (i2 == this.currentBuffer.length) {
            needNewBuffer(this.count + 1);
            i2 = 0;
        }
        this.currentBuffer[i2] = (byte) i;
        this.count++;
    }

    public synchronized int write(InputStream inputStream) throws IOException {
        int i;
        i = this.count - this.filledBufferSum;
        int read = inputStream.read(this.currentBuffer, i, this.currentBuffer.length - i);
        int i2 = i;
        i = 0;
        while (read != -1) {
            i += read;
            i2 += read;
            this.count += read;
            if (i2 == this.currentBuffer.length) {
                needNewBuffer(this.currentBuffer.length);
                i2 = 0;
            }
            read = inputStream.read(this.currentBuffer, i2, this.currentBuffer.length - i2);
        }
        return i;
    }

    public synchronized int size() {
        return this.count;
    }

    public synchronized void reset() {
        this.count = 0;
        this.filledBufferSum = 0;
        this.currentBufferIndex = 0;
        if (this.reuseBuffers) {
            this.currentBuffer = (byte[]) this.buffers.get(this.currentBufferIndex);
        } else {
            this.currentBuffer = null;
            int length = ((byte[]) this.buffers.get(0)).length;
            this.buffers.clear();
            needNewBuffer(length);
            this.reuseBuffers = true;
        }
    }

    public synchronized void writeTo(OutputStream outputStream) throws IOException {
        int i = this.count;
        for (byte[] bArr : this.buffers) {
            int min = Math.min(bArr.length, i);
            outputStream.write(bArr, 0, min);
            i -= min;
            if (i == 0) {
                break;
            }
        }
    }

    public static InputStream toBufferedInputStream(InputStream inputStream) throws IOException {
        return toBufferedInputStream(inputStream, 1024);
    }

    public static InputStream toBufferedInputStream(InputStream inputStream, int i) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i);
        byteArrayOutputStream.write(inputStream);
        return byteArrayOutputStream.toInputStream();
    }

    public synchronized InputStream toInputStream() {
        int i = this.count;
        if (i == 0) {
            return new ClosedInputStream();
        }
        Collection arrayList = new ArrayList(this.buffers.size());
        for (byte[] bArr : this.buffers) {
            int min = Math.min(bArr.length, i);
            arrayList.add(new ByteArrayInputStream(bArr, 0, min));
            i -= min;
            if (i == 0) {
                break;
            }
        }
        this.reuseBuffers = false;
        return new SequenceInputStream(Collections.enumeration(arrayList));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized byte[] toByteArray() {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = r7.count;	 Catch:{ all -> 0x002d }
        if (r0 != 0) goto L_0x0009;
    L_0x0005:
        r0 = EMPTY_BYTE_ARRAY;	 Catch:{ all -> 0x002d }
        monitor-exit(r7);
        return r0;
    L_0x0009:
        r1 = new byte[r0];	 Catch:{ all -> 0x002d }
        r2 = r7.buffers;	 Catch:{ all -> 0x002d }
        r2 = r2.iterator();	 Catch:{ all -> 0x002d }
        r3 = 0;
        r4 = 0;
    L_0x0013:
        r5 = r2.hasNext();	 Catch:{ all -> 0x002d }
        if (r5 == 0) goto L_0x002b;
    L_0x0019:
        r5 = r2.next();	 Catch:{ all -> 0x002d }
        r5 = (byte[]) r5;	 Catch:{ all -> 0x002d }
        r6 = r5.length;	 Catch:{ all -> 0x002d }
        r6 = java.lang.Math.min(r6, r0);	 Catch:{ all -> 0x002d }
        java.lang.System.arraycopy(r5, r3, r1, r4, r6);	 Catch:{ all -> 0x002d }
        r4 = r4 + r6;
        r0 = r0 - r6;
        if (r0 != 0) goto L_0x0013;
    L_0x002b:
        monitor-exit(r7);
        return r1;
    L_0x002d:
        r0 = move-exception;
        monitor-exit(r7);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.output.ByteArrayOutputStream.toByteArray():byte[]");
    }

    @Deprecated
    public String toString() {
        return new String(toByteArray(), Charset.defaultCharset());
    }

    public String toString(String str) throws UnsupportedEncodingException {
        return new String(toByteArray(), str);
    }

    public String toString(Charset charset) {
        return new String(toByteArray(), charset);
    }
}
