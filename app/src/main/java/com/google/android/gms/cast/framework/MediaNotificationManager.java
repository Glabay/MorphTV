package com.google.android.gms.cast.framework;

import android.support.annotation.NonNull;

public class MediaNotificationManager {
    private final SessionManager zzgu;

    public MediaNotificationManager(@NonNull SessionManager sessionManager) {
        this.zzgu = sessionManager;
    }

    public void updateNotification() {
        CastSession currentCastSession = this.zzgu.getCurrentCastSession();
        if (currentCastSession != null) {
            currentCastSession.zzs().zzg(true);
        }
    }
}
