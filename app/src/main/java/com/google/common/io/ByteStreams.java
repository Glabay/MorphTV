package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

@Beta
public final class ByteStreams {
    private static final int BUF_SIZE = 4096;
    private static final OutputStream NULL_OUTPUT_STREAM = new C11441();

    /* renamed from: com.google.common.io.ByteStreams$1 */
    static class C11441 extends OutputStream {
        public String toString() {
            return "ByteStreams.nullOutputStream()";
        }

        public void write(int i) {
        }

        C11441() {
        }

        public void write(byte[] bArr) {
            Preconditions.checkNotNull(bArr);
        }

        public void write(byte[] bArr, int i, int i2) {
            Preconditions.checkNotNull(bArr);
        }
    }

    private static class ByteArrayDataInputStream implements ByteArrayDataInput {
        final DataInput input;

        ByteArrayDataInputStream(ByteArrayInputStream byteArrayInputStream) {
            this.input = new DataInputStream(byteArrayInputStream);
        }

        public void readFully(byte[] bArr) {
            try {
                this.input.readFully(bArr);
            } catch (byte[] bArr2) {
                throw new IllegalStateException(bArr2);
            }
        }

        public void readFully(byte[] bArr, int i, int i2) {
            try {
                this.input.readFully(bArr, i, i2);
            } catch (byte[] bArr2) {
                throw new IllegalStateException(bArr2);
            }
        }

        public int skipBytes(int i) {
            try {
                return this.input.skipBytes(i);
            } catch (int i2) {
                throw new IllegalStateException(i2);
            }
        }

