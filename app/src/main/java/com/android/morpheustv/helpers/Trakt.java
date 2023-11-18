package com.android.morpheustv.helpers;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;
import com.android.morpheustv.helpers.Tmdb.ImageResult;
import com.android.morpheustv.settings.Settings;
import com.google.gson.Gson;
import com.noname.titan.R;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.BaseEpisode;
import com.uwetrottmann.trakt5.entities.BaseMovie;
import com.uwetrottmann.trakt5.entities.BaseSeason;
import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.Episode;
import com.uwetrottmann.trakt5.entities.EpisodeIds;
import com.uwetrottmann.trakt5.entities.Genre;
import com.uwetrottmann.trakt5.entities.Movie;
import com.uwetrottmann.trakt5.entities.MovieIds;
import com.uwetrottmann.trakt5.entities.SearchResult;
import com.uwetrottmann.trakt5.entities.Season;
import com.uwetrottmann.trakt5.entities.Show;
import com.uwetrottmann.trakt5.entities.ShowIds;
import com.uwetrottmann.trakt5.entities.SyncEpisode;
import com.uwetrottmann.trakt5.entities.SyncItems;
import com.uwetrottmann.trakt5.entities.SyncMovie;
import com.uwetrottmann.trakt5.entities.SyncResponse;
import com.uwetrottmann.trakt5.entities.SyncSeason;
import com.uwetrottmann.trakt5.entities.SyncShow;
import com.uwetrottmann.trakt5.entities.TrendingMovie;
import com.uwetrottmann.trakt5.entities.TrendingShow;
import com.uwetrottmann.trakt5.entities.UserSlug;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.Type;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.threeten.bp.OffsetDateTime;

public class Trakt {
    private static final int RESULT_FAIL = 0;
    private static final int RESULT_OK = 1;
    private static final String apiKey = "facaff64d4c609e5cee8140d5a27158d74fa50c50e4749f65d8f578f832d2d39";
    public static Cache cache = null;
    private static final String clientSecret = "6b224cbeb7ad32967e1cd5b0965711c2a4ce2c3e2e7024d814a5e166ee0cdf1c";
    public static boolean isAuthorizing = false;
    private static Trakt mInstance = null;
    private static final String redirectUri = "urn:ietf:wg:oauth:2.0:oob";
    private static boolean shouldForceSync;
    private static HashSet<TraktSyncListener> syncListeners;
    private static Thread syncThread;
    public static DeviceTokenResponse userToken;
    private TraktV2 trakt = new TraktV2(apiKey, clientSecret, redirectUri);

    public interface TraktSyncListener {
        void onTraktSyncComplete(boolean z);

        void onTraktSyncProgress(String str);

        void onTraktSyncStart();
    }

    public interface OnActionListener {
        void onFail(int i);

        void onSuccess(Object obj);
    }

    public interface CompleteListener {
        void OnFail();

        void OnSuccess();
    }

    public interface AuthorizeListener {
        void onAuthorizationSuccess(DeviceTokenResponse deviceTokenResponse);

        void onCodeReceived(DeviceCodeResponse deviceCodeResponse);

        void onFail();
    }

    public static class BaseShowResult {
        public ImageResult images = new ImageResult("", "");
        public ImageResult images2 = new ImageResult("", "");
        public BaseShow show;
        public boolean watched = false;

        public BaseShowResult(BaseShow baseShow) {
            this.show = baseShow;
        }
    }

    public static class Cache {
        public final CopyOnWriteArrayList<BaseMovie> watchedMovies = new CopyOnWriteArrayList();
        public final CopyOnWriteArrayList<BaseShow> watchedShows = new CopyOnWriteArrayList();

        public void save(Context context) {
            Settings.saveObject(context, "traktCache", this);
        }

        public static Cache load(Context context) {
            return (Cache) Settings.loadObject(context, "traktCache", Cache.class);
        }
    }

    public static class DeviceCodeRequest {
        public String client_id = Trakt.apiKey;
        public String client_secret = Trakt.clientSecret;
        public String code = "";
    }

    public static class DeviceCodeResponse {
        public String device_code = "";
        public int expires_in = 0;
        public int interval = 0;
        public String user_code = "";
        public String verification_url = "";
    }

    public static class DeviceTokenResponse {
        public String access_token = "";
        public long created_at = 0;
        public long expires_in = 0;
        public String refresh_token = "";
        public String scope = "";
        public String token_type = "";
    }

    public static class EpisodeResult {
        public Episode episode;
        public ImageResult images = new ImageResult("", "");
        public boolean watched = false;

        public EpisodeResult(Episode episode) {
            this.episode = episode;
        }
    }

    public static class MovieResult {
        public ImageResult images = new ImageResult("", "");
        public Movie movie;
        public boolean watched = false;

        public MovieResult(Movie movie) {
            this.movie = movie;
        }
    }

    public static class SeasonResult {
        public List<EpisodeResult> episodes;
        public ImageResult images = new ImageResult("", "");
        public Season season;
        public boolean watched = false;

        public SeasonResult(Season season) {
            this.season = season;
            this.episodes = new ArrayList();
        }
    }

    public static class ShowResult {
        public ImageResult images = new ImageResult("", "");
        public Show show;
        public boolean watched = false;

        public ShowResult(Show show) {
            this.show = show;
        }
    }

    private Trakt() {
    }

    public static Trakt getInstance() {
        if (mInstance == null) {
            mInstance = new Trakt();
        }
        if (userToken != null) {
            mInstance.trakt.accessToken(userToken.access_token);
        }
        return mInstance;
    }

    public static void addSyncListener(TraktSyncListener traktSyncListener) {
        if (syncListeners == null) {
            syncListeners = new HashSet();
        }
        syncListeners.add(traktSyncListener);
    }

