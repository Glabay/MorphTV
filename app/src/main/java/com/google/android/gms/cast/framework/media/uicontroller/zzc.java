package com.google.android.gms.cast.framework.media.uicontroller;

import android.view.View;
import android.view.View.OnClickListener;

final class zzc implements OnClickListener {
    private final /* synthetic */ UIMediaController zzpt;

    zzc(UIMediaController uIMediaController) {
        this.zzpt = uIMediaController;
    }

    public final void onClick(View view) {
        this.zzpt.onSkipPrevClicked(view);
    }
}
