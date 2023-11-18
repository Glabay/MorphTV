package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.Comment;
import com.uwetrottmann.trakt5.entities.Episode;
import com.uwetrottmann.trakt5.entities.Ratings;
import com.uwetrottmann.trakt5.entities.Stats;
import com.uwetrottmann.trakt5.enums.Extended;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Episodes {
    @GET("shows/{id}/seasons/{season}/episodes/{episode}/comments")
    Call<List<Comment>> comments(@Path("id") String str, @Path("season") int i, @Path("episode") int i2, @Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended);

    @GET("shows/{id}/seasons/{season}/episodes/{episode}/ratings")
    Call<Ratings> ratings(@Path("id") String str, @Path("season") int i, @Path("episode") int i2);

    @GET("shows/{id}/seasons/{season}/episodes/{episode}/stats")
    Call<Stats> stats(@Path("id") String str, @Path("season") int i, @Path("episode") int i2);

    @GET("shows/{id}/seasons/{season}/episodes/{episode}")
    Call<Episode> summary(@Path("id") String str, @Path("season") int i, @Path("episode") int i2, @Query(encoded = true, value = "extended") Extended extended);
}
