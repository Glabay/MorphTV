package com.google.android.gms.cast.framework.media;

import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONObject;

final class zzat extends zzc {
    private final /* synthetic */ JSONObject zzfi;
    private final /* synthetic */ RemoteMediaClient zzns;
    private final /* synthetic */ double zzoa;

    zzat(RemoteMediaClient remoteMediaClient, GoogleApiClient googleApiClient, double d, JSONObject jSONObject) {
        this.zzns = remoteMediaClient;
        this.zzoa = d;
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
        r0 = r5.zzns;	 Catch:{ IllegalStateException -> 0x0019, IllegalStateException -> 0x0019 }
        r0 = r0.zzeu;	 Catch:{ IllegalStateException -> 0x0019, IllegalStateException -> 0x0019 }
        r1 = r5.zzgc;	 Catch:{ IllegalStateException -> 0x0019, IllegalStateException -> 0x0019 }
        r2 = r5.zzoa;	 Catch:{ IllegalStateException -> 0x0019, IllegalStateException -> 0x0019 }
        r4 = r5.zzfi;	 Catch:{ IllegalStateException -> 0x0019, IllegalStateException -> 0x0019 }
        r0.zzb(r1, r2, r4);	 Catch:{ IllegalStateException -> 0x0019, IllegalStateException -> 0x0019 }
        goto L_0x0029;
    L_0x0017:
        r0 = move-exception;
        goto L_0x002b;
    L_0x0019:
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x0017 }
        r1 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x0017 }
        r0.<init>(r1);	 Catch:{ all -> 0x0017 }
        r0 = r5.createFailedResult(r0);	 Catch:{ all -> 0x0017 }
        r0 = (com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult) r0;	 Catch:{ all -> 0x0017 }
        r5.setResult(r0);	 Catch:{ all -> 0x0017 }
    L_0x0029:
        monitor-exit(r6);	 Catch:{ all -> 0x0017 }
        return;	 Catch:{ all -> 0x0017 }
    L_0x002b:
        monitor-exit(r6);	 Catch:{ all -> 0x0017 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.framework.media.zzat.zzb(com.google.android.gms.internal.cast.zzcn):void");
    }
}
