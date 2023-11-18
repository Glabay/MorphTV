package com.uwetrottmann.trakt5.entities;

import com.uwetrottmann.trakt5.enums.Rating;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;

public class SyncShow {
    public OffsetDateTime collected_at;
    public ShowIds ids;
    public OffsetDateTime rated_at;
    public Rating rating;
    public List<SyncSeason> seasons;
    public OffsetDateTime watched_at;

    public SyncShow id(ShowIds showIds) {
        this.ids = showIds;
        return this;
    }

    public SyncShow seasons(List<SyncSeason> list) {
        this.seasons = list;
        return this;
    }

    public SyncShow seasons(SyncSeason syncSeason) {
        List arrayList = new ArrayList(1);
        arrayList.add(syncSeason);
        return seasons(arrayList);
    }

    public SyncShow collectedAt(OffsetDateTime offsetDateTime) {
        this.collected_at = offsetDateTime;
        return this;
    }

    public SyncShow watchedAt(OffsetDateTime offsetDateTime) {
        this.watched_at = offsetDateTime;
        return this;
    }

    public SyncShow ratedAt(OffsetDateTime offsetDateTime) {
        this.rated_at = offsetDateTime;
        return this;
    }

    public SyncShow rating(Rating rating) {
        this.rating = rating;
        return this;
    }
}
