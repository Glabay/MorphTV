package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.List;

public final class zzan implements Creator<MediaStatus> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        double d = 0.0d;
        double d2 = d;
        long j = 0;
        long j2 = j;
        long j3 = j2;
        MediaInfo mediaInfo = null;
        long[] jArr = mediaInfo;
        String str = jArr;
        List list = str;
        AdBreakStatus adBreakStatus = list;
        VideoInfo videoInfo = adBreakStatus;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        boolean z = false;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        boolean z2 = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    mediaInfo = (MediaInfo) SafeParcelReader.createParcelable(parcel2, readHeader, MediaInfo.CREATOR);
                    break;
                case 3:
                    j = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 4:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 5:
                    d = SafeParcelReader.readDouble(parcel2, readHeader);
                    break;
                case 6:
                    i2 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 7:
                    i3 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 8:
                    j2 = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 9:
                    j3 = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 10:
                    d2 = SafeParcelReader.readDouble(parcel2, readHeader);
                    break;
                case 11:
                    z = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 12:
                    jArr = SafeParcelReader.createLongArray(parcel2, readHeader);
                    break;
                case 13:
                    i4 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 14:
                    i5 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 15:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 16:
                    i6 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 17:
                    list = SafeParcelReader.createTypedList(parcel2, readHeader, MediaQueueItem.CREATOR);
                    break;
                case 18:
                    z2 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 19:
                    adBreakStatus = (AdBreakStatus) SafeParcelReader.createParcelable(parcel2, readHeader, AdBreakStatus.CREATOR);
                    break;
                case 20:
                    videoInfo = (VideoInfo) SafeParcelReader.createParcelable(parcel2, readHeader, VideoInfo.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new MediaStatus(mediaInfo, j, i, d, i2, i3, j2, j3, d2, z, jArr, i4, i5, str, i6, list, z2, adBreakStatus, videoInfo);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new MediaStatus[i];
    }
}
