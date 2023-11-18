package com.google.android.gms.cast;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.google.android.gms.cast.CastRemoteDisplay.CastRemoteDisplayOptions;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.internal.cast.zzea;

final class zzo extends AbstractClientBuilder<zzea, CastRemoteDisplayOptions> {
    zzo() {
    }

    public final /* synthetic */ Client buildClient(Context context, Looper looper, ClientSettings clientSettings, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        CastRemoteDisplayOptions castRemoteDisplayOptions = (CastRemoteDisplayOptions) obj;
        Bundle bundle = new Bundle();
        bundle.putInt("configuration", castRemoteDisplayOptions.zzbb);
        return new zzea(context, looper, clientSettings, castRemoteDisplayOptions.zzai, bundle, castRemoteDisplayOptions.zzba, connectionCallbacks, onConnectionFailedListener);
    }
}
