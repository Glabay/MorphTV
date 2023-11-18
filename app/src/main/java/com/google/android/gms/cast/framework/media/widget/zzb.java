package com.google.android.gms.cast.framework.media.widget;

import android.view.View;
import android.view.View.OnClickListener;

final class zzb implements OnClickListener {
    private final /* synthetic */ ExpandedControllerActivity zzsf;

    zzb(ExpandedControllerActivity expandedControllerActivity) {
        this.zzsf = expandedControllerActivity;
    }

    public final void onClick(View view) {
        if (this.zzsf.zzry.isClickable()) {
            this.zzsf.getRemoteMediaClient().skipAd();
        }
    }
}
