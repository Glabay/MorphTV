package com.google.android.gms.internal.cast;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.cast.ApplicationMetadata;

public abstract class zzdc extends zzb implements zzdb {
    public zzdc() {
        super("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                zzn(parcel.readInt());
                break;
            case 2:
                zza((ApplicationMetadata) zzc.zza(parcel, ApplicationMetadata.CREATOR), parcel.readString(), parcel.readString(), zzc.zza(parcel));
                break;
            case 3:
                zzf(parcel.readInt());
                break;
            case 4:
                zza(parcel.readString(), parcel.readDouble(), zzc.zza(parcel));
                break;
            case 5:
                zzb(parcel.readString(), parcel.readString());
                break;
            case 6:
                zza(parcel.readString(), parcel.createByteArray());
                break;
            case 7:
                zzp(parcel.readInt());
                break;
            case 8:
                zzo(parcel.readInt());
                break;
            case 9:
                onApplicationDisconnected(parcel.readInt());
                break;
            case 10:
                zza(parcel.readString(), parcel.readLong(), parcel.readInt());
                break;
            case 11:
                zza(parcel.readString(), parcel.readLong());
                break;
            case 12:
                zzb((zzcd) zzc.zza(parcel, zzcd.CREATOR));
                break;
            case 13:
                zzb((zzcv) zzc.zza(parcel, zzcv.CREATOR));
                break;
            default:
                return false;
        }
        return true;
    }
}
