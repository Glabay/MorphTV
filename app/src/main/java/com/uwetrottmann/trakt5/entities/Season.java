package com.uwetrottmann.trakt5.entities;

import java.util.List;

public class Season {
    public Integer aired_episodes;
    public Integer episode_count;
    public List<Episode> episodes;
    public SeasonIds ids;
    public Integer number;
    public String overview;
    public Double rating;
    public Integer votes;
}
