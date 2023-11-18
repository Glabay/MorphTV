package com.google.android.gms.cast.framework.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;

@Class(creator = "ImageHintsCreator")
@Reserved({1})
public class ImageHints extends AbstractSafeParcelable {
    public static final Creator<ImageHints> CREATOR = new zzi();
    @Field(getter = "getType", id = 2)
    private final int type;
    @Field(getter = "getWidthInPixels", id = 3)
    private final int zzkx;
    @Field(getter = "getHeightInPixels", id = 4)
    private final int zzky;

    @Constructor
    public ImageHints(@Param(id = 2) int i, @Param(id = 3) int i2, @Param(id = 4) int i3) {
        this.type = i;
        this.zzkx = i2;
        this.zzky = i3;
    }

    public int getHeightInPixels() {
        return this.zzky;
    }

    public int getType() {
        return this.type;
    }

    public int getWidthInPixels() {
        return this.zzkx;
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, getType());
        SafeParcelWriter.writeInt(parcel, 3, getWidthInPixels());
        SafeParcelWriter.writeInt(parcel, 4, getHeightInPixels());
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
