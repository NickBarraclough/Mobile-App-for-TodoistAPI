package com.example.todolistapp.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TodoistService {
//    @Headers({"Authorization", "Bearer " + token})

    @GET("projects")
    Call<Results> fetchResults();

    @GET("projects/{query}")
    Call<Results> fetchResults(@Path("{query}") String query);
}
