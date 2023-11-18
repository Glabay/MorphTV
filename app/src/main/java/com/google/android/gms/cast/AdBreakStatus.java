package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
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

@Class(creator = "AdBreakStatusCreator")
@Reserved({1})
public class AdBreakStatus extends AbstractSafeParcelable {
    public static final int AD_BREAK_CLIP_NOT_SKIPPABLE = -1;
    public static final Creator<AdBreakStatus> CREATOR = new zzc();
    @Field(getter = "getWhenSkippableInMs", id = 6)
    private final long zzm;
    @Field(getter = "getCurrentBreakTimeInMs", id = 2)
    private final long zzu;
    @Field(getter = "getCurrentBreakClipTimeInMs", id = 3)
    private final long zzv;
    @Field(getter = "getBreakId", id = 4)
    private final String zzw;
    @Field(getter = "getBreakClipId", id = 5)
    private final String zzx;

    @Constructor
    AdBreakStatus(@Param(id = 2) long j, @Param(id = 3) long j2, @Param(id = 4) String str, @Param(id = 5) String str2, @Param(id = 6) long j3) {
        this.zzu = j;
        this.zzv = j2;
        this.zzw = str;
        this.zzx = str2;
        this.zzm = j3;
    }

    static AdBreakStatus zzc(JSONObject jSONObject) {
        if (jSONObject == null || !jSONObject.has("currentBreakTime") || !jSONObject.has("currentBreakClipTime")) {
            return null;
        }
        try {
            long j = (long) (((double) jSONObject.getLong("currentBreakTime")) * 1000.0d);
            long j2 = (long) (((double) jSONObject.getLong("currentBreakClipTime")) * 1000.0d);
            String optString = jSONObject.optString("breakId", null);
            String optString2 = jSONObject.optString("breakClipId", null);
            long optLong = jSONObject.optLong("whenSkippable", -1);
            if (optLong != -1) {
                optLong = (long) (((double) optLong) * 1000.0d);
            }
            return new AdBreakStatus(j, j2, optString, optString2, optLong);
        } catch (JSONException e) {
            Log.d("AdBreakInfo", String.format(Locale.ROOT, "Error while creating an AdBreakClipInfo from JSON: %s", new Object[]{e.getMessage()}));
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdBreakStatus)) {
            return false;
        }
        AdBreakStatus adBreakStatus = (AdBreakStatus) obj;
        return this.zzu == adBreakStatus.zzu && this.zzv == adBreakStatus.zzv && zzcu.zza(this.zzw, adBreakStatus.zzw) && zzcu.zza(this.zzx, adBreakStatus.zzx) && this.zzm == adBreakStatus.zzm;
    }

    public String getBreakClipId() {
        return this.zzx;
    }

    public String getBreakId() {
        return this.zzw;
    }

    public long getCurrentBreakClipTimeInMs() {
        return this.zzv;
    }

    public long getCurrentBreakTimeInMs() {
        return this.zzu;
    }

    public long getWhenSkippableInMs() {
        return this.zzm;
    }

    public int hashCode() {
        return Objects.hashCode(Long.valueOf(this.zzu), Long.valueOf(this.zzv), this.zzw, this.zzx, Long.valueOf(this.zzm));
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeLong(parcel, 2, getCurrentBreakTimeInMs());
        SafeParcelWriter.writeLong(parcel, 3, getCurrentBreakClipTimeInMs());
        SafeParcelWriter.writeString(parcel, 4, getBreakId(), false);
        SafeParcelWriter.writeString(parcel, 5, getBreakClipId(), false);
        SafeParcelWriter.writeLong(parcel, 6, getWhenSkippableInMs());
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
