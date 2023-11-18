package com.uwetrottmann.trakt5.entities;

import org.threeten.bp.OffsetDateTime;

public class Comment {
    public String comment;
    public OffsetDateTime created_at;
    public Episode episode;
    public Integer id;
    public Movie movie;
    public Integer parent_id;
    public Integer replies;
    public Boolean review;
    public Show show;
    public Boolean spoiler;
    public User user;

    public Comment(Movie movie, String str, boolean z, boolean z2) {
        this(str, z, z2);
        this.movie = movie;
    }

    public Comment(Show show, String str, boolean z, boolean z2) {
        this(str, z, z2);
        this.show = show;
    }

    public Comment(Episode episode, String str, boolean z, boolean z2) {
        this(str, z, z2);
        this.episode = episode;
    }

    public Comment(String str, boolean z, boolean z2) {
        this.comment = str;
        this.spoiler = Boolean.valueOf(z);
        this.review = Boolean.valueOf(z2);
    }
}
