package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.cast.Cast.CastApi.zza;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.cast.zzcn;
import com.google.android.gms.internal.cast.zzcy;

final class zzf extends zzcy {
    private final /* synthetic */ String zzad;
    private final /* synthetic */ String zzae;

    zzf(zza zza, GoogleApiClient googleApiClient, String str, String str2) {
        this.zzad = str;
        this.zzae = str2;
        super(googleApiClient);
    }

    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzcn) anyClient);
    }

    public final void zza(com.google.android.gms.internal.cast.zzcn r3) throws android.os.RemoteException {
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
        r2 = this;
        r0 = r2.zzad;	 Catch:{ IllegalArgumentException -> 0x0008, IllegalArgumentException -> 0x0008 }
        r1 = r2.zzae;	 Catch:{ IllegalArgumentException -> 0x0008, IllegalArgumentException -> 0x0008 }
        r3.zza(r0, r1, r2);	 Catch:{ IllegalArgumentException -> 0x0008, IllegalArgumentException -> 0x0008 }
        return;
    L_0x0008:
        r3 = 2001; // 0x7d1 float:2.804E-42 double:9.886E-321;
        r2.zzk(r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzf.zza(com.google.android.gms.internal.cast.zzcn):void");
    }
}
