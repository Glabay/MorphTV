package com.google.android.gms.cast.framework.media;

import java.util.TimerTask;

final class zzk extends TimerTask {
    private final /* synthetic */ MediaQueue zzls;

    zzk(MediaQueue mediaQueue) {
        this.zzls = mediaQueue;
    }

    public final void run() {
        this.zzls.zzbd();
    }
}
