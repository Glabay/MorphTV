package com.google.android.gms.cast.framework.media.uicontroller;

import android.view.View;
import android.view.View.OnClickListener;

final class zzg implements OnClickListener {
    private final /* synthetic */ UIMediaController zzpt;

    zzg(UIMediaController uIMediaController) {
        this.zzpt = uIMediaController;
    }

    public final void onClick(View view) {
        this.zzpt.onLaunchExpandedControllerClicked(view);
    }
}
