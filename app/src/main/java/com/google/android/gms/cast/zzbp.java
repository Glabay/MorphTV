package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.VisibleForTesting;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.internal.cast.zzcu;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "RequestItemCreator")
@Reserved({1})
public final class zzbp extends AbstractSafeParcelable {
    public static final Creator<zzbp> CREATOR = new zzbq();
    @Field(getter = "getUrl", id = 2)
    private final String url;
    @Field(getter = "getProtocolType", id = 3)
    private final int zzgf;
    @Field(defaultValue = "0", getter = "getInitialTime", id = 4)
    private final int zzgg;
    @Field(getter = "getHlsSegmentFormat", id = 5)
    private final String zzn;

    @Constructor
    public zzbp(@Param(id = 2) String str, @Param(id = 3) int i, @Param(id = 4) int i2, @HlsSegmentFormat @Param(id = 5) String str2) {
        this.url = str;
        this.zzgf = i;
        this.zzgg = i2;
        this.zzn = str2;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzbp)) {
            return false;
        }
        zzbp zzbp = (zzbp) obj;
        return zzcu.zza(this.url, zzbp.url) && zzcu.zza(Integer.valueOf(this.zzgf), Integer.valueOf(zzbp.zzgf)) && zzcu.zza(Integer.valueOf(this.zzgg), Integer.valueOf(zzbp.zzgg)) && zzcu.zza(zzbp.zzn, this.zzn);
    }

    @VisibleForTesting
    public final int hashCode() {
        return Objects.hashCode(this.url, Integer.valueOf(this.zzgf), Integer.valueOf(this.zzgg), this.zzn);
    }

    public final JSONObject toJson() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(ImagesContract.URL, this.url);
        jSONObject.put("protocolType", this.zzgf);
        jSONObject.put("initialTime", this.zzgg);
        jSONObject.put("hlsSegmentFormat", this.zzn);
        return jSONObject;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.url, false);
        SafeParcelWriter.writeInt(parcel, 3, this.zzgf);
        SafeParcelWriter.writeInt(parcel, 4, this.zzgg);
        SafeParcelWriter.writeString(parcel, 5, this.zzn, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
