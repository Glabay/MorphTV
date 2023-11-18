package com.google.android.exoplayer2.upstream.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C0649C;
import java.io.File;

public class CacheSpan implements Comparable<CacheSpan> {
    @Nullable
    public final File file;
    public final boolean isCached;
    public final String key;
    public final long lastAccessTimestamp;
    public final long length;
    public final long position;

    public CacheSpan(String str, long j, long j2) {
        this(str, j, j2, C0649C.TIME_UNSET, null);
    }

    public CacheSpan(String str, long j, long j2, long j3, @Nullable File file) {
        this.key = str;
        this.position = j;
        this.length = j2;
        this.isCached = file != null ? true : null;
        this.file = file;
        this.lastAccessTimestamp = j3;
    }

    public boolean isOpenEnded() {
        return this.length == -1;
    }

    public boolean isHoleSpan() {
        return this.isCached ^ 1;
    }

    public int compareTo(@NonNull CacheSpan cacheSpan) {
        if (!this.key.equals(cacheSpan.key)) {
            return this.key.compareTo(cacheSpan.key);
        }
        long j = this.position - cacheSpan.position;
        cacheSpan = j == 0 ? null : j < 0 ? -1 : true;
        return cacheSpan;
    }
}
