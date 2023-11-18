package com.google.android.gms.internal.cast;

import android.view.ViewGroup;

final class zzq implements Runnable {
    private final /* synthetic */ zzo zziu;

    zzq(zzo zzo) {
        this.zziu = zzo;
    }

    public final void run() {
        if (this.zziu.zzit.zzis) {
            ((ViewGroup) this.zziu.zzit.zzhv.getWindow().getDecorView()).removeView(this.zziu.zzit);
            if (this.zziu.zzit.zzhz != null) {
                this.zziu.zzit.zzhz.onOverlayDismissed();
            }
            this.zziu.zzit.reset();
        }
    }
}
