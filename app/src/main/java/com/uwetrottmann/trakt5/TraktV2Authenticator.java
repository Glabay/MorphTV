package com.uwetrottmann.trakt5;

import com.uwetrottmann.trakt5.entities.AccessToken;
import java.io.IOException;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TraktV2Authenticator implements Authenticator {
    public final TraktV2 trakt;

    public TraktV2Authenticator(TraktV2 traktV2) {
        this.trakt = traktV2;
    }

    public Request authenticate(Route route, Response response) throws IOException {
        return handleAuthenticate(response, this.trakt);
    }

    public static Request handleAuthenticate(Response response, TraktV2 traktV2) throws IOException {
        if (TraktV2.API_HOST.equals(response.request().url().host()) && responseCount(response) < 2 && traktV2.refreshToken() != null) {
            if (traktV2.refreshToken().length() != 0) {
                retrofit2.Response refreshAccessToken = traktV2.refreshAccessToken();
                if (!refreshAccessToken.isSuccessful()) {
                    return null;
                }
                String str = ((AccessToken) refreshAccessToken.body()).access_token;
                traktV2.accessToken(str);
                traktV2.refreshToken(((AccessToken) refreshAccessToken.body()).refresh_token);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Bearer ");
                stringBuilder.append(str);
                return response.request().newBuilder().header("Authorization", stringBuilder.toString()).build();
            }
        }
        return null;
    }

    private static int responseCount(Response response) {
        int i = 1;
        while (true) {
            response = response.priorResponse();
            if (response == null) {
                return i;
            }
            i++;
        }
    }
}
