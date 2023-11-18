package com.google.android.gms.cast;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.List;

public final class zzd implements Creator<ApplicationMetadata> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        String str2 = str;
        List list = str2;
        List list2 = list;
        String str3 = list2;
        Uri uri = str3;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 3:
                    str2 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 4:
                    list = SafeParcelReader.createTypedList(parcel, readHeader, WebImage.CREATOR);
                    break;
                case 5:
                    list2 = SafeParcelReader.createStringList(parcel, readHeader);
                    break;
                case 6:
                    str3 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 7:
                    uri = (Uri) SafeParcelReader.createParcelable(parcel, readHeader, Uri.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new ApplicationMetadata(str, str2, list, list2, str3, uri);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new ApplicationMetadata[i];
    }
}
