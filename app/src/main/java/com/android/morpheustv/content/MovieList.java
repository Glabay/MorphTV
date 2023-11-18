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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
import com.android.morpheustv.BaseActivity.OnCheckDownloadListener;
import com.android.morpheustv.helpers.TaskManager;
import com.android.morpheustv.helpers.Tmdb;
import com.android.morpheustv.helpers.Trakt;
import com.android.morpheustv.helpers.Trakt.CompleteListener;
import com.android.morpheustv.helpers.Trakt.MovieResult;
import com.android.morpheustv.helpers.Trakt.OnActionListener;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.sources.SourceList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.actions.SearchIntents;
import com.noname.titan.R;
import com.tonyodev.fetch2.Download;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MovieList extends BaseActivity {
    public static final int CONTENT_COLLECTION = 3;
    public static final int CONTENT_POPULAR = 2;
    public static final int CONTENT_SEARCH = 4;
    public static final int CONTENT_TRENDING = 1;
    public static final int CONTENT_WATCHLIST = 5;
    private ArrayAdapter<MovieResult> adapter;
    private String currentBackdrop = "";
    private int currentContent = 1;
    private int currentPage = 1;
    private int currentSelection = 0;
    private boolean isForeground = true;
    GridView list;
    private boolean loading = false;
    private ImageView mBackdrop;
    private ArrayList<MovieResult> movies;
    private String searchQuery = "";

    /* renamed from: com.android.morpheustv.content.MovieList$3 */
    class C04223 implements OnScrollListener {
        public void onScrollStateChanged(AbsListView absListView, int i) {
        }

        C04223() {
        }

        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            if (MovieList.this.currentContent != 3 && MovieList.this.currentContent != 5 && i + i2 == i3 && i3 != 0) {
                MovieList.this.loadContent(MovieList.this.currentContent, MovieList.this.currentPage + 1);
            }
        }
    }

    /* renamed from: com.android.morpheustv.content.MovieList$4 */
    class C04234 implements OnItemLongClickListener {
        C04234() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (MovieList.this.movies != null && MovieList.this.movies.size() > null && i < MovieList.this.movies.size()) {
                MovieList.this.showContextMenu((MovieResult) MovieList.this.movies.get(i));
            }
            return true;
        }
    }

    /* renamed from: com.android.morpheustv.content.MovieList$5 */
    class C04245 implements OnItemSelectedListener {
        C04245() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (MovieList.this.movies != null && MovieList.this.movies.size() > null && i < MovieList.this.movies.size()) {
                MovieList.this.currentSelection = i;
                MovieResult movieResult = (MovieResult) MovieList.this.movies.get(i);
                MovieList.this.setOverview(movieResult.movie.overview);
                if (movieResult.images == null || movieResult.images.backdropUrl == null || movieResult.images.backdropUrl.isEmpty() != null || MovieList.this.currentBackdrop.equals(movieResult.images.backdropUrl) != null) {
                    MovieList.this.mBackdrop.setImageResource(null);
                    MovieList.this.currentBackdrop = "";
                } else {
                    MovieList.this.currentBackdrop = movieResult.images.backdropUrl;
                    Glide.with(MovieList.this).load(movieResult.images.backdropUrl).diskCacheStrategy((DiskCacheStrategy) DiskCacheStrategy.ALL).into(MovieList.this.mBackdrop);
                }
                MovieList.this.adapter.notifyDataSetChanged();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            MovieList.this.setOverview("");
        }
    }

    /* renamed from: com.android.morpheustv.content.MovieList$6 */
    class C04266 implements OnItemClickListener {
        C04266() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (MovieList.this.movies != null && MovieList.this.movies.size() > null && i < MovieList.this.movies.size()) {
                final MovieResult movieResult = (MovieResult) MovieList.this.movies.get(i);
                if (i == MovieList.this.currentSelection) {
                    MovieList.this.checkDownloadReadyForPlayback(movieResult.movie.ids.imdb, 0, 0, new OnCheckDownloadListener() {
                        public void OnCheckFinished(Download download) {
                            Intent intent = new Intent(MovieList.this, SourceList.class);
                            intent.putExtra("movie", true);
                            intent.putExtra("title", movieResult.movie.title);
                            intent.putExtra("year", movieResult.movie.year);
                            intent.putExtra("imdb", movieResult.movie.ids.imdb);
                            intent.putExtra("tmdb", movieResult.movie.ids.tmdb);
                            intent.putExtra("poster", movieResult.images.posterUrl);
                            intent.putExtra("backdrop", movieResult.images.backdropUrl);
                            intent.putExtra("overview", movieResult.movie.overview);
                            intent.putExtra("rating", movieResult.movie.rating);
                            if (download != null) {
                                intent.putExtra("isFromDownloads", true);
                                intent.putExtra("location", download.getFile());
                                intent.putExtra("filename", Utils.getFilenameFromUrl(download.getUrl(), movieResult.movie.title));
                            }
                            MovieList.this.startActivityForResult(intent, 5000);
                        }
                    });
                    return;
                }
                MovieList.this.currentSelection = i;
                MovieList.this.setOverview(movieResult.movie.overview);
                if (movieResult.images == null || movieResult.images.backdropUrl == null || movieResult.images.backdropUrl.isEmpty() != null || MovieList.this.currentBackdrop.equals(movieResult.images.backdropUrl) != null) {
                    MovieList.this.mBackdrop.setImageResource(0);
                    MovieList.this.currentBackdrop = "";
                } else {
                    MovieList.this.currentBackdrop = movieResult.images.backdropUrl;
                    Glide.with(MovieList.this).load(movieResult.images.backdropUrl).diskCacheStrategy((DiskCacheStrategy) DiskCacheStrategy.ALL).into(MovieList.this.mBackdrop);
                }
                MovieList.this.adapter.notifyDataSetChanged();
            }
        }
    }

    class LoadPosterTask extends AsyncTask<Void, Void, Void> {
        MovieResult movie = null;
        ImageView view = null;

        public LoadPosterTask(MovieResult movieResult, ImageView imageView) {
            this.view = imageView;
            this.movie = movieResult;
        }

        protected Void doInBackground(Void... voidArr) {
            if (MovieList.this.isForeground != null) {
                this.movie.images = Tmdb.getMovieImages(this.movie.movie.ids.tmdb, "en,null");
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            if (MovieList.this.isForeground != null) {
                MovieList.this.setImage(this.movie, this.view);
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_media_list);
        this.mBackdrop = (ImageView) findViewById(R.id.backdropImage);
        this.currentContent = getIntent().getIntExtra("content", 1);
        this.searchQuery = getIntent().getStringExtra(SearchIntents.EXTRA_QUERY);
        rotateLoading();
        initializeListView();
        if (this.currentContent == 2) {
            setBreadcrumb(R.drawable.highly_rated, getString(R.string.movies_popular), null);
        }
        if (this.currentContent == 1) {
            setBreadcrumb(R.drawable.featured, getString(R.string.movies_trending), null);
        }
        if (this.currentContent == 3) {
            setBreadcrumb(R.drawable.trakt, getString(R.string.movies_collection), null);
        }
        if (this.currentContent == 5) {
            setBreadcrumb(R.drawable.trakt, getString(R.string.movies_watchlist), null);
        }
        if (this.currentContent == 4) {
            setBreadcrumb(R.drawable.search, getString(R.string.search), this.searchQuery);
        }
        loadContent(this.currentContent, this.currentPage);
    }

    protected void onResume() {
        super.onResume();
        this.isForeground = true;
        if (this.adapter != null) {
            Trakt.updateWatchedMovieStatus(this.movies);
            this.adapter.notifyDataSetChanged();
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

    protected void onStop() {
        super.onStop();
        this.isForeground = false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 82) {
            return super.onKeyUp(i, keyEvent);
        }
        if (!(this.list == 0 || this.list.getSelectedItem() == 0)) {
            showContextMenu((MovieResult) this.list.getSelectedItem());
        }
        return true;
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

    private void loadContent(int i, final int i2) {
        if (!this.loading) {
            OnActionListener c04201 = new OnActionListener() {
                public void onSuccess(final Object obj) {
                    MovieList.this.runOnUiThread(new Runnable() {
                        public void run() {
                            MovieList.this.addItems((ArrayList) obj);
                            MovieList.this.currentPage = i2;
                            MovieList.this.loading = false;
                        }
                    });
                }

                public void onFail(final int i) {
                    MovieList.this.runOnUiThread(new Runnable() {

                        /* renamed from: com.android.morpheustv.content.MovieList$1$2$1 */
                        class C04181 implements CompleteListener {
                            C04181() {
                            }

                            public void OnSuccess() {
                                MovieList.this.loadContent(MovieList.this.currentContent, MovieList.this.currentPage);
                            }

                            public void OnFail() {
                                MovieList.this.finish();
                            }
                        }

                        public void run() {
                            MovieList.this.loading = false;
                            if (i == 401) {
                                Trakt.beginAuthFlow(MovieList.this, new C04181());
                            } else {
                                MovieList.this.showError(MovieList.this.getString(R.string.trakt_error));
                            }
                        }
                    });
                }
            };
            this.loading = true;
            if (i == 1) {
                Trakt.getInstance().getTrendingMovies(i2, c04201);
            }
            if (i == 2) {
                Trakt.getInstance().getPopularMovies(i2, c04201);
            }
            if (i == 3) {
                Trakt.getInstance().getMovieCollection(c04201);
            }
            if (i == 5) {
                Trakt.getInstance().getMovieWatchlist(c04201);
            }
            if (i == 4) {
                Trakt.getInstance().searchMovies(this.searchQuery, i2, c04201);
            }
        }
    }

    private void showError(String str) {
        if (this.movies == null || this.movies.isEmpty()) {
            findViewById(R.id.error_notification).setVisibility(0);
            ((TextView) findViewById(R.id.error_message)).setText(str);
            findViewById(R.id.continue_button).setVisibility(8);
            findViewById(R.id.empty_list_view).setVisibility(8);
        }
    }

    private void initializeListView() {
        this.movies = new ArrayList();
        this.list = (GridView) findViewById(R.id.medialist);
        this.adapter = new ArrayAdapter<MovieResult>(this, R.layout.movie_list_item, this.movies) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                try {
                    MovieResult movieResult = (MovieResult) MovieList.this.movies.get(i);
                    if (view == null) {
                        view = MovieList.this.getLayoutInflater().inflate(R.layout.movie_list_item, null);
                    }
                    ((TextView) view.findViewById(R.id.title)).setText(movieResult.movie.title);
                    ((TextView) view.findViewById(R.id.year)).setText(String.valueOf(movieResult.movie.year));
                    if (movieResult.watched) {
                        view.findViewById(R.id.watched).setVisibility(0);
                    } else {
                        view.findViewById(R.id.watched).setVisibility(4);
                    }
                    View findViewById = view.findViewById(R.id.rating_container);
                    if (movieResult.movie.rating != null) {
                        ((TextView) view.findViewById(R.id.rating)).setText(String.format("%.1f", new Object[]{movieResult.movie.rating}).replace(",", "."));
                        findViewById.setVisibility(0);
                    } else {
                        findViewById.setVisibility(4);
                    }
                    ImageView imageView = (ImageView) view.findViewById(R.id.poster);
                    imageView.setImageResource(R.drawable.poster);
                    MovieList.this.loadImage(movieResult, imageView);
                    if (MovieList.this.currentSelection == i) {
                        view.setBackgroundResource(R.drawable.button_selector_fixed);
                    } else {
                        view.setBackgroundResource(0);
                    }
                } catch (int i2) {
                    i2.printStackTrace();
                }
                return view;
            }
        };
        this.list.setAdapter(this.adapter);
        this.list.setEmptyView(findViewById(R.id.empty_list_view));
        this.list.setOnScrollListener(new C04223());
        this.list.setOnItemLongClickListener(new C04234());
        this.list.setOnItemSelectedListener(new C04245());
        this.list.setOnItemClickListener(new C04266());
    }

    private void showContextMenu(final MovieResult movieResult) {
        String string = getString(R.string.option_mark_watched);
        if (movieResult.watched) {
            string = getString(R.string.option_mark_unwatched);
        }
        String string2 = getString(R.string.option_add_collection);
        if (this.currentContent == 3) {
            string2 = getString(R.string.option_del_collection);
        }
        String string3 = getString(R.string.option_add_watchlist);
        if (this.currentContent == 5) {
            string3 = getString(R.string.option_del_watchlist);
        }
        String string4 = getString(R.string.autofetch_dialog_option);
        final CharSequence[] charSequenceArr = new CharSequence[]{string, string2, string3, string4};
        Builder builder = new Builder(this);
        builder.setTitle(movieResult.movie.title);
        builder.setItems(charSequenceArr, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                final int i2 = i;
                dialogInterface.dismiss();
                final ProgressDialog progressDialog = new ProgressDialog(MovieList.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setTitle(charSequenceArr[i2]);
                progressDialog.setMessage(MovieList.this.getString(R.string.please_wait_mark_watched));
                final OnActionListener c04281 = new OnActionListener() {

                    /* renamed from: com.android.morpheustv.content.MovieList$7$1$1 */
                    class C04271 implements Runnable {
                        C04271() {
                        }

                        public void run() {
                            MovieList.this.adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }

                    public void onSuccess(Object obj) {
                        Trakt.syncMovieCache(MovieList.this.getApplicationContext());
                        Trakt.updateWatchedMovieStatus(MovieList.this.movies);
                        MovieList.this.runOnUiThread(new C04271());
                    }

                    public void onFail(int i) {
                        Context context = MovieList.this;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Operation failed with error ");
                        stringBuilder.append(String.valueOf(i));
                        Toast.makeText(context, stringBuilder.toString(), 0).show();
                        progressDialog.dismiss();
                    }
                };
                OnActionListener c04322 = new OnActionListener() {

                    /* renamed from: com.android.morpheustv.content.MovieList$7$2$1 */
                    class C04291 implements Runnable {
                        C04291() {
                        }

                        public void run() {
                            MovieList.this.adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }

                    public void onSuccess(Object obj) {
                        Trakt.syncMovieCache(MovieList.this.getApplicationContext());
                        Trakt.updateWatchedMovieStatus(MovieList.this.movies);
                        MovieList.this.runOnUiThread(new C04291());
                    }

                    public void onFail(final int i) {
                        MovieList.this.runOnUiThread(new Runnable() {

                            /* renamed from: com.android.morpheustv.content.MovieList$7$2$2$1 */
                            class C04301 implements CompleteListener {
                                C04301() {
                                }

                                public void OnSuccess() {
                                    switch (i2) {
                                        case 0:
                                            movieResult.watched ^= true;
                                            Trakt.getInstance().markMovieWatched(movieResult.movie.ids, movieResult.watched, c04281);
                                            MovieList.this.adapter.notifyDataSetChanged();
                                            return;
                                        case 1:
                                            if (MovieList.this.currentContent == 3) {
                                                Trakt.getInstance().movieCollect(movieResult.movie.ids, false, c04281);
                                                MovieList.this.movies.remove(movieResult);
                                                MovieList.this.adapter.notifyDataSetChanged();
                                                return;
                                            }
                                            Trakt.getInstance().movieCollect(movieResult.movie.ids, true, c04281);
                                            return;
                                        case 2:
                                            if (MovieList.this.currentContent == 5) {
                                                Trakt.getInstance().movieAddWatchlist(movieResult.movie.ids, false, c04281);
                                                MovieList.this.movies.remove(movieResult);
                                                MovieList.this.adapter.notifyDataSetChanged();
                                                return;
                                            }
                                            Trakt.getInstance().movieAddWatchlist(movieResult.movie.ids, true, c04281);
                                            return;
                                        default:
                                            return;
                                    }
                                }

                                public void OnFail() {
                                    Context context = MovieList.this;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("Operation failed with error ");
                                    stringBuilder.append(String.valueOf(i));
                                    Toast.makeText(context, stringBuilder.toString(), 0).show();
                                    progressDialog.dismiss();
                                }
                            }

                            public void run() {
                                if (i == 401) {
                                    Trakt.beginAuthFlow(MovieList.this, new C04301());
                                    return;
                                }
                                Context context = MovieList.this;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Operation failed with error ");
                                stringBuilder.append(String.valueOf(i));
                                Toast.makeText(context, stringBuilder.toString(), 0).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                };
                switch (i2) {
                    case 0:
                        progressDialog.show();
                        movieResult.watched ^= true;
                        Trakt.getInstance().markMovieWatched(movieResult.movie.ids, movieResult.watched, c04322);
                        MovieList.this.adapter.notifyDataSetChanged();
                        return;
                    case 1:
                        progressDialog.show();
                        if (MovieList.this.currentContent == 3) {
                            Trakt.getInstance().movieCollect(movieResult.movie.ids, false, c04322);
                            MovieList.this.movies.remove(movieResult);
                            MovieList.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        Trakt.getInstance().movieCollect(movieResult.movie.ids, true, c04322);
                        return;
                    case 2:
                        progressDialog.show();
                        if (MovieList.this.currentContent == 5) {
                            Trakt.getInstance().movieAddWatchlist(movieResult.movie.ids, false, c04322);
                            MovieList.this.movies.remove(movieResult);
                            MovieList.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        Trakt.getInstance().movieAddWatchlist(movieResult.movie.ids, true, c04322);
                        return;
                    case 3:
                        MovieList.this.downloadBestSource(movieResult.movie.title, "", movieResult.movie.title, movieResult.movie.title, movieResult.movie.overview, movieResult.movie.rating.doubleValue(), movieResult.movie.year.intValue(), movieResult.movie.ids.imdb, movieResult.movie.ids.tmdb.intValue(), movieResult.movie.ids.trakt.intValue(), 0, 0);
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

    private void addItems(ArrayList<MovieResult> arrayList) {
        try {
            if (!(this.movies == null || arrayList == null || arrayList.size() <= 0)) {
                Collection arrayList2 = new ArrayList();
                arrayList = arrayList.iterator();
                while (arrayList.hasNext()) {
                    MovieResult movieResult = (MovieResult) arrayList.next();
                    Object obj = null;
                    Iterator it = this.movies.iterator();
                    while (it.hasNext()) {
                        MovieResult movieResult2 = (MovieResult) it.next();
                        if (movieResult.movie.ids != null && movieResult.movie.ids.imdb != null && movieResult.movie.ids.imdb.equals(movieResult2.movie.ids.imdb)) {
                            obj = 1;
                            break;
                        }
                    }
                    if (obj == null) {
                        arrayList2.add(movieResult);
                    }
                }
                this.movies.addAll(arrayList2);
                this.adapter.notifyDataSetChanged();
                this.list.requestFocus();
                if (this.movies.size() > null && this.currentSelection < this.movies.size()) {
                    setOverview(((MovieResult) this.movies.get(this.currentSelection)).movie.overview);
                }
            }
            if (this.movies != null && this.movies.isEmpty() != null) {
                showError(getString(R.string.no_content_in_list_trakt));
            }
        } catch (ArrayList<MovieResult> arrayList3) {
            arrayList3.printStackTrace();
        }
    }

    private void loadImage(MovieResult movieResult, ImageView imageView) {
        imageView.setTag(movieResult);
        if (movieResult.images == null || !movieResult.images.posterUrl.isEmpty() || movieResult.images.isLoading) {
            setImage(movieResult, imageView);
            return;
        }
        new LoadPosterTask(movieResult, imageView).executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        movieResult.images.isLoading = true;
    }

    private void setImage(MovieResult movieResult, ImageView imageView) {
        try {
            if (imageView.getTag() == movieResult && movieResult.images != null && movieResult.images.posterUrl != null && !movieResult.images.posterUrl.isEmpty()) {
                movieResult.images.isLoading = false;
                imageView.setTag(null);
                Glide.with((FragmentActivity) this).load(movieResult.images.posterUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder((int) R.drawable.poster).fitCenter().into(imageView);
                MovieResult movieResult2 = (MovieResult) this.adapter.getItem(this.currentSelection);
                if (movieResult2 != null && movieResult2 == movieResult && movieResult.images != null && movieResult.images.backdropUrl != null && movieResult.images.backdropUrl.isEmpty() == null && this.currentBackdrop.equals(movieResult.images.backdropUrl) == null) {
                    this.currentBackdrop = movieResult.images.backdropUrl;
                    Glide.with((FragmentActivity) this).load(movieResult.images.backdropUrl).diskCacheStrategy((DiskCacheStrategy) DiskCacheStrategy.ALL).into(this.mBackdrop);
                }
            }
        } catch (MovieResult movieResult3) {
            movieResult3.printStackTrace();
        }
    }
}
