package com.google.android.gms.internal.cast;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.cast.LaunchOptions;
import com.google.android.gms.cast.zzaf;

public final class zzda extends zza implements zzcz {
    zzda(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.internal.ICastDeviceController");
    }

    public final void disconnect() throws RemoteException {
        transactOneway(1, obtainAndWriteInterfaceToken());
    }

    public final void requestStatus() throws RemoteException {
        transactOneway(6, obtainAndWriteInterfaceToken());
    }

    public final void zza(double d, double d2, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeDouble(d);
        obtainAndWriteInterfaceToken.writeDouble(d2);
        zzc.zza(obtainAndWriteInterfaceToken, z);
        transactOneway(7, obtainAndWriteInterfaceToken);
    }

    public final void zza(String str, String str2, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeLong(j);
        transactOneway(9, obtainAndWriteInterfaceToken);
    }

    public final void zza(String str, String str2, zzaf zzaf) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzaf);
        transactOneway(14, obtainAndWriteInterfaceToken);
    }

    public final void zza(boolean z, double d, boolean z2) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, z);
        obtainAndWriteInterfaceToken.writeDouble(d);
        zzc.zza(obtainAndWriteInterfaceToken, z2);
        transactOneway(8, obtainAndWriteInterfaceToken);
    }

    public final void zzb(String str, LaunchOptions launchOptions) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) launchOptions);
        transactOneway(13, obtainAndWriteInterfaceToken);
    }

    public final void zzcx() throws RemoteException {
        transactOneway(4, obtainAndWriteInterfaceToken());
    }

    public final void zzi(String str) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        transactOneway(5, obtainAndWriteInterfaceToken);
    }

    public final void zzr(String str) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        transactOneway(11, obtainAndWriteInterfaceToken);
    }

    public final void zzs(String str) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        transactOneway(12, obtainAndWriteInterfaceToken);
    }
}
