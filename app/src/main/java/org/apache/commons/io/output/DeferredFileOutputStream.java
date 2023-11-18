package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

public class DeferredFileOutputStream extends ThresholdingOutputStream {
    private boolean closed;
    private OutputStream currentOutputStream;
    private final File directory;
    private ByteArrayOutputStream memoryOutputStream;
    private File outputFile;
    private final String prefix;
    private final String suffix;

    public DeferredFileOutputStream(int i, File file) {
        this(i, file, null, null, null, 1024);
    }

    public DeferredFileOutputStream(int i, int i2, File file) {
        this(i, file, null, null, null, i2);
        if (i2 < 0) {
            throw new IllegalArgumentException("Initial buffer size must be atleast 0.");
        }
    }

    public DeferredFileOutputStream(int i, String str, String str2, File file) {
        this(i, null, str, str2, file, 1024);
        if (str == null) {
            throw new IllegalArgumentException("Temporary file prefix is missing");
        }
    }

    public DeferredFileOutputStream(int i, int i2, String str, String str2, File file) {
        this(i, null, str, str2, file, i2);
        if (str == null) {
            throw new IllegalArgumentException("Temporary file prefix is missing");
        } else if (i2 < 0) {
            throw new IllegalArgumentException("Initial buffer size must be atleast 0.");
        }
    }

    private DeferredFileOutputStream(int i, File file, String str, String str2, File file2, int i2) {
        super(i);
        this.closed = false;
        this.outputFile = file;
        this.prefix = str;
        this.suffix = str2;
        this.directory = file2;
        this.memoryOutputStream = new ByteArrayOutputStream(i2);
        this.currentOutputStream = this.memoryOutputStream;
    }

    protected OutputStream getStream() throws IOException {
        return this.currentOutputStream;
    }

    protected void thresholdReached() throws IOException {
        if (this.prefix != null) {
            this.outputFile = File.createTempFile(this.prefix, this.suffix, this.directory);
        }
        FileUtils.forceMkdirParent(this.outputFile);
        OutputStream fileOutputStream = new FileOutputStream(this.outputFile);
        try {
            this.memoryOutputStream.writeTo(fileOutputStream);
            this.currentOutputStream = fileOutputStream;
            this.memoryOutputStream = null;
        } catch (IOException e) {
            fileOutputStream.close();
            throw e;
        }
    }

    public boolean isInMemory() {
        return isThresholdExceeded() ^ 1;
    }

    public byte[] getData() {
        return this.memoryOutputStream != null ? this.memoryOutputStream.toByteArray() : null;
    }

    public File getFile() {
        return this.outputFile;
    }

    public void close() throws IOException {
        super.close();
        this.closed = true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeTo(java.io.OutputStream r3) throws java.io.IOException {
        /*
        r2 = this;
        r0 = r2.closed;
        if (r0 != 0) goto L_0x000c;
    L_0x0004:
        r3 = new java.io.IOException;
        r0 = "Stream not closed";
        r3.<init>(r0);
        throw r3;
    L_0x000c:
        r0 = r2.isInMemory();
        if (r0 == 0) goto L_0x0018;
    L_0x0012:
        r0 = r2.memoryOutputStream;
        r0.writeTo(r3);
        goto L_0x0028;
    L_0x0018:
        r0 = new java.io.FileInputStream;
        r1 = r2.outputFile;
        r0.<init>(r1);
        r1 = 0;
        org.apache.commons.io.IOUtils.copy(r0, r3);	 Catch:{ Throwable -> 0x002b }
        if (r0 == 0) goto L_0x0028;
    L_0x0025:
        r0.close();
    L_0x0028:
        return;
    L_0x0029:
        r3 = move-exception;
        goto L_0x002d;
    L_0x002b:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0029 }
    L_0x002d:
        if (r0 == 0) goto L_0x003d;
    L_0x002f:
        if (r1 == 0) goto L_0x003a;
    L_0x0031:
        r0.close();	 Catch:{ Throwable -> 0x0035 }
        goto L_0x003d;
    L_0x0035:
        r0 = move-exception;
        r1.addSuppressed(r0);
        goto L_0x003d;
    L_0x003a:
        r0.close();
    L_0x003d:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.output.DeferredFileOutputStream.writeTo(java.io.OutputStream):void");
    }
}
