package com.uwetrottmann.trakt5.entities;

import java.util.List;
import org.threeten.bp.OffsetDateTime;

public abstract class BaseEntity {
    public List<String> available_translations;
    public String overview;
    public Double rating;
    public String title;
    public OffsetDateTime updated_at;
    public Integer votes;
}
