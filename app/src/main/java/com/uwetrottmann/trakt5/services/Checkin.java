package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.EpisodeCheckin;
import com.uwetrottmann.trakt5.entities.EpisodeCheckinResponse;
import com.uwetrottmann.trakt5.entities.MovieCheckin;
import com.uwetrottmann.trakt5.entities.MovieCheckinResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

public interface Checkin {
    @POST("checkin")
    Call<EpisodeCheckinResponse> checkin(@Body EpisodeCheckin episodeCheckin);

    @POST("checkin")
    Call<MovieCheckinResponse> checkin(@Body MovieCheckin movieCheckin);

    @DELETE("checkin")
    Call<Void> deleteActiveCheckin();
}
