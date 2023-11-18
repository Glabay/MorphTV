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

@Class(creator = "EqualizerBandSettingsCreator")
@Reserved({1})
public final class zzab extends AbstractSafeParcelable {
    public static final Creator<zzab> CREATOR = new zzac();
    @Field(getter = "getFrequency", id = 2)
    private final float zzcr;
    @Field(getter = "getQFactor", id = 3)
    private final float zzcs;
    @Field(getter = "getGainDb", id = 4)
    private final float zzct;

    @Constructor
    public zzab(@Param(id = 2) float f, @Param(id = 3) float f2, @Param(id = 4) float f3) {
        this.zzcr = f;
        this.zzcs = f2;
        this.zzct = f3;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzab)) {
            return false;
        }
        zzab zzab = (zzab) obj;
        return this.zzcr == zzab.zzcr && this.zzcs == zzab.zzcs && this.zzct == zzab.zzct;
    }

    public final int hashCode() {
        return Objects.hashCode(Float.valueOf(this.zzcr), Float.valueOf(this.zzcs), Float.valueOf(this.zzct));
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeFloat(parcel, 2, this.zzcr);
        SafeParcelWriter.writeFloat(parcel, 3, this.zzcs);
        SafeParcelWriter.writeFloat(parcel, 4, this.zzct);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
