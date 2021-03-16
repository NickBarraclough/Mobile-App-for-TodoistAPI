package com.example.spotifysongsearch.data;

import com.example.spotifysongsearch.data.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpotifyService {
    @GET("forecast")
    Call<Results> fetchResults(
//            @Query("q") String location,
//            @Query("units") String units,
            @Query("appid") String apiKey
    );
}
