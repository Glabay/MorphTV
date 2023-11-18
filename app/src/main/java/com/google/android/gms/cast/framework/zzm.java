package com.google.android.gms.cast.framework;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.internal.cast.zza;
import com.google.android.gms.internal.cast.zzc;

public final class zzm extends zza implements zzl {
    zzm(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.framework.ICastSession");
    }

    public final void onConnected(Bundle bundle) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        transactAndReadExceptionReturnVoid(1, obtainAndWriteInterfaceToken);
    }

    public final void onConnectionFailed(ConnectionResult connectionResult) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) connectionResult);
        transactAndReadExceptionReturnVoid(3, obtainAndWriteInterfaceToken);
    }

    public final void onConnectionSuspended(int i) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeInt(i);
        transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
    }

    public final void zza(ApplicationMetadata applicationMetadata, String str, String str2, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) applicationMetadata);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, z);
        transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken);
    }

    public final void zza(boolean z, int i) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, z);
        obtainAndWriteInterfaceToken.writeInt(0);
        transactAndReadExceptionReturnVoid(6, obtainAndWriteInterfaceToken);
    }

    public final void zzf(int i) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeInt(i);
        transactAndReadExceptionReturnVoid(5, obtainAndWriteInterfaceToken);
    }
}
