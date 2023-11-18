package com.google.android.gms.internal.cast;

import android.widget.ProgressBar;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.ProgressListener;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;

public final class zzbb extends UIController implements ProgressListener {
    private final ProgressBar zzqs;
    private final long zzqt;

    public zzbb(ProgressBar progressBar, long j) {
        this.zzqs = progressBar;
        this.zzqt = j;
    }

    public final void onMediaStatusUpdated() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient == null || !remoteMediaClient.hasMediaSession()) {
            this.zzqs.setMax(1);
            this.zzqs.setProgress(0);
        }
    }

    public final void onProgressUpdated(long j, long j2) {
        this.zzqs.setMax((int) j2);
        this.zzqs.setProgress((int) j);
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null) {
            remoteMediaClient.addProgressListener(this, this.zzqt);
            if (remoteMediaClient.hasMediaSession()) {
                this.zzqs.setMax((int) remoteMediaClient.getStreamDuration());
                this.zzqs.setProgress((int) remoteMediaClient.getApproximateStreamPosition());
                return;
            }
            this.zzqs.setMax(1);
            this.zzqs.setProgress(0);
        }
    }

    public final void onSessionEnded() {
        if (getRemoteMediaClient() != null) {
            getRemoteMediaClient().removeProgressListener(this);
        }
        this.zzqs.setMax(1);
        this.zzqs.setProgress(0);
        super.onSessionEnded();
    }
}
