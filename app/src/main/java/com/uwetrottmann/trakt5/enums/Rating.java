package com.uwetrottmann.trakt5.enums;

public enum Rating implements TraktEnum {
    WEAKSAUCE(1),
    TERRIBLE(2),
    BAD(3),
    POOR(4),
    MEH(5),
    FAIR(6),
    GOOD(7),
    GREAT(8),
    SUPERB(9),
    TOTALLYNINJA(10);
    
    public int value;

    private Rating(int i) {
        this.value = i;
    }

    public static Rating fromValue(int i) {
        return values()[i - 1];
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
