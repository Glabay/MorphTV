package com.google.android.gms.cast.framework;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;

public final class zzae<T extends Session> extends zzy {
    private final SessionManagerListener<T> zzik;
    private final Class<T> zzil;

    public zzae(@NonNull SessionManagerListener<T> sessionManagerListener, @NonNull Class<T> cls) {
        this.zzik = sessionManagerListener;
        this.zzil = cls;
    }

    public final void zza(@NonNull IObjectWrapper iObjectWrapper) throws RemoteException {
        Session session = (Session) ObjectWrapper.unwrap(iObjectWrapper);
        if (this.zzil.isInstance(session) && this.zzik != null) {
            this.zzik.onSessionStarting((Session) this.zzil.cast(session));
        }
    }

    public final void zza(@NonNull IObjectWrapper iObjectWrapper, int i) throws RemoteException {
        Session session = (Session) ObjectWrapper.unwrap(iObjectWrapper);
        if (this.zzil.isInstance(session) && this.zzik != null) {
            this.zzik.onSessionStartFailed((Session) this.zzil.cast(session), i);
        }
    }

    public final void zza(@NonNull IObjectWrapper iObjectWrapper, String str) throws RemoteException {
        Session session = (Session) ObjectWrapper.unwrap(iObjectWrapper);
        if (this.zzil.isInstance(session) && this.zzik != null) {
            this.zzik.onSessionStarted((Session) this.zzil.cast(session), str);
        }
    }

    public final void zza(@NonNull IObjectWrapper iObjectWrapper, boolean z) throws RemoteException {
        Session session = (Session) ObjectWrapper.unwrap(iObjectWrapper);
        if (this.zzil.isInstance(session) && this.zzik != null) {
            this.zzik.onSessionResumed((Session) this.zzil.cast(session), z);
        }
    }

    public final void zzb(@NonNull IObjectWrapper iObjectWrapper) throws RemoteException {
        Session session = (Session) ObjectWrapper.unwrap(iObjectWrapper);
        if (this.zzil.isInstance(session) && this.zzik != null) {
            this.zzik.onSessionEnding((Session) this.zzil.cast(session));
        }
    }

    public final void zzb(@NonNull IObjectWrapper iObjectWrapper, int i) throws RemoteException {
        Session session = (Session) ObjectWrapper.unwrap(iObjectWrapper);
        if (this.zzil.isInstance(session) && this.zzik != null) {
            this.zzik.onSessionEnded((Session) this.zzil.cast(session), i);
        }
    }

    public final void zzb(@NonNull IObjectWrapper iObjectWrapper, String str) throws RemoteException {
        Session session = (Session) ObjectWrapper.unwrap(iObjectWrapper);
        if (this.zzil.isInstance(session) && this.zzik != null) {
            this.zzik.onSessionResuming((Session) this.zzil.cast(session), str);
        }
    }

    public final void zzc(@NonNull IObjectWrapper iObjectWrapper, int i) throws RemoteException {
        Session session = (Session) ObjectWrapper.unwrap(iObjectWrapper);
        if (this.zzil.isInstance(session) && this.zzik != null) {
            this.zzik.onSessionResumeFailed((Session) this.zzil.cast(session), i);
        }
    }

    public final void zzd(@NonNull IObjectWrapper iObjectWrapper, int i) throws RemoteException {
        Session session = (Session) ObjectWrapper.unwrap(iObjectWrapper);
        if (this.zzil.isInstance(session) && this.zzik != null) {
            this.zzik.onSessionSuspended((Session) this.zzil.cast(session), i);
        }
    }

    public final int zzm() {
        return 12451009;
    }

    public final IObjectWrapper zzn() {
        return ObjectWrapper.wrap(this.zzik);
    }
}
