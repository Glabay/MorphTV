package com.google.android.gms.cast.framework.media.uicontroller;

import android.view.View;
import android.view.View.OnClickListener;

final class zzd implements OnClickListener {
    private final /* synthetic */ UIMediaController zzpt;
    private final /* synthetic */ long zzpu;

    zzd(UIMediaController uIMediaController, long j) {
        this.zzpt = uIMediaController;
        this.zzpu = j;
    }

    public final void onClick(View view) {
        this.zzpt.onForwardClicked(view, this.zzpu);
    }
}
