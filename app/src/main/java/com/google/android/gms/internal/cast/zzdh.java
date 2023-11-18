package com.google.android.gms.internal.cast;

import android.os.SystemClock;
import com.google.android.gms.cast.AdBreakClipInfo;
import com.google.android.gms.cast.AdBreakStatus;
import com.google.android.gms.cast.CastStatusCodes;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.media.NotificationOptions;
import com.google.android.gms.cast.zzbp;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@VisibleForTesting
public final class zzdh extends zzcg {
    public static final String NAMESPACE = zzcu.zzp("com.google.cast.media");
    private long zzwp;
    private MediaStatus zzwq;
    private zzdj zzwr;
    private final zzdn zzws = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzwt = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzwu = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzwv = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzww = new zzdn(NotificationOptions.SKIP_STEP_TEN_SECONDS_IN_MS);
    private final zzdn zzwx = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzwy = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzwz = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxa = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxb = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxc = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxd = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxe = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxf = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxg = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxh = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxi = new zzdn(DateUtils.MILLIS_PER_DAY);
    private final zzdn zzxj = new zzdn(DateUtils.MILLIS_PER_DAY);

    public zzdh(String str) {
        super(NAMESPACE, "MediaControlChannel", null);
        zza(this.zzws);
        zza(this.zzwt);
        zza(this.zzwu);
        zza(this.zzwv);
        zza(this.zzww);
        zza(this.zzwx);
        zza(this.zzwy);
        zza(this.zzwz);
        zza(this.zzxa);
        zza(this.zzxb);
        zza(this.zzxc);
        zza(this.zzxd);
        zza(this.zzxe);
        zza(this.zzxf);
        zza(this.zzxg);
        zza(this.zzxi);
        zza(this.zzxi);
        zza(this.zzxj);
        zzcz();
    }

    private final void onMetadataUpdated() {
        if (this.zzwr != null) {
            this.zzwr.onMetadataUpdated();
        }
    }

    private final void onPreloadStatusUpdated() {
        if (this.zzwr != null) {
            this.zzwr.onPreloadStatusUpdated();
        }
    }

    private final void onQueueStatusUpdated() {
        if (this.zzwr != null) {
            this.zzwr.onQueueStatusUpdated();
        }
    }

    private final void onStatusUpdated() {
        if (this.zzwr != null) {
            this.zzwr.onStatusUpdated();
        }
    }

    private final long zza(double d, long j, long j2) {
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.zzwp;
        if (elapsedRealtime < 0) {
            elapsedRealtime = 0;
        }
        if (elapsedRealtime == 0) {
            return j;
        }
        long j3 = j + ((long) (((double) elapsedRealtime) * d));
        return (j2 <= 0 || j3 <= j2) ? j3 < 0 ? 0 : j3 : j2;
    }

