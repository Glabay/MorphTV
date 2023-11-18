package com.google.android.gms.cast.framework;

import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.cast.zzb;
import com.google.android.gms.internal.cast.zzc;

public abstract class zzo extends zzb implements zzn {
    public zzo() {
        super("com.google.android.gms.cast.framework.ICastStateListener");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                IInterface zzn = zzn();
                parcel2.writeNoException();
                zzc.zza(parcel2, zzn);
                break;
            case 2:
                onCastStateChanged(parcel.readInt());
                parcel2.writeNoException();
                break;
            case 3:
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
