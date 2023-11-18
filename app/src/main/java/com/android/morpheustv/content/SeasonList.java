package com.android.morpheustv.content;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.morpheustv.BaseActivity;
import com.android.morpheustv.BaseActivity.DownloadMetadata;
import com.android.morpheustv.BaseActivity.FetchBestSourceListener;
import com.android.morpheustv.BaseActivity.OnCheckDownloadListener;
import com.android.morpheustv.helpers.TaskManager;
import com.android.morpheustv.helpers.Tmdb;
import com.android.morpheustv.helpers.Trakt;
import com.android.morpheustv.helpers.Trakt.EpisodeResult;
import com.android.morpheustv.helpers.Trakt.OnActionListener;
import com.android.morpheustv.helpers.Trakt.SeasonResult;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.sources.Fetcher;
import com.android.morpheustv.sources.SourceList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.noname.titan.R;
import com.tonyodev.fetch2.Download;
import io.github.morpheustv.scrapers.model.Source;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.StringUtils;

public class SeasonList extends BaseActivity {
    ArrayAdapter<EpisodeResult> adapterEpisodes;
    ArrayAdapter<SeasonResult> adapterSeasons;
    private String backdrop = "";
    private int currentEpisodeIndex = 0;
    private int currentSeasonIndex = 0;
    private boolean downloadUnwatched = false;
    private String imdb = "";
    private boolean isEpisodeList = false;
    private boolean isForeground = true;
    GridView list;
    private ImageView mBackdrop;
    private String overview = "";
    private int redirectToSeason = 0;
    private ArrayList<SeasonResult> seasons;
    private String title = "";
    private Integer tmdb = Integer.valueOf(0);
    private Integer year = Integer.valueOf(0);

    /* renamed from: com.android.morpheustv.content.SeasonList$1 */
    class C04571 implements OnActionListener {

        /* renamed from: com.android.morpheustv.content.SeasonList$1$2 */
        class C04522 implements Runnable {
            C04522() {
            }

            public void run() {
                SeasonList.this.showError(SeasonList.this.getString(R.string.trakt_error));
            }
        }

        C04571() {
        }

        public void onSuccess(final Object obj) {
            SeasonList.this.runOnUiThread(new Runnable() {
                public void run() {
                    SeasonList.this.seasons = (ArrayList) obj;
                    if (SeasonList.this.seasons == null || SeasonList.this.seasons.isEmpty()) {
                        SeasonList.this.showError(SeasonList.this.getString(R.string.no_seasons_trakt));
                    } else {
                        SeasonList.this.showSeasonList();
                    }
                }
            });
        }

        public void onFail(int i) {
            SeasonList.this.runOnUiThread(new C04522());
        }
    }

