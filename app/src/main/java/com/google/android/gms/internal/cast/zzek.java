package com.google.android.gms.internal.cast;

final class zzek extends zzen {
    private final /* synthetic */ zzej zzyh;

    zzek(zzej zzej) {
        this.zzyh = zzej;
    }

    public final void doFrame(long j) {
        this.zzyh.zzyf = this.zzyh.zzyf + 1;
        if (!this.zzyh.zzb(this.zzyh.animator) && !this.zzyh.animator.isStarted() && !this.zzyh.zzde()) {
            if (this.zzyh.zzyd != null) {
                this.zzyh.zzyd.run();
            }
            this.zzyh.animator.start();
        }
    }
}
