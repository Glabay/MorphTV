package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.internal.cast.zzcu;
import java.util.Arrays;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "AdBreakInfoCreator")
@Reserved({1})
public class AdBreakInfo extends AbstractSafeParcelable {
    public static final Creator<AdBreakInfo> CREATOR = new zzb();
    @Field(getter = "getId", id = 3)
    private final String zze;
    @Field(getter = "getDurationInMs", id = 4)
    private final long zzg;
    @Field(getter = "getPlaybackPositionInMs", id = 2)
    private final long zzq;
    @Field(getter = "isWatched", id = 5)
    private final boolean zzr;
    @Field(getter = "getBreakClipIds", id = 6)
    private String[] zzs;
    @Field(getter = "isEmbedded", id = 7)
    private final boolean zzt;

    public static class Builder {
        private String zze = null;
        private long zzg = 0;
        private long zzq = 0;
        private boolean zzr = false;
        private String[] zzs = null;
        private boolean zzt = false;

        public Builder(long j) {
            this.zzq = j;
        }

        public AdBreakInfo build() {
            return new AdBreakInfo(this.zzq, this.zze, this.zzg, this.zzr, this.zzs, this.zzt);
        }

        public Builder setBreakClipIds(String[] strArr) {
            this.zzs = strArr;
            return this;
        }

        public Builder setDurationInMs(long j) {
            this.zzg = j;
            return this;
        }

        public Builder setId(String str) {
            this.zze = str;
            return this;
        }

        public Builder setIsEmbedded(boolean z) {
            this.zzt = z;
            return this;
        }

        public Builder setIsWatched(boolean z) {
            this.zzr = z;
            return this;
        }
    }

    @Constructor
    public AdBreakInfo(@Param(id = 2) long j, @Param(id = 3) String str, @Param(id = 4) long j2, @Param(id = 5) boolean z, @Param(id = 6) String[] strArr, @Param(id = 7) boolean z2) {
        this.zzq = j;
        this.zze = str;
        this.zzg = j2;
        this.zzr = z;
        this.zzs = strArr;
        this.zzt = z2;
    }

    static AdBreakInfo zzb(JSONObject jSONObject) {
        if (jSONObject == null || !jSONObject.has(TtmlNode.ATTR_ID) || !jSONObject.has("position")) {
            return null;
        }
        try {
            String[] strArr;
            String string = jSONObject.getString(TtmlNode.ATTR_ID);
            long j = (long) (((double) jSONObject.getLong("position")) * 1000.0d);
            boolean optBoolean = jSONObject.optBoolean("isWatched");
            long optLong = (long) (((double) jSONObject.optLong("duration")) * 1000.0d);
            JSONArray optJSONArray = jSONObject.optJSONArray("breakClipIds");
            if (optJSONArray != null) {
                String[] strArr2 = new String[optJSONArray.length()];
                for (int i = 0; i < optJSONArray.length(); i++) {
                    strArr2[i] = optJSONArray.getString(i);
                }
                strArr = strArr2;
            } else {
                strArr = null;
            }
            return new AdBreakInfo(j, string, optLong, optBoolean, strArr, jSONObject.optBoolean("isEmbedded"));
        } catch (JSONException e) {
            Log.d("AdBreakInfo", String.format(Locale.ROOT, "Error while creating an AdBreakInfo from JSON: %s", new Object[]{e.getMessage()}));
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdBreakInfo)) {
            return false;
        }
        AdBreakInfo adBreakInfo = (AdBreakInfo) obj;
        return zzcu.zza(this.zze, adBreakInfo.zze) && this.zzq == adBreakInfo.zzq && this.zzg == adBreakInfo.zzg && this.zzr == adBreakInfo.zzr && Arrays.equals(this.zzs, adBreakInfo.zzs) && this.zzt == adBreakInfo.zzt;
    }

    public String[] getBreakClipIds() {
        return this.zzs;
    }

    public long getDurationInMs() {
        return this.zzg;
    }

    public String getId() {
        return this.zze;
    }

    public long getPlaybackPositionInMs() {
        return this.zzq;
    }

    public int hashCode() {
        return this.zze.hashCode();
    }

    public boolean isEmbedded() {
        return this.zzt;
    }

    public boolean isWatched() {
        return this.zzr;
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
        r6 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = "id";	 Catch:{ JSONException -> 0x004d }
        r2 = r6.zze;	 Catch:{ JSONException -> 0x004d }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x004d }
        r1 = "position";	 Catch:{ JSONException -> 0x004d }
        r2 = r6.zzq;	 Catch:{ JSONException -> 0x004d }
        r2 = (double) r2;	 Catch:{ JSONException -> 0x004d }
        r4 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;	 Catch:{ JSONException -> 0x004d }
        r2 = r2 / r4;	 Catch:{ JSONException -> 0x004d }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x004d }
        r1 = "isWatched";	 Catch:{ JSONException -> 0x004d }
        r2 = r6.zzr;	 Catch:{ JSONException -> 0x004d }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x004d }
        r1 = "isEmbedded";	 Catch:{ JSONException -> 0x004d }
        r2 = r6.zzt;	 Catch:{ JSONException -> 0x004d }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x004d }
        r1 = "duration";	 Catch:{ JSONException -> 0x004d }
        r2 = r6.zzg;	 Catch:{ JSONException -> 0x004d }
        r2 = (double) r2;	 Catch:{ JSONException -> 0x004d }
        r2 = r2 / r4;	 Catch:{ JSONException -> 0x004d }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x004d }
        r1 = r6.zzs;	 Catch:{ JSONException -> 0x004d }
        if (r1 == 0) goto L_0x004d;	 Catch:{ JSONException -> 0x004d }
    L_0x0035:
        r1 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x004d }
        r1.<init>();	 Catch:{ JSONException -> 0x004d }
        r2 = r6.zzs;	 Catch:{ JSONException -> 0x004d }
        r3 = r2.length;	 Catch:{ JSONException -> 0x004d }
        r4 = 0;	 Catch:{ JSONException -> 0x004d }
    L_0x003e:
        if (r4 >= r3) goto L_0x0048;	 Catch:{ JSONException -> 0x004d }
    L_0x0040:
        r5 = r2[r4];	 Catch:{ JSONException -> 0x004d }
        r1.put(r5);	 Catch:{ JSONException -> 0x004d }
        r4 = r4 + 1;	 Catch:{ JSONException -> 0x004d }
        goto L_0x003e;	 Catch:{ JSONException -> 0x004d }
    L_0x0048:
        r2 = "breakClipIds";	 Catch:{ JSONException -> 0x004d }
        r0.put(r2, r1);	 Catch:{ JSONException -> 0x004d }
    L_0x004d:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.AdBreakInfo.toJson():org.json.JSONObject");
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeLong(parcel, 2, getPlaybackPositionInMs());
        SafeParcelWriter.writeString(parcel, 3, getId(), false);
        SafeParcelWriter.writeLong(parcel, 4, getDurationInMs());
        SafeParcelWriter.writeBoolean(parcel, 5, isWatched());
        SafeParcelWriter.writeStringArray(parcel, 6, getBreakClipIds(), false);
        SafeParcelWriter.writeBoolean(parcel, 7, isEmbedded());
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
