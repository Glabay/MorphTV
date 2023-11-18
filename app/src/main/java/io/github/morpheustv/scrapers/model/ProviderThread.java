package io.github.morpheustv.scrapers.model;

import io.github.morpheustv.scrapers.Scraper;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProviderThread extends Thread {
    private int episode;
    private String imdb;
    private boolean isTvShow = false;
    public BaseProvider provider;
    public boolean running = false;
    private Scraper scraper;
    private int season;
    private CopyOnWriteArrayList<Source> sources;
    public boolean started = false;
    public long timeStarted = 0;
    private List<String> titles;
    private String year;

    public ProviderThread(BaseProvider baseProvider, ScraperMediaInfo scraperMediaInfo, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        this.scraper = baseProvider.getScraper();
        this.titles = scraperMediaInfo.getAlternativeTitles();
        this.sources = copyOnWriteArrayList;
        this.season = scraperMediaInfo.getSeason();
        this.episode = scraperMediaInfo.getEpisode();
        this.provider = baseProvider;
        this.year = scraperMediaInfo.getYear();
        this.imdb = scraperMediaInfo.getImdb();
        baseProvider = (this.episode <= null || this.season <= null) ? null : true;
        this.isTvShow = baseProvider;
        this.provider.isTvShow = this.isTvShow;
        this.started = false;
        this.running = false;
    }

    public void stopThread() {
        this.scraper.Log(this.provider.PROVIDER_NAME, "Terminating thread!");
        try {
            this.running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.provider.finish();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            destroyWebview();
        } catch (Exception e22) {
            e22.printStackTrace();
        }
        try {
            interrupt();
        } catch (Exception e222) {
            e222.printStackTrace();
        }
    }

    public void destroyWebview() {
        try {
            if (this.provider.usesWebview) {
                this.scraper.Log(this.provider.PROVIDER_NAME, "Destroying webview...");
                this.scraper.getController().finish(this.provider);
            }
            this.provider.handlingRecaptcha = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            ProviderSearchResult episode;
            this.scraper.Log(this.provider.PROVIDER_NAME, "Starting fetch thread...");
            if (this.provider.usesWebview && this.scraper.getController() != null) {
                this.scraper.getController().addProvider(this.provider);
            }
            this.running = true;
            this.started = true;
            if (this.isTvShow) {
                episode = this.provider.getEpisode(this.provider.getTvShow(this.titles, this.year, this.imdb), this.titles, this.year, this.season, this.episode);
            } else {
                episode = this.provider.getMovie(this.titles, this.year, this.imdb);
            }
            if (episode != null) {
                this.provider.getSources(episode, this.sources);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopThread();
    }

    public String toString() {
        return this.provider.PROVIDER_NAME;
    }
}
