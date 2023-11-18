package com.google.android.gms.internal.cast;

import android.os.Parcel;
import android.os.RemoteException;

public abstract class zzeh extends zzb implements zzeg {
    public zzeh() {
        super("com.google.android.gms.cast.remote_display.ICastRemoteDisplaySessionCallbacks");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (i != 1) {
            return false;
        }
        zzr(parcel.readInt());
        return true;
    }
}
