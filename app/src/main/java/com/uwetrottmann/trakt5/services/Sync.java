package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.BaseMovie;
import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.LastActivities;
import com.uwetrottmann.trakt5.entities.RatedEpisode;
import com.uwetrottmann.trakt5.entities.RatedMovie;
import com.uwetrottmann.trakt5.entities.RatedSeason;
import com.uwetrottmann.trakt5.entities.RatedShow;
import com.uwetrottmann.trakt5.entities.SyncItems;
import com.uwetrottmann.trakt5.entities.SyncResponse;
import com.uwetrottmann.trakt5.entities.WatchlistedEpisode;
import com.uwetrottmann.trakt5.entities.WatchlistedSeason;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.RatingsFilter;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Sync {
    @POST("sync/collection")
    Call<SyncResponse> addItemsToCollection(@Body SyncItems syncItems);

    @POST("sync/history")
    Call<SyncResponse> addItemsToWatchedHistory(@Body SyncItems syncItems);

    @POST("sync/watchlist")
    Call<SyncResponse> addItemsToWatchlist(@Body SyncItems syncItems);

    @POST("sync/ratings")
    Call<SyncResponse> addRatings(@Body SyncItems syncItems);

    @GET("sync/collection/movies")
    Call<List<BaseMovie>> collectionMovies(@Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/collection/shows")
    Call<List<BaseShow>> collectionShows(@Query(encoded = true, value = "extended") Extended extended);

    @POST("sync/collection/remove")
    Call<SyncResponse> deleteItemsFromCollection(@Body SyncItems syncItems);

    @POST("sync/history/remove")
    Call<SyncResponse> deleteItemsFromWatchedHistory(@Body SyncItems syncItems);

    @POST("sync/watchlist/remove")
    Call<SyncResponse> deleteItemsFromWatchlist(@Body SyncItems syncItems);

    @POST("sync/ratings/remove")
    Call<SyncResponse> deleteRatings(@Body SyncItems syncItems);

    @GET("sync/last_activities")
    Call<LastActivities> lastActivities();

    @GET("sync/ratings/episodes{rating}")
    Call<List<RatedEpisode>> ratingsEpisodes(@Path(encoded = true, value = "rating") RatingsFilter ratingsFilter, @Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/ratings/movies{rating}")
    Call<List<RatedMovie>> ratingsMovies(@Path(encoded = true, value = "rating") RatingsFilter ratingsFilter, @Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/ratings/seasons{rating}")
    Call<List<RatedSeason>> ratingsSeasons(@Path(encoded = true, value = "rating") RatingsFilter ratingsFilter, @Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/ratings/shows{rating}")
    Call<List<RatedShow>> ratingsShows(@Path(encoded = true, value = "rating") RatingsFilter ratingsFilter, @Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/watched/movies")
    Call<List<BaseMovie>> watchedMovies(@Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/watched/shows")
    Call<List<BaseShow>> watchedShows(@Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/watchlist/episodes")
    Call<List<WatchlistedEpisode>> watchlistEpisodes(@Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/watchlist/movies")
    Call<List<BaseMovie>> watchlistMovies(@Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/watchlist/seasons")
    Call<List<WatchlistedSeason>> watchlistSeasons(@Query(encoded = true, value = "extended") Extended extended);

    @GET("sync/watchlist/shows")
    Call<List<BaseShow>> watchlistShows(@Query(encoded = true, value = "extended") Extended extended);
}
