package com.uwetrottmann.trakt5.entities;

import java.util.ArrayList;
import java.util.List;

public class SyncItems {
    public List<SyncEpisode> episodes;
    public List<Integer> ids;
    public List<SyncMovie> movies;
    public List<SyncShow> shows;

    public SyncItems movies(SyncMovie syncMovie) {
        List arrayList = new ArrayList(1);
        arrayList.add(syncMovie);
        return movies(arrayList);
    }

    public SyncItems movies(List<SyncMovie> list) {
        this.movies = list;
        return this;
    }

    public SyncItems shows(SyncShow syncShow) {
        List arrayList = new ArrayList(1);
        arrayList.add(syncShow);
        return shows(arrayList);
    }

    public SyncItems shows(List<SyncShow> list) {
        this.shows = list;
        return this;
    }

    public SyncItems episodes(SyncEpisode syncEpisode) {
        List arrayList = new ArrayList(1);
        arrayList.add(syncEpisode);
        return episodes(arrayList);
    }

    public SyncItems episodes(List<SyncEpisode> list) {
        this.episodes = list;
        return this;
    }

    public SyncItems ids(int i) {
        List arrayList = new ArrayList(1);
        arrayList.add(Integer.valueOf(i));
        return ids(arrayList);
    }

    public SyncItems ids(List<Integer> list) {
        this.ids = list;
        return this;
    }
}
