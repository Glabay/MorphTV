package com.google.android.gms.internal.cast;

import android.view.View;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;

public final class zzbd extends UIController {
    private final View view;

    public zzbd(View view) {
        this.view = view;
        this.view.setEnabled(false);
    }

    private final void zzby() {
        View view;
        boolean z;
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (!(remoteMediaClient == null || !remoteMediaClient.hasMediaSession() || remoteMediaClient.isLiveStream())) {
            if (!remoteMediaClient.isPlayingAd()) {
                view = this.view;
                z = true;
                view.setEnabled(z);
            }
        }
        view = this.view;
        z = false;
        view.setEnabled(z);
    }

    public final void onMediaStatusUpdated() {
        zzby();
    }

    public final void onSendingRemoteMediaRequest() {
        this.view.setEnabled(false);
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        zzby();
    }

    public final void onSessionEnded() {
        this.view.setEnabled(false);
        super.onSessionEnded();
    }
}
