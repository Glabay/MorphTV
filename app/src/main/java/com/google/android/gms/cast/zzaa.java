package com.google.android.gms.cast;

import android.support.annotation.NonNull;
import com.google.android.gms.cast.CastRemoteDisplayLocalService.Callbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

final class zzaa implements OnCompleteListener<Void> {
    private final /* synthetic */ CastRemoteDisplayLocalService zzcg;

    zzaa(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {
        this.zzcg = castRemoteDisplayLocalService;
    }

    public final void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            this.zzcg.zzb("remote display stopped");
        } else {
            this.zzcg.zzb("Unable to stop the remote display, result unsuccessful");
            if (this.zzcg.zzbq.get() != null) {
                ((Callbacks) this.zzcg.zzbq.get()).onRemoteDisplaySessionError(new Status(CastStatusCodes.ERROR_STOPPING_SERVICE_FAILED));
            }
        }
        this.zzcg.zzbx = null;
    }
}
