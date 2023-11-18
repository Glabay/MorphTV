package com.uwetrottmann.trakt5.enums;

public enum Extended implements TraktEnum {
    FULL("full"),
    NOSEASONS("noseasons"),
    EPISODES("episodes"),
    FULLEPISODES("full,episodes");
    
    private final String value;

    private Extended(String str) {
        this.value = str;
    }

    public String toString() {
        return this.value;
    }
}
