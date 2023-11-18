package com.google.android.gms.cast.framework;

import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

public interface zzab extends IInterface {
    void end(boolean z) throws RemoteException;

    long getSessionRemainingTimeMs() throws RemoteException;

    void onResuming(Bundle bundle) throws RemoteException;

    void onStarting(Bundle bundle) throws RemoteException;

    void resume(Bundle bundle) throws RemoteException;

    void start(Bundle bundle) throws RemoteException;

    IObjectWrapper zzaa() throws RemoteException;

    int zzm() throws RemoteException;
}
