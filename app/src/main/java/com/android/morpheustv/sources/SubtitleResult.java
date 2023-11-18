package com.android.morpheustv.sources;

public class SubtitleResult {
    public int episode = 0;
    String filename;
    String language;
    public String provider;
    public int season = 0;
    String subtitleID;

    public SubtitleResult(String str) {
        this.provider = str;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public String getSubtitleID() {
        return this.subtitleID;
    }

    public void setSubtitleID(String str) {
        this.subtitleID = str;
    }
}
