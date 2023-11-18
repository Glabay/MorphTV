package com.google.android.gms.cast.framework.media.uicontroller;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

final class zza implements OnClickListener {
    private final /* synthetic */ UIMediaController zzpt;

    zza(UIMediaController uIMediaController) {
        this.zzpt = uIMediaController;
    }

    public final void onClick(View view) {
        this.zzpt.onPlayPauseToggleClicked((ImageView) view);
    }
}
