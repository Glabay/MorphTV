package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.AccessToken;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Authentication {
    @FormUrlEncoded
    @POST("https://trakt.tv/oauth/token")
    Call<AccessToken> exchangeCodeForAccessToken(@Field("grant_type") String str, @Field("code") String str2, @Field("client_id") String str3, @Field("client_secret") String str4, @Field("redirect_uri") String str5);

    @FormUrlEncoded
    @POST("https://trakt.tv/oauth/token")
    Call<AccessToken> refreshAccessToken(@Field("grant_type") String str, @Field("refresh_token") String str2, @Field("client_id") String str3, @Field("client_secret") String str4, @Field("redirect_uri") String str5);
}
