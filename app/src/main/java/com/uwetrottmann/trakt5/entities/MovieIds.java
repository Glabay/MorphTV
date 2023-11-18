package com.uwetrottmann.trakt5.entities;

public class MovieIds extends BaseIds {
    public String slug;

    public static MovieIds trakt(int i) {
        MovieIds movieIds = new MovieIds();
        movieIds.trakt = Integer.valueOf(i);
        return movieIds;
    }

    public static MovieIds imdb(String str) {
        MovieIds movieIds = new MovieIds();
        movieIds.imdb = str;
        return movieIds;
    }

    public static MovieIds tmdb(int i) {
        MovieIds movieIds = new MovieIds();
        movieIds.tmdb = Integer.valueOf(i);
        return movieIds;
    }

    public static MovieIds slug(String str) {
        MovieIds movieIds = new MovieIds();
        movieIds.slug = str;
        return movieIds;
    }
}
