package com.google.android.gms.cast.framework;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.internal.cast.zzb;

public interface zzl extends IInterface {

    public static abstract class zza extends zzb implements zzl {
        public static zzl zzb(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.cast.framework.ICastSession");
            return queryLocalInterface instanceof zzl ? (zzl) queryLocalInterface : new zzm(iBinder);
        }
    }

    void onConnected(Bundle bundle) throws RemoteException;

    void onConnectionFailed(ConnectionResult connectionResult) throws RemoteException;

    void onConnectionSuspended(int i) throws RemoteException;

    void zza(ApplicationMetadata applicationMetadata, String str, String str2, boolean z) throws RemoteException;

    void zza(boolean z, int i) throws RemoteException;

    void zzf(int i) throws RemoteException;
}
