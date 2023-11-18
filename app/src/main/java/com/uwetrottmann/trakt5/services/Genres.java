package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.Genre;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Genres {
    @GET("genres/movies")
    Call<List<Genre>> movies();

    @GET("genres/shows")
    Call<List<Genre>> shows();
}
