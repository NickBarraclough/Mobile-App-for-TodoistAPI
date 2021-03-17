package com.example.todolistapp.data;

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
    private static final String BASE_URL = "https://api.todoist.com/rest/v1/";

    private MutableLiveData<Results> results;
    private MutableLiveData<LoadingStatus> loadingStatus;

    private TodoistService todoistService;
    private String currentQuery;

    public ResultsRepository() {
        this.results = new MutableLiveData<Results>();
        this.results.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ProjectData.class, new ProjectData.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.todoistService = retrofit.create(TodoistService.class);
    }

    public LiveData<Results> getResults() {
        return this.results;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadResults(Boolean show_completed) {
        Results fakeResult = new Results("ProjectName");
        this.results.setValue(fakeResult);
//        if (shouldFetchResults(searchQuery)) {
//            Log.d(TAG, "fetching new project data");
//            this.results.setValue(null);
//            this.loadingStatus.setValue(LoadingStatus.LOADING);
//
//            Call<Results> req = this.todoistService.fetchResults(searchQuery, "album", "US", "10", apiKey);
//
//
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
//            Log.d(TAG, "using cached project data for results list");
//        }
    }

    private boolean shouldFetchResults(String query) {

        Results currentResults = this.results.getValue();

        if (currentResults == null) {
            return true;
        }

        if (this.loadingStatus.getValue() == LoadingStatus.ERROR) {
            return true;
        }

        return false;
    }
}

