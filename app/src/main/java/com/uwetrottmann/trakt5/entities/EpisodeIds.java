package com.uwetrottmann.trakt5.entities;

public class EpisodeIds extends BaseIds {
    public Integer tvdb;
    public Integer tvrage;

    public static EpisodeIds trakt(int i) {
        EpisodeIds episodeIds = new EpisodeIds();
        episodeIds.trakt = Integer.valueOf(i);
        return episodeIds;
    }

    public static EpisodeIds imdb(String str) {
        EpisodeIds episodeIds = new EpisodeIds();
        episodeIds.imdb = str;
        return episodeIds;
    }

    public static EpisodeIds tmdb(int i) {
        EpisodeIds episodeIds = new EpisodeIds();
        episodeIds.tmdb = Integer.valueOf(i);
        return episodeIds;
    }

    public static EpisodeIds tvdb(int i) {
        EpisodeIds episodeIds = new EpisodeIds();
        episodeIds.tvdb = Integer.valueOf(i);
        return episodeIds;
    }

    public static EpisodeIds tvrage(int i) {
        EpisodeIds episodeIds = new EpisodeIds();
        episodeIds.tvrage = Integer.valueOf(i);
        return episodeIds;
    }
}
