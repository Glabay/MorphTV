package io.github.morpheustv.scrapers.model;

import android.app.Activity;

public class ScraperConfig {
    private long TorrentMaxSizeMovies = 0;
    private long TorrentMaxSizeShows = 0;
    private boolean TorrentsEnabled = false;
    private int globalScraperTimeout = 120000;
    private int individualScraperTimeout = 40000;
    private int maxSourcesProvider = 5;
    private int maxThreads = 4;
    private ProviderConfig providers;
    private ResolverConfig resolvers;
    private Activity scraperActivity;
    private boolean verifySources = false;
    private int verifySourcesTimeout = 8000;

    public ScraperConfig(Activity activity) {
        this.scraperActivity = activity;
        this.resolvers = new ResolverConfig();
        this.providers = new ProviderConfig();
    }

    public Activity getContext() {
        return this.scraperActivity;
    }

    public int getMaxSourcesProvider() {
        return this.maxSourcesProvider;
    }

    public void setMaxSourcesProvider(int i) {
        this.maxSourcesProvider = i;
    }

    public boolean shouldVerifySources() {
        return this.verifySources;
    }

    public void setVerifySources(boolean z) {
        this.verifySources = z;
    }

    public int getVerifySourcesTimeout() {
        return this.verifySourcesTimeout;
    }

    public void setVerifySourcesTimeout(int i) {
        this.verifySourcesTimeout = i;
    }

    public long getTorrentMaxSizeMovies() {
        return this.TorrentMaxSizeMovies;
    }

    public void setTorrentMaxSizeMovies(long j) {
        this.TorrentMaxSizeMovies = j;
    }

    public long getTorrentMaxSizeShows() {
        return this.TorrentMaxSizeShows;
    }

    public void setTorrentMaxSizeShows(long j) {
        this.TorrentMaxSizeShows = j;
    }

    public int getIndividualScraperTimeout() {
        return this.individualScraperTimeout;
    }

    public void setIndividualScraperTimeout(int i) {
        this.individualScraperTimeout = i;
    }

    public int getMaxThreads() {
        return this.maxThreads;
    }

    public void setMaxThreads(int i) {
        this.maxThreads = i;
    }

    public ResolverConfig getResolvers() {
        return this.resolvers;
    }

    public void setResolvers(ResolverConfig resolverConfig) {
        this.resolvers = resolverConfig;
    }

    public ProviderConfig getProviders() {
        return this.providers;
    }

    public void setProviders(ProviderConfig providerConfig) {
        this.providers = providerConfig;
    }

    public int getGlobalScraperTimeout() {
        return this.globalScraperTimeout;
    }

    public void setGlobalScraperTimeout(int i) {
        this.globalScraperTimeout = i;
    }

    public boolean isTorrentsEnabled() {
        return this.TorrentsEnabled;
    }

    public void setTorrentsEnabled(boolean z) {
        this.TorrentsEnabled = z;
    }
}
