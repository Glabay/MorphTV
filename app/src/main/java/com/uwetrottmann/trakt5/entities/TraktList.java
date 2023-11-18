package com.uwetrottmann.trakt5.entities;

import com.uwetrottmann.trakt5.enums.ListPrivacy;
import org.threeten.bp.OffsetDateTime;

public class TraktList {
    public Boolean allow_comments;
    public Integer comment_count;
    public String description;
    public Boolean display_numbers;
    public ListIds ids;
    public Integer item_count;
    public Integer likes;
    public String name;
    public ListPrivacy privacy;
    public OffsetDateTime updated_at;

    public TraktList name(String str) {
        this.name = str;
        return this;
    }

    public TraktList description(String str) {
        this.description = str;
        return this;
    }

    public TraktList privacy(ListPrivacy listPrivacy) {
        this.privacy = listPrivacy;
        return this;
    }

    public TraktList displayNumbers(boolean z) {
        this.display_numbers = Boolean.valueOf(z);
        return this;
    }

    public TraktList allowComments(boolean z) {
        this.allow_comments = Boolean.valueOf(z);
        return this;
    }
}
