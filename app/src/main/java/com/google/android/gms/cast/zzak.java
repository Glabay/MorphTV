package com.google.android.gms.cast;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.List;

public final class zzak implements Creator<MediaMetadata> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        List list = null;
        Bundle bundle = null;
        int i = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    list = SafeParcelReader.createTypedList(parcel, readHeader, WebImage.CREATOR);
                    break;
                case 3:
                    bundle = SafeParcelReader.createBundle(parcel, readHeader);
                    break;
                case 4:
                    i = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new MediaMetadata(list, bundle, i);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new MediaMetadata[i];
    }
}
