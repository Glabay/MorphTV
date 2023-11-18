package com.google.android.gms.cast.framework.media;

import android.support.annotation.NonNull;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;

public class ImagePicker {
    public static final int IMAGE_TYPE_EXPANDED_CONTROLLER_BACKGROUND = 4;
    public static final int IMAGE_TYPE_LOCK_SCREEN_BACKGROUND = 3;
    public static final int IMAGE_TYPE_MEDIA_ROUTE_CONTROLLER_DIALOG_BACKGROUND = 0;
    public static final int IMAGE_TYPE_MINI_CONTROLLER_THUMBNAIL = 2;
    public static final int IMAGE_TYPE_NOTIFICATION_THUMBNAIL = 1;
    public static final int IMAGE_TYPE_UNKNOWN = -1;
    private final zzb zzkz = new zza();

    private class zza extends com.google.android.gms.cast.framework.media.zzb.zza {
        private final /* synthetic */ ImagePicker zzla;

        private zza(ImagePicker imagePicker) {
            this.zzla = imagePicker;
        }

        public final WebImage onPickImage(MediaMetadata mediaMetadata, int i) {
            return this.zzla.onPickImage(mediaMetadata, i);
        }

        public final WebImage zza(MediaMetadata mediaMetadata, ImageHints imageHints) {
            return this.zzla.onPickImage(mediaMetadata, imageHints);
        }

        public final IObjectWrapper zzaw() {
            return ObjectWrapper.wrap(this.zzla);
        }

        public final int zzm() {
            return 12451009;
        }
    }

    @Deprecated
    public WebImage onPickImage(MediaMetadata mediaMetadata, int i) {
        if (mediaMetadata != null) {
            if (mediaMetadata.hasImages()) {
                return (WebImage) mediaMetadata.getImages().get(0);
            }
        }
        return null;
    }

    public WebImage onPickImage(MediaMetadata mediaMetadata, @NonNull ImageHints imageHints) {
        return onPickImage(mediaMetadata, imageHints.getType());
    }

    public final zzb zzax() {
        return this.zzkz;
    }
}
