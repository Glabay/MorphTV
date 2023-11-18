package com.google.android.gms.internal.cast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.ImageHints;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.cast.framework.media.MediaUtils;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;
import com.google.android.gms.common.images.WebImage;

public final class zzaq extends UIController {
    private final ImagePicker zzkw;
    private final ImageHints zzow;
    private final ImageView zzpw;
    private final zzx zzpy;
    private final Bitmap zzqa;
    private final View zzqb;

    public zzaq(ImageView imageView, Context context, @NonNull ImageHints imageHints, int i, View view) {
        this.zzpw = imageView;
        this.zzow = imageHints;
        ImagePicker imagePicker = null;
        this.zzqa = i != 0 ? BitmapFactory.decodeResource(context.getResources(), i) : null;
        this.zzqb = view;
        CastContext zzb = CastContext.zzb(context);
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
                Uri uri;
                MediaInfo mediaInfo = remoteMediaClient.getMediaInfo();
                if (mediaInfo == null) {
                    uri = null;
                } else {
                    if (this.zzkw != null) {
                        WebImage onPickImage = this.zzkw.onPickImage(mediaInfo.getMetadata(), this.zzow);
                        if (!(onPickImage == null || onPickImage.getUrl() == null)) {
                            uri = onPickImage.getUrl();
                        }
                    }
                    uri = MediaUtils.getImageUri(mediaInfo, 0);
                }
                if (uri == null) {
                    zzbz();
                    return;
                } else {
                    this.zzpy.zza(uri);
                    return;
                }
            }
        }
        zzbz();
    }

    private final void zzbz() {
        if (this.zzqb != null) {
            this.zzqb.setVisibility(0);
            this.zzpw.setVisibility(4);
        }
        if (this.zzqa != null) {
            this.zzpw.setImageBitmap(this.zzqa);
        }
    }

    public final void onMediaStatusUpdated() {
        zzby();
    }

    public final void onSessionConnected(CastSession castSession) {
        super.onSessionConnected(castSession);
        this.zzpy.zza(new zzar(this));
        zzbz();
        zzby();
    }

    public final void onSessionEnded() {
        this.zzpy.clear();
        zzbz();
        super.onSessionEnded();
    }
}
