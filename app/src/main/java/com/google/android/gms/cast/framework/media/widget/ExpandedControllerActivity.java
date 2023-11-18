package com.google.android.gms.cast.framework.media.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.C0249R;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.gms.cast.AdBreakClipInfo;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.ImageHints;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.Listener;
import com.google.android.gms.cast.framework.media.uicontroller.UIMediaController;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.internal.cast.zzam;
import com.google.android.gms.internal.cast.zzat;
import com.google.android.gms.internal.cast.zzx;
import java.util.Timer;
import java.util.TimerTask;

public class ExpandedControllerActivity extends AppCompatActivity implements ControlButtonsContainer {
    private SessionManager zzgu;
    private final SessionManagerListener<CastSession> zzlq = new zzb();
    private final Listener zzps = new zza();
    private SeekBar zzqe;
    @DrawableRes
    private int zzra;
    @ColorRes
    private int zzrb;
    @DrawableRes
    private int zzrc;
    @DrawableRes
    private int zzrd;
    @DrawableRes
    private int zzre;
    @DrawableRes
    private int zzrf;
    @DrawableRes
    private int zzrg;
    @DrawableRes
    private int zzrh;
    @DrawableRes
    private int zzri;
    @DrawableRes
    private int zzrj;
    @DrawableRes
    private int zzrk;
    @DrawableRes
    private int zzrl;
    @DrawableRes
    private int zzrm;
    private int zzrn;
    private TextView zzro;
    private ImageView zzrp;
    private ImageView zzrq;
    private zzam zzrr;
    private int[] zzrs;
    private ImageView[] zzrt = new ImageView[4];
    private View zzru;
    private ImageView zzrv;
    private TextView zzrw;
    private TextView zzrx;
    private TextView zzry;
    private TextView zzrz;
    private zzx zzsa;
    private UIMediaController zzsb;
    private boolean zzsc;
    private boolean zzsd;
    private Timer zzse;

    private class zza implements Listener {
        private final /* synthetic */ ExpandedControllerActivity zzsf;

        private zza(ExpandedControllerActivity expandedControllerActivity) {
            this.zzsf = expandedControllerActivity;
        }

        public final void onAdBreakStatusUpdated() {
            this.zzsf.zzcf();
        }

        public final void onMetadataUpdated() {
            this.zzsf.zzcd();
        }

        public final void onPreloadStatusUpdated() {
        }

        public final void onQueueStatusUpdated() {
        }

        public final void onSendingRemoteMediaRequest() {
            this.zzsf.zzro.setText(this.zzsf.getResources().getString(C0782R.string.cast_expanded_controller_loading));
        }

        public final void onStatusUpdated() {
            RemoteMediaClient zzd = this.zzsf.getRemoteMediaClient();
            if (zzd != null) {
                if (zzd.hasMediaSession()) {
                    this.zzsf.zzsc = false;
                    this.zzsf.zzce();
                    this.zzsf.zzcf();
                    return;
                }
            }
            if (!this.zzsf.zzsc) {
                this.zzsf.finish();
            }
        }
    }

    private class zzb implements SessionManagerListener<CastSession> {
        private final /* synthetic */ ExpandedControllerActivity zzsf;

        private zzb(ExpandedControllerActivity expandedControllerActivity) {
            this.zzsf = expandedControllerActivity;
        }

        public final /* synthetic */ void onSessionEnded(Session session, int i) {
            this.zzsf.finish();
        }

        public final /* bridge */ /* synthetic */ void onSessionEnding(Session session) {
        }

        public final /* bridge */ /* synthetic */ void onSessionResumeFailed(Session session, int i) {
        }

        public final /* bridge */ /* synthetic */ void onSessionResumed(Session session, boolean z) {
        }

        public final /* bridge */ /* synthetic */ void onSessionResuming(Session session, String str) {
        }

        public final /* bridge */ /* synthetic */ void onSessionStartFailed(Session session, int i) {
        }

