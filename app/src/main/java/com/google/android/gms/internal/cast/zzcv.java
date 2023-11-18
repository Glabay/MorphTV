package com.google.android.gms.internal.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.zzad;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;

@Class(creator = "DeviceStatusCreator")
@Reserved({1})
public final class zzcv extends AbstractSafeParcelable {
    public static final Creator<zzcv> CREATOR = new zzcw();
    @Field(getter = "getVolume", id = 2)
    private double zzei;
    @Field(getter = "getMuteState", id = 3)
    private boolean zzej;
    @Field(getter = "getEqualizerSettings", id = 7)
    private zzad zzvf;
    @Field(getter = "getActiveInputState", id = 4)
    private int zzvg;
    @Field(getter = "getStandbyState", id = 6)
    private int zzvh;
    @Field(getter = "getApplicationMetadata", id = 5)
    private ApplicationMetadata zzvr;

    public zzcv() {
        this(Double.NaN, false, -1, null, -1, null);
    }

    @Constructor
    zzcv(@Param(id = 2) double d, @Param(id = 3) boolean z, @Param(id = 4) int i, @Param(id = 5) ApplicationMetadata applicationMetadata, @Param(id = 6) int i2, @Param(id = 7) zzad zzad) {
        this.zzei = d;
        this.zzej = z;
        this.zzvg = i;
        this.zzvr = applicationMetadata;
        this.zzvh = i2;
        this.zzvf = zzad;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcv)) {
            return false;
        }
        zzcv zzcv = (zzcv) obj;
        return this.zzei == zzcv.zzei && this.zzej == zzcv.zzej && this.zzvg == zzcv.zzvg && zzcu.zza(this.zzvr, zzcv.zzvr) && this.zzvh == zzcv.zzvh && zzcu.zza(this.zzvf, this.zzvf);
    }

    public final int getActiveInputState() {
        return this.zzvg;
    }

    public final ApplicationMetadata getApplicationMetadata() {
        return this.zzvr;
    }

    public final int getStandbyState() {
        return this.zzvh;
    }

    public final double getVolume() {
        return this.zzei;
    }

    public final int hashCode() {
        return Objects.hashCode(Double.valueOf(this.zzei), Boolean.valueOf(this.zzej), Integer.valueOf(this.zzvg), this.zzvr, Integer.valueOf(this.zzvh), this.zzvf);
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeDouble(parcel, 2, this.zzei);
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzej);
        SafeParcelWriter.writeInt(parcel, 4, this.zzvg);
        SafeParcelWriter.writeParcelable(parcel, 5, this.zzvr, i, false);
        SafeParcelWriter.writeInt(parcel, 6, this.zzvh);
        SafeParcelWriter.writeParcelable(parcel, 7, this.zzvf, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final boolean zzcv() {
        return this.zzej;
    }

    public final zzad zzcw() {
        return this.zzvf;
    }
}
