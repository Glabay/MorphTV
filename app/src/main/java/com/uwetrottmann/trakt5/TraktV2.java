package com.uwetrottmann.trakt5;

import com.uwetrottmann.trakt5.entities.AccessToken;
import com.uwetrottmann.trakt5.services.Authentication;
import com.uwetrottmann.trakt5.services.Calendars;
import com.uwetrottmann.trakt5.services.Checkin;
import com.uwetrottmann.trakt5.services.Comments;
import com.uwetrottmann.trakt5.services.Episodes;
import com.uwetrottmann.trakt5.services.Genres;
import com.uwetrottmann.trakt5.services.Movies;
import com.uwetrottmann.trakt5.services.People;
import com.uwetrottmann.trakt5.services.Recommendations;
import com.uwetrottmann.trakt5.services.Search;
import com.uwetrottmann.trakt5.services.Seasons;
import com.uwetrottmann.trakt5.services.Shows;
import com.uwetrottmann.trakt5.services.Sync;
import com.uwetrottmann.trakt5.services.Users;
import java.io.IOException;
import java.net.URLEncoder;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class TraktV2 {
    public static final String API_HOST = "api.trakt.tv";
    public static final String API_URL = "https://api.trakt.tv/";
    public static final String API_VERSION = "2";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_TRAKT_API_KEY = "trakt-api-key";
    public static final String HEADER_TRAKT_API_VERSION = "trakt-api-version";
    public static final String OAUTH2_AUTHORIZATION_URL = "https://trakt.tv/oauth/authorize";
    public static final String OAUTH2_TOKEN_URL = "https://trakt.tv/oauth/token";
    public static final String SITE_URL = "https://trakt.tv";
    private String accessToken;
    private String apiKey;
    private String clientSecret;
    private OkHttpClient okHttpClient;
    private String redirectUri;
    private String refreshToken;
    private Retrofit retrofit;

    public TraktV2(String str) {
        this.apiKey = str;
    }

    public TraktV2(String str, String str2, String str3) {
        this.apiKey = str;
        this.clientSecret = str2;
        this.redirectUri = str3;
    }

    public String apiKey() {
        return this.apiKey;
    }

    public void apiKey(String str) {
        this.apiKey = str;
    }

    public String accessToken() {
        return this.accessToken;
    }

    public TraktV2 accessToken(String str) {
        this.accessToken = str;
        return this;
    }

    public String refreshToken() {
        return this.refreshToken;
    }

    public TraktV2 refreshToken(String str) {
        this.refreshToken = str;
        return this;
    }

    protected Builder retrofitBuilder() {
        return new Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create(TraktV2Helper.getGsonBuilder().create())).client(okHttpClient());
    }

    protected synchronized OkHttpClient okHttpClient() {
        if (this.okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            setOkHttpClientDefaults(builder);
            this.okHttpClient = builder.build();
        }
        return this.okHttpClient;
    }

    protected void setOkHttpClientDefaults(OkHttpClient.Builder builder) {
        builder.addNetworkInterceptor(new TraktV2Interceptor(this));
        builder.authenticator(new TraktV2Authenticator(this));
    }

    protected Retrofit retrofit() {
        if (this.retrofit == null) {
            this.retrofit = retrofitBuilder().build();
        }
        return this.retrofit;
    }

    public String buildAuthorizationUrl(String str) {
        StringBuilder stringBuilder = new StringBuilder(OAUTH2_AUTHORIZATION_URL);
        stringBuilder.append("?");
        stringBuilder.append("response_type=code");
        stringBuilder.append("&");
        stringBuilder.append("redirect_uri=");
        stringBuilder.append(urlEncode(this.redirectUri));
        stringBuilder.append("&");
        stringBuilder.append("state=");
        stringBuilder.append(urlEncode(str));
        stringBuilder.append("&");
        stringBuilder.append("client_id=");
        stringBuilder.append(urlEncode(apiKey()));
        return stringBuilder.toString();
    }

    private String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (String str2) {
            throw new UnsupportedOperationException(str2);
        }
    }

    public Response<AccessToken> exchangeCodeForAccessToken(String str) throws IOException {
        return authentication().exchangeCodeForAccessToken("authorization_code", str, apiKey(), this.clientSecret, this.redirectUri).execute();
    }

    public Response<AccessToken> refreshAccessToken() throws IOException {
        return authentication().refreshAccessToken("refresh_token", refreshToken(), apiKey(), this.clientSecret, this.redirectUri).execute();
    }

    @javax.annotation.Nullable
    public com.uwetrottmann.trakt5.entities.CheckinError checkForCheckinError(retrofit2.Response r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = r4.code();
        r1 = 409; // 0x199 float:5.73E-43 double:2.02E-321;
        if (r0 == r1) goto L_0x000a;
    L_0x0008:
        r4 = 0;
        return r4;
    L_0x000a:
        r0 = r3.retrofit;
        r1 = com.uwetrottmann.trakt5.entities.CheckinError.class;
        r2 = 0;
        r2 = new java.lang.annotation.Annotation[r2];
        r0 = r0.responseBodyConverter(r1, r2);
        r4 = r4.errorBody();	 Catch:{ IOException -> 0x0020 }
        r4 = r0.convert(r4);	 Catch:{ IOException -> 0x0020 }
        r4 = (com.uwetrottmann.trakt5.entities.CheckinError) r4;	 Catch:{ IOException -> 0x0020 }
        return r4;
    L_0x0020:
        r4 = new com.uwetrottmann.trakt5.entities.CheckinError;
        r4.<init>();
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uwetrottmann.trakt5.TraktV2.checkForCheckinError(retrofit2.Response):com.uwetrottmann.trakt5.entities.CheckinError");
    }

    @javax.annotation.Nullable
    public com.uwetrottmann.trakt5.entities.TraktError checkForTraktError(retrofit2.Response r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = r4.isSuccessful();
        if (r0 == 0) goto L_0x0008;
    L_0x0006:
        r4 = 0;
        return r4;
    L_0x0008:
        r0 = r3.retrofit;
        r1 = com.uwetrottmann.trakt5.entities.TraktError.class;
        r2 = 0;
        r2 = new java.lang.annotation.Annotation[r2];
        r0 = r0.responseBodyConverter(r1, r2);
        r4 = r4.errorBody();	 Catch:{ IOException -> 0x001e }
        r4 = r0.convert(r4);	 Catch:{ IOException -> 0x001e }
        r4 = (com.uwetrottmann.trakt5.entities.TraktError) r4;	 Catch:{ IOException -> 0x001e }
        return r4;
    L_0x001e:
        r4 = new com.uwetrottmann.trakt5.entities.TraktError;
        r4.<init>();
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uwetrottmann.trakt5.TraktV2.checkForTraktError(retrofit2.Response):com.uwetrottmann.trakt5.entities.TraktError");
    }

    public Authentication authentication() {
        return (Authentication) retrofit().create(Authentication.class);
    }

    public Calendars calendars() {
        return (Calendars) retrofit().create(Calendars.class);
    }

    public Checkin checkin() {
        return (Checkin) retrofit().create(Checkin.class);
    }

    public Comments comments() {
        return (Comments) retrofit().create(Comments.class);
    }

    public Genres genres() {
        return (Genres) retrofit().create(Genres.class);
    }

    public Movies movies() {
        return (Movies) retrofit().create(Movies.class);
    }

    public People people() {
        return (People) retrofit().create(People.class);
    }

    public Recommendations recommendations() {
        return (Recommendations) retrofit().create(Recommendations.class);
    }

    public Search search() {
        return (Search) retrofit().create(Search.class);
    }

    public Shows shows() {
        return (Shows) retrofit().create(Shows.class);
    }

    public Seasons seasons() {
        return (Seasons) retrofit().create(Seasons.class);
    }

    public Episodes episodes() {
        return (Episodes) retrofit().create(Episodes.class);
    }

    public Sync sync() {
        return (Sync) retrofit().create(Sync.class);
    }

    public Users users() {
        return (Users) retrofit().create(Users.class);
    }
}
