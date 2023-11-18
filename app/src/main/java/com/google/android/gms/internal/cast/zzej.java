package com.google.android.gms.internal.cast;

import android.animation.Animator;
import android.support.annotation.Nullable;

public final class zzej extends zzei {
    protected final Animator animator;
    private final Runnable zzyd;
    private final int zzye;
    private int zzyf;
    private zzen zzyg = new zzek(this);

    private zzej(Animator animator, int i, @Nullable Runnable runnable) {
        this.animator = animator;
        this.zzye = -1;
        this.zzyd = null;
    }

    public static void zza(Animator animator, int i, @Nullable Runnable runnable) {
        animator.addListener(new zzej(animator, -1, null));
    }

    private final boolean zzde() {
        return this.zzye != -1 && this.zzyf >= this.zzye;
    }

    public final void onAnimationEnd(Animator animator) {
        if (!zzb(animator)) {
            zzel.zzdf().zza(this.zzyg);
        }
    }
}
