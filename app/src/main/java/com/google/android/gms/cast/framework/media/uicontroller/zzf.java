package com.google.android.gms.cast.framework.media.uicontroller;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

final class zzf implements OnSeekBarChangeListener {
    private final /* synthetic */ UIMediaController zzpt;

    zzf(UIMediaController uIMediaController) {
        this.zzpt = uIMediaController;
    }

    public final void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        this.zzpt.onSeekBarProgressChanged(seekBar, i, z);
    }

    public final void onStartTrackingTouch(SeekBar seekBar) {
        this.zzpt.onSeekBarStartTrackingTouch(seekBar);
    }

    public final void onStopTrackingTouch(SeekBar seekBar) {
        this.zzpt.onSeekBarStopTrackingTouch(seekBar);
    }
}
