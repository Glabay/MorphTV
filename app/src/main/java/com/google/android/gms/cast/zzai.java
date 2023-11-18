package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.List;

public final class zzai implements Creator<MediaInfo> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        String str2 = str;
        MediaMetadata mediaMetadata = str2;
        List list = mediaMetadata;
        TextTrackStyle textTrackStyle = list;
        String str3 = textTrackStyle;
        List list2 = str3;
        List list3 = list2;
        String str4 = list3;
        long j = 0;
        int i = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 3:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 4:
                    str2 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 5:
                    mediaMetadata = (MediaMetadata) SafeParcelReader.createParcelable(parcel2, readHeader, MediaMetadata.CREATOR);
                    break;
                case 6:
                    j = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 7:
                    list = SafeParcelReader.createTypedList(parcel2, readHeader, MediaTrack.CREATOR);
                    break;
                case 8:
                    textTrackStyle = (TextTrackStyle) SafeParcelReader.createParcelable(parcel2, readHeader, TextTrackStyle.CREATOR);
                    break;
                case 9:
                    str3 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 10:
                    list2 = SafeParcelReader.createTypedList(parcel2, readHeader, AdBreakInfo.CREATOR);
                    break;
                case 11:
                    list3 = SafeParcelReader.createTypedList(parcel2, readHeader, AdBreakClipInfo.CREATOR);
                    break;
                case 12:
                    str4 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new MediaInfo(str, i, str2, mediaMetadata, j, list, textTrackStyle, str3, list2, list3, str4);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new MediaInfo[i];
    }
}
