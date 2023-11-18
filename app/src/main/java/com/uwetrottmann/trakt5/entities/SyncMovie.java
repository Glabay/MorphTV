package com.uwetrottmann.trakt5.entities;

import com.uwetrottmann.trakt5.enums.Rating;
import org.threeten.bp.OffsetDateTime;

public class SyncMovie {
    public OffsetDateTime collected_at;
    public MovieIds ids;
    public OffsetDateTime rated_at;
    public Rating rating;
    public OffsetDateTime watched_at;

    public SyncMovie id(MovieIds movieIds) {
        this.ids = movieIds;
        return this;
    }

    public SyncMovie collectedAt(OffsetDateTime offsetDateTime) {
        this.collected_at = offsetDateTime;
        return this;
    }

    public SyncMovie watchedAt(OffsetDateTime offsetDateTime) {
        this.watched_at = offsetDateTime;
        return this;
    }

    public SyncMovie ratedAt(OffsetDateTime offsetDateTime) {
        this.rated_at = offsetDateTime;
        return this;
    }

    public SyncMovie rating(Rating rating) {
        this.rating = rating;
        return this;
    }
}