        public boolean readBoolean() {
            try {
                return this.input.readBoolean();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public byte readByte() {
            try {
                return this.input.readByte();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            } catch (IOException e2) {
                throw new AssertionError(e2);
            }
        }

        public int readUnsignedByte() {
            try {
                return this.input.readUnsignedByte();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public short readShort() {
            try {
                return this.input.readShort();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public int readUnsignedShort() {
            try {
                return this.input.readUnsignedShort();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public char readChar() {
            try {
                return this.input.readChar();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public int readInt() {
            try {
                return this.input.readInt();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public long readLong() {
            try {
                return this.input.readLong();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public float readFloat() {
            try {
                return this.input.readFloat();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public double readDouble() {
            try {
                return this.input.readDouble();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public String readLine() {
            try {
                return this.input.readLine();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public String readUTF() {
            try {
                return this.input.readUTF();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static class ByteArrayDataOutputStream implements ByteArrayDataOutput {
        final ByteArrayOutputStream byteArrayOutputSteam;
        final DataOutput output;

        ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
            this.byteArrayOutputSteam = byteArrayOutputStream;
            this.output = new DataOutputStream(byteArrayOutputStream);
        }

        public void write(int i) {
            try {
                this.output.write(i);
            } catch (int i2) {
                throw new AssertionError(i2);
            }
        }

        public void write(byte[] bArr) {
            try {
                this.output.write(bArr);
            } catch (byte[] bArr2) {
                throw new AssertionError(bArr2);
            }
        }

        public void write(byte[] bArr, int i, int i2) {
            try {
                this.output.write(bArr, i, i2);
            } catch (byte[] bArr2) {
                throw new AssertionError(bArr2);
            }
        }

        public void writeBoolean(boolean z) {
            try {
                this.output.writeBoolean(z);
            } catch (boolean z2) {
                throw new AssertionError(z2);
            }
        }

        public void writeByte(int i) {
            try {
                this.output.writeByte(i);
            } catch (int i2) {
                throw new AssertionError(i2);
            }
        }

        public void writeBytes(String str) {
            try {
                this.output.writeBytes(str);
            } catch (String str2) {
                throw new AssertionError(str2);
            }
        }

        public void writeChar(int i) {
            try {
                this.output.writeChar(i);
            } catch (int i2) {
                throw new AssertionError(i2);
            }
        }

        public void writeChars(String str) {
            try {
                this.output.writeChars(str);
            } catch (String str2) {
                throw new AssertionError(str2);
            }
        }

        public void writeDouble(double d) {
            try {
                this.output.writeDouble(d);
            } catch (double d2) {
                throw new AssertionError(d2);
            }
        }

        public void writeFloat(float f) {
            try {
                this.output.writeFloat(f);
            } catch (float f2) {
                throw new AssertionError(f2);
            }
        }

        public void writeInt(int i) {
            try {
                this.output.writeInt(i);
            } catch (int i2) {
                throw new AssertionError(i2);
            }
        }

        public void writeLong(long j) {
            try {
                this.output.writeLong(j);
            } catch (long j2) {
                throw new AssertionError(j2);
            }
        }

        public void writeShort(int i) {
            try {
                this.output.writeShort(i);
            } catch (int i2) {
                throw new AssertionError(i2);
            }
        }

        public void writeUTF(String str) {
            try {
                this.output.writeUTF(str);
            } catch (String str2) {
                throw new AssertionError(str2);
            }
        }

        public byte[] toByteArray() {
            return this.byteArrayOutputSteam.toByteArray();
        }
    }

    private static final class FastByteArrayOutputStream extends ByteArrayOutputStream {
        private FastByteArrayOutputStream() {
        }

        void writeTo(byte[] bArr, int i) {
            System.arraycopy(this.buf, 0, bArr, i, this.count);
        }
    }

    private static final class LimitedInputStream extends FilterInputStream {
        private long left;
        private long mark = -1;

        LimitedInputStream(InputStream inputStream, long j) {
            super(inputStream);
            Preconditions.checkNotNull(inputStream);
            Preconditions.checkArgument(j >= 0 ? true : null, "limit must be non-negative");
            this.left = j;
        }

        public int available() throws IOException {
            return (int) Math.min((long) this.in.available(), this.left);
        }

        public synchronized void mark(int i) {
            this.in.mark(i);
            this.mark = this.left;
        }

        public int read() throws IOException {
            if (this.left == 0) {
                return -1;
            }
            int read = this.in.read();
            if (read != -1) {
                this.left--;
            }
            return read;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (this.left == 0) {
                return -1;
            }
            bArr = this.in.read(bArr, i, (int) Math.min((long) i2, this.left));
            if (bArr != -1) {
                this.left -= (long) bArr;
            }
            return bArr;
        }

        public synchronized void reset() throws IOException {
            if (!this.in.markSupported()) {
                throw new IOException("Mark not supported");
            } else if (this.mark == -1) {
                throw new IOException("Mark not set");
            } else {
                this.in.reset();
                this.left = this.mark;
            }
        }

        public long skip(long j) throws IOException {
            j = this.in.skip(Math.min(j, this.left));
            this.left -= j;
            return j;
        }
    }

    private ByteStreams() {
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(outputStream);
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    public static long copy(ReadableByteChannel readableByteChannel, WritableByteChannel writableByteChannel) throws IOException {
        Preconditions.checkNotNull(readableByteChannel);
        Preconditions.checkNotNull(writableByteChannel);
        ByteBuffer allocate = ByteBuffer.allocate(4096);
        long j = 0;
        while (readableByteChannel.read(allocate) != -1) {
            allocate.flip();
            while (allocate.hasRemaining()) {
                j += (long) writableByteChannel.write(allocate);
            }
            allocate.clear();
        }
        return j;
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    static byte[] toByteArray(InputStream inputStream, int i) throws IOException {
        Object obj = new byte[i];
        int i2 = i;
        while (i2 > 0) {
            int i3 = i - i2;
            int read = inputStream.read(obj, i3, i2);
            if (read == -1) {
                return Arrays.copyOf(obj, i3);
            }
            i2 -= read;
        }
        i = inputStream.read();
        if (i == -1) {
            return obj;
        }
        OutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream();
        fastByteArrayOutputStream.write(i);
        copy(inputStream, fastByteArrayOutputStream);
        inputStream = new byte[(obj.length + fastByteArrayOutputStream.size())];
        System.arraycopy(obj, 0, inputStream, 0, obj.length);
        fastByteArrayOutputStream.writeTo(inputStream, obj.length);
        return inputStream;
    }

    public static ByteArrayDataInput newDataInput(byte[] bArr) {
        return newDataInput(new ByteArrayInputStream(bArr));
    }

    public static ByteArrayDataInput newDataInput(byte[] bArr, int i) {
        Preconditions.checkPositionIndex(i, bArr.length);
        return newDataInput(new ByteArrayInputStream(bArr, i, bArr.length - i));
    }

    public static ByteArrayDataInput newDataInput(ByteArrayInputStream byteArrayInputStream) {
        return new ByteArrayDataInputStream((ByteArrayInputStream) Preconditions.checkNotNull(byteArrayInputStream));
    }

    public static ByteArrayDataOutput newDataOutput() {
        return newDataOutput(new ByteArrayOutputStream());
    }

    public static ByteArrayDataOutput newDataOutput(int i) {
        Preconditions.checkArgument(i >= 0, "Invalid size: %s", Integer.valueOf(i));
        return newDataOutput(new ByteArrayOutputStream(i));
    }

    public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream byteArrayOutputStream) {
        return new ByteArrayDataOutputStream((ByteArrayOutputStream) Preconditions.checkNotNull(byteArrayOutputStream));
    }

    public static OutputStream nullOutputStream() {
        return NULL_OUTPUT_STREAM;
    }

    public static InputStream limit(InputStream inputStream, long j) {
        return new LimitedInputStream(inputStream, j);
    }

    public static void readFully(InputStream inputStream, byte[] bArr) throws IOException {
        readFully(inputStream, bArr, 0, bArr.length);
    }

    public static void readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        inputStream = read(inputStream, bArr, i, i2);
        if (inputStream != i2) {
            i = new StringBuilder();
            i.append("reached end of stream after reading ");
            i.append(inputStream);
            i.append(" bytes; ");
            i.append(i2);
            i.append(" bytes expected");
            throw new EOFException(i.toString());
        }
    }

    public static void skipFully(InputStream inputStream, long j) throws IOException {
        long j2 = j;
        while (j2 > 0) {
            long skip = inputStream.skip(j2);
            if (skip != 0) {
                j2 -= skip;
            } else if (inputStream.read() == -1) {
                long j3 = j - j2;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("reached end of stream after skipping ");
                stringBuilder.append(j3);
                stringBuilder.append(" bytes; ");
                stringBuilder.append(j);
                stringBuilder.append(" bytes expected");
                throw new EOFException(stringBuilder.toString());
            } else {
                j2--;
            }
        }
    }

    public static <T> T readBytes(InputStream inputStream, ByteProcessor<T> byteProcessor) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(byteProcessor);
        byte[] bArr = new byte[4096];
        int read;
        do {
            read = inputStream.read(bArr);
            if (read == -1) {
                break;
            }
        } while (byteProcessor.processBytes(bArr, 0, read));
        return byteProcessor.getResult();
    }

    public static int read(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(bArr);
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        int i3 = 0;
        while (i3 < i2) {
            int read = inputStream.read(bArr, i + i3, i2 - i3);
            if (read == -1) {
                break;
            }
            i3 += read;
        }
        return i3;
    }
}
