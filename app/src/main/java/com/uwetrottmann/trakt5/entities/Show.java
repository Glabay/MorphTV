package com.uwetrottmann.trakt5.entities;

import com.uwetrottmann.trakt5.enums.Status;
import java.util.List;
import org.threeten.bp.OffsetDateTime;

public class Show extends BaseEntity {
    public Airs airs;
    public String certification;
    public String country;
    public OffsetDateTime first_aired;
    public List<String> genres;
    public String homepage;
    public ShowIds ids;
    public String language;
    public String network;
    public Integer runtime;
    public Status status;
    public String trailer;
    public Integer year;
}
