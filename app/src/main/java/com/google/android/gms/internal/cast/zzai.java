package com.google.android.gms.internal.cast;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.Callback;
import android.support.v4.media.session.PlaybackStateCompat.Builder;
import android.text.TextUtils;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.ReconnectionService;
import com.google.android.gms.cast.framework.media.MediaNotificationService;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.Listener;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.common.util.PlatformVersion;

public final class zzai implements Listener {
    private CastDevice zzai;
    private final Context zzgs;
    private RemoteMediaClient zzhp;
    private final zzw zzip;
    private boolean zzld;
    private final CastOptions zzpg;
    private final ComponentName zzph;
    private final zzx zzpi;
    private final zzx zzpj;
    private MediaSessionCompat zzpk;
    private Callback zzpl;

    public zzai(Context context, CastOptions castOptions, zzw zzw) {
        this.zzgs = context;
        this.zzpg = castOptions;
        this.zzip = zzw;
        ComponentName componentName = (this.zzpg.getCastMediaOptions() == null || TextUtils.isEmpty(this.zzpg.getCastMediaOptions().getExpandedControllerActivityClassName())) ? null : new ComponentName(this.zzgs, this.zzpg.getCastMediaOptions().getExpandedControllerActivityClassName());
        this.zzph = componentName;
        this.zzpi = new zzx(this.zzgs);
        this.zzpi.zza(new zzaj(this));
        this.zzpj = new zzx(this.zzgs);
        this.zzpj.zza(new zzak(this));
    }

    private final Uri zza(MediaMetadata mediaMetadata, int i) {
        WebImage onPickImage = this.zzpg.getCastMediaOptions().getImagePicker() != null ? this.zzpg.getCastMediaOptions().getImagePicker().onPickImage(mediaMetadata, i) : mediaMetadata.hasImages() ? (WebImage) mediaMetadata.getImages().get(0) : null;
        return onPickImage == null ? null : onPickImage.getUrl();
    }

