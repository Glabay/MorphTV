package com.google.android.gms.cast.framework;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

public interface zzz extends IInterface {
    String getCategory() throws RemoteException;

    boolean isSessionRecoverable() throws RemoteException;

    IObjectWrapper zzj(String str) throws RemoteException;

    int zzm() throws RemoteException;
}
