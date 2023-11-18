package com.google.android.gms.internal.cast;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public final class zzef extends zza implements zzee {
    zzef(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
    }

    public final void disconnect() throws RemoteException {
        transactOneway(3, obtainAndWriteInterfaceToken());
    }

    public final void zza(zzec zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzec);
        transactOneway(6, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzec zzec, int i) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzec);
        obtainAndWriteInterfaceToken.writeInt(i);
        transactOneway(5, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzec zzec, PendingIntent pendingIntent, String str, String str2, Bundle bundle) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzec);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) pendingIntent);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        transactOneway(8, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzec zzec, zzeg zzeg, String str, String str2, Bundle bundle) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzec);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzeg);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        transactOneway(7, obtainAndWriteInterfaceToken);
    }
}
