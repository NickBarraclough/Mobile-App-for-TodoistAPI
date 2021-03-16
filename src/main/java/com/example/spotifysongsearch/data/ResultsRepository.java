package com.example.spotifysongsearch.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultsRepository {
    private static final String TAG = ResultsRepository.class.getSimpleName();
    private static final String BASE_URL = "";

    private MutableLiveData<Results> results;
    private MutableLiveData<LoadingStatus> loadingStatus;

    private SpotifyService spotifyService;

    public ResultsRepository() {
        this.results = new MutableLiveData<Results>();
        this.results.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(SongData.class, new SongData.JsonDeserializer())
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        this.spotifyService = retrofit.create(SpotifyService.class);
    }

    public LiveData<Results> getResults() {
        return this.results;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadResults(String artist, String genre, String apiKey) {
        Results fakeResult = new Results("Africa", artist);
        this.results.setValue(fakeResult);
//        if (shouldFetchResults()) {
//            Log.d(TAG, "fetching new song data");
//            this.results.setValue(null);
//            this.loadingStatus.setValue(LoadingStatus.LOADING);
//            Call<Results> req = this.spotifyService.fetchResults(apiKey);
//            req.enqueue(new Callback<Results>() {
//                @Override
//                public void onResponse(Call<Results> call, Response<Results> response) {
//                    if (response.code() == 200) {
//                        results.setValue(response.body());
//                        loadingStatus.setValue(LoadingStatus.SUCCESS);
//                    } else {
//                        loadingStatus.setValue(LoadingStatus.ERROR);
//                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
//                        Log.d(TAG, "  -- response status code: " + response.code());
//                        Log.d(TAG, "  -- response: " + response.toString());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Results> call, Throwable t) {
//                    loadingStatus.setValue(LoadingStatus.ERROR);
//                    Log.d(TAG, "unsuccessful API request: " + call.request().url());
//                    t.printStackTrace();
//                }
//            });
//        } else {
//            Log.d(TAG, "using cached song data for results list");
//        }
    }

    private boolean shouldFetchResults() {

        Results currentResults = this.results.getValue();

        if (currentResults == null) {
            return true;
        }

        if (this.loadingStatus.getValue() == LoadingStatus.ERROR) {
            return true;
        }

        /*
         * Fetch songs if the earliest of the current song data is timestamped before "now".
         */
        if (currentResults.getSongsDataList() != null && currentResults.getSongsDataList().size() > 0) {
            SongData firstResultsData = currentResults.getSongsDataList().get(0);
            return firstResultsData.getTimeSinceLastSearch() * 1000L < System.currentTimeMillis();
        }

        /*
         * Otherwise, don't fetch the songs.
         */
        return false;
    }
}

