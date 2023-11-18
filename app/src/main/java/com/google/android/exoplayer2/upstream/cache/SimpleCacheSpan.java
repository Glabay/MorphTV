package com.google.android.exoplayer2.upstream.cache;

import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class SimpleCacheSpan extends CacheSpan {
    private static final Pattern CACHE_FILE_PATTERN_V1 = Pattern.compile("^(.+)\\.(\\d+)\\.(\\d+)\\.v1\\.exo$", 32);
    private static final Pattern CACHE_FILE_PATTERN_V2 = Pattern.compile("^(.+)\\.(\\d+)\\.(\\d+)\\.v2\\.exo$", 32);
    private static final Pattern CACHE_FILE_PATTERN_V3 = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)\\.v3\\.exo$", 32);
    private static final String SUFFIX = ".v3.exo";

    public static File getCacheFile(File file, int i, long j, long j2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i);
        stringBuilder.append(".");
        stringBuilder.append(j);
        stringBuilder.append(".");
        stringBuilder.append(j2);
        stringBuilder.append(SUFFIX);
        return new File(file, stringBuilder.toString());
    }

    public static SimpleCacheSpan createLookup(String str, long j) {
        return new SimpleCacheSpan(str, j, -1, C0649C.TIME_UNSET, null);
    }

    public static SimpleCacheSpan createOpenHole(String str, long j) {
        return new SimpleCacheSpan(str, j, -1, C0649C.TIME_UNSET, null);
    }

    public static SimpleCacheSpan createClosedHole(String str, long j, long j2) {
        return new SimpleCacheSpan(str, j, j2, C0649C.TIME_UNSET, null);
    }

    @Nullable
    public static SimpleCacheSpan createCacheEntry(File file, CachedContentIndex cachedContentIndex) {
        CharSequence name = file.getName();
        if (!name.endsWith(SUFFIX)) {
            file = upgradeFile(file, cachedContentIndex);
            if (file == null) {
                return null;
            }
            name = file.getName();
        }
        File file2 = file;
        file = CACHE_FILE_PATTERN_V3.matcher(name);
        if (!file.matches()) {
            return null;
        }
        long length = file2.length();
        String keyForId = cachedContentIndex.getKeyForId(Integer.parseInt(file.group(1)));
        if (keyForId == null) {
            cachedContentIndex = null;
        } else {
            CachedContentIndex simpleCacheSpan = new SimpleCacheSpan(keyForId, Long.parseLong(file.group(2)), length, Long.parseLong(file.group(3)), file2);
        }
        return cachedContentIndex;
    }

    @Nullable
    private static File upgradeFile(File file, CachedContentIndex cachedContentIndex) {
        String unescapeFileName;
        CharSequence name = file.getName();
        Matcher matcher = CACHE_FILE_PATTERN_V2.matcher(name);
        if (matcher.matches()) {
            unescapeFileName = Util.unescapeFileName(matcher.group(1));
            if (unescapeFileName == null) {
                return null;
            }
        }
        matcher = CACHE_FILE_PATTERN_V1.matcher(name);
        if (!matcher.matches()) {
            return null;
        }
        unescapeFileName = matcher.group(1);
        cachedContentIndex = getCacheFile(file.getParentFile(), cachedContentIndex.assignIdForKey(unescapeFileName), Long.parseLong(matcher.group(2)), Long.parseLong(matcher.group(3)));
        if (file.renameTo(cachedContentIndex) == null) {
            return null;
        }
        return cachedContentIndex;
    }

    private SimpleCacheSpan(String str, long j, long j2, long j3, @Nullable File file) {
        super(str, j, j2, j3, file);
    }

    public SimpleCacheSpan copyWithUpdatedLastAccessTime(int i) {
        Assertions.checkState(this.isCached);
        long currentTimeMillis = System.currentTimeMillis();
        return new SimpleCacheSpan(this.key, this.position, this.length, currentTimeMillis, getCacheFile(this.file.getParentFile(), i, this.position, currentTimeMillis));
    }
}
