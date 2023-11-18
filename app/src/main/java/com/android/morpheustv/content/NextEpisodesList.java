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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.morpheustv.BaseActivity;
import com.android.morpheustv.BaseActivity.OnCheckDownloadListener;
import com.android.morpheustv.helpers.TaskManager;
import com.android.morpheustv.helpers.Tmdb;
import com.android.morpheustv.helpers.Trakt;
import com.android.morpheustv.helpers.Trakt.BaseShowResult;
import com.android.morpheustv.helpers.Trakt.OnActionListener;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.sources.SourceList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.noname.titan.R;
import com.tonyodev.fetch2.Download;
import com.uwetrottmann.trakt5.entities.BaseShow;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.OffsetDateTime;

public class NextEpisodesList extends BaseActivity {
    private ArrayAdapter<BaseShowResult> adapter;
    private String currentBackdrop = "";
    public int currentSelection = 0;
    private boolean isForeground = true;
    ListView list;
    private boolean loading = false;
    private ImageView mBackdrop;
    private TextView mEpisodeTitle;
    private ImageView mPoster;
    private ArrayList<BaseShowResult> shows;

    /* renamed from: com.android.morpheustv.content.NextEpisodesList$1 */
    class C04411 implements Comparator<BaseShowResult> {
        C04411() {
        }

        public int compare(BaseShowResult baseShowResult, BaseShowResult baseShowResult2) {
            return baseShowResult2.show.next_episode.first_aired.compareTo(baseShowResult.show.next_episode.first_aired);
        }
    }

    /* renamed from: com.android.morpheustv.content.NextEpisodesList$3 */
    class C04433 implements OnItemSelectedListener {
        C04433() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (NextEpisodesList.this.shows != null && NextEpisodesList.this.shows.size() > null && i < NextEpisodesList.this.shows.size()) {
                BaseShowResult baseShowResult = (BaseShowResult) NextEpisodesList.this.shows.get(i);
                NextEpisodesList.this.currentSelection = i;
                NextEpisodesList.this.adapter.notifyDataSetChanged();
                NextEpisodesList.this.setOverview(baseShowResult);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            NextEpisodesList.this.setOverview(null);
        }
    }

