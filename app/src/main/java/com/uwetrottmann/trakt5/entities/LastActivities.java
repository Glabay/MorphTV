package com.uwetrottmann.trakt5.entities;

import org.threeten.bp.OffsetDateTime;

public class LastActivities {
    public OffsetDateTime all;
    public LastActivityMore episodes;
    public LastActivityMore movies;
    public LastActivity seasons;
    public LastActivity shows;
}