        public final /* bridge */ /* synthetic */ void onSessionStarted(Session session, String str) {
        }

        public final /* bridge */ /* synthetic */ void onSessionStarting(Session session) {
        }

        public final /* bridge */ /* synthetic */ void onSessionSuspended(Session session, int i) {
        }
    }

    private final RemoteMediaClient getRemoteMediaClient() {
        Session currentCastSession = this.zzgu.getCurrentCastSession();
        return (currentCastSession == null || !currentCastSession.isConnected()) ? null : currentCastSession.getRemoteMediaClient();
    }

    private final void zza(View view, int i, int i2, UIMediaController uIMediaController) {
        ImageView imageView = (ImageView) view.findViewById(i);
        if (i2 == C0782R.id.cast_button_type_empty) {
            imageView.setVisibility(4);
            return;
        }
        if (i2 != C0782R.id.cast_button_type_custom) {
            if (i2 == C0782R.id.cast_button_type_play_pause_toggle) {
                imageView.setBackgroundResource(this.zzra);
                Drawable zzb = zze.zzb(this, this.zzrn, this.zzrf);
                Drawable zzb2 = zze.zzb(this, this.zzrn, this.zzre);
                Drawable zzb3 = zze.zzb(this, this.zzrn, this.zzrg);
                imageView.setImageDrawable(zzb2);
                uIMediaController.bindImageViewToPlayPauseToggle(imageView, zzb2, zzb, zzb3, null, false);
            } else if (i2 == C0782R.id.cast_button_type_skip_previous) {
                imageView.setBackgroundResource(this.zzra);
                imageView.setImageDrawable(zze.zzb(this, this.zzrn, this.zzrh));
                imageView.setContentDescription(getResources().getString(C0782R.string.cast_skip_prev));
                uIMediaController.bindViewToSkipPrev(imageView, 0);
            } else if (i2 == C0782R.id.cast_button_type_skip_next) {
                imageView.setBackgroundResource(this.zzra);
                imageView.setImageDrawable(zze.zzb(this, this.zzrn, this.zzri));
                imageView.setContentDescription(getResources().getString(C0782R.string.cast_skip_next));
                uIMediaController.bindViewToSkipNext(imageView, 0);
            } else if (i2 == C0782R.id.cast_button_type_rewind_30_seconds) {
                imageView.setBackgroundResource(this.zzra);
                imageView.setImageDrawable(zze.zzb(this, this.zzrn, this.zzrj));
                imageView.setContentDescription(getResources().getString(C0782R.string.cast_rewind_30));
                uIMediaController.bindViewToRewind(imageView, 30000);
            } else if (i2 == C0782R.id.cast_button_type_forward_30_seconds) {
                imageView.setBackgroundResource(this.zzra);
                imageView.setImageDrawable(zze.zzb(this, this.zzrn, this.zzrk));
                imageView.setContentDescription(getResources().getString(C0782R.string.cast_forward_30));
                uIMediaController.bindViewToForward(imageView, 30000);
            } else if (i2 == C0782R.id.cast_button_type_mute_toggle) {
                imageView.setBackgroundResource(this.zzra);
                imageView.setImageDrawable(zze.zzb(this, this.zzrn, this.zzrl));
                uIMediaController.bindImageViewToMuteToggle(imageView);
            } else if (i2 == C0782R.id.cast_button_type_closed_caption) {
                imageView.setBackgroundResource(this.zzra);
                imageView.setImageDrawable(zze.zzb(this, this.zzrn, this.zzrm));
                uIMediaController.bindViewToClosedCaption(imageView);
            }
        }
    }

