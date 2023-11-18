package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.Movie;
import com.uwetrottmann.trakt5.entities.Show;
import com.uwetrottmann.trakt5.enums.Extended;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Recommendations {
    @DELETE("recommendations/movies/{id}")
    Call<Void> dismissMovie(@Path("id") String str);

    @DELETE("recommendations/shows/{id}")
    Call<Void> dismissShow(@Path("id") String str);

    @GET("recommendations/movies")
    Call<List<Movie>> movies(@Query(encoded = true, value = "extended") Extended extended);

    @GET("recommendations/shows")
    Call<List<Show>> shows(@Query(encoded = true, value = "extended") Extended extended);
}
