package com.uwetrottmann.trakt5.entities;

import org.threeten.bp.OffsetDateTime;

public abstract class BaseCheckinResponse {
    public ShareSettings sharing;
    public OffsetDateTime watched_at;
}
