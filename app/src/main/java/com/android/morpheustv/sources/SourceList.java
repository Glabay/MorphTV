package com.android.morpheustv.sources;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.InputDeviceCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.morpheustv.BaseActivity;
import com.android.morpheustv.BaseActivity.DownloadMetadata;
import com.android.morpheustv.BaseActivity.FetchBestSourceListener;
import com.android.morpheustv.casting.ExpandedControlsActivity;
import com.android.morpheustv.helpers.TaskManager;
import com.android.morpheustv.helpers.Tmdb;
import com.android.morpheustv.helpers.Tmdb.ImageResult;
import com.android.morpheustv.helpers.TorrentHelper;
import com.android.morpheustv.helpers.Trakt;
import com.android.morpheustv.helpers.Trakt.OnActionListener;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.player.PlayerActivity;
import com.android.morpheustv.player.PlayerActivity.MorpheusMediaInfo;
import com.android.morpheustv.service.HttpServer;
import com.android.morpheustv.settings.Settings;
import com.android.morpheustv.sources.Fetcher.DownloadSubtitleListener;
import com.android.morpheustv.sources.Fetcher.FetchSourcesListener;
import com.android.morpheustv.sources.Fetcher.FetchSubtitleListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.se_bastiaan.torrentstream.StreamStatus;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadOptions;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.gson.Gson;
import com.noname.titan.R;
import com.uwetrottmann.trakt5.entities.EpisodeIds;
import com.uwetrottmann.trakt5.entities.MovieIds;
import de.timroes.base64.Base64;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.Source;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.io.FilenameUtils;

public class SourceList extends BaseActivity implements TorrentListener {
    private static final String TORRENT = "TORRENT";
    public static boolean isPlaying;
    private String backdrop;
    String currentLocalSubtitle;
    private Source currentSource;
    private State currentState = State.SOURCES;
    SubtitleResult currentSubtitle;
    private String downloadFilename = "";
    private String downloadLocation = "";
    private int episode;
    private int episodeId;
    private String episode_title;
    private String imdb;
    private boolean isAutoPlay = false;
    private boolean isFromDownloads = false;
    private boolean isFromService = false;
    private ExpandableListView list;
    private ImageView mBackdrop;
    private TextView mEpisodeTitle;
    private ImageView mPoster;
    ImageResult mediaImages = null;
    private boolean movie;
    private String overview;
    private String posterUrl;
    private Double rating;
    private int season;
    private boolean show;
    private SourceListAdapter sourceAdapter;
    private ArrayList<Source> sources;
    private ArrayList<SubtitleResult> subtitles;
    private String titleSimple;
    private String titleWithSE;
    private int tmdb;
    String torrentStatus = "";
    private int year;

    /* renamed from: com.android.morpheustv.sources.SourceList$3 */
    class C05553 implements FetchBestSourceListener {
        public void onSourcesUpdated(CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        }

        C05553() {
        }

        public boolean handleSource(Source source) {
            if (Fetcher.isBusy) {
                Object obj = (SourceList.this.mCastSession == null || !(SourceList.this.mCastSession.isConnected() || SourceList.this.mCastSession.isConnecting())) ? null : 1;
                if (obj == null || source.isCastable()) {
                    SourceList.this.currentSource = source;
                    SourceList.this.loadSubtitles(SourceList.this.imdb, SourceList.this.season, SourceList.this.episode);
                    return true;
                }
            }
            return false;
        }

        public void onFinish(boolean z) {
            if (!Fetcher.bestMatchFound) {
                Toast.makeText(SourceList.this, SourceList.this.getString(R.string.autoplay_dialog_message_error), 0).show();
            }
            SourceList.this.sources = new ArrayList();
            SourceList.this.sources.addAll(Fetcher.currentSources);
            if (SourceList.this.currentState == State.SOURCES) {
                SourceList.this.refreshSourceList(false);
            }
        }
    }

    /* renamed from: com.android.morpheustv.sources.SourceList$4 */
    class C05594 implements FetchSourcesListener {

        /* renamed from: com.android.morpheustv.sources.SourceList$4$3 */
        class C05583 implements Runnable {
            C05583() {
            }

            public void run() {
                if (SourceList.this.currentState == State.SOURCES) {
                    SourceList.this.hideUpperBar();
                    SourceList.this.refreshSourceList(false);
                }
            }
        }

        C05594() {
        }

