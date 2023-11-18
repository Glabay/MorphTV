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
import io.github.morpheustv.scrapers.model.BaseProvider;

@Class(creator = "JoinOptionsCreator")
@Reserved({1})
public final class zzaf extends AbstractSafeParcelable {
    public static final Creator<zzaf> CREATOR = new zzag();
    @Field(getter = "getConnectionType", id = 2)
    private int zzcw;

    public zzaf() {
        this(0);
    }

    @Constructor
    zzaf(@Param(id = 2) int i) {
        this.zzcw = i;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzaf)) {
            return false;
        }
        return this.zzcw == ((zzaf) obj).zzcw;
    }

    public final int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.zzcw));
    }

    public final String toString() {
        int i = this.zzcw;
        String str = i != 0 ? i != 2 ? BaseProvider.UNKNOWN_QUALITY : "INVISIBLE" : "STRONG";
        return String.format("joinOptions(connectionType=%s)", new Object[]{str});
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.zzcw);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
