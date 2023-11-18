package com.google.android.gms.cast.framework;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;
import com.google.android.gms.internal.cast.zza;

public final class zzq extends zza implements zzp {
    zzq(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.framework.IDiscoveryManager");
    }

    public final IObjectWrapper zzx() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(5, obtainAndWriteInterfaceToken());
        IObjectWrapper asInterface = Stub.asInterface(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return asInterface;
    }
}
