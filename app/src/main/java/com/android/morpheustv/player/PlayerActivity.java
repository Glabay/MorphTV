package com.android.morpheustv.player;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.InputDeviceCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.android.morpheustv.KillableActivity;
import com.android.morpheustv.helpers.TorrentHelper;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.player.MyTextRenderer.Output;
import com.android.morpheustv.settings.Settings;
import com.android.morpheustv.settings.SettingsActivity;
import com.android.morpheustv.sources.Fetcher;
import com.android.morpheustv.sources.Fetcher.DownloadSubtitleListener;
import com.android.morpheustv.sources.Fetcher.FetchSubtitleListener;
import com.android.morpheustv.sources.SubtitleListAdapter;
import com.android.morpheustv.sources.SubtitleResult;
import com.github.se_bastiaan.torrentstream.StreamStatus;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.ParametersBuilder;
import com.google.android.exoplayer2.trackselection.TrackSelection.Factory;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView.VisibilityListener;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.noname.titan.R;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.Source;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.io.FilenameUtils;
import org.mozilla.universalchardet.prober.distributionanalysis.Big5DistributionAnalysis;

public class PlayerActivity extends KillableActivity implements TorrentListener {
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TORRENT = "TorrentPlayer";
    boolean controllerVisible = true;
    private int currentScaleMode = 0;
    int currentWindow = 0;
    boolean inSubtitleMenu;
    private boolean inSyncSubtitle = false;
    private BroadcastReceiver mTimeTickReceiver;
    private Handler mainHandler;
    MorpheusMediaInfo mediaInfo;
    boolean playWhenReady = true;
    boolean playbackEnded = false;
    long playbackPosition = 0;
    SimpleExoPlayer player;
    View playerInfoContainer;
    TextView playerStatus;
    View playerStatusContainer;
    ImageView playerStatusIcon;
    TextView playerVideoInfo;
    PlayerView playerView;
    RingProgressBar progressBar;
    View subtitleContainer;
    private boolean subtitleEnabled = true;
    ExpandableListView subtitleList;
    TextView subtitleView;
    private Output textOutputListener = new C05185();
    String torrentStatus = "";
    private TrackSelectionHelper trackSelectionHelper;
    private DefaultTrackSelector trackSelector;

    /* renamed from: com.android.morpheustv.player.PlayerActivity$1 */
    class C05141 extends BroadcastReceiver {
        C05141() {
        }

        public void onReceive(Context context, Intent intent) {
            PlayerActivity.this.initClock();
        }
    }

    /* renamed from: com.android.morpheustv.player.PlayerActivity$2 */
    class C05152 implements OnClickListener {
        C05152() {
        }

        public void onClick(View view) {
            PlayerActivity.this.showRetryError(null);
            PlayerActivity.this.releasePlayer();
            PlayerActivity.this.initializePlayer();
        }
    }

    /* renamed from: com.android.morpheustv.player.PlayerActivity$3 */
    class C05163 implements VisibilityListener {
        C05163() {
        }

        public void onVisibilityChange(int i) {
            LayoutParams layoutParams = (LayoutParams) PlayerActivity.this.subtitleView.getLayoutParams();
            if (i == 0) {
                if (!(PlayerActivity.this.playerView == 0 || PlayerActivity.this.player == 0)) {
                    PlayerActivity.this.player.setPlayWhenReady(false);
                }
                i = PlayerActivity.this.findViewById(R.id.exo_play);
                if (i != 0) {
                    i.requestFocus();
                }
                PlayerActivity.this.controllerVisible = true;
                layoutParams.removeRule(12);
                layoutParams.addRule(3, PlayerActivity.this.playerInfoContainer.getId());
                PlayerActivity.this.playerInfoContainer.setVisibility(0);
                PlayerActivity.this.updateMediaInfoBar();
                return;
            }
            if (!(PlayerActivity.this.playerView == 0 || PlayerActivity.this.player == 0)) {
                PlayerActivity.this.player.setPlayWhenReady(true);
            }
            PlayerActivity.this.controllerVisible = false;
            layoutParams.removeRule(3);
            layoutParams.addRule(12);
            PlayerActivity.this.playerInfoContainer.setVisibility(4);
        }
    }

    /* renamed from: com.android.morpheustv.player.PlayerActivity$4 */
    class C05174 implements EventListener {
        public void onRepeatModeChanged(int i) {
        }

        public void onShuffleModeEnabledChanged(boolean z) {
        }

        C05174() {
        }

        public void onTimelineChanged(Timeline timeline, Object obj, int i) {
            Log.d("Player", "onTimelineChanged()");
        }

