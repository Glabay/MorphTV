package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Beta
public final class FileBackedOutputStream extends OutputStream {
    private File file;
    private final int fileThreshold;
    private MemoryOutput memory;
    private OutputStream out;
    private final boolean resetOnFinalize;
    private final ByteSource source;

    /* renamed from: com.google.common.io.FileBackedOutputStream$1 */
    class C11481 extends ByteSource {
        C11481() {
        }

        public InputStream openStream() throws IOException {
            return FileBackedOutputStream.this.openInputStream();
        }

        protected void finalize() {
            try {
                FileBackedOutputStream.this.reset();
            } catch (Throwable th) {
                th.printStackTrace(System.err);
            }
        }
    }

    /* renamed from: com.google.common.io.FileBackedOutputStream$2 */
    class C11492 extends ByteSource {
        C11492() {
        }

        public InputStream openStream() throws IOException {
            return FileBackedOutputStream.this.openInputStream();
        }
    }

    private static class MemoryOutput extends ByteArrayOutputStream {
        private MemoryOutput() {
        }

        byte[] getBuffer() {
            return this.buf;
        }

        int getCount() {
            return this.count;
        }
    }

    @VisibleForTesting
    synchronized File getFile() {
        return this.file;
    }

    public FileBackedOutputStream(int i) {
        this(i, false);
    }

    public FileBackedOutputStream(int i, boolean z) {
        this.fileThreshold = i;
        this.resetOnFinalize = z;
        this.memory = new MemoryOutput();
        this.out = this.memory;
        if (z) {
            this.source = new C11481();
        } else {
            this.source = new C11492();
        }
    }

    public ByteSource asByteSource() {
        return this.source;
    }

    private synchronized InputStream openInputStream() throws IOException {
        if (this.file != null) {
            return new FileInputStream(this.file);
        }
        return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
    }

    public synchronized void reset() throws IOException {
        try {
            close();
            if (this.memory == null) {
                this.memory = new MemoryOutput();
            } else {
                this.memory.reset();
            }
            this.out = this.memory;
            if (this.file != null) {
                File file = this.file;
                this.file = null;
                if (!file.delete()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Could not delete: ");
                    stringBuilder.append(file);
                    throw new IOException(stringBuilder.toString());
                }
            }
        } catch (Throwable th) {
            if (this.memory == null) {
                this.memory = new MemoryOutput();
            } else {
                this.memory.reset();
            }
            this.out = this.memory;
            if (this.file != null) {
                File file2 = this.file;
                this.file = null;
                if (!file2.delete()) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Could not delete: ");
                    stringBuilder2.append(file2);
                    IOException iOException = new IOException(stringBuilder2.toString());
                }
            }
        }
    }

    public synchronized void write(int i) throws IOException {
        update(1);
        this.out.write(i);
    }

    public synchronized void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
        update(i2);
        this.out.write(bArr, i, i2);
    }

    public synchronized void close() throws IOException {
        this.out.close();
    }

    public synchronized void flush() throws IOException {
        this.out.flush();
    }

    private void update(int i) throws IOException {
        if (this.file == null && this.memory.getCount() + i > this.fileThreshold) {
            i = File.createTempFile("FileBackedOutputStream", null);
            if (this.resetOnFinalize) {
                i.deleteOnExit();
            }
            OutputStream fileOutputStream = new FileOutputStream(i);
            fileOutputStream.write(this.memory.getBuffer(), 0, this.memory.getCount());
            fileOutputStream.flush();
            this.out = fileOutputStream;
            this.file = i;
            this.memory = null;
        }
    }
}
