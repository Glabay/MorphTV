package com.google.android.gms.internal.cast;

final class zzep implements Runnable {
    private final /* synthetic */ zzen zzyl;

    zzep(zzen zzen) {
        this.zzyl = zzen;
    }

    public final void run() {
        this.zzyl.doFrame(System.nanoTime());
    }
}
