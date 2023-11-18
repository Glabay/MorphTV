package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.BaseMovie;
import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.Followed;
import com.uwetrottmann.trakt5.entities.Follower;
import com.uwetrottmann.trakt5.entities.Friend;
import com.uwetrottmann.trakt5.entities.HistoryEntry;
import com.uwetrottmann.trakt5.entities.ListEntry;
import com.uwetrottmann.trakt5.entities.RatedEpisode;
import com.uwetrottmann.trakt5.entities.RatedMovie;
import com.uwetrottmann.trakt5.entities.RatedSeason;
import com.uwetrottmann.trakt5.entities.RatedShow;
import com.uwetrottmann.trakt5.entities.Settings;
import com.uwetrottmann.trakt5.entities.SyncItems;
import com.uwetrottmann.trakt5.entities.SyncResponse;
import com.uwetrottmann.trakt5.entities.TraktList;
import com.uwetrottmann.trakt5.entities.User;
import com.uwetrottmann.trakt5.entities.UserSlug;
import com.uwetrottmann.trakt5.entities.WatchlistedEpisode;
import com.uwetrottmann.trakt5.entities.WatchlistedSeason;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.HistoryType;
import com.uwetrottmann.trakt5.enums.RatingsFilter;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Users {
    @POST("users/{username}/lists/{id}/items")
    Call<SyncResponse> addListItems(@Path("username") UserSlug userSlug, @Path("id") String str, @Body SyncItems syncItems);

    @GET("users/{username}/collection/movies")
    Call<List<BaseMovie>> collectionMovies(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/collection/shows")
    Call<List<BaseShow>> collectionShows(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @POST("users/{username}/lists")
    Call<TraktList> createList(@Path("username") UserSlug userSlug, @Body TraktList traktList);

    @DELETE("users/{username}/lists/{id}")
    Call<Void> deleteList(@Path("username") UserSlug userSlug, @Path("id") String str);

    @POST("users/{username}/lists/{id}/items/remove")
    Call<SyncResponse> deleteListItems(@Path("username") UserSlug userSlug, @Path("id") String str, @Body SyncItems syncItems);

    @POST("users/{username}/follow")
    Call<Followed> follow(@Path("username") UserSlug userSlug);

    @GET("users/{username}/followers")
    Call<List<Follower>> followers(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/following")
    Call<List<Follower>> following(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/friends")
    Call<List<Friend>> friends(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/history/{type}/{id}")
    Call<List<HistoryEntry>> history(@Path("username") UserSlug userSlug, @Path("type") HistoryType historyType, @Path("id") int i, @Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended, @Query("start_at") OffsetDateTime offsetDateTime, @Query("end_at") OffsetDateTime offsetDateTime2);

    @GET("users/{username}/history/{type}")
    Call<List<HistoryEntry>> history(@Path("username") UserSlug userSlug, @Path("type") HistoryType historyType, @Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended, @Query("start_at") OffsetDateTime offsetDateTime, @Query("end_at") OffsetDateTime offsetDateTime2);

    @GET("users/{username}/history")
    Call<List<HistoryEntry>> history(@Path("username") UserSlug userSlug, @Query("page") Integer num, @Query("limit") Integer num2, @Query(encoded = true, value = "extended") Extended extended, @Query("start_at") OffsetDateTime offsetDateTime, @Query("end_at") OffsetDateTime offsetDateTime2);

    @GET("users/{username}/lists/{id}/items")
    Call<List<ListEntry>> listItems(@Path("username") UserSlug userSlug, @Path("id") String str, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/lists")
    Call<List<TraktList>> lists(@Path("username") UserSlug userSlug);

    @GET("users/{username}")
    Call<User> profile(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/ratings/episodes{rating}")
    Call<List<RatedEpisode>> ratingsEpisodes(@Path("username") UserSlug userSlug, @Path(encoded = true, value = "rating") RatingsFilter ratingsFilter, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/ratings/movies{rating}")
    Call<List<RatedMovie>> ratingsMovies(@Path("username") UserSlug userSlug, @Path(encoded = true, value = "rating") RatingsFilter ratingsFilter, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/ratings/seasons{rating}")
    Call<List<RatedSeason>> ratingsSeasons(@Path("username") UserSlug userSlug, @Path(encoded = true, value = "rating") RatingsFilter ratingsFilter, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/ratings/shows{rating}")
    Call<List<RatedShow>> ratingsShows(@Path("username") UserSlug userSlug, @Path(encoded = true, value = "rating") RatingsFilter ratingsFilter, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/settings")
    Call<Settings> settings();

    @DELETE("users/{username}/follow")
    Call<Void> unfollow(@Path("username") UserSlug userSlug);

    @PUT("users/{username}/lists/{id}")
    Call<TraktList> updateList(@Path("username") UserSlug userSlug, @Path("id") String str, @Body TraktList traktList);

    @GET("users/{username}/watched/movies")
    Call<List<BaseMovie>> watchedMovies(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/watched/shows")
    Call<List<BaseShow>> watchedShows(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/watchlist/episodes")
    Call<List<WatchlistedEpisode>> watchlistEpisodes(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/watchlist/movies")
    Call<List<BaseMovie>> watchlistMovies(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/watchlist/seasons")
    Call<List<WatchlistedSeason>> watchlistSeasons(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);

    @GET("users/{username}/watchlist/shows")
    Call<List<BaseShow>> watchlistShows(@Path("username") UserSlug userSlug, @Query(encoded = true, value = "extended") Extended extended);
}
