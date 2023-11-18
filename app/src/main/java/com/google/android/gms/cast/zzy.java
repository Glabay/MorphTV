package com.google.android.gms.cast;

import com.google.android.gms.cast.CastRemoteDisplayLocalService.NotificationSettings;

final class zzy implements Runnable {
    private final /* synthetic */ CastRemoteDisplayLocalService zzcg;
    private final /* synthetic */ NotificationSettings zzcj;

    zzy(CastRemoteDisplayLocalService castRemoteDisplayLocalService, NotificationSettings notificationSettings) {
        this.zzcg = castRemoteDisplayLocalService;
        this.zzcj = notificationSettings;
    }

    public final void run() {
        this.zzcg.zza(this.zzcj);
    }
}
