package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.entities.Comment;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Comments {
    @DELETE("comments/{id}")
    Call<Void> delete(@Path("id") int i);

    @GET("comments/{id}")
    Call<Comment> get(@Path("id") int i);

    @POST("comments")
    Call<Comment> post(@Body Comment comment);

    @POST("comments/{id}/replies")
    Call<Comment> postReply(@Path("id") int i, @Body Comment comment);

    @GET("comments/{id}/replies")
    Call<List<Comment>> replies(@Path("id") int i);

    @PUT("comments/{id}")
    Call<Comment> update(@Path("id") int i, @Body Comment comment);
}
