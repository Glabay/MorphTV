package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.internal.cast.zzcu;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "AdBreakClipInfoCreator")
@Reserved({1})
public class AdBreakClipInfo extends AbstractSafeParcelable {
    public static final long AD_BREAK_CLIP_NOT_SKIPPABLE = -1;
    public static final Creator<AdBreakClipInfo> CREATOR = new zza();
    @Field(getter = "getMimeType", id = 6)
    private final String mimeType;
    @Field(getter = "getId", id = 2)
    private final String zze;
    @Field(getter = "getTitle", id = 3)
    private final String zzf;
    @Field(getter = "getDurationInMs", id = 4)
    private final long zzg;
    @Field(getter = "getContentUrl", id = 5)
    private final String zzh;
    @Field(getter = "getClickThroughUrl", id = 7)
    private final String zzi;
    @Field(getter = "getCustomDataAsString", id = 8)
    private String zzj;
    @Field(getter = "getContentId", id = 9)
    private String zzk;
    @Field(getter = "getImageUrl", id = 10)
    private String zzl;
    @Field(getter = "getWhenSkippableInMs", id = 11)
    private final long zzm;
    @HlsSegmentFormat
    @Field(getter = "getHlsSegmentFormat", id = 12)
    private final String zzn;
    @Field(getter = "getVastAdsRequest", id = 13)
    private final VastAdsRequest zzo;
    private JSONObject zzp;

    public static class Builder {
        private String mimeType = null;
        private String zze = null;
        private String zzf = null;
        private long zzg = 0;
        private String zzh = null;
        private String zzi = null;
        private String zzj = null;
        private String zzk = null;
        private String zzl = null;
        private long zzm = -1;
        @HlsSegmentFormat
        private String zzn;
        private VastAdsRequest zzo = null;

        public Builder(String str) {
            this.zze = str;
        }

        public AdBreakClipInfo build() {
            return new AdBreakClipInfo(this.zze, this.zzf, this.zzg, this.zzh, this.mimeType, this.zzi, this.zzj, this.zzk, this.zzl, this.zzm, this.zzn, this.zzo);
        }

        public Builder setClickThroughUrl(String str) {
            this.zzi = str;
            return this;
        }

        public Builder setContentId(String str) {
            this.zzk = str;
            return this;
        }

        public Builder setContentUrl(String str) {
            this.zzh = str;
            return this;
        }

        public Builder setCustomDataJsonString(String str) {
            this.zzj = str;
            return this;
        }

        public Builder setDurationInMs(long j) {
            this.zzg = j;
            return this;
        }

        public Builder setHlsSegmentFormat(String str) {
            this.zzn = str;
            return this;
        }

        public Builder setImageUrl(String str) {
            this.zzl = str;
            return this;
        }

        public Builder setMimeType(String str) {
            this.mimeType = str;
            return this;
        }

        public Builder setTitle(String str) {
            this.zzf = str;
            return this;
        }

        public Builder setVastAdsRequest(VastAdsRequest vastAdsRequest) {
            this.zzo = vastAdsRequest;
            return this;
        }

        public Builder setWhenSkippableInMs(long j) {
            this.zzm = j;
            return this;
        }
    }

    @Constructor
    AdBreakClipInfo(@Param(id = 2) String str, @Param(id = 3) String str2, @Param(id = 4) long j, @Param(id = 5) String str3, @Param(id = 6) String str4, @Param(id = 7) String str5, @Param(id = 8) String str6, @Param(id = 9) String str7, @Param(id = 10) String str8, @Param(id = 11) long j2, @HlsSegmentFormat @Param(id = 12) String str9, @Param(id = 13) VastAdsRequest vastAdsRequest) {
        this.zze = str;
        this.zzf = str2;
        this.zzg = j;
        this.zzh = str3;
        this.mimeType = str4;
        this.zzi = str5;
        this.zzj = str6;
        this.zzk = str7;
        this.zzl = str8;
        this.zzm = j2;
        this.zzn = str9;
        this.zzo = vastAdsRequest;
        if (TextUtils.isEmpty(this.zzj)) {
            JSONObject jSONObject = new JSONObject();
            this.zzp = jSONObject;
            return;
        }
        try {
            this.zzp = new JSONObject(str6);
        } catch (JSONException e) {
            Log.w("AdBreakClipInfo", String.format(Locale.ROOT, "Error creating AdBreakClipInfo: %s", new Object[]{e.getMessage()}));
            this.zzj = null;
            jSONObject = new JSONObject();
        }
    }

