package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzae implements Creator<zzad> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        zzab zzab = null;
        zzab zzab2 = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    zzab = (zzab) SafeParcelReader.createParcelable(parcel, readHeader, zzab.CREATOR);
                    break;
                case 3:
                    zzab2 = (zzab) SafeParcelReader.createParcelable(parcel, readHeader, zzab.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzad(zzab, zzab2);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzad[i];
    }
}
