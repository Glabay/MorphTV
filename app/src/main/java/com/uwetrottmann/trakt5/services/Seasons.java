package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.Comment;
import com.uwetrottmann.trakt5.entities.Episode;
import com.uwetrottmann.trakt5.entities.Ratings;
import com.uwetrottmann.trakt5.entities.Season;
import com.uwetrottmann.trakt5.entities.Stats;
import com.uwetrottmann.trakt5.enums.Extended;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Seasons {
    @GET("shows/{id}/seasons/{season}/comments")
    Call<List<Comment>> comments(@Path("id") String str, @Path("season") int i);

    @GET("shows/{id}/seasons/{season}/ratings")
    Call<Ratings> ratings(@Path("id") String str, @Path("season") int i);

    @GET("shows/{id}/seasons/{season}")
    Call<List<Episode>> season(@Path("id") String str, @Path("season") int i, @Query(encoded = true, value = "extended") Extended extended);

    @GET("shows/{id}/seasons/{season}/stats")
    Call<Stats> stats(@Path("id") String str, @Path("season") int i);

    @GET("shows/{id}/seasons")
    Call<List<Season>> summary(@Path("id") String str, @Query(encoded = true, value = "extended") Extended extended);
}
