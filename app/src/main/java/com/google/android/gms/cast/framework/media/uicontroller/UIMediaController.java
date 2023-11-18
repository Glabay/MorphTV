package com.google.android.gms.cast.framework.media.uicontroller;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.ImageHints;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.Listener;
import com.google.android.gms.cast.framework.media.TracksChooserDialogFragment;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.cast.zzan;
import com.google.android.gms.internal.cast.zzao;
import com.google.android.gms.internal.cast.zzaq;
import com.google.android.gms.internal.cast.zzas;
import com.google.android.gms.internal.cast.zzav;
import com.google.android.gms.internal.cast.zzaw;
import com.google.android.gms.internal.cast.zzax;
import com.google.android.gms.internal.cast.zzay;
import com.google.android.gms.internal.cast.zzba;
import com.google.android.gms.internal.cast.zzbb;
import com.google.android.gms.internal.cast.zzbc;
import com.google.android.gms.internal.cast.zzbd;
import com.google.android.gms.internal.cast.zzbe;
import com.google.android.gms.internal.cast.zzbf;
import com.google.android.gms.internal.cast.zzbg;
import com.google.android.gms.internal.cast.zzbh;
import com.google.android.gms.internal.cast.zzbi;
import com.google.android.gms.internal.cast.zzbj;
import com.google.android.gms.internal.cast.zzbk;
import com.google.android.gms.internal.cast.zzdg;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UIMediaController implements SessionManagerListener<CastSession>, Listener {
    private static final zzdg zzbd = new zzdg("UIMediaController");
    private final SessionManager zzgu;
    private RemoteMediaClient zzhp;
    private final Activity zzhv;
    private final Map<View, List<UIController>> zzpq = new HashMap();
    private final Set<zzbi> zzpr = new HashSet();
    private Listener zzps;

    public UIMediaController(Activity activity) {
        this.zzhv = activity;
        CastContext zzb = CastContext.zzb(activity);
        this.zzgu = zzb != null ? zzb.getSessionManager() : null;
        if (this.zzgu != null) {
            SessionManager sessionManager = CastContext.getSharedInstance(activity).getSessionManager();
            sessionManager.addSessionManagerListener(this, CastSession.class);
            zza(sessionManager.getCurrentCastSession());
        }
    }

    private final void zza(View view, UIController uIController) {
        if (this.zzgu != null) {
            List list = (List) this.zzpq.get(view);
            if (list == null) {
                list = new ArrayList();
                this.zzpq.put(view, list);
            }
            list.add(uIController);
            if (isActive()) {
                uIController.onSessionConnected(this.zzgu.getCurrentCastSession());
                zzbx();
            }
        }
    }

    private final void zza(Session session) {
        if (!isActive() && (session instanceof CastSession) && session.isConnected()) {
            CastSession castSession = (CastSession) session;
            this.zzhp = castSession.getRemoteMediaClient();
            if (this.zzhp != null) {
                this.zzhp.addListener(this);
                for (List<UIController> it : this.zzpq.values()) {
                    for (UIController onSessionConnected : it) {
                        onSessionConnected.onSessionConnected(castSession);
                    }
                }
                zzbx();
            }
        }
    }

    private final void zzbw() {
        if (isActive()) {
            for (List<UIController> it : this.zzpq.values()) {
                for (UIController onSessionEnded : it) {
                    onSessionEnded.onSessionEnded();
                }
            }
            this.zzhp.removeListener(this);
            this.zzhp = null;
        }
    }

    private final void zzbx() {
        for (List<UIController> it : this.zzpq.values()) {
            for (UIController onMediaStatusUpdated : it) {
                onMediaStatusUpdated.onMediaStatusUpdated();
            }
        }
    }

    @Deprecated
    public void bindImageViewToImageOfCurrentItem(ImageView imageView, int i, @DrawableRes int i2) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(imageView, new zzaq(imageView, this.zzhv, new ImageHints(i, 0, 0), i2, null));
    }

    @Deprecated
    public void bindImageViewToImageOfCurrentItem(ImageView imageView, int i, View view) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(imageView, new zzaq(imageView, this.zzhv, new ImageHints(i, 0, 0), 0, view));
    }

    public void bindImageViewToImageOfCurrentItem(ImageView imageView, @NonNull ImageHints imageHints, @DrawableRes int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(imageView, new zzaq(imageView, this.zzhv, imageHints, i, null));
    }

    public void bindImageViewToImageOfCurrentItem(ImageView imageView, @NonNull ImageHints imageHints, View view) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(imageView, new zzaq(imageView, this.zzhv, imageHints, 0, view));
    }

    @Deprecated
    public void bindImageViewToImageOfPreloadedItem(ImageView imageView, int i, @DrawableRes int i2) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(imageView, new zzao(imageView, this.zzhv, new ImageHints(i, 0, 0), i2));
    }

    public void bindImageViewToImageOfPreloadedItem(ImageView imageView, @NonNull ImageHints imageHints, @DrawableRes int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(imageView, new zzao(imageView, this.zzhv, imageHints, i));
    }

    public void bindImageViewToMuteToggle(ImageView imageView) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        imageView.setOnClickListener(new zzi(this));
        zza(imageView, new zzay(imageView, this.zzhv));
    }

    public void bindImageViewToPlayPauseToggle(@NonNull ImageView imageView, @NonNull Drawable drawable, @NonNull Drawable drawable2, Drawable drawable3, View view, boolean z) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        imageView.setOnClickListener(new zza(this));
        zza(imageView, new zzba(imageView, this.zzhv, drawable, drawable2, drawable3, view, z));
    }

    public void bindProgressBar(ProgressBar progressBar) {
        bindProgressBar(progressBar, 1000);
    }

    public void bindProgressBar(ProgressBar progressBar, long j) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(progressBar, new zzbb(progressBar, j));
    }

    public void bindSeekBar(SeekBar seekBar) {
        bindSeekBar(seekBar, 1000);
    }

    public void bindSeekBar(SeekBar seekBar, long j) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        seekBar.setOnSeekBarChangeListener(new zzf(this));
        zza(seekBar, new zzbc(seekBar, j));
    }

    public void bindTextViewToMetadataOfCurrentItem(TextView textView, String str) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        bindTextViewToMetadataOfCurrentItem(textView, Collections.singletonList(str));
    }

    public void bindTextViewToMetadataOfCurrentItem(TextView textView, List<String> list) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(textView, new zzax(textView, list));
    }

    public void bindTextViewToMetadataOfPreloadedItem(TextView textView, String str) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        bindTextViewToMetadataOfPreloadedItem(textView, Collections.singletonList(str));
    }

    public void bindTextViewToMetadataOfPreloadedItem(TextView textView, List<String> list) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(textView, new zzaw(textView, list));
    }

    public void bindTextViewToSmartSubtitle(TextView textView) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(textView, new zzbg(textView));
    }

    public void bindTextViewToStreamDuration(TextView textView) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(textView, new zzbh(textView, this.zzhv.getString(C0782R.string.cast_invalid_stream_duration_text), null));
    }

    public void bindTextViewToStreamDuration(TextView textView, View view) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(textView, new zzbh(textView, this.zzhv.getString(C0782R.string.cast_invalid_stream_duration_text), view));
    }

    public void bindTextViewToStreamPosition(TextView textView, boolean z) {
        bindTextViewToStreamPosition(textView, z, 1000);
    }

    public void bindTextViewToStreamPosition(TextView textView, boolean z, long j) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        UIController zzbi = new zzbi(textView, j, this.zzhv.getString(C0782R.string.cast_invalid_stream_position_text));
        if (z) {
            this.zzpr.add(zzbi);
        }
        zza(textView, zzbi);
    }

    public void bindViewToClosedCaption(View view) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        view.setOnClickListener(new zzh(this));
        zza(view, new zzan(view, this.zzhv));
    }

    public void bindViewToForward(View view, long j) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        view.setOnClickListener(new zzd(this, j));
        zza(view, new zzbd(view));
    }

    public void bindViewToLaunchExpandedController(View view) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        view.setOnClickListener(new zzg(this));
        zza(view, new zzas(view));
    }

    public void bindViewToLoadingIndicator(View view) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(view, new zzav(view));
    }

    public void bindViewToRewind(View view, long j) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        view.setOnClickListener(new zze(this, j));
        zza(view, new zzbd(view));
    }

    public void bindViewToSkipNext(View view, int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        view.setOnClickListener(new zzb(this));
        zza(view, new zzbe(view, i));
    }

    public void bindViewToSkipPrev(View view, int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        view.setOnClickListener(new zzc(this));
        zza(view, new zzbf(view, i));
    }

    public void bindViewToUIController(View view, UIController uIController) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(view, uIController);
    }

    public void bindViewVisibilityToMediaSession(View view, int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(view, new zzbk(view, i));
    }

    public void bindViewVisibilityToPreloadingEvent(View view, int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zza(view, new zzbj(view, i));
    }

    public void dispose() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zzbw();
        this.zzpq.clear();
        if (this.zzgu != null) {
            this.zzgu.removeSessionManagerListener(this, CastSession.class);
        }
        this.zzps = null;
    }

    public RemoteMediaClient getRemoteMediaClient() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzhp;
    }

    public boolean isActive() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzhp != null;
    }

    public void onAdBreakStatusUpdated() {
        zzbx();
        if (this.zzps != null) {
            this.zzps.onAdBreakStatusUpdated();
        }
    }

    protected void onClosedCaptionClicked(View view) {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession() && (this.zzhv instanceof FragmentActivity)) {
            TracksChooserDialogFragment newInstance = TracksChooserDialogFragment.newInstance();
            FragmentActivity fragmentActivity = (FragmentActivity) this.zzhv;
            FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
            Fragment findFragmentByTag = fragmentActivity.getSupportFragmentManager().findFragmentByTag("TRACKS_CHOOSER_DIALOG_TAG");
            if (findFragmentByTag != null) {
                beginTransaction.remove(findFragmentByTag);
            }
            remoteMediaClient.getMediaInfo();
            remoteMediaClient.getMediaStatus().getActiveTrackIds();
            newInstance.show(beginTransaction, "TRACKS_CHOOSER_DIALOG_TAG");
        }
    }

    protected void onForwardClicked(View view, long j) {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            remoteMediaClient.seek(remoteMediaClient.getApproximateStreamPosition() + j);
        }
    }

    protected void onLaunchExpandedControllerClicked(View view) {
        ComponentName componentName = new ComponentName(this.zzhv.getApplicationContext(), CastContext.getSharedInstance(this.zzhv).getCastOptions().getCastMediaOptions().getExpandedControllerActivityClassName());
        Intent intent = new Intent();
        intent.setComponent(componentName);
        this.zzhv.startActivity(intent);
    }

    public void onMetadataUpdated() {
        zzbx();
        if (this.zzps != null) {
            this.zzps.onMetadataUpdated();
        }
    }

    protected void onMuteToggleClicked(ImageView imageView) {
        Session currentCastSession = CastContext.getSharedInstance(this.zzhv.getApplicationContext()).getSessionManager().getCurrentCastSession();
        if (currentCastSession != null && currentCastSession.isConnected()) {
            try {
                currentCastSession.setMute(currentCastSession.isMute() ^ true);
            } catch (IOException e) {
                zzbd.m26e("Unable to call CastSession.setMute(boolean).", e);
            }
        }
    }

    protected void onPlayPauseToggleClicked(ImageView imageView) {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            remoteMediaClient.togglePlayback();
        }
    }

    public void onPreloadStatusUpdated() {
        zzbx();
        if (this.zzps != null) {
            this.zzps.onPreloadStatusUpdated();
        }
    }

    public void onQueueStatusUpdated() {
        zzbx();
        if (this.zzps != null) {
            this.zzps.onQueueStatusUpdated();
        }
    }

    protected void onRewindClicked(View view, long j) {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            remoteMediaClient.seek(remoteMediaClient.getApproximateStreamPosition() - j);
        }
    }

    protected void onSeekBarProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (z) {
            for (zzbi zzc : this.zzpr) {
                zzc.zzc((long) i);
            }
        }
    }

    protected void onSeekBarStartTrackingTouch(SeekBar seekBar) {
        if (this.zzpq.containsKey(seekBar)) {
            for (UIController uIController : (List) this.zzpq.get(seekBar)) {
                if (uIController instanceof zzbc) {
                    ((zzbc) uIController).zzj(false);
                }
            }
        }
        for (zzbi zzj : this.zzpr) {
            zzj.zzj(false);
        }
    }

    protected void onSeekBarStopTrackingTouch(SeekBar seekBar) {
        if (this.zzpq.containsKey(seekBar)) {
            for (UIController uIController : (List) this.zzpq.get(seekBar)) {
                if (uIController instanceof zzbc) {
                    ((zzbc) uIController).zzj(true);
                }
            }
        }
        for (zzbi zzj : this.zzpr) {
            zzj.zzj(true);
        }
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            remoteMediaClient.seek((long) seekBar.getProgress());
        }
    }

    public void onSendingRemoteMediaRequest() {
        for (List<UIController> it : this.zzpq.values()) {
            for (UIController onSendingRemoteMediaRequest : it) {
                onSendingRemoteMediaRequest.onSendingRemoteMediaRequest();
            }
        }
        if (this.zzps != null) {
            this.zzps.onSendingRemoteMediaRequest();
        }
    }

    public void onSessionEnded(CastSession castSession, int i) {
        zzbw();
    }

    public void onSessionEnding(CastSession castSession) {
    }

    public void onSessionResumeFailed(CastSession castSession, int i) {
        zzbw();
    }

    public void onSessionResumed(CastSession castSession, boolean z) {
        zza(castSession);
    }

    public void onSessionResuming(CastSession castSession, String str) {
    }

    public void onSessionStartFailed(CastSession castSession, int i) {
        zzbw();
    }

    public void onSessionStarted(CastSession castSession, String str) {
        zza(castSession);
    }

    public void onSessionStarting(CastSession castSession) {
    }

    public void onSessionSuspended(CastSession castSession, int i) {
    }

    protected void onSkipNextClicked(View view) {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            remoteMediaClient.queueNext(null);
        }
    }

    protected void onSkipPrevClicked(View view) {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null && remoteMediaClient.hasMediaSession()) {
            remoteMediaClient.queuePrev(null);
        }
    }

    public void onStatusUpdated() {
        zzbx();
        if (this.zzps != null) {
            this.zzps.onStatusUpdated();
        }
    }

    public void setPostRemoteMediaClientListener(Listener listener) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        this.zzps = listener;
    }
}
