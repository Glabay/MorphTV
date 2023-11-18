package com.github.se_bastiaan.torrentstream.listeners;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;

public abstract class TorrentAddedAlertListener implements AlertListener {

    /* renamed from: com.github.se_bastiaan.torrentstream.listeners.TorrentAddedAlertListener$1 */
    static /* synthetic */ class C06481 {
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
            r1 = com.frostwire.jlibtorrent.alerts.AlertType.ADD_TORRENT;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = 1;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.github.se_bastiaan.torrentstream.listeners.TorrentAddedAlertListener.1.<clinit>():void");
        }
    }

    public abstract void torrentAdded(AddTorrentAlert addTorrentAlert);

    public int[] types() {
        return new int[]{AlertType.ADD_TORRENT.swig()};
    }

    public void alert(Alert<?> alert) {
        if (C06481.$SwitchMap$com$frostwire$jlibtorrent$alerts$AlertType[alert.type().ordinal()] == 1) {
            torrentAdded((AddTorrentAlert) alert);
        }
    }
}
