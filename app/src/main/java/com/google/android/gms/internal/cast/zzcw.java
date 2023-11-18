package com.google.android.gms.internal.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.zzad;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzcw implements Creator<zzcv> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        ApplicationMetadata applicationMetadata = null;
        zzad zzad = applicationMetadata;
        double d = 0.0d;
        boolean z = false;
        int i = 0;
        int i2 = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    d = SafeParcelReader.readDouble(parcel, readHeader);
                    break;
                case 3:
                    z = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 4:
                    i = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 5:
                    applicationMetadata = (ApplicationMetadata) SafeParcelReader.createParcelable(parcel, readHeader, ApplicationMetadata.CREATOR);
                    break;
                case 6:
                    i2 = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 7:
                    zzad = (zzad) SafeParcelReader.createParcelable(parcel, readHeader, zzad.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzcv(d, z, i, applicationMetadata, i2, zzad);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzcv[i];
    }
}