        public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
            try {
                trackGroupArray = PlayerActivity.this.trackSelector.getCurrentMappedTrackInfo();
                if (trackGroupArray != null) {
                    trackSelectionArray = new ParametersBuilder();
                    trackSelectionArray.setPreferredAudioLanguage("eng");
                    PlayerActivity.this.trackSelector.setParameters(trackSelectionArray.build());
                    if (trackGroupArray.getTrackTypeRendererSupport(2) == 1) {
                        Toast.makeText(PlayerActivity.this, "Unsupported video track", 0).show();
                    }
                    if (trackGroupArray.getTrackTypeRendererSupport(1) == 1) {
                        Toast.makeText(PlayerActivity.this, "Unsupported audio track", 0).show();
                    }
                }
            } catch (TrackGroupArray trackGroupArray2) {
                trackGroupArray2.printStackTrace();
            }
        }

        public void onLoadingChanged(boolean z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onLoadingChanged() isLoading=");
            stringBuilder.append(z);
            Log.d("Player", stringBuilder.toString());
        }

        public void onPlayerStateChanged(boolean z, int i) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onPlayerStateChanged()");
                stringBuilder.append(String.valueOf(i));
                Log.d("Player", stringBuilder.toString());
                switch (i) {
                    case 1:
                        PlayerActivity.this.updateMediaInfoBar();
                        return;
                    case 2:
                        PlayerActivity.this.updateMediaInfoBar();
                        PlayerActivity.this.showRetryError(null);
                        PlayerActivity.this.setStatus((int) R.drawable.ic_buffering, "Loading");
                        return;
                    case 3:
                        PlayerActivity.this.updateMediaInfoBar();
                        PlayerActivity.this.showRetryError(null);
                        if (z) {
                            PlayerActivity.this.setStatus(0, null);
                            return;
                        } else {
                            PlayerActivity.this.setStatus((int) R.drawable.exo_controls_pause, "Paused");
                            return;
                        }
                    case 4:
                        PlayerActivity.this.updateMediaInfoBar();
                        PlayerActivity.this.showRetryError(null);
                        PlayerActivity.this.setStatus(0, null);
                        PlayerActivity.this.playbackEnded = true;
                        PlayerActivity.this.endPlayback();
                        return;
                    default:
                        PlayerActivity.this.updateMediaInfoBar();
                        PlayerActivity.this.showRetryError(null);
                        PlayerActivity.this.setStatus(0, null);
                        return;
                }
            } catch (boolean z2) {
                z2.printStackTrace();
                PlayerActivity.this.setStatus((int) R.drawable.error_icon, "ERROR");
                PlayerActivity.this.showRetryError(z2.getMessage());
            }
        }

        public void onPlayerError(ExoPlaybackException exoPlaybackException) {
            String str = "UNKNOWN ERROR: An unknown error has ocurred.";
            StringBuilder stringBuilder;
            switch (exoPlaybackException.type) {
                case 0:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("SOURCE ERROR: ");
                    stringBuilder.append(exoPlaybackException.getSourceException().getMessage());
                    str = stringBuilder.toString();
                    break;
                case 1:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("RENDERER ERROR: ");
                    stringBuilder.append(exoPlaybackException.getRendererException().getMessage());
                    str = stringBuilder.toString();
                    break;
                case 2:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("UNEXPECTED ERROR: ");
                    stringBuilder.append(exoPlaybackException.getUnexpectedException().getMessage());
                    str = stringBuilder.toString();
                    break;
                default:
                    break;
            }
            PlayerActivity.this.setStatus((int) R.drawable.error_icon, "ERROR");
            PlayerActivity.this.showRetryError(str);
        }

        public void onPositionDiscontinuity(int i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onPositionDiscontinuity()");
            stringBuilder.append(String.valueOf(i));
            Log.d("Player", stringBuilder.toString());
        }

        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.d("Player", "onPlaybackParametersChanged()");
        }

        public void onSeekProcessed() {
            Log.d("Player", "onSeekProcessed()");
        }
    }

    /* renamed from: com.android.morpheustv.player.PlayerActivity$5 */
    class C05185 implements Output {
        C05185() {
        }

        public void onCues(List<Cue> list) {
            if (list == null || list.size() <= 0 || !PlayerActivity.this.subtitleEnabled) {
                PlayerActivity.this.subtitleView.setText("");
                PlayerActivity.this.subtitleView.setVisibility(8);
                return;
            }
            try {
                String str = "";
                for (Cue cue : list) {
                    StringBuilder stringBuilder;
                    if (!str.isEmpty()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append("\n");
                        str = stringBuilder.toString();
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(cue.text);
                    str = stringBuilder.toString();
                    if (str.contains("{\\")) {
                        str = str.replaceAll("\\{[^\\}]*\\}", "");
                    }
                    PlayerActivity.this.subtitleView.setText(str);
                    PlayerActivity.this.subtitleView.setVisibility(0);
                }
            } catch (List<Cue> list2) {
                list2.printStackTrace();
                PlayerActivity.this.subtitleView.setText("");
                PlayerActivity.this.subtitleView.setVisibility(8);
            }
        }
    }

    /* renamed from: com.android.morpheustv.player.PlayerActivity$6 */
    class C05226 implements OnClickListener {

        /* renamed from: com.android.morpheustv.player.PlayerActivity$6$1 */
        class C05211 implements FetchSubtitleListener {

            /* renamed from: com.android.morpheustv.player.PlayerActivity$6$1$2 */
            class C05202 implements Runnable {
                C05202() {
                }

                public void run() {
                    PlayerActivity.this.hideSubtitleLoading();
                    PlayerActivity.this.showSubtitles();
                }
            }

            C05211() {
            }

            public void onFetch(final CopyOnWriteArrayList<SubtitleResult> copyOnWriteArrayList) {
                PlayerActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        PlayerActivity.this.mediaInfo.otherSubtitles = new ArrayList(copyOnWriteArrayList);
                    }
                });
            }

            public void onFinish() {
                PlayerActivity.this.runOnUiThread(new C05202());
            }
        }

        C05226() {
        }

        public void onClick(View view) {
            if (Fetcher.isBusySubtites == null) {
                PlayerActivity.this.showSubtitleLoading(PlayerActivity.this.getString(R.string.searching_subtitles));
                PlayerActivity.this.mediaInfo.otherSubtitles = new ArrayList();
                Fetcher.fetchSubtitles(PlayerActivity.this.mediaInfo.imdb, PlayerActivity.this.mediaInfo.title, Settings.SUBTITLES_LANGS, PlayerActivity.this.mediaInfo.season, PlayerActivity.this.mediaInfo.episode, new C05211());
            }
        }
    }

    /* renamed from: com.android.morpheustv.player.PlayerActivity$7 */
    class C05237 implements OnCheckedChangeListener {
        C05237() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            PlayerActivity.this.subtitleEnabled = z;
            PlayerActivity.this.hideSubtitlesSelectionView();
        }
    }

    public static class MorpheusMediaInfo {
        public int episode;
        public String imdb;
        public List<SubtitleResult> otherSubtitles;
        public int season;
        public Source source;
        public String subtitle;
        public String title;

        public MorpheusMediaInfo(Source source, String str, List<SubtitleResult> list, String str2, String str3, int i, int i2) {
            this.source = source;
            this.subtitle = str;
            this.otherSubtitles = list;
            this.imdb = str2;
            this.title = str3;
            this.season = i;
            this.episode = i2;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_player);
        this.mainHandler = new Handler();
        this.subtitleView = (TextView) findViewById(R.id.subtitles);
        this.playerView = (PlayerView) findViewById(R.id.player_view);
        this.playerStatusContainer = findViewById(R.id.player_status);
        this.playerInfoContainer = findViewById(R.id.player_info_container);
        this.progressBar = (RingProgressBar) findViewById(R.id.progressBar);
        this.playerStatus = (TextView) findViewById(R.id.player_status_text);
        this.playerStatusIcon = (ImageView) findViewById(R.id.player_status_icon);
        this.playerVideoInfo = (TextView) findViewById(R.id.player_status_videoinfo);
        this.subtitleList = (ExpandableListView) findViewById(R.id.subtitleList);
        this.subtitleContainer = findViewById(R.id.subtitle_container);
        MyTextRenderer.offset = 0;
        Settings.load(this);
        initSubtitleView();
        this.mediaInfo = (MorpheusMediaInfo) new Gson().fromJson(getIntent().getStringExtra("mediaInfo"), MorpheusMediaInfo.class);
        this.playbackPosition = loadPosition(this.mediaInfo.source.getTitle());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Loading Media : ");
        stringBuilder.append(this.mediaInfo.source.getUrl());
        Log.d("Player", stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("SideLoading Subtitle : ");
        stringBuilder.append(this.mediaInfo.subtitle);
        Log.d("Player", stringBuilder.toString());
        ((TextView) findViewById(R.id.player_status_title)).setText(this.mediaInfo.source.getTitle());
        ((TextView) findViewById(R.id.player_status_filename)).setText(this.mediaInfo.source.getFilename());
        ((TextView) findViewById(R.id.media_filename)).setText(this.mediaInfo.source.getFilename());
        initClock();
        showRetryError(null);
        CookieHandler.setDefault(new CookieManager());
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
        Utils.disableSSLCertificateChecking();
    }

    public void initClock() {
        ((TextView) findViewById(R.id.player_status_time)).setText(new SimpleDateFormat("HH:mm").format(new Date()));
    }

    public void onStart() {
        super.onStart();
        try {
            if (Util.SDK_INT > 23) {
                initializePlayer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus((int) R.drawable.error_icon, "ERROR");
            showRetryError(e.getMessage());
        }
    }

    public void onResume() {
        super.onResume();
        try {
            hideSystemUi();
            if (Util.SDK_INT <= 23 || this.player == null) {
                initializePlayer();
            }
            initClock();
            this.mTimeTickReceiver = new C05141();
            registerReceiver(this.mTimeTickReceiver, new IntentFilter("android.intent.action.TIME_TICK"));
            TorrentHelper.addListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus((int) R.drawable.error_icon, "ERROR");
            showRetryError(e.getMessage());
        }
    }

    public void onPause() {
        super.onPause();
        try {
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
            unregisterReceiver(this.mTimeTickReceiver);
            TorrentHelper.removeListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus((int) R.drawable.error_icon, "ERROR");
            showRetryError(e.getMessage());
        }
    }

    public void onStop() {
        super.onStop();
        try {
            if (Util.SDK_INT > 23) {
                releasePlayer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus((int) R.drawable.error_icon, "ERROR");
            showRetryError(e.getMessage());
        }
    }

    private int dp2px(float f) {
        return (int) TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }

    private void initSubtitleView() {
        try {
            if (Settings.SUBTITLES_COLOR.toLowerCase().equals("white")) {
                this.subtitleView.setTextColor(Color.parseColor("#FFFFDE"));
            }
            if (Settings.SUBTITLES_COLOR.toLowerCase().equals("yellow")) {
                this.subtitleView.setTextColor(InputDeviceCompat.SOURCE_ANY);
            }
            LayoutParams layoutParams = (LayoutParams) this.subtitleView.getLayoutParams();
            float parseInt = (float) Integer.parseInt(Settings.SUBTITLES_MARGIN);
            layoutParams.bottomMargin = dp2px(parseInt);
            layoutParams.topMargin = dp2px(parseInt);
            this.subtitleView.setLayoutParams(layoutParams);
            this.subtitleView.setTextSize(Float.parseFloat(Settings.SUBTITLES_SIZE));
            layoutParams.removeRule(12);
            layoutParams.addRule(3, this.playerInfoContainer.getId());
            this.playerInfoContainer.setVisibility(0);
            updateMediaInfoBar();
        } catch (Exception e) {
            e.printStackTrace();
            setStatus((int) R.drawable.error_icon, "ERROR");
            showRetryError(e.getMessage());
        }
    }

    private void showSubtitleError(String str) {
        showRetryError(null);
        findViewById(R.id.subtitleList).setVisibility(8);
        findViewById(R.id.fetchProgress).setVisibility(0);
        findViewById(R.id.fetchbar).setVisibility(8);
        ((TextView) findViewById(R.id.task_name)).setText(str);
    }

    private void showSubtitleLoading(String str) {
        findViewById(R.id.subtitleList).setVisibility(8);
        findViewById(R.id.fetchProgress).setVisibility(0);
        findViewById(R.id.fetchbar).setVisibility(0);
        ((TextView) findViewById(R.id.task_name)).setText(str);
    }

    private void hideSubtitleLoading() {
        findViewById(R.id.subtitleList).setVisibility(0);
        findViewById(R.id.fetchProgress).setVisibility(8);
    }

    public void showSubtitlesSelectionView() {
        hideSubtitleLoading();
        this.player.setPlayWhenReady(false);
        this.subtitleContainer.setVisibility(0);
        this.inSubtitleMenu = true;
    }

    public void hideSubtitlesSelectionView() {
        hideSubtitleLoading();
        this.player.setPlayWhenReady(true);
        this.subtitleContainer.setVisibility(8);
        this.inSubtitleMenu = false;
    }

    public void showController() {
        if (this.playerView != null && this.player != null) {
            this.playerView.showController();
        }
    }

    public void hideController() {
        if (this.playerView != null && this.player != null) {
            this.playerView.hideController();
        }
    }

    public void seekForward() {
        if (this.playerView != null && this.player != null) {
            this.player.seekTo(this.player.getCurrentPosition() + 30000);
        }
    }

    public void seekBackwards() {
        if (this.playerView != null && this.player != null) {
            this.player.seekTo(this.player.getCurrentPosition() - 30000);
        }
    }

    public void seekForwardBig() {
        if (this.playerView != null && this.player != null) {
            this.player.seekTo(this.player.getCurrentPosition() + 300000);
        }
    }

    public void seekBackwardsBig() {
        if (this.playerView != null && this.player != null) {
            this.player.seekTo(this.player.getCurrentPosition() - 300000);
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            switch (i) {
                case 19:
                    if (this.controllerVisible != 0 || this.inSubtitleMenu != 0 || this.inSyncSubtitle != 0) {
                        return false;
                    }
                    seekForwardBig();
                    return true;
                case 20:
                    if (this.controllerVisible != 0 || this.inSubtitleMenu != 0 || this.inSyncSubtitle != 0) {
                        return false;
                    }
                    seekBackwardsBig();
                    return true;
                case 21:
                    if (this.controllerVisible == 0 && this.inSubtitleMenu == 0 && this.inSyncSubtitle == 0) {
                        seekBackwards();
                        return true;
                    } else if (this.inSyncSubtitle == 0) {
                        return false;
                    } else {
                        decSubtitleOffset(null);
                        return true;
                    }
                case 22:
                    if (this.controllerVisible == 0 && this.inSubtitleMenu == 0 && this.inSyncSubtitle == 0) {
                        seekForward();
                        return true;
                    } else if (this.inSyncSubtitle == 0) {
                        return false;
                    } else {
                        incSubtitleOffset(null);
                        return true;
                    }
                case 23:
                    showController();
                    return true;
                default:
                    switch (i) {
                        case 85:
                            this.player.setPlayWhenReady(this.player.getPlayWhenReady() ^ 1);
                            return true;
                        case 86:
                            endPlayback();
                            return true;
                        default:
                            switch (i) {
                                case Big5DistributionAnalysis.LOWBYTE_END_1 /*126*/:
                                    this.player.setPlayWhenReady(true);
                                    return true;
                                case 127:
                                    this.player.setPlayWhenReady(false);
                                    return true;
                                default:
                                    switch (i) {
                                        case 272:
                                            seekForwardBig();
                                            return true;
                                        case 273:
                                            seekBackwardsBig();
                                            return true;
                                        case 274:
                                            seekForward();
                                            return true;
                                        case 275:
                                            seekBackwards();
                                            return true;
                                        default:
                                            return false;
                                    }
                            }
                    }
            }
        }
        if (this.inSubtitleMenu != 0) {
            hideSubtitlesSelectionView();
        } else if (this.inSyncSubtitle != 0) {
            hideSyncSubtitle();
        } else if (this.controllerVisible != 0) {
            hideController();
        } else {
            endPlayback();
        }
        return true;
    }

    public void hideSyncSubtitle() {
        findViewById(R.id.subtitleOffsetContainer).setVisibility(8);
        this.inSyncSubtitle = false;
    }

    public String getOffsetString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(Math.abs(MyTextRenderer.offset / 1000)));
        stringBuilder.append(" ms");
        String stringBuilder2 = stringBuilder.toString();
        if (MyTextRenderer.offset > 0) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("+ ");
            stringBuilder3.append(stringBuilder2);
            stringBuilder2 = stringBuilder3.toString();
        }
        if (MyTextRenderer.offset >= 0) {
            return stringBuilder2;
        }
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("- ");
        stringBuilder3.append(stringBuilder2);
        return stringBuilder3.toString();
    }

    public void syncSubtitle(View view) {
        hideSubtitlesSelectionView();
        showRetryError(null);
        hideController();
        findViewById(R.id.subtitleOffsetContainer).setVisibility(0);
        ((TextView) findViewById(R.id.offsetValue)).setText(getOffsetString());
        findViewById(R.id.offsetRight).requestFocus();
        this.inSyncSubtitle = true;
    }

    public void incSubtitleOffset(View view) {
        MyTextRenderer.offset += 100000;
        ((TextView) findViewById(R.id.offsetValue)).setText(getOffsetString());
        findViewById(R.id.offsetRight).requestFocus();
    }

    public void decSubtitleOffset(View view) {
        MyTextRenderer.offset -= 100000;
        ((TextView) findViewById(R.id.offsetValue)).setText(getOffsetString());
        findViewById(R.id.offsetLeft).requestFocus();
    }

    public void setSubtitles(View view) {
        showSubtitles();
    }

    public void setTrack(View view) {
        if (this.trackSelector != null && this.trackSelectionHelper != null && this.trackSelector.getCurrentMappedTrackInfo() != null) {
            this.trackSelectionHelper.showSelectionDialog(this, getString(R.string.track_dialog_title), this.trackSelector.getCurrentMappedTrackInfo(), 0);
        }
    }

    public void setSettings(View view) {
        startActivityForResult(new Intent(this, SettingsActivity.class), 5000);
    }

    public void updateMediaInfoBar() {
        CharSequence charSequence = "";
        try {
            if (this.player != null) {
                Format videoFormat = this.player.getVideoFormat();
                if (videoFormat != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(String.valueOf(videoFormat.width));
                    stringBuilder.append("x");
                    stringBuilder.append(String.valueOf(videoFormat.height));
                    String stringBuilder2 = stringBuilder.toString();
                    String str = videoFormat.sampleMimeType;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(stringBuilder2);
                    stringBuilder3.append(" (");
                    stringBuilder3.append(str);
                    stringBuilder3.append(")");
                    charSequence = stringBuilder3.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!(this.torrentStatus == null || this.torrentStatus.isEmpty())) {
                StringBuilder stringBuilder4;
                String stringBuilder5;
                if (!charSequence.isEmpty()) {
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(charSequence);
                    stringBuilder4.append("\n");
                    stringBuilder5 = stringBuilder4.toString();
                }
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(stringBuilder5);
                stringBuilder4.append(this.torrentStatus);
                charSequence = stringBuilder4.toString();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (charSequence.isEmpty()) {
            this.playerVideoInfo.setVisibility(8);
            return;
        }
        this.playerVideoInfo.setText(charSequence);
        this.playerVideoInfo.setVisibility(0);
    }

    private void showRetryError(String str) {
        if (str == null || str.isEmpty()) {
            findViewById(R.id.error_notification).setVisibility(8);
            return;
        }
        hideSubtitlesSelectionView();
        findViewById(R.id.error_notification).setVisibility(0);
        ((TextView) findViewById(R.id.error_message)).setText(str);
        findViewById(R.id.continue_button).setVisibility(0);
        findViewById(R.id.continue_button).requestFocus();
        findViewById(R.id.continue_button).setOnClickListener(new C05152());
    }

    public void changeVideoScale(View view) {
        if (this.playerView != null) {
            this.currentScaleMode++;
            if (this.currentScaleMode > 4) {
                this.currentScaleMode = 0;
            }
            this.playerView.setResizeMode(this.currentScaleMode);
            view = "";
            switch (this.currentScaleMode) {
                case 0:
                    view = "FIT";
                    break;
                case 1:
                    view = "FIXED WIDTH";
                    break;
                case 2:
                    view = "FIXED HEIGHT";
                    break;
                case 3:
                    view = "FILL SCREEN";
                    break;
                case 4:
                    view = "ZOOM";
                    break;
                default:
                    break;
            }
            Toast.makeText(this, view, 0).show();
        }
    }

    private void initializePlayer() {
        try {
            DefaultAllocator defaultAllocator = new DefaultAllocator(true, 65536);
            LoadControl defaultLoadControl = new DefaultLoadControl(defaultAllocator, Settings.MIN_BUFFER_SIZE, Settings.MAX_BUFFER_SIZE, Settings.PLAYBACK_BUFFER_SIZE, 5000, -1, true);
            if (this.mediaInfo.source.isTorrent()) {
                defaultLoadControl = new DefaultLoadControl(defaultAllocator, 1000, 1000, 1000, 1000, -1, true);
            }
            Factory factory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            this.trackSelector = new DefaultTrackSelector(factory);
            this.trackSelectionHelper = new TrackSelectionHelper(this.trackSelector, factory);
            this.player = ExoPlayerFactory.newSimpleInstance(new MyDefaultRenderersFactory(this), this.trackSelector, r10);
            this.playerView.setPlayer(this.player);
            this.playerView.setUseController(true);
            this.playerView.setControllerAutoShow(true);
            this.playerView.setControllerShowTimeoutMs(1000);
            this.playerView.getSubtitleView().setVisibility(8);
            this.playerView.setControllerVisibilityListener(new C05163());
            this.player.addListener(new C05174());
            MediaSource buildMediaSource = buildMediaSource();
            this.player.seekTo(this.playbackPosition);
            this.player.setPlayWhenReady(this.playWhenReady);
            this.player.prepare(buildMediaSource, true, false);
            this.player.seekTo(this.playbackPosition);
            updateMediaInfoBar();
        } catch (Exception e) {
            e.printStackTrace();
            setStatus((int) R.drawable.error_icon, "ERROR");
            showRetryError(e.getMessage());
        }
    }

    public void setStatus(String str, int i) {
        this.progressBar.setMax(100);
        this.progressBar.setProgress(i);
        if (i != 100) {
            setStatus(0, str);
            this.progressBar.setVisibility(0);
        }
    }

    public void setStatus(int i, String str) {
        this.progressBar.setVisibility(8);
        if (str == null || str.isEmpty()) {
            this.playerStatusIcon.setImageResource(0);
            this.playerStatus.setText("");
            this.playerStatusContainer.setVisibility(8);
            return;
        }
        this.playerStatusIcon.setImageResource(i);
        this.playerStatus.setText(str);
        this.playerStatusContainer.setVisibility(0);
    }

    private MediaSource buildMediaSource() {
        TorrentDataSourceFactory torrentDataSourceFactory;
        Uri parse = Uri.parse(this.mediaInfo.source.getUrl());
        if (!this.mediaInfo.source.isTorrent() || TorrentHelper.getCurrentTorrent() == null) {
            TorrentDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory(BaseProvider.UserAgent, BANDWIDTH_METER, 10000, 20000, true);
            String str = "";
            if (!(this.mediaInfo.source.getCookieString() == null || this.mediaInfo.source.getCookieString().isEmpty())) {
                str = this.mediaInfo.source.getCookieString();
            }
            if (this.mediaInfo.source.getCookies() != null && this.mediaInfo.source.getCookies().size() > 0) {
                for (Entry entry : this.mediaInfo.source.getCookies().entrySet()) {
                    StringBuilder stringBuilder;
                    if (!str.isEmpty()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append("; ");
                        str = stringBuilder.toString();
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append((String) entry.getKey());
                    stringBuilder.append("=");
                    stringBuilder.append((String) entry.getValue());
                    str = stringBuilder.toString();
                }
            }
            if (!str.isEmpty()) {
                defaultHttpDataSourceFactory.getDefaultRequestProperties().set(HttpHeaders.COOKIE, str);
            }
            defaultHttpDataSourceFactory.getDefaultRequestProperties().set(HttpHeaders.USER_AGENT, BaseProvider.UserAgent);
            defaultHttpDataSourceFactory.getDefaultRequestProperties().set(HttpHeaders.REFERER, this.mediaInfo.source.getReferer() != null ? this.mediaInfo.source.getReferer() : this.mediaInfo.source.getUrl());
        } else {
            torrentDataSourceFactory = new TorrentDataSourceFactory(TorrentHelper.getCurrentTorrent(), this.playbackPosition);
        }
        TorrentDataSourceFactory torrentDataSourceFactory2 = torrentDataSourceFactory;
        if (this.mediaInfo.source.getUrl().contains(".m3u8")) {
            MediaSource hlsMediaSource = new HlsMediaSource(parse, new DefaultHlsDataSourceFactory(torrentDataSourceFactory2), new DefaultHlsExtractorFactory(), 2, this.mainHandler, null, new MyHLSPlaylistParser());
        } else {
            MediaSource extractorMediaSource = new ExtractorMediaSource(parse, torrentDataSourceFactory2, new DefaultExtractorsFactory(), null, null);
        }
        if (buildMediaSourceSubtitle() == null) {
            return hlsMediaSource;
        }
        return new MergingMediaSource(hlsMediaSource, buildMediaSourceSubtitle());
    }

    private MediaSource buildMediaSourceSubtitle() {
        this.player.removeTextOutput(this.textOutputListener);
        if (this.mediaInfo.subtitle == null || this.mediaInfo.subtitle.isEmpty()) {
            this.subtitleView.setVisibility(8);
        } else if (new File(this.mediaInfo.subtitle).exists()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("file://");
            stringBuilder.append(this.mediaInfo.subtitle);
            MediaSource singleSampleMediaSource = new SingleSampleMediaSource(Uri.parse(stringBuilder.toString()), new DefaultDataSourceFactory(this, BaseProvider.UserAgent), Format.createTextSampleFormat("SUB", MimeTypes.APPLICATION_SUBRIP, -1, "en"), C0649C.TIME_UNSET);
            this.player.addTextOutput(this.textOutputListener);
            return singleSampleMediaSource;
        }
        return null;
    }

    private void releasePlayer() {
        if (this.player != null) {
            this.playbackPosition = this.player.getCurrentPosition();
            this.currentWindow = this.player.getCurrentWindowIndex();
            this.playWhenReady = this.player.getPlayWhenReady();
            this.player.removeTextOutput(this.textOutputListener);
            this.player.release();
            this.player = null;
            if (this.playbackEnded) {
                this.playbackPosition = 0;
            }
            savePosition(this.mediaInfo.source.getTitle(), this.playbackPosition);
        }
    }

    @SuppressLint({"InlinedApi"})
    private void hideSystemUi() {
        if (this.playerView != null) {
            this.playerView.setSystemUiVisibility(4871);
        }
    }

    public void showSubtitles() {
        try {
            showRetryError(null);
            this.subtitleList.setOnChildClickListener(null);
            ((TextView) findViewById(R.id.reload_button)).setOnClickListener(new C05226());
            CheckBox checkBox = (CheckBox) findViewById(R.id.btnDisableSubs);
            checkBox.setChecked(this.subtitleEnabled);
            checkBox.setOnCheckedChangeListener(new C05237());
            if (this.mediaInfo.otherSubtitles == null || this.mediaInfo.otherSubtitles.size() <= 0) {
                showSubtitleError(getString(R.string.no_subtitles));
            } else {
                try {
                    final String filename = this.mediaInfo.source.getFilename();
                    Collections.sort(this.mediaInfo.otherSubtitles, new Comparator<SubtitleResult>() {
                        public int compare(SubtitleResult subtitleResult, SubtitleResult subtitleResult2) {
                            if (Utils.stringSimilarity(filename, subtitleResult.getFilename()) < Utils.stringSimilarity(filename, subtitleResult2.getFilename())) {
                                return 1;
                            }
                            return Utils.stringSimilarity(filename, subtitleResult.getFilename()) > Utils.stringSimilarity(filename, subtitleResult2.getFilename()) ? -1 : null;
                        }
                    });
                    Collection arrayList = new ArrayList();
                    for (String str : Arrays.asList(Settings.SUBTITLES_LANGS.split(","))) {
                        for (SubtitleResult subtitleResult : this.mediaInfo.otherSubtitles) {
                            String str2 = "srt";
                            if (subtitleResult.provider.equals("OPENSUBTITLES")) {
                                str2 = FilenameUtils.getExtension(subtitleResult.getFilename()).toLowerCase();
                            }
                            if (str.equals(subtitleResult.getLanguage()) && (r5.equals("srt") || r5.equals("sub"))) {
                                arrayList.add(subtitleResult);
                            }
                        }
                    }
                    this.mediaInfo.otherSubtitles.clear();
                    this.mediaInfo.otherSubtitles.addAll(arrayList);
                    final SubtitleListAdapter subtitleListAdapter = new SubtitleListAdapter(this);
                    this.subtitleList.setAdapter(subtitleListAdapter);
                    subtitleListAdapter.setSources(this.mediaInfo.otherSubtitles);
                    this.subtitleList.setOnChildClickListener(new OnChildClickListener() {

                        /* renamed from: com.android.morpheustv.player.PlayerActivity$9$1 */
                        class C05271 implements DownloadSubtitleListener {

                            /* renamed from: com.android.morpheustv.player.PlayerActivity$9$1$2 */
                            class C05262 implements Runnable {
                                C05262() {
                                }

                                public void run() {
                                    PlayerActivity.this.showSubtitleError(PlayerActivity.this.getString(R.string.error_subtitles_download));
                                }
                            }

                            C05271() {
                            }

                            public void onDownload(final String str) {
                                PlayerActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        PlayerActivity.this.mediaInfo.subtitle = str;
                                        PlayerActivity.this.hideSubtitlesSelectionView();
                                        PlayerActivity.this.releasePlayer();
                                        PlayerActivity.this.initializePlayer();
                                    }
                                });
                            }

                            public void onFail() {
                                PlayerActivity.this.runOnUiThread(new C05262());
                            }
                        }

                        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
                            expandableListView = subtitleListAdapter.getChild(i, i2);
                            PlayerActivity.this.showSubtitleLoading(PlayerActivity.this.getString(R.string.downloading_subtitles));
                            Fetcher.downloadSubtitles(expandableListView, new C05271());
                            return true;
                        }
                    });
                    this.subtitleList.requestFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            showSubtitlesSelectionView();
        } catch (Exception e2) {
            e2.printStackTrace();
            setStatus((int) R.drawable.error_icon, "ERROR");
            showRetryError(e2.getMessage());
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 5000) {
            Settings.load(this);
            initSubtitleView();
        }
    }

    public void stopPlayer(View view) {
        endPlayback();
    }

    public void endPlayback() {
        Intent intent = new Intent();
        if (this.player != null) {
            intent.putExtra("currentPosition", this.player.getCurrentPosition());
            intent.putExtra("duration", this.player.getDuration());
        }
        setResult(-1, intent);
        finish();
    }

    public void savePosition(String str, long j) {
        Settings.saveLong(this, str, j);
    }

    public long loadPosition(String str) {
        return Settings.loadLong(this, str);
    }

    private void setTorrentStatus(String str) {
        this.torrentStatus = str;
        updateMediaInfoBar();
    }

    public void onStreamPrepared(Torrent torrent) {
        Log.d(TORRENT, "OnStreamPrepared");
        setTorrentStatus("Downloading metadata...");
    }

    public void onStreamStarted(Torrent torrent) {
        Log.d(TORRENT, "onStreamStarted");
        setTorrentStatus("Preparing...");
    }

    public void onStreamError(Torrent torrent, Exception exception) {
        Log.e(TORRENT, "onStreamError", exception);
        torrent = new StringBuilder();
        torrent.append("Error - ");
        torrent.append(exception.getMessage());
        setTorrentStatus(torrent.toString());
    }

    public void onStreamReady(Torrent torrent) {
        Log.e(TORRENT, "onStreamReady");
    }

    public void onStreamProgress(Torrent torrent, StreamStatus streamStatus) {
        float f;
        String string;
        String string2 = getString(R.string.torrent_complete);
        if (streamStatus.bufferProgress < 100) {
            f = (float) streamStatus.bufferProgress;
            string = getString(R.string.init_torrent_metadata);
        } else if (streamStatus.progress < 100.0f) {
            f = streamStatus.progress;
            string = getString(R.string.init_torrent_downloading_player);
        } else {
            string = string2;
            f = 0.0f;
        }
        if (torrent.getTorrentHandle() != null) {
            long downloadRate = (long) torrent.getTorrentHandle().status().downloadRate();
            long uploadRate = (long) torrent.getTorrentHandle().status().uploadRate();
            torrent = String.format("%s (%.2f%% completed - D:%s/s U:%s/s - %d seeds)", new Object[]{string, Float.valueOf(f), Utils.formatSize(this, downloadRate), Utils.formatSize(this, uploadRate), Integer.valueOf(streamStatus.seeds)});
            if (torrent.equals(this.torrentStatus) == null) {
                setTorrentStatus(torrent);
            }
        }
    }

    public void onStreamStopped() {
        Log.d(TORRENT, "onStreamStopped");
    }
}
