package com.google.android.gms.internal.cast;

import android.graphics.Bitmap;

final class zzar implements zzy {
    private final /* synthetic */ zzaq zzqc;

    zzar(zzaq zzaq) {
        this.zzqc = zzaq;
    }

    public final void zza(Bitmap bitmap) {
        if (bitmap != null) {
            if (this.zzqc.zzqb != null) {
                this.zzqc.zzqb.setVisibility(4);
            }
            this.zzqc.zzpw.setVisibility(0);
            this.zzqc.zzpw.setImageBitmap(bitmap);
        }
    }
}
