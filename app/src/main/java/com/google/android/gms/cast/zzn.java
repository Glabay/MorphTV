package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.List;

public final class zzn implements Creator<CastDevice> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        String str2 = str;
        String str3 = str2;
        String str4 = str3;
        String str5 = str4;
        List list = str5;
        String str6 = list;
        String str7 = str6;
        String str8 = str7;
        byte[] bArr = str8;
        int i = 0;
        int i2 = 0;
        int i3 = -1;
        int i4 = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 3:
                    str2 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 4:
                    str3 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 5:
                    str4 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 6:
                    str5 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 7:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 8:
                    list = SafeParcelReader.createTypedList(parcel2, readHeader, WebImage.CREATOR);
                    break;
                case 9:
                    i2 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 10:
                    i3 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 11:
                    str6 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 12:
                    str7 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 13:
                    i4 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 14:
                    str8 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 15:
                    bArr = SafeParcelReader.createByteArray(parcel2, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new CastDevice(str, str2, str3, str4, str5, i, list, i2, i3, str6, str7, i4, str8, bArr);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new CastDevice[i];
    }
}
