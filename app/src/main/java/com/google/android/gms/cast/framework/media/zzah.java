package com.google.android.gms.cast.framework.media;

import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONObject;

final class zzah extends zzc {
    private final /* synthetic */ long zzfh;
    private final /* synthetic */ JSONObject zzfi;
    private final /* synthetic */ int zzfs;
    private final /* synthetic */ RemoteMediaClient zzns;

    zzah(RemoteMediaClient remoteMediaClient, GoogleApiClient googleApiClient, int i, long j, JSONObject jSONObject) {
        this.zzns = remoteMediaClient;
        this.zzfs = i;
        this.zzfh = j;
        this.zzfi = jSONObject;
        super(remoteMediaClient, googleApiClient);
    }

    protected final void zzb(com.google.android.gms.internal.cast.zzcn r11) {
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
        r10 = this;
        r11 = r10.zzns;
        r11 = r11.lock;
        monitor-enter(r11);
        r0 = r10.zzns;	 Catch:{ all -> 0x004a }
        r1 = r10.zzfs;	 Catch:{ all -> 0x004a }
        r0 = r0.zzc(r1);	 Catch:{ all -> 0x004a }
        r1 = -1;	 Catch:{ all -> 0x004a }
        if (r0 != r1) goto L_0x0023;	 Catch:{ all -> 0x004a }
    L_0x0012:
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x004a }
        r1 = 0;	 Catch:{ all -> 0x004a }
        r0.<init>(r1);	 Catch:{ all -> 0x004a }
        r0 = r10.createFailedResult(r0);	 Catch:{ all -> 0x004a }
        r0 = (com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult) r0;	 Catch:{ all -> 0x004a }
        r10.setResult(r0);	 Catch:{ all -> 0x004a }
        monitor-exit(r11);	 Catch:{ all -> 0x004a }
        return;
    L_0x0023:
        r0 = r10.zzns;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r1 = r0.zzeu;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r2 = r10.zzgc;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r3 = r10.zzfs;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r4 = r10.zzfh;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r6 = 0;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r7 = 0;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r8 = 0;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r9 = r10.zzfi;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r1.zza(r2, r3, r4, r6, r7, r8, r9);	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        goto L_0x0048;
    L_0x0038:
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x004a }
        r1 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x004a }
        r0.<init>(r1);	 Catch:{ all -> 0x004a }
        r0 = r10.createFailedResult(r0);	 Catch:{ all -> 0x004a }
        r0 = (com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult) r0;	 Catch:{ all -> 0x004a }
        r10.setResult(r0);	 Catch:{ all -> 0x004a }
    L_0x0048:
        monitor-exit(r11);	 Catch:{ all -> 0x004a }
        return;	 Catch:{ all -> 0x004a }
    L_0x004a:
        r0 = move-exception;	 Catch:{ all -> 0x004a }
        monitor-exit(r11);	 Catch:{ all -> 0x004a }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.framework.media.zzah.zzb(com.google.android.gms.internal.cast.zzcn):void");
    }
}
