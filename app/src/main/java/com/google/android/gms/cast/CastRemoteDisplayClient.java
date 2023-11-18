package com.google.android.gms.cast;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.hardware.display.VirtualDisplay;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Surface;
import com.google.android.gms.cast.CastRemoteDisplay.Configuration;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApi.Settings;
import com.google.android.gms.internal.cast.zzdf;
import com.google.android.gms.internal.cast.zzdg;
import com.google.android.gms.internal.cast.zzdz;
import com.google.android.gms.internal.cast.zzed;
import com.google.android.gms.tasks.Task;

@TargetApi(19)
public class CastRemoteDisplayClient extends GoogleApi<NoOptions> {
    private static final Api<NoOptions> API = new Api("CastRemoteDisplay.API", CLIENT_BUILDER, zzdf.zzwf);
    private static final AbstractClientBuilder<zzdz, NoOptions> CLIENT_BUILDER = new zzp();
    private final zzdg zzbd = new zzdg("CastRemoteDisplay");
    private VirtualDisplay zzbe;

    private static class zza extends zzed {
        private zza() {
        }

        public void onDisconnected() throws RemoteException {
            throw new UnsupportedOperationException();
        }

        public void onError(int i) throws RemoteException {
            throw new UnsupportedOperationException();
        }

        public void zza(int i, int i2, Surface surface) throws RemoteException {
            throw new UnsupportedOperationException();
        }

        public void zzc() throws RemoteException {
            throw new UnsupportedOperationException();
        }
    }

    CastRemoteDisplayClient(@NonNull Context context) {
        super(context, API, null, Settings.DEFAULT_SETTINGS);
    }

    @TargetApi(19)
    private final void a_() {
        if (this.zzbe != null) {
            if (this.zzbe.getDisplay() != null) {
                zzdg zzdg = this.zzbd;
                int displayId = this.zzbe.getDisplay().getDisplayId();
                StringBuilder stringBuilder = new StringBuilder(38);
                stringBuilder.append("releasing virtual display: ");
                stringBuilder.append(displayId);
                zzdg.m25d(stringBuilder.toString(), new Object[0]);
            }
            this.zzbe.release();
            this.zzbe = null;
        }
    }

    private static int zza(int i, int i2) {
        return (Math.min(i, i2) * 320) / 1080;
    }

    public Task<Display> startRemoteDisplay(@NonNull CastDevice castDevice, @NonNull String str, @Configuration int i, @Nullable PendingIntent pendingIntent) {
        return doWrite(new zzq(this, i, pendingIntent, castDevice, str));
    }

    public Task<Void> stopRemoteDisplay() {
        return doWrite(new zzs(this));
    }
}
