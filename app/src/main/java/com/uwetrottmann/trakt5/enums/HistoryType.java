package com.uwetrottmann.trakt5.enums;

public enum HistoryType implements TraktEnum {
    MOVIES("movies"),
    SHOWS("shows"),
    SEASONS("seasons"),
    EPISODES("episodes");
    
    private final String value;

    private HistoryType(String str) {
        this.value = str;
    }

    public String toString() {
        return this.value;
    }
}
