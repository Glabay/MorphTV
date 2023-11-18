package okhttp3.internal.http;

import java.net.Proxy.Type;
import okhttp3.HttpUrl;
import okhttp3.Request;

public final class RequestLine {
    private RequestLine() {
    }

    public static String get(Request request, Type type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(request.method());
        stringBuilder.append(' ');
        if (includeAuthorityInRequestLine(request, type) != null) {
            stringBuilder.append(request.url());
        } else {
            stringBuilder.append(requestPath(request.url()));
        }
        stringBuilder.append(" HTTP/1.1");
        return stringBuilder.toString();
    }

    private static boolean includeAuthorityInRequestLine(Request request, Type type) {
        return (request.isHttps() == null && type == Type.HTTP) ? true : null;
    }

    public static String requestPath(HttpUrl httpUrl) {
        String encodedPath = httpUrl.encodedPath();
        httpUrl = httpUrl.encodedQuery();
        if (httpUrl == null) {
            return encodedPath;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(encodedPath);
        stringBuilder.append('?');
        stringBuilder.append(httpUrl);
        return stringBuilder.toString();
    }
}