    private final void zza(AdBreakClipInfo adBreakClipInfo, RemoteMediaClient remoteMediaClient) {
        if (!this.zzsc && !remoteMediaClient.isBuffering()) {
            this.zzry.setVisibility(8);
            if (!(adBreakClipInfo == null || adBreakClipInfo.getWhenSkippableInMs() == -1)) {
                if (!this.zzsd) {
                    TimerTask zzc = new zzc(this, adBreakClipInfo, remoteMediaClient);
                    this.zzse = new Timer();
                    this.zzse.scheduleAtFixedRate(zzc, 0, 500);
                    this.zzsd = true;
                }
                if (((float) (adBreakClipInfo.getWhenSkippableInMs() - remoteMediaClient.getApproximateAdBreakClipPositionMs())) <= 0.0f) {
                    this.zzrz.setVisibility(8);
                    if (this.zzsd) {
                        this.zzse.cancel();
                        this.zzsd = false;
                    }
                    this.zzry.setVisibility(0);
                    this.zzry.setClickable(true);
                    return;
                }
                this.zzrz.setVisibility(0);
                this.zzrz.setText(String.format(getResources().getString(C0782R.string.cast_expanded_controller_skip_ad_text), new Object[]{Integer.valueOf(((int) r10) / 1000)}));
                this.zzry.setClickable(false);
            }
        }
    }

    private final ColorStateList zzcc() {
        int color = getResources().getColor(this.zzrb);
        TypedValue typedValue = new TypedValue();
        getResources().getValue(C0782R.dimen.cast_expanded_controller_seekbar_disabled_alpha, typedValue, true);
        int argb = Color.argb((int) (typedValue.getFloat() * ((float) Color.alpha(color))), Color.red(color), Color.green(color), Color.blue(color));
        int[] iArr = new int[]{color, argb};
        r0 = new int[2][];
        r0[0] = new int[]{16842910};
        r0[1] = new int[]{-16842910};
        return new ColorStateList(r0, iArr);
    }

