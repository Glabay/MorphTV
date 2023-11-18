package com.google.android.gms.cast.framework.media;

import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONObject;

final class zzag extends zzc {
    private final /* synthetic */ JSONObject zzfi;
    private final /* synthetic */ int zzfs;
    private final /* synthetic */ RemoteMediaClient zzns;

    zzag(RemoteMediaClient remoteMediaClient, GoogleApiClient googleApiClient, int i, JSONObject jSONObject) {
        this.zzns = remoteMediaClient;
        this.zzfs = i;
        this.zzfi = jSONObject;
        super(remoteMediaClient, googleApiClient);
    }

    protected final void zzb(com.google.android.gms.internal.cast.zzcn r6) {
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
        r5 = this;
        r6 = r5.zzns;
        r6 = r6.lock;
        monitor-enter(r6);
        r0 = r5.zzns;	 Catch:{ all -> 0x004a }
        r1 = r5.zzfs;	 Catch:{ all -> 0x004a }
        r0 = r0.zzc(r1);	 Catch:{ all -> 0x004a }
        r1 = -1;	 Catch:{ all -> 0x004a }
        r2 = 0;	 Catch:{ all -> 0x004a }
        if (r0 != r1) goto L_0x0023;	 Catch:{ all -> 0x004a }
    L_0x0013:
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x004a }
        r0.<init>(r2);	 Catch:{ all -> 0x004a }
        r0 = r5.createFailedResult(r0);	 Catch:{ all -> 0x004a }
        r0 = (com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult) r0;	 Catch:{ all -> 0x004a }
        r5.setResult(r0);	 Catch:{ all -> 0x004a }
        monitor-exit(r6);	 Catch:{ all -> 0x004a }
        return;
    L_0x0023:
        r0 = r5.zzns;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r0 = r0.zzeu;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r1 = r5.zzgc;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r3 = 1;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r3 = new int[r3];	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r4 = r5.zzfs;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r3[r2] = r4;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r2 = r5.zzfi;	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        r0.zza(r1, r3, r2);	 Catch:{ IllegalStateException -> 0x0038, IllegalStateException -> 0x0038 }
        goto L_0x0048;
    L_0x0038:
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x004a }
        r1 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x004a }
        r0.<init>(r1);	 Catch:{ all -> 0x004a }
        r0 = r5.createFailedResult(r0);	 Catch:{ all -> 0x004a }
        r0 = (com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult) r0;	 Catch:{ all -> 0x004a }
        r5.setResult(r0);	 Catch:{ all -> 0x004a }
    L_0x0048:
        monitor-exit(r6);	 Catch:{ all -> 0x004a }
        return;	 Catch:{ all -> 0x004a }
    L_0x004a:
        r0 = move-exception;	 Catch:{ all -> 0x004a }
        monitor-exit(r6);	 Catch:{ all -> 0x004a }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.framework.media.zzag.zzb(com.google.android.gms.internal.cast.zzcn):void");
    }
}
