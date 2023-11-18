package okhttp3;

import java.util.concurrent.TimeUnit;

public final class CacheControl$Builder {
    boolean immutable;
    int maxAgeSeconds = -1;
    int maxStaleSeconds = -1;
    int minFreshSeconds = -1;
    boolean noCache;
    boolean noStore;
    boolean noTransform;
    boolean onlyIfCached;

    public CacheControl$Builder noCache() {
        this.noCache = true;
        return this;
    }

    public CacheControl$Builder noStore() {
        this.noStore = true;
        return this;
    }

    public CacheControl$Builder maxAge(int i, TimeUnit timeUnit) {
        if (i < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("maxAge < 0: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        i = timeUnit.toSeconds((long) i);
        this.maxAgeSeconds = i > 2147483647L ? Integer.MAX_VALUE : (int) i;
        return this;
    }

    public CacheControl$Builder maxStale(int i, TimeUnit timeUnit) {
        if (i < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("maxStale < 0: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        i = timeUnit.toSeconds((long) i);
        this.maxStaleSeconds = i > 2147483647L ? Integer.MAX_VALUE : (int) i;
        return this;
    }

    public CacheControl$Builder minFresh(int i, TimeUnit timeUnit) {
        if (i < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("minFresh < 0: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        i = timeUnit.toSeconds((long) i);
        this.minFreshSeconds = i > 2147483647L ? Integer.MAX_VALUE : (int) i;
        return this;
    }

    public CacheControl$Builder onlyIfCached() {
        this.onlyIfCached = true;
        return this;
    }

    public CacheControl$Builder noTransform() {
        this.noTransform = true;
        return this;
    }

    public CacheControl$Builder immutable() {
        this.immutable = true;
        return this;
    }

    public CacheControl build() {
        return new CacheControl(this);
    }
}
