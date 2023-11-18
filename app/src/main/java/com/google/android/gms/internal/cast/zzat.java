package com.google.android.gms.internal.cast;

import android.graphics.drawable.ColorDrawable;
import android.widget.SeekBar;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;
import com.google.android.gms.common.util.PlatformVersion;

public final class zzat extends UIController {
    private final SeekBar zzqd;
    private final SeekBar zzqe;

    public zzat(SeekBar seekBar, SeekBar seekBar2) {
        this.zzqd = seekBar;
        this.zzqe = seekBar2;
        this.zzqd.setClickable(false);
        if (PlatformVersion.isAtLeastKitKat()) {
            this.zzqd.setThumb(null);
        } else {
            this.zzqd.setThumb(new ColorDrawable(0));
        }
        this.zzqd.setMax(1);
        this.zzqd.setProgress(1);
        this.zzqd.setOnTouchListener(new zzau(this));
    }

    private final void zzca() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            boolean isLiveStream = remoteMediaClient.isLiveStream();
            int i = 4;
            this.zzqd.setVisibility(isLiveStream ? 0 : 4);
            SeekBar seekBar = this.zzqe;
            if (!isLiveStream) {
                i = 0;
            }
            seekBar.setVisibility(i);
        }
    }

    public final void onMediaStatusUpdated() {
        zzca();
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        zzca();
    }
}
