package org.apache.commons.io.input;

import com.google.common.base.Ascii;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public class Tailer implements Runnable {
    private static final int DEFAULT_BUFSIZE = 4096;
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
    private static final int DEFAULT_DELAY_MILLIS = 1000;
    private static final String RAF_MODE = "r";
    private final Charset cset;
    private final long delayMillis;
    private final boolean end;
    private final File file;
    private final byte[] inbuf;
    private final TailerListener listener;
    private final boolean reOpen;
    private volatile boolean run;

    public Tailer(File file, TailerListener tailerListener) {
        this(file, tailerListener, 1000);
    }

    public Tailer(File file, TailerListener tailerListener, long j) {
        this(file, tailerListener, j, false);
    }

    public Tailer(File file, TailerListener tailerListener, long j, boolean z) {
        this(file, tailerListener, j, z, 4096);
    }

    public Tailer(File file, TailerListener tailerListener, long j, boolean z, boolean z2) {
        this(file, tailerListener, j, z, z2, 4096);
    }

    public Tailer(File file, TailerListener tailerListener, long j, boolean z, int i) {
        this(file, tailerListener, j, z, false, i);
    }

    public Tailer(File file, TailerListener tailerListener, long j, boolean z, boolean z2, int i) {
        this(file, DEFAULT_CHARSET, tailerListener, j, z, z2, i);
    }

    public Tailer(File file, Charset charset, TailerListener tailerListener, long j, boolean z, boolean z2, int i) {
        this.run = true;
        this.file = file;
        this.delayMillis = j;
        this.end = z;
        this.inbuf = new byte[i];
        this.listener = tailerListener;
        tailerListener.init(this);
        this.reOpen = z2;
        this.cset = charset;
    }

    public static Tailer create(File file, TailerListener tailerListener, long j, boolean z, int i) {
        return create(file, tailerListener, j, z, false, i);
    }

    public static Tailer create(File file, TailerListener tailerListener, long j, boolean z, boolean z2, int i) {
        return create(file, DEFAULT_CHARSET, tailerListener, j, z, z2, i);
    }

    public static Tailer create(File file, Charset charset, TailerListener tailerListener, long j, boolean z, boolean z2, int i) {
        Tailer tailer = new Tailer(file, charset, tailerListener, j, z, z2, i);
        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener tailerListener, long j, boolean z) {
        return create(file, tailerListener, j, z, 4096);
    }

    public static Tailer create(File file, TailerListener tailerListener, long j, boolean z, boolean z2) {
        return create(file, tailerListener, j, z, z2, 4096);
    }

    public static Tailer create(File file, TailerListener tailerListener, long j) {
        return create(file, tailerListener, j, false);
    }

    public static Tailer create(File file, TailerListener tailerListener) {
        return create(file, tailerListener, 1000, false);
    }

    public File getFile() {
        return this.file;
    }

    protected boolean getRun() {
        return this.run;
    }

    public long getDelay() {
        return this.delayMillis;
    }

    public void run() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r13 = this;
        r0 = 0;
        r1 = 0;
        r3 = r0;
        r4 = r1;
        r6 = r4;
    L_0x0006:
        r8 = r13.getRun();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        if (r8 == 0) goto L_0x003d;
    L_0x000c:
        if (r3 != 0) goto L_0x003d;
    L_0x000e:
        r8 = new java.io.RandomAccessFile;	 Catch:{ FileNotFoundException -> 0x0019 }
        r9 = r13.file;	 Catch:{ FileNotFoundException -> 0x0019 }
        r10 = "r";	 Catch:{ FileNotFoundException -> 0x0019 }
        r8.<init>(r9, r10);	 Catch:{ FileNotFoundException -> 0x0019 }
        r3 = r8;
        goto L_0x001e;
    L_0x0019:
        r8 = r13.listener;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r8.fileNotFound();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x001e:
        if (r3 != 0) goto L_0x0026;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x0020:
        r8 = r13.delayMillis;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        java.lang.Thread.sleep(r8);	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        goto L_0x0006;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x0026:
        r4 = r13.end;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        if (r4 == 0) goto L_0x0032;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x002a:
        r4 = r13.file;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r4 = r4.length();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r6 = r4;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        goto L_0x0033;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x0032:
        r6 = r1;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x0033:
        r4 = r13.file;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r4 = r4.lastModified();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r3.seek(r6);	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        goto L_0x0006;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x003d:
        r8 = r13.getRun();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        if (r8 == 0) goto L_0x0103;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x0043:
        r8 = r13.file;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r8 = org.apache.commons.io.FileUtils.isFileNewer(r8, r4);	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r9 = r13.file;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r9 = r9.length();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r11 = (r9 > r6 ? 1 : (r9 == r6 ? 0 : -1));	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        if (r11 >= 0) goto L_0x00c0;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x0053:
        r8 = r13.listener;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r8.fileRotated();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r8 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x0090, all -> 0x008c }
        r9 = r13.file;	 Catch:{ Throwable -> 0x0090, all -> 0x008c }
        r10 = "r";	 Catch:{ Throwable -> 0x0090, all -> 0x008c }
        r8.<init>(r9, r10);	 Catch:{ Throwable -> 0x0090, all -> 0x008c }
        r13.readLines(r3);	 Catch:{ IOException -> 0x006e }
        goto L_0x0074;
    L_0x0065:
        r9 = move-exception;
        r10 = r8;
        r8 = r0;
        goto L_0x0097;
    L_0x0069:
        r9 = move-exception;
        r12 = r9;
        r9 = r8;
        r8 = r12;
        goto L_0x0092;
    L_0x006e:
        r9 = move-exception;
        r10 = r13.listener;	 Catch:{ Throwable -> 0x0069, all -> 0x0065 }
        r10.handle(r9);	 Catch:{ Throwable -> 0x0069, all -> 0x0065 }
    L_0x0074:
        if (r3 == 0) goto L_0x0089;
    L_0x0076:
        r3.close();	 Catch:{ FileNotFoundException -> 0x0086 }
        goto L_0x0089;
    L_0x007a:
        r0 = move-exception;
        r3 = r8;
        goto L_0x0139;
    L_0x007e:
        r0 = move-exception;
        r3 = r8;
        goto L_0x010e;
    L_0x0082:
        r0 = move-exception;
        r3 = r8;
        goto L_0x0124;
    L_0x0086:
        r6 = r1;
        r3 = r8;
        goto L_0x00b4;
    L_0x0089:
        r6 = r1;
    L_0x008a:
        r3 = r8;
        goto L_0x003d;
    L_0x008c:
        r9 = move-exception;
        r8 = r0;
        r10 = r3;
        goto L_0x0097;
    L_0x0090:
        r8 = move-exception;
        r9 = r3;
    L_0x0092:
        throw r8;	 Catch:{ all -> 0x0093 }
    L_0x0093:
        r10 = move-exception;
        r12 = r10;
        r10 = r9;
        r9 = r12;
    L_0x0097:
        if (r3 == 0) goto L_0x00a7;
    L_0x0099:
        if (r8 == 0) goto L_0x00a4;
    L_0x009b:
        r3.close();	 Catch:{ Throwable -> 0x009f }
        goto L_0x00a7;
    L_0x009f:
        r3 = move-exception;
        r8.addSuppressed(r3);	 Catch:{ FileNotFoundException -> 0x00b3, InterruptedException -> 0x00af, Exception -> 0x00ac, all -> 0x00a8 }
        goto L_0x00a7;	 Catch:{ FileNotFoundException -> 0x00b3, InterruptedException -> 0x00af, Exception -> 0x00ac, all -> 0x00a8 }
    L_0x00a4:
        r3.close();	 Catch:{ FileNotFoundException -> 0x00b3, InterruptedException -> 0x00af, Exception -> 0x00ac, all -> 0x00a8 }
    L_0x00a7:
        throw r9;	 Catch:{ FileNotFoundException -> 0x00b3, InterruptedException -> 0x00af, Exception -> 0x00ac, all -> 0x00a8 }
    L_0x00a8:
        r0 = move-exception;
        r3 = r10;
        goto L_0x0139;
    L_0x00ac:
        r0 = move-exception;
        r3 = r10;
        goto L_0x010e;
    L_0x00af:
        r0 = move-exception;
        r3 = r10;
        goto L_0x0124;
    L_0x00b3:
        r3 = r10;
    L_0x00b4:
        r8 = r13.listener;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r8.fileNotFound();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r8 = r13.delayMillis;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        java.lang.Thread.sleep(r8);	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        goto L_0x003d;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00c0:
        r11 = (r9 > r6 ? 1 : (r9 == r6 ? 0 : -1));	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        if (r11 <= 0) goto L_0x00cf;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00c4:
        r6 = r13.readLines(r3);	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r4 = r13.file;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r4 = r4.lastModified();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        goto L_0x00de;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00cf:
        if (r8 == 0) goto L_0x00de;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00d1:
        r3.seek(r1);	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r6 = r13.readLines(r3);	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r4 = r13.file;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r4 = r4.lastModified();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00de:
        r8 = r13.reOpen;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        if (r8 == 0) goto L_0x00e7;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00e2:
        if (r3 == 0) goto L_0x00e7;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00e4:
        r3.close();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00e7:
        r8 = r13.delayMillis;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        java.lang.Thread.sleep(r8);	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r8 = r13.getRun();	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        if (r8 == 0) goto L_0x003d;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00f2:
        r8 = r13.reOpen;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        if (r8 == 0) goto L_0x003d;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
    L_0x00f6:
        r8 = new java.io.RandomAccessFile;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r9 = r13.file;	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r10 = "r";	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r8.<init>(r9, r10);	 Catch:{ InterruptedException -> 0x0123, Exception -> 0x010d }
        r8.seek(r6);	 Catch:{ InterruptedException -> 0x0082, Exception -> 0x007e, all -> 0x007a }
        goto L_0x008a;
    L_0x0103:
        if (r3 == 0) goto L_0x011f;
    L_0x0105:
        r3.close();	 Catch:{ IOException -> 0x0109 }
        goto L_0x011f;
    L_0x0109:
        r0 = move-exception;
        goto L_0x011a;
    L_0x010b:
        r0 = move-exception;
        goto L_0x0139;
    L_0x010d:
        r0 = move-exception;
    L_0x010e:
        r1 = r13.listener;	 Catch:{ all -> 0x010b }
        r1.handle(r0);	 Catch:{ all -> 0x010b }
        if (r3 == 0) goto L_0x011f;
    L_0x0115:
        r3.close();	 Catch:{ IOException -> 0x0119 }
        goto L_0x011f;
    L_0x0119:
        r0 = move-exception;
    L_0x011a:
        r1 = r13.listener;
        r1.handle(r0);
    L_0x011f:
        r13.stop();
        goto L_0x0138;
    L_0x0123:
        r0 = move-exception;
    L_0x0124:
        r1 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x010b }
        r1.interrupt();	 Catch:{ all -> 0x010b }
        r1 = r13.listener;	 Catch:{ all -> 0x010b }
        r1.handle(r0);	 Catch:{ all -> 0x010b }
        if (r3 == 0) goto L_0x011f;
    L_0x0132:
        r3.close();	 Catch:{ IOException -> 0x0136 }
        goto L_0x011f;
    L_0x0136:
        r0 = move-exception;
        goto L_0x011a;
    L_0x0138:
        return;
    L_0x0139:
        if (r3 == 0) goto L_0x0145;
    L_0x013b:
        r3.close();	 Catch:{ IOException -> 0x013f }
        goto L_0x0145;
    L_0x013f:
        r1 = move-exception;
        r2 = r13.listener;
        r2.handle(r1);
    L_0x0145:
        r13.stop();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.input.Tailer.run():void");
    }

    public void stop() {
        this.run = false;
    }

    private long readLines(RandomAccessFile randomAccessFile) throws IOException {
        Throwable th;
        Tailer tailer = this;
        RandomAccessFile randomAccessFile2 = randomAccessFile;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(64);
        try {
            long filePointer = randomAccessFile.getFilePointer();
            long j = filePointer;
            Object obj = null;
            while (getRun()) {
                int read = randomAccessFile2.read(tailer.inbuf);
                if (read == -1) {
                    break;
                }
                long j2 = filePointer;
                for (int i = 0; i < read; i++) {
                    byte b = tailer.inbuf[i];
                    if (b == (byte) 10) {
                        tailer.listener.handle(new String(byteArrayOutputStream.toByteArray(), tailer.cset));
                        byteArrayOutputStream.reset();
                        j2 = (j + ((long) i)) + 1;
                        obj = null;
                    } else if (b != Ascii.CR) {
                        if (obj != null) {
                            tailer.listener.handle(new String(byteArrayOutputStream.toByteArray(), tailer.cset));
                            byteArrayOutputStream.reset();
                            j2 = (j + ((long) i)) + 1;
                            obj = null;
                        }
                        byteArrayOutputStream.write(b);
                    } else {
                        if (obj != null) {
                            byteArrayOutputStream.write(13);
                        }
                        obj = 1;
                    }
                }
                j = randomAccessFile.getFilePointer();
                filePointer = j2;
            }
            randomAccessFile2.seek(filePointer);
            if (tailer.listener instanceof TailerListenerAdapter) {
                ((TailerListenerAdapter) tailer.listener).endOfFileReached();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            return filePointer;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
    }
}
