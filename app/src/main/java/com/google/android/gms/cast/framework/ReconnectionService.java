package com.google.android.gms.cast.framework;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.gms.internal.cast.zzdg;
import com.google.android.gms.internal.cast.zze;

public class ReconnectionService extends Service {
    private static final zzdg zzbd = new zzdg("ReconnectionService");
    private zzr zzie;

    public IBinder onBind(Intent intent) {
        try {
            return this.zzie.onBind(intent);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onBind", zzr.class.getSimpleName());
            return null;
        }
    }

    public void onCreate() {
        CastContext sharedInstance = CastContext.getSharedInstance(this);
        this.zzie = zze.zza(this, sharedInstance.getSessionManager().zzr(), sharedInstance.zzq().zzr());
        try {
            this.zzie.onCreate();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onCreate", zzr.class.getSimpleName());
        }
        super.onCreate();
    }

    public void onDestroy() {
        try {
            this.zzie.onDestroy();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onDestroy", zzr.class.getSimpleName());
        }
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        try {
            return this.zzie.onStartCommand(intent, i, i2);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onStartCommand", zzr.class.getSimpleName());
            return 1;
        }
    }
}
