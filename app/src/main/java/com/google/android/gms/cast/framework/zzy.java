package com.google.android.gms.cast.framework;

import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;
import com.google.android.gms.internal.cast.zzb;
import com.google.android.gms.internal.cast.zzc;

public abstract class zzy extends zzb implements zzx {
    public zzy() {
        super("com.google.android.gms.cast.framework.ISessionManagerListener");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                IInterface zzn = zzn();
                parcel2.writeNoException();
                zzc.zza(parcel2, zzn);
                break;
            case 2:
                zza(Stub.asInterface(parcel.readStrongBinder()));
                break;
            case 3:
                zza(Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                break;
            case 4:
                zza(Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                break;
            case 5:
                zzb(Stub.asInterface(parcel.readStrongBinder()));
                break;
            case 6:
                zzb(Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                break;
            case 7:
                zzb(Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                break;
            case 8:
                zza(Stub.asInterface(parcel.readStrongBinder()), zzc.zza(parcel));
                break;
            case 9:
                zzc(Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                break;
            case 10:
                zzd(Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                break;
            case 11:
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
