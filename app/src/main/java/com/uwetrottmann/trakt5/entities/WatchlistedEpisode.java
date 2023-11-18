package com.uwetrottmann.trakt5.entities;

import org.threeten.bp.OffsetDateTime;

public class WatchlistedEpisode {
    public Episode episode;
    public OffsetDateTime listed_at;
    public Show show;
}
