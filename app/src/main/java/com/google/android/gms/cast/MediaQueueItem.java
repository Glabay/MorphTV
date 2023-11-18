package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.cast.zzcu;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "MediaQueueItemCreator")
@Reserved({1})
public class MediaQueueItem extends AbstractSafeParcelable {
    public static final Creator<MediaQueueItem> CREATOR = new zzam();
    public static final double DEFAULT_PLAYBACK_DURATION = Double.POSITIVE_INFINITY;
    public static final int INVALID_ITEM_ID = 0;
    @Field(getter = "getAutoplay", id = 4)
    private boolean zzdj;
    @Field(getter = "getActiveTrackIds", id = 8)
    private long[] zzdm;
    @Field(getter = "getMedia", id = 2)
    private MediaInfo zzdw;
    @Field(getter = "getItemId", id = 3)
    private int zzdx;
    @Field(getter = "getStartTime", id = 5)
    private double zzdy;
    @Field(getter = "getPlaybackDuration", id = 6)
    private double zzdz;
    @Field(getter = "getPreloadTime", id = 7)
    private double zzea;
    @Field(id = 9)
    private String zzj;
    private JSONObject zzp;

    @VisibleForTesting
    public static class Builder {
        private final MediaQueueItem zzeb;

        public Builder(MediaInfo mediaInfo) throws IllegalArgumentException {
            this.zzeb = new MediaQueueItem(mediaInfo);
        }

        public Builder(MediaQueueItem mediaQueueItem) throws IllegalArgumentException {
            this.zzeb = new MediaQueueItem();
        }

        public Builder(JSONObject jSONObject) throws JSONException {
            this.zzeb = new MediaQueueItem(jSONObject);
        }

        public MediaQueueItem build() {
            this.zzeb.zzi();
            return this.zzeb;
        }

        public Builder clearItemId() {
            this.zzeb.zza(0);
            return this;
        }

        public Builder setActiveTrackIds(long[] jArr) {
            this.zzeb.zza(jArr);
            return this;
        }

        public Builder setAutoplay(boolean z) {
            this.zzeb.zze(z);
            return this;
        }

        public Builder setCustomData(JSONObject jSONObject) {
            this.zzeb.setCustomData(jSONObject);
            return this;
        }

        public Builder setPlaybackDuration(double d) {
            this.zzeb.zzb(d);
            return this;
        }

        public Builder setPreloadTime(double d) throws IllegalArgumentException {
            this.zzeb.zzc(d);
            return this;
        }

        public Builder setStartTime(double d) throws IllegalArgumentException {
            this.zzeb.zza(d);
            return this;
        }
    }

    private MediaQueueItem(MediaInfo mediaInfo) throws IllegalArgumentException {
        this(mediaInfo, 0, true, 0.0d, Double.POSITIVE_INFINITY, 0.0d, null, null);
        if (mediaInfo == null) {
            throw new IllegalArgumentException("media cannot be null.");
        }
    }

