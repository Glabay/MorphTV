package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.cast.zzcn;
import org.json.JSONObject;

final class zzbi extends zzb {
    private final /* synthetic */ RemoteMediaPlayer zzfa;
    private final /* synthetic */ GoogleApiClient zzfb;
    private final /* synthetic */ JSONObject zzfi;
    private final /* synthetic */ long zzfu;
    private final /* synthetic */ int zzfv;

    zzbi(RemoteMediaPlayer remoteMediaPlayer, GoogleApiClient googleApiClient, GoogleApiClient googleApiClient2, long j, int i, JSONObject jSONObject) {
        this.zzfa = remoteMediaPlayer;
        this.zzfb = googleApiClient2;
        this.zzfu = j;
        this.zzfv = i;
        this.zzfi = jSONObject;
        super(googleApiClient);
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzcn) anyClient);
    }

    protected final void zza(com.google.android.gms.internal.cast.zzcn r9) {
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
        r8 = this;
        r9 = r8.zzfa;
        r9 = r9.lock;
        monitor-enter(r9);
        r0 = r8.zzfa;	 Catch:{ all -> 0x0053 }
        r0 = r0.zzev;	 Catch:{ all -> 0x0053 }
        r1 = r8.zzfb;	 Catch:{ all -> 0x0053 }
        r0.zza(r1);	 Catch:{ all -> 0x0053 }
        r0 = 0;
        r1 = r8.zzfa;	 Catch:{ IllegalStateException -> 0x0030, IllegalStateException -> 0x0030 }
        r2 = r1.zzeu;	 Catch:{ IllegalStateException -> 0x0030, IllegalStateException -> 0x0030 }
        r3 = r8.zzgc;	 Catch:{ IllegalStateException -> 0x0030, IllegalStateException -> 0x0030 }
        r4 = r8.zzfu;	 Catch:{ IllegalStateException -> 0x0030, IllegalStateException -> 0x0030 }
        r6 = r8.zzfv;	 Catch:{ IllegalStateException -> 0x0030, IllegalStateException -> 0x0030 }
        r7 = r8.zzfi;	 Catch:{ IllegalStateException -> 0x0030, IllegalStateException -> 0x0030 }
        r2.zza(r3, r4, r6, r7);	 Catch:{ IllegalStateException -> 0x0030, IllegalStateException -> 0x0030 }
        r1 = r8.zzfa;	 Catch:{ all -> 0x0053 }
        r1 = r1.zzev;	 Catch:{ all -> 0x0053 }
    L_0x002a:
        r1.zza(r0);	 Catch:{ all -> 0x0053 }
        goto L_0x0047;
    L_0x002e:
        r1 = move-exception;
        goto L_0x0049;
    L_0x0030:
        r1 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x002e }
        r2 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x002e }
        r1.<init>(r2);	 Catch:{ all -> 0x002e }
        r1 = r8.createFailedResult(r1);	 Catch:{ all -> 0x002e }
        r1 = (com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult) r1;	 Catch:{ all -> 0x002e }
        r8.setResult(r1);	 Catch:{ all -> 0x002e }
        r1 = r8.zzfa;	 Catch:{ all -> 0x0053 }
        r1 = r1.zzev;	 Catch:{ all -> 0x0053 }
        goto L_0x002a;	 Catch:{ all -> 0x0053 }
    L_0x0047:
        monitor-exit(r9);	 Catch:{ all -> 0x0053 }
        return;	 Catch:{ all -> 0x0053 }
    L_0x0049:
        r2 = r8.zzfa;	 Catch:{ all -> 0x0053 }
        r2 = r2.zzev;	 Catch:{ all -> 0x0053 }
        r2.zza(r0);	 Catch:{ all -> 0x0053 }
        throw r1;	 Catch:{ all -> 0x0053 }
    L_0x0053:
        r0 = move-exception;	 Catch:{ all -> 0x0053 }
        monitor-exit(r9);	 Catch:{ all -> 0x0053 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzbi.zza(com.google.android.gms.internal.cast.zzcn):void");
    }
}
