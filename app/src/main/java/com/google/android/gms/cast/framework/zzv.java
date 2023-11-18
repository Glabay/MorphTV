package com.google.android.gms.cast.framework;

import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

public interface zzv extends IInterface {
    int getCastState() throws RemoteException;

    void zza(zzn zzn) throws RemoteException;

    void zza(zzx zzx) throws RemoteException;

    void zza(boolean z, boolean z2) throws RemoteException;

    void zzb(zzn zzn) throws RemoteException;

    void zzb(zzx zzx) throws RemoteException;

    void zzc(Bundle bundle) throws RemoteException;

    IObjectWrapper zzx() throws RemoteException;

    IObjectWrapper zzz() throws RemoteException;
}
