package com.uwetrottmann.trakt5.entities;

import com.uwetrottmann.trakt5.enums.Rating;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;

public class SyncSeason {
    public OffsetDateTime collected_at;
    public List<SyncEpisode> episodes;
    public Integer number;
    public OffsetDateTime rated_at;
    public Rating rating;
    public OffsetDateTime watched_at;

    public SyncSeason number(int i) {
        this.number = Integer.valueOf(i);
        return this;
    }

    public SyncSeason episodes(List<SyncEpisode> list) {
        this.episodes = list;
        return this;
    }

    public SyncSeason episodes(SyncEpisode syncEpisode) {
        List arrayList = new ArrayList(1);
        arrayList.add(syncEpisode);
        return episodes(arrayList);
    }

    public SyncSeason collectedAt(OffsetDateTime offsetDateTime) {
        this.collected_at = offsetDateTime;
        return this;
    }

    public SyncSeason watchedAt(OffsetDateTime offsetDateTime) {
        this.watched_at = offsetDateTime;
        return this;
    }

    public SyncSeason ratedAt(OffsetDateTime offsetDateTime) {
        this.rated_at = offsetDateTime;
        return this;
    }

    public SyncSeason rating(Rating rating) {
        this.rating = rating;
        return this;
    }
}
