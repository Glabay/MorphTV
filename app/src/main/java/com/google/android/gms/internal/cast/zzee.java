package com.google.android.gms.internal.cast;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;

public interface zzee extends IInterface {
    void disconnect() throws RemoteException;

    void zza(zzec zzec) throws RemoteException;

    void zza(zzec zzec, int i) throws RemoteException;

    void zza(zzec zzec, PendingIntent pendingIntent, String str, String str2, Bundle bundle) throws RemoteException;

    void zza(zzec zzec, zzeg zzeg, String str, String str2, Bundle bundle) throws RemoteException;
}
