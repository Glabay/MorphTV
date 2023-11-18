package com.google.android.gms.internal.cast;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.cast.zzbp;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.ClientKey;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApi.Settings;
import com.google.android.gms.tasks.Task;
import java.util.List;

public final class zzch extends GoogleApi<NoOptions> {
    private static final Api<NoOptions> API = new Api("CastApi.API", CLIENT_BUILDER, CLIENT_KEY);
    private static final AbstractClientBuilder<zzcl, NoOptions> CLIENT_BUILDER = new zzci();
    private static final ClientKey<zzcl> CLIENT_KEY = new ClientKey();

    public zzch(@NonNull Context context) {
        super(context, API, null, Settings.DEFAULT_SETTINGS);
    }

    public final Task<Void> zza(@NonNull String[] strArr, String str, List<zzbp> list) {
        return doWrite(new zzcj(this, strArr, str, null));
    }
}
