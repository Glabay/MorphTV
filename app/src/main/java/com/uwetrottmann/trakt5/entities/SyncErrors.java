package com.uwetrottmann.trakt5.entities;

import java.util.List;

public class SyncErrors {
    public List<SyncEpisode> episodes;
    public List<Integer> ids;
    public List<SyncMovie> movies;
    public List<SyncSeason> seasons;
    public List<SyncShow> shows;
}
