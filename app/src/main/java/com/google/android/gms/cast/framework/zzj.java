package com.google.android.gms.cast.framework;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.cast.zzb;
import java.util.Map;

public interface zzj extends IInterface {

    public static abstract class zza extends zzb implements zzj {
        public static zzj zza(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.cast.framework.ICastContext");
            return queryLocalInterface instanceof zzj ? (zzj) queryLocalInterface : new zzk(iBinder);
        }
    }

    boolean isAppVisible() throws RemoteException;

    void zza(zzf zzf) throws RemoteException;

    void zza(String str, Map map) throws RemoteException;

    void zzb(zzf zzf) throws RemoteException;

    Bundle zzu() throws RemoteException;

    zzv zzv() throws RemoteException;

    zzp zzw() throws RemoteException;

    IObjectWrapper zzx() throws RemoteException;
}
