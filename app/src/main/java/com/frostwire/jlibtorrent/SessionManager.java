package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.Alerts;
import com.frostwire.jlibtorrent.alerts.DhtGetPeersReplyAlert;
import com.frostwire.jlibtorrent.alerts.DhtImmutableItemAlert;
import com.frostwire.jlibtorrent.alerts.DhtMutableItemAlert;
import com.frostwire.jlibtorrent.alerts.ExternalIpAlert;
import com.frostwire.jlibtorrent.alerts.ListenSucceededAlert;
import com.frostwire.jlibtorrent.alerts.MetadataReceivedAlert;
import com.frostwire.jlibtorrent.alerts.SocketType;
import com.frostwire.jlibtorrent.alerts.TorrentAlert;
import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.alert_category_t;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.create_torrent;
import com.frostwire.jlibtorrent.swig.dht_get_peers_reply_alert;
import com.frostwire.jlibtorrent.swig.dht_immutable_item_alert;
import com.frostwire.jlibtorrent.swig.dht_mutable_item_alert;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.external_ip_alert;
import com.frostwire.jlibtorrent.swig.remove_flags_t;
import com.frostwire.jlibtorrent.swig.session;
import com.frostwire.jlibtorrent.swig.session_params;
import com.frostwire.jlibtorrent.swig.settings_pack;
import com.frostwire.jlibtorrent.swig.settings_pack.int_types;
import com.frostwire.jlibtorrent.swig.settings_pack.string_types;
import com.frostwire.jlibtorrent.swig.sha1_hash;
import com.frostwire.jlibtorrent.swig.tcp_endpoint_vector;
import com.frostwire.jlibtorrent.swig.torrent_alert;
import com.frostwire.jlibtorrent.swig.torrent_handle;
import com.frostwire.jlibtorrent.swig.torrent_handle_vector;
import com.frostwire.jlibtorrent.swig.torrent_info;
import com.frostwire.jlibtorrent.swig.torrent_status;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SessionManager {
    private static final long ALERTS_LOOP_WAIT_MILLIS = 500;
    private static final int[] DHT_GET_PEERS_REPLY_ALERT_TYPES = new int[]{AlertType.DHT_GET_PEERS_REPLY.swig()};
    private static final int[] DHT_IMMUTABLE_ITEM_TYPES = new int[]{AlertType.DHT_IMMUTABLE_ITEM.swig()};
    private static final int[] DHT_MUTABLE_ITEM_TYPES = new int[]{AlertType.DHT_MUTABLE_ITEM.swig()};
    private static final String FETCH_MAGNET_DOWNLOAD_KEY = "fetch_magnet___";
    private static final Logger LOG = Logger.getLogger(SessionManager.class);
    private static final int[] METADATA_ALERT_TYPES = new int[]{AlertType.METADATA_RECEIVED.swig(), AlertType.METADATA_FAILED.swig()};
    private static final long REQUEST_STATS_RESOLUTION_MILLIS = 1000;
    private Thread alertsLoop;
    private String externalAddress;
    private int externalPort;
    private boolean firewalled;
    private Throwable lastAlertError;
    private long lastStatsRequestTime;
    private final Map<String, String> listenEndpoints;
    private final AlertListener[] listeners;
    private final boolean logging;
    private session session;
    private final SessionStats stats;
    private final ReentrantLock sync;
    private final ReentrantLock syncMagnet;

    /* renamed from: com.frostwire.jlibtorrent.SessionManager$5 */
    class C06055 implements Runnable {
        C06055() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r10 = this;
            r0 = new com.frostwire.jlibtorrent.swig.alert_ptr_vector;
            r0.<init>();
        L_0x0005:
            r1 = com.frostwire.jlibtorrent.SessionManager.this;
            r1 = r1.session;
            if (r1 == 0) goto L_0x00f6;
        L_0x000d:
            r1 = com.frostwire.jlibtorrent.SessionManager.this;
            r1 = r1.session;
            r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
            r1 = r1.wait_for_alert_ms(r2);
            r2 = com.frostwire.jlibtorrent.SessionManager.this;
            r2 = r2.session;
            if (r2 != 0) goto L_0x0022;
        L_0x0021:
            return;
        L_0x0022:
            if (r1 == 0) goto L_0x00d3;
        L_0x0024:
            r1 = com.frostwire.jlibtorrent.SessionManager.this;
            r1 = r1.session;
            r1.pop_alerts(r0);
            r1 = r0.size();
            r3 = 0;
            r4 = 0;
        L_0x0033:
            r5 = (long) r4;
            r7 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1));
            if (r7 >= 0) goto L_0x00d0;
        L_0x0038:
            r5 = r0.get(r4);
            r6 = r5.type();
            r7 = 0;
            r8 = com.frostwire.jlibtorrent.SessionManager.C06066.$SwitchMap$com$frostwire$jlibtorrent$alerts$AlertType;
            r9 = com.frostwire.jlibtorrent.alerts.AlertType.fromSwig(r6);
            r9 = r9.ordinal();
            r8 = r8[r9];
            switch(r8) {
                case 1: goto L_0x0088;
                case 2: goto L_0x0082;
                case 3: goto L_0x007b;
                case 4: goto L_0x006e;
                case 5: goto L_0x0061;
                case 6: goto L_0x0051;
                default: goto L_0x0050;
            };
        L_0x0050:
            goto L_0x0098;
        L_0x0051:
            r7 = com.frostwire.jlibtorrent.alerts.Alerts.cast(r5);
            r8 = com.frostwire.jlibtorrent.SessionManager.this;
            r9 = r7;
            r9 = (com.frostwire.jlibtorrent.alerts.AddTorrentAlert) r9;
            r8 = r8.isFetchMagnetDownload(r9);
            if (r8 == 0) goto L_0x0098;
        L_0x0060:
            goto L_0x00cc;
        L_0x0061:
            r7 = com.frostwire.jlibtorrent.alerts.Alerts.cast(r5);
            r8 = com.frostwire.jlibtorrent.SessionManager.this;
            r9 = r7;
            r9 = (com.frostwire.jlibtorrent.alerts.ExternalIpAlert) r9;
            r8.onExternalIpAlert(r9);
            goto L_0x0098;
        L_0x006e:
            r7 = com.frostwire.jlibtorrent.alerts.Alerts.cast(r5);
            r8 = com.frostwire.jlibtorrent.SessionManager.this;
            r9 = r7;
            r9 = (com.frostwire.jlibtorrent.alerts.ListenSucceededAlert) r9;
            r8.onListenSucceeded(r9);
            goto L_0x0098;
        L_0x007b:
            r8 = com.frostwire.jlibtorrent.SessionManager.this;
            r9 = 1;
            r8.firewalled = r9;
            goto L_0x0098;
        L_0x0082:
            r8 = com.frostwire.jlibtorrent.SessionManager.this;
            r8.firewalled = r3;
            goto L_0x0098;
        L_0x0088:
            r7 = com.frostwire.jlibtorrent.alerts.Alerts.cast(r5);
            r8 = com.frostwire.jlibtorrent.SessionManager.this;
            r8 = r8.stats;
            r9 = r7;
            r9 = (com.frostwire.jlibtorrent.alerts.SessionStatsAlert) r9;
            r8.update(r9);
        L_0x0098:
            r8 = com.frostwire.jlibtorrent.SessionManager.this;
            r8 = r8.listeners;
            r8 = r8[r6];
            if (r8 == 0) goto L_0x00ad;
        L_0x00a2:
            if (r7 != 0) goto L_0x00a8;
        L_0x00a4:
            r7 = com.frostwire.jlibtorrent.alerts.Alerts.cast(r5);
        L_0x00a8:
            r8 = com.frostwire.jlibtorrent.SessionManager.this;
            r8.fireAlert(r7, r6);
        L_0x00ad:
            r6 = com.frostwire.jlibtorrent.SessionManager.isSpecialType(r6);
            if (r6 != 0) goto L_0x00cc;
        L_0x00b3:
            r6 = com.frostwire.jlibtorrent.SessionManager.this;
            r6 = r6.listeners;
            r8 = com.frostwire.jlibtorrent.alerts.Alerts.NUM_ALERT_TYPES;
            r6 = r6[r8];
            if (r6 == 0) goto L_0x00cc;
        L_0x00bf:
            if (r7 != 0) goto L_0x00c5;
        L_0x00c1:
            r7 = com.frostwire.jlibtorrent.alerts.Alerts.cast(r5);
        L_0x00c5:
            r5 = com.frostwire.jlibtorrent.SessionManager.this;
            r6 = com.frostwire.jlibtorrent.alerts.Alerts.NUM_ALERT_TYPES;
            r5.fireAlert(r7, r6);
        L_0x00cc:
            r4 = r4 + 1;
            goto L_0x0033;
        L_0x00d0:
            r0.clear();
        L_0x00d3:
            r1 = java.lang.System.currentTimeMillis();
            r3 = com.frostwire.jlibtorrent.SessionManager.this;
            r3 = r3.lastStatsRequestTime;
            r5 = r1 - r3;
            r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1));
            if (r7 < 0) goto L_0x0005;
        L_0x00e5:
            r3 = com.frostwire.jlibtorrent.SessionManager.this;
            r3.lastStatsRequestTime = r1;
            r1 = com.frostwire.jlibtorrent.SessionManager.this;
            r1.postSessionStats();
            r1 = com.frostwire.jlibtorrent.SessionManager.this;
            r1.postTorrentUpdates();
            goto L_0x0005;
        L_0x00f6:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.frostwire.jlibtorrent.SessionManager.5.run():void");
        }
    }

    public static final class MutableItem {
        public final Entry item;
        public final long seq;
        public final byte[] signature;

        private MutableItem(Entry entry, byte[] bArr, long j) {
            this.item = entry;
            this.signature = bArr;
            this.seq = j;
        }
    }

    protected void onAfterStart() {
    }

    protected void onAfterStop() {
    }

    protected void onApplySettings(SettingsPack settingsPack) {
    }

    protected void onBeforeStart() {
    }

    protected void onBeforeStop() {
    }

    public SessionManager(boolean z) {
        this.logging = z;
        this.listeners = new AlertListener[(Alerts.NUM_ALERT_TYPES + 1)];
        this.sync = new ReentrantLock();
        this.syncMagnet = new ReentrantLock();
        this.stats = new SessionStats();
        this.listenEndpoints = new HashMap();
        resetState();
    }

    public SessionManager() {
        this(false);
    }

    public session swig() {
        return this.session;
    }

    public void addListener(AlertListener alertListener) {
        modifyListeners(true, alertListener);
    }

    public void removeListener(AlertListener alertListener) {
        modifyListeners(false, alertListener);
    }

    public void start(SessionParams sessionParams) {
        if (this.session == null) {
            this.sync.lock();
            try {
                if (this.session == null) {
                    onBeforeStart();
                    resetState();
                    sessionParams.settings().setInteger(int_types.alert_mask.swigValue(), alertMask(this.logging).to_int());
                    this.session = new session(sessionParams.swig());
                    alertsLoop();
                    onAfterStart();
                    this.sync.unlock();
                }
            } finally {
                this.sync.unlock();
            }
        }
    }

    public void start() {
        settings_pack settings_pack = new settings_pack();
        settings_pack.set_str(string_types.dht_bootstrap_nodes.swigValue(), dhtBootstrapNodes());
        start(new SessionParams(new session_params(settings_pack)));
    }

    public void stop() {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = r2.session;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = r2.sync;
        r0.lock();
        r0 = r2.session;	 Catch:{ all -> 0x0034 }
        if (r0 != 0) goto L_0x0014;
    L_0x000e:
        r0 = r2.sync;
        r0.unlock();
        return;
    L_0x0014:
        r2.onBeforeStop();	 Catch:{ all -> 0x0034 }
        r0 = r2.session;	 Catch:{ all -> 0x0034 }
        r1 = 0;	 Catch:{ all -> 0x0034 }
        r2.session = r1;	 Catch:{ all -> 0x0034 }
        r1 = r2.alertsLoop;	 Catch:{ all -> 0x0034 }
        if (r1 == 0) goto L_0x0025;
    L_0x0020:
        r1 = r2.alertsLoop;	 Catch:{ Throwable -> 0x0025 }
        r1.join();	 Catch:{ Throwable -> 0x0025 }
    L_0x0025:
        r2.resetState();	 Catch:{ all -> 0x0034 }
        r0.delete();	 Catch:{ all -> 0x0034 }
        r2.onAfterStop();	 Catch:{ all -> 0x0034 }
        r0 = r2.sync;
        r0.unlock();
        return;
    L_0x0034:
        r0 = move-exception;
        r1 = r2.sync;
        r1.unlock();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.frostwire.jlibtorrent.SessionManager.stop():void");
    }

    public void restart() {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = r2.sync;
        r0.lock();
        r2.stop();	 Catch:{ InterruptedException -> 0x0018, all -> 0x0011 }
        r0 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ InterruptedException -> 0x0018, all -> 0x0011 }
        java.lang.Thread.sleep(r0);	 Catch:{ InterruptedException -> 0x0018, all -> 0x0011 }
        r2.start();	 Catch:{ InterruptedException -> 0x0018, all -> 0x0011 }
        goto L_0x0018;
    L_0x0011:
        r0 = move-exception;
        r1 = r2.sync;
        r1.unlock();
        throw r0;
    L_0x0018:
        r0 = r2.sync;
        r0.unlock();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.frostwire.jlibtorrent.SessionManager.restart():void");
    }

    public boolean isRunning() {
        return this.session != null;
    }

    public void pause() {
        if (this.session != null && !this.session.is_paused()) {
            this.session.pause();
        }
    }

    public void resume() {
        if (this.session != null) {
            this.session.resume();
        }
    }

    public boolean isPaused() {
        return this.session != null ? this.session.is_paused() : false;
    }

    public SessionStats stats() {
        return this.stats;
    }

    public long downloadRate() {
        return this.stats.downloadRate();
    }

    public long uploadRate() {
        return this.stats.uploadRate();
    }

    public long totalDownload() {
        return this.stats.totalDownload();
    }

    public long totalUpload() {
        return this.stats.totalUpload();
    }

    public long dhtNodes() {
        return this.stats.dhtNodes();
    }

    public boolean isFirewalled() {
        return this.firewalled;
    }

    public String externalAddress() {
        return this.externalAddress;
    }

    public List<String> listenEndpoints() {
        return new ArrayList(this.listenEndpoints.values());
    }

    public SettingsPack settings() {
        return this.session != null ? new SettingsPack(this.session.get_settings()) : null;
    }

    public void applySettings(SettingsPack settingsPack) {
        if (this.session == null) {
            return;
        }
        if (settingsPack == null) {
            throw new IllegalArgumentException("settings pack can't be null");
        }
        this.session.apply_settings(settingsPack.swig());
        onApplySettings(settingsPack);
    }

    public int downloadRateLimit() {
        if (this.session == null) {
            return 0;
        }
        return settings().downloadRateLimit();
    }

    public void downloadRateLimit(int i) {
        if (this.session != null) {
            applySettings(new SettingsPack().downloadRateLimit(i));
        }
    }

    public int uploadRateLimit() {
        if (this.session == null) {
            return 0;
        }
        return settings().uploadRateLimit();
    }

    public void uploadRateLimit(int i) {
        if (this.session != null) {
            applySettings(new SettingsPack().uploadRateLimit(i));
        }
    }

    public int maxActiveDownloads() {
        if (this.session == null) {
            return 0;
        }
        return settings().activeDownloads();
    }

    public void maxActiveDownloads(int i) {
        if (this.session != null) {
            applySettings(new SettingsPack().activeDownloads(i));
        }
    }

    public int maxActiveSeeds() {
        if (this.session == null) {
            return 0;
        }
        return settings().activeSeeds();
    }

    public void maxActiveSeeds(int i) {
        if (this.session != null) {
            applySettings(new SettingsPack().activeSeeds(i));
        }
    }

    public int maxConnections() {
        if (this.session == null) {
            return 0;
        }
        return settings().connectionsLimit();
    }

    public void maxConnections(int i) {
        if (this.session != null) {
            applySettings(new SettingsPack().connectionsLimit(i));
        }
    }

    public int maxPeers() {
        if (this.session == null) {
            return 0;
        }
        return settings().maxPeerlistSize();
    }

    public void maxPeers(int i) {
        if (this.session != null) {
            applySettings(new SettingsPack().maxPeerlistSize(i));
        }
    }

    public String listenInterfaces() {
        if (this.session == null) {
            return null;
        }
        return settings().listenInterfaces();
    }

    public void listenInterfaces(String str) {
        if (this.session != null) {
            applySettings(new SettingsPack().listenInterfaces(str));
        }
    }

    public void postSessionStats() {
        if (this.session != null) {
            this.session.post_session_stats();
        }
    }

    public void postDhtStats() {
        if (this.session != null) {
            this.session.post_dht_stats();
        }
    }

    public void postTorrentUpdates() {
        if (this.session != null) {
            this.session.post_torrent_updates();
        }
    }

    public boolean isDhtRunning() {
        return this.session != null ? this.session.is_dht_running() : false;
    }

    public void startDht() {
        toggleDht(true);
    }

    public void stopDht() {
        toggleDht(false);
    }

    public TorrentHandle find(Sha1Hash sha1Hash) {
        TorrentHandle torrentHandle = null;
        if (this.session == null) {
            return null;
        }
        sha1Hash = this.session.find_torrent(sha1Hash.swig());
        if (sha1Hash != null && sha1Hash.is_valid()) {
            torrentHandle = new TorrentHandle(sha1Hash);
        }
        return torrentHandle;
    }

    public void download(TorrentInfo torrentInfo, File file, File file2, Priority[] priorityArr, List<TcpEndpoint> list) {
        Throwable th;
        if (this.session != null) {
            if (torrentInfo.isValid()) {
                torrent_handle find_torrent = this.session.find_torrent(torrentInfo.swig().info_hash());
                if (find_torrent == null || !find_torrent.is_valid()) {
                    if (file2 != null) {
                        try {
                            file2 = Files.bytes(file2);
                            error_code error_code = new error_code();
                            file2 = add_torrent_params.read_resume_data(Vectors.bytes2byte_vector(file2), error_code);
                            try {
                                if (error_code.value() != 0) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("Unable to read the resume data: ");
                                    stringBuilder.append(error_code.message());
                                    throw new IllegalArgumentException(stringBuilder.toString());
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                LOG.warn("Unable to set resume data", th);
                                if (file2 == null) {
                                    file2 = add_torrent_params.create_instance();
                                }
                                file2.set_ti(torrentInfo.swig());
                                if (file != null) {
                                    file2.setSave_path(file.getAbsolutePath());
                                }
                                if (priorityArr != null) {
                                    if (torrentInfo.files().numFiles() != priorityArr.length) {
                                        torrentInfo = new byte_vector();
                                        for (Priority swig : priorityArr) {
                                            torrentInfo.push_back((byte) swig.swig());
                                        }
                                        file2.set_file_priorities(torrentInfo);
                                    } else {
                                        throw new IllegalArgumentException("priorities count should be equals to the number of files");
                                    }
                                }
                                torrentInfo = new tcp_endpoint_vector();
                                for (TcpEndpoint swig2 : list) {
                                    torrentInfo.push_back(swig2.swig());
                                }
                                file2.set_peers(torrentInfo);
                                file2.setFlags(file2.getFlags().and_(TorrentFlags.AUTO_MANAGED.inv()));
                                this.session.async_add_torrent(file2);
                                return;
                            }
                        } catch (File file22) {
                            th = file22;
                            file22 = null;
                            LOG.warn("Unable to set resume data", th);
                            if (file22 == null) {
                                file22 = add_torrent_params.create_instance();
                            }
                            file22.set_ti(torrentInfo.swig());
                            if (file != null) {
                                file22.setSave_path(file.getAbsolutePath());
                            }
                            if (priorityArr != null) {
                                if (torrentInfo.files().numFiles() != priorityArr.length) {
                                    throw new IllegalArgumentException("priorities count should be equals to the number of files");
                                }
                                torrentInfo = new byte_vector();
                                while (file < priorityArr.length) {
                                    torrentInfo.push_back((byte) swig.swig());
                                }
                                file22.set_file_priorities(torrentInfo);
                            }
                            torrentInfo = new tcp_endpoint_vector();
                            while (file.hasNext() != null) {
                                torrentInfo.push_back(swig2.swig());
                            }
                            file22.set_peers(torrentInfo);
                            file22.setFlags(file22.getFlags().and_(TorrentFlags.AUTO_MANAGED.inv()));
                            this.session.async_add_torrent(file22);
                            return;
                        }
                    }
                    file22 = null;
                    if (file22 == null) {
                        file22 = add_torrent_params.create_instance();
                    }
                    file22.set_ti(torrentInfo.swig());
                    if (file != null) {
                        file22.setSave_path(file.getAbsolutePath());
                    }
                    if (priorityArr != null) {
                        if (torrentInfo.files().numFiles() != priorityArr.length) {
                            throw new IllegalArgumentException("priorities count should be equals to the number of files");
                        }
                        torrentInfo = new byte_vector();
                        while (file < priorityArr.length) {
                            torrentInfo.push_back((byte) swig.swig());
                        }
                        file22.set_file_priorities(torrentInfo);
                    }
                    if (list != null && list.isEmpty() == null) {
                        torrentInfo = new tcp_endpoint_vector();
                        while (file.hasNext() != null) {
                            torrentInfo.push_back(swig2.swig());
                        }
                        file22.set_peers(torrentInfo);
                    }
                    file22.setFlags(file22.getFlags().and_(TorrentFlags.AUTO_MANAGED.inv()));
                    this.session.async_add_torrent(file22);
                    return;
                }
                if (priorityArr == null) {
                    find_torrent.prioritize_files(Priority.array2int_vector(Priority.array(Priority.NORMAL, torrentInfo.numFiles())));
                } else if (torrentInfo.numFiles() != priorityArr.length) {
                    throw new IllegalArgumentException("priorities count should be equals to the number of files");
                } else {
                    find_torrent.prioritize_files(Priority.array2int_vector(priorityArr));
                }
                return;
            }
            throw new IllegalArgumentException("torrent info not valid");
        }
    }

    public void download(TorrentInfo torrentInfo, File file) {
        download(torrentInfo, file, null, null, null);
    }

    public void remove(TorrentHandle torrentHandle, remove_flags_t remove_flags_t) {
        if (this.session != null && torrentHandle.isValid()) {
            this.session.remove_torrent(torrentHandle.swig(), remove_flags_t);
        }
    }

    public void remove(TorrentHandle torrentHandle) {
        if (this.session != null && torrentHandle.isValid()) {
            this.session.remove_torrent(torrentHandle.swig());
        }
    }

    public byte[] fetchMagnet(String str, int i, boolean z, int i2) {
        Throwable th;
        torrent_handle torrent_handle;
        Throwable th2;
        torrent_handle torrent_handle2;
        String str2 = str;
        if (this.session == null) {
            return null;
        }
        add_torrent_params create_instance_disabled_storage = add_torrent_params.create_instance_disabled_storage();
        error_code error_code = new error_code();
        add_torrent_params.parse_magnet_uri(str2, create_instance_disabled_storage, error_code);
        if (error_code.value() != 0) {
            throw new IllegalArgumentException(error_code.message());
        }
        sha1_hash info_hash = create_instance_disabled_storage.getInfo_hash();
        int i3 = 1;
        byte[][] bArr = new byte[][]{null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final sha1_hash sha1_hash = info_hash;
        final int i4 = i2;
        final byte[][] bArr2 = bArr;
        C06011 c06011 = r1;
        final boolean z2 = z;
        CountDownLatch countDownLatch2 = countDownLatch;
        C06011 c060112 = new AlertListener() {
            public int[] types() {
                return SessionManager.METADATA_ALERT_TYPES;
            }

            public void alert(Alert<?> alert) {
                torrent_handle handle = ((torrent_alert) ((TorrentAlert) alert).swig()).getHandle();
                if (handle != null && handle.is_valid()) {
                    if (!handle.info_hash().op_ne(sha1_hash)) {
                        if (alert.type().equals(AlertType.METADATA_RECEIVED)) {
                            MetadataReceivedAlert metadataReceivedAlert = (MetadataReceivedAlert) alert;
                            int metadataSize = metadataReceivedAlert.metadataSize();
                            if (metadataSize > 0 && metadataSize <= i4) {
                                bArr2[0] = metadataReceivedAlert.torrentData(z2);
                            }
                        }
                        countDownLatch.countDown();
                    }
                }
            }
        };
        addListener(c06011);
        try {
            torrent_handle find_torrent;
            CountDownLatch countDownLatch3;
            r8.syncMagnet.lock();
            try {
                StringBuilder stringBuilder;
                torrent_handle add_torrent;
                find_torrent = r8.session.find_torrent(info_hash);
                if (find_torrent != null) {
                    try {
                        if (find_torrent.is_valid()) {
                            torrent_info torrent_file_ptr = find_torrent.torrent_file_ptr();
                            if (torrent_file_ptr == null || !torrent_file_ptr.is_valid()) {
                                countDownLatch3 = countDownLatch2;
                            } else {
                                entry generate = new create_torrent(torrent_file_ptr).generate();
                                int metadata_size = torrent_file_ptr.metadata_size();
                                if (metadata_size > 0 && metadata_size <= i2) {
                                    bArr[0] = Vectors.byte_vector2bytes(generate.bencode());
                                }
                                countDownLatch3 = countDownLatch2;
                                countDownLatch3.countDown();
                            }
                            i3 = 0;
                            if (i3 != 0) {
                                try {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(FETCH_MAGNET_DOWNLOAD_KEY);
                                    stringBuilder.append(str2);
                                    create_instance_disabled_storage.setName(stringBuilder.toString());
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(FETCH_MAGNET_DOWNLOAD_KEY);
                                    stringBuilder.append(str2);
                                    create_instance_disabled_storage.setSave_path(stringBuilder.toString());
                                    create_instance_disabled_storage.setFlags(create_instance_disabled_storage.getFlags().and_(TorrentFlags.AUTO_MANAGED.inv()).or_(TorrentFlags.UPLOAD_MODE).or_(TorrentFlags.STOP_WHEN_READY));
                                    error_code.clear();
                                    add_torrent = r8.session.add_torrent(create_instance_disabled_storage, error_code);
                                } catch (Throwable th3) {
                                    th = th3;
                                    torrent_handle = find_torrent;
                                    th2 = th;
                                    try {
                                        r8.syncMagnet.unlock();
                                        throw th2;
                                    } catch (Throwable th4) {
                                        th2 = th4;
                                        torrent_handle2 = torrent_handle;
                                        removeListener(c06011);
                                        r8.session.remove_torrent(torrent_handle2);
                                        throw th2;
                                    }
                                }
                                try {
                                    add_torrent.resume();
                                    find_torrent = add_torrent;
                                } catch (Throwable th42) {
                                    th2 = th42;
                                    torrent_handle = add_torrent;
                                    r8.syncMagnet.unlock();
                                    throw th2;
                                }
                            }
                            r8.syncMagnet.unlock();
                            countDownLatch3.await((long) i, TimeUnit.SECONDS);
                            removeListener(c06011);
                            if (!(r8.session == null || i3 == 0 || find_torrent == null || !find_torrent.is_valid())) {
                                r8.session.remove_torrent(find_torrent);
                            }
                            return bArr[0];
                        }
                    } catch (Throwable th5) {
                        th42 = th5;
                        torrent_handle = find_torrent;
                        i3 = 0;
                        th2 = th42;
                        r8.syncMagnet.unlock();
                        throw th2;
                    }
                }
                countDownLatch3 = countDownLatch2;
                if (i3 != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(FETCH_MAGNET_DOWNLOAD_KEY);
                    stringBuilder.append(str2);
                    create_instance_disabled_storage.setName(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(FETCH_MAGNET_DOWNLOAD_KEY);
                    stringBuilder.append(str2);
                    create_instance_disabled_storage.setSave_path(stringBuilder.toString());
                    create_instance_disabled_storage.setFlags(create_instance_disabled_storage.getFlags().and_(TorrentFlags.AUTO_MANAGED.inv()).or_(TorrentFlags.UPLOAD_MODE).or_(TorrentFlags.STOP_WHEN_READY));
                    error_code.clear();
                    add_torrent = r8.session.add_torrent(create_instance_disabled_storage, error_code);
                    add_torrent.resume();
                    find_torrent = add_torrent;
                }
            } catch (Throwable th422) {
                th2 = th422;
                i3 = 0;
                torrent_handle = null;
                r8.syncMagnet.unlock();
                throw th2;
            }
            try {
                r8.syncMagnet.unlock();
                countDownLatch3.await((long) i, TimeUnit.SECONDS);
                removeListener(c06011);
                r8.session.remove_torrent(find_torrent);
            } catch (Throwable th6) {
                th422 = th6;
                torrent_handle2 = find_torrent;
                th2 = th422;
                removeListener(c06011);
                if (!(r8.session == null || r14 == 0 || torrent_handle2 == null || !torrent_handle2.is_valid())) {
                    r8.session.remove_torrent(torrent_handle2);
                }
                throw th2;
            }
        } catch (Throwable th4222) {
            th2 = th4222;
            torrent_handle2 = null;
            i3 = 0;
            removeListener(c06011);
            r8.session.remove_torrent(torrent_handle2);
            throw th2;
        }
        return bArr[0];
    }

    public byte[] fetchMagnet(String str, int i, boolean z) {
        return fetchMagnet(str, i, z, 2097152);
    }

    public byte[] fetchMagnet(String str, int i) {
        return fetchMagnet(str, i, false);
    }

    public Entry dhtGetItem(Sha1Hash sha1Hash, int i) {
        if (this.session == null) {
            return null;
        }
        sha1Hash = sha1Hash.swig();
        final Entry[] entryArr = new Entry[]{null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AlertListener c06022 = new AlertListener() {
            public int[] types() {
                return SessionManager.DHT_IMMUTABLE_ITEM_TYPES;
            }

            public void alert(Alert<?> alert) {
                DhtImmutableItemAlert dhtImmutableItemAlert = (DhtImmutableItemAlert) alert;
                if (sha1Hash.op_eq(((dht_immutable_item_alert) dhtImmutableItemAlert.swig()).getTarget())) {
                    entryArr[0] = new Entry(new entry(((dht_immutable_item_alert) dhtImmutableItemAlert.swig()).getItem()));
                    countDownLatch.countDown();
                }
            }
        };
        addListener(c06022);
        try {
            this.session.dht_get_item(sha1Hash);
            countDownLatch.await((long) i, TimeUnit.SECONDS);
        } catch (Throwable th) {
            removeListener(c06022);
        }
        removeListener(c06022);
        return entryArr[0];
    }

    public Sha1Hash dhtPutItem(Entry entry) {
        return this.session != null ? new SessionHandle(this.session).dhtPutItem(entry) : null;
    }

    public MutableItem dhtGetItem(byte[] bArr, byte[] bArr2, int i) {
        if (this.session == null) {
            return null;
        }
        MutableItem[] mutableItemArr = new MutableItem[]{null};
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final byte[] bArr3 = bArr;
        final byte[] bArr4 = bArr2;
        final MutableItem[] mutableItemArr2 = mutableItemArr;
        final CountDownLatch countDownLatch2 = countDownLatch;
        AlertListener c06033 = new AlertListener() {
            public int[] types() {
                return SessionManager.DHT_MUTABLE_ITEM_TYPES;
            }

            public void alert(Alert<?> alert) {
                DhtMutableItemAlert dhtMutableItemAlert = (DhtMutableItemAlert) alert;
                boolean equals = Arrays.equals(bArr3, dhtMutableItemAlert.key());
                boolean equals2 = Arrays.equals(bArr4, dhtMutableItemAlert.salt());
                if (equals && equals2) {
                    mutableItemArr2[0] = new MutableItem(new Entry(new entry(((dht_mutable_item_alert) dhtMutableItemAlert.swig()).getItem())), dhtMutableItemAlert.signature(), dhtMutableItemAlert.seq());
                    countDownLatch2.countDown();
                }
            }
        };
        addListener(c06033);
        try {
            new SessionHandle(this.session).dhtGetItem(bArr, bArr2);
            countDownLatch.await((long) i, TimeUnit.SECONDS);
        } catch (Throwable th) {
            removeListener(c06033);
        }
        removeListener(c06033);
        return mutableItemArr[0];
    }

    public void dhtPutItem(byte[] bArr, byte[] bArr2, Entry entry, byte[] bArr3) {
        if (this.session != null) {
            new SessionHandle(this.session).dhtPutItem(bArr, bArr2, entry, bArr3);
        }
    }

    public ArrayList<TcpEndpoint> dhtGetPeers(Sha1Hash sha1Hash, int i) {
        final ArrayList<TcpEndpoint> arrayList = new ArrayList();
        if (this.session == null) {
            return arrayList;
        }
        sha1Hash = sha1Hash.swig();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AlertListener c06044 = new AlertListener() {
            public int[] types() {
                return SessionManager.DHT_GET_PEERS_REPLY_ALERT_TYPES;
            }

            public void alert(Alert<?> alert) {
                DhtGetPeersReplyAlert dhtGetPeersReplyAlert = (DhtGetPeersReplyAlert) alert;
                if (sha1Hash.op_eq(((dht_get_peers_reply_alert) dhtGetPeersReplyAlert.swig()).getInfo_hash())) {
                    arrayList.addAll(dhtGetPeersReplyAlert.peers());
                    countDownLatch.countDown();
                }
            }
        };
        addListener(c06044);
        try {
            this.session.dht_get_peers(sha1Hash);
            countDownLatch.await((long) i, TimeUnit.SECONDS);
        } catch (Throwable th) {
            removeListener(c06044);
        }
        removeListener(c06044);
        return arrayList;
    }

    public void dhtAnnounce(Sha1Hash sha1Hash, int i, int i2) {
        if (this.session != null) {
            this.session.dht_announce(sha1Hash.swig(), i, i2);
        }
    }

    public void dhtAnnounce(Sha1Hash sha1Hash) {
        if (this.session != null) {
            this.session.dht_announce(sha1Hash.swig());
        }
    }

    public void moveStorage(File file) {
        if (this.session != null) {
            try {
                torrent_handle_vector torrent_handle_vector = this.session.get_torrents();
                int size = (int) torrent_handle_vector.size();
                file = file.getAbsolutePath();
                for (int i = 0; i < size; i++) {
                    torrent_handle torrent_handle = torrent_handle_vector.get(i);
                    torrent_status status = torrent_handle.status();
                    Object obj = (status.getIs_seeding() || status.getIs_finished()) ? null : 1;
                    if (torrent_handle.is_valid() && obj != null) {
                        torrent_handle.move_storage(file);
                    }
                }
            } catch (File file2) {
                LOG.error("Error changing save path for session", file2);
            }
        }
    }

    public byte[] saveState() {
        return this.session != null ? new SessionHandle(this.session).saveState() : null;
    }

    public void loadState(byte[] bArr) {
        if (this.session != null) {
            new SessionHandle(this.session).loadState(bArr);
        }
    }

    public String magnetPeers() {
        if (this.session == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (this.externalAddress != null && this.externalPort > 0) {
            stringBuilder.append("&x.pe=");
            stringBuilder.append(this.externalAddress);
            stringBuilder.append(":");
            stringBuilder.append(this.externalPort);
        }
        for (String str : this.listenEndpoints.values()) {
            stringBuilder.append("&x.pe=");
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public Throwable lastAlertError() {
        return this.lastAlertError;
    }

    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }

    private void resetState() {
        this.stats.clear();
        this.firewalled = true;
        this.listenEndpoints.clear();
        this.externalAddress = null;
        this.alertsLoop = null;
    }

    private void modifyListeners(boolean z, AlertListener alertListener) {
        if (alertListener != null) {
            int[] types = alertListener.types();
            if (types == null) {
                modifyListeners(z, Alerts.NUM_ALERT_TYPES, alertListener);
            } else {
                for (int modifyListeners : types) {
                    modifyListeners(z, modifyListeners, alertListener);
                }
            }
        }
    }

    private synchronized void modifyListeners(boolean z, int i, AlertListener alertListener) {
        if (z) {
            this.listeners[i] = AlertMulticaster.add(this.listeners[i], alertListener);
        } else {
            this.listeners[i] = AlertMulticaster.remove(this.listeners[i], alertListener);
        }
    }

    private void fireAlert(Alert<?> alert, int i) {
        i = this.listeners[i];
        if (i != 0) {
            try {
                i.alert(alert);
            } catch (Alert<?> alert2) {
                i = LOG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error calling alert listener: ");
                stringBuilder.append(alert2.getMessage());
                i.warn(stringBuilder.toString());
                this.lastAlertError = alert2;
            }
        }
    }

    private void onListenSucceeded(ListenSucceededAlert listenSucceededAlert) {
        try {
            if (listenSucceededAlert.socketType() != SocketType.TCP) {
                Address address = listenSucceededAlert.address();
                if (address.isV4()) {
                    this.externalPort = listenSucceededAlert.port();
                }
                if (!(address.isLoopback() || address.isMulticast())) {
                    if (!address.isUnspecified()) {
                        String address2 = address.toString();
                        listenSucceededAlert = listenSucceededAlert.port();
                        if (!(address2.contains("invalid") || address2.startsWith("127."))) {
                            if (!address2.startsWith("fe80::")) {
                                String stringBuilder;
                                StringBuilder stringBuilder2 = new StringBuilder();
                                if (address.isV6()) {
                                    StringBuilder stringBuilder3 = new StringBuilder();
                                    stringBuilder3.append("[");
                                    stringBuilder3.append(address2);
                                    stringBuilder3.append("]");
                                    stringBuilder = stringBuilder3.toString();
                                } else {
                                    stringBuilder = address2;
                                }
                                stringBuilder2.append(stringBuilder);
                                stringBuilder2.append(":");
                                stringBuilder2.append(listenSucceededAlert);
                                this.listenEndpoints.put(address2, stringBuilder2.toString());
                            }
                        }
                    }
                }
            }
        } catch (ListenSucceededAlert listenSucceededAlert2) {
            LOG.error("Error adding listen endpoint to internal list", listenSucceededAlert2);
        }
    }

    private void toggleDht(boolean z) {
        if (this.session != null) {
            if (isDhtRunning() != z) {
                applySettings(new SettingsPack().enableDht(z));
            }
        }
    }

    private void onExternalIpAlert(ExternalIpAlert externalIpAlert) {
        try {
            if (((external_ip_alert) externalIpAlert.swig()).get_external_address().is_v4()) {
                externalIpAlert = externalIpAlert.externalAddress().toString();
                if (!externalIpAlert.contains("invalid")) {
                    this.externalAddress = externalIpAlert;
                }
            }
        } catch (ExternalIpAlert externalIpAlert2) {
            LOG.error("Error saving reported external ip", externalIpAlert2);
        }
    }

    private boolean isFetchMagnetDownload(AddTorrentAlert addTorrentAlert) {
        addTorrentAlert = addTorrentAlert.torrentName();
        return (addTorrentAlert == null || addTorrentAlert.contains(FETCH_MAGNET_DOWNLOAD_KEY) == null) ? null : true;
    }

    private static alert_category_t alertMask(boolean z) {
        alert_category_t alert_category_t = alert.all_categories;
        return !z ? alert_category_t.and_(alert.session_log_notification.or_(alert.torrent_log_notification).or_(alert.peer_log_notification).or_(alert.dht_log_notification).or_(alert.port_mapping_log_notification).or_(alert.picker_log_notification).inv()) : alert_category_t;
    }

    private static String dhtBootstrapNodes() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dht.libtorrent.org:25401");
        stringBuilder.append(",");
        stringBuilder.append("router.bittorrent.com:6881");
        stringBuilder.append(",");
        stringBuilder.append("dht.transmissionbt.com:6881");
        stringBuilder.append(",");
        stringBuilder.append("outer.silotis.us:6881");
        return stringBuilder.toString();
    }

    private static boolean isSpecialType(int i) {
        if (!(i == AlertType.SESSION_STATS.swig() || i == AlertType.STATE_UPDATE.swig())) {
            if (i != AlertType.SESSION_STATS_HEADER.swig()) {
                return false;
            }
        }
        return true;
    }

    private void alertsLoop() {
        Thread thread = new Thread(new C06055(), "SessionManager-alertsLoop");
        thread.setDaemon(true);
        thread.start();
        this.alertsLoop = thread;
    }
}
