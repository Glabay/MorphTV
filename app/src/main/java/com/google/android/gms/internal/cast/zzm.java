package com.google.android.gms.internal.cast;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public final class zzm extends zza implements zzl {
    zzm(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.framework.internal.IMediaRouterCallback");
    }

    public final void zza(String str, Bundle bundle) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        transactAndReadExceptionReturnVoid(1, obtainAndWriteInterfaceToken);
    }

    public final void zza(String str, Bundle bundle, int i) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        obtainAndWriteInterfaceToken.writeInt(i);
        transactAndReadExceptionReturnVoid(6, obtainAndWriteInterfaceToken);
    }

    public final void zzb(String str, Bundle bundle) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
    }

    public final void zzc(String str, Bundle bundle) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        transactAndReadExceptionReturnVoid(3, obtainAndWriteInterfaceToken);
    }

    public final void zzd(String str, Bundle bundle) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken);
    }
}
