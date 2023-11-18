package com.google.android.gms.cast.framework.media.uicontroller;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

final class zzi implements OnClickListener {
    private final /* synthetic */ UIMediaController zzpt;

    zzi(UIMediaController uIMediaController) {
        this.zzpt = uIMediaController;
    }

    public final void onClick(View view) {
        this.zzpt.onMuteToggleClicked((ImageView) view);
    }
}
