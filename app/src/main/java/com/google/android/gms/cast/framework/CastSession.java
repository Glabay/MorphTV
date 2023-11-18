package com.google.android.gms.cast.framework;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.Cast.ApplicationConnectionResult;
import com.google.android.gms.cast.Cast.CastApi;
import com.google.android.gms.cast.Cast.CastOptions;
import com.google.android.gms.cast.Cast.Listener;
import com.google.android.gms.cast.Cast.MessageReceivedCallback;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.LaunchOptions;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.cast.zzai;
import com.google.android.gms.internal.cast.zzdg;
import com.google.android.gms.internal.cast.zzdh;
import com.google.android.gms.internal.cast.zze;
import com.google.android.gms.internal.cast.zzg;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CastSession extends Session {
    private static final zzdg zzbd = new zzdg("CastSession");
    private final Context zzgs;
    private final CastOptions zzgy;
    private final Set<Listener> zzhj = new HashSet();
    private final zzl zzhk;
    private final CastApi zzhl;
    private final zzg zzhm;
    private final zzai zzhn;
    private GoogleApiClient zzho;
    private RemoteMediaClient zzhp;
    private CastDevice zzhq;
    private ApplicationConnectionResult zzhr;

    private class zza implements ResultCallback<ApplicationConnectionResult> {
        private String command;
        private final /* synthetic */ CastSession zzhs;

        zza(CastSession castSession, String str) {
            this.zzhs = castSession;
            this.command = str;
        }

        public final /* synthetic */ void onResult(@NonNull Result result) {
            ApplicationConnectionResult applicationConnectionResult = (ApplicationConnectionResult) result;
            this.zzhs.zzhr = applicationConnectionResult;
            try {
                if (applicationConnectionResult.getStatus().isSuccess()) {
                    CastSession.zzbd.m25d("%s() -> success result", this.command);
                    this.zzhs.zzhp = new RemoteMediaClient(new zzdh(null), this.zzhs.zzhl);
                    try {
                        this.zzhs.zzhp.zzb(this.zzhs.zzho);
                        this.zzhs.zzhp.zzbl();
                        this.zzhs.zzhp.requestStatus();
                        this.zzhs.zzhn.zza(this.zzhs.zzhp, this.zzhs.getCastDevice());
                    } catch (Throwable e) {
                        CastSession.zzbd.zzc(e, "Exception when setting GoogleApiClient.", new Object[0]);
                        this.zzhs.zzhp = null;
                    }
                    this.zzhs.zzhk.zza(applicationConnectionResult.getApplicationMetadata(), applicationConnectionResult.getApplicationStatus(), applicationConnectionResult.getSessionId(), applicationConnectionResult.getWasLaunched());
                    return;
                }
                CastSession.zzbd.m25d("%s() -> failure result", this.command);
                this.zzhs.zzhk.zzf(applicationConnectionResult.getStatus().getStatusCode());
            } catch (Throwable e2) {
                CastSession.zzbd.zza(e2, "Unable to call %s on %s.", "methods", zzl.class.getSimpleName());
            }
        }
    }

    private class zzb extends zzi {
        private final /* synthetic */ CastSession zzhs;

        private zzb(CastSession castSession) {
            this.zzhs = castSession;
        }

        public final void zza(String str, LaunchOptions launchOptions) {
            if (this.zzhs.zzho != null) {
                this.zzhs.zzhl.launchApplication(this.zzhs.zzho, str, launchOptions).setResultCallback(new zza(this.zzhs, "launchApplication"));
            }
        }

        public final void zza(String str, String str2) {
            if (this.zzhs.zzho != null) {
                this.zzhs.zzhl.joinApplication(this.zzhs.zzho, str, str2).setResultCallback(new zza(this.zzhs, "joinApplication"));
            }
        }

        public final void zze(int i) {
            this.zzhs.zze(i);
        }

        public final void zzi(String str) {
            if (this.zzhs.zzho != null) {
                this.zzhs.zzhl.stopApplication(this.zzhs.zzho, str);
            }
        }

        public final int zzm() {
            return 12451009;
        }
    }

    private class zzc extends Listener {
        private final /* synthetic */ CastSession zzhs;

        private zzc(CastSession castSession) {
            this.zzhs = castSession;
        }

        public final void onActiveInputStateChanged(int i) {
            for (Listener onActiveInputStateChanged : new HashSet(this.zzhs.zzhj)) {
                onActiveInputStateChanged.onActiveInputStateChanged(i);
            }
        }

        public final void onApplicationDisconnected(int i) {
            this.zzhs.zze(i);
            this.zzhs.notifySessionEnded(i);
            for (Listener onApplicationDisconnected : new HashSet(this.zzhs.zzhj)) {
                onApplicationDisconnected.onApplicationDisconnected(i);
            }
        }

        public final void onApplicationMetadataChanged(ApplicationMetadata applicationMetadata) {
            for (Listener onApplicationMetadataChanged : new HashSet(this.zzhs.zzhj)) {
                onApplicationMetadataChanged.onApplicationMetadataChanged(applicationMetadata);
            }
        }

        public final void onApplicationStatusChanged() {
            for (Listener onApplicationStatusChanged : new HashSet(this.zzhs.zzhj)) {
                onApplicationStatusChanged.onApplicationStatusChanged();
            }
        }

        public final void onStandbyStateChanged(int i) {
            for (Listener onStandbyStateChanged : new HashSet(this.zzhs.zzhj)) {
                onStandbyStateChanged.onStandbyStateChanged(i);
            }
        }

        public final void onVolumeChanged() {
            for (Listener onVolumeChanged : new HashSet(this.zzhs.zzhj)) {
                onVolumeChanged.onVolumeChanged();
            }
        }
    }

    private class zzd implements ConnectionCallbacks, OnConnectionFailedListener {
        private final /* synthetic */ CastSession zzhs;

        private zzd(CastSession castSession) {
            this.zzhs = castSession;
        }

        public final void onConnected(Bundle bundle) {
            try {
                if (this.zzhs.zzhp != null) {
                    try {
                        this.zzhs.zzhp.zzbl();
                        this.zzhs.zzhp.requestStatus();
                    } catch (Throwable e) {
                        CastSession.zzbd.zzc(e, "Exception when setting GoogleApiClient.", new Object[0]);
                        this.zzhs.zzhp = null;
                    }
                }
                this.zzhs.zzhk.onConnected(bundle);
            } catch (Throwable e2) {
                CastSession.zzbd.zza(e2, "Unable to call %s on %s.", "onConnected", zzl.class.getSimpleName());
            }
        }

        public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            try {
                this.zzhs.zzhk.onConnectionFailed(connectionResult);
            } catch (Throwable e) {
                CastSession.zzbd.zza(e, "Unable to call %s on %s.", "onConnectionFailed", zzl.class.getSimpleName());
            }
        }

        public final void onConnectionSuspended(int i) {
            try {
                this.zzhs.zzhk.onConnectionSuspended(i);
            } catch (Throwable e) {
                CastSession.zzbd.zza(e, "Unable to call %s on %s.", "onConnectionSuspended", zzl.class.getSimpleName());
            }
        }
    }

    public CastSession(Context context, String str, String str2, CastOptions castOptions, CastApi castApi, zzg zzg, zzai zzai) {
        super(context, str, str2);
        this.zzgs = context.getApplicationContext();
        this.zzgy = castOptions;
        this.zzhl = castApi;
        this.zzhm = zzg;
        this.zzhn = zzai;
        this.zzhk = zze.zza(context, castOptions, zzy(), new zzb());
    }

    private final void zzb(Bundle bundle) {
        this.zzhq = CastDevice.getFromBundle(bundle);
        if (this.zzhq != null) {
            if (this.zzho != null) {
                this.zzho.disconnect();
                this.zzho = null;
            }
            boolean z = true;
            zzbd.m25d("Acquiring a connection to Google Play Services for %s", this.zzhq);
            Object zzd = new zzd();
            Context context = this.zzgs;
            CastDevice castDevice = this.zzhq;
            CastOptions castOptions = this.zzgy;
            Listener zzc = new zzc();
            Bundle bundle2 = new Bundle();
            String str = "com.google.android.gms.cast.EXTRA_CAST_FRAMEWORK_NOTIFICATION_ENABLED";
            boolean z2 = (castOptions == null || castOptions.getCastMediaOptions() == null || castOptions.getCastMediaOptions().getNotificationOptions() == null) ? false : true;
            bundle2.putBoolean(str, z2);
            str = "com.google.android.gms.cast.EXTRA_CAST_REMOTE_CONTROL_NOTIFICATION_ENABLED";
            if (castOptions == null || castOptions.getCastMediaOptions() == null || !castOptions.getCastMediaOptions().zzav()) {
                z = false;
            }
            bundle2.putBoolean(str, z);
            this.zzho = new Builder(context).addApi(Cast.API, new CastOptions.Builder(castDevice, zzc).zza(bundle2).build()).addConnectionCallbacks(zzd).addOnConnectionFailedListener(zzd).build();
            this.zzho.connect();
        } else if (isResuming()) {
            notifyFailedToResumeSession(8);
        } else {
            notifyFailedToStartSession(8);
        }
    }

    private final void zze(int i) {
        this.zzhn.zzi(i);
        if (this.zzho != null) {
            this.zzho.disconnect();
            this.zzho = null;
        }
        this.zzhq = null;
        if (this.zzhp != null) {
            this.zzhp.zzb(null);
            this.zzhp = null;
        }
        this.zzhr = null;
    }

    public void addCastListener(Listener listener) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (listener != null) {
            this.zzhj.add(listener);
        }
    }

    protected void end(boolean z) {
        try {
            this.zzhk.zza(z, 0);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "disconnectFromDevice", zzl.class.getSimpleName());
        }
        notifySessionEnded(0);
    }

    public int getActiveInputState() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzho != null ? this.zzhl.getActiveInputState(this.zzho) : -1;
    }

    public ApplicationConnectionResult getApplicationConnectionResult() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzhr;
    }

    public ApplicationMetadata getApplicationMetadata() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzho != null ? this.zzhl.getApplicationMetadata(this.zzho) : null;
    }

    public String getApplicationStatus() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzho != null ? this.zzhl.getApplicationStatus(this.zzho) : null;
    }

    public CastDevice getCastDevice() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzhq;
    }

    public RemoteMediaClient getRemoteMediaClient() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzhp;
    }

    public long getSessionRemainingTimeMs() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzhp == null ? 0 : this.zzhp.getStreamDuration() - this.zzhp.getApproximateStreamPosition();
    }

    public int getStandbyState() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzho != null ? this.zzhl.getStandbyState(this.zzho) : -1;
    }

    public double getVolume() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzho != null ? this.zzhl.getVolume(this.zzho) : 0.0d;
    }

    public boolean isMute() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzho != null ? this.zzhl.isMute(this.zzho) : false;
    }

    protected void onResuming(Bundle bundle) {
        this.zzhq = CastDevice.getFromBundle(bundle);
    }

    protected void onStarting(Bundle bundle) {
        this.zzhq = CastDevice.getFromBundle(bundle);
    }

    public void removeCastListener(Listener listener) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (listener != null) {
            this.zzhj.remove(listener);
        }
    }

    public void removeMessageReceivedCallbacks(String str) throws IOException, IllegalArgumentException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (this.zzho != null) {
            this.zzhl.removeMessageReceivedCallbacks(this.zzho, str);
        }
    }

    public void requestStatus() throws IOException, IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (this.zzho != null) {
            this.zzhl.requestStatus(this.zzho);
        }
    }

    protected void resume(Bundle bundle) {
        zzb(bundle);
    }

    public PendingResult<Status> sendMessage(String str, String str2) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzho != null ? this.zzhl.sendMessage(this.zzho, str, str2) : null;
    }

    public void setMessageReceivedCallbacks(String str, MessageReceivedCallback messageReceivedCallback) throws IOException, IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (this.zzho != null) {
            this.zzhl.setMessageReceivedCallbacks(this.zzho, str, messageReceivedCallback);
        }
    }

    public void setMute(boolean z) throws IOException, IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (this.zzho != null) {
            this.zzhl.setMute(this.zzho, z);
        }
    }

    public void setVolume(double d) throws IOException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (this.zzho != null) {
            this.zzhl.setVolume(this.zzho, d);
        }
    }

    protected void start(Bundle bundle) {
        zzb(bundle);
    }

    public final zzai zzs() {
        return this.zzhn;
    }
}
