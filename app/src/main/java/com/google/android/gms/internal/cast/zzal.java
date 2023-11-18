package com.google.android.gms.internal.cast;

import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat.Callback;
import android.view.KeyEvent;
import org.mozilla.universalchardet.prober.distributionanalysis.Big5DistributionAnalysis;

final class zzal extends Callback {
    private final /* synthetic */ zzai zzpm;

    zzal(zzai zzai) {
        this.zzpm = zzai;
    }

    public final boolean onMediaButtonEvent(Intent intent) {
        KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
        if (keyEvent != null && (keyEvent.getKeyCode() == 127 || keyEvent.getKeyCode() == Big5DistributionAnalysis.LOWBYTE_END_1)) {
            this.zzpm.zzhp.togglePlayback();
        }
        return true;
    }

    public final void onPause() {
        this.zzpm.zzhp.togglePlayback();
    }

    public final void onPlay() {
        this.zzpm.zzhp.togglePlayback();
    }

    public final void onStop() {
        if (this.zzpm.zzhp.isLiveStream()) {
            this.zzpm.zzhp.togglePlayback();
        }
    }
}
