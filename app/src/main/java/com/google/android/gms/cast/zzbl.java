package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.cast.zzcn;

final class zzbl extends zzb {
    private final /* synthetic */ RemoteMediaPlayer zzfa;
    private final /* synthetic */ GoogleApiClient zzfb;

    zzbl(RemoteMediaPlayer remoteMediaPlayer, GoogleApiClient googleApiClient, GoogleApiClient googleApiClient2) {
        this.zzfa = remoteMediaPlayer;
        this.zzfb = googleApiClient2;
        super(googleApiClient);
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzcn) anyClient);
    }

    protected final void zza(com.google.android.gms.internal.cast.zzcn r4) {
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
        r3 = this;
        r4 = r3.zzfa;
        r4 = r4.lock;
        monitor-enter(r4);
        r0 = r3.zzfa;	 Catch:{ all -> 0x004d }
        r0 = r0.zzev;	 Catch:{ all -> 0x004d }
        r1 = r3.zzfb;	 Catch:{ all -> 0x004d }
        r0.zza(r1);	 Catch:{ all -> 0x004d }
        r0 = 0;
        r1 = r3.zzfa;	 Catch:{ IllegalStateException -> 0x002a }
        r1 = r1.zzeu;	 Catch:{ IllegalStateException -> 0x002a }
        r2 = r3.zzgc;	 Catch:{ IllegalStateException -> 0x002a }
        r1.zzb(r2);	 Catch:{ IllegalStateException -> 0x002a }
        r1 = r3.zzfa;	 Catch:{ all -> 0x004d }
        r1 = r1.zzev;	 Catch:{ all -> 0x004d }
    L_0x0024:
        r1.zza(r0);	 Catch:{ all -> 0x004d }
        goto L_0x0041;
    L_0x0028:
        r1 = move-exception;
        goto L_0x0043;
    L_0x002a:
        r1 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x0028 }
        r2 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x0028 }
        r1.<init>(r2);	 Catch:{ all -> 0x0028 }
        r1 = r3.createFailedResult(r1);	 Catch:{ all -> 0x0028 }
        r1 = (com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult) r1;	 Catch:{ all -> 0x0028 }
        r3.setResult(r1);	 Catch:{ all -> 0x0028 }
        r1 = r3.zzfa;	 Catch:{ all -> 0x004d }
        r1 = r1.zzev;	 Catch:{ all -> 0x004d }
        goto L_0x0024;	 Catch:{ all -> 0x004d }
    L_0x0041:
        monitor-exit(r4);	 Catch:{ all -> 0x004d }
        return;	 Catch:{ all -> 0x004d }
    L_0x0043:
        r2 = r3.zzfa;	 Catch:{ all -> 0x004d }
        r2 = r2.zzev;	 Catch:{ all -> 0x004d }
        r2.zza(r0);	 Catch:{ all -> 0x004d }
        throw r1;	 Catch:{ all -> 0x004d }
    L_0x004d:
        r0 = move-exception;	 Catch:{ all -> 0x004d }
        monitor-exit(r4);	 Catch:{ all -> 0x004d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzbl.zza(com.google.android.gms.internal.cast.zzcn):void");
    }
}
