package com.google.android.gms.internal.cast;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;

public final class zzba extends UIController {
    private final ImageView zzpw;
    private final View zzqk;
    private final boolean zzql;
    private final Drawable zzqm;
    private final String zzqn;
    private final Drawable zzqo;
    private final String zzqp;
    private final Drawable zzqq;
    private final String zzqr;

    public zzba(@NonNull ImageView imageView, Context context, @NonNull Drawable drawable, @NonNull Drawable drawable2, Drawable drawable3, View view, boolean z) {
        this.zzpw = imageView;
        this.zzqm = drawable;
        this.zzqo = drawable2;
        if (drawable3 != null) {
            drawable2 = drawable3;
        }
        this.zzqq = drawable2;
        this.zzqn = context.getString(C0782R.string.cast_play);
        this.zzqp = context.getString(C0782R.string.cast_pause);
        this.zzqr = context.getString(C0782R.string.cast_stop);
        this.zzqk = view;
        this.zzql = z;
        this.zzpw.setEnabled(false);
    }

    private final void zza(Drawable drawable, String str) {
        this.zzpw.setImageDrawable(drawable);
        this.zzpw.setContentDescription(str);
        this.zzpw.setVisibility(0);
        this.zzpw.setEnabled(true);
        if (this.zzqk != null) {
            this.zzqk.setVisibility(8);
        }
    }

    private final void zzby() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null) {
            if (remoteMediaClient.hasMediaSession()) {
                if (remoteMediaClient.isPaused()) {
                    zza(this.zzqm, this.zzqn);
                    return;
                } else if (remoteMediaClient.isPlaying()) {
                    if (remoteMediaClient.isLiveStream()) {
                        zza(this.zzqq, this.zzqr);
                        return;
                    } else {
                        zza(this.zzqo, this.zzqp);
                        return;
                    }
                } else if (remoteMediaClient.isBuffering()) {
                    zzi(false);
                    return;
                } else {
                    if (remoteMediaClient.isLoadingNextItem()) {
                        zzi(true);
                    }
                    return;
                }
            }
        }
        this.zzpw.setEnabled(false);
    }

    private final void zzi(boolean z) {
        int i = 0;
        if (this.zzqk != null) {
            this.zzqk.setVisibility(0);
        }
        ImageView imageView = this.zzpw;
        if (this.zzql) {
            i = 4;
        }
        imageView.setVisibility(i);
        this.zzpw.setEnabled(z ^ 1);
    }

    public final void onMediaStatusUpdated() {
        zzby();
    }

    public final void onSendingRemoteMediaRequest() {
        zzi(true);
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        zzby();
    }

    public final void onSessionEnded() {
        this.zzpw.setEnabled(false);
        super.onSessionEnded();
    }
}
