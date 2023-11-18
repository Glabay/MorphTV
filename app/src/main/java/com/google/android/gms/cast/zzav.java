package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.cast.zzcn;
import org.json.JSONObject;

final class zzav extends zzb {
    private final /* synthetic */ RemoteMediaPlayer zzfa;
    private final /* synthetic */ GoogleApiClient zzfb;
    private final /* synthetic */ JSONObject zzfi;
    private final /* synthetic */ MediaQueueItem[] zzfm;

    zzav(RemoteMediaPlayer remoteMediaPlayer, GoogleApiClient googleApiClient, GoogleApiClient googleApiClient2, MediaQueueItem[] mediaQueueItemArr, JSONObject jSONObject) {
        this.zzfa = remoteMediaPlayer;
        this.zzfb = googleApiClient2;
        this.zzfm = mediaQueueItemArr;
        this.zzfi = jSONObject;
        super(googleApiClient);
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzcn) anyClient);
    }

    protected final void zza(com.google.android.gms.internal.cast.zzcn r12) {
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
        r11 = this;
        r12 = r11.zzfa;
        r12 = r12.lock;
        monitor-enter(r12);
        r0 = r11.zzfa;	 Catch:{ all -> 0x0056 }
        r0 = r0.zzev;	 Catch:{ all -> 0x0056 }
        r1 = r11.zzfb;	 Catch:{ all -> 0x0056 }
        r0.zza(r1);	 Catch:{ all -> 0x0056 }
        r0 = 0;
        r1 = r11.zzfa;	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r2 = r1.zzeu;	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r3 = r11.zzgc;	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r4 = 0;	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r5 = -1;	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r7 = r11.zzfm;	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r8 = 0;	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r9 = 0;	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r10 = r11.zzfi;	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r2.zza(r3, r4, r5, r7, r8, r9, r10);	 Catch:{ IllegalStateException -> 0x0033, IllegalStateException -> 0x0033 }
        r1 = r11.zzfa;	 Catch:{ all -> 0x0056 }
        r1 = r1.zzev;	 Catch:{ all -> 0x0056 }
    L_0x002d:
        r1.zza(r0);	 Catch:{ all -> 0x0056 }
        goto L_0x004a;
    L_0x0031:
        r1 = move-exception;
        goto L_0x004c;
    L_0x0033:
        r1 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x0031 }
        r2 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x0031 }
        r1.<init>(r2);	 Catch:{ all -> 0x0031 }
        r1 = r11.createFailedResult(r1);	 Catch:{ all -> 0x0031 }
        r1 = (com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult) r1;	 Catch:{ all -> 0x0031 }
        r11.setResult(r1);	 Catch:{ all -> 0x0031 }
        r1 = r11.zzfa;	 Catch:{ all -> 0x0056 }
        r1 = r1.zzev;	 Catch:{ all -> 0x0056 }
        goto L_0x002d;	 Catch:{ all -> 0x0056 }
    L_0x004a:
        monitor-exit(r12);	 Catch:{ all -> 0x0056 }
        return;	 Catch:{ all -> 0x0056 }
    L_0x004c:
        r2 = r11.zzfa;	 Catch:{ all -> 0x0056 }
        r2 = r2.zzev;	 Catch:{ all -> 0x0056 }
        r2.zza(r0);	 Catch:{ all -> 0x0056 }
        throw r1;	 Catch:{ all -> 0x0056 }
    L_0x0056:
        r0 = move-exception;	 Catch:{ all -> 0x0056 }
        monitor-exit(r12);	 Catch:{ all -> 0x0056 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzav.zza(com.google.android.gms.internal.cast.zzcn):void");
    }
}
