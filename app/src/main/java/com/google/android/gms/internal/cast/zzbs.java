package com.google.android.gms.internal.cast;

import com.google.android.gms.common.api.Status;

final class zzbs implements zzdm {
    private final /* synthetic */ zzbl zztn;
    private final /* synthetic */ zzbr zzto;

    zzbs(zzbr zzbr, zzbl zzbl) {
        this.zzto = zzbr;
        this.zztn = zzbl;
    }

    public final void zza(long r9, int r11, java.lang.Object r12) {
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
        r0 = 0;
        if (r12 != 0) goto L_0x0017;
    L_0x0003:
        r12 = r8.zzto;	 Catch:{ ClassCastException -> 0x003d }
        r7 = new com.google.android.gms.internal.cast.zzbx;	 Catch:{ ClassCastException -> 0x003d }
        r2 = new com.google.android.gms.common.api.Status;	 Catch:{ ClassCastException -> 0x003d }
        r2.<init>(r11, r0, r0);	 Catch:{ ClassCastException -> 0x003d }
        r3 = 0;	 Catch:{ ClassCastException -> 0x003d }
        r6 = 0;	 Catch:{ ClassCastException -> 0x003d }
        r1 = r7;	 Catch:{ ClassCastException -> 0x003d }
        r4 = r9;	 Catch:{ ClassCastException -> 0x003d }
        r1.<init>(r2, r3, r4, r6);	 Catch:{ ClassCastException -> 0x003d }
        r12.setResult(r7);	 Catch:{ ClassCastException -> 0x003d }
        return;	 Catch:{ ClassCastException -> 0x003d }
    L_0x0017:
        r12 = (com.google.android.gms.internal.cast.zzbz) r12;	 Catch:{ ClassCastException -> 0x003d }
        r3 = r12.zzug;	 Catch:{ ClassCastException -> 0x003d }
        if (r11 != 0) goto L_0x0026;	 Catch:{ ClassCastException -> 0x003d }
    L_0x001d:
        if (r3 == 0) goto L_0x0026;	 Catch:{ ClassCastException -> 0x003d }
    L_0x001f:
        r9 = r8.zzto;	 Catch:{ ClassCastException -> 0x003d }
        r9 = r9.zzth;	 Catch:{ ClassCastException -> 0x003d }
        r9.zztg = r3;	 Catch:{ ClassCastException -> 0x003d }
    L_0x0026:
        r9 = r8.zzto;	 Catch:{ ClassCastException -> 0x003d }
        r10 = new com.google.android.gms.internal.cast.zzbx;	 Catch:{ ClassCastException -> 0x003d }
        r2 = new com.google.android.gms.common.api.Status;	 Catch:{ ClassCastException -> 0x003d }
        r1 = r12.zztz;	 Catch:{ ClassCastException -> 0x003d }
        r2.<init>(r11, r1, r0);	 Catch:{ ClassCastException -> 0x003d }
        r4 = r12.zzuh;	 Catch:{ ClassCastException -> 0x003d }
        r6 = r12.zzua;	 Catch:{ ClassCastException -> 0x003d }
        r1 = r10;	 Catch:{ ClassCastException -> 0x003d }
        r1.<init>(r2, r3, r4, r6);	 Catch:{ ClassCastException -> 0x003d }
        r9.setResult(r10);	 Catch:{ ClassCastException -> 0x003d }
        return;
    L_0x003d:
        r9 = r8.zzto;
        r10 = new com.google.android.gms.common.api.Status;
        r11 = 13;
        r10.<init>(r11);
        r10 = com.google.android.gms.internal.cast.zzbr.zzb(r10);
        r9.setResult(r10);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzbs.zza(long, int, java.lang.Object):void");
    }

    public final void zzb(long j) {
        this.zzto.setResult(zzbr.zzb(new Status(2103)));
    }
}
