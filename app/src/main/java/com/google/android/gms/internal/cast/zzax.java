package com.google.android.gms.internal.cast;

import android.widget.TextView;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;
import java.util.ArrayList;
import java.util.List;

public final class zzax extends UIController {
    private final TextView zzqf;
    private final List<String> zzqg = new ArrayList();

    public zzax(TextView textView, List<String> list) {
        this.zzqf = textView;
        this.zzqg.addAll(list);
    }

    public final void onMediaStatusUpdated() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            MediaInfo mediaInfo = remoteMediaClient.getMediaStatus().getMediaInfo();
            if (mediaInfo != null) {
                MediaMetadata metadata = mediaInfo.getMetadata();
                if (metadata != null) {
                    for (String str : this.zzqg) {
                        if (metadata.containsKey(str)) {
                            this.zzqf.setText(metadata.getString(str));
                            return;
                        }
                    }
                    this.zzqf.setText("");
                }
            }
        }
    }
}
