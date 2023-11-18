package com.google.android.gms.cast.framework.media;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.cast.zzdg;
import com.google.android.gms.internal.cast.zze;

public class MediaNotificationService extends Service {
    public static final String ACTION_UPDATE_NOTIFICATION = "com.google.android.gms.cast.framework.action.UPDATE_NOTIFICATION";
    private static final zzdg zzbd = new zzdg("MediaNotificationService");
    private zzd zzlb;

    public IBinder onBind(Intent intent) {
        try {
            return this.zzlb.onBind(intent);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onBind", zzd.class.getSimpleName());
            return null;
        }
    }

    public void onCreate() {
        this.zzlb = zze.zza((Service) this, CastContext.getSharedInstance(this).zzr(), ObjectWrapper.wrap(null), CastContext.getSharedInstance(this).getCastOptions().getCastMediaOptions());
        try {
            this.zzlb.onCreate();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onCreate", zzd.class.getSimpleName());
        }
        super.onCreate();
    }

    public void onDestroy() {
        try {
            this.zzlb.onDestroy();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onDestroy", zzd.class.getSimpleName());
        }
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        try {
            return this.zzlb.onStartCommand(intent, i, i2);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onStartCommand", zzd.class.getSimpleName());
            return 1;
        }
    }
}
