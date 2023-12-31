package okhttp3;

import com.google.common.net.HttpHeaders;
import java.net.URL;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;

public final class Request {
    @Nullable
    final RequestBody body;
    private volatile CacheControl cacheControl;
    final Headers headers;
    final String method;
    final Object tag;
    final HttpUrl url;

    public static class Builder {
        RequestBody body;
        Headers$Builder headers;
        String method;
        Object tag;
        HttpUrl url;

        public Builder() {
            this.method = "GET";
            this.headers = new Headers$Builder();
        }

        Builder(Request request) {
            this.url = request.url;
            this.method = request.method;
            this.body = request.body;
            this.tag = request.tag;
            this.headers = request.headers.newBuilder();
        }

        public Builder url(HttpUrl httpUrl) {
            if (httpUrl == null) {
                throw new NullPointerException("url == null");
            }
            this.url = httpUrl;
            return this;
        }

        public Builder url(String str) {
            if (str == null) {
                throw new NullPointerException("url == null");
            }
            StringBuilder stringBuilder;
            if (str.regionMatches(true, 0, "ws:", 0, 3)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("http:");
                stringBuilder.append(str.substring(3));
                str = stringBuilder.toString();
            } else {
                if (str.regionMatches(true, 0, "wss:", 0, 4)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("https:");
                    stringBuilder.append(str.substring(4));
                    str = stringBuilder.toString();
                }
            }
            HttpUrl parse = HttpUrl.parse(str);
            if (parse != null) {
                return url(parse);
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("unexpected url: ");
            stringBuilder2.append(str);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }

        public Builder url(URL url) {
            if (url == null) {
                throw new NullPointerException("url == null");
            }
            HttpUrl httpUrl = HttpUrl.get(url);
            if (httpUrl != null) {
                return url(httpUrl);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected url: ");
            stringBuilder.append(url);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder header(String str, String str2) {
            this.headers.set(str, str2);
            return this;
        }

        public Builder addHeader(String str, String str2) {
            this.headers.add(str, str2);
            return this;
        }

        public Builder removeHeader(String str) {
            this.headers.removeAll(str);
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers.newBuilder();
            return this;
        }

        public Builder cacheControl(CacheControl cacheControl) {
            cacheControl = cacheControl.toString();
            if (cacheControl.isEmpty()) {
                return removeHeader(HttpHeaders.CACHE_CONTROL);
            }
            return header(HttpHeaders.CACHE_CONTROL, cacheControl);
        }

        public Builder get() {
            return method("GET", null);
        }

        public Builder head() {
            return method("HEAD", null);
        }

        public Builder post(RequestBody requestBody) {
            return method("POST", requestBody);
        }

        public Builder delete(@Nullable RequestBody requestBody) {
            return method("DELETE", requestBody);
        }

        public Builder delete() {
            return delete(Util.EMPTY_REQUEST);
        }

        public Builder put(RequestBody requestBody) {
            return method("PUT", requestBody);
        }

        public Builder patch(RequestBody requestBody) {
            return method("PATCH", requestBody);
        }

        public Builder method(String str, @Nullable RequestBody requestBody) {
            if (str == null) {
                throw new NullPointerException("method == null");
            } else if (str.length() == 0) {
                throw new IllegalArgumentException("method.length() == 0");
            } else if (requestBody != null && !HttpMethod.permitsRequestBody(str)) {
                r0 = new StringBuilder();
                r0.append("method ");
                r0.append(str);
                r0.append(" must not have a request body.");
                throw new IllegalArgumentException(r0.toString());
            } else if (requestBody == null && HttpMethod.requiresRequestBody(str)) {
                r0 = new StringBuilder();
                r0.append("method ");
                r0.append(str);
                r0.append(" must have a request body.");
                throw new IllegalArgumentException(r0.toString());
            } else {
                this.method = str;
                this.body = requestBody;
                return this;
            }
        }

        public Builder tag(Object obj) {
            this.tag = obj;
            return this;
        }

        public Request build() {
            if (this.url != null) {
                return new Request(this);
            }
            throw new IllegalStateException("url == null");
        }
    }

    Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers.build();
        this.body = builder.body;
        this.tag = builder.tag != null ? builder.tag : this;
    }

    public HttpUrl url() {
        return this.url;
    }

    public String method() {
        return this.method;
    }

    public Headers headers() {
        return this.headers;
    }

    public String header(String str) {
        return this.headers.get(str);
    }

    public List<String> headers(String str) {
        return this.headers.values(str);
    }

    @Nullable
    public RequestBody body() {
        return this.body;
    }

    public Object tag() {
        return this.tag;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public CacheControl cacheControl() {
        CacheControl cacheControl = this.cacheControl;
        if (cacheControl != null) {
            return cacheControl;
        }
        cacheControl = CacheControl.parse(this.headers);
        this.cacheControl = cacheControl;
        return cacheControl;
    }

    public boolean isHttps() {
        return this.url.isHttps();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Request{method=");
        stringBuilder.append(this.method);
        stringBuilder.append(", url=");
        stringBuilder.append(this.url);
        stringBuilder.append(", tag=");
        stringBuilder.append(this.tag != this ? this.tag : null);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
