package com.google.android.gms.cast.framework.media.uicontroller;

import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

public class UIController {
    private RemoteMediaClient zzhp;

    protected RemoteMediaClient getRemoteMediaClient() {
        return this.zzhp;
    }

    public void onMediaStatusUpdated() {
    }

    public void onSendingRemoteMediaRequest() {
    }

    public void onSessionConnected(CastSession castSession) {
        this.zzhp = castSession != null ? castSession.getRemoteMediaClient() : null;
    }

    public void onSessionEnded() {
        this.zzhp = null;
    }
}
