package com.uwetrottmann.trakt5;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Interceptor$Chain;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class TraktV2Interceptor implements Interceptor {
    private TraktV2 trakt;

    public TraktV2Interceptor(TraktV2 traktV2) {
        this.trakt = traktV2;
    }

    public Response intercept(Interceptor$Chain interceptor$Chain) throws IOException {
        return handleIntercept(interceptor$Chain, this.trakt.apiKey(), this.trakt.accessToken());
    }

    public static Response handleIntercept(Interceptor$Chain interceptor$Chain, String str, String str2) throws IOException {
        Request request = interceptor$Chain.request();
        if (!TraktV2.API_HOST.equals(request.url().host())) {
            return interceptor$Chain.proceed(request);
        }
        Builder newBuilder = request.newBuilder();
        newBuilder.header("Content-Type", TraktV2.CONTENT_TYPE_JSON);
        newBuilder.header(TraktV2.HEADER_TRAKT_API_KEY, str);
        newBuilder.header(TraktV2.HEADER_TRAKT_API_VERSION, TraktV2.API_VERSION);
        if (!(hasNoAuthorizationHeader(request) == null || accessTokenIsNotEmpty(str2) == null)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bearer ");
            stringBuilder.append(str2);
            newBuilder.header("Authorization", stringBuilder.toString());
        }
        return interceptor$Chain.proceed(newBuilder.build());
    }

    private static boolean hasNoAuthorizationHeader(Request request) {
        return request.header("Authorization") == null ? true : null;
    }

    private static boolean accessTokenIsNotEmpty(String str) {
        return (str == null || str.length() == null) ? null : true;
    }
}
