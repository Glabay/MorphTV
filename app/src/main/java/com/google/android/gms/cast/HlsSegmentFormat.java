package com.google.android.gms.cast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface HlsSegmentFormat {
    public static final String AAC = "aac";
    public static final String AC3 = "ac3";
    public static final String MP3 = "mp3";
    public static final String TS = "ts";
    public static final String TS_AAC = "ts_aac";
}