    private final long zza(com.google.android.gms.internal.cast.zzdm r6, boolean r7) throws java.lang.IllegalStateException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        if (r7 == 0) goto L_0x0010;
    L_0x000b:
        r7 = r5.zzwz;
        r7.zza(r1, r6);
    L_0x0010:
        r6 = "requestId";	 Catch:{ JSONException -> 0x002b }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x002b }
        r6 = "type";	 Catch:{ JSONException -> 0x002b }
        r7 = "GET_STATUS";	 Catch:{ JSONException -> 0x002b }
        r0.put(r6, r7);	 Catch:{ JSONException -> 0x002b }
        r6 = r5.zzwq;	 Catch:{ JSONException -> 0x002b }
        if (r6 == 0) goto L_0x002b;	 Catch:{ JSONException -> 0x002b }
    L_0x0020:
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x002b }
        r7 = r5.zzwq;	 Catch:{ JSONException -> 0x002b }
        r3 = r7.zzj();	 Catch:{ JSONException -> 0x002b }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x002b }
    L_0x002b:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, boolean):long");
    }

    private static java.lang.String zza(java.lang.String r2, java.util.List<com.google.android.gms.cast.zzbp> r3, long r4) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = "requestId";	 Catch:{ JSONException -> 0x0041 }
        r0.put(r1, r4);	 Catch:{ JSONException -> 0x0041 }
        r4 = "type";	 Catch:{ JSONException -> 0x0041 }
        r5 = "PRECACHE";	 Catch:{ JSONException -> 0x0041 }
        r0.put(r4, r5);	 Catch:{ JSONException -> 0x0041 }
        if (r2 == 0) goto L_0x0018;	 Catch:{ JSONException -> 0x0041 }
    L_0x0013:
        r4 = "precacheData";	 Catch:{ JSONException -> 0x0041 }
        r0.put(r4, r2);	 Catch:{ JSONException -> 0x0041 }
    L_0x0018:
        if (r3 == 0) goto L_0x0041;	 Catch:{ JSONException -> 0x0041 }
    L_0x001a:
        r2 = r3.isEmpty();	 Catch:{ JSONException -> 0x0041 }
        if (r2 != 0) goto L_0x0041;	 Catch:{ JSONException -> 0x0041 }
    L_0x0020:
        r2 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x0041 }
        r2.<init>();	 Catch:{ JSONException -> 0x0041 }
        r4 = 0;	 Catch:{ JSONException -> 0x0041 }
    L_0x0026:
        r5 = r3.size();	 Catch:{ JSONException -> 0x0041 }
        if (r4 >= r5) goto L_0x003c;	 Catch:{ JSONException -> 0x0041 }
    L_0x002c:
        r5 = r3.get(r4);	 Catch:{ JSONException -> 0x0041 }
        r5 = (com.google.android.gms.cast.zzbp) r5;	 Catch:{ JSONException -> 0x0041 }
        r5 = r5.toJson();	 Catch:{ JSONException -> 0x0041 }
        r2.put(r4, r5);	 Catch:{ JSONException -> 0x0041 }
        r4 = r4 + 1;	 Catch:{ JSONException -> 0x0041 }
        goto L_0x0026;	 Catch:{ JSONException -> 0x0041 }
    L_0x003c:
        r3 = "requestItems";	 Catch:{ JSONException -> 0x0041 }
        r0.put(r3, r2);	 Catch:{ JSONException -> 0x0041 }
    L_0x0041:
        r2 = r0.toString();
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(java.lang.String, java.util.List, long):java.lang.String");
    }

    private static int[] zzb(JSONArray jSONArray) throws JSONException {
        if (jSONArray == null) {
            return null;
        }
        int[] iArr = new int[jSONArray.length()];
        for (int i = 0; i < jSONArray.length(); i++) {
            iArr[i] = jSONArray.getInt(i);
        }
        return iArr;
    }

    private final void zzcz() {
        this.zzwp = 0;
        this.zzwq = null;
        for (zzdn zzq : zzcn()) {
            zzq.zzq(CastStatusCodes.CANCELED);
        }
    }

    private final void zzda() {
        zza(null, false);
    }

    private final long zzj() throws zzdk {
        if (this.zzwq != null) {
            return this.zzwq.zzj();
        }
        throw new zzdk();
    }

    public final long getApproximateAdBreakClipPositionMs() {
        if (this.zzwp == 0 || this.zzwq == null) {
            return 0;
        }
        AdBreakStatus adBreakStatus = this.zzwq.getAdBreakStatus();
        if (adBreakStatus == null) {
            return 0;
        }
        AdBreakClipInfo currentAdBreakClip = this.zzwq.getCurrentAdBreakClip();
        if (currentAdBreakClip == null) {
            return 0;
        }
        double d = 0.0d;
        if (this.zzwq.getPlaybackRate() == 0.0d && this.zzwq.getPlayerState() == 2) {
            d = 1.0d;
        }
        return zza(d, adBreakStatus.getCurrentBreakClipTimeInMs(), currentAdBreakClip.getDurationInMs());
    }

    public final long getApproximateStreamPosition() {
        MediaInfo mediaInfo = getMediaInfo();
        if (mediaInfo == null || this.zzwp == 0) {
            return 0;
        }
        double playbackRate = this.zzwq.getPlaybackRate();
        long streamPosition = this.zzwq.getStreamPosition();
        return (playbackRate == 0.0d || this.zzwq.getPlayerState() != 2) ? streamPosition : zza(playbackRate, streamPosition, mediaInfo.getStreamDuration());
    }

    public final MediaInfo getMediaInfo() {
        return this.zzwq == null ? null : this.zzwq.getMediaInfo();
    }

    public final MediaStatus getMediaStatus() {
        return this.zzwq;
    }

    public final long getStreamDuration() {
        MediaInfo mediaInfo = getMediaInfo();
        return mediaInfo != null ? mediaInfo.getStreamDuration() : 0;
    }

    public final long zza(zzdm zzdm) throws IllegalStateException, zzdk {
        JSONObject jSONObject = new JSONObject();
        long zzco = zzco();
        this.zzwx.zza(zzco, zzdm);
        try {
            jSONObject.put("requestId", zzco);
            jSONObject.put("type", "SKIP_AD");
            jSONObject.put("mediaSessionId", zzj());
        } catch (JSONException e) {
            this.zzuv.m28w(String.format(Locale.ROOT, "Error creating SkipAd message: %s", new Object[]{e.getMessage()}), new Object[0]);
        }
        zza(jSONObject.toString(), zzco, null);
        return zzco;
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r6, double r7, org.json.JSONObject r9) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk, java.lang.IllegalArgumentException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = java.lang.Double.isInfinite(r7);
        if (r0 != 0) goto L_0x004f;
    L_0x0006:
        r0 = java.lang.Double.isNaN(r7);
        if (r0 == 0) goto L_0x000d;
    L_0x000c:
        goto L_0x004f;
    L_0x000d:
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzwx;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x0046 }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x0046 }
        r6 = "type";	 Catch:{ JSONException -> 0x0046 }
        r3 = "SET_VOLUME";	 Catch:{ JSONException -> 0x0046 }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x0046 }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x0046 }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x0046 }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x0046 }
        r6 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0046 }
        r6.<init>();	 Catch:{ JSONException -> 0x0046 }
        r3 = "level";	 Catch:{ JSONException -> 0x0046 }
        r6.put(r3, r7);	 Catch:{ JSONException -> 0x0046 }
        r7 = "volume";	 Catch:{ JSONException -> 0x0046 }
        r0.put(r7, r6);	 Catch:{ JSONException -> 0x0046 }
        if (r9 == 0) goto L_0x0046;	 Catch:{ JSONException -> 0x0046 }
    L_0x0041:
        r6 = "customData";	 Catch:{ JSONException -> 0x0046 }
        r0.put(r6, r9);	 Catch:{ JSONException -> 0x0046 }
    L_0x0046:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
    L_0x004f:
        r6 = new java.lang.IllegalArgumentException;
        r9 = 41;
        r0 = new java.lang.StringBuilder;
        r0.<init>(r9);
        r9 = "Volume cannot be ";
        r0.append(r9);
        r0.append(r7);
        r7 = r0.toString();
        r6.<init>(r7);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, double, org.json.JSONObject):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r6, int r7, int r8, int r9) throws com.google.android.gms.internal.cast.zzdk, java.lang.IllegalArgumentException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        if (r8 <= 0) goto L_0x0004;
    L_0x0002:
        if (r9 == 0) goto L_0x0009;
    L_0x0004:
        if (r8 != 0) goto L_0x0048;
    L_0x0006:
        if (r9 > 0) goto L_0x0009;
    L_0x0008:
        goto L_0x0048;
    L_0x0009:
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzxi;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x003f }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x003f }
        r6 = "type";	 Catch:{ JSONException -> 0x003f }
        r3 = "QUEUE_GET_ITEM_RANGE";	 Catch:{ JSONException -> 0x003f }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x003f }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x003f }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x003f }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x003f }
        r6 = "itemId";	 Catch:{ JSONException -> 0x003f }
        r0.put(r6, r7);	 Catch:{ JSONException -> 0x003f }
        if (r8 <= 0) goto L_0x0038;	 Catch:{ JSONException -> 0x003f }
    L_0x0033:
        r6 = "nextCount";	 Catch:{ JSONException -> 0x003f }
        r0.put(r6, r8);	 Catch:{ JSONException -> 0x003f }
    L_0x0038:
        if (r9 <= 0) goto L_0x003f;	 Catch:{ JSONException -> 0x003f }
    L_0x003a:
        r6 = "prevCount";	 Catch:{ JSONException -> 0x003f }
        r0.put(r6, r9);	 Catch:{ JSONException -> 0x003f }
    L_0x003f:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
    L_0x0048:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "Exactly one of nextCount and prevCount must be positive and the other must be zero";
        r6.<init>(r7);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, int, int, int):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r8, int r9, long r10, com.google.android.gms.cast.MediaQueueItem[] r12, int r13, java.lang.Integer r14, org.json.JSONObject r15) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r7 = this;
        r0 = -1;
        r2 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1));
        if (r2 == 0) goto L_0x0025;
    L_0x0006:
        r2 = 0;
        r4 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r4 >= 0) goto L_0x0025;
    L_0x000c:
        r8 = new java.lang.IllegalArgumentException;
        r9 = 53;
        r12 = new java.lang.StringBuilder;
        r12.<init>(r9);
        r9 = "playPosition cannot be negative: ";
        r12.append(r9);
        r12.append(r10);
        r9 = r12.toString();
        r8.<init>(r9);
        throw r8;
    L_0x0025:
        r2 = new org.json.JSONObject;
        r2.<init>();
        r3 = r7.zzco();
        r5 = r7.zzxd;
        r5.zza(r3, r8);
        r8 = "requestId";	 Catch:{ JSONException -> 0x00ad }
        r2.put(r8, r3);	 Catch:{ JSONException -> 0x00ad }
        r8 = "type";	 Catch:{ JSONException -> 0x00ad }
        r5 = "QUEUE_UPDATE";	 Catch:{ JSONException -> 0x00ad }
        r2.put(r8, r5);	 Catch:{ JSONException -> 0x00ad }
        r8 = "mediaSessionId";	 Catch:{ JSONException -> 0x00ad }
        r5 = r7.zzj();	 Catch:{ JSONException -> 0x00ad }
        r2.put(r8, r5);	 Catch:{ JSONException -> 0x00ad }
        if (r9 == 0) goto L_0x004f;	 Catch:{ JSONException -> 0x00ad }
    L_0x004a:
        r8 = "currentItemId";	 Catch:{ JSONException -> 0x00ad }
        r2.put(r8, r9);	 Catch:{ JSONException -> 0x00ad }
    L_0x004f:
        if (r13 == 0) goto L_0x0056;	 Catch:{ JSONException -> 0x00ad }
    L_0x0051:
        r8 = "jump";	 Catch:{ JSONException -> 0x00ad }
        r2.put(r8, r13);	 Catch:{ JSONException -> 0x00ad }
    L_0x0056:
        if (r12 == 0) goto L_0x0075;	 Catch:{ JSONException -> 0x00ad }
    L_0x0058:
        r8 = r12.length;	 Catch:{ JSONException -> 0x00ad }
        if (r8 <= 0) goto L_0x0075;	 Catch:{ JSONException -> 0x00ad }
    L_0x005b:
        r8 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x00ad }
        r8.<init>();	 Catch:{ JSONException -> 0x00ad }
        r9 = 0;	 Catch:{ JSONException -> 0x00ad }
    L_0x0061:
        r13 = r12.length;	 Catch:{ JSONException -> 0x00ad }
        if (r9 >= r13) goto L_0x0070;	 Catch:{ JSONException -> 0x00ad }
    L_0x0064:
        r13 = r12[r9];	 Catch:{ JSONException -> 0x00ad }
        r13 = r13.toJson();	 Catch:{ JSONException -> 0x00ad }
        r8.put(r9, r13);	 Catch:{ JSONException -> 0x00ad }
        r9 = r9 + 1;	 Catch:{ JSONException -> 0x00ad }
        goto L_0x0061;	 Catch:{ JSONException -> 0x00ad }
    L_0x0070:
        r9 = "items";	 Catch:{ JSONException -> 0x00ad }
        r2.put(r9, r8);	 Catch:{ JSONException -> 0x00ad }
    L_0x0075:
        if (r14 == 0) goto L_0x0096;	 Catch:{ JSONException -> 0x00ad }
    L_0x0077:
        r8 = r14.intValue();	 Catch:{ JSONException -> 0x00ad }
        switch(r8) {
            case 0: goto L_0x0091;
            case 1: goto L_0x008c;
            case 2: goto L_0x0087;
            case 3: goto L_0x007f;
            default: goto L_0x007e;
        };	 Catch:{ JSONException -> 0x00ad }
    L_0x007e:
        goto L_0x0096;	 Catch:{ JSONException -> 0x00ad }
    L_0x007f:
        r8 = "repeatMode";	 Catch:{ JSONException -> 0x00ad }
        r9 = "REPEAT_ALL_AND_SHUFFLE";	 Catch:{ JSONException -> 0x00ad }
    L_0x0083:
        r2.put(r8, r9);	 Catch:{ JSONException -> 0x00ad }
        goto L_0x0096;	 Catch:{ JSONException -> 0x00ad }
    L_0x0087:
        r8 = "repeatMode";	 Catch:{ JSONException -> 0x00ad }
        r9 = "REPEAT_SINGLE";	 Catch:{ JSONException -> 0x00ad }
        goto L_0x0083;	 Catch:{ JSONException -> 0x00ad }
    L_0x008c:
        r8 = "repeatMode";	 Catch:{ JSONException -> 0x00ad }
        r9 = "REPEAT_ALL";	 Catch:{ JSONException -> 0x00ad }
        goto L_0x0083;	 Catch:{ JSONException -> 0x00ad }
    L_0x0091:
        r8 = "repeatMode";	 Catch:{ JSONException -> 0x00ad }
        r9 = "REPEAT_OFF";	 Catch:{ JSONException -> 0x00ad }
        goto L_0x0083;	 Catch:{ JSONException -> 0x00ad }
    L_0x0096:
        r8 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1));	 Catch:{ JSONException -> 0x00ad }
        if (r8 == 0) goto L_0x00a6;	 Catch:{ JSONException -> 0x00ad }
    L_0x009a:
        r8 = "currentTime";	 Catch:{ JSONException -> 0x00ad }
        r9 = (double) r10;	 Catch:{ JSONException -> 0x00ad }
        r11 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;	 Catch:{ JSONException -> 0x00ad }
        r9 = r9 / r11;	 Catch:{ JSONException -> 0x00ad }
        r2.put(r8, r9);	 Catch:{ JSONException -> 0x00ad }
    L_0x00a6:
        if (r15 == 0) goto L_0x00ad;	 Catch:{ JSONException -> 0x00ad }
    L_0x00a8:
        r8 = "customData";	 Catch:{ JSONException -> 0x00ad }
        r2.put(r8, r15);	 Catch:{ JSONException -> 0x00ad }
    L_0x00ad:
        r8 = r2.toString();
        r9 = 0;
        r7.zza(r8, r3, r9);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, int, long, com.google.android.gms.cast.MediaQueueItem[], int, java.lang.Integer, org.json.JSONObject):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r6, long r7, int r9, org.json.JSONObject r10) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzww;
        r4 = new com.google.android.gms.internal.cast.zzdi;
        r4.<init>(r5, r6);
        r3.zza(r1, r4);
        r6 = "requestId";	 Catch:{ JSONException -> 0x004e }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x004e }
        r6 = "type";	 Catch:{ JSONException -> 0x004e }
        r3 = "SEEK";	 Catch:{ JSONException -> 0x004e }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x004e }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x004e }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x004e }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x004e }
        r6 = "currentTime";	 Catch:{ JSONException -> 0x004e }
        r7 = (double) r7;	 Catch:{ JSONException -> 0x004e }
        r3 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;	 Catch:{ JSONException -> 0x004e }
        r7 = r7 / r3;	 Catch:{ JSONException -> 0x004e }
        r0.put(r6, r7);	 Catch:{ JSONException -> 0x004e }
        r6 = 1;	 Catch:{ JSONException -> 0x004e }
        if (r9 != r6) goto L_0x003f;	 Catch:{ JSONException -> 0x004e }
    L_0x0037:
        r6 = "resumeState";	 Catch:{ JSONException -> 0x004e }
        r7 = "PLAYBACK_START";	 Catch:{ JSONException -> 0x004e }
    L_0x003b:
        r0.put(r6, r7);	 Catch:{ JSONException -> 0x004e }
        goto L_0x0047;	 Catch:{ JSONException -> 0x004e }
    L_0x003f:
        r6 = 2;	 Catch:{ JSONException -> 0x004e }
        if (r9 != r6) goto L_0x0047;	 Catch:{ JSONException -> 0x004e }
    L_0x0042:
        r6 = "resumeState";	 Catch:{ JSONException -> 0x004e }
        r7 = "PLAYBACK_PAUSE";	 Catch:{ JSONException -> 0x004e }
        goto L_0x003b;	 Catch:{ JSONException -> 0x004e }
    L_0x0047:
        if (r10 == 0) goto L_0x004e;	 Catch:{ JSONException -> 0x004e }
    L_0x0049:
        r6 = "customData";	 Catch:{ JSONException -> 0x004e }
        r0.put(r6, r10);	 Catch:{ JSONException -> 0x004e }
    L_0x004e:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, long, int, org.json.JSONObject):long");
    }

    public final long zza(@android.support.annotation.NonNull com.google.android.gms.internal.cast.zzdm r8, @android.support.annotation.NonNull com.google.android.gms.cast.MediaInfo r9, @android.support.annotation.NonNull com.google.android.gms.cast.MediaLoadOptions r10) throws java.lang.IllegalStateException, java.lang.IllegalArgumentException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r7 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r7.zzco();
        r3 = r7.zzws;
        r3.zza(r1, r8);
        r8 = "requestId";	 Catch:{ JSONException -> 0x008a }
        r0.put(r8, r1);	 Catch:{ JSONException -> 0x008a }
        r8 = "type";	 Catch:{ JSONException -> 0x008a }
        r3 = "LOAD";	 Catch:{ JSONException -> 0x008a }
        r0.put(r8, r3);	 Catch:{ JSONException -> 0x008a }
        r8 = "media";	 Catch:{ JSONException -> 0x008a }
        r9 = r9.toJson();	 Catch:{ JSONException -> 0x008a }
        r0.put(r8, r9);	 Catch:{ JSONException -> 0x008a }
        r8 = "autoplay";	 Catch:{ JSONException -> 0x008a }
        r9 = r10.getAutoplay();	 Catch:{ JSONException -> 0x008a }
        r0.put(r8, r9);	 Catch:{ JSONException -> 0x008a }
        r8 = "currentTime";	 Catch:{ JSONException -> 0x008a }
        r3 = r10.getPlayPosition();	 Catch:{ JSONException -> 0x008a }
        r3 = (double) r3;	 Catch:{ JSONException -> 0x008a }
        r5 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;	 Catch:{ JSONException -> 0x008a }
        r3 = r3 / r5;	 Catch:{ JSONException -> 0x008a }
        r0.put(r8, r3);	 Catch:{ JSONException -> 0x008a }
        r8 = "playbackRate";	 Catch:{ JSONException -> 0x008a }
        r3 = r10.getPlaybackRate();	 Catch:{ JSONException -> 0x008a }
        r0.put(r8, r3);	 Catch:{ JSONException -> 0x008a }
        r8 = r10.getCredentials();	 Catch:{ JSONException -> 0x008a }
        if (r8 == 0) goto L_0x0054;	 Catch:{ JSONException -> 0x008a }
    L_0x004b:
        r8 = "credentials";	 Catch:{ JSONException -> 0x008a }
        r9 = r10.getCredentials();	 Catch:{ JSONException -> 0x008a }
        r0.put(r8, r9);	 Catch:{ JSONException -> 0x008a }
    L_0x0054:
        r8 = r10.getCredentialsType();	 Catch:{ JSONException -> 0x008a }
        if (r8 == 0) goto L_0x0063;	 Catch:{ JSONException -> 0x008a }
    L_0x005a:
        r8 = "credentialsType";	 Catch:{ JSONException -> 0x008a }
        r9 = r10.getCredentialsType();	 Catch:{ JSONException -> 0x008a }
        r0.put(r8, r9);	 Catch:{ JSONException -> 0x008a }
    L_0x0063:
        r8 = r10.getActiveTrackIds();	 Catch:{ JSONException -> 0x008a }
        if (r8 == 0) goto L_0x007f;	 Catch:{ JSONException -> 0x008a }
    L_0x0069:
        r9 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x008a }
        r9.<init>();	 Catch:{ JSONException -> 0x008a }
        r3 = 0;	 Catch:{ JSONException -> 0x008a }
    L_0x006f:
        r4 = r8.length;	 Catch:{ JSONException -> 0x008a }
        if (r3 >= r4) goto L_0x007a;	 Catch:{ JSONException -> 0x008a }
    L_0x0072:
        r4 = r8[r3];	 Catch:{ JSONException -> 0x008a }
        r9.put(r3, r4);	 Catch:{ JSONException -> 0x008a }
        r3 = r3 + 1;	 Catch:{ JSONException -> 0x008a }
        goto L_0x006f;	 Catch:{ JSONException -> 0x008a }
    L_0x007a:
        r8 = "activeTrackIds";	 Catch:{ JSONException -> 0x008a }
        r0.put(r8, r9);	 Catch:{ JSONException -> 0x008a }
    L_0x007f:
        r8 = r10.getCustomData();	 Catch:{ JSONException -> 0x008a }
        if (r8 == 0) goto L_0x008a;	 Catch:{ JSONException -> 0x008a }
    L_0x0085:
        r9 = "customData";	 Catch:{ JSONException -> 0x008a }
        r0.put(r9, r8);	 Catch:{ JSONException -> 0x008a }
    L_0x008a:
        r8 = r0.toString();
        r9 = 0;
        r7.zza(r8, r1, r9);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, com.google.android.gms.cast.MediaInfo, com.google.android.gms.cast.MediaLoadOptions):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r6, com.google.android.gms.cast.TextTrackStyle r7) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzxb;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x002e }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x002e }
        r6 = "type";	 Catch:{ JSONException -> 0x002e }
        r3 = "EDIT_TRACKS_INFO";	 Catch:{ JSONException -> 0x002e }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x002e }
        if (r7 == 0) goto L_0x0025;	 Catch:{ JSONException -> 0x002e }
    L_0x001c:
        r6 = "textTrackStyle";	 Catch:{ JSONException -> 0x002e }
        r7 = r7.toJson();	 Catch:{ JSONException -> 0x002e }
        r0.put(r6, r7);	 Catch:{ JSONException -> 0x002e }
    L_0x0025:
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x002e }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x002e }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x002e }
    L_0x002e:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, com.google.android.gms.cast.TextTrackStyle):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r6, org.json.JSONObject r7) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzwt;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x002a }
        r6 = "type";	 Catch:{ JSONException -> 0x002a }
        r3 = "PAUSE";	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x002a }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x002a }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x002a }
        if (r7 == 0) goto L_0x002a;	 Catch:{ JSONException -> 0x002a }
    L_0x0025:
        r6 = "customData";	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r7);	 Catch:{ JSONException -> 0x002a }
    L_0x002a:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, org.json.JSONObject):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r6, boolean r7, org.json.JSONObject r8) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzwy;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x0039 }
        r6 = "type";	 Catch:{ JSONException -> 0x0039 }
        r3 = "SET_VOLUME";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x0039 }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x0039 }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x0039 }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x0039 }
        r6 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0039 }
        r6.<init>();	 Catch:{ JSONException -> 0x0039 }
        r3 = "muted";	 Catch:{ JSONException -> 0x0039 }
        r6.put(r3, r7);	 Catch:{ JSONException -> 0x0039 }
        r7 = "volume";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r7, r6);	 Catch:{ JSONException -> 0x0039 }
        if (r8 == 0) goto L_0x0039;	 Catch:{ JSONException -> 0x0039 }
    L_0x0034:
        r6 = "customData";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r6, r8);	 Catch:{ JSONException -> 0x0039 }
    L_0x0039:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, boolean, org.json.JSONObject):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r7, int[] r8) throws com.google.android.gms.internal.cast.zzdk, java.lang.IllegalArgumentException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r6 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r6.zzco();
        r3 = r6.zzxh;
        r3.zza(r1, r7);
        r7 = "requestId";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r7, r1);	 Catch:{ JSONException -> 0x0039 }
        r7 = "type";	 Catch:{ JSONException -> 0x0039 }
        r3 = "QUEUE_GET_ITEMS";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r7, r3);	 Catch:{ JSONException -> 0x0039 }
        r7 = "mediaSessionId";	 Catch:{ JSONException -> 0x0039 }
        r3 = r6.zzj();	 Catch:{ JSONException -> 0x0039 }
        r0.put(r7, r3);	 Catch:{ JSONException -> 0x0039 }
        r7 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x0039 }
        r7.<init>();	 Catch:{ JSONException -> 0x0039 }
        r3 = r8.length;	 Catch:{ JSONException -> 0x0039 }
        r4 = 0;	 Catch:{ JSONException -> 0x0039 }
    L_0x002a:
        if (r4 >= r3) goto L_0x0034;	 Catch:{ JSONException -> 0x0039 }
    L_0x002c:
        r5 = r8[r4];	 Catch:{ JSONException -> 0x0039 }
        r7.put(r5);	 Catch:{ JSONException -> 0x0039 }
        r4 = r4 + 1;	 Catch:{ JSONException -> 0x0039 }
        goto L_0x002a;	 Catch:{ JSONException -> 0x0039 }
    L_0x0034:
        r8 = "itemIds";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r8, r7);	 Catch:{ JSONException -> 0x0039 }
    L_0x0039:
        r7 = r0.toString();
        r8 = 0;
        r6.zza(r7, r1, r8);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, int[]):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r6, int[] r7, int r8, org.json.JSONObject r9) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk, java.lang.IllegalArgumentException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        if (r7 == 0) goto L_0x0056;
    L_0x0002:
        r0 = r7.length;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        goto L_0x0056;
    L_0x0006:
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzxf;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x004d }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x004d }
        r6 = "type";	 Catch:{ JSONException -> 0x004d }
        r3 = "QUEUE_REORDER";	 Catch:{ JSONException -> 0x004d }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x004d }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x004d }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x004d }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x004d }
        r6 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x004d }
        r6.<init>();	 Catch:{ JSONException -> 0x004d }
        r3 = 0;	 Catch:{ JSONException -> 0x004d }
    L_0x002f:
        r4 = r7.length;	 Catch:{ JSONException -> 0x004d }
        if (r3 >= r4) goto L_0x003a;	 Catch:{ JSONException -> 0x004d }
    L_0x0032:
        r4 = r7[r3];	 Catch:{ JSONException -> 0x004d }
        r6.put(r3, r4);	 Catch:{ JSONException -> 0x004d }
        r3 = r3 + 1;	 Catch:{ JSONException -> 0x004d }
        goto L_0x002f;	 Catch:{ JSONException -> 0x004d }
    L_0x003a:
        r7 = "itemIds";	 Catch:{ JSONException -> 0x004d }
        r0.put(r7, r6);	 Catch:{ JSONException -> 0x004d }
        if (r8 == 0) goto L_0x0046;	 Catch:{ JSONException -> 0x004d }
    L_0x0041:
        r6 = "insertBefore";	 Catch:{ JSONException -> 0x004d }
        r0.put(r6, r8);	 Catch:{ JSONException -> 0x004d }
    L_0x0046:
        if (r9 == 0) goto L_0x004d;	 Catch:{ JSONException -> 0x004d }
    L_0x0048:
        r6 = "customData";	 Catch:{ JSONException -> 0x004d }
        r0.put(r6, r9);	 Catch:{ JSONException -> 0x004d }
    L_0x004d:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
    L_0x0056:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "itemIdsToReorder must not be null or empty.";
        r6.<init>(r7);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, int[], int, org.json.JSONObject):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r6, int[] r7, org.json.JSONObject r8) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk, java.lang.IllegalArgumentException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        if (r7 == 0) goto L_0x004f;
    L_0x0002:
        r0 = r7.length;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        goto L_0x004f;
    L_0x0006:
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzxe;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x0046 }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x0046 }
        r6 = "type";	 Catch:{ JSONException -> 0x0046 }
        r3 = "QUEUE_REMOVE";	 Catch:{ JSONException -> 0x0046 }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x0046 }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x0046 }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x0046 }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x0046 }
        r6 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x0046 }
        r6.<init>();	 Catch:{ JSONException -> 0x0046 }
        r3 = 0;	 Catch:{ JSONException -> 0x0046 }
    L_0x002f:
        r4 = r7.length;	 Catch:{ JSONException -> 0x0046 }
        if (r3 >= r4) goto L_0x003a;	 Catch:{ JSONException -> 0x0046 }
    L_0x0032:
        r4 = r7[r3];	 Catch:{ JSONException -> 0x0046 }
        r6.put(r3, r4);	 Catch:{ JSONException -> 0x0046 }
        r3 = r3 + 1;	 Catch:{ JSONException -> 0x0046 }
        goto L_0x002f;	 Catch:{ JSONException -> 0x0046 }
    L_0x003a:
        r7 = "itemIds";	 Catch:{ JSONException -> 0x0046 }
        r0.put(r7, r6);	 Catch:{ JSONException -> 0x0046 }
        if (r8 == 0) goto L_0x0046;	 Catch:{ JSONException -> 0x0046 }
    L_0x0041:
        r6 = "customData";	 Catch:{ JSONException -> 0x0046 }
        r0.put(r6, r8);	 Catch:{ JSONException -> 0x0046 }
    L_0x0046:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
    L_0x004f:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "itemIdsToRemove must not be null or empty.";
        r6.<init>(r7);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, int[], org.json.JSONObject):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r7, long[] r8) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r6 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r6.zzco();
        r3 = r6.zzxa;
        r3.zza(r1, r7);
        r7 = "requestId";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r7, r1);	 Catch:{ JSONException -> 0x0039 }
        r7 = "type";	 Catch:{ JSONException -> 0x0039 }
        r3 = "EDIT_TRACKS_INFO";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r7, r3);	 Catch:{ JSONException -> 0x0039 }
        r7 = "mediaSessionId";	 Catch:{ JSONException -> 0x0039 }
        r3 = r6.zzj();	 Catch:{ JSONException -> 0x0039 }
        r0.put(r7, r3);	 Catch:{ JSONException -> 0x0039 }
        r7 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x0039 }
        r7.<init>();	 Catch:{ JSONException -> 0x0039 }
        r3 = 0;	 Catch:{ JSONException -> 0x0039 }
    L_0x0029:
        r4 = r8.length;	 Catch:{ JSONException -> 0x0039 }
        if (r3 >= r4) goto L_0x0034;	 Catch:{ JSONException -> 0x0039 }
    L_0x002c:
        r4 = r8[r3];	 Catch:{ JSONException -> 0x0039 }
        r7.put(r3, r4);	 Catch:{ JSONException -> 0x0039 }
        r3 = r3 + 1;	 Catch:{ JSONException -> 0x0039 }
        goto L_0x0029;	 Catch:{ JSONException -> 0x0039 }
    L_0x0034:
        r8 = "activeTrackIds";	 Catch:{ JSONException -> 0x0039 }
        r0.put(r8, r7);	 Catch:{ JSONException -> 0x0039 }
    L_0x0039:
        r7 = r0.toString();
        r8 = 0;
        r6.zza(r7, r1, r8);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, long[]):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r18, com.google.android.gms.cast.MediaQueueItem[] r19, int r20, int r21, int r22, long r23, org.json.JSONObject r25) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk, java.lang.IllegalArgumentException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r17 = this;
        r0 = r17;
        r1 = r19;
        r2 = r20;
        r3 = r22;
        r4 = r23;
        r6 = r25;
        if (r1 == 0) goto L_0x00ce;
    L_0x000e:
        r7 = r1.length;
        if (r7 != 0) goto L_0x0013;
    L_0x0011:
        goto L_0x00ce;
    L_0x0013:
        r7 = 0;
        r8 = -1;
        if (r3 == r8) goto L_0x003b;
    L_0x0017:
        if (r3 < 0) goto L_0x001c;
    L_0x0019:
        r9 = r1.length;
        if (r3 < r9) goto L_0x003b;
    L_0x001c:
        r2 = new java.lang.IllegalArgumentException;
        r4 = java.util.Locale.ROOT;
        r5 = "currentItemIndexInItemsToInsert %d out of range [0, %d).";
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r3 = java.lang.Integer.valueOf(r22);
        r6[r7] = r3;
        r1 = r1.length;
        r1 = java.lang.Integer.valueOf(r1);
        r3 = 1;
        r6[r3] = r1;
        r1 = java.lang.String.format(r4, r5, r6);
        r2.<init>(r1);
        throw r2;
    L_0x003b:
        r9 = -1;
        r11 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1));
        if (r11 == 0) goto L_0x0060;
    L_0x0041:
        r11 = 0;
        r13 = (r4 > r11 ? 1 : (r4 == r11 ? 0 : -1));
        if (r13 >= 0) goto L_0x0060;
    L_0x0047:
        r1 = new java.lang.IllegalArgumentException;
        r2 = 54;
        r3 = new java.lang.StringBuilder;
        r3.<init>(r2);
        r2 = "playPosition can not be negative: ";
        r3.append(r2);
        r3.append(r4);
        r2 = r3.toString();
        r1.<init>(r2);
        throw r1;
    L_0x0060:
        r11 = new org.json.JSONObject;
        r11.<init>();
        r12 = r17.zzco();
        r14 = r0.zzxc;
        r15 = r18;
        r14.zza(r12, r15);
        r14 = "requestId";	 Catch:{ JSONException -> 0x00c5 }
        r11.put(r14, r12);	 Catch:{ JSONException -> 0x00c5 }
        r14 = "type";	 Catch:{ JSONException -> 0x00c5 }
        r15 = "QUEUE_INSERT";	 Catch:{ JSONException -> 0x00c5 }
        r11.put(r14, r15);	 Catch:{ JSONException -> 0x00c5 }
        r14 = "mediaSessionId";	 Catch:{ JSONException -> 0x00c5 }
        r9 = r17.zzj();	 Catch:{ JSONException -> 0x00c5 }
        r11.put(r14, r9);	 Catch:{ JSONException -> 0x00c5 }
        r9 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x00c5 }
        r9.<init>();	 Catch:{ JSONException -> 0x00c5 }
    L_0x008a:
        r10 = r1.length;	 Catch:{ JSONException -> 0x00c5 }
        if (r7 >= r10) goto L_0x0099;	 Catch:{ JSONException -> 0x00c5 }
    L_0x008d:
        r10 = r1[r7];	 Catch:{ JSONException -> 0x00c5 }
        r10 = r10.toJson();	 Catch:{ JSONException -> 0x00c5 }
        r9.put(r7, r10);	 Catch:{ JSONException -> 0x00c5 }
        r7 = r7 + 1;	 Catch:{ JSONException -> 0x00c5 }
        goto L_0x008a;	 Catch:{ JSONException -> 0x00c5 }
    L_0x0099:
        r1 = "items";	 Catch:{ JSONException -> 0x00c5 }
        r11.put(r1, r9);	 Catch:{ JSONException -> 0x00c5 }
        if (r2 == 0) goto L_0x00a5;	 Catch:{ JSONException -> 0x00c5 }
    L_0x00a0:
        r1 = "insertBefore";	 Catch:{ JSONException -> 0x00c5 }
        r11.put(r1, r2);	 Catch:{ JSONException -> 0x00c5 }
    L_0x00a5:
        if (r3 == r8) goto L_0x00ac;	 Catch:{ JSONException -> 0x00c5 }
    L_0x00a7:
        r1 = "currentItemIndex";	 Catch:{ JSONException -> 0x00c5 }
        r11.put(r1, r3);	 Catch:{ JSONException -> 0x00c5 }
    L_0x00ac:
        r1 = -1;	 Catch:{ JSONException -> 0x00c5 }
        r3 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));	 Catch:{ JSONException -> 0x00c5 }
        if (r3 == 0) goto L_0x00be;	 Catch:{ JSONException -> 0x00c5 }
    L_0x00b2:
        r1 = "currentTime";	 Catch:{ JSONException -> 0x00c5 }
        r2 = (double) r4;	 Catch:{ JSONException -> 0x00c5 }
        r4 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;	 Catch:{ JSONException -> 0x00c5 }
        r2 = r2 / r4;	 Catch:{ JSONException -> 0x00c5 }
        r11.put(r1, r2);	 Catch:{ JSONException -> 0x00c5 }
    L_0x00be:
        if (r6 == 0) goto L_0x00c5;	 Catch:{ JSONException -> 0x00c5 }
    L_0x00c0:
        r1 = "customData";	 Catch:{ JSONException -> 0x00c5 }
        r11.put(r1, r6);	 Catch:{ JSONException -> 0x00c5 }
    L_0x00c5:
        r1 = r11.toString();
        r2 = 0;
        r0.zza(r1, r12, r2);
        return r12;
    L_0x00ce:
        r1 = new java.lang.IllegalArgumentException;
        r2 = "itemsToInsert must not be null or empty.";
        r1.<init>(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, com.google.android.gms.cast.MediaQueueItem[], int, int, int, long, org.json.JSONObject):long");
    }

    public final long zza(com.google.android.gms.internal.cast.zzdm r8, com.google.android.gms.cast.MediaQueueItem[] r9, int r10, int r11, long r12, org.json.JSONObject r14) throws java.lang.IllegalStateException, java.lang.IllegalArgumentException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r7 = this;
        if (r9 == 0) goto L_0x00da;
    L_0x0002:
        r0 = r9.length;
        if (r0 != 0) goto L_0x0007;
    L_0x0005:
        goto L_0x00da;
    L_0x0007:
        if (r10 < 0) goto L_0x00c1;
    L_0x0009:
        r0 = r9.length;
        if (r10 < r0) goto L_0x000e;
    L_0x000c:
        goto L_0x00c1;
    L_0x000e:
        r0 = -1;
        r2 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1));
        if (r2 == 0) goto L_0x0033;
    L_0x0014:
        r2 = 0;
        r4 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1));
        if (r4 >= 0) goto L_0x0033;
    L_0x001a:
        r8 = new java.lang.IllegalArgumentException;
        r9 = 54;
        r10 = new java.lang.StringBuilder;
        r10.<init>(r9);
        r9 = "playPosition can not be negative: ";
        r10.append(r9);
        r10.append(r12);
        r9 = r10.toString();
        r8.<init>(r9);
        throw r8;
    L_0x0033:
        r2 = new org.json.JSONObject;
        r2.<init>();
        r3 = r7.zzco();
        r5 = r7.zzws;
        r5.zza(r3, r8);
        r8 = "requestId";	 Catch:{ JSONException -> 0x00b8 }
        r2.put(r8, r3);	 Catch:{ JSONException -> 0x00b8 }
        r8 = "type";	 Catch:{ JSONException -> 0x00b8 }
        r5 = "QUEUE_LOAD";	 Catch:{ JSONException -> 0x00b8 }
        r2.put(r8, r5);	 Catch:{ JSONException -> 0x00b8 }
        r8 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x00b8 }
        r8.<init>();	 Catch:{ JSONException -> 0x00b8 }
        r5 = 0;	 Catch:{ JSONException -> 0x00b8 }
    L_0x0053:
        r6 = r9.length;	 Catch:{ JSONException -> 0x00b8 }
        if (r5 >= r6) goto L_0x0062;	 Catch:{ JSONException -> 0x00b8 }
    L_0x0056:
        r6 = r9[r5];	 Catch:{ JSONException -> 0x00b8 }
        r6 = r6.toJson();	 Catch:{ JSONException -> 0x00b8 }
        r8.put(r5, r6);	 Catch:{ JSONException -> 0x00b8 }
        r5 = r5 + 1;	 Catch:{ JSONException -> 0x00b8 }
        goto L_0x0053;	 Catch:{ JSONException -> 0x00b8 }
    L_0x0062:
        r9 = "items";	 Catch:{ JSONException -> 0x00b8 }
        r2.put(r9, r8);	 Catch:{ JSONException -> 0x00b8 }
        switch(r11) {
            case 0: goto L_0x007f;
            case 1: goto L_0x007a;
            case 2: goto L_0x0075;
            case 3: goto L_0x006d;
            default: goto L_0x006a;
        };	 Catch:{ JSONException -> 0x00b8 }
    L_0x006a:
        r8 = new java.lang.IllegalArgumentException;	 Catch:{ JSONException -> 0x00b8 }
        goto L_0x00a1;	 Catch:{ JSONException -> 0x00b8 }
    L_0x006d:
        r8 = "repeatMode";	 Catch:{ JSONException -> 0x00b8 }
        r9 = "REPEAT_ALL_AND_SHUFFLE";	 Catch:{ JSONException -> 0x00b8 }
    L_0x0071:
        r2.put(r8, r9);	 Catch:{ JSONException -> 0x00b8 }
        goto L_0x0084;	 Catch:{ JSONException -> 0x00b8 }
    L_0x0075:
        r8 = "repeatMode";	 Catch:{ JSONException -> 0x00b8 }
        r9 = "REPEAT_SINGLE";	 Catch:{ JSONException -> 0x00b8 }
        goto L_0x0071;	 Catch:{ JSONException -> 0x00b8 }
    L_0x007a:
        r8 = "repeatMode";	 Catch:{ JSONException -> 0x00b8 }
        r9 = "REPEAT_ALL";	 Catch:{ JSONException -> 0x00b8 }
        goto L_0x0071;	 Catch:{ JSONException -> 0x00b8 }
    L_0x007f:
        r8 = "repeatMode";	 Catch:{ JSONException -> 0x00b8 }
        r9 = "REPEAT_OFF";	 Catch:{ JSONException -> 0x00b8 }
        goto L_0x0071;	 Catch:{ JSONException -> 0x00b8 }
    L_0x0084:
        r8 = "startIndex";	 Catch:{ JSONException -> 0x00b8 }
        r2.put(r8, r10);	 Catch:{ JSONException -> 0x00b8 }
        r8 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1));	 Catch:{ JSONException -> 0x00b8 }
        if (r8 == 0) goto L_0x0099;	 Catch:{ JSONException -> 0x00b8 }
    L_0x008d:
        r8 = "currentTime";	 Catch:{ JSONException -> 0x00b8 }
        r9 = (double) r12;	 Catch:{ JSONException -> 0x00b8 }
        r11 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;	 Catch:{ JSONException -> 0x00b8 }
        r9 = r9 / r11;	 Catch:{ JSONException -> 0x00b8 }
        r2.put(r8, r9);	 Catch:{ JSONException -> 0x00b8 }
    L_0x0099:
        if (r14 == 0) goto L_0x00b8;	 Catch:{ JSONException -> 0x00b8 }
    L_0x009b:
        r8 = "customData";	 Catch:{ JSONException -> 0x00b8 }
        r2.put(r8, r14);	 Catch:{ JSONException -> 0x00b8 }
        goto L_0x00b8;	 Catch:{ JSONException -> 0x00b8 }
    L_0x00a1:
        r9 = 32;	 Catch:{ JSONException -> 0x00b8 }
        r10 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x00b8 }
        r10.<init>(r9);	 Catch:{ JSONException -> 0x00b8 }
        r9 = "Invalid repeat mode: ";	 Catch:{ JSONException -> 0x00b8 }
        r10.append(r9);	 Catch:{ JSONException -> 0x00b8 }
        r10.append(r11);	 Catch:{ JSONException -> 0x00b8 }
        r9 = r10.toString();	 Catch:{ JSONException -> 0x00b8 }
        r8.<init>(r9);	 Catch:{ JSONException -> 0x00b8 }
        throw r8;	 Catch:{ JSONException -> 0x00b8 }
    L_0x00b8:
        r8 = r2.toString();
        r9 = 0;
        r7.zza(r8, r3, r9);
        return r3;
    L_0x00c1:
        r8 = new java.lang.IllegalArgumentException;
        r9 = 31;
        r11 = new java.lang.StringBuilder;
        r11.<init>(r9);
        r9 = "Invalid startIndex: ";
        r11.append(r9);
        r11.append(r10);
        r9 = r11.toString();
        r8.<init>(r9);
        throw r8;
    L_0x00da:
        r8 = new java.lang.IllegalArgumentException;
        r9 = "items must not be null or empty.";
        r8.<init>(r9);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zza(com.google.android.gms.internal.cast.zzdm, com.google.android.gms.cast.MediaQueueItem[], int, int, long, org.json.JSONObject):long");
    }

    public final void zza(long j, int i) {
        for (zzdn zzc : zzcn()) {
            zzc.zzc(j, i, null);
        }
    }

    public final void zza(zzdj zzdj) {
        this.zzwr = zzdj;
    }

    public final long zzb(zzdm zzdm) throws IllegalStateException {
        return zza(zzdm, true);
    }

    public final long zzb(com.google.android.gms.internal.cast.zzdm r5, double r6, org.json.JSONObject r8) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = r4.zzwq;
        if (r0 != 0) goto L_0x000a;
    L_0x0004:
        r5 = new com.google.android.gms.internal.cast.zzdk;
        r5.<init>();
        throw r5;
    L_0x000a:
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r4.zzco();
        r3 = r4.zzxj;
        r3.zza(r1, r5);
        r5 = "requestId";	 Catch:{ JSONException -> 0x003b }
        r0.put(r5, r1);	 Catch:{ JSONException -> 0x003b }
        r5 = "type";	 Catch:{ JSONException -> 0x003b }
        r3 = "SET_PLAYBACK_RATE";	 Catch:{ JSONException -> 0x003b }
        r0.put(r5, r3);	 Catch:{ JSONException -> 0x003b }
        r5 = "playbackRate";	 Catch:{ JSONException -> 0x003b }
        r0.put(r5, r6);	 Catch:{ JSONException -> 0x003b }
        r5 = "mediaSessionId";	 Catch:{ JSONException -> 0x003b }
        r6 = r4.zzwq;	 Catch:{ JSONException -> 0x003b }
        r6 = r6.zzj();	 Catch:{ JSONException -> 0x003b }
        r0.put(r5, r6);	 Catch:{ JSONException -> 0x003b }
        if (r8 == 0) goto L_0x003b;	 Catch:{ JSONException -> 0x003b }
    L_0x0036:
        r5 = "customData";	 Catch:{ JSONException -> 0x003b }
        r0.put(r5, r8);	 Catch:{ JSONException -> 0x003b }
    L_0x003b:
        r5 = r0.toString();
        r6 = 0;
        r4.zza(r5, r1, r6);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zzb(com.google.android.gms.internal.cast.zzdm, double, org.json.JSONObject):long");
    }

    public final long zzb(com.google.android.gms.internal.cast.zzdm r6, org.json.JSONObject r7) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzwv;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x002a }
        r6 = "type";	 Catch:{ JSONException -> 0x002a }
        r3 = "STOP";	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x002a }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x002a }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x002a }
        if (r7 == 0) goto L_0x002a;	 Catch:{ JSONException -> 0x002a }
    L_0x0025:
        r6 = "customData";	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r7);	 Catch:{ JSONException -> 0x002a }
    L_0x002a:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zzb(com.google.android.gms.internal.cast.zzdm, org.json.JSONObject):long");
    }

    public final long zzb(String str, List<zzbp> list) throws IllegalStateException {
        long zzco = zzco();
        zza(zza(str, (List) list, zzco), zzco, null);
        return zzco;
    }

    public final long zzc(com.google.android.gms.internal.cast.zzdm r6) throws com.google.android.gms.internal.cast.zzdk, java.lang.IllegalStateException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzxg;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x0023 }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x0023 }
        r6 = "type";	 Catch:{ JSONException -> 0x0023 }
        r3 = "QUEUE_GET_ITEM_IDS";	 Catch:{ JSONException -> 0x0023 }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x0023 }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x0023 }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x0023 }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x0023 }
    L_0x0023:
        r6 = r0.toString();
        r0 = 0;
        r5.zza(r6, r1, r0);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zzc(com.google.android.gms.internal.cast.zzdm):long");
    }

    public final long zzc(com.google.android.gms.internal.cast.zzdm r6, org.json.JSONObject r7) throws java.lang.IllegalStateException, com.google.android.gms.internal.cast.zzdk {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r5.zzco();
        r3 = r5.zzwu;
        r3.zza(r1, r6);
        r6 = "requestId";	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r1);	 Catch:{ JSONException -> 0x002a }
        r6 = "type";	 Catch:{ JSONException -> 0x002a }
        r3 = "PLAY";	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x002a }
        r6 = "mediaSessionId";	 Catch:{ JSONException -> 0x002a }
        r3 = r5.zzj();	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r3);	 Catch:{ JSONException -> 0x002a }
        if (r7 == 0) goto L_0x002a;	 Catch:{ JSONException -> 0x002a }
    L_0x0025:
        r6 = "customData";	 Catch:{ JSONException -> 0x002a }
        r0.put(r6, r7);	 Catch:{ JSONException -> 0x002a }
    L_0x002a:
        r6 = r0.toString();
        r7 = 0;
        r5.zza(r6, r1, r7);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zzc(com.google.android.gms.internal.cast.zzdm, org.json.JSONObject):long");
    }

    public final void zzcm() {
        super.zzcm();
        zzcz();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzn(java.lang.String r13) {
        /*
        r12 = this;
        r0 = r12.zzuv;
        r1 = "message received: %s";
        r2 = 1;
        r3 = new java.lang.Object[r2];
        r4 = 0;
        r3[r4] = r13;
        r0.m25d(r1, r3);
        r0 = 2;
        r1 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x02a2 }
        r1.<init>(r13);	 Catch:{ JSONException -> 0x02a2 }
        r3 = "type";
        r3 = r1.getString(r3);	 Catch:{ JSONException -> 0x02a2 }
        r5 = "requestId";
        r6 = -1;
        r5 = r1.optLong(r5, r6);	 Catch:{ JSONException -> 0x02a2 }
        r7 = r3.hashCode();	 Catch:{ JSONException -> 0x02a2 }
        r8 = 3;
        r9 = -1;
        r10 = 4;
        switch(r7) {
            case -1830647528: goto L_0x0072;
            case -1790231854: goto L_0x0068;
            case -1125000185: goto L_0x005e;
            case -262628938: goto L_0x0054;
            case 154411710: goto L_0x004a;
            case 431600379: goto L_0x0040;
            case 823510221: goto L_0x0036;
            case 2107149050: goto L_0x002c;
            default: goto L_0x002b;
        };	 Catch:{ JSONException -> 0x02a2 }
    L_0x002b:
        goto L_0x007c;
    L_0x002c:
        r7 = "QUEUE_ITEM_IDS";
        r3 = r3.equals(r7);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x007c;
    L_0x0034:
        r3 = 5;
        goto L_0x007d;
    L_0x0036:
        r7 = "MEDIA_STATUS";
        r3 = r3.equals(r7);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x007c;
    L_0x003e:
        r3 = 0;
        goto L_0x007d;
    L_0x0040:
        r7 = "INVALID_PLAYER_STATE";
        r3 = r3.equals(r7);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x007c;
    L_0x0048:
        r3 = 1;
        goto L_0x007d;
    L_0x004a:
        r7 = "QUEUE_CHANGE";
        r3 = r3.equals(r7);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x007c;
    L_0x0052:
        r3 = 6;
        goto L_0x007d;
    L_0x0054:
        r7 = "LOAD_FAILED";
        r3 = r3.equals(r7);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x007c;
    L_0x005c:
        r3 = 2;
        goto L_0x007d;
    L_0x005e:
        r7 = "INVALID_REQUEST";
        r3 = r3.equals(r7);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x007c;
    L_0x0066:
        r3 = 4;
        goto L_0x007d;
    L_0x0068:
        r7 = "QUEUE_ITEMS";
        r3 = r3.equals(r7);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x007c;
    L_0x0070:
        r3 = 7;
        goto L_0x007d;
    L_0x0072:
        r7 = "LOAD_CANCELLED";
        r3 = r3.equals(r7);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x007c;
    L_0x007a:
        r3 = 3;
        goto L_0x007d;
    L_0x007c:
        r3 = -1;
    L_0x007d:
        r7 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;
        r11 = 0;
        switch(r3) {
            case 0: goto L_0x01b4;
            case 1: goto L_0x018c;
            case 2: goto L_0x0180;
            case 3: goto L_0x0172;
            case 4: goto L_0x014a;
            case 5: goto L_0x012f;
            case 6: goto L_0x00b8;
            case 7: goto L_0x0084;
            default: goto L_0x0083;
        };	 Catch:{ JSONException -> 0x02a2 }
    L_0x0083:
        return;
    L_0x0084:
        r3 = r12.zzxh;	 Catch:{ JSONException -> 0x02a2 }
        r3.zzc(r5, r4, r11);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x02a1;
    L_0x008d:
        r3 = "items";
        r1 = r1.getJSONArray(r3);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r1.length();	 Catch:{ JSONException -> 0x02a2 }
        r3 = new com.google.android.gms.cast.MediaQueueItem[r3];	 Catch:{ JSONException -> 0x02a2 }
        r5 = 0;
    L_0x009a:
        r6 = r1.length();	 Catch:{ JSONException -> 0x02a2 }
        if (r5 >= r6) goto L_0x00b2;
    L_0x00a0:
        r6 = new com.google.android.gms.cast.MediaQueueItem$Builder;	 Catch:{ JSONException -> 0x02a2 }
        r7 = r1.getJSONObject(r5);	 Catch:{ JSONException -> 0x02a2 }
        r6.<init>(r7);	 Catch:{ JSONException -> 0x02a2 }
        r6 = r6.build();	 Catch:{ JSONException -> 0x02a2 }
        r3[r5] = r6;	 Catch:{ JSONException -> 0x02a2 }
        r5 = r5 + 1;
        goto L_0x009a;
    L_0x00b2:
        r1 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        r1.zzb(r3);	 Catch:{ JSONException -> 0x02a2 }
        return;
    L_0x00b8:
        r3 = r12.zzxi;	 Catch:{ JSONException -> 0x02a2 }
        r3.zzc(r5, r4, r11);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x012e;
    L_0x00c1:
        r3 = "changeType";
        r3 = r1.getString(r3);	 Catch:{ JSONException -> 0x02a2 }
        r5 = "itemIds";
        r5 = r1.getJSONArray(r5);	 Catch:{ JSONException -> 0x02a2 }
        r5 = zzb(r5);	 Catch:{ JSONException -> 0x02a2 }
        r6 = "insertBefore";
        r1 = r1.optInt(r6, r4);	 Catch:{ JSONException -> 0x02a2 }
        if (r5 == 0) goto L_0x012e;
    L_0x00d9:
        r6 = r3.hashCode();	 Catch:{ JSONException -> 0x02a2 }
        switch(r6) {
            case -2130463047: goto L_0x0108;
            case -1881281404: goto L_0x00fe;
            case -1785516855: goto L_0x00f5;
            case 1122976047: goto L_0x00eb;
            case 1395699694: goto L_0x00e1;
            default: goto L_0x00e0;
        };	 Catch:{ JSONException -> 0x02a2 }
    L_0x00e0:
        goto L_0x0112;
    L_0x00e1:
        r6 = "NO_CHANGE";
        r3 = r3.equals(r6);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x0112;
    L_0x00e9:
        r8 = 4;
        goto L_0x0113;
    L_0x00eb:
        r6 = "ITEMS_CHANGE";
        r3 = r3.equals(r6);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x0112;
    L_0x00f3:
        r8 = 1;
        goto L_0x0113;
    L_0x00f5:
        r6 = "UPDATE";
        r3 = r3.equals(r6);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x0112;
    L_0x00fd:
        goto L_0x0113;
    L_0x00fe:
        r6 = "REMOVE";
        r3 = r3.equals(r6);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x0112;
    L_0x0106:
        r8 = 2;
        goto L_0x0113;
    L_0x0108:
        r6 = "INSERT";
        r3 = r3.equals(r6);	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x0112;
    L_0x0110:
        r8 = 0;
        goto L_0x0113;
    L_0x0112:
        r8 = -1;
    L_0x0113:
        switch(r8) {
            case 0: goto L_0x0129;
            case 1: goto L_0x0123;
            case 2: goto L_0x011d;
            case 3: goto L_0x0117;
            default: goto L_0x0116;
        };	 Catch:{ JSONException -> 0x02a2 }
    L_0x0116:
        return;
    L_0x0117:
        r1 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        r1.zza(r5);	 Catch:{ JSONException -> 0x02a2 }
        return;
    L_0x011d:
        r1 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        r1.zzc(r5);	 Catch:{ JSONException -> 0x02a2 }
        return;
    L_0x0123:
        r1 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        r1.zzb(r5);	 Catch:{ JSONException -> 0x02a2 }
        return;
    L_0x0129:
        r3 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        r3.zza(r5, r1);	 Catch:{ JSONException -> 0x02a2 }
    L_0x012e:
        return;
    L_0x012f:
        r3 = r12.zzxg;	 Catch:{ JSONException -> 0x02a2 }
        r3.zzc(r5, r4, r11);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x0149;
    L_0x0138:
        r3 = "itemIds";
        r1 = r1.getJSONArray(r3);	 Catch:{ JSONException -> 0x02a2 }
        r1 = zzb(r1);	 Catch:{ JSONException -> 0x02a2 }
        if (r1 == 0) goto L_0x0149;
    L_0x0144:
        r3 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        r3.zza(r1);	 Catch:{ JSONException -> 0x02a2 }
    L_0x0149:
        return;
    L_0x014a:
        r3 = r12.zzuv;	 Catch:{ JSONException -> 0x02a2 }
        r8 = "received unexpected error: Invalid Request.";
        r9 = new java.lang.Object[r4];	 Catch:{ JSONException -> 0x02a2 }
        r3.m28w(r8, r9);	 Catch:{ JSONException -> 0x02a2 }
        r3 = "customData";
        r1 = r1.optJSONObject(r3);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r12.zzcn();	 Catch:{ JSONException -> 0x02a2 }
        r3 = r3.iterator();	 Catch:{ JSONException -> 0x02a2 }
    L_0x0161:
        r8 = r3.hasNext();	 Catch:{ JSONException -> 0x02a2 }
        if (r8 == 0) goto L_0x0171;
    L_0x0167:
        r8 = r3.next();	 Catch:{ JSONException -> 0x02a2 }
        r8 = (com.google.android.gms.internal.cast.zzdn) r8;	 Catch:{ JSONException -> 0x02a2 }
        r8.zzc(r5, r7, r1);	 Catch:{ JSONException -> 0x02a2 }
        goto L_0x0161;
    L_0x0171:
        return;
    L_0x0172:
        r3 = "customData";
        r1 = r1.optJSONObject(r3);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r12.zzws;	 Catch:{ JSONException -> 0x02a2 }
        r7 = 2101; // 0x835 float:2.944E-42 double:1.038E-320;
        r3.zzc(r5, r7, r1);	 Catch:{ JSONException -> 0x02a2 }
        return;
    L_0x0180:
        r3 = "customData";
        r1 = r1.optJSONObject(r3);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r12.zzws;	 Catch:{ JSONException -> 0x02a2 }
        r3.zzc(r5, r7, r1);	 Catch:{ JSONException -> 0x02a2 }
        return;
    L_0x018c:
        r3 = r12.zzuv;	 Catch:{ JSONException -> 0x02a2 }
        r8 = "received unexpected error: Invalid Player State.";
        r9 = new java.lang.Object[r4];	 Catch:{ JSONException -> 0x02a2 }
        r3.m28w(r8, r9);	 Catch:{ JSONException -> 0x02a2 }
        r3 = "customData";
        r1 = r1.optJSONObject(r3);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r12.zzcn();	 Catch:{ JSONException -> 0x02a2 }
        r3 = r3.iterator();	 Catch:{ JSONException -> 0x02a2 }
    L_0x01a3:
        r8 = r3.hasNext();	 Catch:{ JSONException -> 0x02a2 }
        if (r8 == 0) goto L_0x01b3;
    L_0x01a9:
        r8 = r3.next();	 Catch:{ JSONException -> 0x02a2 }
        r8 = (com.google.android.gms.internal.cast.zzdn) r8;	 Catch:{ JSONException -> 0x02a2 }
        r8.zzc(r5, r7, r1);	 Catch:{ JSONException -> 0x02a2 }
        goto L_0x01a3;
    L_0x01b3:
        return;
    L_0x01b4:
        r3 = "status";
        r1 = r1.getJSONArray(r3);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r1.length();	 Catch:{ JSONException -> 0x02a2 }
        if (r3 <= 0) goto L_0x028e;
    L_0x01c0:
        r1 = r1.getJSONObject(r4);	 Catch:{ JSONException -> 0x02a2 }
        r3 = r12.zzws;	 Catch:{ JSONException -> 0x02a2 }
        r3 = r3.test(r5);	 Catch:{ JSONException -> 0x02a2 }
        r7 = r12.zzww;	 Catch:{ JSONException -> 0x02a2 }
        r7 = r7.zzdb();	 Catch:{ JSONException -> 0x02a2 }
        if (r7 == 0) goto L_0x01dc;
    L_0x01d2:
        r7 = r12.zzww;	 Catch:{ JSONException -> 0x02a2 }
        r7 = r7.test(r5);	 Catch:{ JSONException -> 0x02a2 }
        if (r7 != 0) goto L_0x01dc;
    L_0x01da:
        r7 = 1;
        goto L_0x01dd;
    L_0x01dc:
        r7 = 0;
    L_0x01dd:
        r8 = r12.zzwx;	 Catch:{ JSONException -> 0x02a2 }
        r8 = r8.zzdb();	 Catch:{ JSONException -> 0x02a2 }
        if (r8 == 0) goto L_0x01ed;
    L_0x01e5:
        r8 = r12.zzwx;	 Catch:{ JSONException -> 0x02a2 }
        r8 = r8.test(r5);	 Catch:{ JSONException -> 0x02a2 }
        if (r8 == 0) goto L_0x01fd;
    L_0x01ed:
        r8 = r12.zzwy;	 Catch:{ JSONException -> 0x02a2 }
        r8 = r8.zzdb();	 Catch:{ JSONException -> 0x02a2 }
        if (r8 == 0) goto L_0x01ff;
    L_0x01f5:
        r8 = r12.zzwy;	 Catch:{ JSONException -> 0x02a2 }
        r8 = r8.test(r5);	 Catch:{ JSONException -> 0x02a2 }
        if (r8 != 0) goto L_0x01ff;
    L_0x01fd:
        r8 = 1;
        goto L_0x0200;
    L_0x01ff:
        r8 = 0;
    L_0x0200:
        if (r7 == 0) goto L_0x0204;
    L_0x0202:
        r7 = 2;
        goto L_0x0205;
    L_0x0204:
        r7 = 0;
    L_0x0205:
        if (r8 == 0) goto L_0x0209;
    L_0x0207:
        r7 = r7 | 1;
    L_0x0209:
        if (r3 != 0) goto L_0x0217;
    L_0x020b:
        r3 = r12.zzwq;	 Catch:{ JSONException -> 0x02a2 }
        if (r3 != 0) goto L_0x0210;
    L_0x020f:
        goto L_0x0217;
    L_0x0210:
        r3 = r12.zzwq;	 Catch:{ JSONException -> 0x02a2 }
        r1 = r3.zza(r1, r7);	 Catch:{ JSONException -> 0x02a2 }
        goto L_0x0226;
    L_0x0217:
        r3 = new com.google.android.gms.cast.MediaStatus;	 Catch:{ JSONException -> 0x02a2 }
        r3.<init>(r1);	 Catch:{ JSONException -> 0x02a2 }
        r12.zzwq = r3;	 Catch:{ JSONException -> 0x02a2 }
        r7 = android.os.SystemClock.elapsedRealtime();	 Catch:{ JSONException -> 0x02a2 }
        r12.zzwp = r7;	 Catch:{ JSONException -> 0x02a2 }
        r1 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
    L_0x0226:
        r3 = r1 & 1;
        if (r3 == 0) goto L_0x0233;
    L_0x022a:
        r7 = android.os.SystemClock.elapsedRealtime();	 Catch:{ JSONException -> 0x02a2 }
        r12.zzwp = r7;	 Catch:{ JSONException -> 0x02a2 }
        r12.onStatusUpdated();	 Catch:{ JSONException -> 0x02a2 }
    L_0x0233:
        r3 = r1 & 2;
        if (r3 == 0) goto L_0x0240;
    L_0x0237:
        r7 = android.os.SystemClock.elapsedRealtime();	 Catch:{ JSONException -> 0x02a2 }
        r12.zzwp = r7;	 Catch:{ JSONException -> 0x02a2 }
        r12.onStatusUpdated();	 Catch:{ JSONException -> 0x02a2 }
    L_0x0240:
        r3 = r1 & 4;
        if (r3 == 0) goto L_0x0247;
    L_0x0244:
        r12.onMetadataUpdated();	 Catch:{ JSONException -> 0x02a2 }
    L_0x0247:
        r3 = r1 & 8;
        if (r3 == 0) goto L_0x024e;
    L_0x024b:
        r12.onQueueStatusUpdated();	 Catch:{ JSONException -> 0x02a2 }
    L_0x024e:
        r3 = r1 & 16;
        if (r3 == 0) goto L_0x0255;
    L_0x0252:
        r12.onPreloadStatusUpdated();	 Catch:{ JSONException -> 0x02a2 }
    L_0x0255:
        r3 = r1 & 32;
        if (r3 == 0) goto L_0x0268;
    L_0x0259:
        r7 = android.os.SystemClock.elapsedRealtime();	 Catch:{ JSONException -> 0x02a2 }
        r12.zzwp = r7;	 Catch:{ JSONException -> 0x02a2 }
        r3 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x0268;
    L_0x0263:
        r3 = r12.zzwr;	 Catch:{ JSONException -> 0x02a2 }
        r3.onAdBreakStatusUpdated();	 Catch:{ JSONException -> 0x02a2 }
    L_0x0268:
        r1 = r1 & 64;
        if (r1 == 0) goto L_0x0275;
    L_0x026c:
        r7 = android.os.SystemClock.elapsedRealtime();	 Catch:{ JSONException -> 0x02a2 }
        r12.zzwp = r7;	 Catch:{ JSONException -> 0x02a2 }
        r12.onStatusUpdated();	 Catch:{ JSONException -> 0x02a2 }
    L_0x0275:
        r1 = r12.zzcn();	 Catch:{ JSONException -> 0x02a2 }
        r1 = r1.iterator();	 Catch:{ JSONException -> 0x02a2 }
    L_0x027d:
        r3 = r1.hasNext();	 Catch:{ JSONException -> 0x02a2 }
        if (r3 == 0) goto L_0x028d;
    L_0x0283:
        r3 = r1.next();	 Catch:{ JSONException -> 0x02a2 }
        r3 = (com.google.android.gms.internal.cast.zzdn) r3;	 Catch:{ JSONException -> 0x02a2 }
        r3.zzc(r5, r4, r11);	 Catch:{ JSONException -> 0x02a2 }
        goto L_0x027d;
    L_0x028d:
        return;
    L_0x028e:
        r12.zzwq = r11;	 Catch:{ JSONException -> 0x02a2 }
        r12.onStatusUpdated();	 Catch:{ JSONException -> 0x02a2 }
        r12.onMetadataUpdated();	 Catch:{ JSONException -> 0x02a2 }
        r12.onQueueStatusUpdated();	 Catch:{ JSONException -> 0x02a2 }
        r12.onPreloadStatusUpdated();	 Catch:{ JSONException -> 0x02a2 }
        r1 = r12.zzwz;	 Catch:{ JSONException -> 0x02a2 }
        r1.zzc(r5, r4, r11);	 Catch:{ JSONException -> 0x02a2 }
    L_0x02a1:
        return;
    L_0x02a2:
        r1 = move-exception;
        r3 = r12.zzuv;
        r5 = "Message is malformed (%s); ignoring: %s";
        r0 = new java.lang.Object[r0];
        r1 = r1.getMessage();
        r0[r4] = r1;
        r0[r2] = r13;
        r3.m28w(r5, r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdh.zzn(java.lang.String):void");
    }
}
