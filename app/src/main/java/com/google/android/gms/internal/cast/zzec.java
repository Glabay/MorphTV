package com.google.android.gms.internal.cast;

import android.os.IInterface;
import android.os.RemoteException;
import android.view.Surface;

public interface zzec extends IInterface {
    void onDisconnected() throws RemoteException;

    void onError(int i) throws RemoteException;

    void zza(int i, int i2, Surface surface) throws RemoteException;

    void zzc() throws RemoteException;
}
