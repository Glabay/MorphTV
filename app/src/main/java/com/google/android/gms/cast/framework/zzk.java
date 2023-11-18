package com.google.android.gms.cast.framework;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;
import com.google.android.gms.internal.cast.zza;
import com.google.android.gms.internal.cast.zzc;
import java.util.Map;

public final class zzk extends zza implements zzj {
    zzk(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.framework.ICastContext");
    }

    public final boolean isAppVisible() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken());
        boolean zza = zzc.zza(transactAndReadException);
        transactAndReadException.recycle();
        return zza;
    }

    public final void zza(zzf zzf) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzf);
        transactAndReadExceptionReturnVoid(3, obtainAndWriteInterfaceToken);
    }

    public final void zza(String str, Map map) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeMap(map);
        transactAndReadExceptionReturnVoid(11, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzf zzf) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzf);
        transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken);
    }

    public final Bundle zzu() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken());
        Bundle bundle = (Bundle) zzc.zza(transactAndReadException, Bundle.CREATOR);
        transactAndReadException.recycle();
        return bundle;
    }

    public final zzv zzv() throws RemoteException {
        zzv zzv;
        Parcel transactAndReadException = transactAndReadException(5, obtainAndWriteInterfaceToken());
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzv = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.cast.framework.ISessionManager");
            zzv = queryLocalInterface instanceof zzv ? (zzv) queryLocalInterface : new zzw(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzv;
    }

    public final zzp zzw() throws RemoteException {
        zzp zzp;
        Parcel transactAndReadException = transactAndReadException(6, obtainAndWriteInterfaceToken());
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzp = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.cast.framework.IDiscoveryManager");
            zzp = queryLocalInterface instanceof zzp ? (zzp) queryLocalInterface : new zzq(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzp;
    }

    public final IObjectWrapper zzx() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(10, obtainAndWriteInterfaceToken());
        IObjectWrapper asInterface = Stub.asInterface(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return asInterface;
    }
}
