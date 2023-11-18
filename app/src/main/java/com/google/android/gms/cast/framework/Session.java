package com.google.android.gms.cast.framework;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.cast.zzdg;
import com.google.android.gms.internal.cast.zze;

public abstract class Session {
    private static final zzdg zzbd = new zzdg("Session");
    private final zzt zzif;
    private final zza zzig = new zza();

    private class zza extends zzac {
        private final /* synthetic */ Session zzih;

        private zza(Session session) {
            this.zzih = session;
        }

        public final void end(boolean z) {
            this.zzih.end(z);
        }

        public final long getSessionRemainingTimeMs() {
            return this.zzih.getSessionRemainingTimeMs();
        }

        public final void onResuming(Bundle bundle) {
            this.zzih.onResuming(bundle);
        }

        public final void onStarting(Bundle bundle) {
            this.zzih.onStarting(bundle);
        }

        public final void resume(Bundle bundle) {
            this.zzih.resume(bundle);
        }

        public final void start(Bundle bundle) {
            this.zzih.start(bundle);
        }

        public final IObjectWrapper zzaa() {
            return ObjectWrapper.wrap(this.zzih);
        }

        public final int zzm() {
            return 12451009;
        }
    }

    protected Session(Context context, String str, String str2) {
        this.zzif = zze.zza(context, str, str2, this.zzig);
    }

    protected abstract void end(boolean z);

    public final String getCategory() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return this.zzif.getCategory();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "getCategory", zzt.class.getSimpleName());
            return null;
        }
    }

    public final String getSessionId() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return this.zzif.getSessionId();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "getSessionId", zzt.class.getSimpleName());
            return null;
        }
    }

    public long getSessionRemainingTimeMs() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return 0;
    }

    public boolean isConnected() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return this.zzif.isConnected();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "isConnected", zzt.class.getSimpleName());
            return false;
        }
    }

    public boolean isConnecting() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return this.zzif.isConnecting();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "isConnecting", zzt.class.getSimpleName());
            return false;
        }
    }

    public boolean isDisconnected() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return this.zzif.isDisconnected();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "isDisconnected", zzt.class.getSimpleName());
            return true;
        }
    }

    public boolean isDisconnecting() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return this.zzif.isDisconnecting();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "isDisconnecting", zzt.class.getSimpleName());
            return false;
        }
    }

    public boolean isResuming() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return this.zzif.isResuming();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "isResuming", zzt.class.getSimpleName());
            return false;
        }
    }

    public boolean isSuspended() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return this.zzif.isSuspended();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "isSuspended", zzt.class.getSimpleName());
            return false;
        }
    }

    protected final void notifyFailedToResumeSession(int i) {
        try {
            this.zzif.notifyFailedToResumeSession(i);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "notifyFailedToResumeSession", zzt.class.getSimpleName());
        }
    }

    protected final void notifyFailedToStartSession(int i) {
        try {
            this.zzif.notifyFailedToStartSession(i);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "notifyFailedToStartSession", zzt.class.getSimpleName());
        }
    }

    protected final void notifySessionEnded(int i) {
        try {
            this.zzif.notifySessionEnded(i);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "notifySessionEnded", zzt.class.getSimpleName());
        }
    }

    protected final void notifySessionResumed(boolean z) {
        try {
            this.zzif.notifySessionResumed(z);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "notifySessionResumed", zzt.class.getSimpleName());
        }
    }

    protected final void notifySessionStarted(String str) {
        try {
            this.zzif.notifySessionStarted(str);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "notifySessionStarted", zzt.class.getSimpleName());
        }
    }

    protected final void notifySessionSuspended(int i) {
        try {
            this.zzif.notifySessionSuspended(i);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "notifySessionSuspended", zzt.class.getSimpleName());
        }
    }

    protected void onResuming(Bundle bundle) {
    }

    protected void onStarting(Bundle bundle) {
    }

    protected abstract void resume(Bundle bundle);

    protected abstract void start(Bundle bundle);

    public final IObjectWrapper zzy() {
        try {
            return this.zzif.zzy();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "getWrappedObject", zzt.class.getSimpleName());
            return null;
        }
    }
}
