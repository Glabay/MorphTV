package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.create_torrent;
import com.frostwire.jlibtorrent.swig.metadata_received_alert;
import com.frostwire.jlibtorrent.swig.torrent_handle;
import com.frostwire.jlibtorrent.swig.torrent_info;
import java.util.concurrent.locks.ReentrantLock;

public final class MetadataReceivedAlert extends TorrentAlert<metadata_received_alert> {
    private byte[] data;
    private boolean invalid;
    private int size;
    private final ReentrantLock sync = new ReentrantLock();

    MetadataReceivedAlert(metadata_received_alert metadata_received_alert) {
        super(metadata_received_alert);
    }

    public int metadataSize() {
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
        r0 = r4.invalid;
        r1 = -1;
        if (r0 == 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r4.size;
        if (r0 <= 0) goto L_0x000d;
    L_0x000a:
        r0 = r4.size;
        return r0;
    L_0x000d:
        r0 = r4.sync;
        r0.lock();
        r0 = 1;
        r2 = r4.invalid;	 Catch:{ Throwable -> 0x0060 }
        if (r2 == 0) goto L_0x001d;
    L_0x0017:
        r0 = r4.sync;
        r0.unlock();
        return r1;
    L_0x001d:
        r2 = r4.size;	 Catch:{ Throwable -> 0x0060 }
        if (r2 <= 0) goto L_0x0029;	 Catch:{ Throwable -> 0x0060 }
    L_0x0021:
        r1 = r4.size;	 Catch:{ Throwable -> 0x0060 }
        r0 = r4.sync;
        r0.unlock();
        return r1;
    L_0x0029:
        r2 = r4.alert;	 Catch:{ Throwable -> 0x0060 }
        r2 = (com.frostwire.jlibtorrent.swig.metadata_received_alert) r2;	 Catch:{ Throwable -> 0x0060 }
        r2 = r2.getHandle();	 Catch:{ Throwable -> 0x0060 }
        if (r2 == 0) goto L_0x0056;	 Catch:{ Throwable -> 0x0060 }
    L_0x0033:
        r3 = r2.is_valid();	 Catch:{ Throwable -> 0x0060 }
        if (r3 != 0) goto L_0x003a;	 Catch:{ Throwable -> 0x0060 }
    L_0x0039:
        goto L_0x0056;	 Catch:{ Throwable -> 0x0060 }
    L_0x003a:
        r2 = r2.torrent_file_ptr();	 Catch:{ Throwable -> 0x0060 }
        if (r2 == 0) goto L_0x004e;	 Catch:{ Throwable -> 0x0060 }
    L_0x0040:
        r3 = r2.is_valid();	 Catch:{ Throwable -> 0x0060 }
        if (r3 != 0) goto L_0x0047;	 Catch:{ Throwable -> 0x0060 }
    L_0x0046:
        goto L_0x004e;	 Catch:{ Throwable -> 0x0060 }
    L_0x0047:
        r1 = r2.metadata_size();	 Catch:{ Throwable -> 0x0060 }
        r4.size = r1;	 Catch:{ Throwable -> 0x0060 }
        goto L_0x0062;	 Catch:{ Throwable -> 0x0060 }
    L_0x004e:
        r4.invalid = r0;	 Catch:{ Throwable -> 0x0060 }
        r0 = r4.sync;
        r0.unlock();
        return r1;
    L_0x0056:
        r4.invalid = r0;	 Catch:{ Throwable -> 0x0060 }
        r0 = r4.sync;
        r0.unlock();
        return r1;
    L_0x005e:
        r0 = move-exception;
        goto L_0x006a;
    L_0x0060:
        r4.invalid = r0;	 Catch:{ all -> 0x005e }
    L_0x0062:
        r0 = r4.sync;
        r0.unlock();
        r0 = r4.size;
        return r0;
    L_0x006a:
        r1 = r4.sync;
        r1.unlock();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.frostwire.jlibtorrent.alerts.MetadataReceivedAlert.metadataSize():int");
    }

    public byte[] torrentData(boolean r6) {
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
        r5 = this;
        r0 = r5.invalid;
        r1 = 0;
        if (r0 == 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r5.data;
        if (r0 == 0) goto L_0x000d;
    L_0x000a:
        r6 = r5.data;
        return r6;
    L_0x000d:
        r0 = r5.sync;
        r0.lock();
        r0 = 1;
        r2 = r5.invalid;	 Catch:{ Throwable -> 0x0066 }
        if (r2 == 0) goto L_0x001d;
    L_0x0017:
        r6 = r5.sync;
        r6.unlock();
        return r1;
    L_0x001d:
        r2 = r5.data;	 Catch:{ Throwable -> 0x0066 }
        if (r2 == 0) goto L_0x0029;	 Catch:{ Throwable -> 0x0066 }
    L_0x0021:
        r6 = r5.data;	 Catch:{ Throwable -> 0x0066 }
        r0 = r5.sync;
        r0.unlock();
        return r6;
    L_0x0029:
        r2 = r5.alert;	 Catch:{ Throwable -> 0x0066 }
        r2 = (com.frostwire.jlibtorrent.swig.metadata_received_alert) r2;	 Catch:{ Throwable -> 0x0066 }
        r2 = r2.getHandle();	 Catch:{ Throwable -> 0x0066 }
        if (r2 == 0) goto L_0x005c;	 Catch:{ Throwable -> 0x0066 }
    L_0x0033:
        r3 = r2.is_valid();	 Catch:{ Throwable -> 0x0066 }
        if (r3 != 0) goto L_0x003a;	 Catch:{ Throwable -> 0x0066 }
    L_0x0039:
        goto L_0x005c;	 Catch:{ Throwable -> 0x0066 }
    L_0x003a:
        r3 = r2.torrent_file_ptr();	 Catch:{ Throwable -> 0x0066 }
        if (r3 == 0) goto L_0x0054;	 Catch:{ Throwable -> 0x0066 }
    L_0x0040:
        r4 = r3.is_valid();	 Catch:{ Throwable -> 0x0066 }
        if (r4 != 0) goto L_0x0047;	 Catch:{ Throwable -> 0x0066 }
    L_0x0046:
        goto L_0x0054;	 Catch:{ Throwable -> 0x0066 }
    L_0x0047:
        r1 = r3.metadata_size();	 Catch:{ Throwable -> 0x0066 }
        r5.size = r1;	 Catch:{ Throwable -> 0x0066 }
        r6 = createTorrent(r2, r3, r6);	 Catch:{ Throwable -> 0x0066 }
        r5.data = r6;	 Catch:{ Throwable -> 0x0066 }
        goto L_0x0068;	 Catch:{ Throwable -> 0x0066 }
    L_0x0054:
        r5.invalid = r0;	 Catch:{ Throwable -> 0x0066 }
        r6 = r5.sync;
        r6.unlock();
        return r1;
    L_0x005c:
        r5.invalid = r0;	 Catch:{ Throwable -> 0x0066 }
        r6 = r5.sync;
        r6.unlock();
        return r1;
    L_0x0064:
        r6 = move-exception;
        goto L_0x0070;
    L_0x0066:
        r5.invalid = r0;	 Catch:{ all -> 0x0064 }
    L_0x0068:
        r6 = r5.sync;
        r6.unlock();
        r6 = r5.data;
        return r6;
    L_0x0070:
        r0 = r5.sync;
        r0.unlock();
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.frostwire.jlibtorrent.alerts.MetadataReceivedAlert.torrentData(boolean):byte[]");
    }

    public byte[] torrentData() {
        return torrentData(false);
    }

    private static byte[] createTorrent(torrent_handle torrent_handle, torrent_info torrent_info, boolean z) {
        create_torrent create_torrent = new create_torrent(torrent_info);
        if (z) {
            boolean z2;
            torrent_info = torrent_handle.get_url_seeds();
            z = (int) torrent_info.size();
            for (z2 = false; z2 < z; z2++) {
                create_torrent.add_url_seed(torrent_info.get(z2));
            }
            torrent_info = torrent_handle.get_http_seeds();
            z = (int) torrent_info.size();
            for (z2 = false; z2 < z; z2++) {
                create_torrent.add_http_seed(torrent_info.get(z2));
            }
            torrent_handle = torrent_handle.trackers();
            torrent_info = (int) torrent_handle.size();
            for (int i = 0; i < torrent_info; i++) {
                z = torrent_handle.get(i);
                create_torrent.add_tracker(z.getUrl(), z.getTier());
            }
        }
        return Vectors.byte_vector2bytes(create_torrent.generate().bencode());
    }
}
