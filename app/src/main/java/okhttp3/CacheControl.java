package okhttp3;

import com.google.common.net.HttpHeaders;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import kotlin.text.Typography;

public final class CacheControl {
    public static final CacheControl FORCE_CACHE = new CacheControl$Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
    public static final CacheControl FORCE_NETWORK = new CacheControl$Builder().noCache().build();
    @Nullable
    String headerValue;
    private final boolean immutable;
    private final boolean isPrivate;
    private final boolean isPublic;
    private final int maxAgeSeconds;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean mustRevalidate;
    private final boolean noCache;
    private final boolean noStore;
    private final boolean noTransform;
    private final boolean onlyIfCached;
    private final int sMaxAgeSeconds;

    private CacheControl(boolean z, boolean z2, int i, int i2, boolean z3, boolean z4, boolean z5, int i3, int i4, boolean z6, boolean z7, boolean z8, @Nullable String str) {
        this.noCache = z;
        this.noStore = z2;
        this.maxAgeSeconds = i;
        this.sMaxAgeSeconds = i2;
        this.isPrivate = z3;
        this.isPublic = z4;
        this.mustRevalidate = z5;
        this.maxStaleSeconds = i3;
        this.minFreshSeconds = i4;
        this.onlyIfCached = z6;
        this.noTransform = z7;
        this.immutable = z8;
        this.headerValue = str;
    }

    CacheControl(CacheControl$Builder cacheControl$Builder) {
        this.noCache = cacheControl$Builder.noCache;
        this.noStore = cacheControl$Builder.noStore;
        this.maxAgeSeconds = cacheControl$Builder.maxAgeSeconds;
        this.sMaxAgeSeconds = -1;
        this.isPrivate = false;
        this.isPublic = false;
        this.mustRevalidate = false;
        this.maxStaleSeconds = cacheControl$Builder.maxStaleSeconds;
        this.minFreshSeconds = cacheControl$Builder.minFreshSeconds;
        this.onlyIfCached = cacheControl$Builder.onlyIfCached;
        this.noTransform = cacheControl$Builder.noTransform;
        this.immutable = cacheControl$Builder.immutable;
    }

    public boolean noCache() {
        return this.noCache;
    }

    public boolean noStore() {
        return this.noStore;
    }

    public int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    public int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public boolean mustRevalidate() {
        return this.mustRevalidate;
    }

    public int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    public int minFreshSeconds() {
        return this.minFreshSeconds;
    }

    public boolean onlyIfCached() {
        return this.onlyIfCached;
    }

    public boolean noTransform() {
        return this.noTransform;
    }

    public boolean immutable() {
        return this.immutable;
    }

