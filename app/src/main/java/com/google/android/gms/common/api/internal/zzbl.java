package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.GoogleApiManager.zza;
import com.google.android.gms.common.internal.BaseGmsClient.SignOutCallbacks;

final class zzbl implements SignOutCallbacks {
    final /* synthetic */ zza zzkk;

    zzbl(zza zza) {
        this.zzkk = zza;
    }

    public final void onSignOutComplete() {
        this.zzkk.zzjy.handler.post(new zzbm(this));
    }
}
