package com.example.spotifysongsearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.spotifysongsearch.data.Results;
import com.example.spotifysongsearch.data.ResultsRepository;
import com.example.spotifysongsearch.data.LoadingStatus;

public class ResultsViewModel extends ViewModel {
    private ResultsRepository repository;
    private LiveData<Results> results;
    private LiveData<LoadingStatus> loadingStatus;

    public ResultsViewModel() {
        this.repository = new ResultsRepository();
        results = repository.getResults();
        loadingStatus = repository.getLoadingStatus();
    }

    public LiveData<Results> getResults() {
        return this.results;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadResults(String artist, String genre, String apiKey) {
        this.repository.loadResults(artist, genre, apiKey);
    }
}