    public static CacheControl parse(Headers headers) {
        Headers headers2 = headers;
        int size = headers.size();
        Object obj = 1;
        String str = null;
        boolean z = false;
        boolean z2 = false;
        int i = -1;
        int i2 = -1;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        int i3 = -1;
        int i4 = -1;
        boolean z6 = false;
        boolean z7 = false;
        boolean z8 = false;
        for (int i5 = 0; i5 < size; i5++) {
            int i6;
            int skipUntil;
            int skipUntil2;
            String trim;
            int skipWhitespace;
            String trim2;
            String name = headers2.name(i5);
            String value = headers2.value(i5);
            if (name.equalsIgnoreCase(HttpHeaders.CACHE_CONTROL)) {
                if (str == null) {
                    str = value;
                    for (i6 = 0; i6 < value.length(); i6 = skipUntil) {
                        skipUntil2 = okhttp3.internal.http.HttpHeaders.skipUntil(value, i6, "=,;");
                        trim = value.substring(i6, skipUntil2).trim();
                        if (!(skipUntil2 == value.length() || value.charAt(skipUntil2) == ',')) {
                            if (value.charAt(skipUntil2) == ';') {
                                skipWhitespace = okhttp3.internal.http.HttpHeaders.skipWhitespace(value, skipUntil2 + 1);
                                if (skipWhitespace < value.length() || value.charAt(skipWhitespace) != Typography.quote) {
                                    skipUntil = okhttp3.internal.http.HttpHeaders.skipUntil(value, skipWhitespace, ",;");
                                    trim2 = value.substring(skipWhitespace, skipUntil).trim();
                                } else {
                                    skipWhitespace++;
                                    skipUntil = okhttp3.internal.http.HttpHeaders.skipUntil(value, skipWhitespace, "\"");
                                    trim2 = value.substring(skipWhitespace, skipUntil);
                                    skipUntil++;
                                }
                                if (!"no-cache".equalsIgnoreCase(trim)) {
                                    z = true;
                                } else if (!"no-store".equalsIgnoreCase(trim)) {
                                    z2 = true;
                                } else if (!"max-age".equalsIgnoreCase(trim)) {
                                    i = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                                } else if (!"s-maxage".equalsIgnoreCase(trim)) {
                                    i2 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                                } else if (!"private".equalsIgnoreCase(trim)) {
                                    z3 = true;
                                } else if (!"public".equalsIgnoreCase(trim)) {
                                    z4 = true;
                                } else if (!"must-revalidate".equalsIgnoreCase(trim)) {
                                    z5 = true;
                                } else if (!"max-stale".equalsIgnoreCase(trim)) {
                                    i3 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, Integer.MAX_VALUE);
                                } else if (!"min-fresh".equalsIgnoreCase(trim)) {
                                    i4 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                                } else if (!"only-if-cached".equalsIgnoreCase(trim)) {
                                    z6 = true;
                                } else if (!"no-transform".equalsIgnoreCase(trim)) {
                                    z7 = true;
                                } else if (!"immutable".equalsIgnoreCase(trim)) {
                                    z8 = true;
                                }
                            }
                        }
                        skipUntil = skipUntil2 + 1;
                        trim2 = null;
                        if (!"no-cache".equalsIgnoreCase(trim)) {
                            z = true;
                        } else if (!"no-store".equalsIgnoreCase(trim)) {
                            z2 = true;
                        } else if (!"max-age".equalsIgnoreCase(trim)) {
                            i = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                        } else if (!"s-maxage".equalsIgnoreCase(trim)) {
                            i2 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                        } else if (!"private".equalsIgnoreCase(trim)) {
                            z3 = true;
                        } else if (!"public".equalsIgnoreCase(trim)) {
                            z4 = true;
                        } else if (!"must-revalidate".equalsIgnoreCase(trim)) {
                            z5 = true;
                        } else if (!"max-stale".equalsIgnoreCase(trim)) {
                            i3 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, Integer.MAX_VALUE);
                        } else if (!"min-fresh".equalsIgnoreCase(trim)) {
                            i4 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                        } else if (!"only-if-cached".equalsIgnoreCase(trim)) {
                            z6 = true;
                        } else if (!"no-transform".equalsIgnoreCase(trim)) {
                            z7 = true;
                        } else if (!"immutable".equalsIgnoreCase(trim)) {
                            z8 = true;
                        }
                    }
                }
            } else if (!name.equalsIgnoreCase(HttpHeaders.PRAGMA)) {
            }
            obj = null;
            while (i6 < value.length()) {
                skipUntil2 = okhttp3.internal.http.HttpHeaders.skipUntil(value, i6, "=,;");
                trim = value.substring(i6, skipUntil2).trim();
                if (value.charAt(skipUntil2) == ';') {
                    skipWhitespace = okhttp3.internal.http.HttpHeaders.skipWhitespace(value, skipUntil2 + 1);
                    if (skipWhitespace < value.length()) {
                    }
                    skipUntil = okhttp3.internal.http.HttpHeaders.skipUntil(value, skipWhitespace, ",;");
                    trim2 = value.substring(skipWhitespace, skipUntil).trim();
                    if (!"no-cache".equalsIgnoreCase(trim)) {
                        z = true;
                    } else if (!"no-store".equalsIgnoreCase(trim)) {
                        z2 = true;
                    } else if (!"max-age".equalsIgnoreCase(trim)) {
                        i = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                    } else if (!"s-maxage".equalsIgnoreCase(trim)) {
                        i2 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                    } else if (!"private".equalsIgnoreCase(trim)) {
                        z3 = true;
                    } else if (!"public".equalsIgnoreCase(trim)) {
                        z4 = true;
                    } else if (!"must-revalidate".equalsIgnoreCase(trim)) {
                        z5 = true;
                    } else if (!"max-stale".equalsIgnoreCase(trim)) {
                        i3 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, Integer.MAX_VALUE);
                    } else if (!"min-fresh".equalsIgnoreCase(trim)) {
                        i4 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                    } else if (!"only-if-cached".equalsIgnoreCase(trim)) {
                        z6 = true;
                    } else if (!"no-transform".equalsIgnoreCase(trim)) {
                        z7 = true;
                    } else if (!"immutable".equalsIgnoreCase(trim)) {
                        z8 = true;
                    }
                } else {
                    skipUntil = skipUntil2 + 1;
                    trim2 = null;
                    if (!"no-cache".equalsIgnoreCase(trim)) {
                        z = true;
                    } else if (!"no-store".equalsIgnoreCase(trim)) {
                        z2 = true;
                    } else if (!"max-age".equalsIgnoreCase(trim)) {
                        i = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                    } else if (!"s-maxage".equalsIgnoreCase(trim)) {
                        i2 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                    } else if (!"private".equalsIgnoreCase(trim)) {
                        z3 = true;
                    } else if (!"public".equalsIgnoreCase(trim)) {
                        z4 = true;
                    } else if (!"must-revalidate".equalsIgnoreCase(trim)) {
                        z5 = true;
                    } else if (!"max-stale".equalsIgnoreCase(trim)) {
                        i3 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, Integer.MAX_VALUE);
                    } else if (!"min-fresh".equalsIgnoreCase(trim)) {
                        i4 = okhttp3.internal.http.HttpHeaders.parseSeconds(trim2, -1);
                    } else if (!"only-if-cached".equalsIgnoreCase(trim)) {
                        z6 = true;
                    } else if (!"no-transform".equalsIgnoreCase(trim)) {
                        z7 = true;
                    } else if (!"immutable".equalsIgnoreCase(trim)) {
                        z8 = true;
                    }
                }
            }
        }
        return new CacheControl(z, z2, i, i2, z3, z4, z5, i3, i4, z6, z7, z8, obj == null ? null : str);
    }

    public String toString() {
        String str = this.headerValue;
        if (str != null) {
            return str;
        }
        str = headerValue();
        this.headerValue = str;
        return str;
    }

    private String headerValue() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.noCache) {
            stringBuilder.append("no-cache, ");
        }
        if (this.noStore) {
            stringBuilder.append("no-store, ");
        }
        if (this.maxAgeSeconds != -1) {
            stringBuilder.append("max-age=");
            stringBuilder.append(this.maxAgeSeconds);
            stringBuilder.append(", ");
        }
        if (this.sMaxAgeSeconds != -1) {
            stringBuilder.append("s-maxage=");
            stringBuilder.append(this.sMaxAgeSeconds);
            stringBuilder.append(", ");
        }
        if (this.isPrivate) {
            stringBuilder.append("private, ");
        }
        if (this.isPublic) {
            stringBuilder.append("public, ");
        }
        if (this.mustRevalidate) {
            stringBuilder.append("must-revalidate, ");
        }
        if (this.maxStaleSeconds != -1) {
            stringBuilder.append("max-stale=");
            stringBuilder.append(this.maxStaleSeconds);
            stringBuilder.append(", ");
        }
        if (this.minFreshSeconds != -1) {
            stringBuilder.append("min-fresh=");
            stringBuilder.append(this.minFreshSeconds);
            stringBuilder.append(", ");
        }
        if (this.onlyIfCached) {
            stringBuilder.append("only-if-cached, ");
        }
        if (this.noTransform) {
            stringBuilder.append("no-transform, ");
        }
        if (this.immutable) {
            stringBuilder.append("immutable, ");
        }
        if (stringBuilder.length() == 0) {
            return "";
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return stringBuilder.toString();
    }
}