    public static void removeListener(TraktSyncListener traktSyncListener) {
        if (syncListeners != null) {
            syncListeners.remove(traktSyncListener);
        }
    }

    public static void clearListeners() {
        if (syncListeners != null) {
            syncListeners.clear();
        }
    }

    public Response Post(String str, String str2) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(TraktV2.API_URL);
            stringBuilder.append(str);
            return Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).requestBody(str2).header("Content-Type", TraktV2.CONTENT_TYPE_JSON).header(TraktV2.HEADER_TRAKT_API_KEY, apiKey).header(TraktV2.HEADER_TRAKT_API_VERSION, TraktV2.API_VERSION).data("dummy", "").method(Method.POST).execute();
        } catch (String str3) {
            str3.printStackTrace();
            return null;
        }
    }

    public static void beginAuthFlow(final Activity activity, final CompleteListener completeListener) {
        final AlertDialog create = new Builder(activity).create();
        create.setCancelable(false);
        create.setTitle(activity.getString(R.string.authorize_trakt_title));
        getInstance().AuthorizeDevice(new AuthorizeListener() {

            /* renamed from: com.android.morpheustv.helpers.Trakt$1$3 */
            class C05023 implements Runnable {
                C05023() {
                }

                public void run() {
                    Settings.saveObject(activity, "trakt_token", null);
                    completeListener.OnFail();
                }
            }

            public void onCodeReceived(final DeviceCodeResponse deviceCodeResponse) {
                activity.runOnUiThread(new Runnable() {

                    /* renamed from: com.android.morpheustv.helpers.Trakt$1$1$1 */
                    class C04991 implements OnClickListener {
                        C04991() {
                        }

                        public void onClick(DialogInterface dialogInterface, int i) {
                            Trakt.isAuthorizing = false;
                            dialogInterface.dismiss();
                            completeListener.OnFail();
                        }
                    }

                    public void run() {
                        if (create != null) {
                            try {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(activity.getString(R.string.authorize_trakt_msg1));
                                stringBuilder.append(" <a href=\"");
                                stringBuilder.append(deviceCodeResponse.verification_url);
                                stringBuilder.append("\">");
                                stringBuilder.append(deviceCodeResponse.verification_url);
                                stringBuilder.append("</a> ");
                                stringBuilder.append(activity.getString(R.string.authorize_trakt_msg2));
                                stringBuilder.append(" <b>");
                                stringBuilder.append(deviceCodeResponse.user_code);
                                stringBuilder.append("</b>");
                                create.setMessage(Html.fromHtml(stringBuilder.toString()));
                                create.setButton(-2, activity.getString(R.string.cancel), new C04991());
                                create.show();
                                ((TextView) create.findViewById(16908299)).setMovementMethod(LinkMovementMethod.getInstance());
                                ((ClipboardManager) activity.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("code", deviceCodeResponse.user_code));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Trakt.isAuthorizing = false;
                            }
                        }
                    }
                });
            }

            public void onAuthorizationSuccess(final DeviceTokenResponse deviceTokenResponse) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (create != null) {
                            create.dismiss();
                        }
                        completeListener.OnSuccess();
                        Settings.saveObject(activity, "trakt_token", deviceTokenResponse);
                        Trakt.forceSync();
                    }
                });
            }

            public void onFail() {
                activity.runOnUiThread(new C05023());
            }
        });
    }

    public void AuthorizeDevice(final AuthorizeListener authorizeListener) {
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Trakt.isAuthorizing = true;
                        Gson gson = new Gson();
                        DeviceCodeRequest deviceCodeRequest = new DeviceCodeRequest();
                        deviceCodeRequest.code = "";
                        Response Post = Trakt.this.Post("oauth/device/code", gson.toJson(deviceCodeRequest));
                        if (Post.statusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                            Response Post2;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Got new code:");
                            stringBuilder.append(Post.body());
                            Log.d("Trakt", stringBuilder.toString());
                            DeviceCodeResponse deviceCodeResponse = (DeviceCodeResponse) gson.fromJson(Post.body(), DeviceCodeResponse.class);
                            authorizeListener.onCodeReceived(deviceCodeResponse);
                            int i = 0;
                            do {
                                try {
                                    if (i >= deviceCodeResponse.expires_in || !Trakt.isAuthorizing) {
                                        authorizeListener.onFail();
                                    } else {
                                        Thread.sleep((long) (deviceCodeResponse.interval * 1000));
                                        i += deviceCodeResponse.interval;
                                        Log.d("Trakt", "Checking for code...");
                                        deviceCodeRequest.code = deviceCodeResponse.device_code;
                                        Post2 = Trakt.this.Post("oauth/device/token", gson.toJson(deviceCodeRequest));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    authorizeListener.onFail();
                                }
                            } while (Post2.statusCode() != Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                            DeviceTokenResponse deviceTokenResponse = (DeviceTokenResponse) gson.fromJson(Post2.body(), DeviceTokenResponse.class);
                            Trakt.userToken = deviceTokenResponse;
                            authorizeListener.onAuthorizationSuccess(deviceTokenResponse);
                            return;
                        }
                        authorizeListener.onFail();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        authorizeListener.onFail();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            authorizeListener.onFail();
        }
    }

    public void getShowCollection(final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        System.out.println("Loading Show collection...");
                        voidArr = Trakt.this.trakt.sync().collectionShows(Extended.FULL).execute();
                        if (voidArr.isSuccessful()) {
                            List<BaseShow> list = (List) voidArr.body();
                            List arrayList = new ArrayList();
                            for (BaseShow baseShow : list) {
                                arrayList.add(new ShowResult(baseShow.show));
                            }
                            Trakt.updateWatchedStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(voidArr.code());
                        }
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (Exception e) {
            e.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getShowsWatchlist(final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        System.out.println("Loading Show watchlist...");
                        voidArr = Trakt.this.trakt.sync().watchlistShows(Extended.FULL).execute();
                        if (voidArr.isSuccessful()) {
                            List<BaseShow> list = (List) voidArr.body();
                            List arrayList = new ArrayList();
                            for (BaseShow baseShow : list) {
                                arrayList.add(new ShowResult(baseShow.show));
                            }
                            Trakt.updateWatchedStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(voidArr.code());
                        }
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (Exception e) {
            e.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getMovieCollection(final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        System.out.println("Loading Movie collection...");
                        voidArr = Trakt.this.trakt.sync().collectionMovies(Extended.FULL).execute();
                        if (voidArr.isSuccessful()) {
                            List<BaseMovie> list = (List) voidArr.body();
                            List arrayList = new ArrayList();
                            for (BaseMovie baseMovie : list) {
                                arrayList.add(new MovieResult(baseMovie.movie));
                            }
                            Trakt.updateWatchedMovieStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(voidArr.code());
                        }
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (Exception e) {
            e.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getMovieWatchlist(final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        System.out.println("Loading Movie collection...");
                        voidArr = Trakt.this.trakt.sync().watchlistMovies(Extended.FULL).execute();
                        if (voidArr.isSuccessful()) {
                            List<BaseMovie> list = (List) voidArr.body();
                            List arrayList = new ArrayList();
                            for (BaseMovie baseMovie : list) {
                                arrayList.add(new MovieResult(baseMovie.movie));
                            }
                            Trakt.updateWatchedMovieStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(voidArr.code());
                        }
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (Exception e) {
            e.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getTrendingMovies(final int i, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        PrintStream printStream = System.out;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Loading Trending movies page ");
                        stringBuilder.append(String.valueOf(i));
                        printStream.println(stringBuilder.toString());
                        retrofit2.Response execute = Trakt.this.trakt.movies().trending(Integer.valueOf(i), null, Extended.FULL).execute();
                        if (execute.isSuccessful()) {
                            List<TrendingMovie> list = (List) execute.body();
                            List arrayList = new ArrayList();
                            for (TrendingMovie trendingMovie : list) {
                                arrayList.add(new MovieResult(trendingMovie.movie));
                            }
                            Trakt.updateWatchedMovieStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(execute.code());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (int i2) {
            i2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getTrendingShows(final int i, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        retrofit2.Response execute = Trakt.this.trakt.shows().trending(Integer.valueOf(i), null, Extended.FULL).execute();
                        if (execute.isSuccessful()) {
                            List<TrendingShow> list = (List) execute.body();
                            List arrayList = new ArrayList();
                            for (TrendingShow trendingShow : list) {
                                arrayList.add(new ShowResult(trendingShow.show));
                            }
                            Trakt.updateWatchedStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(execute.code());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (int i2) {
            i2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getPopularMovies(final int i, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        retrofit2.Response execute = Trakt.this.trakt.movies().popular(Integer.valueOf(i), null, Extended.FULL).execute();
                        if (execute.isSuccessful()) {
                            List<Movie> list = (List) execute.body();
                            List arrayList = new ArrayList();
                            for (Movie movieResult : list) {
                                arrayList.add(new MovieResult(movieResult));
                            }
                            Trakt.updateWatchedMovieStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(execute.code());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (int i2) {
            i2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getPopularShows(final int i, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        retrofit2.Response execute = Trakt.this.trakt.shows().popular(Integer.valueOf(i), null, Extended.FULL).execute();
                        if (execute.isSuccessful()) {
                            List<Show> list = (List) execute.body();
                            List arrayList = new ArrayList();
                            for (Show showResult : list) {
                                arrayList.add(new ShowResult(showResult));
                            }
                            Trakt.updateWatchedStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(execute.code());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (int i2) {
            i2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getMovieGenres(final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = Trakt.this.trakt.genres().movies().execute();
                        if (voidArr.isSuccessful()) {
                            List<Genre> list = (List) voidArr.body();
                            ArrayList arrayList = new ArrayList();
                            for (Genre add : list) {
                                arrayList.add(add);
                            }
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(voidArr.code());
                        }
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (Exception e) {
            e.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getShowsGenres(final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = Trakt.this.trakt.genres().shows().execute();
                        if (voidArr.isSuccessful()) {
                            List<Genre> list = (List) voidArr.body();
                            ArrayList arrayList = new ArrayList();
                            for (Genre add : list) {
                                arrayList.add(add);
                            }
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(voidArr.code());
                        }
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (Exception e) {
            e.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void getSeasonsShow(final String str, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        retrofit2.Response execute = Trakt.this.trakt.seasons().summary(str, Extended.FULLEPISODES).execute();
                        if (execute.isSuccessful()) {
                            List<Season> list = (List) execute.body();
                            List arrayList = new ArrayList();
                            for (Season season : list) {
                                if (season.aired_episodes.intValue() > 0 && season.number.intValue() > 0) {
                                    SeasonResult seasonResult = new SeasonResult(season);
                                    arrayList.add(seasonResult);
                                    Trakt.updateWatchedStatus(str, arrayList);
                                    seasonResult.episodes = new ArrayList();
                                    for (Episode episode : season.episodes) {
                                        if (episode.first_aired != null && episode.first_aired.isBefore(OffsetDateTime.now())) {
                                            EpisodeResult episodeResult = new EpisodeResult(episode);
                                            episodeResult.watched = false;
                                            if (episode.number_abs != null) {
                                                if (episode.number_abs.intValue() == 0) {
                                                    episodeResult.watched = false;
                                                }
                                                if (episode.number_abs.intValue() == 1) {
                                                    episodeResult.watched = true;
                                                }
                                            }
                                            seasonResult.episodes.add(episodeResult);
                                        }
                                    }
                                }
                            }
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(execute.code());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (String str2) {
            str2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void searchMovies(final String str, final int i, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = Trakt.this.trakt.search().textQuery(Type.MOVIE, str, null, null, null, null, null, null, null, Integer.valueOf(i), null).execute();
                        if (voidArr.isSuccessful()) {
                            List<SearchResult> list = (List) voidArr.body();
                            List arrayList = new ArrayList();
                            for (SearchResult searchResult : list) {
                                if (!(searchResult.movie == null || searchResult.movie.ids == null || searchResult.movie.ids.tmdb == null)) {
                                    arrayList.add(new MovieResult(searchResult.movie));
                                }
                            }
                            Trakt.updateWatchedMovieStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(voidArr.code());
                        }
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (String str2) {
            str2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void searchShows(final String str, final int i, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = Trakt.this.trakt.search().textQuery(Type.SHOW, str, null, null, null, null, null, null, null, Integer.valueOf(i), null).execute();
                        if (voidArr.isSuccessful()) {
                            List<SearchResult> list = (List) voidArr.body();
                            List arrayList = new ArrayList();
                            for (SearchResult searchResult : list) {
                                if (!(searchResult.show == null || searchResult.show.ids == null || searchResult.show.ids.tmdb == null)) {
                                    arrayList.add(new ShowResult(searchResult.show));
                                }
                            }
                            Trakt.updateWatchedStatus(arrayList);
                            onActionListener.onSuccess(arrayList);
                        } else {
                            onActionListener.onFail(voidArr.code());
                        }
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                    return null;
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (String str2) {
            str2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    private retrofit2.Response<SyncResponse> syncCollection(SyncItems syncItems, boolean z) {
        if (!z) {
            return this.trakt.sync().deleteItemsFromCollection(syncItems).execute();
        }
        try {
            return this.trakt.sync().addItemsToCollection(syncItems).execute();
        } catch (SyncItems syncItems2) {
            syncItems2.printStackTrace();
            return null;
        }
    }

    private retrofit2.Response<SyncResponse> syncWatchlist(SyncItems syncItems, boolean z) {
        if (!z) {
            return this.trakt.sync().deleteItemsFromWatchlist(syncItems).execute();
        }
        try {
            return this.trakt.sync().addItemsToWatchlist(syncItems).execute();
        } catch (SyncItems syncItems2) {
            syncItems2.printStackTrace();
            return null;
        }
    }

    public void movieCollect(final MovieIds movieIds, final boolean z, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = new SyncItems();
                        SyncMovie syncMovie = new SyncMovie();
                        syncMovie.ids = movieIds;
                        syncMovie.collectedAt(OffsetDateTime.now());
                        voidArr.movies(syncMovie);
                        voidArr = Trakt.this.syncCollection(voidArr, z);
                        if (voidArr == null || !voidArr.isSuccessful()) {
                            onActionListener.onFail(voidArr.code());
                            return null;
                        }
                        onActionListener.onSuccess(voidArr.body());
                        return null;
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (MovieIds movieIds2) {
            movieIds2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void movieAddWatchlist(final MovieIds movieIds, final boolean z, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = new SyncItems();
                        SyncMovie syncMovie = new SyncMovie();
                        syncMovie.ids = movieIds;
                        syncMovie.collectedAt(OffsetDateTime.now());
                        voidArr.movies(syncMovie);
                        voidArr = Trakt.this.syncWatchlist(voidArr, z);
                        if (voidArr == null || !voidArr.isSuccessful()) {
                            onActionListener.onFail(voidArr.code());
                            return null;
                        }
                        onActionListener.onSuccess(voidArr.body());
                        return null;
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (MovieIds movieIds2) {
            movieIds2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void showCollect(final ShowIds showIds, final boolean z, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = new SyncItems();
                        SyncShow syncShow = new SyncShow();
                        syncShow.ids = showIds;
                        syncShow.collectedAt(OffsetDateTime.now());
                        voidArr.shows(syncShow);
                        voidArr = Trakt.this.syncCollection(voidArr, z);
                        if (voidArr == null || !voidArr.isSuccessful()) {
                            onActionListener.onFail(voidArr.code());
                            return null;
                        }
                        onActionListener.onSuccess(voidArr.body());
                        return null;
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (ShowIds showIds2) {
            showIds2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void showAddWatchlist(final ShowIds showIds, final boolean z, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = new SyncItems();
                        SyncShow syncShow = new SyncShow();
                        syncShow.ids = showIds;
                        syncShow.collectedAt(OffsetDateTime.now());
                        voidArr.shows(syncShow);
                        voidArr = Trakt.this.syncWatchlist(voidArr, z);
                        if (voidArr == null || !voidArr.isSuccessful()) {
                            onActionListener.onFail(voidArr.code());
                            return null;
                        }
                        onActionListener.onSuccess(voidArr.body());
                        return null;
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (ShowIds showIds2) {
            showIds2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    private retrofit2.Response<SyncResponse> syncWatchedHistory(SyncItems syncItems, boolean z) {
        if (!z) {
            return this.trakt.sync().deleteItemsFromWatchedHistory(syncItems).execute();
        }
        try {
            return this.trakt.sync().addItemsToWatchedHistory(syncItems).execute();
        } catch (SyncItems syncItems2) {
            syncItems2.printStackTrace();
            return null;
        }
    }

    public void markEpisodeWatched(final EpisodeIds episodeIds, final boolean z, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = new SyncItems();
                        SyncEpisode syncEpisode = new SyncEpisode();
                        syncEpisode.watchedAt(OffsetDateTime.now());
                        syncEpisode.id(episodeIds);
                        voidArr.episodes(syncEpisode);
                        voidArr = Trakt.this.syncWatchedHistory(voidArr, z);
                        if (voidArr == null || !voidArr.isSuccessful()) {
                            onActionListener.onFail(voidArr.code());
                            return null;
                        }
                        onActionListener.onSuccess(voidArr.body());
                        return null;
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (EpisodeIds episodeIds2) {
            episodeIds2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void markSeasonWatched(String str, int i, boolean z, OnActionListener onActionListener) {
        try {
            final String str2 = str;
            final int i2 = i;
            final boolean z2 = z;
            final OnActionListener onActionListener2 = onActionListener;
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = new SyncItems();
                        SyncShow syncShow = new SyncShow();
                        syncShow.ids = new ShowIds();
                        syncShow.ids.imdb = str2;
                        SyncSeason syncSeason = new SyncSeason();
                        syncSeason.watchedAt(OffsetDateTime.now());
                        syncSeason.number = Integer.valueOf(i2);
                        syncShow.seasons(syncSeason);
                        voidArr.shows(syncShow);
                        voidArr = Trakt.this.syncWatchedHistory(voidArr, z2);
                        if (voidArr == null || !voidArr.isSuccessful()) {
                            onActionListener2.onFail(voidArr.code());
                            return null;
                        }
                        onActionListener2.onSuccess(voidArr.body());
                        return null;
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener2.onFail(0);
                    }
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (String str3) {
            str3.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void markShowWatched(final ShowIds showIds, final boolean z, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = new SyncItems();
                        SyncShow syncShow = new SyncShow();
                        syncShow.ids = showIds;
                        syncShow.watchedAt(OffsetDateTime.now());
                        voidArr.shows(syncShow);
                        voidArr = Trakt.this.syncWatchedHistory(voidArr, z);
                        if (voidArr == null || !voidArr.isSuccessful()) {
                            onActionListener.onFail(voidArr.code());
                            return null;
                        }
                        onActionListener.onSuccess(voidArr.body());
                        return null;
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (ShowIds showIds2) {
            showIds2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public void markMovieWatched(final MovieIds movieIds, final boolean z, final OnActionListener onActionListener) {
        try {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    try {
                        voidArr = new SyncItems();
                        SyncMovie syncMovie = new SyncMovie();
                        syncMovie.ids = movieIds;
                        syncMovie.watchedAt(OffsetDateTime.now());
                        voidArr.movies(syncMovie);
                        voidArr = Trakt.this.syncWatchedHistory(voidArr, z);
                        if (voidArr == null || !voidArr.isSuccessful()) {
                            onActionListener.onFail(voidArr.code());
                            return null;
                        }
                        onActionListener.onSuccess(voidArr.body());
                        return null;
                    } catch (Void[] voidArr2) {
                        voidArr2.printStackTrace();
                        onActionListener.onFail(0);
                    }
                }
            }.executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        } catch (MovieIds movieIds2) {
            movieIds2.printStackTrace();
            onActionListener.onFail(0);
        }
    }

    public static boolean updateWatchedMovieStatus(List<MovieResult> list) {
        try {
            if (!(cache == null || cache.watchedMovies == null)) {
                Iterator it = cache.watchedMovies.iterator();
                while (it.hasNext()) {
                    BaseMovie baseMovie = (BaseMovie) it.next();
                    for (MovieResult movieResult : list) {
                        if (baseMovie.movie.ids.imdb.equals(movieResult.movie.ids.imdb)) {
                            movieResult.watched = true;
                            break;
                        }
                    }
                }
            }
        } catch (List<MovieResult> list2) {
            list2.printStackTrace();
        }
        return null;
    }

    public static boolean updateWatchedStatus(List<ShowResult> list) {
        try {
            if (!(cache == null || cache.watchedShows == null)) {
                Iterator it = cache.watchedShows.iterator();
                while (it.hasNext()) {
                    BaseShow baseShow = (BaseShow) it.next();
                    for (ShowResult showResult : list) {
                        if (baseShow.show.ids.imdb.equals(showResult.show.ids.imdb)) {
                            if (baseShow.completed == null || baseShow.aired == null) {
                                showResult.watched = false;
                            } else {
                                showResult.watched = baseShow.completed.intValue() >= baseShow.aired.intValue();
                            }
                        }
                    }
                }
            }
        } catch (List<ShowResult> list2) {
            list2.printStackTrace();
        }
        return false;
    }

    public static boolean updateWatchedStatus(BaseShowResult baseShowResult) {
        try {
            if (!(cache == null || cache.watchedShows == null)) {
                Iterator it = cache.watchedShows.iterator();
                while (it.hasNext()) {
                    BaseShow baseShow = (BaseShow) it.next();
                    if (baseShow.show.ids.imdb.equals(baseShowResult.show.show.ids.imdb)) {
                        if (baseShow.completed == null || baseShow.aired == null) {
                            baseShowResult.watched = false;
                        } else {
                            baseShowResult.watched = baseShow.completed.intValue() >= baseShow.aired.intValue();
                        }
                    }
                }
            }
        } catch (BaseShowResult baseShowResult2) {
            baseShowResult2.printStackTrace();
        }
        return false;
    }

    public static boolean updateWatchedStatus(String str, List<SeasonResult> list) {
        if (list != null) {
            try {
                Iterator it;
                for (SeasonResult seasonResult : list) {
                    for (Episode episode : seasonResult.season.episodes) {
                        episode.number_abs = Integer.valueOf(0);
                    }
                }
                if (cache != null) {
                    it = cache.watchedShows.iterator();
                    while (it.hasNext()) {
                        BaseShow baseShow = (BaseShow) it.next();
                        if (baseShow.show.ids.imdb.equals(str)) {
                            for (SeasonResult seasonResult2 : list) {
                                for (BaseSeason baseSeason : baseShow.seasons) {
                                    if (!(!baseSeason.number.equals(seasonResult2.season.number) || baseSeason.completed == null || baseSeason.aired == null)) {
                                        seasonResult2.watched = baseSeason.completed.intValue() >= baseSeason.aired.intValue();
                                        for (Episode episode2 : seasonResult2.season.episodes) {
                                            episode2.number_abs = Integer.valueOf(0);
                                            for (BaseEpisode baseEpisode : baseSeason.episodes) {
                                                if (episode2.number.equals(baseEpisode.number)) {
                                                    if (baseEpisode.completed.booleanValue()) {
                                                        episode2.number_abs = Integer.valueOf(1);
                                                    } else {
                                                        episode2.number_abs = Integer.valueOf(0);
                                                    }
                                                    if (seasonResult2.episodes != null) {
                                                        for (EpisodeResult episodeResult : seasonResult2.episodes) {
                                                            if (baseEpisode.number.equals(episodeResult.episode.number)) {
                                                                episodeResult.watched = baseEpisode.completed.booleanValue();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (String str2) {
                str2.printStackTrace();
            }
        }
        return false;
    }

    public static boolean hasNextEpisode(String str, int i, int i2) {
        try {
            if (cache == null) {
                return false;
            }
            Iterator it = cache.watchedShows.iterator();
            while (it.hasNext()) {
                BaseShow baseShow = (BaseShow) it.next();
                if (baseShow.show.ids.imdb.equals(str)) {
                    for (BaseSeason baseSeason : baseShow.seasons) {
                        if (baseSeason.number.intValue() == i) {
                            for (BaseEpisode baseEpisode : baseSeason.episodes) {
                                if (baseEpisode.number.intValue() == i2 + 1) {
                                    return true;
                                }
                            }
                            return false;
                        }
                    }
                    return false;
                }
            }
            return false;
        } catch (String str2) {
            str2.printStackTrace();
            return false;
        }
    }

    public static boolean isEpisodeWatched(String str, int i, int i2) {
        try {
            if (cache == null) {
                return false;
            }
            Iterator it = cache.watchedShows.iterator();
            while (it.hasNext()) {
                BaseShow baseShow = (BaseShow) it.next();
                if (baseShow.show.ids.imdb.equals(str)) {
                    for (BaseSeason baseSeason : baseShow.seasons) {
                        if (baseSeason.number.intValue() == i) {
                            for (BaseEpisode baseEpisode : baseSeason.episodes) {
                                if (baseEpisode.number.intValue() == i2) {
                                    str = baseEpisode.completed.booleanValue();
                                    break;
                                }
                            }
                            str = null;
                            return str;
                        }
                    }
                    return false;
                }
            }
            return false;
        } catch (String str2) {
            str2.printStackTrace();
            return false;
        }
    }

    public static boolean isMovieWatched(String str) {
        try {
            if (cache == null) {
                return false;
            }
            Iterator it = cache.watchedMovies.iterator();
            while (it.hasNext()) {
                if (((BaseMovie) it.next()).movie.ids.imdb.equals(str)) {
                    return true;
                }
            }
            return false;
        } catch (String str2) {
            str2.printStackTrace();
            return false;
        }
    }

    public static void syncMovieCache(Context context) {
        try {
            if (!(Thread.currentThread().isInterrupted() || cache == null)) {
                retrofit2.Response execute = getInstance().trakt.users().watchedMovies(UserSlug.ME, Extended.FULL).execute();
                if (execute.isSuccessful()) {
                    Log.d("TRAKT", "Sync watched movies...");
                    synchronized (cache.watchedMovies) {
                        List list = (List) execute.body();
                        cache.watchedMovies.clear();
                        if (list != null) {
                            cache.watchedMovies.addAll(list);
                        }
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(context.getString(R.string.syncing_movies));
                stringBuilder.append("...");
                notifySyncListenersProgress(stringBuilder.toString());
                Log.d("TRAKT", "Persisting cache...");
                cache.save(context);
            }
        } catch (Context context2) {
            context2.printStackTrace();
        }
    }

    public static void syncShowCache(String str, List<BaseShow> list) throws IOException {
        if (cache != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Sync show:");
            stringBuilder.append(str);
            stringBuilder.append(" progress...");
            Log.d("TRAKT", stringBuilder.toString());
            for (BaseShow baseShow : list) {
                if (baseShow.show.ids.imdb.equals(str)) {
                    str = getInstance().trakt.shows().watchedProgress(baseShow.show.ids.imdb, Boolean.valueOf(false), Boolean.valueOf(false), Extended.FULL).execute();
                    if (str.isSuccessful() != null) {
                        BaseShow baseShow2 = (BaseShow) str.body();
                        if (baseShow2 != null) {
                            baseShow.completed = baseShow2.completed;
                            baseShow.aired = baseShow2.aired;
                            baseShow.seasons = baseShow2.seasons;
                            baseShow.next_episode = baseShow2.next_episode;
                            return;
                        }
                        return;
                    }
                    return;
                }
            }
        }
    }

    public static void syncShowCache(Context context, String str) {
        try {
            if (!(Thread.currentThread().isInterrupted() || cache == null)) {
                retrofit2.Response execute;
                BaseShow baseShow;
                Object obj;
                List list;
                Object obj2;
                Object obj3;
                Iterator it;
                BaseShow baseShow2;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Sync show:");
                stringBuilder.append(str);
                stringBuilder.append(" progress...");
                Log.d("TRAKT", stringBuilder.toString());
                Iterator it2 = cache.watchedShows.iterator();
                while (it2.hasNext()) {
                    BaseShow baseShow3 = (BaseShow) it2.next();
                    if (baseShow3.show.ids.imdb.equals(str)) {
                        execute = getInstance().trakt.shows().watchedProgress(baseShow3.show.ids.imdb, Boolean.valueOf(false), Boolean.valueOf(false), Extended.FULL).execute();
                        if (execute.isSuccessful()) {
                            baseShow = (BaseShow) execute.body();
                            if (baseShow != null) {
                                baseShow3.completed = baseShow.completed;
                                baseShow3.aired = baseShow.aired;
                                baseShow3.seasons = baseShow.seasons;
                                baseShow3.next_episode = baseShow.next_episode;
                            }
                        }
                        obj = 1;
                        if (obj == null) {
                            execute = getInstance().trakt.users().watchedShows(UserSlug.ME, Extended.FULL).execute();
                            if (execute.isSuccessful()) {
                                list = (List) execute.body();
                                if (list != null) {
                                    it2 = list.iterator();
                                    do {
                                        obj3 = null;
                                        if (it2.hasNext()) {
                                            obj2 = null;
                                            break;
                                        }
                                        obj2 = (BaseShow) it2.next();
                                    } while (!obj2.show.ids.imdb.equals(str));
                                    execute = getInstance().trakt.shows().watchedProgress(obj2.show.ids.imdb, Boolean.valueOf(false), Boolean.valueOf(false), Extended.FULL).execute();
                                    if (execute.isSuccessful()) {
                                        baseShow = (BaseShow) execute.body();
                                        if (baseShow != null) {
                                            obj2.completed = baseShow.completed;
                                            obj2.aired = baseShow.aired;
                                            obj2.seasons = baseShow.seasons;
                                            obj2.next_episode = baseShow.next_episode;
                                        }
                                    }
                                    synchronized (cache.watchedShows) {
                                        it = cache.watchedShows.iterator();
                                        while (it.hasNext()) {
                                            baseShow2 = (BaseShow) it.next();
                                            if (baseShow2.show.ids == null && baseShow2.show.ids.imdb.equals(str)) {
                                                obj3 = baseShow2;
                                                break;
                                            }
                                        }
                                        if (obj3 != null) {
                                            cache.watchedShows.remove(obj3);
                                        }
                                        cache.watchedShows.add(obj2);
                                    }
                                }
                            }
                        }
                        Log.d("TRAKT", "Persisting cache...");
                        cache.save(context);
                    }
                }
                obj = null;
                if (obj == null) {
                    execute = getInstance().trakt.users().watchedShows(UserSlug.ME, Extended.FULL).execute();
                    if (execute.isSuccessful()) {
                        list = (List) execute.body();
                        if (list != null) {
                            it2 = list.iterator();
                            do {
                                obj3 = null;
                                if (it2.hasNext()) {
                                    obj2 = null;
                                    break;
                                }
                                obj2 = (BaseShow) it2.next();
                            } while (!obj2.show.ids.imdb.equals(str));
                            execute = getInstance().trakt.shows().watchedProgress(obj2.show.ids.imdb, Boolean.valueOf(false), Boolean.valueOf(false), Extended.FULL).execute();
                            if (execute.isSuccessful()) {
                                baseShow = (BaseShow) execute.body();
                                if (baseShow != null) {
                                    obj2.completed = baseShow.completed;
                                    obj2.aired = baseShow.aired;
                                    obj2.seasons = baseShow.seasons;
                                    obj2.next_episode = baseShow.next_episode;
                                }
                            }
                            synchronized (cache.watchedShows) {
                                it = cache.watchedShows.iterator();
                                while (it.hasNext()) {
                                    baseShow2 = (BaseShow) it.next();
                                    if (baseShow2.show.ids == null) {
                                    }
                                }
                                if (obj3 != null) {
                                    cache.watchedShows.remove(obj3);
                                }
                                cache.watchedShows.add(obj2);
                            }
                        }
                    }
                }
                Log.d("TRAKT", "Persisting cache...");
                cache.save(context);
            }
        } catch (Context context2) {
            context2.printStackTrace();
        }
    }

    public static void syncShowsCache(Context context) throws IOException {
        if (cache != null) {
            retrofit2.Response execute = getInstance().trakt.users().watchedShows(UserSlug.ME, Extended.FULL).execute();
            if (execute.isSuccessful()) {
                Log.d("TRAKT", "Sync shows...");
                List<BaseShow> list = (List) execute.body();
                if (list != null) {
                    Log.d("TRAKT", "Sync watched shows progress...");
                    for (BaseShow baseShow : list) {
                        syncShowCache(baseShow.show.ids.imdb, (List) list);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(context.getString(R.string.syncing_show));
                        stringBuilder.append(" '");
                        stringBuilder.append(baseShow.show.title);
                        stringBuilder.append("'...");
                        notifySyncListenersProgress(stringBuilder.toString());
                    }
                    synchronized (cache.watchedShows) {
                        cache.watchedShows.clear();
                        cache.watchedShows.addAll(list);
                    }
                }
                Log.d("TRAKT", "Persisting cache...");
                cache.save(context);
            }
        }
    }

    private static void notifySyncListenersStart() {
        if (syncListeners != null) {
            Iterator it = syncListeners.iterator();
            while (it.hasNext()) {
                ((TraktSyncListener) it.next()).onTraktSyncStart();
            }
        }
    }

    private static void notifySyncListenersComplete(boolean z) {
        if (syncListeners != null) {
            Iterator it = syncListeners.iterator();
            while (it.hasNext()) {
                ((TraktSyncListener) it.next()).onTraktSyncComplete(z);
            }
        }
    }

    private static void notifySyncListenersProgress(String str) {
        if (syncListeners != null) {
            Iterator it = syncListeners.iterator();
            while (it.hasNext()) {
                ((TraktSyncListener) it.next()).onTraktSyncProgress(str);
            }
        }
    }

    public static void forceSync() {
        shouldForceSync = true;
    }

    public static void Idle(int i) {
        try {
            long currentTimeMillis = System.currentTimeMillis() + ((long) i);
            while (System.currentTimeMillis() < currentTimeMillis && shouldForceSync == 0) {
                Thread.sleep(1000);
            }
            shouldForceSync = false;
        } catch (int i2) {
            i2.printStackTrace();
        }
    }

    public static void startSyncThread(Context context) {
        if (syncThread == null || syncThread.isInterrupted()) {
            context = context.getApplicationContext();
            killSyncThread();
            Log.d("TRAKT", "Starting Trakt sync thread...");
            Log.d("TRAKT", "Loading persisted cache...");
            cache = Cache.load(context);
            syncThread = new Thread(new Runnable() {
                public void run() {
                    /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
                    /*
                    r7 = this;
                L_0x0000:
                    r0 = java.lang.Thread.currentThread();	 Catch:{ Exception -> 0x007e }
                    r0 = r0.isInterrupted();	 Catch:{ Exception -> 0x007e }
                    if (r0 != 0) goto L_0x0082;	 Catch:{ Exception -> 0x007e }
                L_0x000a:
                    r0 = com.android.morpheustv.helpers.Trakt.cache;	 Catch:{ Exception -> 0x007e }
                    if (r0 == 0) goto L_0x0082;	 Catch:{ Exception -> 0x007e }
                L_0x000e:
                    r0 = com.android.morpheustv.helpers.Trakt.userToken;	 Catch:{ Exception -> 0x007e }
                    if (r0 == 0) goto L_0x0078;	 Catch:{ Exception -> 0x007e }
                L_0x0012:
                    r0 = com.android.morpheustv.sources.SourceList.isPlaying;	 Catch:{ Exception -> 0x007e }
                    if (r0 != 0) goto L_0x0078;	 Catch:{ Exception -> 0x007e }
                L_0x0016:
                    r0 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x007e }
                    r2 = "TRAKT";	 Catch:{ Exception -> 0x007e }
                    r3 = "Trakt sync init...";	 Catch:{ Exception -> 0x007e }
                    android.util.Log.d(r2, r3);	 Catch:{ Exception -> 0x007e }
                    com.android.morpheustv.helpers.Trakt.notifySyncListenersStart();	 Catch:{ Exception -> 0x007e }
                    r2 = 1;
                    r3 = r2;	 Catch:{ Exception -> 0x0030 }
                    com.android.morpheustv.helpers.Trakt.syncMovieCache(r3);	 Catch:{ Exception -> 0x0030 }
                    r3 = r2;	 Catch:{ Exception -> 0x0030 }
                    com.android.morpheustv.helpers.Trakt.syncShowsCache(r3);	 Catch:{ Exception -> 0x0030 }
                    goto L_0x0031;
                L_0x0030:
                    r2 = 0;
                L_0x0031:
                    com.android.morpheustv.helpers.Trakt.notifySyncListenersComplete(r2);	 Catch:{ Exception -> 0x007e }
                    r3 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x007e }
                    r5 = 0;	 Catch:{ Exception -> 0x007e }
                    r5 = r3 - r0;	 Catch:{ Exception -> 0x007e }
                    r0 = "TRAKT";	 Catch:{ Exception -> 0x007e }
                    r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007e }
                    r1.<init>();	 Catch:{ Exception -> 0x007e }
                    r3 = "Sync finished in ";	 Catch:{ Exception -> 0x007e }
                    r1.append(r3);	 Catch:{ Exception -> 0x007e }
                    r3 = java.lang.String.valueOf(r5);	 Catch:{ Exception -> 0x007e }
                    r1.append(r3);	 Catch:{ Exception -> 0x007e }
                    r3 = "ms";	 Catch:{ Exception -> 0x007e }
                    r1.append(r3);	 Catch:{ Exception -> 0x007e }
                    r1 = r1.toString();	 Catch:{ Exception -> 0x007e }
                    android.util.Log.d(r0, r1);	 Catch:{ Exception -> 0x007e }
                    if (r2 == 0) goto L_0x006a;	 Catch:{ Exception -> 0x007e }
                L_0x005c:
                    r0 = "TRAKT";	 Catch:{ Exception -> 0x007e }
                    r1 = "Sync success, going IDLE for 10 minutes...";	 Catch:{ Exception -> 0x007e }
                    android.util.Log.d(r0, r1);	 Catch:{ Exception -> 0x007e }
                    r0 = 600000; // 0x927c0 float:8.40779E-40 double:2.964394E-318;	 Catch:{ Exception -> 0x007e }
                    com.android.morpheustv.helpers.Trakt.Idle(r0);	 Catch:{ Exception -> 0x007e }
                    goto L_0x0000;	 Catch:{ Exception -> 0x007e }
                L_0x006a:
                    r0 = "TRAKT";	 Catch:{ Exception -> 0x007e }
                    r1 = "Sync failed, try again in 1 minute...";	 Catch:{ Exception -> 0x007e }
                    android.util.Log.d(r0, r1);	 Catch:{ Exception -> 0x007e }
                    r0 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;	 Catch:{ Exception -> 0x007e }
                    com.android.morpheustv.helpers.Trakt.Idle(r0);	 Catch:{ Exception -> 0x007e }
                    goto L_0x0000;	 Catch:{ Exception -> 0x007e }
                L_0x0078:
                    r0 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Exception -> 0x007e }
                    com.android.morpheustv.helpers.Trakt.Idle(r0);	 Catch:{ Exception -> 0x007e }
                    goto L_0x0000;
                L_0x007e:
                    r0 = move-exception;
                    r0.printStackTrace();
                L_0x0082:
                    r0 = "TRAKT";
                    r1 = "Sync thread killed.";
                    android.util.Log.d(r0, r1);
                    r0 = 0;
                    com.android.morpheustv.helpers.Trakt.syncThread = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.helpers.Trakt.24.run():void");
                }
            });
            syncThread.start();
        }
    }

    public static void killSyncThread() {
        if (syncThread != null) {
            syncThread.interrupt();
        }
        syncThread = null;
    }
}
