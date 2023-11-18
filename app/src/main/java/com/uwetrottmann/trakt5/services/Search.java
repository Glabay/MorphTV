package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.SearchResult;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.IdType;
import com.uwetrottmann.trakt5.enums.Type;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Search {
    @GET("search/{id_type}/{id}")
    Call<List<SearchResult>> idLookup(@Path(encoded = true, value = "id_type") IdType idType, @Path(encoded = true, value = "id") String str, @Query("type") Type type, @Query("extended") Extended extended, @Query("page") Integer num, @Query("limit") Integer num2);

    @GET("search/{type}")
    Call<List<SearchResult>> textQuery(@Path("type") Type type, @Query("query") String str, @Query("years") String str2, @Query("genres") String str3, @Query("languages") String str4, @Query("countries") String str5, @Query("runtimes") String str6, @Query("ratings") String str7, @Query("extended") Extended extended, @Query("page") Integer num, @Query("limit") Integer num2);

    @GET("search/movie")
    Call<List<SearchResult>> textQueryMovie(@Query("query") String str, @Query("years") String str2, @Query("genres") String str3, @Query("languages") String str4, @Query("countries") String str5, @Query("runtimes") String str6, @Query("ratings") String str7, @Query("certifications") String str8, @Query("extended") Extended extended, @Query("page") Integer num, @Query("limit") Integer num2);

    @GET("search/show")
    Call<List<SearchResult>> textQueryShow(@Query("query") String str, @Query("years") String str2, @Query("genres") String str3, @Query("languages") String str4, @Query("countries") String str5, @Query("runtimes") String str6, @Query("ratings") String str7, @Query("certifications") String str8, @Query("networks") String str9, @Query("status") String str10, @Query("extended") Extended extended, @Query("page") Integer num, @Query("limit") Integer num2);
}
