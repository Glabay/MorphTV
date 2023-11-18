package com.github.se_bastiaan.torrentstream;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

class TorrentInputStream extends FilterInputStream implements AlertListener {
    private long location;
    private boolean stopped;
    private Torrent torrent;

    /* renamed from: com.github.se_bastiaan.torrentstream.TorrentInputStream$1 */
    static /* synthetic */ class C06281 {
        static final /* synthetic */ int[] $SwitchMap$com$frostwire$jlibtorrent$alerts$AlertType = new int[AlertType.values().length];

        static {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = com.frostwire.jlibtorrent.alerts.AlertType.values();
            r0 = r0.length;
            r0 = new int[r0];
            $SwitchMap$com$frostwire$jlibtorrent$alerts$AlertType = r0;
            r0 = $SwitchMap$com$frostwire$jlibtorrent$alerts$AlertType;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = com.frostwire.jlibtorrent.alerts.AlertType.PIECE_FINISHED;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = 1;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.github.se_bastiaan.torrentstream.TorrentInputStream.1.<clinit>():void");
        }
    }

    public boolean markSupported() {
        return false;
    }

    TorrentInputStream(Torrent torrent, InputStream inputStream) {
        super(inputStream);
        this.torrent = torrent;
    }

    protected void finalize() throws Throwable {
        synchronized (this) {
            this.stopped = true;
            notifyAll();
        }
        super.finalize();
    }

    private synchronized boolean waitForPiece(long r2) {
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
        r1 = this;
        monitor-enter(r1);
    L_0x0001:
        r0 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0029 }
        r0 = r0.isInterrupted();	 Catch:{ all -> 0x0029 }
        if (r0 != 0) goto L_0x0026;	 Catch:{ all -> 0x0029 }
    L_0x000b:
        r0 = r1.stopped;	 Catch:{ all -> 0x0029 }
        if (r0 != 0) goto L_0x0026;
    L_0x000f:
        r0 = r1.torrent;	 Catch:{ InterruptedException -> 0x001e }
        r0 = r0.hasBytes(r2);	 Catch:{ InterruptedException -> 0x001e }
        if (r0 == 0) goto L_0x001a;
    L_0x0017:
        r2 = 1;
        monitor-exit(r1);
        return r2;
    L_0x001a:
        r1.wait();	 Catch:{ InterruptedException -> 0x001e }
        goto L_0x0001;
    L_0x001e:
        r0 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0029 }
        r0.interrupt();	 Catch:{ all -> 0x0029 }
        goto L_0x0001;
    L_0x0026:
        r2 = 0;
        monitor-exit(r1);
        return r2;
    L_0x0029:
        r2 = move-exception;
        monitor-exit(r1);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.se_bastiaan.torrentstream.TorrentInputStream.waitForPiece(long):boolean");
    }

    public synchronized int read() throws IOException {
        if (!waitForPiece(this.location)) {
            return -1;
        }
        this.location++;
        return super.read();
    }

    public synchronized int read(byte[] bArr, int i, int i2) throws IOException {
        int pieceLength = this.torrent.getTorrentHandle().torrentFile().pieceLength();
        for (int i3 = 0; i3 < i2; i3 += pieceLength) {
            if (!waitForPiece(this.location + ((long) i3))) {
                return -1;
            }
        }
        this.location += (long) i2;
        return super.read(bArr, i, i2);
    }

    public void close() throws IOException {
        synchronized (this) {
            this.stopped = true;
            notifyAll();
        }
        super.close();
    }

    public synchronized long skip(long j) throws IOException {
        this.location += j;
        return super.skip(j);
    }

    private synchronized void pieceFinished() {
        notifyAll();
    }

    public int[] types() {
        return new int[]{AlertType.PIECE_FINISHED.swig()};
    }

    public void alert(Alert<?> alert) {
        if (C06281.$SwitchMap$com$frostwire$jlibtorrent$alerts$AlertType[alert.type().ordinal()] == 1) {
            pieceFinished();
        }
    }
}
