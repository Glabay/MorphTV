package com.google.android.exoplayer2.drm;

import android.util.Pair;

public final class WidevineUtil {
    public static final String PROPERTY_LICENSE_DURATION_REMAINING = "LicenseDurationRemaining";
    public static final String PROPERTY_PLAYBACK_DURATION_REMAINING = "PlaybackDurationRemaining";

    private WidevineUtil() {
    }

    public static Pair<Long, Long> getLicenseDurationRemainingSec(DrmSession<?> drmSession) {
        drmSession = drmSession.queryKeyStatus();
        if (drmSession == null) {
            return null;
        }
        return new Pair(Long.valueOf(getDurationRemainingSec(drmSession, PROPERTY_LICENSE_DURATION_REMAINING)), Long.valueOf(getDurationRemainingSec(drmSession, PROPERTY_PLAYBACK_DURATION_REMAINING)));
    }

    private static long getDurationRemainingSec(java.util.Map<java.lang.String, java.lang.String> r0, java.lang.String r1) {
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
        if (r0 == 0) goto L_0x000f;
    L_0x0002:
        r0 = r0.get(r1);	 Catch:{ NumberFormatException -> 0x000f }
        r0 = (java.lang.String) r0;	 Catch:{ NumberFormatException -> 0x000f }
        if (r0 == 0) goto L_0x000f;	 Catch:{ NumberFormatException -> 0x000f }
    L_0x000a:
        r0 = java.lang.Long.parseLong(r0);	 Catch:{ NumberFormatException -> 0x000f }
        return r0;
    L_0x000f:
        r0 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.drm.WidevineUtil.getDurationRemainingSec(java.util.Map, java.lang.String):long");
    }
}
