package com.android.morpheustv.sources;

import java.util.ArrayList;

public abstract class BaseSubtitleProvider {
    public String PROVIDER_NAME = "BaseSubtitleProvider";

    public String downloadSubtitle(SubtitleResult subtitleResult) {
        return null;
    }

    public ArrayList<SubtitleResult> getSubtitles(String str, String str2, int i, int i2) {
        return null;
    }

    public BaseSubtitleProvider(String str) {
        this.PROVIDER_NAME = str;
    }
}
