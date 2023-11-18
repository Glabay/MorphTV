package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.Comment;
import com.uwetrottmann.trakt5.entities.Credits;
import com.uwetrottmann.trakt5.entities.Movie;
import com.uwetrottmann.trakt5.entities.MovieTranslation;
import com.uwetrottmann.trakt5.entities.Ratings;
import com.uwetrottmann.trakt5.entities.Stats;
import com.uwetrottmann.trakt5.entities.TrendingMovie;
import com.uwetrottmann.trakt5.enums.Extended;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Movies {
    @GET("movies/{id}/comments")
    Call<List<Comment>> comments(@Path("id") String str, @Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended);

    @GET("movies/{id}/people")
    Call<Credits> people(@Path("id") String str);

    @GET("movies/popular")
    Call<List<Movie>> popular(@Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended);

    @GET("movies/{id}/ratings")
    Call<Ratings> ratings(@Path("id") String str);

    @GET("movies/{id}/stats")
    Call<Stats> stats(@Path("id") String str);

    @GET("movies/{id}")
    Call<Movie> summary(@Path("id") String str, @Query(encoded = true, value = "extended") Extended extended);

    @GET("movies/{id}/translations/{language}")
    Call<List<MovieTranslation>> translation(@Path("id") String str, @Path("language") String str2);

    @GET("movies/{id}/translations")
    Call<List<MovieTranslation>> translations(@Path("id") String str);

    @GET("movies/watched/weekly")
    Call<List<TrendingMovie>> trending(@Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended);
}
