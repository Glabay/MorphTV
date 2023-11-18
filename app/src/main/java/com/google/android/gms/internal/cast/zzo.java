package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.framework.IntroductoryOverlay.zza;
import com.google.android.gms.cast.framework.internal.featurehighlight.zzh;

final class zzo implements zzh {
    final /* synthetic */ zzn zzit;

    zzo(zzn zzn) {
        this.zzit = zzn;
    }

    public final void dismiss() {
        if (this.zzit.zzis) {
            zza.zzd(this.zzit.zzhv);
            this.zzit.zzir.zzb(new zzq(this));
        }
    }

    public final void zzan() {
        if (this.zzit.zzis) {
            zza.zzd(this.zzit.zzhv);
            this.zzit.zzir.zzc(new zzp(this));
        }
    }
}
