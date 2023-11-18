package io.github.morpheustv.scrapers.model;

import java.util.List;

public class ProviderSearchResult {
    private String content;
    private String cookie;
    private int episode = 0;
    private String imdb;
    private String pageUrl;
    private String referer;
    private int season = 0;
    private String title;
    private List<String> titles;
    private String year;

    public String getTitle() {
        if (this.season <= 0 || this.episode <= 0) {
            return this.title;
        }
        return String.format("%s S%02dE%02d", new Object[]{this.title, Integer.valueOf(this.season), Integer.valueOf(this.episode)});
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

    public String getPageUrl() {
        return this.pageUrl;
    }

    public void setPageUrl(String str) {
        this.pageUrl = str;
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

    public String getCookie() {
        return this.cookie;
    }

    public void setCookie(String str) {
        this.cookie = str;
    }

    public String getImdb() {
        return this.imdb;
    }

    public void setImdb(String str) {
        this.imdb = str;
    }

    public List<String> getTitles() {
        return this.titles;
    }

    public void setTitles(List<String> list) {
        this.titles = list;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public String getReferer() {
        return this.referer;
    }

    public void setReferer(String str) {
        this.referer = str;
    }
}
