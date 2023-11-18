package com.google.android.gms.cast.framework;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.cast.zzb;

public interface zzt extends IInterface {

    public static abstract class zza extends zzb implements zzt {
        public static zzt zzd(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.cast.framework.ISession");
            return queryLocalInterface instanceof zzt ? (zzt) queryLocalInterface : new zzu(iBinder);
        }
    }

    String getCategory() throws RemoteException;

    String getSessionId() throws RemoteException;

    boolean isConnected() throws RemoteException;

    boolean isConnecting() throws RemoteException;

    boolean isDisconnected() throws RemoteException;

    boolean isDisconnecting() throws RemoteException;

    boolean isResuming() throws RemoteException;

    boolean isSuspended() throws RemoteException;

    void notifyFailedToResumeSession(int i) throws RemoteException;

    void notifyFailedToStartSession(int i) throws RemoteException;

    void notifySessionEnded(int i) throws RemoteException;

    void notifySessionResumed(boolean z) throws RemoteException;

    void notifySessionStarted(String str) throws RemoteException;

    void notifySessionSuspended(int i) throws RemoteException;

    IObjectWrapper zzy() throws RemoteException;
}
