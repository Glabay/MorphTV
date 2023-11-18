package io.github.morpheustv.scrapers;

import android.app.Activity;
import android.util.Log;
import io.github.morpheustv.scrapers.controller.WebviewController;
import io.github.morpheustv.scrapers.helper.ScraperListener;
import io.github.morpheustv.scrapers.helper.TaskManager;
import io.github.morpheustv.scrapers.helper.Tmdb;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.ProviderConfig;
import io.github.morpheustv.scrapers.model.ProviderThread;
import io.github.morpheustv.scrapers.model.ScraperConfig;
import io.github.morpheustv.scrapers.model.ScraperMediaInfo;
import io.github.morpheustv.scrapers.model.ScrapingStatus;
import io.github.morpheustv.scrapers.model.Source;
import io.github.morpheustv.scrapers.providers.AZMoviesProvider;
import io.github.morpheustv.scrapers.providers.AfdahProvider;
import io.github.morpheustv.scrapers.providers.BMoviesFreeProvider;
import io.github.morpheustv.scrapers.providers.CMoviesHDProvider;
import io.github.morpheustv.scrapers.providers.CartoonHDProvider;
import io.github.morpheustv.scrapers.providers.CineBloomProvider;
import io.github.morpheustv.scrapers.providers.EZTVProvider;
import io.github.morpheustv.scrapers.providers.FMoviesProvider;
import io.github.morpheustv.scrapers.providers.FlixanityProvider;
import io.github.morpheustv.scrapers.providers.GoMoviesProvider;
import io.github.morpheustv.scrapers.providers.HdGoProvider;
import io.github.morpheustv.scrapers.providers.Hulu123Provider;
import io.github.morpheustv.scrapers.providers.IceFilmsProvider;
import io.github.morpheustv.scrapers.providers.MehlizMoviesHDProvider;
import io.github.morpheustv.scrapers.providers.Movie4KProvider;
import io.github.morpheustv.scrapers.providers.MoviesFree123Provider;
import io.github.morpheustv.scrapers.providers.MoviesHD123Provider;
import io.github.morpheustv.scrapers.providers.MoviesHub123Provider;
import io.github.morpheustv.scrapers.providers.OdbToProvider;
import io.github.morpheustv.scrapers.providers.PlayMoviesProvider;
import io.github.morpheustv.scrapers.providers.PutlockerSkProvider;
import io.github.morpheustv.scrapers.providers.PutlockersMovieProvider;
import io.github.morpheustv.scrapers.providers.Rarbg123Provider;
import io.github.morpheustv.scrapers.providers.RarbgProvider;
import io.github.morpheustv.scrapers.providers.RedditProvider;
import io.github.morpheustv.scrapers.providers.ScreenCouchIsProvider;
import io.github.morpheustv.scrapers.providers.Series9Provider;
import io.github.morpheustv.scrapers.providers.SeriesFreeProvider;
import io.github.morpheustv.scrapers.providers.SolarMovieProvider;
import io.github.morpheustv.scrapers.providers.StreamlordProvider;
import io.github.morpheustv.scrapers.providers.TV21Provider;
import io.github.morpheustv.scrapers.providers.WatchSeriesMoviesProvider;
import io.github.morpheustv.scrapers.providers.WatchonlineALProvider;
import io.github.morpheustv.scrapers.providers.XMovies8Provider;
import io.github.morpheustv.scrapers.providers.XWatchSeriesProvider;
import io.github.morpheustv.scrapers.providers.YesMovieIoProvider;
import io.github.morpheustv.scrapers.providers.YesMoviesProvider;
import io.github.morpheustv.scrapers.providers.YesMoviesScProvider;
import io.github.morpheustv.scrapers.providers.Yts123Provider;
import io.github.morpheustv.scrapers.providers.YtsAgProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Scraper {
    private boolean isBusy = false;
    private ScraperConfig mScraperConfig;
    private ScraperListener mScraperListener;
    private CopyOnWriteArrayList<Source> sourceList;
    private WebviewController webviewController;

    /* renamed from: io.github.morpheustv.scrapers.Scraper$2 */
    class C13722 implements Comparator<ProviderThread> {
        C13722() {
        }

        public int compare(ProviderThread providerThread, ProviderThread providerThread2) {
            if (providerThread2.provider.usesWebview == providerThread.provider.usesWebview) {
                return null;
            }
            return providerThread.provider.usesWebview != null ? 1 : -1;
        }
    }

    public void Log(String str, String str2) {
        Log.d(str, str2);
    }

    public Scraper(ScraperConfig scraperConfig) {
        this.mScraperConfig = scraperConfig;
    }

    public void startAsync(final ScraperMediaInfo scraperMediaInfo, final ScraperListener scraperListener) {
        new Thread(new Runnable() {
            public void run() {
                Scraper.this.startSync(scraperMediaInfo, scraperListener);
            }
        }).start();
    }

    public void startSync(final ScraperMediaInfo scraperMediaInfo, ScraperListener scraperListener) {
        if (!this.isBusy) {
            boolean z = true;
            setBusy(true);
            this.mScraperListener = scraperListener;
            this.sourceList = new CopyOnWriteArrayList();
            initializeScraping();
            if (scraperMediaInfo.getAlternativeTitles() == null || scraperMediaInfo.getAlternativeTitles().size() == null) {
                scraperListener = scraperMediaInfo.getTitle();
                String imdb = scraperMediaInfo.getImdb();
                String year = scraperMediaInfo.getYear();
                if (scraperMediaInfo.getSeason() != 0 || scraperMediaInfo.getEpisode() != 0) {
                    z = false;
                }
                scraperListener = Tmdb.getAlternativeTitles(scraperListener, imdb, year, z);
                List arrayList = new ArrayList();
                arrayList.addAll(scraperListener);
                scraperMediaInfo.setAlternativeTitles(arrayList);
            }
            scraperListener = new ArrayList();
            ProviderConfig providers = getConfig().getProviders();
            if (providers.isAfdahEnabled()) {
                scraperListener.add(new ProviderThread(new AfdahProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isBMoviesfreeEnabled()) {
                scraperListener.add(new ProviderThread(new BMoviesFreeProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isCartoonHDEnabled()) {
                scraperListener.add(new ProviderThread(new CartoonHDProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isCMoviesHDEnabled()) {
                scraperListener.add(new ProviderThread(new CMoviesHDProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isFlixanityEnabled()) {
                scraperListener.add(new ProviderThread(new FlixanityProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isFMoviesEnabled()) {
                scraperListener.add(new ProviderThread(new FMoviesProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isGoMoviesEnabled()) {
                scraperListener.add(new ProviderThread(new GoMoviesProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isHdgoEnabled()) {
                scraperListener.add(new ProviderThread(new HdGoProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isIcefilmsEnabled()) {
                scraperListener.add(new ProviderThread(new IceFilmsProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isMehlizmoviesEnabled()) {
                scraperListener.add(new ProviderThread(new MehlizMoviesHDProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isMoviesHub123Enabled()) {
                scraperListener.add(new ProviderThread(new MoviesHub123Provider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isOnionPlayEnabled()) {
                scraperListener.add(new ProviderThread(new WatchonlineALProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isPutlockerSkEnabled()) {
                scraperListener.add(new ProviderThread(new PutlockerSkProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isPutlockersMovieEnabled()) {
                scraperListener.add(new ProviderThread(new PutlockersMovieProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isRarbg123Enabled()) {
                scraperListener.add(new ProviderThread(new Rarbg123Provider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isSeries9Enabled()) {
                scraperListener.add(new ProviderThread(new Series9Provider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isSolarMovieEnabled()) {
                scraperListener.add(new ProviderThread(new SolarMovieProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isWatchonlineSCEnabled()) {
                scraperListener.add(new ProviderThread(new YesMoviesScProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isStreamlordEnabled()) {
                scraperListener.add(new ProviderThread(new StreamlordProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isWatchSeriesMoviesEnabled()) {
                scraperListener.add(new ProviderThread(new WatchSeriesMoviesProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isYesMovieIOEnabled()) {
                scraperListener.add(new ProviderThread(new YesMovieIoProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isYesMoviesEnabled()) {
                scraperListener.add(new ProviderThread(new YesMoviesProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isYts123Enabled()) {
                scraperListener.add(new ProviderThread(new Yts123Provider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isPlayMoviesEsEnabled()) {
                scraperListener.add(new ProviderThread(new PlayMoviesProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isOdbToEnabled()) {
                scraperListener.add(new ProviderThread(new OdbToProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isScreenCouchEnabled()) {
                scraperListener.add(new ProviderThread(new ScreenCouchIsProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isCineBloomEnabled()) {
                scraperListener.add(new ProviderThread(new CineBloomProvider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isHulu123Enabled()) {
                scraperListener.add(new ProviderThread(new Hulu123Provider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isXmovies8Enabled()) {
                scraperListener.add(new ProviderThread(new XMovies8Provider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isMoviesfree123Enabled()) {
                scraperListener.add(new ProviderThread(new MoviesFree123Provider(this), scraperMediaInfo, this.sourceList));
            }
            if (providers.isMoviesHD123Enabled()) {
                scraperListener.add(new ProviderThread(new MoviesHD123Provider(this), scraperMediaInfo, this.sourceList));
            }
            if (scraperMediaInfo.getSeason() == 0 && scraperMediaInfo.getEpisode() == 0) {
                if (providers.isMovie4KEnabled()) {
                    scraperListener.add(new ProviderThread(new Movie4KProvider(this), scraperMediaInfo, this.sourceList));
                }
                if (providers.isTV21Enabled()) {
                    scraperListener.add(new ProviderThread(new TV21Provider(this), scraperMediaInfo, this.sourceList));
                }
                if (providers.isRedditEnabled()) {
                    scraperListener.add(new ProviderThread(new RedditProvider(this), scraperMediaInfo, this.sourceList));
                }
                if (providers.isAzMoviesEnabled()) {
                    scraperListener.add(new ProviderThread(new AZMoviesProvider(this), scraperMediaInfo, this.sourceList));
                }
            }
            if (scraperMediaInfo.getSeason() > 0 && scraperMediaInfo.getEpisode() > 0) {
                if (providers.isSeriesFreeEnabled()) {
                    scraperListener.add(new ProviderThread(new SeriesFreeProvider(this), scraperMediaInfo, this.sourceList));
                }
                if (providers.isXWatchSeriesEnabled()) {
                    scraperListener.add(new ProviderThread(new XWatchSeriesProvider(this), scraperMediaInfo, this.sourceList));
                }
            }
            if (getConfig().isTorrentsEnabled()) {
                if (providers.isEZTVEnabled()) {
                    scraperListener.add(new ProviderThread(new EZTVProvider(this), scraperMediaInfo, this.sourceList));
                }
                if (providers.isRarbgEnabled()) {
                    scraperListener.add(new ProviderThread(new RarbgProvider(this), scraperMediaInfo, this.sourceList));
                }
                if (providers.isYtsAgEnabled()) {
                    scraperListener.add(new ProviderThread(new YtsAgProvider(this), scraperMediaInfo, this.sourceList));
                }
            }
            Collections.shuffle(scraperListener);
            Collections.sort(scraperListener, new C13722());
            if (scraperListener.size() == 0) {
                setBusy(false);
                if (getScraperListener() != null) {
                    getContext().runOnUiThread(new Runnable() {
                        public void run() {
                            Scraper.this.getScraperListener().onFinish(scraperMediaInfo, false);
                        }
                    });
                }
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            final long individualScraperTimeout = (long) getConfig().getIndividualScraperTimeout();
            final long j = currentTimeMillis;
            final ScraperMediaInfo scraperMediaInfo2 = scraperMediaInfo;
            final ScraperListener scraperListener2 = scraperListener;
            Thread thread = new Thread(new Runnable() {

                /* renamed from: io.github.morpheustv.scrapers.Scraper$4$1 */
                class C13741 implements Runnable {
                    C13741() {
                    }

                    public void run() {
                        Scraper.this.getScraperListener().onStart(scraperMediaInfo2);
                    }
                }

                public void run() {
                    TaskManager.setExecutors(Scraper.this.getConfig().getMaxThreads());
                    BaseResolver.runningResolvers = 0;
                    long globalScraperTimeout = Scraper.this.getConfig().getGlobalScraperTimeout() > 0 ? j + ((long) Scraper.this.getConfig().getGlobalScraperTimeout()) : 0;
                    if (Scraper.this.getScraperListener() != null) {
                        Scraper.this.getContext().runOnUiThread(new C13741());
                    }
                    while (true) {
                        if ((globalScraperTimeout == 0 || System.currentTimeMillis() < globalScraperTimeout) && Scraper.this.isBusy()) {
                            try {
                                ProviderThread providerThread;
                                Object obj;
                                final ScrapingStatus scrapingStatus;
                                Iterator it;
                                String quality;
                                int hashCode;
                                Object obj2;
                                Iterator it2 = scraperListener2.iterator();
                                int i = 0;
                                while (it2.hasNext()) {
                                    ProviderThread providerThread2 = (ProviderThread) it2.next();
                                    if (providerThread2.running) {
                                        i++;
                                        if (individualScraperTimeout > 0 && System.currentTimeMillis() - providerThread2.timeStarted > individualScraperTimeout) {
                                            Scraper.this.Log(providerThread2.provider.PROVIDER_NAME, "Stopping thread, time share ended...");
                                            providerThread2.stopThread();
                                        }
                                    }
                                }
                                if (i < Scraper.this.getConfig().getMaxThreads()) {
                                    it2 = scraperListener2.iterator();
                                    while (it2.hasNext()) {
                                        providerThread = (ProviderThread) it2.next();
                                        if (!providerThread.started) {
                                            providerThread.started = true;
                                            providerThread.running = true;
                                            providerThread.timeStarted = System.currentTimeMillis();
                                            providerThread.start();
                                        }
                                    }
                                }
                                it2 = scraperListener2.iterator();
                                while (it2.hasNext()) {
                                    providerThread = (ProviderThread) it2.next();
                                    if (providerThread.started && providerThread.running) {
                                        obj = 1;
                                        if (obj == null || BaseResolver.runningResolvers != 0 || BaseProvider.runningVerifiers != 0 || TaskManager.anyTaskRunning()) {
                                            Thread.sleep(1000);
                                            if (Scraper.this.getScraperListener() != null) {
                                                scrapingStatus = new ScrapingStatus();
                                                it = Scraper.this.sourceList.iterator();
                                                while (it.hasNext()) {
                                                    quality = ((Source) it.next()).getQuality();
                                                    hashCode = quality.hashCode();
                                                    if (hashCode != 2300) {
                                                        if (quality.equals(BaseProvider.HD)) {
                                                            obj2 = 1;
                                                            switch (obj2) {
                                                                case null:
                                                                    scrapingStatus.fullhd_sources++;
                                                                    break;
                                                                case 1:
                                                                    scrapingStatus.hd_sources++;
                                                                    break;
                                                                case 2:
                                                                    scrapingStatus.sd_sources++;
                                                                    break;
                                                                case 3:
                                                                    scrapingStatus.ukn_sources++;
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                        }
                                                    } else if (hashCode != 2641) {
                                                        if (quality.equals(BaseProvider.SD)) {
                                                            obj2 = 2;
                                                            switch (obj2) {
                                                                case null:
                                                                    scrapingStatus.fullhd_sources++;
                                                                    break;
                                                                case 1:
                                                                    scrapingStatus.hd_sources++;
                                                                    break;
                                                                case 2:
                                                                    scrapingStatus.sd_sources++;
                                                                    break;
                                                                case 3:
                                                                    scrapingStatus.ukn_sources++;
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                        }
                                                    } else if (hashCode != 46737913) {
                                                        if (quality.equals(BaseProvider.FULLHD)) {
                                                            obj2 = null;
                                                            switch (obj2) {
                                                                case null:
                                                                    scrapingStatus.fullhd_sources++;
                                                                    break;
                                                                case 1:
                                                                    scrapingStatus.hd_sources++;
                                                                    break;
                                                                case 2:
                                                                    scrapingStatus.sd_sources++;
                                                                    break;
                                                                case 3:
                                                                    scrapingStatus.ukn_sources++;
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                        }
                                                    } else if (hashCode != 433141802) {
                                                        if (quality.equals(BaseProvider.UNKNOWN_QUALITY)) {
                                                            obj2 = 3;
                                                            switch (obj2) {
                                                                case null:
                                                                    scrapingStatus.fullhd_sources++;
                                                                    break;
                                                                case 1:
                                                                    scrapingStatus.hd_sources++;
                                                                    break;
                                                                case 2:
                                                                    scrapingStatus.sd_sources++;
                                                                    break;
                                                                case 3:
                                                                    scrapingStatus.ukn_sources++;
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                        }
                                                    }
                                                    obj2 = -1;
                                                    switch (obj2) {
                                                        case null:
                                                            scrapingStatus.fullhd_sources++;
                                                            break;
                                                        case 1:
                                                            scrapingStatus.hd_sources++;
                                                            break;
                                                        case 2:
                                                            scrapingStatus.sd_sources++;
                                                            break;
                                                        case 3:
                                                            scrapingStatus.ukn_sources++;
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                }
                                                scrapingStatus.total_sources = Scraper.this.sourceList.size();
                                                scrapingStatus.started_time = j;
                                                scrapingStatus.elapsed_time = System.currentTimeMillis() - j;
                                                scrapingStatus.abort_time = globalScraperTimeout;
                                                Scraper.this.getContext().runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Scraper.this.getScraperListener().onStatusUpdated(scrapingStatus);
                                                    }
                                                });
                                            }
                                        } else {
                                            Scraper.this.Log("SCRAPER", "Aborting scraper, all providers finished and none is resolving...");
                                            Scraper.this.setBusy(false);
                                        }
                                    }
                                }
                                obj = null;
                                if (obj == null) {
                                }
                                Thread.sleep(1000);
                                if (Scraper.this.getScraperListener() != null) {
                                    scrapingStatus = new ScrapingStatus();
                                    it = Scraper.this.sourceList.iterator();
                                    while (it.hasNext()) {
                                        quality = ((Source) it.next()).getQuality();
                                        hashCode = quality.hashCode();
                                        if (hashCode != 2300) {
                                            if (quality.equals(BaseProvider.HD)) {
                                                obj2 = 1;
                                                switch (obj2) {
                                                    case null:
                                                        scrapingStatus.fullhd_sources++;
                                                        break;
                                                    case 1:
                                                        scrapingStatus.hd_sources++;
                                                        break;
                                                    case 2:
                                                        scrapingStatus.sd_sources++;
                                                        break;
                                                    case 3:
                                                        scrapingStatus.ukn_sources++;
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        } else if (hashCode != 2641) {
                                            if (quality.equals(BaseProvider.SD)) {
                                                obj2 = 2;
                                                switch (obj2) {
                                                    case null:
                                                        scrapingStatus.fullhd_sources++;
                                                        break;
                                                    case 1:
                                                        scrapingStatus.hd_sources++;
                                                        break;
                                                    case 2:
                                                        scrapingStatus.sd_sources++;
                                                        break;
                                                    case 3:
                                                        scrapingStatus.ukn_sources++;
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        } else if (hashCode != 46737913) {
                                            if (quality.equals(BaseProvider.FULLHD)) {
                                                obj2 = null;
                                                switch (obj2) {
                                                    case null:
                                                        scrapingStatus.fullhd_sources++;
                                                        break;
                                                    case 1:
                                                        scrapingStatus.hd_sources++;
                                                        break;
                                                    case 2:
                                                        scrapingStatus.sd_sources++;
                                                        break;
                                                    case 3:
                                                        scrapingStatus.ukn_sources++;
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        } else if (hashCode != 433141802) {
                                            if (quality.equals(BaseProvider.UNKNOWN_QUALITY)) {
                                                obj2 = 3;
                                                switch (obj2) {
                                                    case null:
                                                        scrapingStatus.fullhd_sources++;
                                                        break;
                                                    case 1:
                                                        scrapingStatus.hd_sources++;
                                                        break;
                                                    case 2:
                                                        scrapingStatus.sd_sources++;
                                                        break;
                                                    case 3:
                                                        scrapingStatus.ukn_sources++;
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        }
                                        obj2 = -1;
                                        switch (obj2) {
                                            case null:
                                                scrapingStatus.fullhd_sources++;
                                                break;
                                            case 1:
                                                scrapingStatus.hd_sources++;
                                                break;
                                            case 2:
                                                scrapingStatus.sd_sources++;
                                                break;
                                            case 3:
                                                scrapingStatus.ukn_sources++;
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    scrapingStatus.total_sources = Scraper.this.sourceList.size();
                                    scrapingStatus.started_time = j;
                                    scrapingStatus.elapsed_time = System.currentTimeMillis() - j;
                                    scrapingStatus.abort_time = globalScraperTimeout;
                                    Scraper.this.getContext().runOnUiThread(/* anonymous class already generated */);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Iterator it3 = scraperListener2.iterator();
                        while (it3.hasNext()) {
                            try {
                                ((ProviderThread) it3.next()).stopThread();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                        return;
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scraperListener = scraperListener.iterator();
            while (scraperListener.hasNext()) {
                try {
                    ((ProviderThread) scraperListener.next()).stopThread();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            TaskManager.clearAllResolvers();
            j = System.currentTimeMillis() - currentTimeMillis;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("All threads completed in ");
            stringBuilder.append(String.valueOf(j));
            stringBuilder.append("ms");
            Log("ProviderThread", stringBuilder.toString());
            if (getScraperListener() != null) {
                getContext().runOnUiThread(new Runnable() {
                    public void run() {
                        Scraper.this.getScraperListener().onFinish(scraperMediaInfo, false);
                    }
                });
            }
            setBusy(false);
        }
    }

    public void stop() {
        if (isBusy()) {
            setBusy(false);
        }
    }

    public ScraperListener getScraperListener() {
        return this.mScraperListener;
    }

    public ScraperConfig getConfig() {
        return this.mScraperConfig;
    }

    public Activity getContext() {
        return this.mScraperConfig.getContext();
    }

    public boolean isBusy() {
        return this.isBusy;
    }

    private void setBusy(boolean z) {
        this.isBusy = z;
    }

    public CopyOnWriteArrayList<Source> getSourceList() {
        return this.sourceList;
    }

    private void initializeScraping() {
        this.webviewController = new WebviewController(this);
    }

    public WebviewController getController() {
        return this.webviewController;
    }
}
