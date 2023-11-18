package com.google.android.gms.internal.cast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.util.SimpleArrayMap;

public class zzei extends AnimatorListenerAdapter {
    private SimpleArrayMap<Animator, Boolean> zzyc = new SimpleArrayMap();

    public void onAnimationCancel(Animator animator) {
        this.zzyc.put(animator, Boolean.valueOf(true));
    }

    public void onAnimationStart(Animator animator) {
        this.zzyc.put(animator, Boolean.valueOf(false));
    }

    protected final boolean zzb(Animator animator) {
        return this.zzyc.containsKey(animator) && ((Boolean) this.zzyc.get(animator)).booleanValue();
    }
}
