package com.uwetrottmann.trakt5.enums;

public enum RatingsFilter implements TraktEnum {
    ALL(""),
    WEAKSAUCE("/1"),
    TERRIBLE("/2"),
    BAD("/3"),
    POOR("/4"),
    MEH("/5"),
    FAIR("/6"),
    GOOD("/7"),
    GREAT("/8"),
    SUPERB("/9"),
    TOTALLYNINJA("/10");
    
    private final String value;

    private RatingsFilter(String str) {
        this.value = str;
    }

    public String toString() {
        return this.value;
    }
}
