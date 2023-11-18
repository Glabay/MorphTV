package com.google.android.gms.internal.cast;

import android.widget.SeekBar;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.ProgressListener;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;

public final class zzbc extends UIController implements ProgressListener {
    private boolean zzld = true;
    private final SeekBar zzqe;
    private final long zzqt;

    public zzbc(SeekBar seekBar, long j) {
        this.zzqe = seekBar;
        this.zzqt = j;
        this.zzqe.setEnabled(false);
    }

    public final void onProgressUpdated(long j, long j2) {
        if (this.zzld) {
            RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
            MediaStatus mediaStatus = remoteMediaClient == null ? null : remoteMediaClient.getMediaStatus();
            Object obj = (mediaStatus == null || !mediaStatus.isPlayingAd()) ? null : 1;
            if (obj != null) {
                this.zzqe.setEnabled(false);
            } else {
                this.zzqe.setProgress((int) j);
                this.zzqe.setEnabled(true);
            }
            this.zzqe.setMax((int) j2);
        }
    }

    public final void onSendingRemoteMediaRequest() {
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        boolean z = true;
        if (remoteMediaClient != null) {
            remoteMediaClient.addProgressListener(this, this.zzqt);
            if (remoteMediaClient.hasMediaSession()) {
                this.zzqe.setMax((int) remoteMediaClient.getStreamDuration());
                this.zzqe.setProgress((int) remoteMediaClient.getApproximateStreamPosition());
                this.zzqe.setEnabled(z);
            }
        }
        this.zzqe.setMax(1);
        z = false;
        this.zzqe.setProgress(0);
        this.zzqe.setEnabled(z);
    }

    public final void onSessionEnded() {
        if (getRemoteMediaClient() != null) {
            getRemoteMediaClient().removeProgressListener(this);
        }
        this.zzqe.setMax(1);
        this.zzqe.setProgress(0);
        this.zzqe.setEnabled(false);
        super.onSessionEnded();
    }

    public final void zzj(boolean z) {
        this.zzld = z;
    }
}