        public void onFetch(final CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
            SourceList.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (SourceList.this.currentState == State.SOURCES && copyOnWriteArrayList != null && copyOnWriteArrayList.size() > 0) {
                        if (SourceList.this.sources == null) {
                            SourceList.this.sources = new ArrayList();
                        }
                        boolean z = SourceList.this.sources.size() == 0;
                        Collection arrayList = new ArrayList();
                        Iterator it = copyOnWriteArrayList.iterator();
                        while (it.hasNext()) {
                            Object obj;
                            Source source = (Source) it.next();
                            Iterator it2 = SourceList.this.sources.iterator();
                            while (it2.hasNext()) {
                                Source source2 = (Source) it2.next();
                                if (source.getUrl().equals(source2.getUrl()) && source.getProvider().equals(source2.getProvider())) {
                                    obj = 1;
                                    break;
                                }
                            }
                            obj = null;
                            if (obj == null) {
                                arrayList.add(source);
                            }
                        }
                        if (arrayList.size() > 0) {
                            SourceList.this.sources.addAll(arrayList);
                        }
                        SourceList.this.refreshSourceList(z);
                    }
                }
            });
        }

        public void onProgress(final long j) {
            SourceList.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (SourceList.this.currentState != State.SOURCES) {
                        return;
                    }
                    if (System.currentTimeMillis() < j) {
                        RingProgressBar ringProgressBar = (RingProgressBar) SourceList.this.findViewById(R.id.upper_loading_progress);
                        ringProgressBar.setMax(Settings.SCRAPING_TIMEOUT / 1000);
                        ringProgressBar.setProgress(ringProgressBar.getMax() - ((int) ((j - System.currentTimeMillis()) / 1000)));
                        return;
                    }
                    SourceList.this.hideUpperBar();
                }
            });
        }

        public void onFinish() {
            SourceList.this.runOnUiThread(new C05583());
        }
    }

    /* renamed from: com.android.morpheustv.sources.SourceList$5 */
    class C05605 implements OnItemLongClickListener {
        C05605() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (ExpandableListView.getPackedPositionType(j) != 1) {
                return null;
            }
            SourceList.this.showContextMenu(SourceList.this.sourceAdapter.getChild(ExpandableListView.getPackedPositionGroup(j), ExpandableListView.getPackedPositionChild(j)));
            return true;
        }
    }

    /* renamed from: com.android.morpheustv.sources.SourceList$6 */
    class C05616 implements OnChildClickListener {
        C05616() {
        }

        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
            if (SourceList.this.sources != null && SourceList.this.sources.size() > null) {
                SourceList.this.currentSource = SourceList.this.sourceAdapter.getChild(i, i2);
                SourceList.this.sourceAdapter.setCurrentSelection(i, i2);
                if (SourceList.this.isFromService == null) {
                    SourceList.this.loadSubtitles(SourceList.this.imdb, SourceList.this.season, SourceList.this.episode);
                }
            }
            return true;
        }
    }

    /* renamed from: com.android.morpheustv.sources.SourceList$7 */
    class C05627 implements Comparator<Source> {
        C05627() {
        }

        public int compare(Source source, Source source2) {
            return source.getProvider().compareTo(source2.getProvider());
        }
    }

    /* renamed from: com.android.morpheustv.sources.SourceList$8 */
    class C05638 implements Comparator<Source> {
        C05638() {
        }

        public int compare(Source source, Source source2) {
            return source.getProvider().compareTo(source2.getProvider());
        }
    }

    /* renamed from: com.android.morpheustv.sources.SourceList$9 */
    class C05649 implements Comparator<Source> {
        C05649() {
        }

        public int compare(Source source, Source source2) {
            return source.getProvider().compareTo(source2.getProvider());
        }
    }

    class LoadPosterTask extends AsyncTask<Void, Void, ImageResult> {
        LoadPosterTask() {
        }

        protected ImageResult doInBackground(Void... voidArr) {
            if (SourceList.this.season <= null || SourceList.this.episode <= null) {
                ImageResult movieImages = Tmdb.getMovieImages(Integer.valueOf(SourceList.this.tmdb), "en,null");
                SourceList.this.mediaImages = movieImages;
                return movieImages;
            }
            voidArr = Tmdb.getShowImages(Integer.valueOf(SourceList.this.tmdb), "en,null");
            movieImages = Tmdb.getEpisodeImages(Integer.valueOf(SourceList.this.tmdb), SourceList.this.season, SourceList.this.episode, "en,null");
            movieImages.backdropUrl = voidArr.backdropUrl;
            SourceList.this.mediaImages = voidArr;
            return movieImages;
        }

        protected void onPostExecute(ImageResult imageResult) {
            if (imageResult != null) {
                SourceList.this.backdrop = imageResult.backdropUrl;
                SourceList.this.posterUrl = imageResult.posterUrl;
            }
            SourceList.this.setMediaImages(false);
        }
    }

    public enum State {
        SOURCES,
        SUBTITLES,
        PREPARE_TORRENT,
        PLAYING
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_source_list);
        Settings.load(this);
        this.mBackdrop = (ImageView) findViewById(R.id.backdropImage);
        this.mEpisodeTitle = (TextView) findViewById(R.id.episode_name);
        this.titleSimple = getIntent().getStringExtra("title");
        this.titleWithSE = this.titleSimple;
        this.episode_title = getIntent().getStringExtra("episode_title");
        this.year = getIntent().getIntExtra("year", 0);
        this.imdb = getIntent().getStringExtra("imdb");
        this.tmdb = getIntent().getIntExtra("tmdb", 0);
        this.backdrop = getIntent().getStringExtra("backdrop");
        this.season = getIntent().getIntExtra("season", 0);
        this.episode = getIntent().getIntExtra("episode", 0);
        this.episodeId = getIntent().getIntExtra("episodeId", 0);
        this.posterUrl = getIntent().getStringExtra("poster");
        this.overview = getIntent().getStringExtra("overview");
        this.rating = Double.valueOf(getIntent().getDoubleExtra("rating", 0.0d));
        if (this.season > null && this.episode > null) {
            this.titleSimple = this.titleSimple.replace(String.format(" S%02dE%02d", new Object[]{Integer.valueOf(this.season), Integer.valueOf(this.episode)}), "");
        }
        hideList();
        hideLoading();
        hideError();
        hideUpperBar();
        this.isFromService = getIntent().getBooleanExtra("isFromService", false);
        this.isFromDownloads = getIntent().getBooleanExtra("isFromDownloads", false);
        this.downloadLocation = getIntent().getStringExtra("location");
        this.downloadFilename = getIntent().getStringExtra("filename");
        this.movie = getIntent().getBooleanExtra("movie", false);
        this.show = getIntent().getBooleanExtra("show", false);
        if (!(Settings.AUTOSELECT_SOURCES_MOVIES == null || this.movie == null)) {
            this.isAutoPlay = true;
        }
        if (Settings.AUTOSELECT_SOURCES_SHOWS != null && this.movie == null) {
            this.isAutoPlay = true;
        }
        if (this.movie == null) {
            if (this.show == null) {
                finish();
                return;
            }
        }
        setContent(this.movie);
    }

    private void showContextMenu(final Source source) {
        String string = getString(R.string.option_download);
        String string2 = getString(R.string.option_playwith);
        String string3 = getString(R.string.option_copylink);
        CharSequence[] charSequenceArr = new CharSequence[]{string, string2, string3};
        Builder builder = new Builder(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.titleWithSE);
        stringBuilder.append(" (");
        stringBuilder.append(source.getSource());
        stringBuilder.append(")");
        builder.setTitle(stringBuilder.toString());
        builder.setItems(charSequenceArr, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                switch (i) {
                    case 0:
                        if (source.isTorrent() == 0 && source.getSource().toLowerCase().contains("hls") == 0 && source.getUrl().toLowerCase().contains(".m3u8") == 0) {
                            dialogInterface = new DownloadMetadata();
                            dialogInterface.source = source;
                            dialogInterface.season = SourceList.this.season;
                            dialogInterface.episode = SourceList.this.episode;
                            dialogInterface.episode_title = SourceList.this.episode_title;
                            dialogInterface.episodeId = SourceList.this.episodeId;
                            dialogInterface.imdb = SourceList.this.imdb;
                            dialogInterface.movie = SourceList.this.movie;
                            dialogInterface.show = SourceList.this.show;
                            dialogInterface.overview = SourceList.this.overview;
                            dialogInterface.titleSimple = SourceList.this.titleSimple;
                            dialogInterface.titleWithSE = SourceList.this.titleWithSE;
                            dialogInterface.tmdb = SourceList.this.tmdb;
                            dialogInterface.year = SourceList.this.year;
                            dialogInterface.rating = SourceList.this.rating.doubleValue();
                            SourceList.this.downloadSource(source, dialogInterface, true);
                            return;
                        }
                        Toast.makeText(SourceList.this, SourceList.this.getString(R.string.error_add_download), 0).show();
                        return;
                    case 1:
                        try {
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.setDataAndType(Uri.parse(source.getUrl()), "video/*");
                            SourceList.this.startActivityForResult(Intent.createChooser(intent, SourceList.this.getString(R.string.option_playwith)), 12345);
                            return;
                        } catch (int i2) {
                            i2.printStackTrace();
                            Toast.makeText(SourceList.this, SourceList.this.getString(R.string.error_playwith), 0).show();
                            return;
                        }
                    case 2:
                        ClipboardManager clipboardManager = (ClipboardManager) SourceList.this.getSystemService("clipboard");
                        if (clipboardManager != null) {
                            clipboardManager.setPrimaryClip(ClipData.newPlainText(ImagesContract.URL, source.getUrl()));
                            i2 = SourceList.this;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(SourceList.this.getString(R.string.copiedlink));
                            stringBuilder.append("\n");
                            stringBuilder.append(source.getUrl());
                            Toast.makeText(i2, stringBuilder.toString(), 0).show();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        });
        builder.create().show();
    }

    protected void onResume() {
        if (this.list != null) {
            this.list.requestFocus();
        }
        TorrentHelper.addListener(this);
        super.onResume();
    }

    protected void onPause() {
        TorrentHelper.removeListener(this);
        super.onPause();
    }

    public void sortSources(View view) {
        initializeSourceList();
        String string = getString(R.string.pref_sourcelist_grouping_host);
        if (Settings.GROUP_SOURCES.toLowerCase().equals(getString(R.string.pref_sourcelist_grouping_host).toLowerCase())) {
            string = getString(R.string.pref_sourcelist_grouping_provider);
        } else if (Settings.GROUP_SOURCES.toLowerCase().equals(getString(R.string.pref_sourcelist_grouping_provider).toLowerCase())) {
            string = getString(R.string.pref_sourcelist_grouping_quality);
        } else if (Settings.GROUP_SOURCES.toLowerCase().equals(getString(R.string.pref_sourcelist_grouping_quality).toLowerCase())) {
            string = getString(R.string.pref_sourcelist_grouping_size);
        } else if (Settings.GROUP_SOURCES.toLowerCase().equals(getString(R.string.pref_sourcelist_grouping_size).toLowerCase())) {
            string = getString(R.string.pref_sourcelist_grouping_host);
        }
        Settings.GROUP_SOURCES = string;
        refreshSourceList(false);
        if (!(this.list == null || this.sourceAdapter == null)) {
            for (int i = 0; i < this.sourceAdapter.getGroupCount(); i++) {
                this.list.collapseGroup(i);
            }
        }
        refreshSourceList(false);
    }

    private void PlayVideoCast(Source source, SubtitleResult subtitleResult) {
        Exception exception;
        Exception e;
        SourceList sourceList;
        SubtitleResult subtitleResult2 = subtitleResult;
        Context context;
        try {
            if (source.isCastable()) {
                long loadLong = Settings.loadLong(r1, source.getTitle());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Playing URL : ");
                stringBuilder.append(source.getUrl());
                Log.d("Casting", stringBuilder.toString());
                final RemoteMediaClient mediaClient = getMediaClient();
                if (r1.mCastSession != null) {
                    if (mediaClient != null) {
                        String stringBuilder2;
                        MediaMetadata mediaMetadata = new MediaMetadata(1);
                        mediaMetadata.putString(MediaMetadata.KEY_SUBTITLE, source.getFilename());
                        mediaMetadata.putString(MediaMetadata.KEY_TITLE, r1.titleWithSE);
                        if (r1.mediaImages != null) {
                            if (r1.mediaImages.posterUrl != null && !r1.mediaImages.posterUrl.isEmpty()) {
                                mediaMetadata.addImage(new WebImage(Uri.parse(r1.mediaImages.posterUrl)));
                            } else if (!(r1.mediaImages.backdropUrl == null || r1.mediaImages.backdropUrl.isEmpty())) {
                                mediaMetadata.addImage(new WebImage(Uri.parse(r1.mediaImages.backdropUrl)));
                            }
                        } else if (r1.movie && r1.posterUrl != null && !r1.posterUrl.isEmpty()) {
                            mediaMetadata.addImage(new WebImage(Uri.parse(r1.posterUrl)));
                        } else if (!(r1.backdrop == null || r1.backdrop.isEmpty())) {
                            mediaMetadata.addImage(new WebImage(Uri.parse(r1.backdrop)));
                        }
                        ArrayList arrayList = new ArrayList();
                        if (subtitleResult2 != null) {
                            arrayList.add(subtitleResult2);
                        }
                        if (r1.subtitles != null) {
                            Iterator it = r1.subtitles.iterator();
                            while (it.hasNext()) {
                                SubtitleResult subtitleResult3 = (SubtitleResult) it.next();
                                if (subtitleResult3 != subtitleResult2) {
                                    Iterator it2 = arrayList.iterator();
                                    int i = 0;
                                    while (it2.hasNext()) {
                                        if (((SubtitleResult) it2.next()).getLanguage().equals(subtitleResult3.getLanguage())) {
                                            i++;
                                        }
                                    }
                                    if (i < 4) {
                                        arrayList.add(subtitleResult3);
                                    }
                                }
                            }
                        }
                        Gson gson = new Gson();
                        List arrayList2 = new ArrayList();
                        Iterator it3 = arrayList.iterator();
                        int i2 = 1;
                        while (it3.hasNext()) {
                            SubtitleResult subtitleResult4 = (SubtitleResult) it3.next();
                            String encode = Base64.encode(gson.toJson(subtitleResult4));
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("http://");
                            stringBuilder3.append(Utils.getIP());
                            stringBuilder3.append(":16737/subtitles.vtt?id=");
                            stringBuilder3.append(encode);
                            stringBuilder2 = stringBuilder3.toString();
                            Gson gson2 = gson;
                            try {
                                MediaTrack.Builder builder = new MediaTrack.Builder((long) i2, 1);
                                StringBuilder stringBuilder4 = new StringBuilder();
                                stringBuilder4.append("[");
                                stringBuilder4.append(subtitleResult4.getLanguage().toUpperCase());
                                stringBuilder4.append("] ");
                                stringBuilder4.append(subtitleResult4.getFilename().toLowerCase());
                                arrayList2.add(builder.setName(stringBuilder4.toString()).setSubtype(1).setContentId(stringBuilder2).setContentType(MimeTypes.TEXT_VTT).setLanguage(subtitleResult4.getLanguage()).build());
                                i2++;
                                gson = gson2;
                                SourceList sourceList2 = this;
                            } catch (Exception e2) {
                                exception = e2;
                                sourceList = this;
                            }
                        }
                        final TextTrackStyle textTrackStyle = new TextTrackStyle();
                        textTrackStyle.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        if (Settings.SUBTITLES_COLOR.toLowerCase().equals("white")) {
                            textTrackStyle.setForegroundColor(Color.parseColor("#FFFFDE"));
                        }
                        try {
                            if (Settings.SUBTITLES_COLOR.toLowerCase().equals("yellow")) {
                                textTrackStyle.setForegroundColor(Color.parseColor("#FFFFDE"));
                            }
                            textTrackStyle.setEdgeColor(Color.parseColor("#000000"));
                            textTrackStyle.setFontFamily("Droid Sans");
                            textTrackStyle.setFontGenericFamily(0);
                            textTrackStyle.setFontStyle(1);
                            textTrackStyle.setEdgeType(1);
                            textTrackStyle.setWindowType(0);
                            textTrackStyle.setFontScale((Float.parseFloat(Settings.SUBTITLES_SIZE) / 100.0f) + 0.8f);
                            String url = source.getUrl();
                            StringBuilder stringBuilder5 = new StringBuilder();
                            stringBuilder5.append("SubFont=");
                            stringBuilder5.append(String.valueOf(textTrackStyle.getFontScale()));
                            stringBuilder5.append(" url=");
                            stringBuilder5.append(url);
                            Log.d("Casting", stringBuilder5.toString());
                            stringBuilder2 = MimeTypes.VIDEO_MP4;
                            if (source.getUrl().contains(".m3u8")) {
                                stringBuilder2 = MimeTypes.APPLICATION_M3U8;
                            }
                            MediaInfo.Builder metadata = new MediaInfo.Builder(url).setStreamType(1).setContentType(stringBuilder2).setMetadata(mediaMetadata);
                            if (arrayList2.size() > 0) {
                                metadata.setMediaTracks(arrayList2);
                            }
                            MediaInfo build = metadata.build();
                            MediaLoadOptions.Builder builder2 = new MediaLoadOptions.Builder();
                            builder2.setAutoplay(true);
                            builder2.setPlayPosition(loadLong);
                            if (arrayList.size() > 0) {
                                builder2.setActiveTrackIds(new long[]{1});
                            }
                            PendingResult load = mediaClient.load(build, builder2.build());
                            final Source source2 = source;
                            try {
                                load.setResultCallback(new ResultCallback<MediaChannelResult>() {

                                    /* renamed from: com.android.morpheustv.sources.SourceList$2$1 */
                                    class C05521 implements View.OnClickListener {
                                        C05521() {
                                        }

                                        public void onClick(View view) {
                                            SourceList.this.PlayVideo(source2, SourceList.this.currentSubtitle, SourceList.this.currentLocalSubtitle);
                                        }
                                    }

                                    /* renamed from: com.android.morpheustv.sources.SourceList$2$2 */
                                    class C05532 implements View.OnClickListener {
                                        C05532() {
                                        }

                                        public void onClick(View view) {
                                            SourceList.this.PlayVideo(source2, SourceList.this.currentSubtitle, SourceList.this.currentLocalSubtitle);
                                        }
                                    }

                                    public void onResult(@NonNull MediaChannelResult mediaChannelResult) {
                                        SourceList.this.initializeSourceList();
                                        SourceList.isPlaying = false;
                                        if (mediaChannelResult.getCustomData() != null) {
                                            Log.d("CastCustomData", mediaChannelResult.getCustomData().toString());
                                        }
                                        if (mediaChannelResult.getStatus() != null) {
                                            Log.d("CastCustomData", mediaChannelResult.getStatus().toString());
                                            if (mediaChannelResult.getStatus().isSuccess()) {
                                                mediaClient.setTextTrackStyle(textTrackStyle);
                                                BaseActivity.setCastMediaKey(SourceList.this, source2.getTitle(), SourceList.this.season, SourceList.this.episode, SourceList.this.episodeId, SourceList.this.imdb);
                                                SourceList.this.startActivity(new Intent(SourceList.this, ExpandedControlsActivity.class));
                                                if (source2.isTorrent()) {
                                                    SourceList.this.setState(State.PREPARE_TORRENT);
                                                    SourceList.this.showQuestion(SourceList.this.getString(R.string.initiate_torrent_playback), null, SourceList.this.getString(R.string.play_no_subtitles), null, new C05521());
                                                    return;
                                                }
                                                return;
                                            }
                                            SourceList.this.setState(State.PLAYING);
                                            if (!(source2.isProxified() || source2.isTorrent())) {
                                                if (!SourceList.this.isFromDownloads) {
                                                    SourceList.this.setProxy(source2);
                                                    SourceList.this.PlayVideo(source2, SourceList.this.currentSubtitle, SourceList.this.currentLocalSubtitle);
                                                    return;
                                                }
                                            }
                                            BaseActivity.setCastMediaKey(SourceList.this, null, 0, 0, 0, null);
                                            SourceList sourceList = SourceList.this;
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append(SourceList.this.getString(R.string.cast_error));
                                            stringBuilder.append(String.valueOf(mediaChannelResult.getStatus().getStatusCode()));
                                            sourceList.showError(stringBuilder.toString(), null, SourceList.this.getString(R.string.try_again), null, new C05532());
                                        }
                                    }
                                });
                            } catch (Exception e3) {
                                e2 = e3;
                                exception = e2;
                                exception.printStackTrace();
                            }
                        } catch (Exception e4) {
                            e2 = e4;
                            sourceList = this;
                            exception = e2;
                            exception.printStackTrace();
                        }
                    }
                }
                context = r1;
                return;
            }
            showError(getString(R.string.not_castable));
        } catch (Exception e5) {
            e2 = e5;
            context = this;
            exception = e2;
            exception.printStackTrace();
        }
    }

    private void setProxy(Source source) {
        try {
            HttpServer.setTargetSource(source);
            if (source.getOriginalUrl().contains(".m3u8")) {
                Uri.Builder scheme = Uri.parse(source.getUrl()).buildUpon().scheme("http");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Utils.getIP());
                stringBuilder.append(":16737");
                source.setUrl(scheme.encodedAuthority(stringBuilder.toString()).build().toString());
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("http://");
                stringBuilder2.append(Utils.getIP());
                stringBuilder2.append(":16737/video.mp4");
                source.setUrl(stringBuilder2.toString());
            }
            source.setProxified(true);
        } catch (Source source2) {
            source2.printStackTrace();
        }
    }

    private void setContent(boolean z) {
        showLoading(getString(R.string.searching_sources));
        if (this.isAutoPlay) {
            showLoading(getString(R.string.searching_sources_autoplay));
        }
        if (r8.isFromService) {
            ((TextView) findViewById(R.id.movie_name)).setText(r8.titleWithSE);
            r8.mEpisodeTitle.setVisibility(8);
            r8.mBackdrop.setBackgroundResource(R.drawable.fanart);
            findViewById(R.id.media_info_panel).setVisibility(4);
            findViewById(R.id.sourcesTitle).setVisibility(8);
        } else {
            View findViewById = findViewById(R.id.rating_container);
            if (r8.rating.doubleValue() > 0.0d) {
                ((TextView) findViewById(R.id.rating)).setText(String.format("%.1f", new Object[]{r8.rating}).replace(",", "."));
                findViewById.setVisibility(0);
            } else {
                findViewById.setVisibility(8);
            }
            if (z) {
                r8.mPoster = (ImageView) findViewById(R.id.movie_poster);
                r8.mPoster.setVisibility(0);
                r8.mEpisodeTitle.setVisibility(8);
            } else {
                r8.mPoster = (ImageView) findViewById(R.id.show_poster);
                r8.mPoster.setVisibility(0);
                r8.mEpisodeTitle.setVisibility(0);
                r8.mEpisodeTitle.setText(r8.episode_title);
            }
            setMediaImages(true);
            TextView textView = (TextView) findViewById(R.id.movie_synopsis);
            if (!(r8.overview == null || r8.overview.isEmpty())) {
                textView.setText(r8.overview);
            }
            ((TextView) findViewById(R.id.movie_name)).setText(r8.titleWithSE);
        }
        if (r8.isFromDownloads) {
            setState(State.SOURCES);
            HttpServer.setTargetDownloadedFile(r8.downloadLocation);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://");
            stringBuilder.append(Utils.getIP());
            stringBuilder.append(":16737/download.mp4");
            Source source = new Source(r8.titleWithSE, "FILE", BaseProvider.getQualityFromUrl(r8.downloadFilename), "DOWNLOADS", stringBuilder.toString());
            source.setFilename(r8.downloadFilename);
            source.setCastable(true);
            r8.sources = new ArrayList();
            initializeSourceList();
            r8.sources.add(source);
            refreshSourceList(true);
            r8.currentSource = source;
            loadSubtitles(r8.imdb, r8.season, r8.episode);
        } else if (r8.isAutoPlay) {
            setState(State.SOURCES);
            Fetcher.isBusy = true;
            initializeSourceList();
            findBestSourceAutoPlay(r8.titleSimple, r8.titleWithSE, r8.year, r8.imdb, r8.season, r8.episode, new C05553());
        } else {
            fetchSources(r8.titleSimple, r8.year, r8.imdb, r8.season, r8.episode);
        }
    }

    private void setState(State state) {
        this.currentState = state;
        if (this.currentState != State.SOURCES || this.sources == null || this.sources.size() <= null) {
            findViewById(R.id.sortSources).setVisibility(4);
        } else {
            findViewById(R.id.sortSources).setVisibility(0);
        }
    }

    private void showUpperLoading(String str) {
        findViewById(R.id.upperloading).setVisibility(0);
        ((TextView) findViewById(R.id.upper_loading_message)).setText(str);
        findViewById(R.id.upper_loading_progress).setVisibility(0);
        ((TextView) findViewById(R.id.upper_loading_message)).setTextColor(getResources().getColor(R.color.black_overlay_text_color));
    }

    private void hideUpperBar() {
        findViewById(R.id.upperloading).setVisibility(8);
    }

    private void showUpperSecondaryTitle(String str) {
        findViewById(R.id.upperloading).setVisibility(0);
        ((TextView) findViewById(R.id.upper_loading_message)).setText(str);
        ((TextView) findViewById(R.id.upper_loading_message)).setTextColor(InputDeviceCompat.SOURCE_ANY);
        findViewById(R.id.upper_loading_progress).setVisibility(8);
    }

    private void showLoading(String str) {
        hideList();
        hideError();
        findViewById(R.id.fetchProgress).setVisibility(0);
        findViewById(R.id.fetchProgressBar).setVisibility(0);
        ((TextView) findViewById(R.id.task_name)).setText(str);
    }

    private void showStatusMessage(String str) {
        hideList();
        hideError();
        findViewById(R.id.fetchProgressBar).setVisibility(4);
        findViewById(R.id.fetchProgress).setVisibility(0);
        ((TextView) findViewById(R.id.task_name)).setText(str);
    }

    private void hideLoading() {
        findViewById(R.id.fetchProgress).setVisibility(8);
    }

    private void showList(String str) {
        hideLoading();
        hideError();
        findViewById(R.id.sourceList).setVisibility(0);
        findViewById(R.id.sourcesTitle).setVisibility(0);
        ((TextView) findViewById(R.id.sourcesTitle)).setText(str);
        if (this.currentState != State.SOURCES || this.sources == null || this.sources.size() <= null) {
            findViewById(R.id.sortSources).setVisibility(4);
        } else {
            findViewById(R.id.sortSources).setVisibility(0);
        }
    }

    private void hideList() {
        findViewById(R.id.sourceList).setVisibility(8);
        findViewById(R.id.sourcesTitle).setVisibility(8);
        findViewById(R.id.sortSources).setVisibility(4);
    }

    private void showError(String str) {
        hideLoading();
        hideList();
        findViewById(R.id.error_icon).setVisibility(0);
        findViewById(R.id.error_notification).setVisibility(0);
        ((TextView) findViewById(R.id.error_message)).setText(str);
        findViewById(R.id.continue_button).setVisibility(8);
    }

    private void showError(String str, String str2, String str3, View.OnClickListener onClickListener, View.OnClickListener onClickListener2) {
        showError(str);
        if (!(str2 == null || onClickListener == null)) {
            Button button = (Button) findViewById(R.id.continue_button);
            button.setVisibility(0);
            button.setText(str2);
            button.setOnClickListener(onClickListener);
        }
        if (str3 != null && onClickListener2 != null) {
            Button button2 = (Button) findViewById(R.id.retry_button);
            button2.setVisibility(0);
            button2.setText(str3);
            button2.setOnClickListener(onClickListener2);
            button2.requestFocus();
        }
    }

    private void showQuestion(String str, String str2, String str3, View.OnClickListener onClickListener, View.OnClickListener onClickListener2) {
        showError(str);
        findViewById(R.id.error_icon).setVisibility(8);
        if (!(str2 == null || onClickListener == null)) {
            Button button = (Button) findViewById(R.id.continue_button);
            button.setVisibility(0);
            button.setText(str2);
            button.setOnClickListener(onClickListener);
        }
        if (str3 != null && onClickListener2 != null) {
            Button button2 = (Button) findViewById(R.id.retry_button);
            button2.setVisibility(0);
            button2.setText(str3);
            button2.setOnClickListener(onClickListener2);
            button2.requestFocus();
        }
    }

    private void hideError() {
        findViewById(R.id.error_notification).setVisibility(8);
    }

    private void fetchSources(String str, int i, String str2, int i2, int i3) {
        Fetcher.isBusy = true;
        initializeSourceList();
        Fetcher.fetchSources(this, str, String.valueOf(i), String.valueOf(str2), i2, i3, 0, new C05594());
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 82) {
            return super.onKeyUp(i, keyEvent);
        }
        if (!(this.list == 0 || this.list.getSelectedItem() == 0)) {
            showContextMenu((Source) this.list.getSelectedItem());
        }
        return true;
    }

    public void initializeSourceList() {
        setState(State.SOURCES);
        if (this.sources == null) {
            this.sources = new ArrayList();
        }
        this.list = (ExpandableListView) findViewById(R.id.sourceList);
        this.list.setOnChildClickListener(null);
        this.sourceAdapter = new SourceListAdapter(this);
        this.sourceAdapter.setSources(this.sources);
        this.list.setAdapter(this.sourceAdapter);
        this.list.setOnItemLongClickListener(new C05605());
        this.list.setOnChildClickListener(new C05616());
        refreshSourceList(true);
    }

    public void refreshSourceList(boolean z) {
        if (this.list != null && this.sources != null && this.sources.size() > 0) {
            showList(getString(R.string.select_source));
            Collection arrayList = new ArrayList();
            Collection arrayList2 = new ArrayList();
            Collection arrayList3 = new ArrayList();
            Collection arrayList4 = new ArrayList();
            Iterator it = this.sources.iterator();
            while (true) {
                int i = 1;
                if (it.hasNext()) {
                    Source source = (Source) it.next();
                    String quality = source.getQuality();
                    int hashCode = quality.hashCode();
                    if (hashCode == 2300) {
                        if (quality.equals(BaseProvider.HD)) {
                            switch (i) {
                                case 0:
                                    arrayList3.add(source);
                                    break;
                                case 1:
                                    arrayList2.add(source);
                                    break;
                                case 2:
                                    arrayList.add(source);
                                    break;
                                default:
                                    arrayList4.add(source);
                                    break;
                            }
                        }
                    } else if (hashCode == 2641) {
                        if (quality.equals(BaseProvider.SD)) {
                            i = 0;
                            switch (i) {
                                case 0:
                                    arrayList3.add(source);
                                    break;
                                case 1:
                                    arrayList2.add(source);
                                    break;
                                case 2:
                                    arrayList.add(source);
                                    break;
                                default:
                                    arrayList4.add(source);
                                    break;
                            }
                        }
                    } else if (hashCode == 46737913) {
                        if (quality.equals(BaseProvider.FULLHD)) {
                            i = 2;
                            switch (i) {
                                case 0:
                                    arrayList3.add(source);
                                    break;
                                case 1:
                                    arrayList2.add(source);
                                    break;
                                case 2:
                                    arrayList.add(source);
                                    break;
                                default:
                                    arrayList4.add(source);
                                    break;
                            }
                        }
                    }
                    i = -1;
                    switch (i) {
                        case 0:
                            arrayList3.add(source);
                            break;
                        case 1:
                            arrayList2.add(source);
                            break;
                        case 2:
                            arrayList.add(source);
                            break;
                        default:
                            arrayList4.add(source);
                            break;
                    }
                }
                Collections.sort(arrayList, new C05627());
                Collections.sort(arrayList2, new C05638());
                Collections.sort(arrayList3, new C05649());
                Collections.sort(arrayList4, new Comparator<Source>() {
                    public int compare(Source source, Source source2) {
                        return source.getProvider().compareTo(source2.getProvider());
                    }
                });
                String format = String.format("1080p:%s    HD:%s    SD:%s    UKN:%s", new Object[]{String.valueOf(arrayList.size()), String.valueOf(arrayList2.size()), String.valueOf(arrayList3.size()), String.valueOf(arrayList4.size())});
                if (Fetcher.isBusy) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(getString(R.string.searching_sources));
                    stringBuilder.append("\n[ ");
                    stringBuilder.append(format);
                    stringBuilder.append(" ]");
                    showUpperLoading(stringBuilder.toString());
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("[ ");
                    stringBuilder2.append(format);
                    stringBuilder2.append(" ]");
                    showUpperSecondaryTitle(stringBuilder2.toString());
                }
                this.sources.clear();
                this.sources.addAll(arrayList);
                this.sources.addAll(arrayList2);
                this.sources.addAll(arrayList3);
                this.sources.addAll(arrayList4);
                if (this.sourceAdapter != null) {
                    this.sourceAdapter.setSources(this.sources);
                    if (this.currentState == State.SOURCES) {
                        findViewById(R.id.sortSources).setVisibility(0);
                    } else {
                        findViewById(R.id.sortSources).setVisibility(4);
                    }
                    if (z) {
                        this.list.requestFocus();
                        return;
                    }
                    return;
                }
                return;
            }
        } else if (Fetcher.isBusy) {
            showLoading(getString(R.string.searching_sources));
            if (this.isAutoPlay) {
                showLoading(getString(true));
            }
        } else {
            showError(getString(true), null, getString(true), new View.OnClickListener() {
                public void onClick(View view) {
                }
            }, new View.OnClickListener() {
                public void onClick(View view) {
                    if (SourceList.this.movie == null) {
                        if (SourceList.this.show == null) {
                            SourceList.this.finish();
                            return;
                        }
                    }
                    SourceList.this.setContent(SourceList.this.movie);
                }
            });
        }
    }

    public void loadSubtitles(final String str, final int i, final int i2) {
        setState(State.SUBTITLES);
        this.subtitles = new ArrayList();
        Fetcher.isBusy = false;
        if (this.currentSource == null || this.currentSource.isCastable() || this.mCastSession == null || !(this.mCastSession.isConnected() || this.mCastSession.isConnecting())) {
            if (Settings.SUBTITLES_LANGS.trim().isEmpty()) {
                PlayVideo(this.currentSource, null, null);
            } else {
                showLoading(getString(R.string.searching_subtitles));
                if (this.currentSource != null) {
                    showUpperSecondaryTitle(this.currentSource.getFilename());
                } else {
                    hideUpperBar();
                }
                Fetcher.fetchSubtitles(str, this.titleSimple, Settings.SUBTITLES_LANGS, i, i2, new FetchSubtitleListener() {

                    /* renamed from: com.android.morpheustv.sources.SourceList$13$2 */
                    class C05442 implements Runnable {

                        /* renamed from: com.android.morpheustv.sources.SourceList$13$2$1 */
                        class C05421 implements View.OnClickListener {
                            C05421() {
                            }

                            public void onClick(View view) {
                                SourceList.this.PlayVideo(SourceList.this.currentSource, null, null);
                            }
                        }

                        /* renamed from: com.android.morpheustv.sources.SourceList$13$2$2 */
                        class C05432 implements View.OnClickListener {
                            C05432() {
                            }

                            public void onClick(View view) {
                                SourceList.this.loadSubtitles(str, i, i2);
                            }
                        }

                        C05442() {
                        }

                        public void run() {
                            if (SourceList.this.currentSource != null) {
                                SourceList.this.showUpperSecondaryTitle(SourceList.this.currentSource.getFilename());
                            } else {
                                SourceList.this.hideUpperBar();
                            }
                            if (SourceList.this.currentState == State.SUBTITLES && SourceList.this.isForeground) {
                                if (SourceList.this.subtitles != null) {
                                    if (!SourceList.this.subtitles.isEmpty()) {
                                        if (Settings.SUBTITLES_AUTOSEL) {
                                            SourceList.this.downloadSubtitle((SubtitleResult) SourceList.this.subtitles.get(0));
                                            return;
                                        }
                                        return;
                                    }
                                }
                                SourceList.this.showError(SourceList.this.getString(R.string.no_subtitles), SourceList.this.getString(R.string.play_no_subtitles), SourceList.this.getString(R.string.try_again), new C05421(), new C05432());
                            }
                        }
                    }

                    public void onFetch(final CopyOnWriteArrayList<SubtitleResult> copyOnWriteArrayList) {
                        if (SourceList.this.currentState == State.SUBTITLES && SourceList.this.isForeground && Fetcher.isBusySubtites) {
                            SourceList.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    SourceList.this.subtitles = new ArrayList(copyOnWriteArrayList);
                                    final String filename = SourceList.this.currentSource.getFilename();
                                    Collections.sort(SourceList.this.subtitles, new Comparator<SubtitleResult>() {
                                        public int compare(SubtitleResult subtitleResult, SubtitleResult subtitleResult2) {
                                            if (Utils.stringSimilarity(filename, subtitleResult.getFilename()) < Utils.stringSimilarity(filename, subtitleResult2.getFilename())) {
                                                return 1;
                                            }
                                            return Utils.stringSimilarity(filename, subtitleResult.getFilename()) > Utils.stringSimilarity(filename, subtitleResult2.getFilename()) ? -1 : null;
                                        }
                                    });
                                    Collection arrayList = new ArrayList();
                                    for (String str : Arrays.asList(Settings.SUBTITLES_LANGS.split(","))) {
                                        Iterator it = SourceList.this.subtitles.iterator();
                                        while (it.hasNext()) {
                                            SubtitleResult subtitleResult = (SubtitleResult) it.next();
                                            String str2 = "srt";
                                            if (subtitleResult.provider.equals("OPENSUBTITLES")) {
                                                str2 = FilenameUtils.getExtension(subtitleResult.getFilename()).toLowerCase();
                                            }
                                            if (str.equals(subtitleResult.getLanguage()) && (r5.equals("srt") || r5.equals("sub"))) {
                                                arrayList.add(subtitleResult);
                                            }
                                        }
                                    }
                                    SourceList.this.subtitles.clear();
                                    SourceList.this.subtitles.addAll(arrayList);
                                    if (!Settings.SUBTITLES_AUTOSEL) {
                                        SourceList.this.showSubtitles();
                                    }
                                }
                            });
                        }
                    }

                    public void onFinish() {
                        SourceList.this.runOnUiThread(new C05442());
                    }
                });
            }
            return;
        }
        showError(getString(R.string.not_castable));
    }

    public void showSubtitles() {
        setState(State.SUBTITLES);
        Fetcher.isBusy = false;
        this.list = (ExpandableListView) findViewById(R.id.sourceList);
        this.list.setOnItemClickListener(null);
        if (this.currentSource != null) {
            showUpperSecondaryTitle(this.currentSource.getFilename());
        } else {
            hideUpperBar();
        }
        if (this.subtitles == null || this.subtitles.size() <= 0) {
            showError(getString(R.string.no_subtitles), getString(R.string.play_no_subtitles), getString(R.string.try_again), new View.OnClickListener() {
                public void onClick(View view) {
                    SourceList.this.PlayVideo(SourceList.this.currentSource, null, null);
                }
            }, new View.OnClickListener() {
                public void onClick(View view) {
                    SourceList.this.loadSubtitles(SourceList.this.imdb, SourceList.this.season, SourceList.this.episode);
                }
            });
            return;
        }
        try {
            if (Fetcher.isBusySubtites) {
                showUpperSecondaryTitle(getString(R.string.searching_subtitles));
            }
            showList(getString(R.string.select_subtitle));
            final SubtitleListAdapter subtitleListAdapter = new SubtitleListAdapter(this);
            this.list.setAdapter(subtitleListAdapter);
            subtitleListAdapter.setSources(this.subtitles);
            this.list.setOnChildClickListener(new OnChildClickListener() {
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
                    SourceList.this.downloadSubtitle(subtitleListAdapter.getChild(i, i2));
                    return true;
                }
            });
            this.list.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadSubtitle(final SubtitleResult subtitleResult) {
        if (this.subtitles != null && this.subtitles.size() > 0) {
            Fetcher.isBusySubtites = false;
            if (this.mCastSession == null || !(this.mCastSession.isConnected() || this.mCastSession.isConnecting())) {
                showLoading(getString(R.string.downloading_subtitles));
                Fetcher.downloadSubtitles(subtitleResult, new DownloadSubtitleListener() {

                    /* renamed from: com.android.morpheustv.sources.SourceList$17$2 */
                    class C05482 implements Runnable {

                        /* renamed from: com.android.morpheustv.sources.SourceList$17$2$1 */
                        class C05461 implements View.OnClickListener {
                            C05461() {
                            }

                            public void onClick(View view) {
                                SourceList.this.PlayVideo(SourceList.this.currentSource, null, null);
                            }
                        }

                        /* renamed from: com.android.morpheustv.sources.SourceList$17$2$2 */
                        class C05472 implements View.OnClickListener {
                            C05472() {
                            }

                            public void onClick(View view) {
                                SourceList.this.loadSubtitles(SourceList.this.imdb, SourceList.this.season, SourceList.this.episode);
                            }
                        }

                        C05482() {
                        }

                        public void run() {
                            if (SourceList.this.currentState == State.SUBTITLES && SourceList.this.isForeground) {
                                SourceList.this.showError(SourceList.this.getString(R.string.no_subtitles), SourceList.this.getString(R.string.play_no_subtitles), SourceList.this.getString(R.string.try_again), new C05461(), new C05472());
                            }
                        }
                    }

                    public void onDownload(final String str) {
                        SourceList.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if (SourceList.this.currentState == State.SUBTITLES && SourceList.this.isForeground) {
                                    SourceList.this.PlayVideo(SourceList.this.currentSource, subtitleResult, str);
                                }
                            }
                        });
                    }

                    public void onFail() {
                        SourceList.this.runOnUiThread(new C05482());
                    }
                });
                return;
            }
            PlayVideo(this.currentSource, subtitleResult, null);
        }
    }

    public void PlayVideo(Source source, SubtitleResult subtitleResult, String str) {
        this.currentLocalSubtitle = str;
        this.currentSubtitle = subtitleResult;
        if (!source.isTorrent() || source.isTorrentReady()) {
            if (!(!Settings.PROXY_STREAMS || source.isTorrent() || this.isFromDownloads)) {
                setProxy(source);
            }
            setState(State.PLAYING);
            showLoading(getString(R.string.playing_video));
            source.setTitle(this.titleWithSE);
            if (this.mCastSession == null || !(this.mCastSession.isConnected() || this.mCastSession.isConnecting())) {
                PlayVideoInternal(source, str);
                return;
            } else {
                PlayVideoCast(source, subtitleResult);
                return;
            }
        }
        setState(State.PREPARE_TORRENT);
        showLoading(getString(R.string.playing_video));
        initializeTorrentStreamer(source);
    }

    public void PlayVideoInternal(Source source, String str) {
        source = new Gson().toJson(new MorpheusMediaInfo(source, str, this.subtitles, this.imdb, this.titleSimple, this.season, this.episode));
        str = new Intent(this, PlayerActivity.class);
        str.putExtra("mediaInfo", source);
        startActivityForResult(str, 5500);
        isPlaying = true;
    }

    public void updateWatchedStatusFromPlayer(long j, long j2) {
        if (j > 0 && j2 > 0 && j >= (j2 / 5) * 4) {
            j = new ProgressDialog(this);
            try {
                j.setIndeterminate(true);
                j.setCancelable(0);
                j.setTitle(this.titleWithSE);
                j.setMessage(getString(2131624148));
                j.show();
            } catch (long j22) {
                j22.printStackTrace();
            }
            j22 = new OnActionListener() {

                /* renamed from: com.android.morpheustv.sources.SourceList$18$1 */
                class C05491 implements Runnable {
                    C05491() {
                    }

                    public void run() {
                        try {
                            j.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SourceList.this.finish();
                    }
                }

                public void onSuccess(Object obj) {
                    if (SourceList.this.season <= null || SourceList.this.episode <= null) {
                        Trakt.syncMovieCache(SourceList.this.getApplicationContext());
                    } else {
                        Trakt.syncShowCache(SourceList.this.getApplicationContext(), SourceList.this.imdb);
                    }
                    SourceList.this.runOnUiThread(new C05491());
                }

                public void onFail(final int i) {
                    SourceList.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                j.dismiss();
                                Context context = SourceList.this;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Operation failed with error ");
                                stringBuilder.append(String.valueOf(i));
                                Toast.makeText(context, stringBuilder.toString(), 0).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            };
            if (this.season <= null || this.episode <= null) {
                j = new MovieIds();
                j.imdb = this.imdb;
                Trakt.getInstance().markMovieWatched(j, true, j22);
                return;
            }
            j = new EpisodeIds();
            j.trakt = Integer.valueOf(this.episodeId);
            Trakt.getInstance().markEpisodeWatched(j, true, j22);
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 5500) {
            isPlaying = false;
            if (i2 == -1) {
                initializeSourceList();
                updateWatchedStatusFromPlayer(intent.getLongExtra("currentPosition", 0), intent.getLongExtra("duration", 0));
            }
            if (!(this.currentSource == 0 || this.currentSource.isTorrent() == 0)) {
                setState(State.PREPARE_TORRENT);
                showQuestion(getString(R.string.initiate_torrent_playback), null, getString(R.string.play_no_subtitles), null, new View.OnClickListener() {
                    public void onClick(View view) {
                        SourceList.this.PlayVideo(SourceList.this.currentSource, SourceList.this.currentSubtitle, SourceList.this.currentLocalSubtitle);
                    }
                });
            }
            if (this.isFromDownloads != 0) {
                finish();
            }
        }
    }

    public void onBackPressed() {
        if (Fetcher.isBusy && this.currentState == State.SOURCES) {
            Fetcher.isBusy = false;
            initializeSourceList();
        } else if (this.currentState == State.SOURCES || this.isFromDownloads) {
            Fetcher.isBusy = false;
            super.onBackPressed();
        } else {
            initializeSourceList();
        }
        TorrentHelper.stopTorrentStream(this.currentSource, this);
    }

    public void setMediaImages(boolean z) {
        try {
            if (this.backdrop == null || this.backdrop.isEmpty()) {
                this.mBackdrop.setImageResource(0);
            } else {
                Glide.with((FragmentActivity) this).load(this.backdrop).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.mBackdrop);
            }
            if (this.posterUrl == null || this.posterUrl.isEmpty()) {
                this.mPoster.setImageResource(R.drawable.poster);
            } else {
                Glide.clear(this.mPoster);
                Glide.with((FragmentActivity) this).load(this.posterUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.mPoster);
            }
            if (z) {
                new LoadPosterTask().executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
            }
        } catch (boolean z2) {
            z2.printStackTrace();
        }
    }

    private void setTorrentStatus(String str, boolean z) {
        this.torrentStatus = str;
        if (z) {
            showLoading(str);
        } else {
            showStatusMessage(str);
        }
    }

    private void setTorrentError(String str) {
        this.torrentStatus = str;
        showError(str);
    }

    public void onStreamPrepared(Torrent torrent) {
        Log.d(TORRENT, "OnStreamPrepared");
        setTorrentStatus(getString(R.string.init_torrent_loadingfile), true);
        this.currentSource.setFilename(torrent.getVideoFile().getName());
        showUpperSecondaryTitle(this.currentSource.getFilename());
    }

    public void onStreamStarted(Torrent torrent) {
        Log.d(TORRENT, "onStreamStarted");
        setTorrentStatus(getString(R.string.init_torrent_connecting), true);
    }

    public void onStreamError(Torrent torrent, Exception exception) {
        Log.e(TORRENT, "onStreamError", exception);
        TorrentHelper.stopTorrentStream(this.currentSource, this);
        torrent = new StringBuilder();
        torrent.append(getString(R.string.init_torrent_failed));
        torrent.append(" : ");
        torrent.append(exception.getMessage());
        showError(torrent.toString(), null, getString(R.string.try_again), null, new View.OnClickListener() {
            public void onClick(View view) {
                SourceList.this.PlayVideo(SourceList.this.currentSource, SourceList.this.currentSubtitle, SourceList.this.currentLocalSubtitle);
            }
        });
    }

    public void onStreamReady(Torrent torrent) {
        Log.e(TORRENT, "onStreamReady");
        if (this.currentState == State.PREPARE_TORRENT) {
            torrent.setInterestedBytes(Settings.loadLong(this, this.titleWithSE));
            this.currentSource.setTorrentReady(true);
            torrent = this.currentSource;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://");
            stringBuilder.append(Utils.getIP());
            stringBuilder.append(":16737/torrent.mp4");
            torrent.setUrl(stringBuilder.toString());
            showQuestion(getString(R.string.init_torrent_downloading), null, getString(R.string.play_no_subtitles), null, new View.OnClickListener() {
                public void onClick(View view) {
                    SourceList.this.PlayVideo(SourceList.this.currentSource, SourceList.this.currentSubtitle, SourceList.this.currentLocalSubtitle);
                }
            });
            return;
        }
        TorrentHelper.stopTorrentStream(this.currentSource, this);
    }

    public void onStreamProgress(Torrent torrent, StreamStatus streamStatus) {
        if (this.currentState == State.PREPARE_TORRENT) {
            float f = 0.0f;
            String string = getString(R.string.torrent_complete);
            if (streamStatus.bufferProgress < 100) {
                f = (float) streamStatus.bufferProgress;
                string = getString(R.string.init_torrent_metadata);
            } else if (streamStatus.progress < 100.0f) {
                f = streamStatus.progress;
                string = getString(R.string.init_torrent_downloading);
            }
            if (torrent.getTorrentHandle() != null) {
                long downloadRate = (long) torrent.getTorrentHandle().status().downloadRate();
                long uploadRate = (long) torrent.getTorrentHandle().status().uploadRate();
                String format = String.format("%s\n(%.2f%% completed - D:%s/s U:%s/s - %d seeds)", new Object[]{string, Float.valueOf(f), Utils.formatSize(this, downloadRate), Utils.formatSize(this, uploadRate), Integer.valueOf(streamStatus.seeds)});
                if (format.equals(this.torrentStatus) != null) {
                    return;
                }
                if (this.currentSource.isTorrentReady() != null) {
                    showQuestion(format, null, getString(R.string.play_no_subtitles), null, new View.OnClickListener() {
                        public void onClick(View view) {
                            SourceList.this.PlayVideo(SourceList.this.currentSource, SourceList.this.currentSubtitle, SourceList.this.currentLocalSubtitle);
                        }
                    });
                } else {
                    setTorrentStatus(format, true);
                }
            }
        }
    }

    public void onStreamStopped() {
        Log.d(TORRENT, "onStreamStopped");
        this.currentSource.setTorrentReady(false);
    }

    public void initializeTorrentStreamer(final Source source) {
        try {
            TorrentHelper.startTorrentStream(source.getUrl(), this);
            setTorrentStatus(getString(R.string.init_torrent_connecting), true);
        } catch (Exception e) {
            e.printStackTrace();
            showError(getString(R.string.init_torrent_failed), null, getString(R.string.try_again), null, new View.OnClickListener() {
                public void onClick(View view) {
                    SourceList.this.initializeTorrentStreamer(source);
                }
            });
        }
    }

    protected void onDestroy() {
        TorrentHelper.stopTorrentStream(this.currentSource, this);
        super.onDestroy();
    }
}
