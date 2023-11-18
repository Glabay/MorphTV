package com.google.android.gms.internal.cast;

import android.view.View;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;

public final class zzas extends UIController {
    private final View view;

    public zzas(View view) {
        this.view = view;
        this.view.setEnabled(false);
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        this.view.setEnabled(true);
    }

    public final void onSessionEnded() {
        this.view.setEnabled(false);
        super.onSessionEnded();
    }
}
