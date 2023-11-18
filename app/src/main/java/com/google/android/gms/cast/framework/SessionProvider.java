package com.google.android.gms.cast.framework;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.IObjectWrapper;

public abstract class SessionProvider {
    private final String category;
    private final Context zzim;
    private final zza zzin = new zza();

    private class zza extends zzaa {
        private final /* synthetic */ SessionProvider zzio;

        private zza(SessionProvider sessionProvider) {
            this.zzio = sessionProvider;
        }

        public final String getCategory() {
            return this.zzio.getCategory();
        }

        public final boolean isSessionRecoverable() {
            return this.zzio.isSessionRecoverable();
        }

        public final IObjectWrapper zzj(String str) {
            Session createSession = this.zzio.createSession(str);
            return createSession == null ? null : createSession.zzy();
        }

        public final int zzm() {
            return 12451009;
        }
    }

    protected SessionProvider(Context context, String str) {
        this.zzim = ((Context) Preconditions.checkNotNull(context)).getApplicationContext();
        this.category = Preconditions.checkNotEmpty(str);
    }

    public abstract Session createSession(String str);

    public final String getCategory() {
        return this.category;
    }

    public final Context getContext() {
        return this.zzim;
    }

    public abstract boolean isSessionRecoverable();

    public final IBinder zzai() {
        return this.zzin;
    }
}
