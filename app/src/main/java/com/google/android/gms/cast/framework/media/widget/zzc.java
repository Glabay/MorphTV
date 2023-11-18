package com.google.android.gms.cast.framework.media.widget;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.cast.AdBreakClipInfo;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import java.util.TimerTask;

final class zzc extends TimerTask {
    final /* synthetic */ ExpandedControllerActivity zzsf;
    final /* synthetic */ AdBreakClipInfo zzsg;
    final /* synthetic */ RemoteMediaClient zzsh;

    zzc(ExpandedControllerActivity expandedControllerActivity, AdBreakClipInfo adBreakClipInfo, RemoteMediaClient remoteMediaClient) {
        this.zzsf = expandedControllerActivity;
        this.zzsg = adBreakClipInfo;
        this.zzsh = remoteMediaClient;
    }

    public final void run() {
        new Handler(Looper.getMainLooper()).post(new zzd(this));
    }
}
