package com.uwetrottmann.trakt5.entities;

import org.threeten.bp.OffsetDateTime;

public class BaseEpisode {
    public OffsetDateTime collected_at;
    public Boolean completed;
    public OffsetDateTime last_watched_at;
    public Integer number;
    public Integer plays;
}