    /* renamed from: com.android.morpheustv.content.SeasonList$3 */
    class C04593 implements OnItemLongClickListener {
        C04593() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            SeasonList.this.showSeasonsContextMenu((SeasonResult) SeasonList.this.seasons.get(i));
            return true;
        }
    }

    /* renamed from: com.android.morpheustv.content.SeasonList$4 */
    class C04604 implements OnItemClickListener {
        C04604() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            SeasonResult seasonResult = (SeasonResult) SeasonList.this.seasons.get(i);
            if (SeasonList.this.currentSeasonIndex == i) {
                SeasonList.this.showEpisodeList(seasonResult);
                return;
            }
            SeasonList.this.setOverview(seasonResult.season.overview);
            SeasonList.this.currentSeasonIndex = i;
            SeasonList.this.adapterSeasons.notifyDataSetChanged();
        }
    }

    /* renamed from: com.android.morpheustv.content.SeasonList$5 */
    class C04615 implements OnItemSelectedListener {
        C04615() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            SeasonList.this.setOverview(((SeasonResult) SeasonList.this.seasons.get(i)).season.overview);
            SeasonList.this.currentSeasonIndex = i;
            SeasonList.this.adapterSeasons.notifyDataSetChanged();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            SeasonList.this.setOverview("");
        }
    }

    class LoadEpisodePosterTask extends AsyncTask<Void, Void, Void> {
        EpisodeResult episode = null;
        ImageView view = null;

        public LoadEpisodePosterTask(EpisodeResult episodeResult, ImageView imageView) {
            this.view = imageView;
            this.episode = episodeResult;
        }

        protected Void doInBackground(Void... voidArr) {
            if (!(SeasonList.this.isForeground == null || SeasonList.this.isEpisodeList == null)) {
                this.episode.images = Tmdb.getEpisodeImages(SeasonList.this.tmdb, this.episode.episode.season.intValue(), this.episode.episode.number.intValue(), "en,null");
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            if (SeasonList.this.isForeground != null && SeasonList.this.isEpisodeList != null) {
                SeasonList.this.setEpisodeImage(this.episode, this.view);
            }
        }
    }

    class LoadSeasonPosterTask extends AsyncTask<Void, Void, Void> {
        SeasonResult season = null;
        ImageView view = null;

        public LoadSeasonPosterTask(SeasonResult seasonResult, ImageView imageView) {
            this.view = imageView;
            this.season = seasonResult;
        }

        protected Void doInBackground(Void... voidArr) {
            if (SeasonList.this.isForeground != null && SeasonList.this.isEpisodeList == null) {
                this.season.images = Tmdb.getSeasonImages(SeasonList.this.tmdb, this.season.season.number.intValue(), "en,null");
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            if (SeasonList.this.isForeground != null && SeasonList.this.isEpisodeList == null) {
                SeasonList.this.setSeasonImage(this.season, this.view);
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_media_list);
        this.mBackdrop = (ImageView) findViewById(R.id.backdropImage);
        this.title = getIntent().getStringExtra("title");
        this.year = Integer.valueOf(getIntent().getIntExtra("year", 0));
        this.imdb = getIntent().getStringExtra("imdb");
        this.tmdb = Integer.valueOf(getIntent().getIntExtra("tmdb", 0));
        this.overview = getIntent().getStringExtra("overview");
        this.backdrop = getIntent().getStringExtra("backdrop");
        this.redirectToSeason = getIntent().getIntExtra("redirectToSeason", 0);
        this.downloadUnwatched = getIntent().getBooleanExtra("downloadUnwatched", false);
        setOverview(this.overview);
        rotateLoading();
        if (this.backdrop != null) {
            Glide.with((FragmentActivity) this).load(this.backdrop).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(this.mBackdrop);
        }
        setBreadcrumb(0, this.title, null);
        loadSeasons(this.imdb);
    }

    public void rotateLoading() {
        Animation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        findViewById(R.id.loadingview).startAnimation(rotateAnimation);
    }

    public void setBreadcrumb(int i, String str, String str2) {
        ((TextView) findViewById(R.id.breadcrumb_title)).setText(str);
        if (i > 0) {
            ((ImageView) findViewById(R.id.breadcrumb_image)).setImageResource(i);
            ((ImageView) findViewById(R.id.breadcrumb_image)).setVisibility(0);
        } else {
            ((ImageView) findViewById(R.id.breadcrumb_image)).setVisibility(8);
        }
        if (str2 == null || str2.isEmpty()) {
            ((TextView) findViewById(R.id.breadcrumb_secondary_title)).setVisibility(8);
            return;
        }
        ((TextView) findViewById(R.id.breadcrumb_secondary_title)).setVisibility(0);
        ((TextView) findViewById(R.id.breadcrumb_secondary_title)).setText(str2);
    }

    protected void onResume() {
        super.onResume();
        this.isForeground = true;
        Trakt.updateWatchedStatus(this.imdb, this.seasons);
        if (!(this.adapterSeasons == null || this.isEpisodeList)) {
            this.adapterSeasons.notifyDataSetChanged();
        }
        if (this.adapterEpisodes != null && this.seasons != null && this.seasons.size() > this.currentSeasonIndex && this.isEpisodeList) {
            this.adapterEpisodes.notifyDataSetChanged();
        }
    }

    protected void onStop() {
        super.onStop();
        this.isForeground = false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 82) {
            return super.onKeyUp(i, keyEvent);
        }
        if (!(this.list == 0 || this.list.getSelectedItem() == 0)) {
            if (this.isEpisodeList != 0) {
                showEpisodesContextMenu((EpisodeResult) this.list.getSelectedItem());
            } else {
                showSeasonsContextMenu((SeasonResult) this.list.getSelectedItem());
            }
        }
        return true;
    }

    private void loadSeasons(String str) {
        setBreadcrumb(0, this.title, null);
        Trakt.getInstance().getSeasonsShow(str, new C04571());
    }

    private void showError(String str) {
        if (this.seasons == null || this.seasons.isEmpty()) {
            findViewById(R.id.error_notification).setVisibility(0);
            ((TextView) findViewById(R.id.error_message)).setText(str);
            findViewById(R.id.continue_button).setVisibility(8);
            findViewById(R.id.empty_list_view).setVisibility(8);
        }
    }

    private void showSeasonList() {
        setOverview(this.overview);
        int i = 0;
        setBreadcrumb(0, this.title, null);
        this.isEpisodeList = false;
        Trakt.updateWatchedStatus(this.imdb, this.seasons);
        this.list = (GridView) findViewById(R.id.medialist);
        this.list.setOnItemLongClickListener(null);
        this.list.setOnItemClickListener(null);
        this.list.setOnItemSelectedListener(null);
        this.adapterSeasons = new ArrayAdapter<SeasonResult>(this, R.layout.movie_list_item, this.seasons) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    try {
                        view = SeasonList.this.getLayoutInflater().inflate(R.layout.movie_list_item, null);
                    } catch (int i2) {
                        i2.printStackTrace();
                    }
                }
                SeasonResult seasonResult = (SeasonResult) SeasonList.this.seasons.get(i2);
                TextView textView = (TextView) view.findViewById(R.id.title);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Season ");
                stringBuilder.append(String.valueOf(seasonResult.season.number));
                textView.setText(stringBuilder.toString());
                textView = (TextView) view.findViewById(R.id.year);
                stringBuilder = new StringBuilder();
                stringBuilder.append(String.valueOf(seasonResult.season.aired_episodes));
                stringBuilder.append(" Episodes");
                textView.setText(stringBuilder.toString());
                if (seasonResult.watched) {
                    view.findViewById(R.id.watched).setVisibility(0);
                } else {
                    view.findViewById(R.id.watched).setVisibility(8);
                }
                view.findViewById(R.id.rating_container).setVisibility(8);
                ((TextView) view.findViewById(R.id.rating)).setText(String.valueOf((int) Math.round(seasonResult.season.rating.doubleValue())));
                if (SeasonList.this.currentSeasonIndex == i2) {
                    view.setBackgroundResource(R.drawable.button_selector_fixed);
                } else {
                    view.setBackgroundResource(0);
                }
                ImageView imageView = (ImageView) view.findViewById(R.id.poster);
                imageView.setImageResource(R.drawable.poster);
                SeasonList.this.loadSeasonImage(seasonResult, imageView);
                return view;
            }
        };
        this.list.setAdapter(this.adapterSeasons);
        this.list.setEmptyView(findViewById(R.id.empty_list_view));
        this.list.setOnItemLongClickListener(new C04593());
        this.list.setOnItemClickListener(new C04604());
        this.list.setOnItemSelectedListener(new C04615());
        if (this.seasons != null && this.seasons.size() > 0) {
            Iterator it;
            if (this.redirectToSeason > 0) {
                it = this.seasons.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    if (((SeasonResult) it.next()).season.number.intValue() == this.redirectToSeason) {
                        this.currentSeasonIndex = i2;
                        break;
                    }
                    i2++;
                }
                this.redirectToSeason = 0;
                this.list.setSelection(this.currentSeasonIndex);
                this.list.requestFocus();
                if (this.currentSeasonIndex < this.seasons.size()) {
                    SeasonResult seasonResult = (SeasonResult) this.seasons.get(this.currentSeasonIndex);
                    if (this.downloadUnwatched) {
                        downloadUnwatchedEpisodes(seasonResult, 0);
                        return;
                    } else {
                        showEpisodeList(seasonResult);
                        return;
                    }
                }
                return;
            }
            if (this.currentSeasonIndex >= this.seasons.size()) {
                this.currentSeasonIndex = 0;
            }
            if (this.currentSeasonIndex == 0) {
                it = this.seasons.iterator();
                while (it.hasNext()) {
                    if (!((SeasonResult) it.next()).watched) {
                        this.currentSeasonIndex = i;
                        break;
                    }
                    i++;
                }
            }
            this.list.setSelection(this.currentSeasonIndex);
            this.list.requestFocus();
        }
    }

    private void downloadUnwatchedEpisodes(final SeasonResult seasonResult, int i) {
        for (final EpisodeResult episodeResult : seasonResult.episodes) {
            if (!episodeResult.watched && episodeResult.episode.number.intValue() > i) {
                final String format = String.format("%s S%02dE%02d", new Object[]{this.title, episodeResult.episode.season, episodeResult.episode.number});
                findBestSourceDialog(this.title, format, this.year.intValue(), this.imdb, seasonResult.season.number.intValue(), episodeResult.episode.number.intValue(), new FetchBestSourceListener() {

                    /* renamed from: com.android.morpheustv.content.SeasonList$6$1 */
                    class C04621 implements Runnable {
                        C04621() {
                        }

                        public void run() {
                            SeasonList.this.downloadUnwatchedEpisodes(seasonResult, episodeResult.episode.number.intValue());
                        }
                    }

                    public void onSourcesUpdated(CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
                    }

                    public boolean handleSource(Source source) {
                        if (!Fetcher.isBusy || source.isTorrent() || source.getSource().toLowerCase().contains("hls") || source.getUrl().toLowerCase().contains(".m3u8")) {
                            return false;
                        }
                        DownloadMetadata downloadMetadata = new DownloadMetadata();
                        downloadMetadata.source = source;
                        downloadMetadata.season = seasonResult.season.number.intValue();
                        downloadMetadata.episode = episodeResult.episode.number.intValue();
                        downloadMetadata.episode_title = episodeResult.episode.title;
                        downloadMetadata.episodeId = episodeResult.episode.ids.trakt.intValue();
                        downloadMetadata.imdb = SeasonList.this.imdb;
                        if (downloadMetadata.season <= 0 || downloadMetadata.episode <= 0) {
                            downloadMetadata.movie = true;
                            downloadMetadata.show = false;
                        } else {
                            downloadMetadata.movie = false;
                            downloadMetadata.show = true;
                        }
                        downloadMetadata.overview = SeasonList.this.overview;
                        downloadMetadata.titleSimple = SeasonList.this.title;
                        downloadMetadata.titleWithSE = format;
                        downloadMetadata.tmdb = SeasonList.this.tmdb.intValue();
                        downloadMetadata.year = SeasonList.this.year.intValue();
                        downloadMetadata.rating = seasonResult.season.rating.doubleValue();
                        SeasonList.this.downloadSource(source, downloadMetadata, false);
                        return true;
                    }

                    public void onFinish(boolean z) {
                        if (!z) {
                            SeasonList.this.mainHandler.postDelayed(new C04621(), 1000);
                        }
                    }
                });
                return;
            }
        }
    }

    private void showSeasonsContextMenu(final SeasonResult seasonResult) {
        String string = getString(R.string.option_mark_watched);
        if (seasonResult.watched) {
            string = getString(R.string.option_mark_unwatched);
        }
        String string2 = getString(R.string.autofetch_dialog_option_season);
        final CharSequence[] charSequenceArr = new CharSequence[]{string, string2};
        Builder builder = new Builder(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.title);
        stringBuilder.append(" - ");
        stringBuilder.append(getString(R.string.season));
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(String.valueOf(seasonResult.season.number));
        builder.setTitle(stringBuilder.toString());
        builder.setItems(charSequenceArr, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dialogInterface = new ProgressDialog(SeasonList.this);
                dialogInterface.setIndeterminate(true);
                dialogInterface.setCancelable(false);
                dialogInterface.setTitle(charSequenceArr[i]);
                dialogInterface.setMessage(SeasonList.this.getString(R.string.please_wait_mark_watched));
                OnActionListener c04661 = new OnActionListener() {

                    /* renamed from: com.android.morpheustv.content.SeasonList$7$1$1 */
                    class C04641 implements Runnable {
                        C04641() {
                        }

                        public void run() {
                            SeasonList.this.adapterSeasons.notifyDataSetChanged();
                            dialogInterface.dismiss();
                        }
                    }

                    public void onSuccess(Object obj) {
                        Trakt.syncShowCache(SeasonList.this.getApplicationContext(), SeasonList.this.imdb);
                        Trakt.updateWatchedStatus(SeasonList.this.imdb, SeasonList.this.seasons);
                        SeasonList.this.runOnUiThread(new C04641());
                    }

                    public void onFail(final int i) {
                        SeasonList.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Context context = SeasonList.this;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Operation failed with error ");
                                stringBuilder.append(String.valueOf(i));
                                Toast.makeText(context, stringBuilder.toString(), 0).show();
                                dialogInterface.dismiss();
                            }
                        });
                    }
                };
                switch (i) {
                    case 0:
                        dialogInterface.show();
                        seasonResult.watched ^= 1;
                        Trakt.getInstance().markSeasonWatched(SeasonList.this.imdb, seasonResult.season.number.intValue(), seasonResult.watched, c04661);
                        return;
                    case 1:
                        SeasonList.this.downloadUnwatchedEpisodes(seasonResult, 0);
                        return;
                    default:
                        return;
                }
            }
        });
        builder.create().show();
    }

    private void showEpisodeList(final SeasonResult seasonResult) {
        setOverview("");
        String str = this.title;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.season));
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(String.valueOf(seasonResult.season.number));
        int i = 0;
        setBreadcrumb(0, str, stringBuilder.toString());
        this.isEpisodeList = true;
        Trakt.updateWatchedStatus(this.imdb, this.seasons);
        this.list = (GridView) findViewById(R.id.medialist);
        this.list.setOnItemLongClickListener(null);
        this.list.setOnItemClickListener(null);
        this.list.setOnItemSelectedListener(null);
        final SeasonResult seasonResult2 = seasonResult;
        this.adapterEpisodes = new ArrayAdapter<EpisodeResult>(this, R.layout.episode_list_item, seasonResult.episodes) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    try {
                        view = SeasonList.this.getLayoutInflater().inflate(R.layout.episode_list_item, null);
                    } catch (int i2) {
                        i2.printStackTrace();
                    }
                }
                EpisodeResult episodeResult = (EpisodeResult) seasonResult2.episodes.get(i2);
                ((TextView) view.findViewById(R.id.title)).setText(episodeResult.episode.title);
                TextView textView = (TextView) view.findViewById(R.id.year);
                if (episodeResult.episode.first_aired != null) {
                    textView.setText(String.valueOf(episodeResult.episode.first_aired.toLocalDate().toString()));
                } else {
                    textView.setText(String.valueOf(""));
                }
                textView = (TextView) view.findViewById(R.id.episode);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(SeasonList.this.getString(R.string.episode));
                stringBuilder.append(StringUtils.SPACE);
                stringBuilder.append(String.format("%02d", new Object[]{episodeResult.episode.number}));
                textView.setText(stringBuilder.toString());
                if (episodeResult.watched) {
                    view.findViewById(R.id.watched).setVisibility(0);
                } else {
                    view.findViewById(R.id.watched).setVisibility(8);
                }
                ImageView imageView = (ImageView) view.findViewById(R.id.poster);
                imageView.setImageResource(R.drawable.poster);
                SeasonList.this.loadEpisodeImage(episodeResult, imageView);
                if (SeasonList.this.currentEpisodeIndex == i2) {
                    view.setBackgroundResource(R.drawable.button_selector_fixed);
                } else {
                    view.setBackgroundResource(0);
                }
                return view;
            }
        };
        this.list.setAdapter(this.adapterEpisodes);
        this.list.setEmptyView(findViewById(R.id.empty_list_view));
        this.list.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                SeasonList.this.showEpisodesContextMenu((EpisodeResult) seasonResult.episodes.get(i));
                return true;
            }
        });
        this.list.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                SeasonList.this.currentEpisodeIndex = i;
                SeasonList.this.setOverview(((EpisodeResult) seasonResult.episodes.get(i)).episode.overview);
                SeasonList.this.adapterEpisodes.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                SeasonList.this.setOverview("");
            }
        });
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                final EpisodeResult episodeResult = (EpisodeResult) seasonResult.episodes.get(i);
                if (SeasonList.this.currentEpisodeIndex == i) {
                    SeasonList.this.checkDownloadReadyForPlayback(SeasonList.this.imdb, episodeResult.episode.season.intValue(), episodeResult.episode.number.intValue(), new OnCheckDownloadListener() {
                        public void OnCheckFinished(Download download) {
                            String format = String.format("%s S%02dE%02d", new Object[]{SeasonList.this.title, episodeResult.episode.season, episodeResult.episode.number});
                            Intent intent = new Intent(SeasonList.this, SourceList.class);
                            intent.putExtra("show", true);
                            intent.putExtra("title", format);
                            intent.putExtra("episode_title", episodeResult.episode.title);
                            intent.putExtra("year", SeasonList.this.year);
                            intent.putExtra("imdb", SeasonList.this.imdb);
                            intent.putExtra("tmdb", SeasonList.this.tmdb);
                            intent.putExtra("season", episodeResult.episode.season);
                            intent.putExtra("episode", episodeResult.episode.number);
                            intent.putExtra("episodeId", episodeResult.episode.ids.trakt);
                            intent.putExtra("poster", episodeResult.images.posterUrl);
                            intent.putExtra("backdrop", SeasonList.this.backdrop);
                            intent.putExtra("overview", episodeResult.episode.overview);
                            intent.putExtra("rating", episodeResult.episode.rating);
                            if (download != null) {
                                intent.putExtra("isFromDownloads", true);
                                intent.putExtra("location", download.getFile());
                                intent.putExtra("filename", Utils.getFilenameFromUrl(download.getUrl(), format));
                            }
                            SeasonList.this.startActivityForResult(intent, 5000);
                        }
                    });
                    return;
                }
                SeasonList.this.currentEpisodeIndex = i;
                SeasonList.this.setOverview(episodeResult.episode.overview);
                SeasonList.this.adapterEpisodes.notifyDataSetChanged();
            }
        });
        if (seasonResult.episodes.size() > 0) {
            if (this.currentEpisodeIndex >= seasonResult.episodes.size()) {
                this.currentEpisodeIndex = 0;
            }
            if (this.currentEpisodeIndex == 0) {
                for (EpisodeResult episodeResult : seasonResult.episodes) {
                    if (!episodeResult.watched) {
                        this.currentEpisodeIndex = i;
                        break;
                    }
                    i++;
                }
            }
            this.list.setSelection(this.currentEpisodeIndex);
            this.list.requestFocus();
            setOverview(((EpisodeResult) seasonResult.episodes.get(this.currentEpisodeIndex)).episode.overview);
            this.adapterEpisodes.notifyDataSetChanged();
        }
    }

    private void showEpisodesContextMenu(final EpisodeResult episodeResult) {
        String string = getString(R.string.option_mark_watched);
        if (episodeResult.watched) {
            string = getString(R.string.option_mark_unwatched);
        }
        String string2 = getString(R.string.autofetch_dialog_option);
        final CharSequence[] charSequenceArr = new CharSequence[]{string, string2};
        Builder builder = new Builder(this);
        builder.setTitle(String.format("%s S%02dE%02d", new Object[]{this.title, episodeResult.episode.season, episodeResult.episode.number}));
        builder.setItems(charSequenceArr, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                final ProgressDialog progressDialog = new ProgressDialog(SeasonList.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setTitle(charSequenceArr[i]);
                progressDialog.setMessage(SeasonList.this.getString(R.string.please_wait_mark_watched));
                OnActionListener c04561 = new OnActionListener() {

                    /* renamed from: com.android.morpheustv.content.SeasonList$12$1$1 */
                    class C04541 implements Runnable {
                        C04541() {
                        }

                        public void run() {
                            SeasonList.this.adapterEpisodes.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }

                    public void onSuccess(Object obj) {
                        Trakt.syncShowCache(SeasonList.this.getApplicationContext(), SeasonList.this.imdb);
                        Trakt.updateWatchedStatus(SeasonList.this.imdb, SeasonList.this.seasons);
                        SeasonList.this.runOnUiThread(new C04541());
                    }

                    public void onFail(final int i) {
                        SeasonList.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Context context = SeasonList.this;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Operation failed with error ");
                                stringBuilder.append(String.valueOf(i));
                                Toast.makeText(context, stringBuilder.toString(), 0).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                };
                switch (i) {
                    case 0:
                        progressDialog.show();
                        episodeResult.watched ^= true;
                        Trakt.getInstance().markEpisodeWatched(episodeResult.episode.ids, episodeResult.watched, c04561);
                        return;
                    case 1:
                        SeasonList.this.downloadBestSource(SeasonList.this.title, episodeResult.episode.title, SeasonList.this.title, String.format("%s S%02dE%02d", new Object[]{SeasonList.this.title, episodeResult.episode.season, episodeResult.episode.number}), episodeResult.episode.overview, episodeResult.episode.rating.doubleValue(), SeasonList.this.year.intValue(), SeasonList.this.imdb, SeasonList.this.tmdb.intValue(), episodeResult.episode.ids.trakt.intValue(), episodeResult.episode.season.intValue(), episodeResult.episode.number.intValue());
                        return;
                    default:
                        return;
                }
            }
        });
        builder.create().show();
    }

    private void setOverview(String str) {
        ((TextView) findViewById(R.id.overview)).setText(str);
    }

    public void onBackPressed() {
        if (this.isEpisodeList) {
            showSeasonList();
        } else {
            super.onBackPressed();
        }
    }

    private void loadSeasonImage(SeasonResult seasonResult, ImageView imageView) {
        imageView.setTag(seasonResult);
        if (seasonResult.images == null || seasonResult.images.posterUrl == null || !seasonResult.images.posterUrl.isEmpty() || seasonResult.images.isLoading) {
            setSeasonImage(seasonResult, imageView);
            return;
        }
        new LoadSeasonPosterTask(seasonResult, imageView).executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        seasonResult.images.isLoading = true;
    }

    private void setSeasonImage(SeasonResult seasonResult, ImageView imageView) {
        try {
            if (imageView.getTag() == seasonResult && seasonResult.images != null && seasonResult.images.posterUrl != null && !seasonResult.images.posterUrl.isEmpty()) {
                seasonResult.images.isLoading = false;
                imageView.setTag(null);
                Glide.with((FragmentActivity) this).load(seasonResult.images.posterUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder((int) R.drawable.poster).fitCenter().into(imageView);
            }
        } catch (SeasonResult seasonResult2) {
            seasonResult2.printStackTrace();
        }
    }

    private void loadEpisodeImage(EpisodeResult episodeResult, ImageView imageView) {
        imageView.setTag(episodeResult);
        if (episodeResult.images == null || episodeResult.images.posterUrl == null || !episodeResult.images.posterUrl.isEmpty() || episodeResult.images.isLoading) {
            setEpisodeImage(episodeResult, imageView);
            return;
        }
        new LoadEpisodePosterTask(episodeResult, imageView).executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        episodeResult.images.isLoading = true;
    }

    private void setEpisodeImage(EpisodeResult episodeResult, ImageView imageView) {
        try {
            if (imageView.getTag() == episodeResult && episodeResult.images != null && episodeResult.images.posterUrl != null && !episodeResult.images.posterUrl.isEmpty()) {
                episodeResult.images.isLoading = false;
                imageView.setTag(null);
                Glide.with((FragmentActivity) this).load(episodeResult.images.posterUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder((int) R.drawable.poster).fitCenter().into(imageView);
            }
        } catch (EpisodeResult episodeResult2) {
            episodeResult2.printStackTrace();
        }
    }
}
