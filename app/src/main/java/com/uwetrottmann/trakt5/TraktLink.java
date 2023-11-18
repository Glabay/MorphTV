package com.uwetrottmann.trakt5;

public class TraktLink {
    private static final String PATH_EPISODES = "/episodes/";
    private static final String PATH_SEASONS = "/seasons/";
    private static final String URL_COMMENT = "https://trakt.tv/comments/";
    private static final String URL_EPISODE = "https://trakt.tv/episodes/";
    private static final String URL_IMDB = "https://trakt.tv/search/imdb/";
    private static final String URL_MOVIE = "https://trakt.tv/movies/";
    private static final String URL_PERSON = "https://trakt.tv/people/";
    private static final String URL_SEASON = "https://trakt.tv/seasons/";
    private static final String URL_SHOW = "https://trakt.tv/shows/";
    private static final String URL_TMDB = "https://trakt.tv/search/tmdb/";
    private static final String URL_TVDB = "https://trakt.tv/search/tvdb/";
    private static final String URL_TVRAGE = "https://trakt.tv/search/tvrage/";

    public static String movie(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_MOVIE);
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public static String show(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_SHOW);
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public static String season(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_SEASON);
        stringBuilder.append(i);
        return stringBuilder.toString();
    }

    public static String season(int i, int i2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(show(String.valueOf(i)));
        stringBuilder.append(PATH_SEASONS);
        stringBuilder.append(i2);
        return stringBuilder.toString();
    }

    public static String episode(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_EPISODE);
        stringBuilder.append(i);
        return stringBuilder.toString();
    }

    public static String episode(int i, int i2, int i3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(show(String.valueOf(i)));
        stringBuilder.append(PATH_SEASONS);
        stringBuilder.append(i2);
        stringBuilder.append(PATH_EPISODES);
        stringBuilder.append(i3);
        return stringBuilder.toString();
    }

    public static String person(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_PERSON);
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public static String comment(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_COMMENT);
        stringBuilder.append(i);
        return stringBuilder.toString();
    }

    public static String imdb(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_IMDB);
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public static String tmdb(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_TMDB);
        stringBuilder.append(i);
        return stringBuilder.toString();
    }

    public static String tvdb(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_TVDB);
        stringBuilder.append(i);
        return stringBuilder.toString();
    }

    public static String tvrage(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_TVRAGE);
        stringBuilder.append(i);
        return stringBuilder.toString();
    }
}
