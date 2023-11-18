package com.google.android.gms.internal.cast;

import com.google.android.gms.common.api.Status;

final class zzbv implements zzdm {
    private final /* synthetic */ zzbl zztn;
    private final /* synthetic */ zzbu zztr;

    zzbv(zzbu zzbu, zzbl zzbl) {
        this.zztr = zzbu;
        this.zztn = zzbl;
    }

    public final void zza(long r5, int r7, java.lang.Object r8) {
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
        r5 = 0;
        if (r8 != 0) goto L_0x0019;
    L_0x0003:
        r6 = r4.zztr;	 Catch:{ ClassCastException -> 0x0072 }
        r8 = new com.google.android.gms.internal.cast.zzbw;	 Catch:{ ClassCastException -> 0x0072 }
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ ClassCastException -> 0x0072 }
        r0.<init>(r7, r5, r5);	 Catch:{ ClassCastException -> 0x0072 }
        r5 = r4.zztr;	 Catch:{ ClassCastException -> 0x0072 }
        r5 = r5.zztq;	 Catch:{ ClassCastException -> 0x0072 }
        r8.<init>(r0, r5);	 Catch:{ ClassCastException -> 0x0072 }
        r6.setResult(r8);	 Catch:{ ClassCastException -> 0x0072 }
        return;	 Catch:{ ClassCastException -> 0x0072 }
    L_0x0019:
        r8 = (com.google.android.gms.internal.cast.zzbz) r8;	 Catch:{ ClassCastException -> 0x0072 }
        r6 = r8.zzuj;	 Catch:{ ClassCastException -> 0x0072 }
        if (r6 == 0) goto L_0x005a;	 Catch:{ ClassCastException -> 0x0072 }
    L_0x001f:
        r0 = "1.0.0";	 Catch:{ ClassCastException -> 0x0072 }
        r1 = r6.getVersion();	 Catch:{ ClassCastException -> 0x0072 }
        r0 = com.google.android.gms.internal.cast.zzcu.zza(r0, r1);	 Catch:{ ClassCastException -> 0x0072 }
        if (r0 != 0) goto L_0x005a;	 Catch:{ ClassCastException -> 0x0072 }
    L_0x002b:
        r7 = r4.zztr;	 Catch:{ ClassCastException -> 0x0072 }
        r7 = r7.zzth;	 Catch:{ ClassCastException -> 0x0072 }
        r7.zzsx = null;	 Catch:{ ClassCastException -> 0x0072 }
        r5 = r4.zztr;	 Catch:{ ClassCastException -> 0x0072 }
        r7 = new com.google.android.gms.common.api.Status;	 Catch:{ ClassCastException -> 0x0072 }
        r8 = 2150; // 0x866 float:3.013E-42 double:1.062E-320;	 Catch:{ ClassCastException -> 0x0072 }
        r0 = java.util.Locale.ROOT;	 Catch:{ ClassCastException -> 0x0072 }
        r1 = "Incorrect Game Manager SDK version. Receiver: %s Sender: %s";	 Catch:{ ClassCastException -> 0x0072 }
        r2 = 2;	 Catch:{ ClassCastException -> 0x0072 }
        r2 = new java.lang.Object[r2];	 Catch:{ ClassCastException -> 0x0072 }
        r3 = 0;	 Catch:{ ClassCastException -> 0x0072 }
        r6 = r6.getVersion();	 Catch:{ ClassCastException -> 0x0072 }
        r2[r3] = r6;	 Catch:{ ClassCastException -> 0x0072 }
        r6 = 1;	 Catch:{ ClassCastException -> 0x0072 }
        r3 = "1.0.0";	 Catch:{ ClassCastException -> 0x0072 }
        r2[r6] = r3;	 Catch:{ ClassCastException -> 0x0072 }
        r6 = java.lang.String.format(r0, r1, r2);	 Catch:{ ClassCastException -> 0x0072 }
        r7.<init>(r8, r6);	 Catch:{ ClassCastException -> 0x0072 }
        r6 = com.google.android.gms.internal.cast.zzbu.zzc(r7);	 Catch:{ ClassCastException -> 0x0072 }
        r5.setResult(r6);	 Catch:{ ClassCastException -> 0x0072 }
        return;	 Catch:{ ClassCastException -> 0x0072 }
    L_0x005a:
        r6 = r4.zztr;	 Catch:{ ClassCastException -> 0x0072 }
        r0 = new com.google.android.gms.internal.cast.zzbw;	 Catch:{ ClassCastException -> 0x0072 }
        r1 = new com.google.android.gms.common.api.Status;	 Catch:{ ClassCastException -> 0x0072 }
        r8 = r8.zztz;	 Catch:{ ClassCastException -> 0x0072 }
        r1.<init>(r7, r8, r5);	 Catch:{ ClassCastException -> 0x0072 }
        r5 = r4.zztr;	 Catch:{ ClassCastException -> 0x0072 }
        r5 = r5.zztq;	 Catch:{ ClassCastException -> 0x0072 }
        r0.<init>(r1, r5);	 Catch:{ ClassCastException -> 0x0072 }
        r6.setResult(r0);	 Catch:{ ClassCastException -> 0x0072 }
        return;
    L_0x0072:
        r5 = r4.zztr;
        r6 = new com.google.android.gms.common.api.Status;
        r7 = 13;
        r6.<init>(r7);
        r6 = com.google.android.gms.internal.cast.zzbu.zzc(r6);
        r5.setResult(r6);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzbv.zza(long, int, java.lang.Object):void");
    }

    public final void zzb(long j) {
        this.zztr.setResult(zzbu.zzc(new Status(2103)));
    }
}
