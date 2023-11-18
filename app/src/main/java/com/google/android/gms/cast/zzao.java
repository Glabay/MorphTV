package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzao implements Creator<MediaTrack> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        String str2 = str;
        String str3 = str2;
        String str4 = str3;
        String str5 = str4;
        long j = 0;
        int i = 0;
        int i2 = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    j = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 3:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 4:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 5:
                    str2 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 6:
                    str3 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 7:
                    str4 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 8:
                    i2 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 9:
                    str5 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new MediaTrack(j, i, str, str2, str3, str4, i2, str5);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new MediaTrack[i];
    }
}
