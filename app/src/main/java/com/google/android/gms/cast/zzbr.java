package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzbr implements Creator<TextTrackStyle> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        String str2 = str;
        float f = 0.0f;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    f = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 3:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 4:
                    i2 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 5:
                    i3 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 6:
                    i4 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 7:
                    i5 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 8:
                    i6 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 9:
                    i7 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 10:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 11:
                    i8 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 12:
                    i9 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 13:
                    str2 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new TextTrackStyle(f, i, i2, i3, i4, i5, i6, i7, str, i8, i9, str2);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new TextTrackStyle[i];
    }
}