    private final void zza(int i, MediaInfo mediaInfo) {
        if (i == 0) {
            this.zzpk.setPlaybackState(new Builder().setState(0, 0, 1.0f).build());
            this.zzpk.setMetadata(new MediaMetadataCompat.Builder().build());
            return;
        }
        PendingIntent pendingIntent;
        this.zzpk.setPlaybackState(new Builder().setState(i, 0, 1.0f).setActions(mediaInfo.getStreamType() == 2 ? 5 : 512).build());
        MediaSessionCompat mediaSessionCompat = this.zzpk;
        if (this.zzph == null) {
            pendingIntent = null;
        } else {
            Intent intent = new Intent();
            intent.setComponent(this.zzph);
            pendingIntent = PendingIntent.getActivity(this.zzgs, 0, intent, 134217728);
        }
        mediaSessionCompat.setSessionActivity(pendingIntent);
        MediaMetadata metadata = mediaInfo.getMetadata();
        this.zzpk.setMetadata(zzbt().putString("android.media.metadata.TITLE", metadata.getString(MediaMetadata.KEY_TITLE)).putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, metadata.getString(MediaMetadata.KEY_TITLE)).putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, metadata.getString(MediaMetadata.KEY_SUBTITLE)).putLong("android.media.metadata.DURATION", mediaInfo.getStreamDuration()).build());
        Uri zza = zza(metadata, 0);
        if (zza != null) {
            this.zzpi.zza(zza);
        } else {
            zza(null, 0);
        }
        Uri zza2 = zza(metadata, 3);
        if (zza2 != null) {
            this.zzpj.zza(zza2);
        } else {
            zza(null, 3);
        }
    }

    private final void zza(Bitmap bitmap, int i) {
        if (i == 0) {
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
                bitmap.eraseColor(0);
            }
            this.zzpk.setMetadata(zzbt().putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap).build());
            return;
        }
        if (i == 3) {
            this.zzpk.setMetadata(zzbt().putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap).build());
        }
    }

    private final MediaMetadataCompat.Builder zzbt() {
        MediaMetadataCompat metadata = this.zzpk.getController().getMetadata();
        return metadata == null ? new MediaMetadataCompat.Builder() : new MediaMetadataCompat.Builder(metadata);
    }

    private final void zzbu() {
        if (this.zzpg.getCastMediaOptions().getNotificationOptions() != null) {
            Intent intent = new Intent(this.zzgs, MediaNotificationService.class);
            intent.setPackage(this.zzgs.getPackageName());
            intent.setAction(MediaNotificationService.ACTION_UPDATE_NOTIFICATION);
            this.zzgs.stopService(intent);
        }
    }

    private final void zzbv() {
        if (this.zzpg.getEnableReconnectionService()) {
            Intent intent = new Intent(this.zzgs, ReconnectionService.class);
            intent.setPackage(this.zzgs.getPackageName());
            this.zzgs.stopService(intent);
        }
    }

    public final void onAdBreakStatusUpdated() {
        zzg(false);
    }

    public final void onMetadataUpdated() {
        zzg(false);
    }

    public final void onPreloadStatusUpdated() {
        zzg(false);
    }

    public final void onQueueStatusUpdated() {
        zzg(false);
    }

    public final void onSendingRemoteMediaRequest() {
    }

    public final void onStatusUpdated() {
        zzg(false);
    }

    public final void zza(RemoteMediaClient remoteMediaClient, CastDevice castDevice) {
        if (!this.zzld && this.zzpg != null && this.zzpg.getCastMediaOptions() != null && remoteMediaClient != null && castDevice != null) {
            this.zzhp = remoteMediaClient;
            this.zzhp.addListener(this);
            this.zzai = castDevice;
            if (!PlatformVersion.isAtLeastLollipop()) {
                ((AudioManager) this.zzgs.getSystemService(MimeTypes.BASE_TYPE_AUDIO)).requestAudioFocus(null, 3, 3);
            }
            ComponentName componentName = new ComponentName(this.zzgs, this.zzpg.getCastMediaOptions().getMediaIntentReceiverClassName());
            Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
            intent.setComponent(componentName);
            this.zzpk = new MediaSessionCompat(this.zzgs, "CastMediaSession", componentName, PendingIntent.getBroadcast(this.zzgs, 0, intent, 0));
            this.zzpk.setFlags(3);
            zza(0, null);
            if (!(this.zzai == null || TextUtils.isEmpty(this.zzai.getFriendlyName()))) {
                this.zzpk.setMetadata(new MediaMetadataCompat.Builder().putString("android.media.metadata.ALBUM_ARTIST", this.zzgs.getResources().getString(C0782R.string.cast_casting_to_device, new Object[]{this.zzai.getFriendlyName()})).build());
            }
            this.zzpl = new zzal(this);
            this.zzpk.setCallback(this.zzpl);
            this.zzpk.setActive(true);
            this.zzip.setMediaSessionCompat(this.zzpk);
            this.zzld = true;
            zzg(false);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzg(boolean r12) {
        /*
        r11 = this;
        r0 = r11.zzhp;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = r11.zzhp;
        r0 = r0.getMediaStatus();
        r1 = 0;
        if (r0 != 0) goto L_0x0010;
    L_0x000e:
        r2 = r1;
        goto L_0x0014;
    L_0x0010:
        r2 = r0.getMediaInfo();
    L_0x0014:
        if (r2 != 0) goto L_0x0018;
    L_0x0016:
        r3 = r1;
        goto L_0x001c;
    L_0x0018:
        r3 = r2.getMetadata();
    L_0x001c:
        r4 = 3;
        r5 = 6;
        r6 = 2;
        r7 = 1;
        r8 = 0;
        if (r0 == 0) goto L_0x0066;
    L_0x0023:
        if (r2 == 0) goto L_0x0066;
    L_0x0025:
        if (r3 != 0) goto L_0x0028;
    L_0x0027:
        goto L_0x0066;
    L_0x0028:
        r3 = r11.zzhp;
        r3 = r3.getPlayerState();
        switch(r3) {
            case 1: goto L_0x003a;
            case 2: goto L_0x0037;
            case 3: goto L_0x0034;
            case 4: goto L_0x0032;
            default: goto L_0x0031;
        };
    L_0x0031:
        goto L_0x0066;
    L_0x0032:
        r3 = 0;
        goto L_0x0068;
    L_0x0034:
        r3 = 0;
    L_0x0035:
        r5 = 2;
        goto L_0x0068;
    L_0x0037:
        r3 = 0;
        r5 = 3;
        goto L_0x0068;
    L_0x003a:
        r3 = r0.getIdleReason();
        r9 = r11.zzhp;
        r9 = r9.isLiveStream();
        if (r9 == 0) goto L_0x004a;
    L_0x0046:
        if (r3 != r6) goto L_0x004a;
    L_0x0048:
        r9 = 1;
        goto L_0x004b;
    L_0x004a:
        r9 = 0;
    L_0x004b:
        r10 = r0.getLoadingItemId();
        if (r10 == 0) goto L_0x0057;
    L_0x0051:
        if (r3 == r7) goto L_0x0055;
    L_0x0053:
        if (r3 != r4) goto L_0x0057;
    L_0x0055:
        r3 = 1;
        goto L_0x0058;
    L_0x0057:
        r3 = 0;
    L_0x0058:
        if (r9 == 0) goto L_0x005b;
    L_0x005a:
        goto L_0x0035;
    L_0x005b:
        r0 = r0.getQueueItemById(r10);
        if (r0 == 0) goto L_0x0067;
    L_0x0061:
        r2 = r0.getMedia();
        goto L_0x0068;
    L_0x0066:
        r3 = 0;
    L_0x0067:
        r5 = 0;
    L_0x0068:
        r11.zza(r5, r2);
        if (r5 != 0) goto L_0x0074;
    L_0x006d:
        r11.zzbu();
        r11.zzbv();
        return;
    L_0x0074:
        r0 = r11.zzpg;
        r0 = r0.getCastMediaOptions();
        r0 = r0.getNotificationOptions();
        if (r0 == 0) goto L_0x010e;
    L_0x0080:
        r0 = r11.zzhp;
        if (r0 == 0) goto L_0x010e;
    L_0x0084:
        r0 = new android.content.Intent;
        r2 = r11.zzgs;
        r4 = com.google.android.gms.cast.framework.media.MediaNotificationService.class;
        r0.<init>(r2, r4);
        r2 = "extra_media_notification_force_update";
        r0.putExtra(r2, r12);
        r12 = r11.zzgs;
        r12 = r12.getPackageName();
        r0.setPackage(r12);
        r12 = "com.google.android.gms.cast.framework.action.UPDATE_NOTIFICATION";
        r0.setAction(r12);
        r12 = "extra_media_info";
        r2 = r11.zzhp;
        r2 = r2.getMediaInfo();
        r0.putExtra(r12, r2);
        r12 = "extra_remote_media_client_player_state";
        r2 = r11.zzhp;
        r2 = r2.getPlayerState();
        r0.putExtra(r12, r2);
        r12 = "extra_cast_device";
        r2 = r11.zzai;
        r0.putExtra(r12, r2);
        r12 = "extra_media_session_token";
        r2 = r11.zzpk;
        if (r2 != 0) goto L_0x00c4;
    L_0x00c3:
        goto L_0x00ca;
    L_0x00c4:
        r1 = r11.zzpk;
        r1 = r1.getSessionToken();
    L_0x00ca:
        r0.putExtra(r12, r1);
        r12 = r11.zzhp;
        r12 = r12.getMediaStatus();
        if (r12 == 0) goto L_0x0109;
    L_0x00d5:
        r1 = r12.getQueueRepeatMode();
        switch(r1) {
            case 1: goto L_0x00ee;
            case 2: goto L_0x00ee;
            case 3: goto L_0x00ee;
            default: goto L_0x00dc;
        };
    L_0x00dc:
        r1 = r12.getCurrentItemId();
        r1 = r12.getIndexById(r1);
        if (r1 == 0) goto L_0x00fd;
    L_0x00e6:
        r2 = r1.intValue();
        if (r2 <= 0) goto L_0x00f0;
    L_0x00ec:
        r2 = 1;
        goto L_0x00f1;
    L_0x00ee:
        r2 = 1;
        goto L_0x00ff;
    L_0x00f0:
        r2 = 0;
    L_0x00f1:
        r1 = r1.intValue();
        r12 = r12.getQueueItemCount();
        r12 = r12 - r7;
        if (r1 >= r12) goto L_0x00fe;
    L_0x00fc:
        goto L_0x00ff;
    L_0x00fd:
        r2 = 0;
    L_0x00fe:
        r7 = 0;
    L_0x00ff:
        r12 = "extra_can_skip_next";
        r0.putExtra(r12, r7);
        r12 = "extra_can_skip_prev";
        r0.putExtra(r12, r2);
    L_0x0109:
        r12 = r11.zzgs;
        r12.startService(r0);
    L_0x010e:
        if (r3 != 0) goto L_0x012f;
    L_0x0110:
        r12 = r11.zzpg;
        r12 = r12.getEnableReconnectionService();
        if (r12 == 0) goto L_0x012f;
    L_0x0118:
        r12 = new android.content.Intent;
        r0 = r11.zzgs;
        r1 = com.google.android.gms.cast.framework.ReconnectionService.class;
        r12.<init>(r0, r1);
        r0 = r11.zzgs;
        r0 = r0.getPackageName();
        r12.setPackage(r0);
        r0 = r11.zzgs;
        r0.startService(r12);
    L_0x012f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzai.zzg(boolean):void");
    }

    public final void zzi(int i) {
        if (this.zzld) {
            this.zzld = false;
            if (this.zzhp != null) {
                this.zzhp.removeListener(this);
            }
            if (!PlatformVersion.isAtLeastLollipop()) {
                ((AudioManager) this.zzgs.getSystemService(MimeTypes.BASE_TYPE_AUDIO)).abandonAudioFocus(null);
            }
            this.zzip.setMediaSessionCompat(null);
            if (this.zzpi != null) {
                this.zzpi.clear();
            }
            if (this.zzpj != null) {
                this.zzpj.clear();
            }
            if (this.zzpk != null) {
                this.zzpk.setSessionActivity(null);
                this.zzpk.setCallback(null);
                this.zzpk.setMetadata(new MediaMetadataCompat.Builder().build());
                zza(0, null);
                this.zzpk.setActive(false);
                this.zzpk.release();
                this.zzpk = null;
            }
            this.zzhp = null;
            this.zzai = null;
            this.zzpl = null;
            zzbu();
            if (i == 0) {
                zzbv();
            }
        }
    }
}
