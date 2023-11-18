package com.google.android.gms.cast;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.cast.Cast.CastOptions;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.cast.zzcn;

final class zze extends AbstractClientBuilder<zzcn, CastOptions> {
    zze() {
    }

    public final /* synthetic */ Client buildClient(Context context, Looper looper, ClientSettings clientSettings, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        CastOptions castOptions = (CastOptions) obj;
        Preconditions.checkNotNull(castOptions, "Setting the API options is required.");
        return new zzcn(context, looper, clientSettings, castOptions.zzai, (long) castOptions.zzak, castOptions.zzaj, castOptions.extras, connectionCallbacks, onConnectionFailedListener);
    }
}
