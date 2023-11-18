package com.uwetrottmann.trakt5.entities;

import com.uwetrottmann.trakt5.enums.Rating;
import org.threeten.bp.OffsetDateTime;

public class SyncEpisode {
    public OffsetDateTime collected_at;
    public EpisodeIds ids;
    public Integer number;
    public OffsetDateTime rated_at;
    public Rating rating;
    public Integer season;
    public OffsetDateTime watched_at;

    public SyncEpisode number(int i) {
        this.number = Integer.valueOf(i);
        return this;
    }

    public SyncEpisode season(int i) {
        this.season = Integer.valueOf(i);
        return this;
    }

    public SyncEpisode id(EpisodeIds episodeIds) {
        this.ids = episodeIds;
        return this;
    }

    public SyncEpisode collectedAt(OffsetDateTime offsetDateTime) {
        this.collected_at = offsetDateTime;
        return this;
    }

    public SyncEpisode watchedAt(OffsetDateTime offsetDateTime) {
        this.watched_at = offsetDateTime;
        return this;
    }

    public SyncEpisode ratedAt(OffsetDateTime offsetDateTime) {
        this.rated_at = offsetDateTime;
        return this;
    }

    public SyncEpisode rating(Rating rating) {
        this.rating = rating;
        return this;
    }
}
