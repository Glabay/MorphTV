package com.google.android.gms.cast.framework.media;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import java.util.List;

public abstract class NotificationActionsProvider {
    private final Context zzgs;
    private final zzf zzma = new zza();

    private class zza extends zzg {
        private final /* synthetic */ NotificationActionsProvider zzmb;

        private zza(NotificationActionsProvider notificationActionsProvider) {
            this.zzmb = notificationActionsProvider;
        }

        public final int[] getCompactViewActionIndices() {
            return this.zzmb.getCompactViewActionIndices();
        }

        public final List<NotificationAction> getNotificationActions() {
            return this.zzmb.getNotificationActions();
        }

        public final IObjectWrapper zzaw() {
            return ObjectWrapper.wrap(this.zzmb);
        }

        public final int zzm() {
            return 12451009;
        }
    }

    public NotificationActionsProvider(@NonNull Context context) {
        this.zzgs = context.getApplicationContext();
    }

    public Context getApplicationContext() {
        return this.zzgs;
    }

    public abstract int[] getCompactViewActionIndices();

    public abstract List<NotificationAction> getNotificationActions();

    public final zzf zzbi() {
        return this.zzma;
    }
}
