package com.google.android.gms.internal.cast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.ImageHints;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.cast.framework.media.MediaUtils;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;
import com.google.android.gms.common.images.WebImage;

public final class zzao extends UIController {
    private final ImagePicker zzkw;
    private final ImageHints zzow;
    private final ImageView zzpw;
    private final Bitmap zzpx;
    private final zzx zzpy;

    public zzao(ImageView imageView, Context context, @NonNull ImageHints imageHints, int i) {
        this.zzpw = imageView;
        this.zzow = imageHints;
        this.zzpx = BitmapFactory.decodeResource(context.getResources(), i);
        CastContext zzb = CastContext.zzb(context);
        ImagePicker imagePicker = null;
        if (zzb != null) {
            CastMediaOptions castMediaOptions = zzb.getCastOptions().getCastMediaOptions();
            if (castMediaOptions != null) {
                imagePicker = castMediaOptions.getImagePicker();
            }
        }
        this.zzkw = imagePicker;
        this.zzpy = new zzx(context.getApplicationContext());
    }

    private final void zzby() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null) {
            if (remoteMediaClient.hasMediaSession()) {
                MediaQueueItem preloadedItem = remoteMediaClient.getPreloadedItem();
                Uri uri = null;
                if (preloadedItem != null) {
                    MediaInfo media = preloadedItem.getMedia();
                    if (media != null) {
                        if (this.zzkw != null) {
                            WebImage onPickImage = this.zzkw.onPickImage(media.getMetadata(), this.zzow);
                            if (!(onPickImage == null || onPickImage.getUrl() == null)) {
                                uri = onPickImage.getUrl();
                            }
                        }
                        uri = MediaUtils.getImageUri(media, 0);
                    }
                }
                if (uri == null) {
                    this.zzpw.setImageBitmap(this.zzpx);
                    return;
                } else {
                    this.zzpy.zza(uri);
                    return;
                }
            }
        }
        this.zzpw.setImageBitmap(this.zzpx);
    }

    public final void onMediaStatusUpdated() {
        zzby();
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        this.zzpy.zza(new zzap(this));
        this.zzpw.setImageBitmap(this.zzpx);
        zzby();
    }

    public final void onSessionEnded() {
        this.zzpy.clear();
        this.zzpw.setImageBitmap(this.zzpx);
        super.onSessionEnded();
    }
}
