package com.google.android.gms.internal.cast;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.Cast.ApplicationConnectionResult;
import com.google.android.gms.cast.Cast.Listener;
import com.google.android.gms.cast.Cast.MessageReceivedCallback;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastStatusCodes;
import com.google.android.gms.cast.LaunchOptions;
import com.google.android.gms.cast.zzad;
import com.google.android.gms.cast.zzaf;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ResultHolder;
import com.google.android.gms.common.internal.BinderWrapper;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClient;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class zzcn extends GmsClient<zzcz> {
    private static final zzdg zzbd = new zzdg("CastClientImpl");
    private static final Object zzvp = new Object();
    private static final Object zzvq = new Object();
    private final Bundle extras;
    private final Listener zzaj;
    private double zzei;
    private boolean zzej;
    private final CastDevice zzhq;
    private ApplicationMetadata zzux;
    private final Map<String, MessageReceivedCallback> zzuy = new HashMap();
    private final long zzuz;
    private zzcp zzva;
    private String zzvb;
    private boolean zzvc;
    private boolean zzvd;
    private boolean zzve;
    private zzad zzvf;
    private int zzvg;
    private int zzvh;
    private final AtomicLong zzvi = new AtomicLong(0);
    private String zzvj;
    private String zzvk;
    private Bundle zzvl;
    private final Map<Long, ResultHolder<Status>> zzvm = new HashMap();
    private ResultHolder<ApplicationConnectionResult> zzvn;
    private ResultHolder<Status> zzvo;

    public zzcn(Context context, Looper looper, ClientSettings clientSettings, CastDevice castDevice, long j, Listener listener, Bundle bundle, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 10, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zzhq = castDevice;
        this.zzaj = listener;
        this.zzuz = j;
        this.extras = bundle;
        zzcp();
    }

    private final void zza(ResultHolder<ApplicationConnectionResult> resultHolder) {
        synchronized (zzvp) {
            if (this.zzvn != null) {
                this.zzvn.setResult(new zzco(new Status(CastStatusCodes.CANCELED)));
            }
            this.zzvn = resultHolder;
        }
    }

    private final void zza(zzcd zzcd) {
        boolean z;
        String zzcl = zzcd.zzcl();
        if (zzcu.zza(zzcl, this.zzvb)) {
            z = false;
        } else {
            this.zzvb = zzcl;
            z = true;
        }
        zzbd.m25d("hasChanged=%b, mFirstApplicationStatusUpdate=%b", Boolean.valueOf(z), Boolean.valueOf(this.zzvc));
        if (this.zzaj != null && (z || this.zzvc)) {
            this.zzaj.onApplicationStatusChanged();
        }
        this.zzvc = false;
    }

    private final void zza(zzcv zzcv) {
        boolean z;
        ApplicationMetadata applicationMetadata = zzcv.getApplicationMetadata();
        if (!zzcu.zza(applicationMetadata, this.zzux)) {
            this.zzux = applicationMetadata;
            this.zzaj.onApplicationMetadataChanged(this.zzux);
        }
        double volume = zzcv.getVolume();
        if (Double.isNaN(volume) || Math.abs(volume - this.zzei) <= 1.0E-7d) {
            z = false;
        } else {
            this.zzei = volume;
            z = true;
        }
        boolean zzcv2 = zzcv.zzcv();
        if (zzcv2 != this.zzej) {
            this.zzej = zzcv2;
            z = true;
        }
        zzbd.m25d("hasVolumeChanged=%b, mFirstDeviceStatusUpdate=%b", Boolean.valueOf(z), Boolean.valueOf(this.zzvd));
        if (this.zzaj != null && (z || this.zzvd)) {
            this.zzaj.onVolumeChanged();
        }
        int activeInputState = zzcv.getActiveInputState();
        if (activeInputState != this.zzvg) {
            this.zzvg = activeInputState;
            z = true;
        } else {
            z = false;
        }
        zzbd.m25d("hasActiveInputChanged=%b, mFirstDeviceStatusUpdate=%b", Boolean.valueOf(z), Boolean.valueOf(this.zzvd));
        if (this.zzaj != null && (z || this.zzvd)) {
            this.zzaj.onActiveInputStateChanged(this.zzvg);
        }
        activeInputState = zzcv.getStandbyState();
        if (activeInputState != this.zzvh) {
            this.zzvh = activeInputState;
            z = true;
        } else {
            z = false;
        }
        zzbd.m25d("hasStandbyStateChanged=%b, mFirstDeviceStatusUpdate=%b", Boolean.valueOf(z), Boolean.valueOf(this.zzvd));
        if (this.zzaj != null && (z || this.zzvd)) {
            this.zzaj.onStandbyStateChanged(this.zzvh);
        }
        if (!zzcu.zza(this.zzvf, zzcv.zzcw())) {
            this.zzvf = zzcv.zzcw();
        }
        this.zzvd = false;
    }

    private final void zzb(long j, int i) {
        synchronized (this.zzvm) {
            ResultHolder resultHolder = (ResultHolder) this.zzvm.remove(Long.valueOf(j));
        }
        if (resultHolder != null) {
            resultHolder.setResult(new Status(i));
        }
    }

    private final void zzc(ResultHolder<Status> resultHolder) {
        synchronized (zzvq) {
            if (this.zzvo != null) {
                resultHolder.setResult(new Status(CastStatusCodes.INVALID_REQUEST));
                return;
            }
            this.zzvo = resultHolder;
        }
    }

    private final void zzcp() {
        this.zzve = false;
        this.zzvg = -1;
        this.zzvh = -1;
        this.zzux = null;
        this.zzvb = null;
        this.zzei = 0.0d;
        this.zzej = false;
        this.zzvf = null;
    }

    private final void zzcq() {
        zzbd.m25d("removing all MessageReceivedCallbacks", new Object[0]);
        synchronized (this.zzuy) {
            this.zzuy.clear();
        }
    }

    @VisibleForTesting
    private final boolean zzcr() {
        return (!this.zzve || this.zzva == null || this.zzva.isDisposed()) ? false : true;
    }

    private final void zzm(int i) {
        synchronized (zzvq) {
            if (this.zzvo != null) {
                this.zzvo.setResult(new Status(i));
                this.zzvo = null;
            }
        }
    }

    protected final /* synthetic */ IInterface createServiceInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.cast.internal.ICastDeviceController");
        return queryLocalInterface instanceof zzcz ? (zzcz) queryLocalInterface : new zzda(iBinder);
    }

    public final void disconnect() {
        zzbd.m25d("disconnect(); ServiceListener=%s, isConnected=%b", this.zzva, Boolean.valueOf(isConnected()));
        zzcp zzcp = this.zzva;
        this.zzva = null;
        if (zzcp != null) {
            if (zzcp.zzcu() != null) {
                zzcq();
                try {
                    ((zzcz) getService()).disconnect();
                    return;
                } catch (Throwable e) {
                    zzbd.zza(e, "Error while disconnecting the controller interface: %s", e.getMessage());
                    return;
                } finally {
                    super.disconnect();
                }
            }
        }
        zzbd.m25d("already disposed, so short-circuiting", new Object[0]);
    }

    public final int getActiveInputState() throws IllegalStateException {
        checkConnected();
        return this.zzvg;
    }

    public final ApplicationMetadata getApplicationMetadata() throws IllegalStateException {
        checkConnected();
        return this.zzux;
    }

    public final String getApplicationStatus() throws IllegalStateException {
        checkConnected();
        return this.zzvb;
    }

    public final Bundle getConnectionHint() {
        if (this.zzvl == null) {
            return super.getConnectionHint();
        }
        Bundle bundle = this.zzvl;
        this.zzvl = null;
        return bundle;
    }

    protected final Bundle getGetServiceRequestExtraArgs() {
        Bundle bundle = new Bundle();
        zzbd.m25d("getRemoteService(): mLastApplicationId=%s, mLastSessionId=%s", this.zzvj, this.zzvk);
        this.zzhq.putInBundle(bundle);
        bundle.putLong("com.google.android.gms.cast.EXTRA_CAST_FLAGS", this.zzuz);
        if (this.extras != null) {
            bundle.putAll(this.extras);
        }
        this.zzva = new zzcp(this);
        bundle.putParcelable(CastExtraArgs.LISTENER, new BinderWrapper(this.zzva.asBinder()));
        if (this.zzvj != null) {
            bundle.putString("last_application_id", this.zzvj);
            if (this.zzvk != null) {
                bundle.putString("last_session_id", this.zzvk);
            }
        }
        return bundle;
    }

    public final int getMinApkVersion() {
        return GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }

    @NonNull
    protected final String getServiceDescriptor() {
        return "com.google.android.gms.cast.internal.ICastDeviceController";
    }

    public final int getStandbyState() throws IllegalStateException {
        checkConnected();
        return this.zzvh;
    }

    @NonNull
    protected final String getStartServiceAction() {
        return "com.google.android.gms.cast.service.BIND_CAST_DEVICE_CONTROLLER_SERVICE";
    }

    public final double getVolume() throws IllegalStateException {
        checkConnected();
        return this.zzei;
    }

    public final boolean isMute() throws IllegalStateException {
        checkConnected();
        return this.zzej;
    }

    public final void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        zzcq();
    }

    protected final void onPostInitHandler(int i, IBinder iBinder, Bundle bundle, int i2) {
        zzbd.m25d("in onPostInitHandler; statusCode=%d", Integer.valueOf(i));
        if (i != 0) {
            if (i != 1001) {
                this.zzve = false;
                if (i == 1001) {
                    this.zzvl = new Bundle();
                    this.zzvl.putBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING, true);
                    i = 0;
                }
                super.onPostInitHandler(i, iBinder, bundle, i2);
            }
        }
        this.zzve = true;
        this.zzvc = true;
        this.zzvd = true;
        if (i == 1001) {
            this.zzvl = new Bundle();
            this.zzvl.putBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING, true);
            i = 0;
        }
        super.onPostInitHandler(i, iBinder, bundle, i2);
    }

    public final void removeMessageReceivedCallbacks(String str) throws IllegalArgumentException, RemoteException {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Channel namespace cannot be null or empty");
        }
        synchronized (this.zzuy) {
            MessageReceivedCallback messageReceivedCallback = (MessageReceivedCallback) this.zzuy.remove(str);
        }
        if (messageReceivedCallback != null) {
            try {
                ((zzcz) getService()).zzs(str);
            } catch (Throwable e) {
                zzbd.zza(e, "Error unregistering namespace (%s): %s", str, e.getMessage());
            }
        }
    }

    public final void requestStatus() throws IllegalStateException, RemoteException {
        zzcz zzcz = (zzcz) getService();
        if (zzcr()) {
            zzcz.requestStatus();
        }
    }

    public final void setMessageReceivedCallbacks(String str, MessageReceivedCallback messageReceivedCallback) throws IllegalArgumentException, IllegalStateException, RemoteException {
        zzcu.zzo(str);
        removeMessageReceivedCallbacks(str);
        if (messageReceivedCallback != null) {
            synchronized (this.zzuy) {
                this.zzuy.put(str, messageReceivedCallback);
            }
            zzcz zzcz = (zzcz) getService();
            if (zzcr()) {
                zzcz.zzr(str);
            }
        }
    }

    public final void setMute(boolean z) throws IllegalStateException, RemoteException {
        zzcz zzcz = (zzcz) getService();
        if (zzcr()) {
            zzcz.zza(z, this.zzei, this.zzej);
        }
    }

    public final void setVolume(double d) throws IllegalArgumentException, IllegalStateException, RemoteException {
        if (!Double.isInfinite(d)) {
            if (!Double.isNaN(d)) {
                zzcz zzcz = (zzcz) getService();
                if (zzcr()) {
                    zzcz.zza(d, this.zzei, this.zzej);
                    return;
                }
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder(41);
        stringBuilder.append("Volume cannot be ");
        stringBuilder.append(d);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final void zza(String str, LaunchOptions launchOptions, ResultHolder<ApplicationConnectionResult> resultHolder) throws IllegalStateException, RemoteException {
        zza((ResultHolder) resultHolder);
        zzcz zzcz = (zzcz) getService();
        if (zzcr()) {
            zzcz.zzb(str, launchOptions);
        } else {
            zzl(CastStatusCodes.DEVICE_CONNECTION_SUSPENDED);
        }
    }

    public final void zza(String str, ResultHolder<Status> resultHolder) throws IllegalStateException, RemoteException {
        zzc((ResultHolder) resultHolder);
        zzcz zzcz = (zzcz) getService();
        if (zzcr()) {
            zzcz.zzi(str);
        } else {
            zzm(CastStatusCodes.DEVICE_CONNECTION_SUSPENDED);
        }
    }

    public final void zza(String str, String str2, zzaf zzaf, ResultHolder<ApplicationConnectionResult> resultHolder) throws IllegalStateException, RemoteException {
        zza((ResultHolder) resultHolder);
        if (zzaf == null) {
            zzaf = new zzaf();
        }
        zzcz zzcz = (zzcz) getService();
        if (zzcr()) {
            zzcz.zza(str, str2, zzaf);
        } else {
            zzl(CastStatusCodes.DEVICE_CONNECTION_SUSPENDED);
        }
    }

    public final void zza(String str, String str2, ResultHolder<Status> resultHolder) throws IllegalArgumentException, IllegalStateException, RemoteException {
        if (TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("The message payload cannot be null or empty");
        } else if (str2.length() > 524288) {
            zzbd.m28w("Message send failed. Message exceeds maximum size", new Object[0]);
            throw new IllegalArgumentException("Message exceeds maximum size");
        } else {
            zzcu.zzo(str);
            long incrementAndGet = this.zzvi.incrementAndGet();
            try {
                this.zzvm.put(Long.valueOf(incrementAndGet), resultHolder);
                zzcz zzcz = (zzcz) getService();
                if (zzcr()) {
                    zzcz.zza(str, str2, incrementAndGet);
                } else {
                    zzb(incrementAndGet, (int) CastStatusCodes.DEVICE_CONNECTION_SUSPENDED);
                }
            } catch (Throwable th) {
                this.zzvm.remove(Long.valueOf(incrementAndGet));
            }
        }
    }

    public final void zzb(ResultHolder<Status> resultHolder) throws IllegalStateException, RemoteException {
        zzc((ResultHolder) resultHolder);
        zzcz zzcz = (zzcz) getService();
        if (zzcr()) {
            zzcz.zzcx();
        } else {
            zzm(CastStatusCodes.DEVICE_CONNECTION_SUSPENDED);
        }
    }

    public final void zzl(int i) {
        synchronized (zzvp) {
            if (this.zzvn != null) {
                this.zzvn.setResult(new zzco(new Status(i)));
                this.zzvn = null;
            }
        }
    }
}
