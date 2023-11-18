package com.google.android.gms.cast.framework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.cast.zzdg;

public class SessionManager {
    private static final zzdg zzbd = new zzdg("SessionManager");
    private final zzv zzii;
    private final Context zzij;

    public SessionManager(zzv zzv, Context context) {
        this.zzii = zzv;
        this.zzij = context;
    }

    final void addCastStateListener(CastStateListener castStateListener) throws NullPointerException {
        Preconditions.checkNotNull(castStateListener);
        try {
            this.zzii.zza(new zzd(castStateListener));
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "addCastStateListener", zzv.class.getSimpleName());
        }
    }

    public void addSessionManagerListener(SessionManagerListener<Session> sessionManagerListener) throws NullPointerException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        addSessionManagerListener(sessionManagerListener, Session.class);
    }

    public <T extends Session> void addSessionManagerListener(SessionManagerListener<T> sessionManagerListener, Class<T> cls) throws NullPointerException {
        Preconditions.checkNotNull(sessionManagerListener);
        Preconditions.checkNotNull(cls);
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            this.zzii.zza(new zzae(sessionManagerListener, cls));
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "addSessionManagerListener", zzv.class.getSimpleName());
        }
    }

    public void endCurrentSession(boolean z) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            this.zzii.zza(true, z);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "endCurrentSession", zzv.class.getSimpleName());
        }
    }

    final int getCastState() {
        try {
            return this.zzii.getCastState();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "addCastStateListener", zzv.class.getSimpleName());
            return 1;
        }
    }

    public CastSession getCurrentCastSession() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        Session currentSession = getCurrentSession();
        return (currentSession == null || !(currentSession instanceof CastSession)) ? null : (CastSession) currentSession;
    }

    public Session getCurrentSession() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return (Session) ObjectWrapper.unwrap(this.zzii.zzz());
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "getWrappedCurrentSession", zzv.class.getSimpleName());
            return null;
        }
    }

    final void removeCastStateListener(CastStateListener castStateListener) {
        if (castStateListener != null) {
            try {
                this.zzii.zzb(new zzd(castStateListener));
            } catch (Throwable e) {
                zzbd.zza(e, "Unable to call %s on %s.", "removeCastStateListener", zzv.class.getSimpleName());
            }
        }
    }

    public void removeSessionManagerListener(SessionManagerListener<Session> sessionManagerListener) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        removeSessionManagerListener(sessionManagerListener, Session.class);
    }

    public <T extends Session> void removeSessionManagerListener(SessionManagerListener<T> sessionManagerListener, Class cls) {
        Preconditions.checkNotNull(cls);
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (sessionManagerListener != null) {
            try {
                this.zzii.zzb(new zzae(sessionManagerListener, cls));
            } catch (Throwable e) {
                zzbd.zza(e, "Unable to call %s on %s.", "removeSessionManagerListener", zzv.class.getSimpleName());
            }
        }
    }

    public void startSession(Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.getString("CAST_INTENT_TO_CAST_ROUTE_ID_KEY") != null) {
                String string = extras.getString("CAST_INTENT_TO_CAST_DEVICE_NAME_KEY");
                if (!extras.getBoolean("CAST_INTENT_TO_CAST_NO_TOAST_KEY")) {
                    Toast.makeText(this.zzij, this.zzij.getString(C0782R.string.cast_connecting_to_device, new Object[]{string}), 0).show();
                }
                this.zzii.zzc(new Bundle(extras));
                intent.removeExtra("CAST_INTENT_TO_CAST_ROUTE_ID_KEY");
            }
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "startSession", zzv.class.getSimpleName());
        }
    }

    public final IObjectWrapper zzr() {
        try {
            return this.zzii.zzx();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "getWrappedThis", zzv.class.getSimpleName());
            return null;
        }
    }
}
