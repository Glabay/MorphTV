package com.google.android.gms.internal.cast;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.gms.common.util.PlatformVersion;

final class zzs implements OnClickListener {
    final /* synthetic */ zzr zzix;

    zzs(zzr zzr) {
        this.zzix = zzr;
    }

    public final void onClick(View view) {
        if (PlatformVersion.isAtLeastJellyBean()) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0f});
            ofFloat.setDuration(400).addListener(new zzt(this));
            ofFloat.start();
            return;
        }
        this.zzix.zzao();
    }
}
