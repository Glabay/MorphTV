package com.uwetrottmann.trakt5.entities;

import java.util.List;
import org.threeten.bp.LocalDate;

public class Movie extends BaseEntity {
    public String certification;
    public List<String> genres;
    public String homepage;
    public MovieIds ids;
    public String language;
    public LocalDate released;
    public Integer runtime;
    public String tagline;
    public String trailer;
    public Integer year;
}
