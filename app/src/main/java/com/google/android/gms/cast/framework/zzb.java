package com.google.android.gms.cast.framework;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.cast.LaunchOptions;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.List;

public final class zzb implements Creator<CastOptions> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        List list = str;
        LaunchOptions launchOptions = list;
        CastMediaOptions castMediaOptions = launchOptions;
        double d = 0.0d;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 3:
                    list = SafeParcelReader.createStringList(parcel2, readHeader);
                    break;
                case 4:
                    z = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 5:
                    launchOptions = (LaunchOptions) SafeParcelReader.createParcelable(parcel2, readHeader, LaunchOptions.CREATOR);
                    break;
                case 6:
                    z2 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 7:
                    castMediaOptions = (CastMediaOptions) SafeParcelReader.createParcelable(parcel2, readHeader, CastMediaOptions.CREATOR);
                    break;
                case 8:
                    z3 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 9:
                    d = SafeParcelReader.readDouble(parcel2, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new CastOptions(str, list, z, launchOptions, z2, castMediaOptions, z3, d);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new CastOptions[i];
    }
}
