package com.github.se_bastiaan.torrentstream;

import android.os.Handler;
import android.os.HandlerThread;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.SessionParams;
import com.frostwire.jlibtorrent.SettingsPack;
import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.swig.settings_pack.string_types;
import com.github.se_bastiaan.torrentstream.exceptions.NotInitializedException;
import com.github.se_bastiaan.torrentstream.listeners.DHTStatsAlertListener;
import com.github.se_bastiaan.torrentstream.listeners.TorrentAddedAlertListener;
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener;
import com.github.se_bastiaan.torrentstream.utils.ThreadUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public final class TorrentStream {
    private static final String LIBTORRENT_THREAD_NAME = "TORRENTSTREAM_LIBTORRENT";
    private static final String STREAMING_THREAD_NAME = "TORRENTSTREAMER_STREAMING";
    private static TorrentStream sThis;
    private Torrent currentTorrent;
    private String currentTorrentUrl;
    private Integer dhtNodes = Integer.valueOf(0);
    private final DHTStatsAlertListener dhtStatsAlertListener = new C06301();
    private Boolean initialised = Boolean.valueOf(false);
    private Boolean initialising = Boolean.valueOf(false);
    private CountDownLatch initialisingLatch;
    private Boolean isCanceled = Boolean.valueOf(false);
    private Boolean isStreaming = Boolean.valueOf(false);
    private Handler libTorrentHandler;
    private HandlerThread libTorrentThread;
    private final List<TorrentListener> listeners = new ArrayList();
    private Handler streamingHandler;
    private HandlerThread streamingThread;
    private final TorrentAddedAlertListener torrentAddedAlertListener = new C06312();
    private TorrentOptions torrentOptions;
    private SessionManager torrentSession;

    /* renamed from: com.github.se_bastiaan.torrentstream.TorrentStream$1 */
    class C06301 extends DHTStatsAlertListener {
        C06301() {
        }

        public void stats(int i) {
            TorrentStream.this.dhtNodes = Integer.valueOf(i);
        }
    }

    /* renamed from: com.github.se_bastiaan.torrentstream.TorrentStream$2 */
    class C06312 extends TorrentAddedAlertListener {
        C06312() {
        }

        public void torrentAdded(AddTorrentAlert addTorrentAlert) {
            TorrentListener internalTorrentListener = new InternalTorrentListener();
            TorrentStream.this.currentTorrent = new Torrent(TorrentStream.this.torrentSession.find(addTorrentAlert.handle().infoHash()), internalTorrentListener, TorrentStream.this.torrentOptions.prepareSize);
            TorrentStream.this.torrentSession.addListener(TorrentStream.this.currentTorrent);
        }
    }

    /* renamed from: com.github.se_bastiaan.torrentstream.TorrentStream$3 */
    class C06323 implements Runnable {
        C06323() {
        }

        public void run() {
            TorrentStream.this.torrentSession = new SessionManager();
            TorrentStream.this.setOptions(TorrentStream.this.torrentOptions);
            TorrentStream.this.torrentSession.addListener(TorrentStream.this.dhtStatsAlertListener);
            TorrentStream.this.torrentSession.startDht();
            TorrentStream.this.initialising = Boolean.valueOf(false);
            TorrentStream.this.initialised = Boolean.valueOf(true);
            TorrentStream.this.initialisingLatch.countDown();
        }
    }

    /* renamed from: com.github.se_bastiaan.torrentstream.TorrentStream$4 */
    class C06334 implements Runnable {
        C06334() {
        }

        public void run() {
            TorrentStream.this.torrentSession.resume();
        }
    }

    /* renamed from: com.github.se_bastiaan.torrentstream.TorrentStream$5 */
    class C06345 implements Runnable {
        C06345() {
        }

        public void run() {
            TorrentStream.this.torrentSession.startDht();
        }
    }

    /* renamed from: com.github.se_bastiaan.torrentstream.TorrentStream$6 */
    class C06356 implements Runnable {
        C06356() {
        }

        public void run() {
            TorrentStream.this.torrentSession.pause();
        }
    }

    protected class InternalTorrentListener implements TorrentListener {
        public void onStreamStopped() {
        }

        protected InternalTorrentListener() {
        }

        public void onStreamStarted(final Torrent torrent) {
            for (final TorrentListener torrentListener : TorrentStream.this.listeners) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    public void run() {
                        torrentListener.onStreamStarted(torrent);
                    }
                });
            }
        }

        public void onStreamError(final Torrent torrent, final Exception exception) {
            for (final TorrentListener torrentListener : TorrentStream.this.listeners) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    public void run() {
                        torrentListener.onStreamError(torrent, exception);
                    }
                });
            }
        }

        public void onStreamReady(final Torrent torrent) {
            for (final TorrentListener torrentListener : TorrentStream.this.listeners) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    public void run() {
                        torrentListener.onStreamReady(torrent);
                    }
                });
            }
        }

        public void onStreamProgress(final Torrent torrent, final StreamStatus streamStatus) {
            for (final TorrentListener torrentListener : TorrentStream.this.listeners) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    public void run() {
                        torrentListener.onStreamProgress(torrent, streamStatus);
                    }
                });
            }
        }

        public void onStreamPrepared(final Torrent torrent) {
            if (TorrentStream.this.torrentOptions.autoDownload.booleanValue()) {
                torrent.startDownload();
            }
            for (final TorrentListener torrentListener : TorrentStream.this.listeners) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    public void run() {
                        torrentListener.onStreamPrepared(torrent);
                    }
                });
            }
        }
    }

    private TorrentStream(TorrentOptions torrentOptions) {
        this.torrentOptions = torrentOptions;
        initialise();
    }

    public static TorrentStream init(TorrentOptions torrentOptions) {
        sThis = new TorrentStream(torrentOptions);
        return sThis;
    }

    public static TorrentStream getInstance() throws NotInitializedException {
        if (sThis != null) {
            return sThis;
        }
        throw new NotInitializedException();
    }

    private void initialise() {
        if (this.libTorrentThread == null || this.torrentSession == null) {
            if ((this.initialising.booleanValue() || this.initialised.booleanValue()) && this.libTorrentThread != null) {
                this.libTorrentThread.interrupt();
            }
            this.initialising = Boolean.valueOf(true);
            this.initialised = Boolean.valueOf(false);
            this.initialisingLatch = new CountDownLatch(1);
            this.libTorrentThread = new HandlerThread(LIBTORRENT_THREAD_NAME);
            this.libTorrentThread.start();
            this.libTorrentHandler = new Handler(this.libTorrentThread.getLooper());
            this.libTorrentHandler.post(new C06323());
            return;
        }
        resumeSession();
    }

    public void resumeSession() {
        if (this.libTorrentThread != null && this.torrentSession != null) {
            this.libTorrentHandler.removeCallbacksAndMessages(null);
            if (this.torrentSession.isPaused()) {
                this.libTorrentHandler.post(new C06334());
            }
            if (!this.torrentSession.isDhtRunning()) {
                this.libTorrentHandler.post(new C06345());
            }
        }
    }

    public void pauseSession() {
        if (!this.isStreaming.booleanValue()) {
            this.libTorrentHandler.post(new C06356());
        }
    }

    private com.frostwire.jlibtorrent.TorrentInfo getTorrentInfo(java.lang.String r5) throws com.github.se_bastiaan.torrentstream.exceptions.TorrentInfoException {
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
        r4 = this;
        r0 = "magnet";
        r0 = r5.startsWith(r0);
        if (r0 == 0) goto L_0x001d;
    L_0x0008:
        r0 = r4.torrentSession;
        r1 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r5 = r0.fetchMagnet(r5, r1);
        if (r5 == 0) goto L_0x0095;
    L_0x0012:
        r5 = com.frostwire.jlibtorrent.TorrentInfo.bdecode(r5);	 Catch:{ IllegalArgumentException -> 0x0017 }
        return r5;
    L_0x0017:
        r5 = new com.github.se_bastiaan.torrentstream.exceptions.TorrentInfoException;
        r5.<init>();
        throw r5;
    L_0x001d:
        r0 = "http";
        r0 = r5.startsWith(r0);
        if (r0 != 0) goto L_0x005d;
    L_0x0025:
        r0 = "https";
        r0 = r5.startsWith(r0);
        if (r0 == 0) goto L_0x002e;
    L_0x002d:
        goto L_0x005d;
    L_0x002e:
        r0 = "file";
        r0 = r5.startsWith(r0);
        if (r0 == 0) goto L_0x0095;
    L_0x0036:
        r5 = android.net.Uri.parse(r5);
        r0 = new java.io.File;
        r5 = r5.getPath();
        r0.<init>(r5);
        r5 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0057, IOException -> 0x0057 }
        r5.<init>(r0);	 Catch:{ IOException -> 0x0057, IOException -> 0x0057 }
        r0 = r4.getBytesFromInputStream(r5);	 Catch:{ IOException -> 0x0057, IOException -> 0x0057 }
        r5.close();	 Catch:{ IOException -> 0x0057, IOException -> 0x0057 }
        r5 = r0.length;	 Catch:{ IOException -> 0x0057, IOException -> 0x0057 }
        if (r5 <= 0) goto L_0x0095;	 Catch:{ IOException -> 0x0057, IOException -> 0x0057 }
    L_0x0052:
        r5 = com.frostwire.jlibtorrent.TorrentInfo.bdecode(r0);	 Catch:{ IOException -> 0x0057, IOException -> 0x0057 }
        return r5;
    L_0x0057:
        r5 = new com.github.se_bastiaan.torrentstream.exceptions.TorrentInfoException;
        r5.<init>();
        throw r5;
    L_0x005d:
        r0 = new java.net.URL;	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r0.<init>(r5);	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r5 = r0.openConnection();	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r5 = (java.net.HttpURLConnection) r5;	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r0 = "GET";	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r5.setRequestMethod(r0);	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r0 = 1;	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r5.setInstanceFollowRedirects(r0);	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r5.connect();	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r0 = r5.getInputStream();	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r1 = 0;	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r1 = new byte[r1];	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r2 = r5.getResponseCode();	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        if (r2 != r3) goto L_0x0087;	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
    L_0x0083:
        r1 = r4.getBytesFromInputStream(r0);	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
    L_0x0087:
        r0.close();	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r5.disconnect();	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        r5 = r1.length;	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        if (r5 <= 0) goto L_0x0095;	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
    L_0x0090:
        r5 = com.frostwire.jlibtorrent.TorrentInfo.bdecode(r1);	 Catch:{ IOException -> 0x0097, IOException -> 0x0097 }
        return r5;
    L_0x0095:
        r5 = 0;
        return r5;
    L_0x0097:
        r5 = new com.github.se_bastiaan.torrentstream.exceptions.TorrentInfoException;
        r5.<init>();
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.se_bastiaan.torrentstream.TorrentStream.getTorrentInfo(java.lang.String):com.frostwire.jlibtorrent.TorrentInfo");
    }

    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public void startStream(final String str) {
        if (!(this.initialising.booleanValue() || this.initialised.booleanValue())) {
            initialise();
        }
        if (this.libTorrentHandler != null) {
            if (!this.isStreaming.booleanValue()) {
                this.isCanceled = Boolean.valueOf(false);
                this.streamingThread = new HandlerThread(STREAMING_THREAD_NAME);
                this.streamingThread.start();
                this.streamingHandler = new Handler(this.streamingThread.getLooper());
                this.streamingHandler.post(new Runnable() {
                    public void run() {
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
                        r10 = this;
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r1 = 1;
                        r1 = java.lang.Boolean.valueOf(r1);
                        r0.isStreaming = r1;
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r0 = r0.initialisingLatch;
                        r1 = 0;
                        r2 = 0;
                        if (r0 == 0) goto L_0x002d;
                    L_0x0014:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;	 Catch:{ InterruptedException -> 0x0023 }
                        r0 = r0.initialisingLatch;	 Catch:{ InterruptedException -> 0x0023 }
                        r0.await();	 Catch:{ InterruptedException -> 0x0023 }
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;	 Catch:{ InterruptedException -> 0x0023 }
                        r0.initialisingLatch = r1;	 Catch:{ InterruptedException -> 0x0023 }
                        goto L_0x002d;
                    L_0x0023:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r1 = java.lang.Boolean.valueOf(r2);
                        r0.isStreaming = r1;
                        return;
                    L_0x002d:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r3 = r3;
                        r0.currentTorrentUrl = r3;
                        r6 = new java.io.File;
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r0 = r0.torrentOptions;
                        r0 = r0.saveLocation;
                        r6.<init>(r0);
                        r0 = r6.isDirectory();
                        if (r0 != 0) goto L_0x0076;
                    L_0x0047:
                        r0 = r6.mkdirs();
                        if (r0 != 0) goto L_0x0076;
                    L_0x004d:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r0 = r0.listeners;
                        r0 = r0.iterator();
                    L_0x0057:
                        r1 = r0.hasNext();
                        if (r1 == 0) goto L_0x006c;
                    L_0x005d:
                        r1 = r0.next();
                        r1 = (com.github.se_bastiaan.torrentstream.listeners.TorrentListener) r1;
                        r3 = new com.github.se_bastiaan.torrentstream.TorrentStream$7$1;
                        r3.<init>(r1);
                        com.github.se_bastiaan.torrentstream.utils.ThreadUtils.runOnUiThread(r3);
                        goto L_0x0057;
                    L_0x006c:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r1 = java.lang.Boolean.valueOf(r2);
                        r0.isStreaming = r1;
                        return;
                    L_0x0076:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r0 = r0.torrentSession;
                        r3 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r3 = r3.torrentAddedAlertListener;
                        r0.removeListener(r3);
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;	 Catch:{ TorrentInfoException -> 0x008f }
                        r3 = r3;	 Catch:{ TorrentInfoException -> 0x008f }
                        r0 = r0.getTorrentInfo(r3);	 Catch:{ TorrentInfoException -> 0x008f }
                        r5 = r0;
                        goto L_0x00b0;
                    L_0x008f:
                        r0 = move-exception;
                        r3 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r3 = r3.listeners;
                        r3 = r3.iterator();
                    L_0x009a:
                        r4 = r3.hasNext();
                        if (r4 == 0) goto L_0x00af;
                    L_0x00a0:
                        r4 = r3.next();
                        r4 = (com.github.se_bastiaan.torrentstream.listeners.TorrentListener) r4;
                        r5 = new com.github.se_bastiaan.torrentstream.TorrentStream$7$2;
                        r5.<init>(r4, r0);
                        com.github.se_bastiaan.torrentstream.utils.ThreadUtils.runOnUiThread(r5);
                        goto L_0x009a;
                    L_0x00af:
                        r5 = r1;
                    L_0x00b0:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r0 = r0.torrentSession;
                        r1 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r1 = r1.torrentAddedAlertListener;
                        r0.addListener(r1);
                        if (r5 != 0) goto L_0x00ea;
                    L_0x00c1:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r0 = r0.listeners;
                        r0 = r0.iterator();
                    L_0x00cb:
                        r1 = r0.hasNext();
                        if (r1 == 0) goto L_0x00e0;
                    L_0x00d1:
                        r1 = r0.next();
                        r1 = (com.github.se_bastiaan.torrentstream.listeners.TorrentListener) r1;
                        r3 = new com.github.se_bastiaan.torrentstream.TorrentStream$7$3;
                        r3.<init>(r1);
                        com.github.se_bastiaan.torrentstream.utils.ThreadUtils.runOnUiThread(r3);
                        goto L_0x00cb;
                    L_0x00e0:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r1 = java.lang.Boolean.valueOf(r2);
                        r0.isStreaming = r1;
                        return;
                    L_0x00ea:
                        r0 = r5.numFiles();
                        r8 = new com.frostwire.jlibtorrent.Priority[r0];
                    L_0x00f0:
                        r0 = r8.length;
                        if (r2 >= r0) goto L_0x00fa;
                    L_0x00f3:
                        r0 = com.frostwire.jlibtorrent.Priority.IGNORE;
                        r8[r2] = r0;
                        r2 = r2 + 1;
                        goto L_0x00f0;
                    L_0x00fa:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r0 = r0.currentTorrentUrl;
                        r1 = r3;
                        r0 = r0.equals(r1);
                        if (r0 == 0) goto L_0x0121;
                    L_0x0108:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r0 = r0.isCanceled;
                        r0 = r0.booleanValue();
                        if (r0 == 0) goto L_0x0115;
                    L_0x0114:
                        goto L_0x0121;
                    L_0x0115:
                        r0 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r4 = r0.torrentSession;
                        r7 = 0;
                        r9 = 0;
                        r4.download(r5, r6, r7, r8, r9);
                        return;
                    L_0x0121:
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.github.se_bastiaan.torrentstream.TorrentStream.7.run():void");
                    }
                });
            }
        }
    }

    public void stopStream() {
        if (this.libTorrentHandler != null) {
            this.libTorrentHandler.removeCallbacksAndMessages(null);
        }
        if (this.streamingHandler != null) {
            this.streamingHandler.removeCallbacksAndMessages(null);
        }
        this.isCanceled = Boolean.valueOf(true);
        this.isStreaming = Boolean.valueOf(false);
        if (this.currentTorrent != null) {
            final File saveLocation = this.currentTorrent.getSaveLocation();
            this.currentTorrent.pause();
            this.torrentSession.removeListener(this.currentTorrent);
            this.torrentSession.remove(this.currentTorrent.getTorrentHandle());
            this.currentTorrent = null;
            if (this.torrentOptions.removeFiles.booleanValue()) {
                new Thread(new Runnable() {
                    public void run() {
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
                        r4 = this;
                        r0 = 0;
                    L_0x0001:
                        r1 = r0;
                        r1 = com.github.se_bastiaan.torrentstream.utils.FileUtils.recursiveDelete(r1);
                        if (r1 != 0) goto L_0x0033;
                    L_0x0009:
                        r1 = 5;
                        if (r0 >= r1) goto L_0x0033;
                    L_0x000c:
                        r0 = r0 + 1;
                        r1 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
                        java.lang.Thread.sleep(r1);	 Catch:{ InterruptedException -> 0x0014 }
                        goto L_0x0001;
                    L_0x0014:
                        r1 = com.github.se_bastiaan.torrentstream.TorrentStream.this;
                        r1 = r1.listeners;
                        r1 = r1.iterator();
                    L_0x001e:
                        r2 = r1.hasNext();
                        if (r2 == 0) goto L_0x0001;
                    L_0x0024:
                        r2 = r1.next();
                        r2 = (com.github.se_bastiaan.torrentstream.listeners.TorrentListener) r2;
                        r3 = new com.github.se_bastiaan.torrentstream.TorrentStream$8$1;
                        r3.<init>(r2);
                        com.github.se_bastiaan.torrentstream.utils.ThreadUtils.runOnUiThread(r3);
                        goto L_0x001e;
                    L_0x0033:
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.github.se_bastiaan.torrentstream.TorrentStream.8.run():void");
                    }
                }).start();
            }
        }
        if (this.streamingThread != null) {
            this.streamingThread.interrupt();
        }
        for (final TorrentListener torrentListener : this.listeners) {
            ThreadUtils.runOnUiThread(new Runnable() {
                public void run() {
                    torrentListener.onStreamStopped();
                }
            });
        }
    }

    public TorrentOptions getOptions() {
        return this.torrentOptions;
    }

    public void setOptions(TorrentOptions torrentOptions) {
        this.torrentOptions = torrentOptions;
        SettingsPack activeDhtLimit = new SettingsPack().anonymousMode(this.torrentOptions.anonymousMode.booleanValue()).connectionsLimit(this.torrentOptions.maxConnections.intValue()).downloadRateLimit(this.torrentOptions.maxDownloadSpeed.intValue()).uploadRateLimit(this.torrentOptions.maxUploadSpeed.intValue()).activeDhtLimit(this.torrentOptions.maxDht.intValue());
        if (this.torrentOptions.listeningPort.intValue() != -1) {
            activeDhtLimit.setString(string_types.listen_interfaces.swigValue(), String.format(Locale.ENGLISH, "%s:%d", new Object[]{"0.0.0.0", this.torrentOptions.listeningPort}));
        }
        if (this.torrentOptions.proxyHost != null) {
            activeDhtLimit.setString(string_types.proxy_hostname.swigValue(), this.torrentOptions.proxyHost);
            if (this.torrentOptions.proxyUsername != null) {
                activeDhtLimit.setString(string_types.proxy_username.swigValue(), this.torrentOptions.proxyUsername);
                if (this.torrentOptions.proxyPassword != null) {
                    activeDhtLimit.setString(string_types.proxy_password.swigValue(), this.torrentOptions.proxyPassword);
                }
            }
        }
        if (this.torrentOptions.peerFingerprint != null) {
            activeDhtLimit.setString(string_types.peer_fingerprint.swigValue(), this.torrentOptions.peerFingerprint);
        }
        if (this.torrentSession.isRunning()) {
            this.torrentSession.applySettings(activeDhtLimit);
            return;
        }
        this.torrentSession.start(new SessionParams(activeDhtLimit));
    }

    public boolean isStreaming() {
        return this.isStreaming.booleanValue();
    }

    public String getCurrentTorrentUrl() {
        return this.currentTorrentUrl;
    }

    public Integer getTotalDhtNodes() {
        return this.dhtNodes;
    }

    public Torrent getCurrentTorrent() {
        return this.currentTorrent;
    }

    public void addListener(TorrentListener torrentListener) {
        if (torrentListener != null) {
            this.listeners.add(torrentListener);
        }
    }

    public void removeListener(TorrentListener torrentListener) {
        if (torrentListener != null) {
            this.listeners.remove(torrentListener);
        }
    }
}
