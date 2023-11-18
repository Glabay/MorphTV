package com.google.android.gms.cast.framework.media.uicontroller;

import android.view.View;
import android.view.View.OnClickListener;

final class zze implements OnClickListener {
    private final /* synthetic */ UIMediaController zzpt;
    private final /* synthetic */ long zzpu;

    zze(UIMediaController uIMediaController, long j) {
        this.zzpt = uIMediaController;
        this.zzpu = j;
    }

    public final void onClick(View view) {
        this.zzpt.onRewindClicked(view, this.zzpu);
    }
}
