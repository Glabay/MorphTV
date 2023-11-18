package com.uwetrottmann.trakt5.entities;

public class ListIds {
    public String slug;
    public Integer trakt;

    public static ListIds trakt(int i) {
        ListIds listIds = new ListIds();
        listIds.trakt = Integer.valueOf(i);
        return listIds;
    }

    public static ListIds slug(String str) {
        ListIds listIds = new ListIds();
        listIds.slug = str;
        return listIds;
    }
}
