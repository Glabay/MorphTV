package com.android.morpheustv.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import com.android.morpheustv.helpers.TaskManager;
import com.android.morpheustv.sources.subtitles.LegendasDivx;
import com.google.gson.Gson;
import com.noname.titan.R;
import com.tonyodev.fetch2.util.FetchDefaults;
import com.uwetrottmann.trakt5.TraktV2;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Settings {
    public static Set<String> AUTOSELECT_MOVIES_HOST = null;
    public static Set<String> AUTOSELECT_MOVIES_QUALITY = null;
    public static Set<String> AUTOSELECT_SHOWS_HOST = null;
    public static Set<String> AUTOSELECT_SHOWS_QUALITY = null;
    public static boolean AUTOSELECT_SOURCES_MOVIES = false;
    public static boolean AUTOSELECT_SOURCES_MOVIES_FAST = false;
    public static boolean AUTOSELECT_SOURCES_SHOWS = false;
    public static boolean AUTOSELECT_SOURCES_SHOWS_FAST = false;
    public static int BETA_UPDATES = 0;
    public static String CAST_RECEIVER_APP_ID = "3B221EB7";
    public static boolean DOWNLOADS_CELLULAR = false;
    public static String DOWNLOADS_DOWNLOAD_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    public static boolean ENABLE_123HULU = true;
    public static boolean ENABLE_123MOVIESFREE = true;
    public static boolean ENABLE_123MOVIESHD = true;
    public static boolean ENABLE_123MOVIESHUB = true;
    public static boolean ENABLE_123RARBG = true;
    public static boolean ENABLE_123YTS = true;
    public static boolean ENABLE_AFDAH = true;
    public static boolean ENABLE_AZMOVIES = true;
    public static boolean ENABLE_BMOVIESFREE = true;
    public static boolean ENABLE_CARTOONHD = true;
    public static boolean ENABLE_CINEBLOOM = true;
    public static boolean ENABLE_CONSISTENTSTREAM = true;
    public static boolean ENABLE_ENTERVIDEO = true;
    public static boolean ENABLE_EZTV = true;
    public static boolean ENABLE_FLIXANITY = true;
    public static boolean ENABLE_FMOVIES = true;
    public static boolean ENABLE_GOMOVIES = true;
    public static boolean ENABLE_GOMOVIESSC = true;
    public static boolean ENABLE_GVIDEO = true;
    public static boolean ENABLE_HDGO = true;
    public static boolean ENABLE_ICEFILMS = true;
    public static boolean ENABLE_LEGENDASDIVX = false;
    public static boolean ENABLE_LEMONHLS = true;
    public static boolean ENABLE_MCLOUDHLS = true;
    public static boolean ENABLE_MOVIE4K = true;
    public static boolean ENABLE_MOVIETOKEN = true;
    public static boolean ENABLE_ODBTO = true;
    public static boolean ENABLE_OPENLOAD = true;
    public static boolean ENABLE_OPENLOADMOVIES = true;
    public static boolean ENABLE_OPENSUBTITLES = true;
    public static boolean ENABLE_OTHERS = true;
    public static boolean ENABLE_PLAYMOVIES = true;
    public static boolean ENABLE_PMOVIES = true;
    public static boolean ENABLE_PUTLOCKER = true;
    public static boolean ENABLE_PUTLOCKERSMOVIES = true;
    public static boolean ENABLE_RARBG = true;
    public static boolean ENABLE_REDDIT = true;
    public static boolean ENABLE_SCREENCOUCH = true;
    public static boolean ENABLE_SERIESFREE = true;
    public static boolean ENABLE_SERIESONLINE = true;
    public static boolean ENABLE_SOLARMOVIE = true;
    public static boolean ENABLE_STREAMANGO = true;
    public static boolean ENABLE_STREAMLORD = true;
    public static boolean ENABLE_STRMGOHLS = true;
    public static boolean ENABLE_THEMOVIESERIES = true;
    public static boolean ENABLE_THEVIDEO = true;
    public static boolean ENABLE_THEWATCHSERIES = true;
    public static boolean ENABLE_TORRENTS = false;
    public static boolean ENABLE_TV21 = true;
    public static boolean ENABLE_TVSUBS = true;
    public static boolean ENABLE_VIDOZA = true;
    public static boolean ENABLE_VIDUP = true;
    public static boolean ENABLE_XMOVIES8 = true;
    public static boolean ENABLE_XWATCHSERIES = true;
    public static boolean ENABLE_YESMOVIEIO = true;
    public static boolean ENABLE_YESMOVIES = true;
    public static boolean ENABLE_YIFY = true;
    public static String GROUP_SOURCES = "host";
    public static int MAX_BUFFER_SIZE = 30000;
    public static int MAX_PROVIDERS = TaskManager.NUMBER_OF_CORES;
    public static int MAX_SIMULTANEOUS_DOWNLOADS = 4;
    public static int MAX_SOURCES = 5;
    public static int MIN_BUFFER_SIZE = 15000;
    public static boolean NOTIFICATION_DOWNLOADS = false;
    public static boolean NOTIFICATION_MAIN = false;
    public static int PLAYBACK_BUFFER_SIZE = 2000;
    public static boolean PROXY_STREAMS = false;
    public static int SCRAPING_TIMEOUT = 120000;
    public static boolean SHOW_CAPTCHAS = false;
    public static String STREAMLORD_PASSWORD = "123456";
    public static String STREAMLORD_USERNAME = "morpheus_titan";
    public static boolean SUBTITLES_AUTOSEL = true;
    public static String SUBTITLES_COLOR = "White";
    public static boolean SUBTITLES_ENABLED = true;
    public static String SUBTITLES_LANGS = "";
    public static String SUBTITLES_MARGIN = "30";
    public static String SUBTITLES_SIZE = "30";
    public static int THREAD_TIMEOUT = 40000;
    public static String TORRENT_DOWNLOAD_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    public static int TORRENT_MAX_CONNECTIONS = 150;
    public static int TORRENT_MAX_DOWNLOAD = 0;
    public static long TORRENT_MAX_SIZE_MOVIES = 0;
    public static long TORRENT_MAX_SIZE_SHOWS = 0;
    public static int TORRENT_MAX_UPLOAD = 0;
    public static boolean TORRENT_REMOVE_DOWNLOADS = true;
    public static boolean VALIDATE_SOURCES = true;
    public static int VERIFY_TIMEOUT = 8000;

    public static void load(Context context) {
        String string;
        Set hashSet;
        String string2;
        Log.d("Settings", "Loading Settings...");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        CAST_RECEIVER_APP_ID = defaultSharedPreferences.getString("prefCastReceiver", CAST_RECEIVER_APP_ID);
        MAX_PROVIDERS = Integer.parseInt(defaultSharedPreferences.getString("prefMaxThreads", String.valueOf(MAX_PROVIDERS)));
        SCRAPING_TIMEOUT = Integer.parseInt(defaultSharedPreferences.getString("prefTimeout", "120")) * 1000;
        THREAD_TIMEOUT = Integer.parseInt(defaultSharedPreferences.getString("prefTimeoutThread", "40")) * 1000;
        BETA_UPDATES = defaultSharedPreferences.getInt("prefAlternateUpdater", 0);
        MAX_SOURCES = Integer.parseInt(defaultSharedPreferences.getString("prefMaxSourcesProvider", "5"));
        ENABLE_FLIXANITY = defaultSharedPreferences.getBoolean("pref_provider_FLIXANITY", true);
        ENABLE_CARTOONHD = defaultSharedPreferences.getBoolean("pref_provider_CARTOONHD", true);
        ENABLE_FMOVIES = defaultSharedPreferences.getBoolean("pref_provider_FMOVIES", true);
        ENABLE_123MOVIESHD = defaultSharedPreferences.getBoolean("pref_provider_123MOVIESHD", true);
        ENABLE_YESMOVIEIO = defaultSharedPreferences.getBoolean("pref_provider_YESMOVIEIO", true);
        ENABLE_GOMOVIES = defaultSharedPreferences.getBoolean("pref_provider_GOMOVIES", true);
        ENABLE_PMOVIES = defaultSharedPreferences.getBoolean("pref_provider_PMOVIES", true);
        ENABLE_PUTLOCKER = defaultSharedPreferences.getBoolean("pref_provider_PUTLOCKER", true);
        ENABLE_SERIESONLINE = defaultSharedPreferences.getBoolean("pref_provider_SERIESONLINE", true);
        ENABLE_OPENLOADMOVIES = defaultSharedPreferences.getBoolean("pref_provider_OPENLOADMOVIES", true);
        ENABLE_YESMOVIES = defaultSharedPreferences.getBoolean("pref_provider_YESMOVIES", true);
        ENABLE_REDDIT = defaultSharedPreferences.getBoolean("pref_provider_REDDIT", true);
        ENABLE_THEWATCHSERIES = defaultSharedPreferences.getBoolean("pref_provider_THEWATCHSERIES", true);
        ENABLE_MOVIETOKEN = defaultSharedPreferences.getBoolean("pref_provider_MOVIETOKEN", true);
        ENABLE_SOLARMOVIE = defaultSharedPreferences.getBoolean("pref_provider_SOLARMOVIE", true);
        ENABLE_THEMOVIESERIES = defaultSharedPreferences.getBoolean("pref_provider_THEMOVIESERIES", true);
        ENABLE_ICEFILMS = defaultSharedPreferences.getBoolean("pref_provider_ICEFILMS", true);
        ENABLE_STREAMLORD = defaultSharedPreferences.getBoolean("pref_provider_STREAMLORD", true);
        STREAMLORD_USERNAME = defaultSharedPreferences.getString("prefSTREAMLORDUsername", "Morphtv_TNN");
        STREAMLORD_PASSWORD = defaultSharedPreferences.getString("prefSTREAMLORDPassword", "123456");
        ENABLE_PUTLOCKERSMOVIES = defaultSharedPreferences.getBoolean("pref_provider_PUTLOCKERSMOVIES", true);
        ENABLE_CONSISTENTSTREAM = defaultSharedPreferences.getBoolean("pref_provider_PUTSTREAM", true);
        ENABLE_SERIESFREE = defaultSharedPreferences.getBoolean("pref_provider_SERIESFREE", true);
        ENABLE_XWATCHSERIES = defaultSharedPreferences.getBoolean("pref_provider_XWATCHSERIES", true);
        ENABLE_123YTS = defaultSharedPreferences.getBoolean("pref_provider_123YTS", true);
        ENABLE_GOMOVIESSC = defaultSharedPreferences.getBoolean("pref_provider_GOMOVIESSC", true);
        ENABLE_AFDAH = defaultSharedPreferences.getBoolean("pref_provider_AFDAH", true);
        ENABLE_MOVIE4K = defaultSharedPreferences.getBoolean("pref_provider_MOVIE4K", true);
        ENABLE_RARBG = defaultSharedPreferences.getBoolean("pref_provider_RARBG", true);
        ENABLE_EZTV = defaultSharedPreferences.getBoolean("pref_provider_EZTV", true);
        ENABLE_YIFY = defaultSharedPreferences.getBoolean("pref_provider_YIFY", true);
        ENABLE_PLAYMOVIES = defaultSharedPreferences.getBoolean("pref_provider_PLAYMOVIES", true);
        ENABLE_123RARBG = defaultSharedPreferences.getBoolean("pref_provider_123RARBG", true);
        ENABLE_BMOVIESFREE = defaultSharedPreferences.getBoolean("pref_provider_BMOVIESFREE", true);
        ENABLE_123MOVIESHUB = defaultSharedPreferences.getBoolean("pref_provider_123MOVIESHUB", true);
        ENABLE_HDGO = defaultSharedPreferences.getBoolean("pref_provider_HDGO", true);
        ENABLE_TV21 = defaultSharedPreferences.getBoolean("pref_provider_TV21", true);
        ENABLE_ODBTO = defaultSharedPreferences.getBoolean("pref_provider_ODBTO", true);
        ENABLE_SCREENCOUCH = defaultSharedPreferences.getBoolean("pref_provider_SCREENCOUCH", true);
        ENABLE_AZMOVIES = defaultSharedPreferences.getBoolean("pref_provider_AZMOVIES", true);
        ENABLE_CINEBLOOM = defaultSharedPreferences.getBoolean("pref_provider_CINEBLOOM", true);
        ENABLE_123HULU = defaultSharedPreferences.getBoolean("pref_provider_123HULU", true);
        ENABLE_123MOVIESFREE = defaultSharedPreferences.getBoolean("pref_provider_123MOVIESFREE", true);
        ENABLE_XMOVIES8 = defaultSharedPreferences.getBoolean("pref_provider_XMOVIES8", true);
        ENABLE_GVIDEO = defaultSharedPreferences.getBoolean("pref_source_GVIDEO", true);
        ENABLE_OPENLOAD = defaultSharedPreferences.getBoolean("pref_source_OPENLOAD", true);
        ENABLE_STREAMANGO = defaultSharedPreferences.getBoolean("pref_source_STREAMANGO", true);
        ENABLE_THEVIDEO = defaultSharedPreferences.getBoolean("pref_source_THEVIDEO", true);
        ENABLE_VIDUP = defaultSharedPreferences.getBoolean("pref_source_VIDUP", true);
        ENABLE_VIDOZA = defaultSharedPreferences.getBoolean("pref_source_VIDOZA", true);
        ENABLE_ENTERVIDEO = defaultSharedPreferences.getBoolean("pref_source_ENTERVIDEO", true);
        ENABLE_STRMGOHLS = defaultSharedPreferences.getBoolean("pref_source_STRMGOHLS", true);
        ENABLE_LEMONHLS = defaultSharedPreferences.getBoolean("pref_source_LEMONHLS", true);
        ENABLE_MCLOUDHLS = defaultSharedPreferences.getBoolean("pref_source_MCLOUDHLS", true);
        ENABLE_TORRENTS = defaultSharedPreferences.getBoolean("pref_source_TORRENTS", false);
        ENABLE_OTHERS = defaultSharedPreferences.getBoolean("pref_source_OTHERS", true);
        TORRENT_REMOVE_DOWNLOADS = defaultSharedPreferences.getBoolean("prefTorrentsRemoveDownloads", true);
        SUBTITLES_ENABLED = defaultSharedPreferences.getBoolean("pref_subtitle_enable", true);
        Set<String> stringSet = defaultSharedPreferences.getStringSet("prefSubtitleSecondaryLangs", new HashSet(Arrays.asList(context.getResources().getStringArray(R.array.lang_default_values))));
        String string3 = defaultSharedPreferences.getString("prefSubtitlePrimaryLang", Locale.getDefault().getISO3Language().replace("por", "por,pob"));
        String str = string3;
        for (String string4 : stringSet) {
            if (!(string4.equals(string3) || string4.equals("disabled"))) {
                StringBuilder stringBuilder;
                if (!str.isEmpty()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(",");
                    str = stringBuilder.toString();
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(string4);
                str = stringBuilder.toString();
            }
        }
        if (!str.toLowerCase().contains("disabled")) {
            if (SUBTITLES_ENABLED) {
                SUBTITLES_LANGS = str.toLowerCase();
                SUBTITLES_COLOR = defaultSharedPreferences.getString("prefSubtitlesColor", SUBTITLES_COLOR);
                SUBTITLES_MARGIN = defaultSharedPreferences.getString("prefSubtitlesDistance", SUBTITLES_MARGIN);
                SUBTITLES_SIZE = defaultSharedPreferences.getString("prefSubtitlesTextSize", SUBTITLES_SIZE);
                SUBTITLES_AUTOSEL = defaultSharedPreferences.getBoolean("pref_subtitle_autoselect", SUBTITLES_AUTOSEL);
                MIN_BUFFER_SIZE = Integer.parseInt(defaultSharedPreferences.getString("prefMinBufferSize", "15")) * 1000;
                MAX_BUFFER_SIZE = Integer.parseInt(defaultSharedPreferences.getString("prefMaxBufferSize", "30")) * 1000;
                PLAYBACK_BUFFER_SIZE = Integer.parseInt(defaultSharedPreferences.getString("prefBufferForPlayback", TraktV2.API_VERSION)) * 1000;
                VALIDATE_SOURCES = defaultSharedPreferences.getBoolean("prefVerifySources", VALIDATE_SOURCES);
                SHOW_CAPTCHAS = defaultSharedPreferences.getBoolean("pref_captchas", SHOW_CAPTCHAS);
                VERIFY_TIMEOUT = Integer.parseInt(defaultSharedPreferences.getString("prefTimeoutVerify", "8")) * 1000;
                AUTOSELECT_SOURCES_MOVIES = defaultSharedPreferences.getBoolean("prefSourceAutoSel", AUTOSELECT_SOURCES_MOVIES);
                AUTOSELECT_SOURCES_SHOWS = defaultSharedPreferences.getBoolean("prefSourceAutoSelShows", AUTOSELECT_SOURCES_SHOWS);
                AUTOSELECT_SOURCES_MOVIES_FAST = defaultSharedPreferences.getBoolean("prefSourceAutoSelMoviesFast", AUTOSELECT_SOURCES_MOVIES_FAST);
                AUTOSELECT_SOURCES_SHOWS_FAST = defaultSharedPreferences.getBoolean("prefSourceAutoSelShowsFast", AUTOSELECT_SOURCES_SHOWS_FAST);
                hashSet = new HashSet(Arrays.asList(context.getResources().getStringArray(R.array.pref_autoplay_quality_defaults)));
                AUTOSELECT_MOVIES_QUALITY = defaultSharedPreferences.getStringSet("prefSourceAutoSelQualityMoviesList", hashSet);
                AUTOSELECT_SHOWS_QUALITY = defaultSharedPreferences.getStringSet("prefSourceAutoSelQualityShowsList", hashSet);
                AUTOSELECT_MOVIES_HOST = defaultSharedPreferences.getStringSet("prefSourceAutoSelHostMoviesList", hashSet);
                AUTOSELECT_SHOWS_HOST = defaultSharedPreferences.getStringSet("prefSourceAutoSelHostShowsList", hashSet);
                GROUP_SOURCES = defaultSharedPreferences.getString("prefSourceGroup", GROUP_SOURCES);
                PROXY_STREAMS = defaultSharedPreferences.getBoolean("prefProxyStreams", PROXY_STREAMS);
                TORRENT_MAX_CONNECTIONS = Integer.parseInt(defaultSharedPreferences.getString("prefTorrentsMaxConnections", "100"));
                TORRENT_DOWNLOAD_FOLDER = defaultSharedPreferences.getString("prefTorrentsDownloadFolder", TORRENT_DOWNLOAD_FOLDER).replace(":", "");
                TORRENT_DOWNLOAD_FOLDER = new File(TORRENT_DOWNLOAD_FOLDER).getAbsolutePath();
                context = defaultSharedPreferences.getString("prefTorrentsDownloadLimit", "Unlimited");
                string2 = defaultSharedPreferences.getString("prefTorrentsUploadLimit", "Unlimited");
                str = defaultSharedPreferences.getString("prefTorrentsSizeMovies", "All");
                string4 = defaultSharedPreferences.getString("prefTorrentsSizeShows", "All");
                if (context.equals("Unlimited")) {
                    context = "0";
                }
                if (string2.equals("Unlimited")) {
                    string2 = "0";
                }
                if (str.equals("All")) {
                    str = "0";
                }
                if (string4.equals("All")) {
                    string4 = "0";
                }
                TORRENT_MAX_DOWNLOAD = Integer.parseInt(context) * 1000;
                TORRENT_MAX_UPLOAD = Integer.parseInt(string2) * 1000;
                TORRENT_MAX_SIZE_MOVIES = (long) ((Integer.parseInt(str) * 1000) * 1000);
                TORRENT_MAX_SIZE_SHOWS = (long) ((Integer.parseInt(string4) * 1000) * 1000);
                LegendasDivx.USERNAME = defaultSharedPreferences.getString("prefLegendasDivxUsername", "");
                LegendasDivx.PASSWORD = defaultSharedPreferences.getString("prefLegendasDivxPassword", "");
                ENABLE_OPENSUBTITLES = defaultSharedPreferences.getBoolean("pref_subtitle_OPENSUBTITLES", ENABLE_OPENSUBTITLES);
                ENABLE_TVSUBS = defaultSharedPreferences.getBoolean("pref_subtitle_TVSUBS", ENABLE_TVSUBS);
                ENABLE_LEGENDASDIVX = defaultSharedPreferences.getBoolean("pref_subtitle_LEGENDASDIVX", ENABLE_LEGENDASDIVX);
                MAX_SIMULTANEOUS_DOWNLOADS = Integer.parseInt(defaultSharedPreferences.getString("prefMaxSimulDownloads", "4"));
                DOWNLOADS_DOWNLOAD_FOLDER = defaultSharedPreferences.getString("prefDownloadsDownloadFolder", DOWNLOADS_DOWNLOAD_FOLDER).replace(":", "");
                DOWNLOADS_DOWNLOAD_FOLDER = new File(DOWNLOADS_DOWNLOAD_FOLDER).getAbsolutePath();
                DOWNLOADS_CELLULAR = defaultSharedPreferences.getBoolean("prefDownloadsCellular", DOWNLOADS_CELLULAR);
                NOTIFICATION_MAIN = defaultSharedPreferences.getBoolean("prefMainNotification2", NOTIFICATION_MAIN);
                NOTIFICATION_DOWNLOADS = defaultSharedPreferences.getBoolean("prefDownloadNotifications2", NOTIFICATION_DOWNLOADS);
                context = defaultSharedPreferences.edit();
                context.putString("prefSubtitlePrimaryLang", string3);
                context.apply();
            }
        }
        SUBTITLES_LANGS = "";
        SUBTITLES_COLOR = defaultSharedPreferences.getString("prefSubtitlesColor", SUBTITLES_COLOR);
        SUBTITLES_MARGIN = defaultSharedPreferences.getString("prefSubtitlesDistance", SUBTITLES_MARGIN);
        SUBTITLES_SIZE = defaultSharedPreferences.getString("prefSubtitlesTextSize", SUBTITLES_SIZE);
        SUBTITLES_AUTOSEL = defaultSharedPreferences.getBoolean("pref_subtitle_autoselect", SUBTITLES_AUTOSEL);
        MIN_BUFFER_SIZE = Integer.parseInt(defaultSharedPreferences.getString("prefMinBufferSize", "15")) * 1000;
        MAX_BUFFER_SIZE = Integer.parseInt(defaultSharedPreferences.getString("prefMaxBufferSize", "30")) * 1000;
        PLAYBACK_BUFFER_SIZE = Integer.parseInt(defaultSharedPreferences.getString("prefBufferForPlayback", TraktV2.API_VERSION)) * 1000;
        VALIDATE_SOURCES = defaultSharedPreferences.getBoolean("prefVerifySources", VALIDATE_SOURCES);
        SHOW_CAPTCHAS = defaultSharedPreferences.getBoolean("pref_captchas", SHOW_CAPTCHAS);
        VERIFY_TIMEOUT = Integer.parseInt(defaultSharedPreferences.getString("prefTimeoutVerify", "8")) * 1000;
        AUTOSELECT_SOURCES_MOVIES = defaultSharedPreferences.getBoolean("prefSourceAutoSel", AUTOSELECT_SOURCES_MOVIES);
        AUTOSELECT_SOURCES_SHOWS = defaultSharedPreferences.getBoolean("prefSourceAutoSelShows", AUTOSELECT_SOURCES_SHOWS);
        AUTOSELECT_SOURCES_MOVIES_FAST = defaultSharedPreferences.getBoolean("prefSourceAutoSelMoviesFast", AUTOSELECT_SOURCES_MOVIES_FAST);
        AUTOSELECT_SOURCES_SHOWS_FAST = defaultSharedPreferences.getBoolean("prefSourceAutoSelShowsFast", AUTOSELECT_SOURCES_SHOWS_FAST);
        hashSet = new HashSet(Arrays.asList(context.getResources().getStringArray(R.array.pref_autoplay_quality_defaults)));
        AUTOSELECT_MOVIES_QUALITY = defaultSharedPreferences.getStringSet("prefSourceAutoSelQualityMoviesList", hashSet);
        AUTOSELECT_SHOWS_QUALITY = defaultSharedPreferences.getStringSet("prefSourceAutoSelQualityShowsList", hashSet);
        AUTOSELECT_MOVIES_HOST = defaultSharedPreferences.getStringSet("prefSourceAutoSelHostMoviesList", hashSet);
        AUTOSELECT_SHOWS_HOST = defaultSharedPreferences.getStringSet("prefSourceAutoSelHostShowsList", hashSet);
        GROUP_SOURCES = defaultSharedPreferences.getString("prefSourceGroup", GROUP_SOURCES);
        PROXY_STREAMS = defaultSharedPreferences.getBoolean("prefProxyStreams", PROXY_STREAMS);
        TORRENT_MAX_CONNECTIONS = Integer.parseInt(defaultSharedPreferences.getString("prefTorrentsMaxConnections", "100"));
        TORRENT_DOWNLOAD_FOLDER = defaultSharedPreferences.getString("prefTorrentsDownloadFolder", TORRENT_DOWNLOAD_FOLDER).replace(":", "");
        TORRENT_DOWNLOAD_FOLDER = new File(TORRENT_DOWNLOAD_FOLDER).getAbsolutePath();
        context = defaultSharedPreferences.getString("prefTorrentsDownloadLimit", "Unlimited");
        string2 = defaultSharedPreferences.getString("prefTorrentsUploadLimit", "Unlimited");
        str = defaultSharedPreferences.getString("prefTorrentsSizeMovies", "All");
        string4 = defaultSharedPreferences.getString("prefTorrentsSizeShows", "All");
        if (context.equals("Unlimited")) {
            context = "0";
        }
        if (string2.equals("Unlimited")) {
            string2 = "0";
        }
        if (str.equals("All")) {
            str = "0";
        }
        if (string4.equals("All")) {
            string4 = "0";
        }
        TORRENT_MAX_DOWNLOAD = Integer.parseInt(context) * 1000;
        TORRENT_MAX_UPLOAD = Integer.parseInt(string2) * 1000;
        TORRENT_MAX_SIZE_MOVIES = (long) ((Integer.parseInt(str) * 1000) * 1000);
        TORRENT_MAX_SIZE_SHOWS = (long) ((Integer.parseInt(string4) * 1000) * 1000);
        LegendasDivx.USERNAME = defaultSharedPreferences.getString("prefLegendasDivxUsername", "");
        LegendasDivx.PASSWORD = defaultSharedPreferences.getString("prefLegendasDivxPassword", "");
        ENABLE_OPENSUBTITLES = defaultSharedPreferences.getBoolean("pref_subtitle_OPENSUBTITLES", ENABLE_OPENSUBTITLES);
        ENABLE_TVSUBS = defaultSharedPreferences.getBoolean("pref_subtitle_TVSUBS", ENABLE_TVSUBS);
        ENABLE_LEGENDASDIVX = defaultSharedPreferences.getBoolean("pref_subtitle_LEGENDASDIVX", ENABLE_LEGENDASDIVX);
        MAX_SIMULTANEOUS_DOWNLOADS = Integer.parseInt(defaultSharedPreferences.getString("prefMaxSimulDownloads", "4"));
        DOWNLOADS_DOWNLOAD_FOLDER = defaultSharedPreferences.getString("prefDownloadsDownloadFolder", DOWNLOADS_DOWNLOAD_FOLDER).replace(":", "");
        DOWNLOADS_DOWNLOAD_FOLDER = new File(DOWNLOADS_DOWNLOAD_FOLDER).getAbsolutePath();
        DOWNLOADS_CELLULAR = defaultSharedPreferences.getBoolean("prefDownloadsCellular", DOWNLOADS_CELLULAR);
        NOTIFICATION_MAIN = defaultSharedPreferences.getBoolean("prefMainNotification2", NOTIFICATION_MAIN);
        NOTIFICATION_DOWNLOADS = defaultSharedPreferences.getBoolean("prefDownloadNotifications2", NOTIFICATION_DOWNLOADS);
        context = defaultSharedPreferences.edit();
        context.putString("prefSubtitlePrimaryLang", string3);
        context.apply();
    }

    public static void saveObject(Context context, String str, Object obj) {
        context = PreferenceManager.getDefaultSharedPreferences(context).edit();
        String toJson = new Gson().toJson(obj);
        if (obj == null) {
            context.remove(str);
        } else {
            context.putString(str, toJson);
        }
        context.apply();
    }

    public static Object loadObject(Context context, String str, Class cls) {
        return new Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(str, FetchDefaults.EMPTY_JSON_OBJECT_STRING), cls);
    }

    public static void saveLong(Context context, String str, long j) {
        context = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (j == 0) {
            context.remove(str);
        } else {
            context.putLong(str, j);
        }
        context.apply();
    }

    public static long loadLong(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(str, 0);
    }

    public static void saveInt(Context context, String str, int i) {
        context = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (i == 0) {
            context.remove(str);
        } else {
            context.putInt(str, i);
        }
        context.apply();
    }

    public static int loadInt(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(str, 0);
    }

    public static void saveString(Context context, String str, String str2) {
        context = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (str2 == null) {
            context.remove(str);
        } else {
            context.putString(str, str2);
        }
        context.apply();
    }

    public static String loadString(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, null);
    }
}
