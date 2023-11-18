package com.google.android.gms.cast.framework;

import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.internal.cast.zzb;

public interface zzr extends IInterface {

    public static abstract class zza extends zzb implements zzr {
        public static zzr zzc(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.cast.framework.IReconnectionService");
            return queryLocalInterface instanceof zzr ? (zzr) queryLocalInterface : new zzs(iBinder);
        }
    }

    IBinder onBind(Intent intent) throws RemoteException;

    void onCreate() throws RemoteException;

    void onDestroy() throws RemoteException;

    int onStartCommand(Intent intent, int i, int i2) throws RemoteException;
}
