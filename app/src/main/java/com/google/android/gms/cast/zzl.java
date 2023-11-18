package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.cast.Cast.CastApi.zza;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.cast.zzcn;
import com.google.android.gms.internal.cast.zzcy;

final class zzl extends zzcy {
    private final /* synthetic */ String val$sessionId;

    zzl(zza zza, GoogleApiClient googleApiClient, String str) {
        this.val$sessionId = str;
        super(googleApiClient);
    }

    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzcn) anyClient);
    }

    public final void zza(com.google.android.gms.internal.cast.zzcn r4) throws android.os.RemoteException {
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
        r0 = r3.val$sessionId;
        r0 = android.text.TextUtils.isEmpty(r0);
        r1 = 2001; // 0x7d1 float:2.804E-42 double:9.886E-321;
        if (r0 == 0) goto L_0x001a;
    L_0x000a:
        r4 = "IllegalArgument: sessionId cannot be null or empty";
        r0 = new com.google.android.gms.common.api.Status;
        r2 = 0;
        r0.<init>(r1, r4, r2);
        r4 = r3.createFailedResult(r0);
        r3.setResult(r4);
        return;
    L_0x001a:
        r0 = r3.val$sessionId;	 Catch:{ IllegalStateException -> 0x0020 }
        r4.zza(r0, r3);	 Catch:{ IllegalStateException -> 0x0020 }
        return;
    L_0x0020:
        r3.zzk(r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzl.zza(com.google.android.gms.internal.cast.zzcn):void");
    }
}
