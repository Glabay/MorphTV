package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.CalendarMovieEntry;
import com.uwetrottmann.trakt5.entities.CalendarShowEntry;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Calendars {
    @GET("calendars/all/movies/{startdate}/{days}")
    Call<List<CalendarMovieEntry>> movies(@Path("startdate") String str, @Path("days") int i);

    @GET("calendars/my/movies/{startdate}/{days}")
    Call<List<CalendarMovieEntry>> myMovies(@Path("startdate") String str, @Path("days") int i);

    @GET("calendars/my/shows/new/{startdate}/{days}")
    Call<List<CalendarShowEntry>> myNewShows(@Path("startdate") String str, @Path("days") int i);

    @GET("calendars/my/shows/premieres/{startdate}/{days}")
    Call<List<CalendarShowEntry>> mySeasonPremieres(@Path("startdate") String str, @Path("days") int i);

    @GET("calendars/my/shows/{startdate}/{days}")
    Call<List<CalendarShowEntry>> myShows(@Path("startdate") String str, @Path("days") int i);

    @GET("calendars/all/shows/new/{startdate}/{days}")
    Call<List<CalendarShowEntry>> newShows(@Path("startdate") String str, @Path("days") int i);

    @GET("calendars/all/shows/premieres/{startdate}/{days}")
    Call<List<CalendarShowEntry>> seasonPremieres(@Path("startdate") String str, @Path("days") int i);

    @GET("calendars/all/shows/{startdate}/{days}")
    Call<List<CalendarShowEntry>> shows(@Path("startdate") String str, @Path("days") int i);
}
