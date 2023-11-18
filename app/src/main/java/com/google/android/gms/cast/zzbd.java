package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.cast.zzcn;
import org.json.JSONObject;

final class zzbd extends zzb {
    private final /* synthetic */ RemoteMediaPlayer zzfa;
    private final /* synthetic */ GoogleApiClient zzfb;
    private final /* synthetic */ long zzfh;
    private final /* synthetic */ JSONObject zzfi;
    private final /* synthetic */ int zzfs;

    zzbd(RemoteMediaPlayer remoteMediaPlayer, GoogleApiClient googleApiClient, int i, GoogleApiClient googleApiClient2, long j, JSONObject jSONObject) {
        this.zzfa = remoteMediaPlayer;
        this.zzfs = i;
        this.zzfb = googleApiClient2;
        this.zzfh = j;
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
        r0 = r11.zzfa;	 Catch:{ all -> 0x0072 }
        r1 = r11.zzfs;	 Catch:{ all -> 0x0072 }
        r0 = r0.zzc(r1);	 Catch:{ all -> 0x0072 }
        r1 = -1;	 Catch:{ all -> 0x0072 }
        if (r0 != r1) goto L_0x0023;	 Catch:{ all -> 0x0072 }
    L_0x0012:
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x0072 }
        r1 = 0;	 Catch:{ all -> 0x0072 }
        r0.<init>(r1);	 Catch:{ all -> 0x0072 }
        r0 = r11.createFailedResult(r0);	 Catch:{ all -> 0x0072 }
        r0 = (com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult) r0;	 Catch:{ all -> 0x0072 }
        r11.setResult(r0);	 Catch:{ all -> 0x0072 }
        monitor-exit(r12);	 Catch:{ all -> 0x0072 }
        return;	 Catch:{ all -> 0x0072 }
    L_0x0023:
        r0 = r11.zzfa;	 Catch:{ all -> 0x0072 }
        r0 = r0.zzev;	 Catch:{ all -> 0x0072 }
        r1 = r11.zzfb;	 Catch:{ all -> 0x0072 }
        r0.zza(r1);	 Catch:{ all -> 0x0072 }
        r0 = 0;
        r1 = r11.zzfa;	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r2 = r1.zzeu;	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r3 = r11.zzgc;	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r4 = r11.zzfs;	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r5 = r11.zzfh;	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r7 = 0;	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r8 = 0;	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r9 = 0;	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r10 = r11.zzfi;	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r2.zza(r3, r4, r5, r7, r8, r9, r10);	 Catch:{ IllegalStateException -> 0x004f, IllegalStateException -> 0x004f }
        r1 = r11.zzfa;	 Catch:{ all -> 0x0072 }
        r1 = r1.zzev;	 Catch:{ all -> 0x0072 }
    L_0x0049:
        r1.zza(r0);	 Catch:{ all -> 0x0072 }
        goto L_0x0066;
    L_0x004d:
        r1 = move-exception;
        goto L_0x0068;
    L_0x004f:
        r1 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x004d }
        r2 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x004d }
        r1.<init>(r2);	 Catch:{ all -> 0x004d }
        r1 = r11.createFailedResult(r1);	 Catch:{ all -> 0x004d }
        r1 = (com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult) r1;	 Catch:{ all -> 0x004d }
        r11.setResult(r1);	 Catch:{ all -> 0x004d }
        r1 = r11.zzfa;	 Catch:{ all -> 0x0072 }
        r1 = r1.zzev;	 Catch:{ all -> 0x0072 }
        goto L_0x0049;	 Catch:{ all -> 0x0072 }
    L_0x0066:
        monitor-exit(r12);	 Catch:{ all -> 0x0072 }
        return;	 Catch:{ all -> 0x0072 }
    L_0x0068:
        r2 = r11.zzfa;	 Catch:{ all -> 0x0072 }
        r2 = r2.zzev;	 Catch:{ all -> 0x0072 }
        r2.zza(r0);	 Catch:{ all -> 0x0072 }
        throw r1;	 Catch:{ all -> 0x0072 }
    L_0x0072:
        r0 = move-exception;	 Catch:{ all -> 0x0072 }
        monitor-exit(r12);	 Catch:{ all -> 0x0072 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzbd.zza(com.google.android.gms.internal.cast.zzcn):void");
    }
}