    @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
    MediaQueueItem(@com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 2) com.google.android.gms.cast.MediaInfo r1, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 3) int r2, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 4) boolean r3, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 5) double r4, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 6) double r6, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 7) double r8, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 8) long[] r10, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 9) java.lang.String r11) {
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
        r0 = this;
        r0.<init>();
        r0.zzdw = r1;
        r0.zzdx = r2;
        r0.zzdj = r3;
        r0.zzdy = r4;
        r0.zzdz = r6;
        r0.zzea = r8;
        r0.zzdm = r10;
        r0.zzj = r11;
        r1 = r0.zzj;
        r2 = 0;
        if (r1 == 0) goto L_0x0027;
    L_0x0018:
        r1 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0022 }
        r3 = r0.zzj;	 Catch:{ JSONException -> 0x0022 }
        r1.<init>(r3);	 Catch:{ JSONException -> 0x0022 }
        r0.zzp = r1;	 Catch:{ JSONException -> 0x0022 }
        return;
    L_0x0022:
        r0.zzp = r2;
        r0.zzj = r2;
        return;
    L_0x0027:
        r0.zzp = r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaQueueItem.<init>(com.google.android.gms.cast.MediaInfo, int, boolean, double, double, double, long[], java.lang.String):void");
    }

    private MediaQueueItem(MediaQueueItem mediaQueueItem) throws IllegalArgumentException {
        this(mediaQueueItem.getMedia(), mediaQueueItem.getItemId(), mediaQueueItem.getAutoplay(), mediaQueueItem.getStartTime(), mediaQueueItem.getPlaybackDuration(), mediaQueueItem.getPreloadTime(), mediaQueueItem.getActiveTrackIds(), null);
        if (this.zzdw == null) {
            throw new IllegalArgumentException("media cannot be null.");
        }
        this.zzp = mediaQueueItem.getCustomData();
    }

    MediaQueueItem(JSONObject jSONObject) throws JSONException {
        this(null, 0, true, 0.0d, Double.POSITIVE_INFINITY, 0.0d, null, null);
        zzf(jSONObject);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaQueueItem)) {
            return false;
        }
        MediaQueueItem mediaQueueItem = (MediaQueueItem) obj;
        return (this.zzp == null ? 1 : null) != (mediaQueueItem.zzp == null ? 1 : null) ? false : (this.zzp == null || mediaQueueItem.zzp == null || JsonUtils.areJsonValuesEquivalent(this.zzp, mediaQueueItem.zzp)) && zzcu.zza(this.zzdw, mediaQueueItem.zzdw) && this.zzdx == mediaQueueItem.zzdx && this.zzdj == mediaQueueItem.zzdj && this.zzdy == mediaQueueItem.zzdy && this.zzdz == mediaQueueItem.zzdz && this.zzea == mediaQueueItem.zzea && Arrays.equals(this.zzdm, mediaQueueItem.zzdm);
    }

    public long[] getActiveTrackIds() {
        return this.zzdm;
    }

    public boolean getAutoplay() {
        return this.zzdj;
    }

    public JSONObject getCustomData() {
        return this.zzp;
    }

    public int getItemId() {
        return this.zzdx;
    }

    public MediaInfo getMedia() {
        return this.zzdw;
    }

    public double getPlaybackDuration() {
        return this.zzdz;
    }

    public double getPreloadTime() {
        return this.zzea;
    }

    public double getStartTime() {
        return this.zzdy;
    }

    public int hashCode() {
        return Objects.hashCode(this.zzdw, Integer.valueOf(this.zzdx), Boolean.valueOf(this.zzdj), Double.valueOf(this.zzdy), Double.valueOf(this.zzdz), Double.valueOf(this.zzea), Integer.valueOf(Arrays.hashCode(this.zzdm)), String.valueOf(this.zzp));
    }

    final void setCustomData(JSONObject jSONObject) {
        this.zzp = jSONObject;
    }

    public final org.json.JSONObject toJson() {
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
        r7 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = "media";	 Catch:{ JSONException -> 0x0066 }
        r2 = r7.zzdw;	 Catch:{ JSONException -> 0x0066 }
        r2 = r2.toJson();	 Catch:{ JSONException -> 0x0066 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0066 }
        r1 = r7.zzdx;	 Catch:{ JSONException -> 0x0066 }
        if (r1 == 0) goto L_0x001b;	 Catch:{ JSONException -> 0x0066 }
    L_0x0014:
        r1 = "itemId";	 Catch:{ JSONException -> 0x0066 }
        r2 = r7.zzdx;	 Catch:{ JSONException -> 0x0066 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0066 }
    L_0x001b:
        r1 = "autoplay";	 Catch:{ JSONException -> 0x0066 }
        r2 = r7.zzdj;	 Catch:{ JSONException -> 0x0066 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0066 }
        r1 = "startTime";	 Catch:{ JSONException -> 0x0066 }
        r2 = r7.zzdy;	 Catch:{ JSONException -> 0x0066 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0066 }
        r1 = r7.zzdz;	 Catch:{ JSONException -> 0x0066 }
        r3 = 9218868437227405312; // 0x7ff0000000000000 float:0.0 double:Infinity;	 Catch:{ JSONException -> 0x0066 }
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ JSONException -> 0x0066 }
        if (r5 == 0) goto L_0x0038;	 Catch:{ JSONException -> 0x0066 }
    L_0x0031:
        r1 = "playbackDuration";	 Catch:{ JSONException -> 0x0066 }
        r2 = r7.zzdz;	 Catch:{ JSONException -> 0x0066 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0066 }
    L_0x0038:
        r1 = "preloadTime";	 Catch:{ JSONException -> 0x0066 }
        r2 = r7.zzea;	 Catch:{ JSONException -> 0x0066 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0066 }
        r1 = r7.zzdm;	 Catch:{ JSONException -> 0x0066 }
        if (r1 == 0) goto L_0x005b;	 Catch:{ JSONException -> 0x0066 }
    L_0x0043:
        r1 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x0066 }
        r1.<init>();	 Catch:{ JSONException -> 0x0066 }
        r2 = r7.zzdm;	 Catch:{ JSONException -> 0x0066 }
        r3 = r2.length;	 Catch:{ JSONException -> 0x0066 }
        r4 = 0;	 Catch:{ JSONException -> 0x0066 }
    L_0x004c:
        if (r4 >= r3) goto L_0x0056;	 Catch:{ JSONException -> 0x0066 }
    L_0x004e:
        r5 = r2[r4];	 Catch:{ JSONException -> 0x0066 }
        r1.put(r5);	 Catch:{ JSONException -> 0x0066 }
        r4 = r4 + 1;	 Catch:{ JSONException -> 0x0066 }
        goto L_0x004c;	 Catch:{ JSONException -> 0x0066 }
    L_0x0056:
        r2 = "activeTrackIds";	 Catch:{ JSONException -> 0x0066 }
        r0.put(r2, r1);	 Catch:{ JSONException -> 0x0066 }
    L_0x005b:
        r1 = r7.zzp;	 Catch:{ JSONException -> 0x0066 }
        if (r1 == 0) goto L_0x0066;	 Catch:{ JSONException -> 0x0066 }
    L_0x005f:
        r1 = "customData";	 Catch:{ JSONException -> 0x0066 }
        r2 = r7.zzp;	 Catch:{ JSONException -> 0x0066 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0066 }
    L_0x0066:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaQueueItem.toJson():org.json.JSONObject");
    }

    public void writeToParcel(Parcel parcel, int i) {
        this.zzj = this.zzp == null ? null : this.zzp.toString();
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 2, getMedia(), i, false);
        SafeParcelWriter.writeInt(parcel, 3, getItemId());
        SafeParcelWriter.writeBoolean(parcel, 4, getAutoplay());
        SafeParcelWriter.writeDouble(parcel, 5, getStartTime());
        SafeParcelWriter.writeDouble(parcel, 6, getPlaybackDuration());
        SafeParcelWriter.writeDouble(parcel, 7, getPreloadTime());
        SafeParcelWriter.writeLongArray(parcel, 8, getActiveTrackIds(), false);
        SafeParcelWriter.writeString(parcel, 9, this.zzj, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    final void zza(double d) throws IllegalArgumentException {
        if (!Double.isNaN(d)) {
            if (d >= 0.0d) {
                this.zzdy = d;
                return;
            }
        }
        throw new IllegalArgumentException("startTime cannot be negative or NaN.");
    }

    final void zza(int i) {
        this.zzdx = 0;
    }

    final void zza(long[] jArr) {
        this.zzdm = jArr;
    }

    final void zzb(double d) throws IllegalArgumentException {
        if (Double.isNaN(d)) {
            throw new IllegalArgumentException("playbackDuration cannot be NaN.");
        }
        this.zzdz = d;
    }

    final void zzc(double d) throws IllegalArgumentException {
        if (!Double.isNaN(d)) {
            if (d >= 0.0d) {
                this.zzea = d;
                return;
            }
        }
        throw new IllegalArgumentException("preloadTime cannot be negative or NaN.");
    }

    final void zze(boolean z) {
        this.zzdj = z;
    }

    public final boolean zzf(JSONObject jSONObject) throws JSONException {
        boolean z;
        int i;
        double d;
        Object obj = null;
        if (jSONObject.has("media")) {
            this.zzdw = new MediaInfo(jSONObject.getJSONObject("media"));
            z = true;
        } else {
            z = false;
        }
        if (jSONObject.has("itemId")) {
            i = jSONObject.getInt("itemId");
            if (this.zzdx != i) {
                this.zzdx = i;
                z = true;
            }
        }
        if (jSONObject.has("autoplay")) {
            boolean z2 = jSONObject.getBoolean("autoplay");
            if (this.zzdj != z2) {
                this.zzdj = z2;
                z = true;
            }
        }
        if (jSONObject.has("startTime")) {
            d = jSONObject.getDouble("startTime");
            if (Math.abs(d - this.zzdy) > 1.0E-7d) {
                this.zzdy = d;
                z = true;
            }
        }
        if (jSONObject.has("playbackDuration")) {
            d = jSONObject.getDouble("playbackDuration");
            if (Math.abs(d - this.zzdz) > 1.0E-7d) {
                this.zzdz = d;
                z = true;
            }
        }
        if (jSONObject.has("preloadTime")) {
            d = jSONObject.getDouble("preloadTime");
            if (Math.abs(d - this.zzea) > 1.0E-7d) {
                this.zzea = d;
                z = true;
            }
        }
        long[] jArr;
        if (jSONObject.has("activeTrackIds")) {
            JSONArray jSONArray = jSONObject.getJSONArray("activeTrackIds");
            int length = jSONArray.length();
            jArr = new long[length];
            for (int i2 = 0; i2 < length; i2++) {
                jArr[i2] = jSONArray.getLong(i2);
            }
            if (this.zzdm != null) {
                if (this.zzdm.length == length) {
                    i = 0;
                    while (i < length) {
                        if (this.zzdm[i] == jArr[i]) {
                            i++;
                        }
                    }
                }
            }
            obj = 1;
            break;
        }
        jArr = null;
        if (obj != null) {
            this.zzdm = jArr;
            z = true;
        }
        if (!jSONObject.has("customData")) {
            return z;
        }
        this.zzp = jSONObject.getJSONObject("customData");
        return true;
    }

    final void zzi() throws IllegalArgumentException {
        if (this.zzdw == null) {
            throw new IllegalArgumentException("media cannot be null.");
        }
        if (!Double.isNaN(this.zzdy)) {
            if (this.zzdy >= 0.0d) {
                if (Double.isNaN(this.zzdz)) {
                    throw new IllegalArgumentException("playbackDuration cannot be NaN.");
                }
                if (!Double.isNaN(this.zzea)) {
                    if (this.zzea >= 0.0d) {
                        return;
                    }
                }
                throw new IllegalArgumentException("preloadTime cannot be negative or Nan.");
            }
        }
        throw new IllegalArgumentException("startTime cannot be negative or NaN.");
    }
}
