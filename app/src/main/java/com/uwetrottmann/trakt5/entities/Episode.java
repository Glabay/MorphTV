package com.uwetrottmann.trakt5.entities;

import org.threeten.bp.OffsetDateTime;

public class Episode extends BaseEntity {
    public OffsetDateTime first_aired;
    public EpisodeIds ids;
    public Integer number;
    public Integer number_abs;
    public Integer season;
}
