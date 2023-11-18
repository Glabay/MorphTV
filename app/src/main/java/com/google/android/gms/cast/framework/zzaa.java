package com.google.android.gms.cast.framework;

import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.cast.zzb;
import com.google.android.gms.internal.cast.zzc;

public abstract class zzaa extends zzb implements zzz {
    public zzaa() {
        super("com.google.android.gms.cast.framework.ISessionProvider");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                IInterface zzj = zzj(parcel.readString());
                parcel2.writeNoException();
                zzc.zza(parcel2, zzj);
                break;
            case 2:
                boolean isSessionRecoverable = isSessionRecoverable();
                parcel2.writeNoException();
                zzc.zza(parcel2, isSessionRecoverable);
                break;
            case 3:
                String category = getCategory();
                parcel2.writeNoException();
                parcel2.writeString(category);
                break;
            case 4:
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
