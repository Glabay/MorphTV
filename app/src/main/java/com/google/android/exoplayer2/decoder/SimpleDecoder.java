package com.google.android.exoplayer2.decoder;

import com.google.android.exoplayer2.util.Assertions;
import java.util.LinkedList;

public abstract class SimpleDecoder<I extends DecoderInputBuffer, O extends OutputBuffer, E extends Exception> implements Decoder<I, O, E> {
    private int availableInputBufferCount;
    private final I[] availableInputBuffers;
    private int availableOutputBufferCount;
    private final O[] availableOutputBuffers;
    private final Thread decodeThread;
    private I dequeuedInputBuffer;
    private E exception;
    private boolean flushed;
    private final Object lock = new Object();
    private final LinkedList<I> queuedInputBuffers = new LinkedList();
    private final LinkedList<O> queuedOutputBuffers = new LinkedList();
    private boolean released;
    private int skippedOutputBufferCount;

    /* renamed from: com.google.android.exoplayer2.decoder.SimpleDecoder$1 */
    class C06711 extends Thread {
        C06711() {
        }

        public void run() {
            SimpleDecoder.this.run();
        }
    }

    protected abstract I createInputBuffer();

    protected abstract O createOutputBuffer();

    protected abstract E createUnexpectedDecodeException(Throwable th);

    protected abstract E decode(I i, O o, boolean z);

    protected SimpleDecoder(I[] iArr, O[] oArr) {
        this.availableInputBuffers = iArr;
        this.availableInputBufferCount = iArr.length;
        for (int i = 0; i < this.availableInputBufferCount; i++) {
            this.availableInputBuffers[i] = createInputBuffer();
        }
        this.availableOutputBuffers = oArr;
        this.availableOutputBufferCount = oArr.length;
        for (iArr = null; iArr < this.availableOutputBufferCount; iArr++) {
            this.availableOutputBuffers[iArr] = createOutputBuffer();
        }
        this.decodeThread = new C06711();
        this.decodeThread.start();
    }

    protected final void setInitialInputBufferSize(int i) {
        Assertions.checkState(this.availableInputBufferCount == this.availableInputBuffers.length);
        for (DecoderInputBuffer ensureSpaceForWrite : this.availableInputBuffers) {
            ensureSpaceForWrite.ensureSpaceForWrite(i);
        }
    }

    public final I dequeueInputBuffer() throws Exception {
        I i;
        synchronized (this.lock) {
            DecoderInputBuffer decoderInputBuffer;
            maybeThrowException();
            Assertions.checkState(this.dequeuedInputBuffer == null);
            if (this.availableInputBufferCount == 0) {
                decoderInputBuffer = null;
            } else {
                DecoderInputBuffer[] decoderInputBufferArr = this.availableInputBuffers;
                int i2 = this.availableInputBufferCount - 1;
                this.availableInputBufferCount = i2;
                decoderInputBuffer = decoderInputBufferArr[i2];
            }
            this.dequeuedInputBuffer = decoderInputBuffer;
            i = this.dequeuedInputBuffer;
        }
        return i;
    }

    public final void queueInputBuffer(I i) throws Exception {
        synchronized (this.lock) {
            maybeThrowException();
            Assertions.checkArgument(i == this.dequeuedInputBuffer);
            this.queuedInputBuffers.addLast(i);
            maybeNotifyDecodeLoop();
            this.dequeuedInputBuffer = null;
        }
    }

    public final O dequeueOutputBuffer() throws Exception {
        synchronized (this.lock) {
            maybeThrowException();
            if (this.queuedOutputBuffers.isEmpty()) {
                return null;
            }
            OutputBuffer outputBuffer = (OutputBuffer) this.queuedOutputBuffers.removeFirst();
            return outputBuffer;
        }
    }

    protected void releaseOutputBuffer(O o) {
        synchronized (this.lock) {
            releaseOutputBufferInternal(o);
            maybeNotifyDecodeLoop();
        }
    }

    public final void flush() {
        synchronized (this.lock) {
            this.flushed = true;
            this.skippedOutputBufferCount = 0;
            if (this.dequeuedInputBuffer != null) {
                releaseInputBufferInternal(this.dequeuedInputBuffer);
                this.dequeuedInputBuffer = null;
            }
            while (!this.queuedInputBuffers.isEmpty()) {
                releaseInputBufferInternal((DecoderInputBuffer) this.queuedInputBuffers.removeFirst());
            }
            while (!this.queuedOutputBuffers.isEmpty()) {
                releaseOutputBufferInternal((OutputBuffer) this.queuedOutputBuffers.removeFirst());
            }
        }
    }

