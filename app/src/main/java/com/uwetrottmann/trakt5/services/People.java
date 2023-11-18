package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.Credits;
import com.uwetrottmann.trakt5.entities.Person;
import com.uwetrottmann.trakt5.enums.Extended;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface People {
    @GET("people/{id}/movies")
    Call<Credits> movieCredits(@Path("id") String str);

    @GET("people/{id}/shows")
    Call<Credits> showCredits(@Path("id") String str);

    @GET("people/{id}")
    Call<Person> summary(@Path("id") String str, @Query("extended") Extended extended);
}
