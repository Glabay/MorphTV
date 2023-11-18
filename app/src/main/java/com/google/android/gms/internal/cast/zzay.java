package com.google.android.gms.internal.cast;

import android.content.Context;
import android.widget.ImageView;
import com.google.android.gms.cast.Cast.Listener;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;

public final class zzay extends UIController {
    private Listener zzaj;
    private final Context zzgs;
    private final ImageView zzpw;
    private final String zzqh = this.zzgs.getString(C0782R.string.cast_mute);
    private final String zzqi = this.zzgs.getString(C0782R.string.cast_unmute);

    public zzay(ImageView imageView, Context context) {
        this.zzpw = imageView;
        this.zzgs = context.getApplicationContext();
        this.zzpw.setEnabled(false);
        this.zzaj = null;
    }

    private final void zzh(boolean z) {
        this.zzpw.setSelected(z);
        this.zzpw.setContentDescription(z ? this.zzqh : this.zzqi);
    }

    public final void onMediaStatusUpdated() {
        zzby();
    }

    public final void onSendingRemoteMediaRequest() {
        this.zzpw.setEnabled(false);
    }

    public final void onSessionConnected(CastSession castSession) {
        if (this.zzaj == null) {
            this.zzaj = new zzaz(this);
        }
        super.onSessionConnected(castSession);
        castSession.addCastListener(this.zzaj);
        zzby();
    }

    public final void onSessionEnded() {
        this.zzpw.setEnabled(false);
        CastSession currentCastSession = CastContext.getSharedInstance(this.zzgs).getSessionManager().getCurrentCastSession();
        if (!(currentCastSession == null || this.zzaj == null)) {
            currentCastSession.removeCastListener(this.zzaj);
        }
        super.onSessionEnded();
    }

    protected final void zzby() {
        Session currentCastSession = CastContext.getSharedInstance(this.zzgs).getSessionManager().getCurrentCastSession();
        if (currentCastSession == null || !currentCastSession.isConnected()) {
            this.zzpw.setEnabled(false);
            return;
        }
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null) {
            if (remoteMediaClient.hasMediaSession()) {
                this.zzpw.setEnabled(true);
                if (currentCastSession.isMute()) {
                    zzh(false);
                } else {
                    zzh(true);
                }
            }
        }
        this.zzpw.setEnabled(false);
        if (currentCastSession.isMute()) {
            zzh(false);
        } else {
            zzh(true);
        }
    }
}
