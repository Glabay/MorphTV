package com.google.android.gms.internal.cast;

import android.content.Context;
import android.view.View;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;
import java.util.List;

public final class zzan extends UIController {
    private final View view;
    private final String zzlz;
    private final String zzpv;

    public zzan(View view, Context context) {
        this.view = view;
        this.zzlz = context.getString(C0782R.string.cast_closed_captions);
        this.zzpv = context.getString(C0782R.string.cast_closed_captions_unavailable);
        this.view.setEnabled(false);
    }

    private final void zzby() {
        View view;
        CharSequence charSequence;
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            Object obj;
            MediaInfo mediaInfo = remoteMediaClient.getMediaInfo();
            if (mediaInfo != null) {
                List<MediaTrack> mediaTracks = mediaInfo.getMediaTracks();
                if (mediaTracks != null) {
                    if (!mediaTracks.isEmpty()) {
                        int i = 0;
                        for (MediaTrack mediaTrack : mediaTracks) {
                            if (mediaTrack.getType() == 2) {
                                i++;
                                if (i > 1) {
                                }
                            } else if (mediaTrack.getType() == 1) {
                            }
                            obj = 1;
                        }
                    }
                }
            }
            obj = null;
            if (obj != null) {
                if (!remoteMediaClient.isPlayingAd()) {
                    this.view.setEnabled(true);
                    view = this.view;
                    charSequence = this.zzlz;
                    view.setContentDescription(charSequence);
                }
            }
        }
        this.view.setEnabled(false);
        view = this.view;
        charSequence = this.zzpv;
        view.setContentDescription(charSequence);
    }

    public final void onMediaStatusUpdated() {
        zzby();
    }

    public final void onSendingRemoteMediaRequest() {
        this.view.setEnabled(false);
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        this.view.setEnabled(true);
        zzby();
    }

    public final void onSessionEnded() {
        this.view.setEnabled(false);
        super.onSessionEnded();
    }
}