    static AdBreakClipInfo zza(JSONObject jSONObject) {
        JSONObject jSONObject2 = jSONObject;
        if (jSONObject2 == null || !jSONObject2.has(TtmlNode.ATTR_ID)) {
            return null;
        }
        try {
            String str;
            long intValue;
            String jSONObject3;
            String string = jSONObject2.getString(TtmlNode.ATTR_ID);
            long optLong = (long) (((double) jSONObject2.optLong("duration")) * 1000.0d);
            String optString = jSONObject2.optString("clickThroughUrl", null);
            String optString2 = jSONObject2.optString("contentUrl", null);
            String optString3 = jSONObject2.optString("mimeType", null);
            if (optString3 == null) {
                optString3 = jSONObject2.optString("contentType", null);
            }
            String str2 = optString3;
            String optString4 = jSONObject2.optString("title", null);
            JSONObject optJSONObject = jSONObject2.optJSONObject("customData");
            String optString5 = jSONObject2.optString("contentId", null);
            String optString6 = jSONObject2.optString("posterUrl", null);
            if (jSONObject2.has("whenSkippable")) {
                str = optString2;
                intValue = (long) (((double) ((Integer) jSONObject2.get("whenSkippable")).intValue()) * 1000.0d);
            } else {
                str = optString2;
                intValue = -1;
            }
            String optString7 = jSONObject2.optString("hlsSegmentFormat", null);
            VastAdsRequest fromJson = VastAdsRequest.fromJson(jSONObject2.optJSONObject("vastAdsRequest"));
            if (optJSONObject != null) {
                if (optJSONObject.length() != 0) {
                    jSONObject3 = optJSONObject.toString();
                    return new AdBreakClipInfo(string, optString4, optLong, str, str2, optString, jSONObject3, optString5, optString6, intValue, optString7, fromJson);
                }
            }
            jSONObject3 = null;
            return new AdBreakClipInfo(string, optString4, optLong, str, str2, optString, jSONObject3, optString5, optString6, intValue, optString7, fromJson);
        } catch (JSONException e) {
            JSONException jSONException = e;
            Log.d("AdBreakClipInfo", String.format(Locale.ROOT, "Error while creating an AdBreakClipInfo from JSON: %s", new Object[]{jSONException.getMessage()}));
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdBreakClipInfo)) {
            return false;
        }
        AdBreakClipInfo adBreakClipInfo = (AdBreakClipInfo) obj;
        return zzcu.zza(this.zze, adBreakClipInfo.zze) && zzcu.zza(this.zzf, adBreakClipInfo.zzf) && this.zzg == adBreakClipInfo.zzg && zzcu.zza(this.zzh, adBreakClipInfo.zzh) && zzcu.zza(this.mimeType, adBreakClipInfo.mimeType) && zzcu.zza(this.zzi, adBreakClipInfo.zzi) && zzcu.zza(this.zzj, adBreakClipInfo.zzj) && zzcu.zza(this.zzk, adBreakClipInfo.zzk) && zzcu.zza(this.zzl, adBreakClipInfo.zzl) && this.zzm == adBreakClipInfo.zzm && zzcu.zza(this.zzn, adBreakClipInfo.zzn) && zzcu.zza(this.zzo, adBreakClipInfo.zzo);
    }

    public String getClickThroughUrl() {
        return this.zzi;
    }

    public String getContentId() {
        return this.zzk;
    }

    public String getContentUrl() {
        return this.zzh;
    }

    public JSONObject getCustomData() {
        return this.zzp;
    }

    public long getDurationInMs() {
        return this.zzg;
    }

    public String getHlsSegmentFormat() {
        return this.zzn;
    }

    public String getId() {
        return this.zze;
    }

    public String getImageUrl() {
        return this.zzl;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getTitle() {
        return this.zzf;
    }

    public VastAdsRequest getVastAdsRequest() {
        return this.zzo;
    }

    public long getWhenSkippableInMs() {
        return this.zzm;
    }

    public int hashCode() {
        return Objects.hashCode(this.zze, this.zzf, Long.valueOf(this.zzg), this.zzh, this.mimeType, this.zzi, this.zzj, this.zzk, this.zzl, Long.valueOf(this.zzm), this.zzn, this.zzo);
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r8 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = "id";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zze;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
        r1 = "duration";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzg;	 Catch:{ JSONException -> 0x0092 }
        r2 = (double) r2;	 Catch:{ JSONException -> 0x0092 }
        r4 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;	 Catch:{ JSONException -> 0x0092 }
        r2 = r2 / r4;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
        r1 = r8.zzm;	 Catch:{ JSONException -> 0x0092 }
        r6 = -1;	 Catch:{ JSONException -> 0x0092 }
        r3 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1));	 Catch:{ JSONException -> 0x0092 }
        if (r3 == 0) goto L_0x002b;	 Catch:{ JSONException -> 0x0092 }
    L_0x0022:
        r1 = "whenSkippable";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzm;	 Catch:{ JSONException -> 0x0092 }
        r2 = (double) r2;	 Catch:{ JSONException -> 0x0092 }
        r2 = r2 / r4;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x002b:
        r1 = r8.zzk;	 Catch:{ JSONException -> 0x0092 }
        if (r1 == 0) goto L_0x0036;	 Catch:{ JSONException -> 0x0092 }
    L_0x002f:
        r1 = "contentId";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzk;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x0036:
        r1 = r8.mimeType;	 Catch:{ JSONException -> 0x0092 }
        if (r1 == 0) goto L_0x0041;	 Catch:{ JSONException -> 0x0092 }
    L_0x003a:
        r1 = "contentType";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.mimeType;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x0041:
        r1 = r8.zzf;	 Catch:{ JSONException -> 0x0092 }
        if (r1 == 0) goto L_0x004c;	 Catch:{ JSONException -> 0x0092 }
    L_0x0045:
        r1 = "title";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzf;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x004c:
        r1 = r8.zzh;	 Catch:{ JSONException -> 0x0092 }
        if (r1 == 0) goto L_0x0057;	 Catch:{ JSONException -> 0x0092 }
    L_0x0050:
        r1 = "contentUrl";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzh;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x0057:
        r1 = r8.zzi;	 Catch:{ JSONException -> 0x0092 }
        if (r1 == 0) goto L_0x0062;	 Catch:{ JSONException -> 0x0092 }
    L_0x005b:
        r1 = "clickThroughUrl";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzi;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x0062:
        r1 = r8.zzp;	 Catch:{ JSONException -> 0x0092 }
        if (r1 == 0) goto L_0x006d;	 Catch:{ JSONException -> 0x0092 }
    L_0x0066:
        r1 = "customData";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzp;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x006d:
        r1 = r8.zzl;	 Catch:{ JSONException -> 0x0092 }
        if (r1 == 0) goto L_0x0078;	 Catch:{ JSONException -> 0x0092 }
    L_0x0071:
        r1 = "posterUrl";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzl;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x0078:
        r1 = r8.zzn;	 Catch:{ JSONException -> 0x0092 }
        if (r1 == 0) goto L_0x0083;	 Catch:{ JSONException -> 0x0092 }
    L_0x007c:
        r1 = "hlsSegmentFormat";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzn;	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x0083:
        r1 = r8.zzo;	 Catch:{ JSONException -> 0x0092 }
        if (r1 == 0) goto L_0x0092;	 Catch:{ JSONException -> 0x0092 }
    L_0x0087:
        r1 = "vastAdsRequest";	 Catch:{ JSONException -> 0x0092 }
        r2 = r8.zzo;	 Catch:{ JSONException -> 0x0092 }
        r2 = r2.toJson();	 Catch:{ JSONException -> 0x0092 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0092 }
    L_0x0092:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.AdBreakClipInfo.toJson():org.json.JSONObject");
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, getId(), false);
        SafeParcelWriter.writeString(parcel, 3, getTitle(), false);
        SafeParcelWriter.writeLong(parcel, 4, getDurationInMs());
        SafeParcelWriter.writeString(parcel, 5, getContentUrl(), false);
        SafeParcelWriter.writeString(parcel, 6, getMimeType(), false);
        SafeParcelWriter.writeString(parcel, 7, getClickThroughUrl(), false);
        SafeParcelWriter.writeString(parcel, 8, this.zzj, false);
        SafeParcelWriter.writeString(parcel, 9, getContentId(), false);
        SafeParcelWriter.writeString(parcel, 10, getImageUrl(), false);
        SafeParcelWriter.writeLong(parcel, 11, getWhenSkippableInMs());
        SafeParcelWriter.writeString(parcel, 12, getHlsSegmentFormat(), false);
        SafeParcelWriter.writeParcelable(parcel, 13, getVastAdsRequest(), i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
