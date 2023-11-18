package com.google.android.gms.cast.framework;

import android.os.Bundle;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.cast.zzb;
import com.google.android.gms.internal.cast.zzc;

public abstract class zzac extends zzb implements zzab {
    public zzac() {
        super("com.google.android.gms.cast.framework.ISessionProxy");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                IInterface zzaa = zzaa();
                parcel2.writeNoException();
                zzc.zza(parcel2, zzaa);
                break;
            case 2:
                start((Bundle) zzc.zza(parcel, Bundle.CREATOR));
                break;
            case 3:
                resume((Bundle) zzc.zza(parcel, Bundle.CREATOR));
                break;
            case 4:
                end(zzc.zza(parcel));
                break;
            case 5:
                long sessionRemainingTimeMs = getSessionRemainingTimeMs();
                parcel2.writeNoException();
                parcel2.writeLong(sessionRemainingTimeMs);
                break;
            case 6:
                zzm();
                parcel2.writeNoException();
                parcel2.writeInt(12451009);
                break;
            case 7:
                onStarting((Bundle) zzc.zza(parcel, Bundle.CREATOR));
                break;
            case 8:
                onResuming((Bundle) zzc.zza(parcel, Bundle.CREATOR));
                break;
            default:
                return false;
        }
        parcel2.writeNoException();
        return true;
    }
}
