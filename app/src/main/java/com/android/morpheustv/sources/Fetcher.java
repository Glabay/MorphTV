package com.android.morpheustv.sources;

import android.util.Log;
import com.android.morpheustv.BaseActivity;
import com.android.morpheustv.settings.Settings;
import com.android.morpheustv.sources.subtitles.LegendasDivx;
import com.android.morpheustv.sources.subtitles.OpenSubtitles;
import com.android.morpheustv.sources.subtitles.TVSubs;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.ScraperListener;
import io.github.morpheustv.scrapers.model.ProviderConfig;
import io.github.morpheustv.scrapers.model.ResolverConfig;
import io.github.morpheustv.scrapers.model.ScraperConfig;
import io.github.morpheustv.scrapers.model.ScraperMediaInfo;
import io.github.morpheustv.scrapers.model.ScrapingStatus;
import io.github.morpheustv.scrapers.model.Source;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Fetcher {
    public static boolean bestMatchFound = false;
    public static CopyOnWriteArrayList<Source> currentSources = null;
    private static CopyOnWriteArrayList<SubtitleResult> currentSubtitleResults = null;
    public static String currentTitle = "";
    public static boolean isBusy;
    public static boolean isBusySubtites;
    public static boolean shouldSelectOne;
    public static boolean wasCancelled;

    public interface FetchSourcesListener {
        void onFetch(CopyOnWriteArrayList<Source> copyOnWriteArrayList);

        void onFinish();

        void onProgress(long j);
    }

    public interface FetchSubtitleListener {
        void onFetch(CopyOnWriteArrayList<SubtitleResult> copyOnWriteArrayList);

        void onFinish();
    }

    public interface DownloadSubtitleListener {
        void onDownload(String str);

        void onFail();
    }

    public static class SubtitleFetchThread extends Thread {
        private int episode;
        private String imdb;
        private String lang;
        private BaseSubtitleProvider provider;
        boolean running = false;
        private int season;
        boolean started = false;
        long timeStarted = 0;

        public SubtitleFetchThread(BaseSubtitleProvider baseSubtitleProvider, String str, String str2, int i, int i2) {
            this.season = i;
            this.episode = i2;
            this.provider = baseSubtitleProvider;
            this.imdb = str;
            this.lang = str2;
            this.started = false;
            this.running = false;
        }

        public void stopThread() {
            try {
                this.running = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                Log.d(this.provider.PROVIDER_NAME, "Starting subtitle fetch thread...");
                this.running = true;
                this.started = true;
                Collection subtitles = this.provider.getSubtitles(this.imdb, this.lang, this.season, this.episode);
                if (!(subtitles == null || subtitles.isEmpty())) {
                    Fetcher.currentSubtitleResults.addAll(subtitles);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            stopThread();
            Log.d(this.provider.PROVIDER_NAME, "Terminating subtitle fetch thread!");
        }

        public String toString() {
            return this.provider.PROVIDER_NAME;
        }
    }

    public static void fetchSources(BaseActivity baseActivity, String str, String str2, String str3, int i, int i2, int i3, FetchSourcesListener fetchSourcesListener) {
        int i4 = i3;
        currentSources = new CopyOnWriteArrayList();
        currentTitle = str;
        if (i > 0 && i2 > 0) {
            currentTitle = String.format("%s S%02dE%02d", new Object[]{str, Integer.valueOf(i), Integer.valueOf(i2)});
        }
        isBusy = true;
        Settings.load(baseActivity);
        ScraperConfig scraperConfig = new ScraperConfig(baseActivity);
        scraperConfig.setMaxSourcesProvider(Settings.MAX_SOURCES);
        scraperConfig.setGlobalScraperTimeout(Settings.SCRAPING_TIMEOUT);
        if (i4 > 0) {
            scraperConfig.setGlobalScraperTimeout(i4);
        }
        scraperConfig.setIndividualScraperTimeout(Settings.THREAD_TIMEOUT);
        scraperConfig.setVerifySources(Settings.VALIDATE_SOURCES);
        scraperConfig.setVerifySourcesTimeout(Settings.VERIFY_TIMEOUT);
        scraperConfig.setMaxThreads(Settings.MAX_PROVIDERS);
        scraperConfig.setTorrentsEnabled(Settings.ENABLE_TORRENTS);
        scraperConfig.setTorrentMaxSizeMovies(Settings.TORRENT_MAX_SIZE_MOVIES);
        scraperConfig.setTorrentMaxSizeShows(Settings.TORRENT_MAX_SIZE_SHOWS);
        ProviderConfig providers = scraperConfig.getProviders();
        providers.setAfdahEnabled(Settings.ENABLE_AFDAH);
        providers.setRarbgEnabled(Settings.ENABLE_RARBG);
        providers.setRarbg123Enabled(Settings.ENABLE_123RARBG);
        providers.setYtsAgEnabled(Settings.ENABLE_YIFY);
        providers.setYesMoviesEnabled(Settings.ENABLE_YESMOVIES);
        providers.setYesMovieIOEnabled(Settings.ENABLE_YESMOVIEIO);
        providers.setSeriesFreeEnabled(Settings.ENABLE_SERIESFREE);
        providers.setXWatchSeriesEnabled(Settings.ENABLE_XWATCHSERIES);
        providers.setStreamlordEnabled(Settings.ENABLE_STREAMLORD);
        providers.setStreamlordUsername(Settings.STREAMLORD_USERNAME);
        providers.setStreamlordPassword(Settings.STREAMLORD_PASSWORD);
        providers.setYts123Enabled(Settings.ENABLE_123YTS);
        providers.setWatchSeriesMoviesEnabled(Settings.ENABLE_THEWATCHSERIES);
        providers.setWatchonlineSCEnabled(Settings.ENABLE_GOMOVIES);
        providers.setTV21Enabled(Settings.ENABLE_TV21);
        providers.setSolarMovieEnabled(Settings.ENABLE_SOLARMOVIE);
        providers.setSeries9Enabled(Settings.ENABLE_SERIESONLINE);
        providers.setRedditEnabled(Settings.ENABLE_REDDIT);
        providers.setPutlockersMovieEnabled(Settings.ENABLE_PUTLOCKERSMOVIES);
        providers.setPutlockerSkEnabled(Settings.ENABLE_PUTLOCKER);
        providers.setOnionPlayEnabled(Settings.ENABLE_CONSISTENTSTREAM);
        providers.setMoviesHub123Enabled(Settings.ENABLE_123MOVIESHUB);
        providers.setMovie4KEnabled(Settings.ENABLE_MOVIE4K);
        providers.setMehlizmoviesEnabled(Settings.ENABLE_OPENLOADMOVIES);
        providers.setIcefilmsEnabled(Settings.ENABLE_ICEFILMS);
        providers.setHdgoEnabled(Settings.ENABLE_HDGO);
        providers.setGoMoviesEnabled(Settings.ENABLE_GOMOVIESSC);
        providers.setFMoviesEnabled(Settings.ENABLE_FMOVIES);
        providers.setFlixanityEnabled(Settings.ENABLE_FLIXANITY);
        providers.setCMoviesHDEnabled(Settings.ENABLE_THEMOVIESERIES);
        providers.setEZTVEnabled(Settings.ENABLE_EZTV);
        providers.setCartoonHDEnabled(Settings.ENABLE_CARTOONHD);
        providers.setBMoviesfreeEnabled(Settings.ENABLE_BMOVIESFREE);
        providers.setPlayMoviesEsEnabled(Settings.ENABLE_PLAYMOVIES);
        providers.setOdbToEnabled(Settings.ENABLE_ODBTO);
        providers.setScreenCouchEnabled(Settings.ENABLE_SCREENCOUCH);
        providers.setCineBloomEnabled(Settings.ENABLE_CINEBLOOM);
        providers.setAzMoviesEnabled(Settings.ENABLE_AZMOVIES);
        providers.setHulu123Enabled(Settings.ENABLE_123HULU);
        providers.setXmovies8Enabled(Settings.ENABLE_XMOVIES8);
        providers.setMoviesfree123Enabled(Settings.ENABLE_123MOVIESFREE);
        providers.setMoviesHD123Enabled(Settings.ENABLE_123MOVIESHD);
        ResolverConfig resolvers = scraperConfig.getResolvers();
        resolvers.setEntervideoEnabled(Settings.ENABLE_ENTERVIDEO);
        resolvers.setGvideoEnabled(Settings.ENABLE_GVIDEO);
        resolvers.setLemonHLSEnabled(Settings.ENABLE_LEMONHLS);
        resolvers.setMcloudHLSEnabled(Settings.ENABLE_MCLOUDHLS);
        resolvers.setOpenloadEnabled(Settings.ENABLE_OPENLOAD);
        resolvers.setStreamangoEnabled(Settings.ENABLE_STREAMANGO);
        resolvers.setThevideoEnabled(Settings.ENABLE_THEVIDEO);
        resolvers.setStrmgoHLSEnabled(Settings.ENABLE_STRMGOHLS);
        resolvers.setVidozaEnabled(Settings.ENABLE_VIDOZA);
        resolvers.setVidupEnabled(Settings.ENABLE_VIDUP);
        resolvers.setOthersEnabled(Settings.ENABLE_OTHERS);
        final Scraper scraper = new Scraper(scraperConfig);
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final int i5 = i;
        final int i6 = i2;
        final FetchSourcesListener fetchSourcesListener2 = fetchSourcesListener;
        new Thread(new Runnable() {

            /* renamed from: com.android.morpheustv.sources.Fetcher$1$1 */
            class C05351 implements ScraperListener {
                C05351() {
                }

                public void onStart(ScraperMediaInfo scraperMediaInfo) {
                    Log.d(Fetcher.class.getSimpleName(), "onStart()");
                }

                public void onFinish(ScraperMediaInfo scraperMediaInfo, boolean z) {
                    Log.d(Fetcher.class.getSimpleName(), "onFinish()");
                    Fetcher.isBusy = null;
                    if (fetchSourcesListener2 != null) {
                        fetchSourcesListener2.onFinish();
                    }
                }

                public void onSourceFound(Source source) {
                    Fetcher.currentSources.add(source);
                    if (fetchSourcesListener2 != null) {
                        fetchSourcesListener2.onFetch(Fetcher.currentSources);
                    }
                    String simpleName = Fetcher.class.getSimpleName();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onSourceFound() ");
                    stringBuilder.append(source.toString());
                    Log.d(simpleName, stringBuilder.toString());
                }

                public void onStatusUpdated(ScrapingStatus scrapingStatus) {
                    String simpleName = Fetcher.class.getSimpleName();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onStatusUpdated() ");
                    stringBuilder.append(scrapingStatus.toString());
                    Log.d(simpleName, stringBuilder.toString());
                    if (fetchSourcesListener2 != null) {
                        fetchSourcesListener2.onProgress(scrapingStatus.abort_time);
                    }
                }
            }

            public void run() {
                scraper.startAsync(new ScraperMediaInfo(str4, str5, str6, i5, i6), new C05351());
                while (Fetcher.isBusy) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                scraper.stop();
                Fetcher.isBusy = false;
            }
        }).start();
    }

    public static void fetchSubtitles(String str, String str2, String str3, int i, int i2, FetchSubtitleListener fetchSubtitleListener) {
        currentSubtitleResults = new CopyOnWriteArrayList();
        isBusySubtites = true;
        final String str4 = str2;
        final String str5 = str3;
        final int i3 = i;
        final int i4 = i2;
        final String str6 = str;
        final FetchSubtitleListener fetchSubtitleListener2 = fetchSubtitleListener;
        new Thread(new Runnable() {
            public void run() {
                final ArrayList arrayList = new ArrayList();
                if (Settings.ENABLE_TVSUBS) {
                    arrayList.add(new SubtitleFetchThread(new TVSubs(), str4, str5, i3, i4));
                }
                if (Settings.ENABLE_OPENSUBTITLES) {
                    arrayList.add(new SubtitleFetchThread(new OpenSubtitles(), str6, str5, i3, i4));
                }
                if (Settings.ENABLE_LEGENDASDIVX) {
                    arrayList.add(new SubtitleFetchThread(new LegendasDivx(), str6, str5, i3, i4));
                }
                long currentTimeMillis = System.currentTimeMillis();
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        int i = 0;
                        while (Fetcher.isBusySubtites) {
                            try {
                                Iterator it = arrayList.iterator();
                                int i2 = 0;
                                while (it.hasNext()) {
                                    SubtitleFetchThread subtitleFetchThread = (SubtitleFetchThread) it.next();
                                    if (subtitleFetchThread.running) {
                                        i2++;
                                        if (System.currentTimeMillis() - subtitleFetchThread.timeStarted > ((long) Settings.THREAD_TIMEOUT)) {
                                            Log.d(subtitleFetchThread.provider.PROVIDER_NAME, "Stopping subtitle thread, time share ended...");
                                            subtitleFetchThread.stopThread();
                                            subtitleFetchThread.interrupt();
                                        }
                                    }
                                }
                                if (i2 < Settings.MAX_PROVIDERS) {
                                    it = arrayList.iterator();
                                    while (it.hasNext()) {
                                        SubtitleFetchThread subtitleFetchThread2 = (SubtitleFetchThread) it.next();
                                        if (!subtitleFetchThread2.started) {
                                            subtitleFetchThread2.started = true;
                                            subtitleFetchThread2.running = true;
                                            subtitleFetchThread2.timeStarted = System.currentTimeMillis();
                                            subtitleFetchThread2.start();
                                            break;
                                        }
                                    }
                                }
                                if (!(fetchSubtitleListener2 == null || Fetcher.currentSubtitleResults == null || Fetcher.currentSubtitleResults.size() <= r1)) {
                                    fetchSubtitleListener2.onFetch(Fetcher.currentSubtitleResults);
                                    i = Fetcher.currentSubtitleResults.size();
                                }
                                it = arrayList.iterator();
                                Object obj = null;
                                while (it.hasNext()) {
                                    SubtitleFetchThread subtitleFetchThread3 = (SubtitleFetchThread) it.next();
                                    if (subtitleFetchThread3.started && subtitleFetchThread3.running) {
                                        obj = 1;
                                    }
                                }
                                if (obj == null) {
                                    Fetcher.isBusySubtites = false;
                                }
                                Thread.sleep(100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        SubtitleFetchThread subtitleFetchThread = (SubtitleFetchThread) it.next();
                        try {
                            subtitleFetchThread.stopThread();
                            subtitleFetchThread.interrupt();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Fetcher.isBusySubtites = false;
                    if (fetchSubtitleListener2 != null) {
                        fetchSubtitleListener2.onFinish();
                    }
                    long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("All subtitle threads completed in ");
                    stringBuilder.append(String.valueOf(currentTimeMillis2));
                    stringBuilder.append("ms");
                    Log.d("ScraperThread", stringBuilder.toString());
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    public static void downloadSubtitles(final SubtitleResult subtitleResult, final DownloadSubtitleListener downloadSubtitleListener) {
        new Thread(new Runnable() {
            public void run() {
                BaseSubtitleProvider baseSubtitleProvider = null;
                try {
                    if (subtitleResult.provider.equals("OPENSUBTITLES")) {
                        baseSubtitleProvider = new OpenSubtitles();
                    }
                    if (subtitleResult.provider.equals("LEGENDASDIVX")) {
                        baseSubtitleProvider = new LegendasDivx();
                    }
                    if (subtitleResult.provider.equals("TVSUBS")) {
                        baseSubtitleProvider = new TVSubs();
                    }
                    if (baseSubtitleProvider != null) {
                        String downloadSubtitle = baseSubtitleProvider.downloadSubtitle(subtitleResult);
                        if (downloadSubtitle == null || downloadSubtitle.isEmpty()) {
                            downloadSubtitleListener.onFail();
                            return;
                        } else {
                            downloadSubtitleListener.onDownload(downloadSubtitle);
                            return;
                        }
                    }
                    downloadSubtitleListener.onFail();
                } catch (Exception e) {
                    e.printStackTrace();
                    downloadSubtitleListener.onFail();
                }
            }
        }).start();
    }
}
