package com.google.android.gms.internal.cast;

import android.text.format.DateUtils;
import android.widget.TextView;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.ProgressListener;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;

public final class zzbi extends UIController implements ProgressListener {
    private boolean zzld = true;
    private final long zzqt;
    private final TextView zzqw;
    private final String zzqz;

    public zzbi(TextView textView, long j, String str) {
        this.zzqw = textView;
        this.zzqt = j;
        this.zzqz = str;
    }

    public final void onProgressUpdated(long j, long j2) {
        if (this.zzld) {
            this.zzqw.setText(DateUtils.formatElapsedTime(j / 1000));
        }
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null) {
            remoteMediaClient.addProgressListener(this, this.zzqt);
            if (remoteMediaClient.hasMediaSession()) {
                this.zzqw.setText(DateUtils.formatElapsedTime(remoteMediaClient.getApproximateStreamPosition() / 1000));
                return;
            }
            this.zzqw.setText(this.zzqz);
        }
    }

    public final void onSessionEnded() {
        this.zzqw.setText(this.zzqz);
        if (getRemoteMediaClient() != null) {
            getRemoteMediaClient().removeProgressListener(this);
        }
        super.onSessionEnded();
    }

    public final void zzc(long j) {
        this.zzqw.setText(DateUtils.formatElapsedTime(j / 1000));
    }

    public final void zzj(boolean z) {
        this.zzld = z;
    }
}
