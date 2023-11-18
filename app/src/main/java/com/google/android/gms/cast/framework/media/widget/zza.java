package com.google.android.gms.cast.framework.media.widget;

import android.graphics.Bitmap;
import com.google.android.gms.internal.cast.zzy;

final class zza implements zzy {
    private final /* synthetic */ ExpandedControllerActivity zzsf;

    zza(ExpandedControllerActivity expandedControllerActivity) {
        this.zzsf = expandedControllerActivity;
    }

    public final void zza(Bitmap bitmap) {
        if (bitmap != null) {
            if (this.zzsf.zzrw != null) {
                this.zzsf.zzrw.setVisibility(8);
            }
            if (this.zzsf.zzrv != null) {
                this.zzsf.zzrv.setVisibility(0);
                this.zzsf.zzrv.setImageBitmap(bitmap);
            }
        }
    }
}
