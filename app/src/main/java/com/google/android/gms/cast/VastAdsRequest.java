package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.internal.cast.zzcu;
import org.json.JSONObject;

@Class(creator = "VastAdsRequestCreator")
@Reserved({1})
public class VastAdsRequest extends AbstractSafeParcelable {
    public static final Creator<VastAdsRequest> CREATOR = new zzbs();
    @Field(getter = "getAdTagUrl", id = 2)
    private final String zzgl;
    @Field(getter = "getAdsResponse", id = 3)
    private final String zzgm;

    public static class Builder {
        private String zzgl = null;
        private String zzgm = null;

        public VastAdsRequest build() {
            return new VastAdsRequest(this.zzgl, this.zzgm);
        }

        public Builder setAdTagUrl(String str) {
            this.zzgl = str;
            return this;
        }

        public Builder setAdsResponse(String str) {
            this.zzgm = str;
            return this;
        }
    }

    @Constructor
    VastAdsRequest(@Param(id = 2) String str, @Param(id = 3) String str2) {
        this.zzgl = str;
        this.zzgm = str2;
    }

    public static VastAdsRequest fromJson(JSONObject jSONObject) {
        return jSONObject == null ? null : new VastAdsRequest(jSONObject.optString("adTagUrl", null), jSONObject.optString("adsResponse", null));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VastAdsRequest)) {
            return false;
        }
        VastAdsRequest vastAdsRequest = (VastAdsRequest) obj;
        return zzcu.zza(this.zzgl, vastAdsRequest.zzgl) && zzcu.zza(this.zzgm, vastAdsRequest.zzgm);
    }

    public String getAdTagUrl() {
        return this.zzgl;
    }

    public String getAdsResponse() {
        return this.zzgm;
    }

    public int hashCode() {
        return Objects.hashCode(this.zzgl, this.zzgm);
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
        r3 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r3.zzgl;	 Catch:{ JSONException -> 0x001b }
        if (r1 == 0) goto L_0x0010;	 Catch:{ JSONException -> 0x001b }
    L_0x0009:
        r1 = "adTagUrl";	 Catch:{ JSONException -> 0x001b }
        r2 = r3.zzgl;	 Catch:{ JSONException -> 0x001b }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x001b }
    L_0x0010:
        r1 = r3.zzgm;	 Catch:{ JSONException -> 0x001b }
        if (r1 == 0) goto L_0x001b;	 Catch:{ JSONException -> 0x001b }
    L_0x0014:
        r1 = "adsResponse";	 Catch:{ JSONException -> 0x001b }
        r2 = r3.zzgm;	 Catch:{ JSONException -> 0x001b }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x001b }
    L_0x001b:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.VastAdsRequest.toJson():org.json.JSONObject");
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, getAdTagUrl(), false);
        SafeParcelWriter.writeString(parcel, 3, getAdsResponse(), false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
