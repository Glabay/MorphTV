package com.google.android.gms.common.config;

import android.content.Context;

final class zze extends GservicesValue<Float> {
    zze(String str, Float f) {
        super(str, f);
    }

    private static java.lang.Float zza(android.content.Context r2, java.lang.String r3, java.lang.Float r4) {
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
        r0 = "gservices-direboot-cache";
        r1 = 0;
        r2 = r2.getSharedPreferences(r0, r1);
        r0 = 0;
        r2 = r2.getString(r3, r0);
        if (r2 == 0) goto L_0x0017;
    L_0x000e:
        r2 = java.lang.Float.parseFloat(r2);	 Catch:{ NumberFormatException -> 0x0017 }
        r2 = java.lang.Float.valueOf(r2);	 Catch:{ NumberFormatException -> 0x0017 }
        return r2;
    L_0x0017:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.config.zze.zza(android.content.Context, java.lang.String, java.lang.Float):java.lang.Float");
    }

    protected final /* synthetic */ Object retrieve(String str) {
        return GservicesValue.zzmu.zza(this.mKey, (Float) this.mDefaultValue);
    }

    protected final /* synthetic */ Object retrieveFromDirectBootCache(Context context, String str, Object obj) {
        return zza(context, str, (Float) obj);
    }
}
