package com.google.android.gms.cast.framework;

import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.internal.cast.zza;
import com.google.android.gms.internal.cast.zzc;

public final class zzs extends zza implements zzr {
    zzs(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.framework.IReconnectionService");
    }

    public final IBinder onBind(Intent intent) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) intent);
        Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        transactAndReadException.recycle();
        return readStrongBinder;
    }

    public final void onCreate() throws RemoteException {
        transactAndReadExceptionReturnVoid(1, obtainAndWriteInterfaceToken());
    }

    public final void onDestroy() throws RemoteException {
        transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken());
    }

    public final int onStartCommand(Intent intent, int i, int i2) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) intent);
        obtainAndWriteInterfaceToken.writeInt(i);
        obtainAndWriteInterfaceToken.writeInt(i2);
        Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken);
        i = transactAndReadException.readInt();
        transactAndReadException.recycle();
        return i;
    }
}
