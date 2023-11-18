package com.google.android.gms.cast.framework.media;

import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONObject;

final class zzw extends zzc {
    private final /* synthetic */ MediaQueueItem[] zzfe;
    private final /* synthetic */ int zzff;
    private final /* synthetic */ int zzfg;
    private final /* synthetic */ long zzfh;
    private final /* synthetic */ JSONObject zzfi;
    private final /* synthetic */ RemoteMediaClient zzns;

    zzw(RemoteMediaClient remoteMediaClient, GoogleApiClient googleApiClient, MediaQueueItem[] mediaQueueItemArr, int i, int i2, long j, JSONObject jSONObject) {
        this.zzns = remoteMediaClient;
        this.zzfe = mediaQueueItemArr;
        this.zzff = i;
        this.zzfg = i2;
        this.zzfh = j;
        this.zzfi = jSONObject;
        super(remoteMediaClient, googleApiClient);
    }

    protected final void zzb(com.google.android.gms.internal.cast.zzcn r10) {
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
        r9 = this;
        r10 = r9.zzns;
        r10 = r10.lock;
        monitor-enter(r10);
        r0 = r9.zzns;	 Catch:{ IllegalStateException -> 0x001f }
        r1 = r0.zzeu;	 Catch:{ IllegalStateException -> 0x001f }
        r2 = r9.zzgc;	 Catch:{ IllegalStateException -> 0x001f }
        r3 = r9.zzfe;	 Catch:{ IllegalStateException -> 0x001f }
        r4 = r9.zzff;	 Catch:{ IllegalStateException -> 0x001f }
        r5 = r9.zzfg;	 Catch:{ IllegalStateException -> 0x001f }
        r6 = r9.zzfh;	 Catch:{ IllegalStateException -> 0x001f }
        r8 = r9.zzfi;	 Catch:{ IllegalStateException -> 0x001f }
        r1.zza(r2, r3, r4, r5, r6, r8);	 Catch:{ IllegalStateException -> 0x001f }
        goto L_0x002f;
    L_0x001d:
        r0 = move-exception;
        goto L_0x0031;
    L_0x001f:
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x001d }
        r1 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x001d }
        r0.<init>(r1);	 Catch:{ all -> 0x001d }
        r0 = r9.createFailedResult(r0);	 Catch:{ all -> 0x001d }
        r0 = (com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult) r0;	 Catch:{ all -> 0x001d }
        r9.setResult(r0);	 Catch:{ all -> 0x001d }
    L_0x002f:
        monitor-exit(r10);	 Catch:{ all -> 0x001d }
        return;	 Catch:{ all -> 0x001d }
    L_0x0031:
        monitor-exit(r10);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.framework.media.zzw.zzb(com.google.android.gms.internal.cast.zzcn):void");
    }
}