    public void release() {
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
        r2 = this;
        r0 = r2.lock;
        monitor-enter(r0);
        r1 = 1;
        r2.released = r1;	 Catch:{ all -> 0x001a }
        r1 = r2.lock;	 Catch:{ all -> 0x001a }
        r1.notify();	 Catch:{ all -> 0x001a }
        monitor-exit(r0);	 Catch:{ all -> 0x001a }
        r0 = r2.decodeThread;	 Catch:{ InterruptedException -> 0x0012 }
        r0.join();	 Catch:{ InterruptedException -> 0x0012 }
        goto L_0x0019;
    L_0x0012:
        r0 = java.lang.Thread.currentThread();
        r0.interrupt();
    L_0x0019:
        return;
    L_0x001a:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001a }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.decoder.SimpleDecoder.release():void");
    }

    private void maybeThrowException() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
    }

    private void maybeNotifyDecodeLoop() {
        if (canDecodeBuffer()) {
            this.lock.notify();
        }
    }

    private void run() {
        while (decode()) {
            try {
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean decode() throws java.lang.InterruptedException {
        /*
        r6 = this;
        r0 = r6.lock;
        monitor-enter(r0);
    L_0x0003:
        r1 = r6.released;	 Catch:{ all -> 0x0096 }
        if (r1 != 0) goto L_0x0013;
    L_0x0007:
        r1 = r6.canDecodeBuffer();	 Catch:{ all -> 0x0096 }
        if (r1 != 0) goto L_0x0013;
    L_0x000d:
        r1 = r6.lock;	 Catch:{ all -> 0x0096 }
        r1.wait();	 Catch:{ all -> 0x0096 }
        goto L_0x0003;
    L_0x0013:
        r1 = r6.released;	 Catch:{ all -> 0x0096 }
        r2 = 0;
        if (r1 == 0) goto L_0x001a;
    L_0x0018:
        monitor-exit(r0);	 Catch:{ all -> 0x0096 }
        return r2;
    L_0x001a:
        r1 = r6.queuedInputBuffers;	 Catch:{ all -> 0x0096 }
        r1 = r1.removeFirst();	 Catch:{ all -> 0x0096 }
        r1 = (com.google.android.exoplayer2.decoder.DecoderInputBuffer) r1;	 Catch:{ all -> 0x0096 }
        r3 = r6.availableOutputBuffers;	 Catch:{ all -> 0x0096 }
        r4 = r6.availableOutputBufferCount;	 Catch:{ all -> 0x0096 }
        r5 = 1;
        r4 = r4 - r5;
        r6.availableOutputBufferCount = r4;	 Catch:{ all -> 0x0096 }
        r3 = r3[r4];	 Catch:{ all -> 0x0096 }
        r4 = r6.flushed;	 Catch:{ all -> 0x0096 }
        r6.flushed = r2;	 Catch:{ all -> 0x0096 }
        monitor-exit(r0);	 Catch:{ all -> 0x0096 }
        r0 = r1.isEndOfStream();
        if (r0 == 0) goto L_0x003c;
    L_0x0037:
        r0 = 4;
        r3.addFlag(r0);
        goto L_0x0069;
    L_0x003c:
        r0 = r1.isDecodeOnly();
        if (r0 == 0) goto L_0x0047;
    L_0x0042:
        r0 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r3.addFlag(r0);
    L_0x0047:
        r0 = r6.decode(r1, r3, r4);	 Catch:{ RuntimeException -> 0x0056, OutOfMemoryError -> 0x004e }
        r6.exception = r0;	 Catch:{ RuntimeException -> 0x0056, OutOfMemoryError -> 0x004e }
        goto L_0x005d;
    L_0x004e:
        r0 = move-exception;
        r0 = r6.createUnexpectedDecodeException(r0);
        r6.exception = r0;
        goto L_0x005d;
    L_0x0056:
        r0 = move-exception;
        r0 = r6.createUnexpectedDecodeException(r0);
        r6.exception = r0;
    L_0x005d:
        r0 = r6.exception;
        if (r0 == 0) goto L_0x0069;
    L_0x0061:
        r0 = r6.lock;
        monitor-enter(r0);
        monitor-exit(r0);	 Catch:{ all -> 0x0066 }
        return r2;
    L_0x0066:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0066 }
        throw r1;
    L_0x0069:
        r4 = r6.lock;
        monitor-enter(r4);
        r0 = r6.flushed;	 Catch:{ all -> 0x0093 }
        if (r0 == 0) goto L_0x0074;
    L_0x0070:
        r6.releaseOutputBufferInternal(r3);	 Catch:{ all -> 0x0093 }
        goto L_0x008e;
    L_0x0074:
        r0 = r3.isDecodeOnly();	 Catch:{ all -> 0x0093 }
        if (r0 == 0) goto L_0x0083;
    L_0x007a:
        r0 = r6.skippedOutputBufferCount;	 Catch:{ all -> 0x0093 }
        r0 = r0 + r5;
        r6.skippedOutputBufferCount = r0;	 Catch:{ all -> 0x0093 }
        r6.releaseOutputBufferInternal(r3);	 Catch:{ all -> 0x0093 }
        goto L_0x008e;
    L_0x0083:
        r0 = r6.skippedOutputBufferCount;	 Catch:{ all -> 0x0093 }
        r3.skippedOutputBufferCount = r0;	 Catch:{ all -> 0x0093 }
        r6.skippedOutputBufferCount = r2;	 Catch:{ all -> 0x0093 }
        r0 = r6.queuedOutputBuffers;	 Catch:{ all -> 0x0093 }
        r0.addLast(r3);	 Catch:{ all -> 0x0093 }
    L_0x008e:
        r6.releaseInputBufferInternal(r1);	 Catch:{ all -> 0x0093 }
        monitor-exit(r4);	 Catch:{ all -> 0x0093 }
        return r5;
    L_0x0093:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0093 }
        throw r0;
    L_0x0096:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0096 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.decoder.SimpleDecoder.decode():boolean");
    }

    private boolean canDecodeBuffer() {
        return !this.queuedInputBuffers.isEmpty() && this.availableOutputBufferCount > 0;
    }

    private void releaseInputBufferInternal(I i) {
        i.clear();
        DecoderInputBuffer[] decoderInputBufferArr = this.availableInputBuffers;
        int i2 = this.availableInputBufferCount;
        this.availableInputBufferCount = i2 + 1;
        decoderInputBufferArr[i2] = i;
    }

    private void releaseOutputBufferInternal(O o) {
        o.clear();
        OutputBuffer[] outputBufferArr = this.availableOutputBuffers;
        int i = this.availableOutputBufferCount;
        this.availableOutputBufferCount = i + 1;
        outputBufferArr[i] = o;
    }
}
