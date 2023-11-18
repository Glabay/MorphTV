package okhttp3.internal.http;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.Interceptor$Chain;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

public final class BridgeInterceptor implements Interceptor {
    private final CookieJar cookieJar;

    public BridgeInterceptor(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }

    public Response intercept(Interceptor$Chain interceptor$Chain) throws IOException {
        Request request = interceptor$Chain.request();
        Builder newBuilder = request.newBuilder();
        RequestBody body = request.body();
        if (body != null) {
            MediaType contentType = body.contentType();
            if (contentType != null) {
                newBuilder.header("Content-Type", contentType.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                newBuilder.header(HttpHeaders.CONTENT_LENGTH, Long.toString(contentLength));
                newBuilder.removeHeader(HttpHeaders.TRANSFER_ENCODING);
            } else {
                newBuilder.header(HttpHeaders.TRANSFER_ENCODING, "chunked");
                newBuilder.removeHeader(HttpHeaders.CONTENT_LENGTH);
            }
        }
        boolean z = false;
        if (request.header(HttpHeaders.HOST) == null) {
            newBuilder.header(HttpHeaders.HOST, Util.hostHeader(request.url(), false));
        }
        if (request.header(HttpHeaders.CONNECTION) == null) {
            newBuilder.header(HttpHeaders.CONNECTION, "Keep-Alive");
        }
        if (request.header(HttpHeaders.ACCEPT_ENCODING) == null && request.header(HttpHeaders.RANGE) == null) {
            z = true;
            newBuilder.header(HttpHeaders.ACCEPT_ENCODING, "gzip");
        }
        List loadForRequest = this.cookieJar.loadForRequest(request.url());
        if (!loadForRequest.isEmpty()) {
            newBuilder.header(HttpHeaders.COOKIE, cookieHeader(loadForRequest));
        }
        if (request.header(HttpHeaders.USER_AGENT) == null) {
            newBuilder.header(HttpHeaders.USER_AGENT, Version.userAgent());
        }
        interceptor$Chain = interceptor$Chain.proceed(newBuilder.build());
        HttpHeaders.receiveHeaders(this.cookieJar, request.url(), interceptor$Chain.headers());
        Response.Builder request2 = interceptor$Chain.newBuilder().request(request);
        if (z && "gzip".equalsIgnoreCase(interceptor$Chain.header("Content-Encoding")) && HttpHeaders.hasBody(interceptor$Chain)) {
            Source gzipSource = new GzipSource(interceptor$Chain.body().source());
            interceptor$Chain = interceptor$Chain.headers().newBuilder().removeAll("Content-Encoding").removeAll(HttpHeaders.CONTENT_LENGTH).build();
            request2.headers(interceptor$Chain);
            request2.body(new RealResponseBody(interceptor$Chain, Okio.buffer(gzipSource)));
        }
        return request2.build();
    }

    private String cookieHeader(List<Cookie> list) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuilder.append("; ");
            }
            Cookie cookie = (Cookie) list.get(i);
            stringBuilder.append(cookie.name());
            stringBuilder.append('=');
            stringBuilder.append(cookie.value());
        }
        return stringBuilder.toString();
    }
}
