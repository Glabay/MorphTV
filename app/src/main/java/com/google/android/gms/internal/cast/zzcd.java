package com.google.android.gms.internal.cast;

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

@Class(creator = "ApplicationStatusCreator")
@Reserved({1})
public final class zzcd extends AbstractSafeParcelable {
    public static final Creator<zzcd> CREATOR = new zzce();
    @Field(getter = "getApplicationStatusText", id = 2)
    private String zzur;

    public zzcd() {
        this(null);
    }

    @Constructor
    zzcd(@Param(id = 2) String str) {
        this.zzur = str;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcd)) {
            return false;
        }
        return zzcu.zza(this.zzur, ((zzcd) obj).zzur);
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzur);
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzur, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }

    public final String zzcl() {
        return this.zzur;
    }
}
