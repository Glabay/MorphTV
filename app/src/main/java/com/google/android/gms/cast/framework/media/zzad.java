package com.google.android.gms.cast.framework.media;

import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONObject;

final class zzad extends zzc {
    private final /* synthetic */ JSONObject zzfi;
    private final /* synthetic */ RemoteMediaClient zzns;

    zzad(RemoteMediaClient remoteMediaClient, GoogleApiClient googleApiClient, JSONObject jSONObject) {
        this.zzns = remoteMediaClient;
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
        r0 = r10.zzns;	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        r1 = r0.zzeu;	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        r2 = r10.zzgc;	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        r3 = 0;	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        r4 = -1;	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        r6 = 0;	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        r7 = -1;	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        r8 = 0;	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        r9 = r10.zzfi;	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        r1.zza(r2, r3, r4, r6, r7, r8, r9);	 Catch:{ IllegalStateException -> 0x001d, IllegalStateException -> 0x001d }
        goto L_0x002d;
    L_0x001b:
        r0 = move-exception;
        goto L_0x002f;
    L_0x001d:
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x001b }
        r1 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x001b }
        r0.<init>(r1);	 Catch:{ all -> 0x001b }
        r0 = r10.createFailedResult(r0);	 Catch:{ all -> 0x001b }
        r0 = (com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult) r0;	 Catch:{ all -> 0x001b }
        r10.setResult(r0);	 Catch:{ all -> 0x001b }
    L_0x002d:
        monitor-exit(r11);	 Catch:{ all -> 0x001b }
        return;	 Catch:{ all -> 0x001b }
    L_0x002f:
        monitor-exit(r11);	 Catch:{ all -> 0x001b }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.framework.media.zzad.zzb(com.google.android.gms.internal.cast.zzcn):void");
    }
}
