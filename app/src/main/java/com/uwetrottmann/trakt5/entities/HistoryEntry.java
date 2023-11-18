package com.uwetrottmann.trakt5.entities;

import org.threeten.bp.OffsetDateTime;

public class HistoryEntry {
    public String action;
    public Episode episode;
    public Long id;
    public Movie movie;
    public Show show;
    public String type;
    public OffsetDateTime watched_at;
}
