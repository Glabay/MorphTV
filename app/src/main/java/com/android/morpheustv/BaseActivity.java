package com.android.morpheustv;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;
import com.android.morpheustv.content.Downloads;
import com.android.morpheustv.helpers.Trakt;
import com.android.morpheustv.helpers.Trakt.TraktSyncListener;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.service.BackgroundService;
import com.android.morpheustv.settings.Settings;
import com.android.morpheustv.sources.Fetcher;
import com.android.morpheustv.sources.Fetcher.FetchSourcesListener;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.Callback;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.ProgressListener;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.common.net.HttpHeaders;
import com.noname.titan.R;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.Func;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.Status;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.Source;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class BaseActivity extends KillableActivity implements TraktSyncListener, SessionManagerListener<CastSession>, ProgressListener, CastStateListener, FetchListener {
    private static final int REQUEST_SPEECH = 6666;
    static boolean lastDownloadButtonVisible;
    Callback castCallback = new C03723();
    public SpeechListener currentSpeechListener;
    public String currentSpeechPrompt = "";
    public boolean isForeground = true;
    public MediaRouteButton mCastButton;
    public CastContext mCastContext;
    public CastSession mCastSession;
    public Handler mainHandler;
    public RemoteMediaClient remoteMediaClient;
    Runnable updateDownloadsRunnable = new Runnable() {

        /* renamed from: com.android.morpheustv.BaseActivity$10$1 */
        class C03681 implements OnClickListener {
            C03681() {
            }

            public void onClick(View view) {
                ContextCompat.startActivity(BaseActivity.this, new Intent(BaseActivity.this, Downloads.class), ActivityOptionsCompat.makeBasic().toBundle());
            }
        }

        public void run() {
            BaseActivity.this.mainHandler.removeCallbacks(BaseActivity.this.updateDownloadsRunnable);
            if (BaseActivity.this.isForeground) {
                final View findViewById = BaseActivity.this.findViewById(R.id.downloadBarButton);
                if (findViewById != null) {
                    final TextView textView = (TextView) findViewById.findViewById(R.id.downloadBadge);
                    findViewById.setOnClickListener(new C03681());
                    if (!(textView == null || BaseActivity.this.getFetchInstance() == null)) {
                        BaseActivity.this.getFetchInstance().getDownloads(new Func<List<? extends Download>>() {
                            public void call(List<? extends Download> list) {
                                boolean z = false;
                                int i = 0;
                                for (Download download : list) {
                                    if (BaseActivity.this.getDownloadMetadata(download.getId()) != null && download.getStatus() == Status.COMPLETED) {
                                        i++;
                                    }
                                }
                                if (i > 0) {
                                    textView.setText(String.valueOf(i));
                                    textView.setBackgroundResource(R.drawable.badge_circle);
                                    textView.setVisibility(0);
                                } else {
                                    textView.setVisibility(8);
                                }
                                if (list.size() == null) {
                                    findViewById.setVisibility(8);
                                } else {
                                    findViewById.setVisibility(0);
                                }
                                if (findViewById.getVisibility() == null) {
                                    z = true;
                                }
                                BaseActivity.lastDownloadButtonVisible = z;
                            }
                        });
                    }
                }
            }
        }
    };

    /* renamed from: com.android.morpheustv.BaseActivity$1 */
    class C03701 implements Runnable {
        C03701() {
        }

        public void run() {
            Animation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
            rotateAnimation.setDuration(2000);
            rotateAnimation.setRepeatCount(-1);
            rotateAnimation.setRepeatMode(-1);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            View findViewById = BaseActivity.this.findViewById(R.id.breadcrumb_image);
            if (findViewById == null) {
                return;
            }
            if (findViewById.getAnimation() == null || (findViewById.getAnimation() != null && findViewById.getAnimation().hasEnded())) {
                findViewById.startAnimation(rotateAnimation);
            }
        }
    }

    /* renamed from: com.android.morpheustv.BaseActivity$2 */
    class C03712 implements Runnable {
        C03712() {
        }

        public void run() {
            View findViewById = BaseActivity.this.findViewById(R.id.breadcrumb_image);
            if (findViewById != null) {
                findViewById.clearAnimation();
            }
        }
    }

    /* renamed from: com.android.morpheustv.BaseActivity$3 */
    class C03723 extends Callback {
        C03723() {
        }

        public void onStatusUpdated() {
            super.onStatusUpdated();
        }

        public void onMetadataUpdated() {
            super.onMetadataUpdated();
        }

        public void onQueueStatusUpdated() {
            super.onQueueStatusUpdated();
        }

        public void onPreloadStatusUpdated() {
            super.onPreloadStatusUpdated();
        }

        public void onSendingRemoteMediaRequest() {
            super.onSendingRemoteMediaRequest();
        }

        public void onAdBreakStatusUpdated() {
            super.onAdBreakStatusUpdated();
        }
    }

    public interface FetchBestSourceListener {
        boolean handleSource(Source source);

        void onFinish(boolean z);

        void onSourcesUpdated(CopyOnWriteArrayList<Source> copyOnWriteArrayList);
    }

    /* renamed from: com.android.morpheustv.BaseActivity$5 */
    class C03745 implements OnDismissListener {
        C03745() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            Fetcher.isBusy = null;
        }
    }

    /* renamed from: com.android.morpheustv.BaseActivity$6 */
    class C03756 implements DialogInterface.OnClickListener {
        C03756() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Fetcher.wasCancelled = true;
            dialogInterface.dismiss();
        }
    }

    /* renamed from: com.android.morpheustv.BaseActivity$9 */
    class C03849 implements Func<List<? extends Download>> {
        C03849() {
        }

        public void call(List<? extends Download> list) {
            for (Download id : list) {
                boolean isMovieWatched;
                DownloadMetadata downloadMetadata = BaseActivity.this.getDownloadMetadata(id.getId());
                if (downloadMetadata.season <= 0 || downloadMetadata.episode <= 0) {
                    isMovieWatched = Trakt.isMovieWatched(downloadMetadata.imdb);
                } else {
                    isMovieWatched = Trakt.isEpisodeWatched(downloadMetadata.imdb, downloadMetadata.season, downloadMetadata.episode);
                }
                if (isMovieWatched) {
                    Log.d("DownloadManager", "clearWatchedDownloads");
                }
            }
        }
    }

    public static class DownloadMetadata {
        public int downloadId = 0;
        public int episode;
        public int episodeId;
        public String episode_title;
        public String imdb;
        public boolean movie;
        public String overview;
        public double rating;
        public int season;
        public boolean show;
        public Source source;
        public String titleSimple;
        public String titleWithSE;
        public int tmdb;
        public int year;
    }

    public interface OnCheckDownloadListener {
        void OnCheckFinished(Download download);
    }

    public interface SpeechListener {
        void onSpeechRecognized(String str);
    }

    public void onProgressUpdated(long j, long j2) {
    }

    public void onSessionEnding(CastSession castSession) {
    }

    public void onSessionResuming(CastSession castSession, String str) {
    }

    public void onSessionStarting(CastSession castSession) {
    }

    public void onSessionSuspended(CastSession castSession, int i) {
    }

    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mainHandler = new Handler(Looper.getMainLooper());
        Settings.load(this);
        startBackgroundService();
    }

    protected void onResume() {
        super.onResume();
        this.isForeground = true;
        Trakt.addSyncListener(this);
        try {
            this.mCastContext = CastContext.getSharedInstance(this);
            this.mCastSession = this.mCastContext.getSessionManager().getCurrentCastSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.mCastContext != null) {
            this.mCastContext.addCastStateListener(this);
            this.mCastContext.getSessionManager().addSessionManagerListener(this, CastSession.class);
        }
        getMediaClient();
        if (getFetchInstance() != null) {
            getFetchInstance().addListener(this);
        }
        updateDownloadCount();
    }

    protected void onPause() {
        super.onPause();
        this.isForeground = false;
        Trakt.removeListener(this);
        stopTraktRotating();
        if (this.mCastContext != null) {
            this.mCastContext.removeCastStateListener(this);
            this.mCastContext.getSessionManager().removeSessionManagerListener(this, CastSession.class);
        }
        if (this.remoteMediaClient != null) {
            this.remoteMediaClient.unregisterCallback(this.castCallback);
            this.remoteMediaClient.removeProgressListener(this);
        }
        if (getFetchInstance() != null) {
            getFetchInstance().removeListener(this);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void setContentView(int i) {
        super.setContentView(i);
        try {
            inflateCastMiniController();
            this.mCastButton = (MediaRouteButton) findViewById(R.id.media_route_button);
            CastButtonFactory.setUpMediaRouteButton(this, this.mCastButton);
        } catch (int i2) {
            i2.printStackTrace();
        }
    }

    public void rotateTraktLoading() {
        runOnUiThread(new C03701());
    }

    public void stopTraktRotating() {
        runOnUiThread(new C03712());
    }

    public void onTraktSyncStart() {
        rotateTraktLoading();
    }

    public void onTraktSyncProgress(String str) {
        rotateTraktLoading();
    }

    public void onTraktSyncComplete(boolean z) {
        stopTraktRotating();
    }

    public void onCastStateChanged(int i) {
        if (i != 1) {
            Log.d("Casting", "Casting devices available...");
        }
    }

    public void onSessionEnded(CastSession castSession, int i) {
        onApplicationDisconnected();
    }

    public void onSessionResumed(CastSession castSession, boolean z) {
        onApplicationConnected(castSession);
    }

    public void onSessionResumeFailed(CastSession castSession, int i) {
        onApplicationDisconnected();
    }

    public void onSessionStarted(CastSession castSession, String str) {
        onApplicationConnected(castSession);
    }

    public void onSessionStartFailed(CastSession castSession, int i) {
        onApplicationDisconnected();
    }

    private void onApplicationConnected(CastSession castSession) {
        this.mCastSession = castSession;
    }

    private void onApplicationDisconnected() {
        this.mCastSession = null;
    }

    public boolean isPlayEnabled() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == 0;
    }

    public void inflateCastMiniController() {
        try {
            if (isPlayEnabled()) {
                ViewStub viewStub = (ViewStub) findViewById(R.id.cast_minicontroller);
                if (viewStub != null) {
                    viewStub.inflate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCastMediaKey(Context context, String str, int i, int i2, int i3, String str2) {
        Settings.saveString(context, "CastMediaKey", str);
        Settings.saveInt(context, "CastMediaKeySeason", i);
        Settings.saveInt(context, "CastMediaKeyEpisode", i2);
        Settings.saveInt(context, "CastMediaKeyTraktId", i3);
        Settings.saveString(context, "CastMediaKeyImdb", str2);
    }

    public static boolean saveCastPosition(Context context, long j) {
        String loadString = Settings.loadString(context, "CastMediaKey");
        if (loadString == null) {
            return null;
        }
        Settings.saveLong(context, loadString, j);
        return true;
    }

    public RemoteMediaClient getMediaClient() {
        try {
            if (this.mCastSession != null && (this.mCastSession.isConnected() || this.mCastSession.isConnecting())) {
                if (this.remoteMediaClient != null) {
                    this.remoteMediaClient.unregisterCallback(this.castCallback);
                    this.remoteMediaClient.removeProgressListener(this);
                }
                this.remoteMediaClient = this.mCastSession.getRemoteMediaClient();
                if (this.remoteMediaClient != null) {
                    this.remoteMediaClient.addProgressListener(this, 1000);
                    this.remoteMediaClient.registerCallback(this.castCallback);
                }
                return this.remoteMediaClient;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void downloadBestSource(String str, String str2, String str3, String str4, String str5, double d, int i, String str6, int i2, int i3, int i4, int i5) {
        final int i6 = i4;
        final int i7 = i5;
        final String str7 = str2;
        final int i8 = i3;
        final String str8 = str6;
        final String str9 = str5;
        final String str10 = str3;
        final String str11 = str4;
        final int i9 = i2;
        final int i10 = i;
        final double d2 = d;
        findBestSourceDialog(str, str4, i, str6, i4, i5, new FetchBestSourceListener() {
            public void onFinish(boolean z) {
            }

            public void onSourcesUpdated(CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
            }

            public boolean handleSource(Source source) {
                if (!Fetcher.isBusy || source.isTorrent() || source.getSource().toLowerCase().contains("hls") || source.getUrl().toLowerCase().contains(".m3u8")) {
                    return false;
                }
                DownloadMetadata downloadMetadata = new DownloadMetadata();
                downloadMetadata.source = source;
                downloadMetadata.season = i6;
                downloadMetadata.episode = i7;
                downloadMetadata.episode_title = str7;
                downloadMetadata.episodeId = i8;
                downloadMetadata.imdb = str8;
                if (i6 <= 0 || i7 <= 0) {
                    downloadMetadata.movie = true;
                    downloadMetadata.show = false;
                } else {
                    downloadMetadata.movie = false;
                    downloadMetadata.show = true;
                }
                downloadMetadata.overview = str9;
                downloadMetadata.titleSimple = str10;
                downloadMetadata.titleWithSE = str11;
                downloadMetadata.tmdb = i9;
                downloadMetadata.year = i10;
                downloadMetadata.rating = d2;
                BaseActivity.this.downloadSource(source, downloadMetadata, true);
                return true;
            }
        });
    }

    public void findBestSourceDialog(String str, String str2, int i, String str3, int i2, int i3, FetchBestSourceListener fetchBestSourceListener) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnDismissListener(new C03745());
        progressDialog.setTitle(str2);
        progressDialog.setMessage(getString(R.string.autofetch_dialog_message));
        progressDialog.setButton(-2, getString(R.string.cancel), new C03756());
        progressDialog.show();
        Fetcher.isBusy = true;
        Fetcher.bestMatchFound = false;
        Fetcher.wasCancelled = false;
        Fetcher.shouldSelectOne = false;
        final FetchBestSourceListener fetchBestSourceListener2 = fetchBestSourceListener;
        Fetcher.fetchSources(this, str, String.valueOf(i), String.valueOf(str3), i2, i3, DefaultLoadControl.DEFAULT_MAX_BUFFER_MS, new FetchSourcesListener() {

            /* renamed from: com.android.morpheustv.BaseActivity$7$2 */
            class C03782 implements Runnable {
                C03782() {
                }

                public void run() {
                    Toast.makeText(BaseActivity.this, BaseActivity.this.getString(R.string.autofetch_dialog_message_error), 0).show();
                }
            }

            public void onFetch(final CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
                if (Fetcher.isBusy && !Fetcher.wasCancelled) {
                    BaseActivity.this.runOnUiThread(new Runnable() {

                        /* renamed from: com.android.morpheustv.BaseActivity$7$1$1 */
                        class C03761 implements Comparator<Source> {
                            C03761() {
                            }

                            public int compare(Source source, Source source2) {
                                return -Long.compare(source.getSize(), source2.getSize());
                            }
                        }

                        public void run() {
                            if (copyOnWriteArrayList != null && copyOnWriteArrayList.size() > 0) {
                                Object arrayList = new ArrayList();
                                arrayList.addAll(copyOnWriteArrayList);
                                Collections.sort(arrayList, new C03761());
                                Iterator it = arrayList.iterator();
                                while (it.hasNext()) {
                                    Source source = (Source) it.next();
                                    if (Fetcher.currentTitle.contains(source.getTitle()) && (((Fetcher.shouldSelectOne && !Fetcher.bestMatchFound) || source.getQuality().equals(BaseProvider.FULLHD) || source.getQuality().equals(BaseProvider.HD)) && fetchBestSourceListener2.handleSource(source))) {
                                        Fetcher.isBusy = false;
                                        Fetcher.bestMatchFound = true;
                                        return;
                                    }
                                }
                            }
                        }
                    });
                }
            }

            public void onProgress(long j) {
                if (j - System.currentTimeMillis() < 5000 && Fetcher.bestMatchFound == null) {
                    Fetcher.shouldSelectOne = 1;
                }
            }

            public void onFinish() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (!Fetcher.bestMatchFound) {
                    BaseActivity.this.runOnUiThread(new C03782());
                }
                fetchBestSourceListener2.onFinish(Fetcher.wasCancelled);
            }
        });
    }

    public boolean matchesAutoPlay(Source source, int i, int i2) {
        Object obj;
        boolean z;
        Set set;
        Set set2;
        if (Settings.VALIDATE_SOURCES) {
            if (!Utils.getColorForMs(source.getResolve_time()).equals("green")) {
                obj = null;
                z = Settings.AUTOSELECT_SOURCES_MOVIES_FAST;
                if (i > 0 && i2 > 0) {
                    z = Settings.AUTOSELECT_SOURCES_SHOWS_FAST;
                }
                if (z) {
                    if (obj != null) {
                        obj = null;
                        set = Settings.AUTOSELECT_MOVIES_QUALITY;
                        if (i > 0 && i2 > 0) {
                            set = Settings.AUTOSELECT_SHOWS_QUALITY;
                        }
                        set2 = Settings.AUTOSELECT_MOVIES_HOST;
                        if (i > 0 && i2 > 0) {
                            set2 = Settings.AUTOSELECT_SHOWS_HOST;
                        }
                        if (r3.isEmpty() == 0) {
                            for (String equals : r3) {
                                if (source.getQuality().equals(equals) != 0) {
                                }
                            }
                            i = 0;
                            if (r4.isEmpty() == 0) {
                                for (String startsWith : r4) {
                                    if (source.getSource().startsWith(startsWith)) {
                                    }
                                }
                                source = null;
                                if (source != null || r7 == 0 || r0 == null) {
                                    return false;
                                }
                                return true;
                            }
                            source = true;
                            return source != null ? false : false;
                        }
                        i = 1;
                        if (r4.isEmpty() == 0) {
                            while (i2.hasNext()) {
                                if (source.getSource().startsWith(startsWith)) {
                                }
                            }
                            source = null;
                            if (source != null) {
                            }
                        }
                        source = true;
                        if (source != null) {
                        }
                    }
                }
                obj = 1;
                set = Settings.AUTOSELECT_MOVIES_QUALITY;
                set = Settings.AUTOSELECT_SHOWS_QUALITY;
                set2 = Settings.AUTOSELECT_MOVIES_HOST;
                set2 = Settings.AUTOSELECT_SHOWS_HOST;
                if (r3.isEmpty() == 0) {
                    while (i.hasNext() != 0) {
                        if (source.getQuality().equals(equals) != 0) {
                        }
                    }
                    i = 0;
                    if (r4.isEmpty() == 0) {
                        while (i2.hasNext()) {
                            if (source.getSource().startsWith(startsWith)) {
                            }
                        }
                        source = null;
                        if (source != null) {
                        }
                    }
                    source = true;
                    if (source != null) {
                    }
                }
                i = 1;
                if (r4.isEmpty() == 0) {
                    while (i2.hasNext()) {
                        if (source.getSource().startsWith(startsWith)) {
                        }
                    }
                    source = null;
                    if (source != null) {
                    }
                }
                source = true;
                if (source != null) {
                }
            }
        }
        obj = 1;
        z = Settings.AUTOSELECT_SOURCES_MOVIES_FAST;
        z = Settings.AUTOSELECT_SOURCES_SHOWS_FAST;
        if (z) {
            if (obj != null) {
                obj = null;
                set = Settings.AUTOSELECT_MOVIES_QUALITY;
                set = Settings.AUTOSELECT_SHOWS_QUALITY;
                set2 = Settings.AUTOSELECT_MOVIES_HOST;
                set2 = Settings.AUTOSELECT_SHOWS_HOST;
                if (r3.isEmpty() == 0) {
                    while (i.hasNext() != 0) {
                        if (source.getQuality().equals(equals) != 0) {
                        }
                    }
                    i = 0;
                    if (r4.isEmpty() == 0) {
                        while (i2.hasNext()) {
                            if (source.getSource().startsWith(startsWith)) {
                            }
                        }
                        source = null;
                        if (source != null) {
                        }
                    }
                    source = true;
                    if (source != null) {
                    }
                }
                i = 1;
                if (r4.isEmpty() == 0) {
                    while (i2.hasNext()) {
                        if (source.getSource().startsWith(startsWith)) {
                        }
                    }
                    source = null;
                    if (source != null) {
                    }
                }
                source = true;
                if (source != null) {
                }
            }
        }
        obj = 1;
        set = Settings.AUTOSELECT_MOVIES_QUALITY;
        set = Settings.AUTOSELECT_SHOWS_QUALITY;
        set2 = Settings.AUTOSELECT_MOVIES_HOST;
        set2 = Settings.AUTOSELECT_SHOWS_HOST;
        if (r3.isEmpty() == 0) {
            while (i.hasNext() != 0) {
                if (source.getQuality().equals(equals) != 0) {
                }
            }
            i = 0;
            if (r4.isEmpty() == 0) {
                while (i2.hasNext()) {
                    if (source.getSource().startsWith(startsWith)) {
                    }
                }
                source = null;
                if (source != null) {
                }
            }
            source = true;
            if (source != null) {
            }
        }
        i = 1;
        if (r4.isEmpty() == 0) {
            while (i2.hasNext()) {
                if (source.getSource().startsWith(startsWith)) {
                }
            }
            source = null;
            if (source != null) {
            }
        }
        source = true;
        if (source != null) {
        }
    }

    public void findBestSourceAutoPlay(String str, String str2, int i, String str3, final int i2, final int i3, final FetchBestSourceListener fetchBestSourceListener) {
        Fetcher.isBusy = true;
        Fetcher.bestMatchFound = false;
        Fetcher.wasCancelled = false;
        Fetcher.shouldSelectOne = false;
        Fetcher.fetchSources(this, str, String.valueOf(i), str3, i2, i3, 0, new FetchSourcesListener() {

            /* renamed from: com.android.morpheustv.BaseActivity$8$2 */
            class C03822 implements Runnable {
                C03822() {
                }

                public void run() {
                    fetchBestSourceListener.onFinish(false);
                }
            }

            public void onFetch(final CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
                if (Fetcher.isBusy && !Fetcher.wasCancelled) {
                    BaseActivity.this.runOnUiThread(new Runnable() {

                        /* renamed from: com.android.morpheustv.BaseActivity$8$1$1 */
                        class C03801 implements Comparator<Source> {
                            C03801() {
                            }

                            public int compare(Source source, Source source2) {
                                return -Long.compare(source.getSize(), source2.getSize());
                            }
                        }

                        public void run() {
                            if (copyOnWriteArrayList != null && copyOnWriteArrayList.size() > 0) {
                                fetchBestSourceListener.onSourcesUpdated(copyOnWriteArrayList);
                                Object arrayList = new ArrayList();
                                arrayList.addAll(copyOnWriteArrayList);
                                Collections.sort(arrayList, new C03801());
                                Iterator it = arrayList.iterator();
                                while (it.hasNext()) {
                                    Source source = (Source) it.next();
                                    if (Fetcher.currentTitle.contains(source.getTitle()) && BaseActivity.this.matchesAutoPlay(source, i2, i3) && fetchBestSourceListener.handleSource(source)) {
                                        Fetcher.isBusy = false;
                                        Fetcher.bestMatchFound = true;
                                        return;
                                    }
                                }
                            }
                        }
                    });
                }
            }

            public void onProgress(long j) {
                if (j - System.currentTimeMillis() < 5000 && Fetcher.bestMatchFound == null) {
                    Fetcher.shouldSelectOne = 1;
                }
            }

            public void onFinish() {
                BaseActivity.this.runOnUiThread(new C03822());
            }
        });
    }

    public void clearWatchedDownloads() {
        if (getFetchInstance() != null) {
            getFetchInstance().getDownloads(new C03849());
        }
    }

    private void updateDownloadCount() {
        View findViewById = findViewById(R.id.downloadBarButton);
        if (findViewById != null) {
            if (lastDownloadButtonVisible) {
                findViewById.setVisibility(0);
            } else {
                findViewById.setVisibility(8);
            }
        }
        this.mainHandler.postDelayed(this.updateDownloadsRunnable, 1000);
    }

    public void onQueued(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download enqueued: ");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        updateDownloadCount();
    }

    public void onCompleted(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download completed: ");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        updateDownloadCount();
    }

    public void onError(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download error:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        updateDownloadCount();
    }

    public void onProgress(Download download, long j, long j2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download progress:");
        stringBuilder.append(String.valueOf(j));
        stringBuilder.append(" - ");
        stringBuilder.append(String.valueOf(j2));
        Log.d("Downloads", stringBuilder.toString());
        updateDownloadCount();
    }

    public void onPaused(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download paused:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        updateDownloadCount();
    }

    public void onResumed(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download resumed:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        updateDownloadCount();
    }

    public void onCancelled(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download cancelled:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        updateDownloadCount();
    }

    public void onRemoved(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download removed:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        updateDownloadCount();
    }

    public void onDeleted(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download deleted:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        updateDownloadCount();
    }

    public void downloadSource(Source source, final DownloadMetadata downloadMetadata, final boolean z) {
        String md5 = Utils.md5(source.getUrl());
        String url = source.getUrl();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Settings.DOWNLOADS_DOWNLOAD_FOLDER);
        stringBuilder.append("/");
        stringBuilder.append(md5);
        stringBuilder.append(".download");
        Request request = new Request(url, stringBuilder.toString());
        request.setPriority(Priority.NORMAL);
        if (Settings.DOWNLOADS_CELLULAR) {
            request.setNetworkType(NetworkType.ALL);
        } else {
            request.setNetworkType(NetworkType.WIFI_ONLY);
        }
        md5 = "";
        if (!(source.getCookieString() == null || source.getCookieString().isEmpty())) {
            md5 = source.getCookieString();
        }
        if (source.getCookies() != null && source.getCookies().size() > 0) {
            for (Entry entry : source.getCookies().entrySet()) {
                StringBuilder stringBuilder2;
                if (!md5.isEmpty()) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(md5);
                    stringBuilder2.append("; ");
                    md5 = stringBuilder2.toString();
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(md5);
                stringBuilder2.append((String) entry.getKey());
                stringBuilder2.append("=");
                stringBuilder2.append((String) entry.getValue());
                md5 = stringBuilder2.toString();
            }
        }
        if (!md5.isEmpty()) {
            request.addHeader(HttpHeaders.COOKIE, md5);
        }
        request.addHeader(HttpHeaders.USER_AGENT, BaseProvider.UserAgent);
        request.addHeader(HttpHeaders.REFERER, source.getReferer() != null ? source.getReferer() : source.getUrl());
        if (getFetchInstance() != null) {
            getFetchInstance().enqueue(request, new Func<Download>() {
                public void call(Download download) {
                    Toast.makeText(BaseActivity.this, BaseActivity.this.getString(R.string.success_add_download), 0).show();
                    BaseActivity.this.setDownloadMetadata(download.getId(), downloadMetadata);
                    if (z != null) {
                        ContextCompat.startActivity(BaseActivity.this, new Intent(BaseActivity.this, Downloads.class), ActivityOptionsCompat.makeBasic().toBundle());
                    }
                }
            }, new Func<Error>() {
                public void call(Error error) {
                    Toast.makeText(BaseActivity.this, BaseActivity.this.getString(R.string.error_add_download), 0).show();
                    Log.d("Downloads", "Download enqueue error");
                }
            });
        }
    }

    public void checkDownloadReadyForPlayback(String str, int i, int i2, OnCheckDownloadListener onCheckDownloadListener) {
        if (getFetchInstance() != null) {
            final String str2 = str;
            final int i3 = i;
            final int i4 = i2;
            final OnCheckDownloadListener onCheckDownloadListener2 = onCheckDownloadListener;
            getFetchInstance().getDownloads(new Func<List<? extends Download>>() {
                public void call(List<? extends Download> list) {
                    for (Download download : list) {
                        DownloadMetadata downloadMetadata = BaseActivity.this.getDownloadMetadata(download.getId());
                        if ((download.getStatus() == Status.DOWNLOADING || download.getStatus() == Status.COMPLETED) && downloadMetadata.imdb.equals(str2) && downloadMetadata.season == i3 && downloadMetadata.episode == i4) {
                            onCheckDownloadListener2.OnCheckFinished(download);
                            return;
                        }
                    }
                    onCheckDownloadListener2.OnCheckFinished(null);
                }
            });
            return;
        }
        onCheckDownloadListener.OnCheckFinished(null);
    }

    public DownloadMetadata getDownloadMetadata(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dwmeta_");
        stringBuilder.append(String.valueOf(i));
        return (DownloadMetadata) Settings.loadObject(this, stringBuilder.toString(), DownloadMetadata.class);
    }

    public void setDownloadMetadata(int i, DownloadMetadata downloadMetadata) {
        if (downloadMetadata != null) {
            downloadMetadata.downloadId = i;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dwmeta_");
        stringBuilder.append(String.valueOf(i));
        Settings.saveObject(this, stringBuilder.toString(), downloadMetadata);
    }

    @SuppressLint({"NewApi"})
    public void startBackgroundService() {
        Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
        intent.setAction(BackgroundService.START_SERVICE_ACTION);
        if (VERSION.SDK_INT < 26 || !Settings.NOTIFICATION_MAIN) {
            startService(intent);
        } else {
            startForegroundService(intent);
        }
    }

    @SuppressLint({"NewApi"})
    public void stopBackgroundService() {
        Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
        intent.setAction(BackgroundService.STOP_SERVICE_ACTION);
        if (VERSION.SDK_INT < 26 || !Settings.NOTIFICATION_MAIN) {
            startService(intent);
        } else {
            startForegroundService(intent);
        }
    }

    public Fetch getFetchInstance() {
        return BackgroundService.mainFetch;
    }

    public boolean isPortrait() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = 1;
        r1 = r2.getResources();	 Catch:{ Exception -> 0x0010 }
        r1 = r1.getConfiguration();	 Catch:{ Exception -> 0x0010 }
        r1 = r1.orientation;	 Catch:{ Exception -> 0x0010 }
        if (r1 != r0) goto L_0x000e;
    L_0x000d:
        return r0;
    L_0x000e:
        r0 = 0;
        return r0;
    L_0x0010:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.BaseActivity.isPortrait():boolean");
    }

    public void startSpeechRecognition() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.PROMPT", this.currentSpeechPrompt);
        intent.putExtra("android.speech.extra.MAX_RESULTS", 1);
        startActivityForResult(intent, REQUEST_SPEECH);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == REQUEST_SPEECH && i2 == -1) {
            i = intent.getStringArrayListExtra("android.speech.extra.RESULTS");
            if (this.currentSpeechListener != 0) {
                this.currentSpeechListener.onSpeechRecognized((String) i.get(null));
            }
        }
    }
}
