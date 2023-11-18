package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.cast.zzcn;
import org.json.JSONObject;

final class zzas extends zzb {
    private final /* synthetic */ RemoteMediaPlayer zzfa;
    private final /* synthetic */ GoogleApiClient zzfb;
    private final /* synthetic */ MediaQueueItem[] zzfe;
    private final /* synthetic */ int zzff;
    private final /* synthetic */ int zzfg;
    private final /* synthetic */ long zzfh;
    private final /* synthetic */ JSONObject zzfi;

    zzas(RemoteMediaPlayer remoteMediaPlayer, GoogleApiClient googleApiClient, GoogleApiClient googleApiClient2, MediaQueueItem[] mediaQueueItemArr, int i, int i2, long j, JSONObject jSONObject) {
        this.zzfa = remoteMediaPlayer;
        this.zzfb = googleApiClient2;
        this.zzfe = mediaQueueItemArr;
        this.zzff = i;
        this.zzfg = i2;
        this.zzfh = j;
        this.zzfi = jSONObject;
        super(googleApiClient);
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzcn) anyClient);
    }

    protected final void zza(com.google.android.gms.internal.cast.zzcn r11) {
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
        r11 = r10.zzfa;
        r11 = r11.lock;
        monitor-enter(r11);
        r0 = r10.zzfa;	 Catch:{ all -> 0x0057 }
        r0 = r0.zzev;	 Catch:{ all -> 0x0057 }
        r1 = r10.zzfb;	 Catch:{ all -> 0x0057 }
        r0.zza(r1);	 Catch:{ all -> 0x0057 }
        r0 = 0;
        r1 = r10.zzfa;	 Catch:{ IllegalStateException -> 0x0034 }
        r2 = r1.zzeu;	 Catch:{ IllegalStateException -> 0x0034 }
        r3 = r10.zzgc;	 Catch:{ IllegalStateException -> 0x0034 }
        r4 = r10.zzfe;	 Catch:{ IllegalStateException -> 0x0034 }
        r5 = r10.zzff;	 Catch:{ IllegalStateException -> 0x0034 }
        r6 = r10.zzfg;	 Catch:{ IllegalStateException -> 0x0034 }
        r7 = r10.zzfh;	 Catch:{ IllegalStateException -> 0x0034 }
        r9 = r10.zzfi;	 Catch:{ IllegalStateException -> 0x0034 }
        r2.zza(r3, r4, r5, r6, r7, r9);	 Catch:{ IllegalStateException -> 0x0034 }
        r1 = r10.zzfa;	 Catch:{ all -> 0x0057 }
        r1 = r1.zzev;	 Catch:{ all -> 0x0057 }
    L_0x002e:
        r1.zza(r0);	 Catch:{ all -> 0x0057 }
        goto L_0x004b;
    L_0x0032:
        r1 = move-exception;
        goto L_0x004d;
    L_0x0034:
        r1 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x0032 }
        r2 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x0032 }
        r1.<init>(r2);	 Catch:{ all -> 0x0032 }
        r1 = r10.createFailedResult(r1);	 Catch:{ all -> 0x0032 }
        r1 = (com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult) r1;	 Catch:{ all -> 0x0032 }
        r10.setResult(r1);	 Catch:{ all -> 0x0032 }
        r1 = r10.zzfa;	 Catch:{ all -> 0x0057 }
        r1 = r1.zzev;	 Catch:{ all -> 0x0057 }
        goto L_0x002e;	 Catch:{ all -> 0x0057 }
    L_0x004b:
        monitor-exit(r11);	 Catch:{ all -> 0x0057 }
        return;	 Catch:{ all -> 0x0057 }
    L_0x004d:
        r2 = r10.zzfa;	 Catch:{ all -> 0x0057 }
        r2 = r2.zzev;	 Catch:{ all -> 0x0057 }
        r2.zza(r0);	 Catch:{ all -> 0x0057 }
        throw r1;	 Catch:{ all -> 0x0057 }
    L_0x0057:
        r0 = move-exception;	 Catch:{ all -> 0x0057 }
        monitor-exit(r11);	 Catch:{ all -> 0x0057 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzas.zza(com.google.android.gms.internal.cast.zzcn):void");
    }
}
