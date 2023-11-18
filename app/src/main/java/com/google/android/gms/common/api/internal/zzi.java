package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.Preconditions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import org.apache.commons.lang3.StringUtils;

public class zzi extends zzk {
    private final SparseArray<zza> zzed = new SparseArray();

    private class zza implements OnConnectionFailedListener {
        public final int zzee;
        public final GoogleApiClient zzef;
        public final OnConnectionFailedListener zzeg;
        private final /* synthetic */ zzi zzeh;

        public zza(zzi zzi, int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
            this.zzeh = zzi;
            this.zzee = i;
            this.zzef = googleApiClient;
            this.zzeg = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener(this);
        }

        public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            String valueOf = String.valueOf(connectionResult);
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 27);
            stringBuilder.append("beginFailureResolution for ");
            stringBuilder.append(valueOf);
            Log.d("AutoManageHelper", stringBuilder.toString());
            this.zzeh.zzb(connectionResult, this.zzee);
        }
    }

    private zzi(LifecycleFragment lifecycleFragment) {
        super(lifecycleFragment);
        this.mLifecycleFragment.addCallback("AutoManageHelper", this);
    }

    public static zzi zza(LifecycleActivity lifecycleActivity) {
        LifecycleFragment fragment = LifecycleCallback.getFragment(lifecycleActivity);
        zzi zzi = (zzi) fragment.getCallbackOrNull("AutoManageHelper", zzi.class);
        return zzi != null ? zzi : new zzi(fragment);
    }

    @Nullable
    private final zza zzd(int i) {
        return this.zzed.size() <= i ? null : (zza) this.zzed.get(this.zzed.keyAt(i));
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        for (int i = 0; i < this.zzed.size(); i++) {
            zza zzd = zzd(i);
            if (zzd != null) {
                printWriter.append(str).append("GoogleApiClient #").print(zzd.zzee);
                printWriter.println(":");
                zzd.zzef.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
            }
        }
    }

    public final void onStart() {
        super.onStart();
        boolean z = this.mStarted;
        String valueOf = String.valueOf(this.zzed);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 14);
        stringBuilder.append("onStart ");
        stringBuilder.append(z);
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(valueOf);
        Log.d("AutoManageHelper", stringBuilder.toString());
        if (this.zzer.get() == null) {
            for (int i = 0; i < this.zzed.size(); i++) {
                zza zzd = zzd(i);
                if (zzd != null) {
                    zzd.zzef.connect();
                }
            }
        }
    }

    public void onStop() {
        super.onStop();
        for (int i = 0; i < this.zzed.size(); i++) {
            zza zzd = zzd(i);
            if (zzd != null) {
                zzd.zzef.disconnect();
            }
        }
    }

    public final void zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
        Preconditions.checkNotNull(googleApiClient, "GoogleApiClient instance cannot be null");
        boolean z = this.zzed.indexOfKey(i) < 0;
        StringBuilder stringBuilder = new StringBuilder(54);
        stringBuilder.append("Already managing a GoogleApiClient with id ");
        stringBuilder.append(i);
        Preconditions.checkState(z, stringBuilder.toString());
        zzl zzl = (zzl) this.zzer.get();
        boolean z2 = this.mStarted;
        String valueOf = String.valueOf(zzl);
        StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(valueOf).length() + 49);
        stringBuilder2.append("starting AutoManage for client ");
        stringBuilder2.append(i);
        stringBuilder2.append(StringUtils.SPACE);
        stringBuilder2.append(z2);
        stringBuilder2.append(StringUtils.SPACE);
        stringBuilder2.append(valueOf);
        Log.d("AutoManageHelper", stringBuilder2.toString());
        this.zzed.put(i, new zza(this, i, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && zzl == null) {
            String valueOf2 = String.valueOf(googleApiClient);
            StringBuilder stringBuilder3 = new StringBuilder(String.valueOf(valueOf2).length() + 11);
            stringBuilder3.append("connecting ");
            stringBuilder3.append(valueOf2);
            Log.d("AutoManageHelper", stringBuilder3.toString());
            googleApiClient.connect();
        }
    }

    protected final void zza(ConnectionResult connectionResult, int i) {
        Log.w("AutoManageHelper", "Unresolved error while connecting client. Stopping auto-manage.");
        if (i < 0) {
            Log.wtf("AutoManageHelper", "AutoManageLifecycleHelper received onErrorResolutionFailed callback but no failing client ID is set", new Exception());
            return;
        }
        zza zza = (zza) this.zzed.get(i);
        if (zza != null) {
            zzc(i);
            OnConnectionFailedListener onConnectionFailedListener = zza.zzeg;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
    }

    public final void zzc(int i) {
        zza zza = (zza) this.zzed.get(i);
        this.zzed.remove(i);
        if (zza != null) {
            zza.zzef.unregisterConnectionFailedListener(zza);
            zza.zzef.disconnect();
        }
    }

    protected final void zzr() {
        for (int i = 0; i < this.zzed.size(); i++) {
            zza zzd = zzd(i);
            if (zzd != null) {
                zzd.zzef.connect();
            }
        }
    }
}
