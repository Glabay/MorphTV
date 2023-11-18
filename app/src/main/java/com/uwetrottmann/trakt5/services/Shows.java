package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.Comment;
import com.uwetrottmann.trakt5.entities.Credits;
import com.uwetrottmann.trakt5.entities.Ratings;
import com.uwetrottmann.trakt5.entities.Show;
import com.uwetrottmann.trakt5.entities.Stats;
import com.uwetrottmann.trakt5.entities.Translation;
import com.uwetrottmann.trakt5.entities.TrendingShow;
import com.uwetrottmann.trakt5.enums.Extended;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Shows {
    @GET("shows/{id}/progress/collection")
    Call<BaseShow> collectedProgress(@Path("id") String str, @Query("hidden") Boolean bool, @Query("specials") Boolean bool2, @Query(encoded = true, value = "extended") Extended extended);

    @GET("shows/{id}/comments")
    Call<List<Comment>> comments(@Path("id") String str, @Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended);

    @GET("shows/{id}/people")
    Call<Credits> people(@Path("id") String str);

    @GET("shows/popular")
    Call<List<Show>> popular(@Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended);

    @GET("shows/{id}/ratings")
    Call<Ratings> ratings(@Path("id") String str);

    @GET("shows/{id}/stats")
    Call<Stats> stats(@Path("id") String str);

    @GET("shows/{id}")
    Call<Show> summary(@Path("id") String str, @Query(encoded = true, value = "extended") Extended extended);

    @GET("shows/{id}/translations/{language}")
    Call<List<Translation>> translation(@Path("id") String str, @Path("language") String str2);

    @GET("shows/{id}/translations")
    Call<List<Translation>> translations(@Path("id") String str);

    @GET("shows/trending")
    Call<List<TrendingShow>> trending(@Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended);

    @GET("shows/{id}/progress/watched")
    Call<BaseShow> watchedProgress(@Path("id") String str, @Query("hidden") Boolean bool, @Query("specials") Boolean bool2, @Query(encoded = true, value = "extended") Extended extended);
}