    private final void zzcd() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            MediaInfo mediaInfo = remoteMediaClient.getMediaInfo();
            if (mediaInfo != null) {
                MediaMetadata metadata = mediaInfo.getMetadata();
                if (metadata != null) {
                    ActionBar supportActionBar = getSupportActionBar();
                    if (supportActionBar != null) {
                        supportActionBar.setTitle(metadata.getString(MediaMetadata.KEY_TITLE));
                    }
                }
            }
        }
    }

    private final void zzce() {
        CastSession currentCastSession = this.zzgu.getCurrentCastSession();
        if (currentCastSession != null) {
            CastDevice castDevice = currentCastSession.getCastDevice();
            if (castDevice != null) {
                if (!TextUtils.isEmpty(castDevice.getFriendlyName())) {
                    this.zzro.setText(getResources().getString(C0782R.string.cast_casting_to_device, new Object[]{r0}));
                    return;
                }
            }
        }
        this.zzro.setText("");
    }

    private final void zzcf() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        Object obj = null;
        MediaInfo mediaInfo = remoteMediaClient == null ? null : remoteMediaClient.getMediaInfo();
        MediaStatus mediaStatus = remoteMediaClient == null ? null : remoteMediaClient.getMediaStatus();
        Object obj2 = (mediaStatus == null || !mediaStatus.isPlayingAd()) ? null : 1;
        if (obj2 != null) {
            CharSequence title;
            this.zzqe.setEnabled(false);
            if (PlatformVersion.isAtLeastJellyBeanMR1() && this.zzrq.getVisibility() == 8) {
                Drawable drawable = this.zzrp.getDrawable();
                if (drawable != null && (drawable instanceof BitmapDrawable)) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    if (bitmap != null) {
                        bitmap = zze.zza(this, bitmap, 0.25f, 7.5f);
                        if (bitmap != null) {
                            this.zzrq.setImageBitmap(bitmap);
                            this.zzrq.setVisibility(0);
                        }
                    }
                }
            }
            AdBreakClipInfo currentAdBreakClip = mediaStatus.getCurrentAdBreakClip();
            if (currentAdBreakClip != null) {
                title = currentAdBreakClip.getTitle();
                obj = currentAdBreakClip.getImageUrl();
            } else {
                title = null;
            }
            this.zzrw.setVisibility(0);
            if (TextUtils.isEmpty(obj)) {
                this.zzrv.setVisibility(8);
            } else {
                this.zzsa.zza(Uri.parse(obj));
            }
            TextView textView = this.zzrx;
            if (TextUtils.isEmpty(title)) {
                title = getResources().getString(C0782R.string.cast_ad_label);
            }
            textView.setText(title);
            this.zzqe.setEnabled(false);
            this.zzru.setVisibility(0);
            zza(currentAdBreakClip, remoteMediaClient);
        } else {
            this.zzqe.setEnabled(true);
            this.zzrz.setVisibility(8);
            this.zzry.setVisibility(8);
            this.zzru.setVisibility(8);
            if (PlatformVersion.isAtLeastJellyBeanMR1()) {
                this.zzrq.setVisibility(8);
                this.zzrq.setImageBitmap(null);
            }
        }
        if (mediaInfo != null) {
            this.zzrr.zzj(this.zzqe.getMax());
            this.zzrr.zzb(mediaInfo.getAdBreaks(), -1);
        }
    }

    public final ImageView getButtonImageViewAt(int i) throws IndexOutOfBoundsException {
        return this.zzrt[i];
    }

    public final int getButtonSlotCount() {
        return 4;
    }

    public final int getButtonTypeAt(int i) throws IndexOutOfBoundsException {
        return this.zzrs[i];
    }

    public SeekBar getSeekBar() {
        return this.zzqe;
    }

    public TextView getStatusTextView() {
        return this.zzro;
    }

    public UIMediaController getUIMediaController() {
        return this.zzsb;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzgu = CastContext.getSharedInstance(this).getSessionManager();
        if (this.zzgu.getCurrentCastSession() == null) {
            finish();
        }
        this.zzsb = new UIMediaController(this);
        this.zzsb.setPostRemoteMediaClientListener(this.zzps);
        setContentView(C0782R.layout.cast_expanded_controller_activity);
        TypedArray obtainStyledAttributes = obtainStyledAttributes(new int[]{C0249R.attr.selectableItemBackgroundBorderless, C0249R.attr.colorControlActivated});
        this.zzra = obtainStyledAttributes.getResourceId(0, 0);
        this.zzrb = obtainStyledAttributes.getResourceId(1, 0);
        obtainStyledAttributes.recycle();
        ColorStateList colorStateList = null;
        obtainStyledAttributes = obtainStyledAttributes(null, C0782R.styleable.CastExpandedController, C0782R.attr.castExpandedControllerStyle, C0782R.style.CastExpandedController);
        this.zzrn = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castButtonColor, 0);
        this.zzrc = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castSeekBarProgressDrawable, 0);
        this.zzrd = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castSeekBarThumbDrawable, 0);
        this.zzre = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castPlayButtonDrawable, 0);
        this.zzrf = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castPauseButtonDrawable, 0);
        this.zzrg = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castStopButtonDrawable, 0);
        this.zzrh = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castSkipPreviousButtonDrawable, 0);
        this.zzri = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castSkipNextButtonDrawable, 0);
        this.zzrj = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castRewind30ButtonDrawable, 0);
        this.zzrk = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castForward30ButtonDrawable, 0);
        this.zzrl = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castMuteToggleButtonDrawable, 0);
        this.zzrm = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castClosedCaptionsButtonDrawable, 0);
        int resourceId = obtainStyledAttributes.getResourceId(C0782R.styleable.CastExpandedController_castControlButtons, 0);
        if (resourceId != 0) {
            TypedArray obtainTypedArray = getResources().obtainTypedArray(resourceId);
            Preconditions.checkArgument(obtainTypedArray.length() == 4);
            this.zzrs = new int[obtainTypedArray.length()];
            for (int i = 0; i < obtainTypedArray.length(); i++) {
                this.zzrs[i] = obtainTypedArray.getResourceId(i, 0);
            }
            obtainTypedArray.recycle();
        } else {
            this.zzrs = new int[]{C0782R.id.cast_button_type_empty, C0782R.id.cast_button_type_empty, C0782R.id.cast_button_type_empty, C0782R.id.cast_button_type_empty};
        }
        obtainStyledAttributes.recycle();
        View findViewById = findViewById(C0782R.id.expanded_controller_layout);
        UIMediaController uIMediaController = this.zzsb;
        this.zzrp = (ImageView) findViewById.findViewById(C0782R.id.background_image_view);
        this.zzrq = (ImageView) findViewById.findViewById(C0782R.id.blurred_background_image_view);
        View findViewById2 = findViewById.findViewById(C0782R.id.background_place_holder_image_view);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        uIMediaController.bindImageViewToImageOfCurrentItem(this.zzrp, new ImageHints(4, displayMetrics.widthPixels, displayMetrics.heightPixels), findViewById2);
        this.zzro = (TextView) findViewById.findViewById(C0782R.id.status_text);
        uIMediaController.bindViewToLoadingIndicator((ProgressBar) findViewById.findViewById(C0782R.id.loading_indicator));
        TextView textView = (TextView) findViewById.findViewById(C0782R.id.start_text);
        TextView textView2 = (TextView) findViewById.findViewById(C0782R.id.end_text);
        ImageView imageView = (ImageView) findViewById.findViewById(C0782R.id.live_stream_indicator);
        this.zzqe = (SeekBar) findViewById.findViewById(C0782R.id.seek_bar);
        Drawable drawable = getResources().getDrawable(this.zzrc);
        if (drawable != null) {
            if (this.zzrc == C0782R.drawable.cast_expanded_controller_seekbar_track) {
                colorStateList = zzcc();
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                Drawable wrap = DrawableCompat.wrap(layerDrawable.findDrawableByLayerId(16908301));
                DrawableCompat.setTintList(wrap, colorStateList);
                layerDrawable.setDrawableByLayerId(16908301, wrap);
                layerDrawable.findDrawableByLayerId(16908288).setColorFilter(getResources().getColor(C0782R.color.cast_expanded_controller_seek_bar_progress_background_tint_color), Mode.SRC_IN);
            }
            this.zzqe.setProgressDrawable(drawable);
        }
        drawable = getResources().getDrawable(this.zzrd);
        if (drawable != null) {
            if (this.zzrd == C0782R.drawable.cast_expanded_controller_seekbar_thumb) {
                if (colorStateList == null) {
                    colorStateList = zzcc();
                }
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTintList(drawable, colorStateList);
            }
            this.zzqe.setThumb(drawable);
        }
        if (PlatformVersion.isAtLeastLollipop()) {
            this.zzqe.setSplitTrack(false);
        }
        SeekBar seekBar = (SeekBar) findViewById.findViewById(C0782R.id.live_stream_seek_bar);
        uIMediaController.bindTextViewToStreamPosition(textView, true);
        uIMediaController.bindTextViewToStreamDuration(textView2, imageView);
        uIMediaController.bindSeekBar(this.zzqe);
        uIMediaController.bindViewToUIController(seekBar, new zzat(seekBar, this.zzqe));
        this.zzrt[0] = (ImageView) findViewById.findViewById(C0782R.id.button_0);
        this.zzrt[1] = (ImageView) findViewById.findViewById(C0782R.id.button_1);
        this.zzrt[2] = (ImageView) findViewById.findViewById(C0782R.id.button_2);
        this.zzrt[3] = (ImageView) findViewById.findViewById(C0782R.id.button_3);
        zza(findViewById, C0782R.id.button_0, this.zzrs[0], uIMediaController);
        zza(findViewById, C0782R.id.button_1, this.zzrs[1], uIMediaController);
        zza(findViewById, C0782R.id.button_play_pause_toggle, C0782R.id.cast_button_type_play_pause_toggle, uIMediaController);
        zza(findViewById, C0782R.id.button_2, this.zzrs[2], uIMediaController);
        zza(findViewById, C0782R.id.button_3, this.zzrs[3], uIMediaController);
        this.zzru = findViewById(C0782R.id.ad_container);
        this.zzrv = (ImageView) this.zzru.findViewById(C0782R.id.ad_image_view);
        this.zzrx = (TextView) this.zzru.findViewById(C0782R.id.ad_label);
        this.zzrw = (TextView) this.zzru.findViewById(C0782R.id.ad_in_progress_label);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById.findViewById(C0782R.id.seek_bar_controls);
        findViewById = new zzam(this);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(0, C0782R.id.end_text);
        layoutParams.addRule(1, C0782R.id.start_text);
        layoutParams.addRule(6, C0782R.id.seek_bar);
        layoutParams.addRule(7, C0782R.id.seek_bar);
        layoutParams.addRule(5, C0782R.id.seek_bar);
        layoutParams.addRule(8, C0782R.id.seek_bar);
        findViewById.setLayoutParams(layoutParams);
        if (PlatformVersion.isAtLeastJellyBeanMR1()) {
            findViewById.setPaddingRelative(this.zzqe.getPaddingStart(), this.zzqe.getPaddingTop(), this.zzqe.getPaddingEnd(), this.zzqe.getPaddingBottom());
        } else {
            findViewById.setPadding(this.zzqe.getPaddingLeft(), this.zzqe.getPaddingTop(), this.zzqe.getPaddingRight(), this.zzqe.getPaddingBottom());
        }
        findViewById.setContentDescription(getResources().getString(C0782R.string.cast_seek_bar));
        findViewById.setBackgroundColor(0);
        relativeLayout.addView(findViewById);
        this.zzrr = findViewById;
        this.zzrz = (TextView) findViewById(C0782R.id.ad_skip_text);
        this.zzry = (TextView) findViewById(C0782R.id.ad_skip_button);
        this.zzry.setOnClickListener(new zzb(this));
        setSupportActionBar((Toolbar) findViewById(C0782R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(C0782R.drawable.quantum_ic_keyboard_arrow_down_white_36);
        }
        zzce();
        zzcd();
        this.zzsa = new zzx(getApplicationContext(), new ImageHints(-1, this.zzrv.getWidth(), this.zzrv.getHeight()));
        this.zzsa.zza(new zza(this));
    }

    protected void onDestroy() {
        this.zzsa.clear();
        if (this.zzsb != null) {
            this.zzsb.setPostRemoteMediaClientListener(null);
            this.zzsb.dispose();
        }
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return true;
    }

    protected void onPause() {
        CastContext.getSharedInstance(this).getSessionManager().removeSessionManagerListener(this.zzlq, CastSession.class);
        super.onPause();
    }

    protected void onResume() {
        boolean z;
        CastContext.getSharedInstance(this).getSessionManager().addSessionManagerListener(this.zzlq, CastSession.class);
        Session currentCastSession = CastContext.getSharedInstance(this).getSessionManager().getCurrentCastSession();
        if (currentCastSession == null || !(currentCastSession.isConnected() || currentCastSession.isConnecting())) {
            finish();
        }
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null) {
            if (remoteMediaClient.hasMediaSession()) {
                z = false;
                this.zzsc = z;
                zzce();
                zzcf();
                super.onResume();
            }
        }
        z = true;
        this.zzsc = z;
        zzce();
        zzcf();
        super.onResume();
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility() ^ 2;
            if (PlatformVersion.isAtLeastJellyBean()) {
                systemUiVisibility ^= 4;
            }
            if (PlatformVersion.isAtLeastKitKat()) {
                systemUiVisibility ^= 4096;
            }
            getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
            if (PlatformVersion.isAtLeastJellyBeanMR2()) {
                setImmersive(true);
            }
        }
    }
}
