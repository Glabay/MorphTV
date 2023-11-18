package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

public class Timeout {
    public static final Timeout NONE = new Timeout$1();
    private long deadlineNanoTime;
    private boolean hasDeadline;
    private long timeoutNanos;

    public Timeout timeout(long j, TimeUnit timeUnit) {
        if (j < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("timeout < 0: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        } else {
            this.timeoutNanos = timeUnit.toNanos(j);
            return this;
        }
    }

    public long timeoutNanos() {
        return this.timeoutNanos;
    }

    public boolean hasDeadline() {
        return this.hasDeadline;
    }

    public long deadlineNanoTime() {
        if (this.hasDeadline) {
            return this.deadlineNanoTime;
        }
        throw new IllegalStateException("No deadline");
    }

    public Timeout deadlineNanoTime(long j) {
        this.hasDeadline = true;
        this.deadlineNanoTime = j;
        return this;
    }

    public final Timeout deadline(long j, TimeUnit timeUnit) {
        if (j <= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("duration <= 0: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (timeUnit != null) {
            return deadlineNanoTime(System.nanoTime() + timeUnit.toNanos(j));
        } else {
            throw new IllegalArgumentException("unit == null");
        }
    }

    public Timeout clearTimeout() {
        this.timeoutNanos = 0;
        return this;
    }

    public Timeout clearDeadline() {
        this.hasDeadline = false;
        return this;
    }

    public void throwIfReached() throws IOException {
        if (Thread.interrupted()) {
            throw new InterruptedIOException("thread interrupted");
        } else if (this.hasDeadline && this.deadlineNanoTime - System.nanoTime() <= 0) {
            throw new InterruptedIOException("deadline reached");
        }
    }

    public final void waitUntilNotified(java.lang.Object r12) throws java.io.InterruptedIOException {
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
        r11 = this;
        r0 = r11.hasDeadline();	 Catch:{ InterruptedException -> 0x005b }
        r1 = r11.timeoutNanos();	 Catch:{ InterruptedException -> 0x005b }
        r3 = 0;	 Catch:{ InterruptedException -> 0x005b }
        if (r0 != 0) goto L_0x0014;	 Catch:{ InterruptedException -> 0x005b }
    L_0x000c:
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ InterruptedException -> 0x005b }
        if (r5 != 0) goto L_0x0014;	 Catch:{ InterruptedException -> 0x005b }
    L_0x0010:
        r12.wait();	 Catch:{ InterruptedException -> 0x005b }
        return;	 Catch:{ InterruptedException -> 0x005b }
    L_0x0014:
        r5 = java.lang.System.nanoTime();	 Catch:{ InterruptedException -> 0x005b }
        if (r0 == 0) goto L_0x002a;	 Catch:{ InterruptedException -> 0x005b }
    L_0x001a:
        r7 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ InterruptedException -> 0x005b }
        if (r7 == 0) goto L_0x002a;	 Catch:{ InterruptedException -> 0x005b }
    L_0x001e:
        r7 = r11.deadlineNanoTime();	 Catch:{ InterruptedException -> 0x005b }
        r0 = 0;	 Catch:{ InterruptedException -> 0x005b }
        r9 = r7 - r5;	 Catch:{ InterruptedException -> 0x005b }
        r1 = java.lang.Math.min(r1, r9);	 Catch:{ InterruptedException -> 0x005b }
        goto L_0x0034;	 Catch:{ InterruptedException -> 0x005b }
    L_0x002a:
        if (r0 == 0) goto L_0x0034;	 Catch:{ InterruptedException -> 0x005b }
    L_0x002c:
        r0 = r11.deadlineNanoTime();	 Catch:{ InterruptedException -> 0x005b }
        r2 = 0;	 Catch:{ InterruptedException -> 0x005b }
        r7 = r0 - r5;	 Catch:{ InterruptedException -> 0x005b }
        r1 = r7;	 Catch:{ InterruptedException -> 0x005b }
    L_0x0034:
        r0 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ InterruptedException -> 0x005b }
        if (r0 <= 0) goto L_0x004e;	 Catch:{ InterruptedException -> 0x005b }
    L_0x0038:
        r3 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;	 Catch:{ InterruptedException -> 0x005b }
        r7 = r1 / r3;	 Catch:{ InterruptedException -> 0x005b }
        r3 = r3 * r7;	 Catch:{ InterruptedException -> 0x005b }
        r0 = 0;	 Catch:{ InterruptedException -> 0x005b }
        r9 = r1 - r3;	 Catch:{ InterruptedException -> 0x005b }
        r0 = (int) r9;	 Catch:{ InterruptedException -> 0x005b }
        r12.wait(r7, r0);	 Catch:{ InterruptedException -> 0x005b }
        r3 = java.lang.System.nanoTime();	 Catch:{ InterruptedException -> 0x005b }
        r12 = 0;	 Catch:{ InterruptedException -> 0x005b }
        r7 = r3 - r5;	 Catch:{ InterruptedException -> 0x005b }
        r3 = r7;	 Catch:{ InterruptedException -> 0x005b }
    L_0x004e:
        r12 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));	 Catch:{ InterruptedException -> 0x005b }
        if (r12 < 0) goto L_0x005a;	 Catch:{ InterruptedException -> 0x005b }
    L_0x0052:
        r12 = new java.io.InterruptedIOException;	 Catch:{ InterruptedException -> 0x005b }
        r0 = "timeout";	 Catch:{ InterruptedException -> 0x005b }
        r12.<init>(r0);	 Catch:{ InterruptedException -> 0x005b }
        throw r12;	 Catch:{ InterruptedException -> 0x005b }
    L_0x005a:
        return;
    L_0x005b:
        r12 = new java.io.InterruptedIOException;
        r0 = "interrupted";
        r12.<init>(r0);
        throw r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Timeout.waitUntilNotified(java.lang.Object):void");
    }
}
