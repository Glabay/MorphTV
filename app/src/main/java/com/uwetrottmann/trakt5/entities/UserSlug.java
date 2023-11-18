package com.uwetrottmann.trakt5.entities;

import org.apache.commons.lang3.StringUtils;

public class UserSlug {
    public static final UserSlug ME = new UserSlug("me");
    private String userSlug;

    public UserSlug(String str) {
        if (str == null) {
            throw new IllegalArgumentException("trakt user slug can not be null.");
        }
        str = str.trim();
        if (str.length() == 0) {
            throw new IllegalArgumentException("trakt user slug can not be empty.");
        }
        this.userSlug = str;
    }

    public static UserSlug fromUsername(String str) {
        if (str != null) {
            return new UserSlug(str.replace(".", "-").replace(StringUtils.SPACE, "-").replaceAll("(-)+", "-"));
        }
        throw new IllegalArgumentException("trakt username can not be null.");
    }

    public String toString() {
        return this.userSlug;
    }
}