    /* renamed from: com.android.morpheustv.content.NextEpisodesList$4 */
    class C04444 implements OnItemLongClickListener {
        C04444() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            NextEpisodesList.this.showEpisodesContextMenu((BaseShowResult) NextEpisodesList.this.adapter.getItem(i));
            return true;
        }
    }

    /* renamed from: com.android.morpheustv.content.NextEpisodesList$5 */
    class C04465 implements OnItemClickListener {
        C04465() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (NextEpisodesList.this.shows != null && NextEpisodesList.this.shows.size() > null && i < NextEpisodesList.this.shows.size()) {
                final BaseShowResult baseShowResult = (BaseShowResult) NextEpisodesList.this.shows.get(i);
                if (i == NextEpisodesList.this.currentSelection) {
                    NextEpisodesList.this.checkDownloadReadyForPlayback(baseShowResult.show.show.ids.imdb, baseShowResult.show.next_episode.season.intValue(), baseShowResult.show.next_episode.number.intValue(), new OnCheckDownloadListener() {
                        public void OnCheckFinished(Download download) {
                            String format = String.format("%s S%02dE%02d", new Object[]{baseShowResult.show.show.title, baseShowResult.show.next_episode.season, baseShowResult.show.next_episode.number});
                            Intent intent = new Intent(NextEpisodesList.this, SourceList.class);
                            intent.putExtra("show", true);
                            intent.putExtra("title", format);
                            intent.putExtra("episode_title", baseShowResult.show.next_episode.title);
                            intent.putExtra("year", baseShowResult.show.show.year);
                            intent.putExtra("imdb", baseShowResult.show.show.ids.imdb);
                            intent.putExtra("tmdb", baseShowResult.show.show.ids.tmdb);
                            intent.putExtra("season", baseShowResult.show.next_episode.season);
                            intent.putExtra("episode", baseShowResult.show.next_episode.number);
                            intent.putExtra("episodeId", baseShowResult.show.next_episode.ids.trakt);
                            intent.putExtra("poster", baseShowResult.images2.posterUrl);
                            intent.putExtra("backdrop", baseShowResult.images.backdropUrl);
                            intent.putExtra("overview", baseShowResult.show.next_episode.overview);
                            intent.putExtra("rating", baseShowResult.show.next_episode.rating);
                            if (download != null) {
                                intent.putExtra("isFromDownloads", true);
                                intent.putExtra("location", download.getFile());
                                intent.putExtra("filename", Utils.getFilenameFromUrl(download.getUrl(), format));
                            }
                            NextEpisodesList.this.startActivityForResult(intent, 5000);
                        }
                    });
                    return;
                }
                NextEpisodesList.this.currentSelection = i;
                NextEpisodesList.this.adapter.notifyDataSetChanged();
                NextEpisodesList.this.setOverview(baseShowResult);
            }
        }
    }

    class LoadPosterTask extends AsyncTask<Void, Void, Void> {
        BaseShowResult show = null;

        public LoadPosterTask(BaseShowResult baseShowResult) {
            this.show = baseShowResult;
        }

        protected Void doInBackground(Void... voidArr) {
            if (NextEpisodesList.this.isForeground != null) {
                this.show.images = Tmdb.getShowImages(this.show.show.show.ids.tmdb, "en,null");
                this.show.images2 = Tmdb.getEpisodeImages(this.show.show.show.ids.tmdb, this.show.show.next_episode.season.intValue(), this.show.show.next_episode.number.intValue(), "en,null");
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            try {
                this.show.images.isLoading = false;
                this.show.images2.isLoading = false;
                if (NextEpisodesList.this.isForeground != null) {
                    if (NextEpisodesList.this.adapter.getItem(NextEpisodesList.this.currentSelection) == this.show) {
                        NextEpisodesList.this.setOverview(this.show);
                    }
                    NextEpisodesList.this.adapter.notifyDataSetChanged();
                }
            } catch (Void voidR2) {
                voidR2.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_next_episodes_list);
        this.mBackdrop = (ImageView) findViewById(R.id.backdropImage);
        rotateLoading();
        initializeListView();
        setBreadcrumb(R.drawable.trakt, getString(R.string.episodes_next), null);
        setOverview(null);
        loadContent();
    }

    protected void onResume() {
        super.onResume();
        this.isForeground = true;
        loadContent();
    }

    protected void onStop() {
        super.onStop();
        this.isForeground = false;
    }

    private void showError(String str) {
        if (this.shows == null || this.shows.isEmpty()) {
            findViewById(R.id.error_notification).setVisibility(0);
            ((TextView) findViewById(R.id.error_message)).setText(str);
            findViewById(R.id.continue_button).setVisibility(8);
            findViewById(R.id.empty_list_view).setVisibility(8);
        }
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

    private void setOverview(BaseShowResult baseShowResult) {
        try {
            View findViewById = findViewById(R.id.rating_container);
            TextView textView = (TextView) findViewById(R.id.rating);
            this.mPoster = (ImageView) findViewById(R.id.show_poster);
            this.mEpisodeTitle = (TextView) findViewById(R.id.episode_name);
            TextView textView2 = (TextView) findViewById(R.id.movie_name);
            TextView textView3 = (TextView) findViewById(R.id.movie_synopsis);
            if (baseShowResult != null) {
                if (baseShowResult.show.next_episode.rating.doubleValue() > 0.0d) {
                    textView.setText(String.format("%.1f", new Object[]{baseShowResult.show.next_episode.rating}).replace(",", "."));
                    findViewById.setVisibility(0);
                } else {
                    findViewById.setVisibility(8);
                }
                this.mPoster.setVisibility(0);
                this.mEpisodeTitle.setVisibility(0);
                this.mEpisodeTitle.setText(baseShowResult.show.next_episode.title);
                if (baseShowResult.images == null || baseShowResult.images.backdropUrl == null || baseShowResult.images.backdropUrl.isEmpty() || baseShowResult.images.isLoading || this.currentBackdrop.equals(baseShowResult.images.backdropUrl)) {
                    this.currentBackdrop = "";
                    this.mBackdrop.setImageResource(0);
                } else {
                    this.currentBackdrop = baseShowResult.images.backdropUrl;
                    Glide.with((FragmentActivity) this).load(baseShowResult.images.backdropUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.mBackdrop);
                }
                if (baseShowResult.images2.posterUrl == null || baseShowResult.images2.posterUrl.isEmpty() || baseShowResult.images2.isLoading) {
                    this.mPoster.setImageResource(0);
                } else {
                    Glide.with((FragmentActivity) this).load(baseShowResult.images2.posterUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.mPoster);
                }
                if (baseShowResult.show.next_episode.overview == null || baseShowResult.show.next_episode.overview.isEmpty()) {
                    textView3.setText("");
                } else {
                    textView3.setText(baseShowResult.show.next_episode.overview);
                }
                textView2.setText(String.format("%s S%02dE%02d", new Object[]{baseShowResult.show.show.title, baseShowResult.show.next_episode.season, baseShowResult.show.next_episode.number}));
                findViewById(R.id.media_info_panel).setVisibility(0);
                return;
            }
            findViewById(R.id.media_info_panel).setVisibility(4);
        } catch (BaseShowResult baseShowResult2) {
            baseShowResult2.printStackTrace();
            findViewById(R.id.media_info_panel).setVisibility(4);
        }
    }

    private void loadContent() {
        if (!this.loading) {
            this.loading = true;
            if (Trakt.cache == null || Trakt.cache.watchedShows == null || Trakt.cache.watchedShows.size() <= 0) {
                showError(getString(R.string.no_watched_progress));
            } else {
                this.shows.clear();
                Iterator it = Trakt.cache.watchedShows.iterator();
                while (it.hasNext()) {
                    BaseShow baseShow = (BaseShow) it.next();
                    if (!(baseShow == null || baseShow.show == null || baseShow.next_episode == null || baseShow.next_episode.season == null || baseShow.next_episode.number == null || baseShow.next_episode.first_aired == null || !baseShow.next_episode.first_aired.isBefore(OffsetDateTime.now()))) {
                        this.shows.add(new BaseShowResult(baseShow));
                    }
                }
                Collections.sort(this.shows, new C04411());
                if (this.shows.size() > 0 && this.isForeground) {
                    this.currentSelection = 0;
                    loadImage((BaseShowResult) this.shows.get(this.currentSelection));
                    if (this.list != null) {
                        this.list.setSelection(this.currentSelection);
                        this.list.smoothScrollToPosition(this.currentSelection);
                    }
                    setOverview((BaseShowResult) this.shows.get(this.currentSelection));
                }
                it = this.shows.iterator();
                while (it.hasNext()) {
                    loadImage((BaseShowResult) it.next());
                }
                this.adapter.notifyDataSetChanged();
            }
            this.loading = false;
        }
    }

    private void initializeListView() {
        this.shows = new ArrayList();
        this.list = (ListView) findViewById(R.id.medialist);
        this.adapter = new ArrayAdapter<BaseShowResult>(this, R.layout.nextepisode_list_item, this.shows) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    try {
                        view = NextEpisodesList.this.getLayoutInflater().inflate(R.layout.nextepisode_list_item, null);
                    } catch (int i2) {
                        i2.printStackTrace();
                    }
                }
                BaseShowResult baseShowResult = (BaseShowResult) NextEpisodesList.this.shows.get(i2);
                ((TextView) view.findViewById(R.id.show_name)).setText(baseShowResult.show.show.title);
                ((TextView) view.findViewById(R.id.episode_title)).setText(baseShowResult.show.next_episode.title);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(NextEpisodesList.this.getString(R.string.season));
                stringBuilder.append(StringUtils.SPACE);
                stringBuilder.append(String.valueOf(baseShowResult.show.next_episode.season));
                stringBuilder.append(" - ");
                stringBuilder.append(NextEpisodesList.this.getString(R.string.episode));
                stringBuilder.append(StringUtils.SPACE);
                stringBuilder.append(String.valueOf(baseShowResult.show.next_episode.number));
                ((TextView) view.findViewById(R.id.episode_number)).setText(stringBuilder.toString());
                ImageView imageView = (ImageView) view.findViewById(R.id.play_button);
                if (NextEpisodesList.this.currentSelection == i2) {
                    view.setBackgroundResource(R.drawable.button_selector_fixed);
                    imageView.setVisibility(0);
                } else {
                    view.setBackgroundResource(0);
                    imageView.setVisibility(8);
                }
                ImageView imageView2 = (ImageView) view.findViewById(R.id.showPoster);
                if (baseShowResult.images == null || baseShowResult.images.posterUrl == null || baseShowResult.images.posterUrl.isEmpty() || baseShowResult.images.isLoading) {
                    imageView2.setImageResource(R.drawable.poster);
                    return view;
                }
                Glide.with(NextEpisodesList.this).load(baseShowResult.images.posterUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView2);
                return view;
            }
        };
        this.list.setAdapter(this.adapter);
        this.list.setEmptyView(findViewById(R.id.empty_list_view));
        this.list.setOnItemSelectedListener(new C04433());
        this.list.setOnItemLongClickListener(new C04444());
        this.list.setOnItemClickListener(new C04465());
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 82) {
            return super.onKeyUp(i, keyEvent);
        }
        if (!(this.list == 0 || this.list.getSelectedItem() == 0)) {
            showEpisodesContextMenu((BaseShowResult) this.list.getSelectedItem());
        }
        return true;
    }

    private void showEpisodesContextMenu(final BaseShowResult baseShowResult) {
        String string = getString(R.string.option_mark_watched);
        String string2 = getString(R.string.gotoSeason);
        String string3 = getString(R.string.option_download);
        String string4 = getString(R.string.autofetch_dialog_option_season);
        final CharSequence[] charSequenceArr = new CharSequence[]{string, string2, string3, string4};
        Builder builder = new Builder(this);
        builder.setTitle(String.format("%s S%02dE%02d", new Object[]{baseShowResult.show.show.title, baseShowResult.show.next_episode.season, baseShowResult.show.next_episode.number}));
        builder.setItems(charSequenceArr, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                final ProgressDialog progressDialog = new ProgressDialog(NextEpisodesList.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setTitle(charSequenceArr[i]);
                progressDialog.setMessage(NextEpisodesList.this.getString(R.string.please_wait_mark_watched));
                OnActionListener c04491 = new OnActionListener() {

                    /* renamed from: com.android.morpheustv.content.NextEpisodesList$6$1$1 */
                    class C04471 implements Runnable {
                        C04471() {
                        }

                        public void run() {
                            NextEpisodesList.this.loadContent();
                            progressDialog.dismiss();
                        }
                    }

                    public void onSuccess(Object obj) {
                        Trakt.syncShowCache(NextEpisodesList.this.getApplicationContext(), baseShowResult.show.show.ids.imdb);
                        NextEpisodesList.this.runOnUiThread(new C04471());
                    }

                    public void onFail(final int i) {
                        NextEpisodesList.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Context context = NextEpisodesList.this;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Operation failed with error ");
                                stringBuilder.append(String.valueOf(i));
                                Toast.makeText(context, stringBuilder.toString(), 0).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                };
                Intent intent;
                switch (i) {
                    case 0:
                        progressDialog.show();
                        Trakt.getInstance().markEpisodeWatched(baseShowResult.show.next_episode.ids, true, c04491);
                        return;
                    case 1:
                        intent = new Intent(NextEpisodesList.this, SeasonList.class);
                        intent.putExtra("title", baseShowResult.show.show.title);
                        intent.putExtra("year", baseShowResult.show.show.year);
                        intent.putExtra("imdb", baseShowResult.show.show.ids.imdb);
                        intent.putExtra("tmdb", baseShowResult.show.show.ids.tmdb);
                        intent.putExtra("overview", baseShowResult.show.show.overview);
                        intent.putExtra("backdrop", baseShowResult.images.backdropUrl);
                        intent.putExtra("redirectToSeason", baseShowResult.show.next_episode.season);
                        NextEpisodesList.this.startActivityForResult(intent, 5000);
                        return;
                    case 2:
                        NextEpisodesList.this.downloadBestSource(baseShowResult.show.show.title, baseShowResult.show.next_episode.title, baseShowResult.show.show.title, String.format("%s S%02dE%02d", new Object[]{baseShowResult.show.show.title, baseShowResult.show.next_episode.season, baseShowResult.show.next_episode.number}), baseShowResult.show.next_episode.overview, baseShowResult.show.next_episode.rating.doubleValue(), baseShowResult.show.show.year.intValue(), baseShowResult.show.show.ids.imdb, baseShowResult.show.show.ids.tmdb.intValue(), baseShowResult.show.next_episode.ids.trakt.intValue(), baseShowResult.show.next_episode.season.intValue(), baseShowResult.show.next_episode.number.intValue());
                        return;
                    case 3:
                        intent = new Intent(NextEpisodesList.this, SeasonList.class);
                        intent.putExtra("title", baseShowResult.show.show.title);
                        intent.putExtra("year", baseShowResult.show.show.year);
                        intent.putExtra("imdb", baseShowResult.show.show.ids.imdb);
                        intent.putExtra("tmdb", baseShowResult.show.show.ids.tmdb);
                        intent.putExtra("overview", baseShowResult.show.show.overview);
                        intent.putExtra("backdrop", baseShowResult.images.backdropUrl);
                        intent.putExtra("redirectToSeason", baseShowResult.show.next_episode.season);
                        intent.putExtra("downloadUnwatched", true);
                        NextEpisodesList.this.startActivityForResult(intent, 5000);
                        return;
                    default:
                        return;
                }
            }
        });
        builder.create().show();
    }

    private void loadImage(BaseShowResult baseShowResult) {
        if (baseShowResult.images != null && baseShowResult.images.posterUrl != null && baseShowResult.images.posterUrl.isEmpty() && !baseShowResult.images.isLoading && !baseShowResult.images2.isLoading) {
            new LoadPosterTask(baseShowResult).executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
            baseShowResult.images.isLoading = true;
            baseShowResult.images2.isLoading = true;
        }
    }
}
