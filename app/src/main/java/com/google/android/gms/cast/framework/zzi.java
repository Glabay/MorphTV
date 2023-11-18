package com.google.android.gms.cast.framework;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.cast.LaunchOptions;
import com.google.android.gms.internal.cast.zzb;
import com.google.android.gms.internal.cast.zzc;

public abstract class zzi extends zzb implements zzh {
    public zzi() {
        super("com.google.android.gms.cast.framework.ICastConnectionController");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                zza(parcel.readString(), parcel.readString());
                break;
            case 2:
                zza(parcel.readString(), (LaunchOptions) zzc.zza(parcel, LaunchOptions.CREATOR));
                break;
            case 3:
                zzi(parcel.readString());
                break;
            case 4:
                zze(parcel.readInt());
                break;
            case 5:
                zzm();
                parcel2.writeNoException();
                parcel2.writeInt(12451009);
                break;
            default:
                return false;
        }
        parcel2.writeNoException();
        return true;
    }
}
