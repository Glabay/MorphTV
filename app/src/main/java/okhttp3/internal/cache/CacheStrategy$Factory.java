package okhttp3.internal.cache;

import com.google.common.net.HttpHeaders;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Headers$Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Response.Builder;
import okhttp3.internal.Internal;
import okhttp3.internal.http.HttpDate;
import org.apache.commons.lang3.time.DateUtils;

public class CacheStrategy$Factory {
    private int ageSeconds = -1;
    final Response cacheResponse;
    private String etag;
    private Date expires;
    private Date lastModified;
    private String lastModifiedString;
    final long nowMillis;
    private long receivedResponseMillis;
    final Request request;
    private long sentRequestMillis;
    private Date servedDate;
    private String servedDateString;

    public CacheStrategy$Factory(long j, Request request, Response response) {
        this.nowMillis = j;
        this.request = request;
        this.cacheResponse = response;
        if (response != null) {
            this.sentRequestMillis = response.sentRequestAtMillis();
            this.receivedResponseMillis = response.receivedResponseAtMillis();
            j = response.headers();
            request = j.size();
            for (int i = 0; i < request; i++) {
                response = j.name(i);
                String value = j.value(i);
                if (HttpHeaders.DATE.equalsIgnoreCase(response)) {
                    this.servedDate = HttpDate.parse(value);
                    this.servedDateString = value;
                } else if (HttpHeaders.EXPIRES.equalsIgnoreCase(response)) {
                    this.expires = HttpDate.parse(value);
                } else if (HttpHeaders.LAST_MODIFIED.equalsIgnoreCase(response)) {
                    this.lastModified = HttpDate.parse(value);
                    this.lastModifiedString = value;
                } else if (HttpHeaders.ETAG.equalsIgnoreCase(response)) {
                    this.etag = value;
                } else if (HttpHeaders.AGE.equalsIgnoreCase(response) != null) {
                    this.ageSeconds = okhttp3.internal.http.HttpHeaders.parseSeconds(value, -1);
                }
            }
        }
    }

    public CacheStrategy get() {
        CacheStrategy candidate = getCandidate();
        return (candidate.networkRequest == null || !this.request.cacheControl().onlyIfCached()) ? candidate : new CacheStrategy(null, null);
    }

    private CacheStrategy getCandidate() {
        if (this.cacheResponse == null) {
            return new CacheStrategy(this.request, null);
        }
        if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
            return new CacheStrategy(this.request, null);
        }
        if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
            return new CacheStrategy(this.request, null);
        }
        CacheControl cacheControl = this.request.cacheControl();
        if (!cacheControl.noCache()) {
            if (!hasConditions(this.request)) {
                String str;
                String str2;
                long cacheResponseAge = cacheResponseAge();
                long computeFreshnessLifetime = computeFreshnessLifetime();
                if (cacheControl.maxAgeSeconds() != -1) {
                    computeFreshnessLifetime = Math.min(computeFreshnessLifetime, TimeUnit.SECONDS.toMillis((long) cacheControl.maxAgeSeconds()));
                }
                long j = 0;
                long toMillis = cacheControl.minFreshSeconds() != -1 ? TimeUnit.SECONDS.toMillis((long) cacheControl.minFreshSeconds()) : 0;
                CacheControl cacheControl2 = this.cacheResponse.cacheControl();
                if (!(cacheControl2.mustRevalidate() || cacheControl.maxStaleSeconds() == -1)) {
                    j = TimeUnit.SECONDS.toMillis((long) cacheControl.maxStaleSeconds());
                }
                if (!cacheControl2.noCache()) {
                    long j2 = cacheResponseAge + toMillis;
                    if (j2 < computeFreshnessLifetime + j) {
                        Builder newBuilder = this.cacheResponse.newBuilder();
                        if (j2 >= computeFreshnessLifetime) {
                            newBuilder.addHeader(HttpHeaders.WARNING, "110 HttpURLConnection \"Response is stale\"");
                        }
                        if (cacheResponseAge > DateUtils.MILLIS_PER_DAY && isFreshnessLifetimeHeuristic()) {
                            newBuilder.addHeader(HttpHeaders.WARNING, "113 HttpURLConnection \"Heuristic expiration\"");
                        }
                        return new CacheStrategy(null, newBuilder.build());
                    }
                }
                if (this.etag != null) {
                    str = HttpHeaders.IF_NONE_MATCH;
                    str2 = this.etag;
                } else if (this.lastModified != null) {
                    str = HttpHeaders.IF_MODIFIED_SINCE;
                    str2 = this.lastModifiedString;
                } else if (this.servedDate == null) {
                    return new CacheStrategy(this.request, null);
                } else {
                    str = HttpHeaders.IF_MODIFIED_SINCE;
                    str2 = this.servedDateString;
                }
                Headers$Builder newBuilder2 = this.request.headers().newBuilder();
                Internal.instance.addLenient(newBuilder2, str, str2);
                return new CacheStrategy(this.request.newBuilder().headers(newBuilder2.build()).build(), this.cacheResponse);
            }
        }
        return new CacheStrategy(this.request, null);
    }

    private long computeFreshnessLifetime() {
        CacheControl cacheControl = this.cacheResponse.cacheControl();
        if (cacheControl.maxAgeSeconds() != -1) {
            return TimeUnit.SECONDS.toMillis((long) cacheControl.maxAgeSeconds());
        }
        long j = 0;
        long time;
        long time2;
        if (this.expires != null) {
            if (this.servedDate != null) {
                time = this.servedDate.getTime();
            } else {
                time = this.receivedResponseMillis;
            }
            time2 = this.expires.getTime() - time;
            if (time2 > 0) {
                j = time2;
            }
            return j;
        } else if (this.lastModified == null || this.cacheResponse.request().url().query() != null) {
            return 0;
        } else {
            if (this.servedDate != null) {
                time = this.servedDate.getTime();
            } else {
                time = this.sentRequestMillis;
            }
            time2 = time - this.lastModified.getTime();
            if (time2 > 0) {
                j = time2 / 10;
            }
            return j;
        }
    }

    private long cacheResponseAge() {
        long j = 0;
        if (this.servedDate != null) {
            j = Math.max(0, this.receivedResponseMillis - this.servedDate.getTime());
        }
        if (this.ageSeconds != -1) {
            j = Math.max(j, TimeUnit.SECONDS.toMillis((long) this.ageSeconds));
        }
        return (j + (this.receivedResponseMillis - this.sentRequestMillis)) + (this.nowMillis - this.receivedResponseMillis);
    }

    private boolean isFreshnessLifetimeHeuristic() {
        return this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
    }

    private static boolean hasConditions(Request request) {
        if (request.header(HttpHeaders.IF_MODIFIED_SINCE) == null) {
            if (request.header(HttpHeaders.IF_NONE_MATCH) == null) {
                return null;
            }
        }
        return true;
    }
}
