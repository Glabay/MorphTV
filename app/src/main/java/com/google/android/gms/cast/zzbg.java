package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.cast.zzcn;
import org.json.JSONObject;

final class zzbg extends zzb {
    private final /* synthetic */ RemoteMediaPlayer zzfa;
    private final /* synthetic */ GoogleApiClient zzfb;
    private final /* synthetic */ JSONObject zzfi;

    zzbg(RemoteMediaPlayer remoteMediaPlayer, GoogleApiClient googleApiClient, GoogleApiClient googleApiClient2, JSONObject jSONObject) {
        this.zzfa = remoteMediaPlayer;
        this.zzfb = googleApiClient2;
        this.zzfi = jSONObject;
        super(googleApiClient);
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzcn) anyClient);
    }

    protected final void zza(com.google.android.gms.internal.cast.zzcn r5) {
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
        r4 = this;
        r5 = r4.zzfa;
        r5 = r5.lock;
        monitor-enter(r5);
        r0 = r4.zzfa;	 Catch:{ all -> 0x004f }
        r0 = r0.zzev;	 Catch:{ all -> 0x004f }
        r1 = r4.zzfb;	 Catch:{ all -> 0x004f }
        r0.zza(r1);	 Catch:{ all -> 0x004f }
        r0 = 0;
        r1 = r4.zzfa;	 Catch:{ IllegalStateException -> 0x002c, IllegalStateException -> 0x002c }
        r1 = r1.zzeu;	 Catch:{ IllegalStateException -> 0x002c, IllegalStateException -> 0x002c }
        r2 = r4.zzgc;	 Catch:{ IllegalStateException -> 0x002c, IllegalStateException -> 0x002c }
        r3 = r4.zzfi;	 Catch:{ IllegalStateException -> 0x002c, IllegalStateException -> 0x002c }
        r1.zzb(r2, r3);	 Catch:{ IllegalStateException -> 0x002c, IllegalStateException -> 0x002c }
        r1 = r4.zzfa;	 Catch:{ all -> 0x004f }
        r1 = r1.zzev;	 Catch:{ all -> 0x004f }
    L_0x0026:
        r1.zza(r0);	 Catch:{ all -> 0x004f }
        goto L_0x0043;
    L_0x002a:
        r1 = move-exception;
        goto L_0x0045;
    L_0x002c:
        r1 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x002a }
        r2 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x002a }
        r1.<init>(r2);	 Catch:{ all -> 0x002a }
        r1 = r4.createFailedResult(r1);	 Catch:{ all -> 0x002a }
        r1 = (com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult) r1;	 Catch:{ all -> 0x002a }
        r4.setResult(r1);	 Catch:{ all -> 0x002a }
        r1 = r4.zzfa;	 Catch:{ all -> 0x004f }
        r1 = r1.zzev;	 Catch:{ all -> 0x004f }
        goto L_0x0026;	 Catch:{ all -> 0x004f }
    L_0x0043:
        monitor-exit(r5);	 Catch:{ all -> 0x004f }
        return;	 Catch:{ all -> 0x004f }
    L_0x0045:
        r2 = r4.zzfa;	 Catch:{ all -> 0x004f }
        r2 = r2.zzev;	 Catch:{ all -> 0x004f }
        r2.zza(r0);	 Catch:{ all -> 0x004f }
        throw r1;	 Catch:{ all -> 0x004f }
    L_0x004f:
        r0 = move-exception;	 Catch:{ all -> 0x004f }
        monitor-exit(r5);	 Catch:{ all -> 0x004f }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzbg.zza(com.google.android.gms.internal.cast.zzcn):void");
    }
}
