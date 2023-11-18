package io.github.morpheustv.scrapers.model;

import java.util.List;

public class ScraperMediaInfo {
    private List<String> alternativeTitles;
    private int episode = 0;
    private String imdb = "";
    private int season = 0;
    private String title = "";
    private String year = "";

    public ScraperMediaInfo(String str, String str2, String str3) {
        this.title = str;
        this.year = str2;
        this.imdb = str3;
    }

    public ScraperMediaInfo(String str, String str2, String str3, int i, int i2) {
        this.title = str;
        this.year = str2;
        this.imdb = str3;
        this.season = i;
        this.episode = i2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String str) {
        this.year = str;
    }

    public String getImdb() {
        return this.imdb;
    }

    public void setImdb(String str) {
        this.imdb = str;
    }

    public int getSeason() {
        return this.season;
    }

    public void setSeason(int i) {
        this.season = i;
    }

    public int getEpisode() {
        return this.episode;
    }

    public void setEpisode(int i) {
        this.episode = i;
    }

    public List<String> getAlternativeTitles() {
        return this.alternativeTitles;
    }

    public void setAlternativeTitles(List<String> list) {
        this.alternativeTitles = list;
    }
}
