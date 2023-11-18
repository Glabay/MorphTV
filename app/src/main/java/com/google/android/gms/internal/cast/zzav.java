package com.google.android.gms.internal.cast;

import android.view.View;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;

public final class zzav extends UIController {
    private final View view;

    public zzav(View view) {
        this.view = view;
    }

    private final void zzby() {
        View view;
        int i;
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            if (!remoteMediaClient.isBuffering()) {
                view = this.view;
                i = 8;
                view.setVisibility(i);
            }
        }
        view = this.view;
        i = 0;
        view.setVisibility(i);
    }

    public final void onMediaStatusUpdated() {
        zzby();
    }

    public final void onSendingRemoteMediaRequest() {
        this.view.setVisibility(0);
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        zzby();
    }

    public final void onSessionEnded() {
        this.view.setVisibility(8);
        super.onSessionEnded();
    }
}
