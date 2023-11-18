package com.google.android.gms.internal.cast;

import android.os.Parcel;
import android.os.RemoteException;

public abstract class zzah extends zzb implements zzag {
    public zzah() {
        super("com.google.android.gms.cast.framework.media.internal.IFetchBitmapTaskProgressPublisher");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                zza(parcel.readLong(), parcel.readLong());
                parcel2.writeNoException();
                break;
            case 2:
                zzm();
                parcel2.writeNoException();
                parcel2.writeInt(12451009);
                break;
            default:
                return false;
        }
        return true;
    }
}
