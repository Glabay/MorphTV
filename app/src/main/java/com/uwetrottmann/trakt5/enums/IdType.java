package com.uwetrottmann.trakt5.enums;

public enum IdType implements TraktEnum {
    TRAKT("trakt"),
    IMDB("imdb"),
    TMDB("tmdb"),
    TVDB("tvdb"),
    TVRAGE("tvrage");
    
    private final String value;

    private IdType(String str) {
        this.value = str;
    }

    public String toString() {
        return this.value;
    }
}
